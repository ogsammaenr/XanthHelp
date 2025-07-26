package xanth.ogsammaenr.xanthHelpDevelop.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelpDevelop.util.ItemBuilder;

import java.util.List;

/**
 * The type Ticket category.
 */
public class TicketCategory {
    private String id;
    private String displayName;
    private List<String> lore;
    private TicketCategoryType type;
    private Material icon;


    /**
     * Instantiates a new Ticket category.
     *
     * @param id          the id
     * @param displayName the display name
     * @param lore        the lore
     * @param type        the type
     * @param icon        the icon
     */
    public TicketCategory(String id, String displayName, List<String> lore, TicketCategoryType type, Material icon) {
        this.id = id;
        this.displayName = displayName;
        this.lore = lore;
        this.type = type;
        this.icon = icon;
    }

    /*      GETTERS     */

    /**
     * Gets ıd.
     *
     * @return the ıd
     */
    public String getId() {
        return id;
    }

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
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
     * Gets lore.
     *
     * @return the lore
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public TicketCategoryType getType() {
        return type;
    }

    public ItemStack toItemStack() {
        return new ItemBuilder(icon)
                .setName(displayName)
                .setLore(lore)
                .setNBT("ticketCategory", id)
                .build();
    }

    @Override
    public String toString() {
        return "TicketCategory{" +
               "id='" + id + '\'' +
               ", displayName='" + displayName + '\'' +
               ", lore=" + lore +
               ", type=" + type +
               ", icon=" + icon +
               '}';
    }
}
