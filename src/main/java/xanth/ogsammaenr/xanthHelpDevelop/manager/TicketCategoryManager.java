package xanth.ogsammaenr.xanthHelpDevelop.manager;

import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;

import java.util.*;
import java.util.stream.Collectors;

public class TicketCategoryManager {
    private final XanthHelp plugin;

    private final Map<String, TicketCategory> categories;
    private final Map<String, TicketCategoryType> types;

    public TicketCategoryManager(XanthHelp plugin) {
        this.plugin = plugin;
        categories = new HashMap<>();
        types = new HashMap<>();
    }

    public TicketCategory getCategoryById(String categoryId) {
        return categories.get(categoryId);
    }

    public TicketCategoryType getCategoryTypeById(String typeId) {
        return types.get(typeId);
    }

    public Collection<TicketCategory> getAllCategories() {
        return categories.values();
    }

    public Collection<TicketCategoryType> getAllCategoryTypes() {
        return types.values();
    }

    public List<TicketCategory> getCategoriesByType(String typeId) {
        if (typeId == null) return Collections.emptyList();

        return categories.values().stream()
                .filter(t -> t.getType() != null && typeId.equals(t.getType().getId()))
                .collect(Collectors.toList());
    }

    public void addCategory(TicketCategory category) {
        if (category == null || category.getId() == null) return;

        categories.put(category.getId(), category);
    }

    public void addCategoryType(TicketCategoryType categoryType) {
        if (categoryType == null || categoryType.getId() == null) return;

        types.put(categoryType.getId(), categoryType);
    }

    public void clear() {
        categories.clear();
        types.clear();
    }
}
