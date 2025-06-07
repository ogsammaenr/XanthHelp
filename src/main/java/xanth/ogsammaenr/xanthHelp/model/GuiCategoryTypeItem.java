package xanth.ogsammaenr.xanthHelp.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelp.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiCategoryTypeItem extends GuiItem {
    public GuiCategoryTypeItem(String id, Material material, String name, List<String> lore, int slot) {
        super(id, material, name, lore, slot);
    }

    @Override
    public ItemStack toItemStack() {
        Map<String, String> nbtData = new HashMap<>();
        nbtData.put("category_type", getId());

        return Utils.createItem(getMaterial(), getName(), getLore(), nbtData);
    }
}
