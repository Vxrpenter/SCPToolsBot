package dev.vxrp.util.records.regular;

import com.google.gson.JsonObject;

public record Regular(String name, String description, boolean use_custom_role, String id, JsonObject config) {
}