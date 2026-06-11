package dev.breach.cherryCore.CherryGuns.armi.accesso;

import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Munizione;
import dev.breach.cherryCore.CherryGuns.armi.tipi.TipoArma;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class CreatoreArmi {

    public enum Esito {
        SUCCESSO,
        GIA_ESISTENTE,
        MATERIALE_INVALIDO,
        MUNIZIONE_INVALIDA,
        ERRORE
    }

    private CreatoreArmi() {}

    public static Esito creaArma(JavaPlugin plugin, String nome, String nomeVisualizzato, String materialeStr,
                                 String nomeMunizione, int danno, int caricatoreMassimo) {
        if (nome == null || nome.isEmpty()) return Esito.ERRORE;
        if (Registro.get().getPerNome(nome) != null) return Esito.GIA_ESISTENTE;

        Material materiale;
        try {
            materiale = Material.valueOf(materialeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Esito.MATERIALE_INVALIDO;
        }

        OggettoBase munizione = Registro.get().getPerNome(nomeMunizione);
        if (!(munizione instanceof Munizione)) return Esito.MUNIZIONE_INVALIDA;

        try {
            int customModelData = trovaProssimoCustomModelData(materiale);
            Identificatore id = Identificatore.di(materiale, customModelData);

            Arma nuovaArma = new Arma(nome, id, Colori.traduci(nomeVisualizzato));
            nuovaArma.setTipo(TipoArma.PISTOLA);
            nuovaArma.setDanno(danno);
            nuovaArma.setCaricatoreMassimo(caricatoreMassimo);
            nuovaArma.setMunizione((Munizione) munizione);

            salvaSuFile(plugin, nuovaArma, nomeMunizione, materialeStr, customModelData, danno, caricatoreMassimo);

            Registro.get().registraArma(nuovaArma);
            return Esito.SUCCESSO;
        } catch (Exception e) {
            plugin.getLogger().warning("Errore creazione arma " + nome + ": " + e.getMessage());
            return Esito.ERRORE;
        }
    }

    public static boolean eliminaArma(JavaPlugin plugin, String nome) {
        if (nome == null) return false;

        File cartella = new File(plugin.getDataFolder(), "armi_data");
        if (!cartella.exists()) return false;

        File file = new File(cartella, nome + ".yml");
        if (!file.exists()) return false;

        boolean eliminato = file.delete();
        if (eliminato) {
            GestoreAccessi.get().eliminaPerArma(nome);
            Registro.get().svuota();
        }
        return eliminato;
    }

    private static int trovaProssimoCustomModelData(Material materiale) {
        int max = 0;
        for (OggettoBase obj : Registro.get().tutto()) {
            if (obj.getIdentificatore().getMateriale() == materiale) {
                int cmd = obj.getIdentificatore().getCustomModelData();
                if (cmd > max) max = cmd;
            }
        }
        return max + 1;
    }

    private static void salvaSuFile(JavaPlugin plugin, Arma arma, String nomeMunizione,
                                    String materialeStr, int customModelData,
                                    int danno, int caricatoreMassimo) throws IOException {
        File cartella = new File(plugin.getDataFolder(), "armi_data");
        if (!cartella.exists()) cartella.mkdirs();

        File file = new File(cartella, arma.getNome() + ".yml");
        FileConfiguration yml = new YamlConfiguration();

        yml.set("nome", arma.getNome());
        yml.set("nome_visualizzato", arma.getNomeVisualizzato().replace("§", "&"));
        yml.set("materiale", materialeStr.toUpperCase());
        yml.set("custom_model_data", customModelData);
        yml.set("tipo", arma.getTipo().name());
        yml.set("munizione", nomeMunizione);
        yml.set("danno", danno);
        yml.set("caricatore_massimo", caricatoreMassimo);
        yml.set("iron_sights", true);
        yml.set("automatica", false);
        yml.set("ritardo_tra_colpi", 0.25);
        yml.set("tempo_ricarica", 1.5);
        yml.set("distanza_massima", 150);
        yml.set("disabilitato", false);

        yml.save(file);
    }
}