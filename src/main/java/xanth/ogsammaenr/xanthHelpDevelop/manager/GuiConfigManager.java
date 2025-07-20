package xanth.ogsammaenr.xanthHelpDevelop.manager;

import xanth.ogsammaenr.xanthHelpDevelop.XanthHelp;
import xanth.ogsammaenr.xanthHelpDevelop.model.GuiConfig;

public class GuiConfigManager {
    private final XanthHelp plugin;

    private GuiConfig.ConfirmationMenuConfig confirmationMenuConfig;
    private GuiConfig.MainMenuConfig mainMenuConfig;
    private GuiConfig.TicketCategoriesMenuConfig ticketCategoriesMenuConfig;
    private GuiConfig.TicketDetailMenuConfig ticketDetailMenuConfig;
    private GuiConfig.TicketsMenuConfig ticketsMenuConfig;

    public GuiConfigManager(XanthHelp plugin) {
        this.plugin = plugin;
    }

    /*
     * +---------------------+
     * |       GETTERS       |
     * +---------------------+
     */
    public GuiConfig.ConfirmationMenuConfig getConfirmationMenuConfig() {
        return confirmationMenuConfig;
    }

    public GuiConfig.MainMenuConfig getMainMenuConfig() {
        return mainMenuConfig;
    }

    public GuiConfig.TicketCategoriesMenuConfig getTicketCategoriesMenuConfig() {
        return ticketCategoriesMenuConfig;
    }

    public GuiConfig.TicketDetailMenuConfig getTicketDetailMenuConfig() {
        return ticketDetailMenuConfig;
    }

    public GuiConfig.TicketsMenuConfig getTicketsMenuConfig() {
        return ticketsMenuConfig;
    }

    /*
     * +---------------------+
     * |       SETTERS       |
     * +---------------------+
     */

    public void setConfirmationMenuConfig(GuiConfig.ConfirmationMenuConfig confirmationMenuConfig) {
        this.confirmationMenuConfig = confirmationMenuConfig;
    }

    public void setMainMenuConfig(GuiConfig.MainMenuConfig mainMenuConfig) {
        this.mainMenuConfig = mainMenuConfig;
    }

    public void setTicketCategoriesMenuConfig(GuiConfig.TicketCategoriesMenuConfig ticketCategoriesMenuConfig) {
        this.ticketCategoriesMenuConfig = ticketCategoriesMenuConfig;
    }

    public void setTicketDetailMenuConfig(GuiConfig.TicketDetailMenuConfig ticketDetailMenuConfig) {
        this.ticketDetailMenuConfig = ticketDetailMenuConfig;
    }

    public void setTicketsMenuConfig(GuiConfig.TicketsMenuConfig ticketsMenuConfig) {
        this.ticketsMenuConfig = ticketsMenuConfig;
    }

}
