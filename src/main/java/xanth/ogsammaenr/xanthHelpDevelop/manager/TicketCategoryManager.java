package xanth.ogsammaenr.xanthHelpDevelop.manager;

import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategory;
import xanth.ogsammaenr.xanthHelpDevelop.model.TicketCategoryType;

import java.util.HashMap;
import java.util.Map;

public class TicketCategoryManager {
    private final XanthHelp plugin;

    private final Map<String, TicketCategory> categories;
    private final Map<String, TicketCategoryType> types;

    public TicketCategoryManager(XanthHelp plugin) {
        this.plugin = plugin;
        categories = new HashMap<>();
        types = new HashMap<>();
    }

    public TicketCategory getCategoryById(String categoryid) {
        return categories.get(categoryid);
    }

    public TicketCategoryType getCategoryTypeById(String categoryid) {
        return types.get(categoryid);
    }

    public void addCategory(TicketCategory category) {
        categories.put(category.getId(), category);
    }

    public void addCategoryType(TicketCategoryType categoryType) {
        types.put(categoryType.getId(), categoryType);
    }

    public void clear() {
        categories.clear();
        types.clear();
    }
}
