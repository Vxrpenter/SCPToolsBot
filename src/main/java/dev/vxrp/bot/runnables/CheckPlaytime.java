package dev.vxrp.bot.runnables;

import com.google.gson.JsonObject;
import dev.vxrp.bot.ScpTools;
import dev.vxrp.bot.database.queue.QueueManager;
import dev.vxrp.bot.database.sqlite.RegularsTableManager;
import dev.vxrp.util.parser.ActionQueueParser;
import dev.vxrp.util.records.actionQueue.ActionQueue;
import dev.vxrp.util.records.actionQueue.RegularActionQueue;
import dev.vxrp.util.records.regular.RegularMember;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CheckPlaytime {
    private final static Logger logger = LoggerFactory.getLogger(CheckPlaytime.class);
    private final static RegularsTableManager regularsTableManager = ScpTools.getSqliteManager().getRegularsTableManager();
    private final static QueueManager queueManager = ScpTools.getSqliteManager().getQueueManager();

    public static Runnable runPlaytimeCheck(JDA api) {
        return () -> {
            logger.info("Checking playtime of user...");
            try {
                writeToQueue();

                int count = 0;
                for (ActionQueue element : queueManager.getEveryActionQueue()) {
                    if (count >= 25) break;
                    JsonObject object = new JsonObject().getAsJsonObject(element.command());
                    RegularActionQueue actionQueue = new ActionQueueParser().parseJsonObject(object);
                    double activity = ScpTools.getCedModApi().getActivity(actionQueue.userId(), String.valueOf(actionQueue.timeframe()));
                    System.out.println(activity);
                    regularsTableManager.updateTime(element.id(), activity);
                    regularsTableManager.updateTimeLastChecked(element.id(), LocalDate.now().toString());
                    queueManager.updateProcessed(element.id(), true);
                    count++;
                    TimeUnit.SECONDS.sleep(2);
                }

                //Putting the role stuff here later
            } catch (SQLException | InterruptedException | IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        };
    }

    private static void writeToQueue() throws SQLException, InterruptedException {
        List<RegularMember> members;
        members = regularsTableManager.getEveryRegular();

        for (RegularMember member : members) {
            if (!queueManager.getProcessed(member.id()) && !queueManager.exists(member.id())) {
                ActionQueueParser actionQueueParser = new ActionQueueParser();

                LocalDate now = LocalDate.now();
                LocalDate time = null;
                long timeframe = 0;
                if (member.time_last_checked() == null) {
                    timeframe = 365;
                } else {
                    String[] last_checked = member.time_last_checked().split("\\.");
                    time = LocalDate.of(Integer.parseInt(last_checked[2]), Integer.parseInt(last_checked[1]), Integer.parseInt(last_checked[0]));
                    timeframe = ChronoUnit.DAYS.between(time, now);
                }

                assert time != null;
                if (time.toString().equals(now.toString()) || !queueManager.exists(member.id())) {
                    queueManager.addAction(member.id(), actionQueueParser.parseRegularActionQueue(new RegularActionQueue(
                                    member.user_name(), (int) timeframe)).toString(),
                            now.toString());
                }
            } else {
                queueManager.deleteAction(member.id());
            }
        }
    }
}
