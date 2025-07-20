package xanth.ogsammaenr.xanthHelpDevelop.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;

public class GuiUtils {

    protected void placeFillers(GuiConfig.FillerSettings fillerSettings, Inventory inventory, int rows) {
        ItemStack topRow = fillerSettings.getTopRow();
        ItemStack bottomRow = fillerSettings.getBottomRow();
        ItemStack general = fillerSettings.getGeneral();

        for (int i = 0; i < rows * 9; i++) {
            if (topRow != null && i < 9) {
                inventory.setItem(i, topRow);
            } else if (bottomRow != null && i >= (rows - 1) * 9) {
                inventory.setItem(i, bottomRow);
            } else if (general != null) {
                inventory.setItem(i, general);
            }
        }

    }
}
