package xanth.ogsammaenr.xanthHelp.model;

import org.bukkit.Material;
import xanth.ogsammaenr.xanthHelp.XanthHelp;

import java.util.List;

public class GuiCategoryItem extends GuiItem {
    private final Category category;

    public GuiCategoryItem(Category category, Material material, String name, List<String> lore, int slot) {
        super(category.getId(), material, name, lore, slot);
        this.category = category;
    }

    public GuiCategoryItem(String id, Material material, String name, List<String> lore, int slot) {
        super(id, material, name, lore, slot);
        this.category = XanthHelp.getInstance().getCategoryManager().getCategory(id);
    }

    public Category getCategory() {
        return category;
    }
}
