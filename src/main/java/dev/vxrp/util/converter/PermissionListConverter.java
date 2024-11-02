package dev.vxrp.util.converter;

import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionListConverter {
    List<String> permissions;
    public PermissionListConverter(List<String> permissions) {
        this.permissions = permissions;
    }
    public List<Permission> convert() {
        List<Permission> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            permissionList.add(Permission.valueOf(permission));
        }
        return permissionList;
    }
}
