package dev.breach.cherryCore.CherryGuns.armi.persistenza;

import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Munizione;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Taser;
import dev.breach.cherryCore.CherryGuns.armi.tipi.TipoArma;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class CaricatoreArmi {

    private CaricatoreArmi() {}

    public static int caricaTutto(JavaPlugin plugin) {
        File cartella = new File(plugin.getDataFolder(), "armi_data");
        if (!cartella.exists()) cartella.mkdirs();

        File[] files = cartella.listFiles();
        if (files == null) return 0;

        int caricate = 0;
        for (File f : files) {
            if (!f.getName().endsWith(".yml")) continue;
            try {
                Arma a = caricaSingolo(plugin, f);
                if (a != null) {
                    Registro.get().registraArma(a);
                    caricate++;
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Errore caricamento arma " + f.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        return caricate;
    }

    private static Arma caricaSingolo(JavaPlugin plugin, File file) {
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);

        if (yml.getBoolean("disabilitato", false)) return null;

        String nome = yml.getString("nome");
        if (nome == null || nome.isEmpty()) return null;

        Material materiale = Material.matchMaterial(yml.getString("materiale", "CROSSBOW"));
        if (materiale == null) materiale = Material.CROSSBOW;

        int customModelData = yml.getInt("custom_model_data", 0);
        Identificatore id = Identificatore.di(materiale, customModelData);

        String nomeVisualizzato = yml.contains("nome_visualizzato")
                ? Colori.traduci(yml.getString("nome_visualizzato"))
                : ChatColor.GOLD + nome;

        TipoArma tipo = TipoArma.PISTOLA;
        if (yml.contains("tipo")) {
            try {
                tipo = TipoArma.valueOf(yml.getString("tipo").toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }

        Arma arma = tipo == TipoArma.TASER
                ? new Taser(nome, id, nomeVisualizzato)
                : new Arma(nome, id, nomeVisualizzato);

        arma.setTipo(tipo);
        arma.setLorePersonalizzata(leggiLore(yml));

        applicaImpostazioni(arma, yml);

        if (arma instanceof Taser) {
            applicaImpostazioniTaser((Taser) arma, yml);
        }

        return arma;
    }

    private static void applicaImpostazioni(Arma arma, FileConfiguration yml) {
        if (yml.contains("munizione")) {
            String nomeMunizione = yml.getString("munizione");
            OggettoBase trovata = Registro.get().getPerNome(nomeMunizione);
            if (trovata instanceof Munizione) {
                arma.setMunizione((Munizione) trovata);
            }
        }

        if (yml.contains("iron_sights")) arma.setHaIronSights(yml.getBoolean("iron_sights"));
        if (yml.contains("livello_zoom")) arma.setLivelloZoom(yml.getInt("livello_zoom"));
        if (yml.contains("visione_notturna")) arma.setVisioneNotturna(yml.getBoolean("visione_notturna"));

        if (yml.contains("danno")) arma.setDanno((float) yml.getDouble("danno"));
        if (yml.contains("caricatore_massimo")) arma.setCaricatoreMassimo(yml.getInt("caricatore_massimo"));
        if (yml.contains("moltiplicatore_headshot")) arma.setMoltiplicatoreHeadshot(yml.getDouble("moltiplicatore_headshot"));

        if (yml.contains("sway.valore")) arma.setSway(yml.getDouble("sway.valore"));
        if (yml.contains("sway.moltiplicatore")) arma.setMoltiplicatoreSway(yml.getDouble("sway.moltiplicatore"));
        if (yml.contains("sway.moltiplicatore_non_zoom")) arma.setMoltiplicatoreSwayNonZoom(yml.getDouble("sway.moltiplicatore_non_zoom"));
        if (yml.contains("sway.modificatore_movimento")) arma.setModificatoreSwayMovimento(yml.getBoolean("sway.modificatore_movimento"));
        if (yml.contains("sway.modificatore_shift")) arma.setModificatoreSwayShift(yml.getBoolean("sway.modificatore_shift"));
        if (yml.contains("sway.modificatore_corsa")) arma.setModificatoreSwayCorsa(yml.getBoolean("sway.modificatore_corsa"));

        if (yml.contains("automatica")) arma.setAutomatica(yml.getBoolean("automatica"));
        if (yml.contains("rateo_fuoco")) arma.setRateoFuoco(yml.getInt("rateo_fuoco"));
        if (yml.contains("proiettili_per_colpo")) arma.setProiettiliPerColpo(yml.getInt("proiettili_per_colpo"));
        if (yml.contains("ritardo_tra_colpi")) arma.setRitardoTraColpi(yml.getDouble("ritardo_tra_colpi"));
        if (yml.contains("tempo_ricarica")) arma.setTempoRicarica(yml.getDouble("tempo_ricarica"));
        if (yml.contains("distanza_massima")) arma.setDistanzaMassima(yml.getInt("distanza_massima"));

        if (yml.contains("primaria")) arma.setPrimaria(yml.getBoolean("primaria"));
        if (yml.contains("munizioni_infinite")) arma.setMunizioniInfinite(yml.getBoolean("munizioni_infinite"));

        if (yml.contains("rinculo")) arma.setRinculo(yml.getDouble("rinculo"));
        if (yml.contains("knockback_sparo")) arma.setKnockbackSparo(yml.getDouble("knockback_sparo"));
        if (yml.contains("lentezza_equipaggia")) arma.setLentezzaEquipaggia(yml.getInt("lentezza_equipaggia"));
        if (yml.contains("volume")) arma.setVolume(yml.getDouble("volume"));

        if (yml.contains("suoni.sparo")) arma.setSuonoSparo(yml.getString("suoni.sparo"));
        if (yml.contains("suoni.ricarica")) arma.setSuonoRicarica(yml.getString("suoni.ricarica"));
        if (yml.contains("suoni.carica")) arma.setSuonoCarica(yml.getString("suoni.carica"));
        if (yml.contains("suoni.equipaggia")) arma.setSuonoEquipaggia(yml.getString("suoni.equipaggia"));

        if (yml.contains("fumo_canna")) arma.setFumoCanna(yml.getBoolean("fumo_canna"));
        if (yml.contains("livello_luce")) arma.setLivelloLuce(yml.getInt("livello_luce"));
        if (yml.contains("messaggio_morte")) arma.setMessaggioMorte(yml.getString("messaggio_morte"));

        if (yml.contains("particella.tipo")) {
            try {
                Particle p = Particle.valueOf(yml.getString("particella.tipo"));
                double r = yml.getDouble("particella.r", 1.0);
                double g = yml.getDouble("particella.g", 1.0);
                double b = yml.getDouble("particella.b", 1.0);
                int data = yml.getInt("particella.data", 0);
                Material mat = Material.matchMaterial(yml.getString("particella.materiale", "COAL_BLOCK"));
                if (mat == null) mat = Material.COAL_BLOCK;
                arma.setParticella(p, r, g, b, mat, data);
            } catch (Exception ignored) {}
        }
    }

    private static void applicaImpostazioniTaser(Taser taser, FileConfiguration yml) {
        if (yml.contains("taser.durata_stun_secondi")) taser.setDurataStunSecondi(yml.getInt("taser.durata_stun_secondi"));
        if (yml.contains("taser.forza_lentezza")) taser.setForzaLentezza(yml.getInt("taser.forza_lentezza"));
        if (yml.contains("taser.applica_cecita")) taser.setApplicaCecita(yml.getBoolean("taser.applica_cecita"));
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