package xanth.ogsammaenr.xanthHelp.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GuiItem {
    private final String id;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private final int slot;

    public GuiItem(String id, Material material, String name, List<String> lore, int slot) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.slot = slot;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name.replace("&", "§"));
            meta.setLore(lore.stream().map(s -> s.replace("&", "§")).toList());
            item.setItemMeta(meta);
        }
        return item;
    }
}
