package dev.vxrp.util.builder;

import net.dv8tion.jda.api.EmbedBuilder;

public class StatsBuilder {
    public static EmbedBuilder buildStatus(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Ticket Status: [2;32m[1;32mOpen[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Handler: [2;31m[1;31mNone[0m[2;31m[0m | Creator: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }
    public static EmbedBuilder buildDismissed(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Ticket Answer: [2;31m[1;31mDismissed[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Creator: [2;31m[1;31m[1;33mYou[0m[2;31m[0m | Handler: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }
    public static EmbedBuilder buildAccepted(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Ticket Answer: [2;32m[1;32mAccepted[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Creator: [2;31m[1;31m[1;33mYou[0m[2;31m[0m | Handler: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }
    public static EmbedBuilder buildEnded(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Notice of Departure: [2;31m[1;31mEnded[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Creator: [2;31m[1;31m[1;33mYou[0m[2;31m[0m | Handler: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }
    public static EmbedBuilder buildRevoked(String username) {
        return new EmbedBuilder()
                .setDescription("""
                                    ```ansi
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Notice of Departure: [2;31m[1;31mRevoked[0m[2;32m[0m ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎\s
                                     ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ Creator: [2;31m[1;31m[1;33mYou[0m[2;31m[0m | Handler: [2;31m[1;31m[1;33m""" +username+"""
                                     [0m[1;31m[0m[2;31m[0m
                                    ```
                                    """);
    }
}
