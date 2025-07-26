package xanth.ogsammaenr.xanthHelpDevelop.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The type Item builder.
 */
public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    /**
     * Instantiates a new Item builder.
     *
     * @param material the material
     */
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    /**
     * Instantiates a new Item builder.
     *
     * @param material the material
     * @param amount   the amount
     */
    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    /**
     * Instantiates a new Item builder.
     *
     * @param baseItem the base ıtem
     */
    public ItemBuilder(ItemStack baseItem) {
        this.item = baseItem;
        this.meta = item.getItemMeta();
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    /**
     * Sets lore.
     *
     * @param lines the lines
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setLore(String... lines) {
        meta.setLore(Arrays.asList(lines));
        return this;
    }

    /**
     * Sets lore.
     *
     * @param lines the lines
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setLore(List<String> lines) {
        meta.setLore(lines);
        return this;
    }

    /**
     * Add lore line ıtem builder.
     *
     * @param line the line
     * @return {@link ItemBuilder}
     */
    public ItemBuilder addLoreLine(String line) {
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add(line);
        meta.setLore(lore);
        return this;
    }

    /**
     * Sets amount.
     *
     * @param amount the amount
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Add enchant ıtem builder.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return {@link ItemBuilder}
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Add unsafe enchant ıtem builder.
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return {@link ItemBuilder}
     */
    public ItemBuilder addUnsafeEnchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Add ıtem flags ıtem builder.
     *
     * @param flags the flags
     * @return {@link ItemBuilder}
     */
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    /**
     * Sets unbreakable.
     *
     * @param unbreakable the unbreakable
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Sets custom model data.
     *
     * @param modelData the model data
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setCustomModelData(int modelData) {
        meta.setCustomModelData(modelData);
        return this;
    }

    /**
     * Sets nbt.
     *
     * @param key   the key
     * @param value the value
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setNBT(String key, String value) {
        XanthHelp plugin = XanthHelp.getInstance();

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
        return this;
    }


    /**
     * Sets player head.
     *
     * @param playerUUID the player uuıd
     * @return {@link ItemBuilder}
     */
    public ItemBuilder setPlayerHead(UUID playerUUID) {
        item.setType(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        if (skullMeta == null) return this;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
        skullMeta.setOwningPlayer(offlinePlayer);

        item.setItemMeta(skullMeta);
        return this;
    }

    /**
     * Add glow ıf condition is true
     *
     * @param condition the condition
     * @return {@link ItemBuilder}
     */
    public ItemBuilder addGlowIf(boolean condition) {
        if (condition) {
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    /**
     * Build Builder to item stack.
     *
     * @return the ıtem stack
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
