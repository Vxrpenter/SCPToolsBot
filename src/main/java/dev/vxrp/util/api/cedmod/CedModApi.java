package dev.vxrp.util.api.cedmod;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import dev.vxrp.util.Enums.DCColor_DEPRECATED;
import dev.vxrp.util.colors.ColorTool;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedModApi {
    private static final Logger logger = LoggerFactory.getLogger(CedModApi.class);
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private final String instanceURL;
    private final String apiKey;

    public CedModApi(String instanceURL, String apiKey) {
        this.instanceURL = instanceURL;
        this.apiKey = apiKey;
    }

    public void executeUnban(String banID, String reason) throws IOException {
        if (banID == null) return;
        String json = "{\"logReason\":\""+reason+"\"}";

        RequestBody requestBody =  RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(instanceURL+"/Api/Ban/"+banID)
                .header("accept", "*/*")
                .header("Authorization", "Bearer "+apiKey)
                .put(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 204) {
            logger.info("Unbanned ID {}", ColorTool.apply(DCColor_DEPRECATED.RED, banID));
        } else {
            logger.error("Unbanning of {} ID failed, does it exist? ({})", ColorTool.apply(DCColor_DEPRECATED.RED, banID), response.code());
        }
    }
    public String getBanId(String banList, String userID) throws IOException {

        Request request = new Request.Builder()
                .url(instanceURL+"/Api/Ban/Query?q="+userID+"%40steam&banList="+banList+"&max=10&page=0&idOnly=true")
                .header("Authorization", "Bearer "+apiKey)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        try {
            assert response.body() != null;
            JsonArray array = JsonParser.parseString(response.body().string()).getAsJsonObject().get("players").getAsJsonArray();
            int banID = array.get(0).getAsJsonObject().get("id").getAsInt();

            logger.info("Succeeded HTTP request to cedmod servers, banID data of {} received ({})", ColorTool.apply(DCColor_DEPRECATED.RED, userID + "@steam"), ColorTool.apply(DCColor_DEPRECATED.RED, String.valueOf(banID)));

            return String.valueOf(banID);
        } catch (IndexOutOfBoundsException e) {
            logger.error("Couldn't commence HTTP request to get banID, is the user banned?\n {}", e.getLocalizedMessage());
        }
        return null;
    }

    public double getActivity(String userID, String days) throws IOException {
        Request request = new Request.Builder()
                .url(instanceURL+"/Api/Player/Query?q="+userID+"@steam&max=10&page=0&staffOnly=false&create=false&sortLabel=id_field&activityMin="+days+"&basicStats=true&moderationData=false")
                .header("Authorization", "Bearer "+apiKey)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        try {
            assert response.body() != null;
            JsonArray array = JsonParser.parseString(response.body().string()).getAsJsonObject().getAsJsonArray("players");

            return array.get(0).getAsJsonObject().get("activity").getAsDouble();
        } catch (IndexOutOfBoundsException e) {
            logger.error("Couldn't commence HTTP request to get activity, is the user not present?\n {}", e.getLocalizedMessage());
        }
        return 0;
    }
}
