package xanth.ogsammaenr.xanthHelpDevelop.model;

import org.bukkit.Material;

import java.util.List;

public class TicketCategory {
    private String id;
    private String displayName;
    private List<String> lore;
    private TicketCategoryType type;
    private Material icon;


    public TicketCategory(String id, String displayName, List<String> lore, TicketCategoryType type, Material icon) {
        this.id = id;
        this.displayName = displayName;
        this.lore = lore;
        this.type = type;
        this.icon = icon;
    }

    /*      GETTERS     */

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getIcon() {
        return icon;
    }

    public List<String> getLore() {
        return lore;
    }

    public TicketCategoryType getType() {
        return type;
    }
}
