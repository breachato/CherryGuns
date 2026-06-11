package dev.breach.cherryCore.CherryGuns.configurazione;

import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class Messaggi {

    private static Messaggi istanza;

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;

    private Messaggi(JavaPlugin plugin) {
        this.plugin = plugin;
        File langDir = new File(plugin.getDataFolder(), "lang");
        if (!langDir.exists()) langDir.mkdirs();
        this.file = new File(langDir, "it.yml");
        ricarica();
    }

    public static void inizializza(JavaPlugin plugin) {
        if (istanza == null) istanza = new Messaggi(plugin);
    }

    public static Messaggi get() {
        return istanza;
    }

    public void ricarica() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Impossibile creare lang/it.yml: " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        InputStream defStream = plugin.getResource("lang/it.yml");
        if (defStream != null) {
            try (InputStreamReader reader = new InputStreamReader(defStream)) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
                config.setDefaults(defConfig);
                config.options().copyDefaults(true);
                config.save(file);
            } catch (IOException ignored) {
            }
        }
    }

    public void salva() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Impossibile salvare lang/it.yml: " + e.getMessage());
        }
    }

    public String stringa(String chiave) {
        return stringa(chiave, chiave);
    }

    public String stringa(String chiave, String predefinito) {
        String valore = config.getString(chiave, predefinito);
        return Colori.traduci(valore);
    }

    public String stringaGrezza(String chiave, String predefinito) {
        return config.getString(chiave, predefinito);
    }

    public List<String> lista(String chiave) {
        List<String> raw = config.getStringList(chiave);
        if (raw.isEmpty()) return new ArrayList<>();
        return Colori.traduci(raw);
    }

    public int intero(String chiave, int predefinito) {
        return config.getInt(chiave, predefinito);
    }

    public double decimale(String chiave, double predefinito) {
        return config.getDouble(chiave, predefinito);
    }

    public boolean booleano(String chiave, boolean predefinito) {
        return config.getBoolean(chiave, predefinito);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}