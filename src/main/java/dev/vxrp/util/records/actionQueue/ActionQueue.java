package dev.vxrp.util.records.actionQueue;

public record ActionQueue(String id, String command, String time_added, boolean processed) {
}
