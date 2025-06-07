package xanth.ogsammaenr.xanthHelp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xanth.ogsammaenr.xanthHelp.command.GenelYardımCommand;
import xanth.ogsammaenr.xanthHelp.command.HelpCommand;
import xanth.ogsammaenr.xanthHelp.command.TeknikYardımCommand;
import xanth.ogsammaenr.xanthHelp.listener.ChatInputListener;
import xanth.ogsammaenr.xanthHelp.listener.InventoryClickListener;
import xanth.ogsammaenr.xanthHelp.manager.CategoryManager;
import xanth.ogsammaenr.xanthHelp.manager.GuiConfigManager;
import xanth.ogsammaenr.xanthHelp.manager.TicketManager;
import xanth.ogsammaenr.xanthHelp.storage.DatabaseConnector;
import xanth.ogsammaenr.xanthHelp.storage.MySQLConnector;
import xanth.ogsammaenr.xanthHelp.storage.SQLiteConnector;

import java.io.File;
import java.sql.SQLException;

public final class XanthHelp extends JavaPlugin {
    private static XanthHelp instance;


    private CategoryManager categoryManager;
    private DatabaseConnector databaseConnector;
    private TicketManager ticketManager;
    private GuiConfigManager guiConfigManager;
    private ChatInputListener chatInputListener;

    private FileConfiguration guiConfig;
    private File guiFile;

    @Override

    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadGuiConfig();

        setupDatabase();

        categoryManager = new CategoryManager(this);
        categoryManager.loadCategories();
        categoryManager.registerPermissions();

        ticketManager = new TicketManager(this);

        guiConfigManager = new GuiConfigManager(this);
        guiConfigManager.load();

        chatInputListener = new ChatInputListener(this);

        getCommand("yardım").setExecutor(new HelpCommand(this));
        getCommand("genelyardım").setExecutor(new GenelYardımCommand());
        getCommand("teknikyardım").setExecutor(new TeknikYardımCommand());

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new InventoryClickListener(this), this);
        pm.registerEvents(chatInputListener, this);

        getLogger().info("***** Xanth Help Enabled *****");

    }

    @Override
    public void onDisable() {


        getLogger().info("***** Xanth Help Disabled *****");
    }

    private void setupDatabase() {
        String type = getConfig().getString("storage.type", "sqlite").toLowerCase();
        if (type.equals("mysql")) {
            databaseConnector = new MySQLConnector(this);
        } else {
            databaseConnector = new SQLiteConnector(this);
        }
        try {
            databaseConnector.connect();
            databaseConnector.initializeTables();
        } catch (SQLException e) {
            getLogger().severe("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void loadGuiConfig() {
        guiFile = new File(getDataFolder(), "gui.yml");
        if (!guiFile.exists()) {
            saveResource("gui.yml", false);
        }
        guiConfig = YamlConfiguration.loadConfiguration(guiFile);
    }

    public FileConfiguration getGuiConfig() {
        return guiConfig;
    }

    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public GuiConfigManager getGuiConfigManager() {
        return guiConfigManager;
    }

    public ChatInputListener getChatInputListener() {
        return chatInputListener;
    }

    public static XanthHelp getInstance() {
        return instance;
    }
}
