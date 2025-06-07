package xanth.ogsammaenr.xanthHelp.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.XanthHelp;
import xanth.ogsammaenr.xanthHelp.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public ItemStack toItemStack() {
        Map<String, String> nbtData = new HashMap<>();
        nbtData.put("category_id", category.getId());

        return Utils.createItem(getMaterial(), getName(), getLore(), nbtData);
    }
}
