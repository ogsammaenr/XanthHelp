package xanth.ogsammaenr.xanthHelpDevelop.listener;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.gui.CustomInventoryHolder;
import xanth.ogsammaenr.xanthHelpDevelop.gui.TicketCategoriesMenu;
import xanth.ogsammaenr.xanthHelpDevelop.gui.TicketDetailsMenu;
import xanth.ogsammaenr.xanthHelpDevelop.gui.TicketsMenu;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketCategoryManager;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketInputManager;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketManager;
import xanth.ogsammaenr.xanthHelpDevelop.model.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class InventoryClickListener implements Listener {
    private XanthHelp plugin;
    private TicketManager ticketManager;
    private TicketCategoryManager catManager;
    private TicketInputManager inputManager;

    public InventoryClickListener(XanthHelp plugin) {
        this.plugin = plugin;
        this.ticketManager = plugin.getTicketManager();
        this.catManager = plugin.getTicketCategoryManager();
        this.inputManager = plugin.getTicketInputManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem().getType().isAir() || event.getCurrentItem() == null) return;
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof CustomInventoryHolder)) {
            return;
        }
        if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() != inventory.getHolder()) {
            return;
        }
        event.setCancelled(true);

        CustomInventoryHolder holder = (CustomInventoryHolder) inventory.getHolder();
        MenuType menuType = holder.getMenuType();

        switch (menuType) {
            case MAIN_MENU -> handleMainMenu(event);
            case TICKETS_MENU -> handleTicketsMenu(event);
            case CONFIRMATION_MENU -> handleConfirmationMenu(event, holder);
            case TICKET_DETAILS_MENU -> handleTicketDetailsMenu(event);
            case TICKET_CATEGORIES_MENU -> handleTicketCategoriesMenu(event);
        }

    }

    private void handleMainMenu(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        String typeId = getStringTag(item, "ticketCategoryType");

        Player player = (Player) event.getWhoClicked();
        new TicketCategoriesMenu().open(player, catManager.getCategoryTypeById(typeId));
    }

    private void handleTicketsMenu(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        String button = getStringTag(item, "button");
        String ticketId = getStringTag(item, "ticketId");
        if (ticketId != null) {
            Optional<Ticket> ticketOpt = ticketManager.getTicket(ticketId);
            if (ticketOpt.isPresent()) {
                new TicketDetailsMenu().open((Player) event.getWhoClicked(), ticketOpt.get());
                return;
            }
        }
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getClickedInventory();
        CustomInventoryHolder holder = (CustomInventoryHolder) inventory.getHolder();

        TicketStatus status = holder.getData(GuiDataKey.STATUS, TicketStatus.class);
        UUID filterId = holder.getData(GuiDataKey.FILTER_UUID, UUID.class);
        int page = holder.getData(GuiDataKey.PAGE_NUMBER, Integer.class);

        if (button != null) {
            switch (button) {
                case "previous" -> new TicketsMenu().open(player, page - 1, filterId, status);
                case "next" -> new TicketsMenu().open(player, page + 1, filterId, status);
                case "OPEN" -> new TicketsMenu().open(player, 0, filterId, TicketStatus.OPEN);
                case "IN_PROGRESS" -> new TicketsMenu().open(player, 0, filterId, TicketStatus.IN_PROGRESS);
                case "CANCELED" -> new TicketsMenu().open(player, 0, filterId, TicketStatus.CANCELED);
                case "RESOLVED" -> new TicketsMenu().open(player, 0, filterId, TicketStatus.RESOLVED);
                case "ALL" -> new TicketsMenu().open(player, 0, filterId, null);
                case "player-filter" -> handleFilter();
            }
        }
    }

    private void handleConfirmationMenu(InventoryClickEvent event, CustomInventoryHolder holder) {
        String action = holder.getData(GuiDataKey.CONFIRMATION_ACTION, String.class);
        if (action == null) return;
        Inventory inventory = event.getClickedInventory();

        Player player = (Player) event.getWhoClicked();

        switch (action) {
            case "Create Ticket" -> {
                String description = holder.getData(GuiDataKey.TICKET_DESCRIPTION, String.class);
                TicketCategory category = holder.getData(GuiDataKey.CATEGORY, TicketCategory.class);

                ItemStack item = event.getCurrentItem();
                String key = getStringTag(item, "button");

                if (key == null) return;
                if (key.equals("accept")) {
                    Ticket ticket = new Ticket.Builder
                            ("TCK - " + ticketManager.getLastTicketNumber() + 1
                                    , player.getUniqueId(), TicketStatus.OPEN, category
                                    , description, LocalDateTime.now()).build();
                    ticketManager.createTicket(ticket);
                    inventory.close();

                    player.sendMessage(ChatColor.GREEN + "Ticket created! : " + ticket.getTicketId());
                } else if (key.equals("deny")) {
                    inventory.close();

                    player.sendMessage(ChatColor.RED + "Ticket denied!");
                }

                
            }
        }
    }

    private void handleTicketDetailsMenu(InventoryClickEvent event) {

    }

    private void handleTicketCategoriesMenu(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();

        Player player = (Player) event.getWhoClicked();

        if (inventory == null || item == null || item.getType().isAir()) {
            return;
        }

        String categoryId = getStringTag(item, "ticketCategory");
        if (categoryId == null) {
            return;
        }

        TicketCategory category = catManager.getCategoryById(categoryId);
        if (category == null) {
            player.sendMessage(ChatColor.RED + "Bu kategori geçersiz.");
            return;
        }

        inputManager.waitForInput(player.getUniqueId(), category);
        inventory.close();

        player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
        player.sendMessage(ChatColor.GRAY + "Lütfen Ticket İçin Bir Açıklama Giriniz");
        player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
    }

    private static String getStringTag(ItemStack item, String key) {
        XanthHelp plugin = XanthHelp.getInstance();
        if (item == null || item.getItemMeta() == null) return null;

        var meta = item.getItemMeta();
        var container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        if (container.has(namespacedKey, PersistentDataType.STRING)) {
            return container.get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    private void handleFilter() {

    }

}
