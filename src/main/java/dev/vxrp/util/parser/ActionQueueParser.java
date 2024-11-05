package dev.vxrp.util.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.vxrp.util.records.actionQueue.RegularActionQueue;

public class ActionQueueParser {
    String regularId = "HTTPS-REQUEST-STORED-REGULARS";

    public RegularActionQueue parseJsonObject(JsonObject object) {
        String id = object.get("id").getAsString();

        if (!id.equals(regularId)) return null;
        JsonArray arguments = object.get("arguments").getAsJsonArray();

        String userId = object.get("userId").getAsString();
        int timeframe = object.get("timeframe").getAsInt();

        return new RegularActionQueue(userId, timeframe);
    }

    public JsonObject parseRegularActionQueue(RegularActionQueue queue) {
        String userId = queue.userId();
        int timeframe = queue.timeframe();

        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("timeframe", timeframe);
        array.add(object);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", regularId);
        jsonObject.add("arguments", array);
        return jsonObject;
    }
}