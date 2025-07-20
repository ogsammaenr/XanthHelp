---
title: Untitled doc (3)
---
<SwmSnippet path="/src/main/java/xanth/ogsammaenr/xanthHelpDevelop/XanthHelp.java" line="1">

---

&nbsp;

```java
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

        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        File dbFile = new File(dataFolder, "tickets.db");

        File categoryFile = new File(dataFolder, "categories.yml");
        FileConfiguration categories = new YamlConfiguration().loadConfiguration(categoryFile);

        this.databaseConnector = new SQLiteConnector(dbFile.getAbsolutePath());
        this.ticketDAO = new SQLiteTicketDAO(databaseConnector);


        this.ticketManager = new TicketManager(ticketDAO);
        this.ticketCategoryManager = new TicketCategoryManager(this);
        this.categoryLoader = new CategoryLoader(categories, this);

        categoryLoader.loadAll();


        getLogger().info("******* XanthHelp Enabled *******");
    }

    @Override
    public void onDisable() {
        getLogger().info("******* XanthHelp Disabled *******");
    }

    /* ===== GETTERS ===== */

    public TicketManager getTicketManager() {
        return ticketManager;
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

```

---

</SwmSnippet>

<SwmMeta version="3.0.0" repo-id="Z2l0aHViJTNBJTNBWGFudGhIZWxwJTNBJTNBb2dzYW1tYWVucg==" repo-name="XanthHelp"><sup>Powered by [Swimm](https://app.swimm.io/)</sup></SwmMeta>
