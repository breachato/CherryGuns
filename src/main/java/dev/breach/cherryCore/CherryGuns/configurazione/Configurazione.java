package dev.breach.cherryCore.CherryGuns.configurazione;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Configurazione {

    private static Configurazione istanza;

    private final JavaPlugin plugin;
    private FileConfiguration config;

    private String prefisso;
    private boolean debug;
    private boolean cambiaMessaggiMorte;
    private int distanzaImpattoMassima;
    private boolean prevenisciArmiHopper;

    private boolean abilitaLog;
    private String permessoLog;
    private long resetTrackerSecondi;

    private Configurazione(JavaPlugin plugin) {
        this.plugin = plugin;
        ricarica();
    }

    public static void inizializza(JavaPlugin plugin) {
        if (istanza == null) istanza = new Configurazione(plugin);
    }

    public static Configurazione get() {
        return istanza;
    }

    public void ricarica() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        caricaValori();
    }

    private void caricaValori() {
        prefisso = config.getString("prefisso", "&8[&dCherry Guns&8]&r");
        debug = config.getBoolean("debug", false);
        cambiaMessaggiMorte = config.getBoolean("cambia_messaggi_morte", true);
        distanzaImpattoMassima = config.getInt("distanza_impatto_massima", 200);
        prevenisciArmiHopper = config.getBoolean("previeni_armi_hopper", true);

        abilitaLog = config.getBoolean("log.abilitato", true);
        permessoLog = config.getString("log.permesso", "cherryguns.logs");
        resetTrackerSecondi = config.getLong("log.reset_tracker_secondi", 5);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getPrefisso() {
        return prefisso;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isCambiaMessaggiMorte() {
        return cambiaMessaggiMorte;
    }

    public int getDistanzaImpattoMassima() {
        return distanzaImpattoMassima;
    }

    public boolean isPrevenisciArmiHopper() {
        return prevenisciArmiHopper;
    }

    public boolean isAbilitaLog() {
        return abilitaLog;
    }

    public String getPermessoLog() {
        return permessoLog;
    }

    public long getResetTrackerSecondi() {
        return resetTrackerSecondi;
    }

    public void salva() {
        plugin.saveConfig();
    }
}