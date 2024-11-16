package dev.vxrp.bot.runnables;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.config.managers.regulars.RegularsManager;
import dev.vxrp.bot.database.queue.QueueManager;
import dev.vxrp.bot.database.sqlite.RegularsTableManager;
import dev.vxrp.util.Enums.DCColor;
import dev.vxrp.util.colors.ColorTool;
import dev.vxrp.util.parser.ActionQueueParser;
import dev.vxrp.util.records.actionQueue.ActionQueue;
import dev.vxrp.util.records.actionQueue.RegularActionQueue;
import dev.vxrp.util.records.regular.Regular;
import dev.vxrp.util.records.regular.RegularConfig;
import dev.vxrp.util.records.regular.RegularMember;
import dev.vxrp.util.records.regular.RoleHierarchy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CheckPlaytime {
    private final static Logger logger = LoggerFactory.getLogger(CheckPlaytime.class);
    private final static RegularsTableManager regularsTableManager = ScpTools.getSqliteManager().getRegularsTableManager();
    private final static QueueManager queueManager = ScpTools.getSqliteManager().getQueueManager();

    public static Runnable runPlaytimeCheck(JDA api) {
        return () -> {
            logger.info("Checking playtime of user...");
            try {
                // Write all actions to queue
                writeToQueue();

                // Process actions in queue and break loop at 25
                int count = 0;
                for (ActionQueue element : queueManager.getEveryActionQueue()) {
                    if (count >= 25) break;
                    JsonObject object = JsonParser.parseString(element.command()).getAsJsonObject();
                    RegularActionQueue actionQueue = new ActionQueueParser().parseJsonObject(object);
                    double dataTime = regularsTableManager.getRegularMember(element.id()).time();
                    double activity = ScpTools.getCedModApi().getActivity(actionQueue.userId(), String.valueOf(actionQueue.timeframe()));
                    regularsTableManager.updateTime(element.id(),dataTime+activity);
                    regularsTableManager.updateTimeLastChecked(element.id(), LocalDate.now().toString().split("-")[2]+"."+LocalDate.now().toString().split("-")[1]+"."+LocalDate.now().toString().split("-")[0]);

                    // Apply roles on updated playtime
                    RegularsManager regularsManager = ScpTools.getRegularsManager();

                    String group_role = regularsTableManager.getRegularMember(element.id()).group_role();
                    List<RegularConfig> config = null;
                    List<RoleHierarchy> roles = new ArrayList<>();
                    for (Regular regular : regularsManager.getRegulars()) {
                        if (group_role != null) {
                            if (regular.use_custom_role() && regular.id().equals(group_role)) {
                                config = regularsManager.getSingleConfig(regular);
                            }
                        } else {
                            config = regularsManager.getSingleConfig(regularsManager.getRegulars().getFirst());
                        }
                    }

                    assert config != null;
                    for (int i = 0; i < config.size(); i++) {
                        roles.add(new RoleHierarchy(i, ScpTools.getGuild().getRoleById(config.get(i).id())));
                    }

                    for (int i = 0; i < config.size(); i++) {
                        RegularConfig configElement = config.get(i);
                        int time = configElement.playtime_requirements();

                        int timeOfPlayer = (int) Math.round(regularsTableManager.getRegularMember(element.id()).time()/3600);

                        if (timeOfPlayer >= time) {
                            System.out.println(timeOfPlayer+" >"+time);
                            Role role = ScpTools.getGuild().getRoleById(configElement.id());

                            int finalI = i;
                            ScpTools.getGuild().retrieveMemberById(element.id()).queue(member -> {
                                try {
                                    if (!member.getRoles().contains(role)) {
                                        assert role != null;
                                        if (Objects.equals(regularsTableManager.getRegularMember(element.id()).role(), role.getId())) return;
                                        regularsTableManager.updateRole(element.id(), role.getId());
                                        ScpTools.getGuild().addRoleToMember(member, role).queue(_ -> {
                                            roles.forEach(x -> {removeIfContainsAndNotCurrentIteratorRole(member, x, role, finalI);});
                                        });

                                        logger.info("Applied role: {} to user: {} after reaching the timeframe of: {}h",
                                                ColorTool.apply(DCColor.RED, role.getName()),
                                                ColorTool.apply(DCColor.RED, member.getUser().getGlobalName()),
                                                ColorTool.apply(DCColor.GREEN, String.valueOf(time)));
                                    } else {
                                        if (regularsTableManager.getRegularMember(element.id()).role() == null) {
                                            assert role != null;
                                            regularsTableManager.updateRole(element.id(), role.getId());
                                        }
                                    }
                                } catch (SQLException e) {
                                    assert role != null;
                                    logger.error("Could not correctly add roles to the regular database id: {}, role: {}",
                                            ColorTool.apply(DCColor.RED, element.id()),
                                            ColorTool.apply(DCColor.RED, role.getName()));
                                }
                            });
                        }
                    }
                    queueManager.updateProcessed(element.id(), true);
                    count++;
                    TimeUnit.SECONDS.sleep(2);
                }

            } catch (SQLException | InterruptedException | IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        };
    }

    private static void removeIfContainsAndNotCurrentIteratorRole(Member member, RoleHierarchy roleHierarchy, Role currentRole, int currentIndex) {
        logger.trace("---------------------------------------------------------------------------------------");
        logger.trace("Fired for: {}", ColorTool.apply(DCColor.GREEN, roleHierarchy.role().getName()));
        logger.trace("Hierarchy Index: {}", ColorTool.apply(DCColor.GOLD, String.valueOf(roleHierarchy.hierarchyPlace())));
        logger.trace("Current Index: {}", ColorTool.apply(DCColor.GOLD, String.valueOf(currentIndex)));
        logger.trace("Current Role: {}", ColorTool.apply(DCColor.GREEN, currentRole.getName()));
        logger.trace("Is not currentRole: {}", ColorTool.apply(DCColor.GREEN, String.valueOf(roleHierarchy.role() != currentRole)));
        logger.trace("Current index is higher than place: {}", ColorTool.apply(DCColor.GREEN, String.valueOf(currentIndex > roleHierarchy.hierarchyPlace())));
        logger.trace("---------------------------------------------------------------------------------------");

        if (roleHierarchy.role() != currentRole && currentIndex > roleHierarchy.hierarchyPlace()) {
            ScpTools.getGuild().removeRoleFromMember(member, roleHierarchy.role()).queue();
            logger.info("Removed role : {} from user: {} because of higher existing role",
                    roleHierarchy.role().getName(), member.getUser().getGlobalName());
        }
    }

    private static void writeToQueue() throws SQLException, InterruptedException {
        List<RegularMember> members;
        members = regularsTableManager.getEveryRegularMember();

        for (RegularMember member : members) {
            System.out.println(queueManager.getProcessed(member.id()));
            if (!queueManager.exists(member.id()) && !regularsTableManager.getRegularMember(member.id()).deactivated()) {
                write(member);
            } else if (queueManager.getProcessed(member.id())) {
                queueManager.deleteAction(member.id());
            }
        }
    }

    private static void write(RegularMember member) throws SQLException, InterruptedException {
        ActionQueueParser actionQueueParser = new ActionQueueParser();

        LocalDate now = LocalDate.now();
        LocalDate time;
        long timeframe;
        if (member.time_last_checked() == null) {
            time = now;
            timeframe = 365;
        } else {
            String[] last_checked = member.time_last_checked().split("\\.");
            time = LocalDate.of(Integer.parseInt(last_checked[2]), Integer.parseInt(last_checked[1]), Integer.parseInt(last_checked[0]));
            timeframe = ChronoUnit.DAYS.between(time, now);
        }

        if (!time.equals(now) || !queueManager.exists(member.id())) {
            queueManager.addAction(member.id(), actionQueueParser.parseRegularActionQueue(new RegularActionQueue(
                            member.user_name(), (int) timeframe)).toString(),
                    now.toString());
        }
    }
}