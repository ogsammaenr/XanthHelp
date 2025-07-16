package xanth.ogsammaenr.xanthHelpDevelop.model;

import org.bukkit.Material;

import java.util.List;

public class TicketCategoryType {
    private String id;
    private String displayName;
    private List<String> lore;
    private Material icon;
    private String permission;

    public TicketCategoryType(String id, String displayName, String permission, List<String> lore, Material icon) {
        this.id = id;
        this.displayName = displayName;
        this.permission = permission;
        this.lore = lore;
        this.icon = icon;
    }

    /*      GETTERS     */

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getIcon() {
        return icon;
    }

    public String getPermission() {
        return permission;
    }
}
