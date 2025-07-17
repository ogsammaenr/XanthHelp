package xanth.ogsammaenr.xanthHelpDevelop.model;

import org.bukkit.Material;

import java.util.List;

/**
 * The type Ticket category type.
 */
public class TicketCategoryType {
    private String id;
    private String displayName;
    private List<String> lore;
    private Material icon;
    private String permission;

    /**
     * Instantiates a new Ticket category type.
     *
     * @param id          the id
     * @param displayName the display name
     * @param permission  the permission
     * @param lore        the lore
     * @param icon        the icon
     */
    public TicketCategoryType(String id, String displayName, String permission, List<String> lore, Material icon) {
        this.id = id;
        this.displayName = displayName;
        this.permission = permission;
        this.lore = lore;
        this.icon = icon;
    }

    /*      GETTERS     */

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets ıd.
     *
     * @return the ıd
     */
    public String getId() {
        return id;
    }

    /**
     * Gets lore.
     *
     * @return the lore
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * Gets ıcon.
     *
     * @return the ıcon
     */
    public Material getIcon() {
        return icon;
    }

    /**
     * Gets permission.
     *
     * @return the permission
     */
    public String getPermission() {
        return permission;
    }
}
