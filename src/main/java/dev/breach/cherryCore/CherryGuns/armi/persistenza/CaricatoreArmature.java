package dev.breach.cherryCore.CherryGuns.armi.persistenza;

import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Armatura;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class CaricatoreArmature {

    private CaricatoreArmature() {}

    public static int caricaTutto(JavaPlugin plugin) {
        File cartella = new File(plugin.getDataFolder(), "armature");
        if (!cartella.exists()) cartella.mkdirs();

        File[] files = cartella.listFiles();
        if (files == null) return 0;

        int caricate = 0;
        for (File f : files) {
            if (!f.getName().endsWith(".yml")) continue;
            try {
                Armatura a = caricaSingolo(f);
                if (a != null) {
                    Registro.get().registraArmatura(a);
                    caricate++;
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Errore caricamento armatura " + f.getName() + ": " + e.getMessage());
            }
        }

        return caricate;
    }

    private static Armatura caricaSingolo(File file) {
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);

        if (yml.getBoolean("disabilitato", false)) return null;

        String nome = yml.getString("nome");
        if (nome == null || nome.isEmpty()) return null;

        Material materiale = Material.matchMaterial(yml.getString("materiale", "LEATHER_HELMET"));
        if (materiale == null) materiale = Material.LEATHER_HELMET;

        int customModelData = yml.getInt("custom_model_data", 0);
        Identificatore id = Identificatore.di(materiale, customModelData);

        String nomeVisualizzato = yml.contains("nome_visualizzato")
                ? Colori.traduci(yml.getString("nome_visualizzato"))
                : ChatColor.WHITE + nome;

        List<String> lore = leggiLore(yml);

        Armatura armatura = new Armatura(nome, id, nomeVisualizzato, lore);

        if (yml.contains("protezione")) armatura.setProtezione(yml.getInt("protezione"));
        if (yml.contains("annulla_headshot")) armatura.setAnnullaHeadshot(yml.getBoolean("annulla_headshot"));
        if (yml.contains("suono_equipaggia")) armatura.setSuonoEquipaggia(yml.getString("suono_equipaggia"));

        return armatura;
    }

    private static List<String> leggiLore(FileConfiguration yml) {
        List<String> lore = new ArrayList<>();
        if (yml.contains("lore")) {
            for (String riga : yml.getStringList("lore")) {
                lore.add(Colori.traduci(riga));
            }
        }
        return lore;
    }
}