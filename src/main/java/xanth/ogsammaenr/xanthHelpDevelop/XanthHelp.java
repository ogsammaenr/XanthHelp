package xanth.ogsammaenr.xanthHelpDevelop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketCategoryManager;
import xanth.ogsammaenr.xanthHelpDevelop.manager.TicketManager;
import xanth.ogsammaenr.xanthHelpDevelop.storage.CategoryLoader;
import xanth.ogsammaenr.xanthHelpDevelop.storage.SQLiteConnector;
import xanth.ogsammaenr.xanthHelpDevelop.storage.SQLiteTicketDAO;
import xanth.ogsammaenr.xanthHelpDevelop.storage.TicketDAO;

import java.io.File;


public final class XanthHelp extends JavaPlugin {
    private static XanthHelp instance;

    private SQLiteConnector databaseConnector;
    private TicketDAO ticketDAO;
    private TicketManager ticketManager;
    private TicketCategoryManager ticketCategoryManager;
    private CategoryLoader categoryLoader;

    @Override
    public void onEnable() {
        instance = this;

        // Veritabanı dosyasının yolu
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        File dbFile = new File(dataFolder, "tickets.db");

        File categoryFile = new File(dataFolder, "categories.yml");
        FileConfiguration categories = new YamlConfiguration().loadConfiguration(categoryFile);

        // Veritabanı bağlantısını oluştur
        this.databaseConnector = new SQLiteConnector(dbFile.getAbsolutePath());

        // DAO ve Manager sınıflarını başlat
        this.ticketDAO = new SQLiteTicketDAO(databaseConnector);
        this.ticketManager = new TicketManager(ticketDAO);
        this.ticketCategoryManager = new TicketCategoryManager(this);
        this.categoryLoader = new CategoryLoader(categories, this);

        // Komutlar, EventListener'lar burada kaydedilir (ileride eklenecek)

        getLogger().info("******* XanthHelp Enabled *******");
    }

    @Override
    public void onDisable() {
        getLogger().info("******* XanthHelp Disabled *******");
    }

    public TicketCategoryManager getTicketCategoryManager() {
        return ticketCategoryManager;
    }

    public CategoryLoader getCategoryLoader() {
        return categoryLoader;
    }

    public static XanthHelp getInstance() {
        return instance;
    }

}
