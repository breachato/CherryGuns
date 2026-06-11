package dev.breach.cherryCore.CherryGuns.interfaccia;

import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Armatura;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Granata;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Munizione;
import dev.breach.cherryCore.CherryGuns.interfaccia.menu.MenuCategorie;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class GestoreInterfaccia {

    private static GestoreInterfaccia istanza;

    private final JavaPlugin plugin;
    private FileConfiguration menuConfig;
    private final Map<String, CategoriaArma> categorie = new LinkedHashMap<>();

    private GestoreInterfaccia(JavaPlugin plugin) {
        this.plugin = plugin;
        caricaConfig();
        caricaCategorie();
    }

    public static void inizializza(JavaPlugin plugin) {
        if (istanza == null) istanza = new GestoreInterfaccia(plugin);
    }

    public static GestoreInterfaccia get() {
        return istanza;
    }

    private void caricaConfig() {
        File menuFile = new File(plugin.getDataFolder(), "menu.yml");
        if (!menuFile.exists()) {
            try {
                plugin.saveResource("menu.yml", false);
            } catch (Exception ignored) {}
        }
        menuConfig = YamlConfiguration.loadConfiguration(menuFile);

        try (InputStream defStream = plugin.getResource("menu.yml")) {
            if (defStream != null) {
                try (InputStreamReader reader = new InputStreamReader(defStream)) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(reader);
                    menuConfig.setDefaults(defConfig);
                }
            }
        } catch (Exception ignored) {}
    }

    private void caricaCategorie() {
        categorie.clear();
        ConfigurationSection section = menuConfig.getConfigurationSection("categorie");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection catSection = section.getConfigurationSection(key);
            if (catSection != null) {
                categorie.put(key, new CategoriaArma(key, catSection));
            }
        }
    }

    public void ricarica() {
        caricaConfig();
        caricaCategorie();
    }

    public FileConfiguration getMenuConfig() {
        return menuConfig;
    }

    public List<CategoriaArma> getCategorie() {
        return new ArrayList<>(categorie.values());
    }

    public List<OggettoBase> oggettiPerCategoria(CategoriaArma categoria) {
        if (categoria == null) return new ArrayList<>();

        List<OggettoBase> tutti;
        switch (categoria.getFiltro()) {
            case ARMI:
                tutti = new ArrayList<>(Registro.get().tutteLeArmi());
                break;
            case MUNIZIONI:
                tutti = new ArrayList<>(Registro.get().tutteLeMunizioni());
                break;
            case ARMATURE:
                tutti = new ArrayList<>(Registro.get().tutteLeArmature());
                break;
            case GRANATE:
                tutti = new ArrayList<>(Registro.get().tutteLeGranate());
                break;
            case TUTTE:
            default:
                tutti = Registro.get().tutto();
        }

        return tutti.stream()
                .sorted(Comparator.comparing(OggettoBase::getNomeVisualizzato, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public void apriMenuArmi(Player player) {
        new MenuCategorie(plugin, this).apri(player);
    }
}