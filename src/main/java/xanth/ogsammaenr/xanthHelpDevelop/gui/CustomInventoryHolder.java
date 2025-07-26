package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiDataKey;
import xanth.ogsammaenr.xanthHelpDevelop.model.MenuType;

import java.util.EnumMap;
import java.util.Map;

/**
 * The type Custom ınventory holder.
 */
public class CustomInventoryHolder implements InventoryHolder {
    private final MenuType menuType;
    private final Map<GuiDataKey, Object> data;

    private Inventory inventory;

    /**
     * Instantiates a new Custom ınventory holder.
     *
     * @param menuType the menu type
     */
    public CustomInventoryHolder(MenuType menuType) {
        this.menuType = menuType;

        data = new EnumMap<>(GuiDataKey.class);
    }

    /**
     * Gets menu type.
     *
     * @return the menu type
     */
    public MenuType getMenuType() {
        return menuType;
    }

    /**
     * Put data.
     *
     * @param key   the key
     * @param value the value
     */
    public void putData(GuiDataKey key, Object value) {
        data.put(key, value);
    }

    /**
     * Has data boolean.
     *
     * @param key the key
     * @return the boolean
     */
    public boolean hasData(GuiDataKey key) {
        return data.containsKey(key);
    }

    /**
     * Sets inventory.
     *
     * @param inventory the inventory
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Gets data.
     *
     * @param <T>  the type parameter
     * @param key  the key
     * @param type the type
     * @return the data
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(GuiDataKey key, Class<T> type) {
        Object value = data.get(key);
        if (value == null) return null;
        if (!type.isInstance(value)) {
            throw new ClassCastException("Expected " + type.getSimpleName() + " for key " + key + " but got " + value.getClass().getSimpleName());
        }
        return type.cast(value);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
