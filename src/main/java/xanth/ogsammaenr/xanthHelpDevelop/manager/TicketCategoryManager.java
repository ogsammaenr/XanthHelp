package xanth.ogsammaenr.xanthHelpDevelop.manager;

import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Ticket category manager.
 */
public class TicketCategoryManager {
    private final XanthHelp plugin;

    private final Map<String, TicketCategory> categories;
    private final Map<String, TicketCategoryType> types;

    /**
     * Instantiates a new Ticket category manager.
     *
     * @param plugin the plugin
     */
    public TicketCategoryManager(XanthHelp plugin) {
        this.plugin = plugin;
        categories = new HashMap<>();
        types = new HashMap<>();
    }

    /**
     * Gets category by ıd.
     *
     * @param categoryId the category ıd
     * @return the category by ıd
     */
    public TicketCategory getCategoryById(String categoryId) {
        return categories.get(categoryId);
    }

    /**
     * Gets category type by ıd.
     *
     * @param typeId the type ıd
     * @return the category type by ıd
     */
    public TicketCategoryType getCategoryTypeById(String typeId) {
        return types.get(typeId);
    }

    /**
     * Gets all categories.
     *
     * @return the all categories
     */
    public Collection<TicketCategory> getAllCategories() {
        return categories.values();
    }

    /**
     * Gets all category types.
     *
     * @return the all category types
     */
    public Collection<TicketCategoryType> getAllCategoryTypes() {
        return types.values();
    }

    /**
     * Gets categories by type.
     *
     * @param typeId the type ıd
     * @return the categories by type
     */
    public List<TicketCategory> getCategoriesByType(String typeId) {
        if (typeId == null) return Collections.emptyList();

        return categories.values().stream()
                .filter(t -> t.getType() != null && typeId.equals(t.getType().getId()))
                .collect(Collectors.toList());
    }

    /**
     * Add category to Category Map.
     *
     * @param category the category
     */
    public void addCategory(TicketCategory category) {
        if (category == null || category.getId() == null) return;

        categories.put(category.getId(), category);
    }

    /**
     * Add category type to Category Types Map.
     *
     * @param categoryType the category type
     */
    public void addCategoryType(TicketCategoryType categoryType) {
        if (categoryType == null || categoryType.getId() == null) return;

        types.put(categoryType.getId(), categoryType);
    }

    /**
     * Clear Maps.
     */
    public void clear() {
        categories.clear();
        types.clear();
    }
}
