package dev.breach.cherryCore.CherryGuns.armi.persistenza;

import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Munizione;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class CaricatoreMunizioni {

    private CaricatoreMunizioni() {}

    public static int caricaTutto(JavaPlugin plugin) {
        File cartella = new File(plugin.getDataFolder(), "munizioni");
        if (!cartella.exists()) cartella.mkdirs();

        File[] files = cartella.listFiles();
        if (files == null) return 0;

        int caricate = 0;
        for (File f : files) {
            if (!f.getName().endsWith(".yml")) continue;
            try {
                Munizione m = caricaSingolo(f);
                if (m != null) {
                    Registro.get().registraMunizione(m);
                    caricate++;
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Errore caricamento munizione " + f.getName() + ": " + e.getMessage());
            }
        }

        return caricate;
    }

    private static Munizione caricaSingolo(File file) {
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);

        if (yml.getBoolean("disabilitato", false)) return null;

        String nome = yml.getString("nome");
        if (nome == null || nome.isEmpty()) return null;

        Material materiale = Material.matchMaterial(yml.getString("materiale", "PHANTOM_MEMBRANE"));
        if (materiale == null) materiale = Material.PHANTOM_MEMBRANE;

        int customModelData = yml.getInt("custom_model_data", 0);
        Identificatore id = Identificatore.di(materiale, customModelData);

        String nomeVisualizzato = yml.contains("nome_visualizzato")
                ? Colori.traduci(yml.getString("nome_visualizzato"))
                : ChatColor.WHITE + nome;

        List<String> lore = leggiLore(yml);

        Munizione munizione = new Munizione(nome, id, nomeVisualizzato, lore);

        if (yml.contains("stack_massimo")) munizione.setStackMassimo(yml.getInt("stack_massimo"));
        if (yml.contains("danno_perforante")) munizione.setDannoPerforante(yml.getDouble("danno_perforante"));
        if (yml.contains("gravita")) munizione.setGravita(yml.getDouble("gravita"));
        if (yml.contains("velocita")) munizione.setVelocita(yml.getInt("velocita"));

        return munizione;
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