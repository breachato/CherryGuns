package dev.breach.cherryCore.CherryGuns.configurazione;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class MessaggiPredefiniti {

    private MessaggiPredefiniti() {}

    public static void generaConfigSeMancante(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (configFile.exists()) return;

        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();

        FileConfiguration config = new YamlConfiguration();
        config.set("prefisso", "&8[&dCherry Guns&8]&r");
        config.set("debug", false);
        config.set("cambia_messaggi_morte", true);
        config.set("distanza_impatto_massima", 200);
        config.set("previeni_armi_hopper", true);

        config.set("log.abilitato", true);
        config.set("log.permesso", "cherryguns.logs");
        config.set("log.reset_tracker_secondi", 5);

        config.set("permessi.bypass_whitelist", "cherryguns.armi.bypass");
        config.set("permessi.usa_armi", "cherryguns.usegun");

        try {
            config.save(configFile);
            plugin.getLogger().info("config.yml generato.");
        } catch (IOException e) {
            plugin.getLogger().warning("Impossibile generare config.yml: " + e.getMessage());
        }
    }

    public static void generaLangSeMancante(JavaPlugin plugin) {
        File langDir = new File(plugin.getDataFolder(), "lang");
        if (!langDir.exists()) langDir.mkdirs();

        File langFile = new File(langDir, "it.yml");
        if (langFile.exists()) return;

        FileConfiguration lang = new YamlConfiguration();

        lang.set("generale.nessun_permesso", "&cNon hai il permesso di eseguire questa azione.");
        lang.set("generale.player_non_trovato", "&cPlayer non trovato.");
        lang.set("generale.player_non_online", "&cIl player non è online.");
        lang.set("generale.solo_player", "&cQuesto comando può essere eseguito solo da un player.");
        lang.set("generale.ricaricato", "&aConfigurazione ricaricata.");

        lang.set("armi.arma_non_trovata", "&cArma \"%nome%\" non trovata.");
        lang.set("armi.arma_consegnata", "&aHai dato &f%arma% &aa &f%player%&a.");
        lang.set("armi.arma_ricevuta", "&aHai ricevuto: &f%arma%");
        lang.set("armi.non_puoi_usare", "&cNon puoi usare quest'arma.");
        lang.set("armi.creazione_successo", "&aArma \"%nome%\" creata con successo.");
        lang.set("armi.creazione_gia_esistente", "&cEsiste già un'arma con quel nome.");
        lang.set("armi.creazione_materiale_invalido", "&cMateriale \"%materiale%\" non valido.");
        lang.set("armi.creazione_munizione_invalida", "&cMunizione \"%munizione%\" non trovata.");
        lang.set("armi.creazione_errore", "&cErrore durante la creazione dell'arma.");
        lang.set("armi.eliminata", "&aArma \"%nome%\" eliminata.");
        lang.set("armi.eliminazione_fallita", "&cImpossibile eliminare l'arma.");

        lang.set("accesso.whitelist_aggiunto", "&a%player% aggiunto alla whitelist di %arma%.");
        lang.set("accesso.whitelist_gia_presente", "&e%player% era già nella whitelist.");
        lang.set("accesso.whitelist_rimosso", "&a%player% rimosso dalla whitelist di %arma%.");
        lang.set("accesso.whitelist_non_presente", "&e%player% non era nella whitelist.");
        lang.set("accesso.whitelist_vuota", "&eLa whitelist di %arma% è vuota.");
        lang.set("accesso.whitelist_lista", "&aWhitelist di %arma% (%numero%):");

        lang.set("accesso.blacklist_aggiunto", "&a%player% aggiunto alla blacklist di %arma%.");
        lang.set("accesso.blacklist_gia_presente", "&e%player% era già nella blacklist.");
        lang.set("accesso.blacklist_rimosso", "&a%player% rimosso dalla blacklist di %arma%.");
        lang.set("accesso.blacklist_non_presente", "&e%player% non era nella blacklist.");
        lang.set("accesso.blacklist_vuota", "&eLa blacklist di %arma% è vuota.");
        lang.set("accesso.blacklist_lista", "&aBlacklist di %arma% (%numero%):");

        lang.set("comando.help_titolo", "&d&lCherry Guns &7- &fComandi disponibili");
        lang.set("comando.help_gui", "&d/armi gui &7- Apre la GUI delle armi");
        lang.set("comando.help_prendi", "&d/armi prendi <arma> [player] &7- Dà un'arma");
        lang.set("comando.help_aggiungi", "&d/armi aggiungi <nome> <displayname> <materiale> <munizione> <danno> <caricatore>");
        lang.set("comando.help_rimuovi", "&d/armi rimuovi <arma> &7- Elimina arma");
        lang.set("comando.help_cerca", "&d/armi cerca <nome> &7- Cerca arma");
        lang.set("comando.help_whitelist", "&d/armi whitelist <aggiungi|rimuovi|lista> <arma> [player]");
        lang.set("comando.help_blacklist", "&d/armi blacklist <aggiungi|rimuovi|lista> <arma> [player]");
        lang.set("comando.help_reload", "&d/armi reload &7- Ricarica config");

        lang.set("comando.ricerca_nessun_risultato", "&cNessuna arma trovata per \"%query%\".");
        lang.set("comando.ricerca_risultati", "&aRisultati ricerca (%numero%):");

        lang.set("ricarica.in_corso", "[Ricarica...]");
        lang.set("ricarica.completata", "&aArma ricaricata.");

        lang.set("hotbar.formato", "&f%arma% &7| &b%munizioni_attuali%&7/&b%munizioni_massime% %stato%");
        lang.set("hotbar.stato_normale", "&a✓");
        lang.set("hotbar.stato_ricarica", "&e⟳");
        lang.set("hotbar.stato_vuoto", "&c✗");

        lang.set("log.tp_fallito", "&cImpossibile teletrasportarti: posizione non più disponibile.");

        try {
            lang.save(langFile);
            plugin.getLogger().info("lang/it.yml generato.");
        } catch (IOException e) {
            plugin.getLogger().warning("Impossibile generare lang/it.yml: " + e.getMessage());
        }
    }
}