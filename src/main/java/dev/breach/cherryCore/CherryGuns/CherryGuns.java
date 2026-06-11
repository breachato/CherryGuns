package dev.breach.cherryCore.CherryGuns;

import dev.breach.cherryCore.CherryGuns.armi.accesso.ComandoArmi;
import dev.breach.cherryCore.CherryGuns.armi.accesso.ComandoTeletrasporto;
import dev.breach.cherryCore.CherryGuns.armi.accesso.GestoreAccessi;
import dev.breach.cherryCore.CherryGuns.armi.motore.MotoreRicarica;
import dev.breach.cherryCore.CherryGuns.armi.persistenza.CaricatoreArmature;
import dev.breach.cherryCore.CherryGuns.armi.persistenza.CaricatoreArmi;
import dev.breach.cherryCore.CherryGuns.armi.persistenza.CaricatoreMunizioni;
import dev.breach.cherryCore.CherryGuns.ascoltatori.AscoltatoreArmi;
import dev.breach.cherryCore.CherryGuns.ascoltatori.AscoltatoreServer;
import dev.breach.cherryCore.CherryGuns.configurazione.Configurazione;
import dev.breach.cherryCore.CherryGuns.configurazione.Messaggi;
import dev.breach.cherryCore.CherryGuns.configurazione.MessaggiPredefiniti;
import dev.breach.cherryCore.CherryGuns.hook.HookLuckPerms;
import dev.breach.cherryCore.CherryGuns.hook.HookProtezione;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.log.AscoltatoreLog;
import dev.breach.cherryCore.CherryGuns.log.GestoreLog;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class CherryGuns extends JavaPlugin {

    private static CherryGuns istanza;

    public static CherryGuns get() {
        return istanza;
    }

    @Override
    public void onEnable() {
        istanza = this;

        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        getLogger().info("Avvio Cherry Guns...");

        MessaggiPredefiniti.generaConfigSeMancante(this);
        MessaggiPredefiniti.generaLangSeMancante(this);

        Configurazione.inizializza(this);
        Messaggi.inizializza(this);

        HookLuckPerms.registra(this);
        HookProtezione.registra(this);

        GestoreAccessi.inizializza(this);
        GestoreInterfaccia.inizializza(this);
        MotoreRicarica.inizializza(this);
        GestoreLog.inizializza(this);

        caricaArmi();
        registraComandi();
        registraAscoltatori();

        getLogger().info("Cherry Guns avviato correttamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Cherry Guns disabilitato.");
    }

    public void ricaricaTutto() {
        Configurazione.get().ricarica();
        Messaggi.get().ricarica();
        GestoreAccessi.get().ricarica();
        GestoreInterfaccia.get().ricarica();

        dev.breach.cherryCore.CherryGuns.armi.base.Registro.get().svuota();
        caricaArmi();

        getLogger().info("Cherry Guns ricaricato.");
    }

    private void caricaArmi() {
        int munizioni = CaricatoreMunizioni.caricaTutto(this);
        int armature = CaricatoreArmature.caricaTutto(this);
        int armi = CaricatoreArmi.caricaTutto(this);

        getLogger().info("Caricate: " + munizioni + " munizioni, " + armature + " armature, " + armi + " armi.");
    }

    private void registraComandi() {
        PluginCommand armiCmd = getCommand("armi");
        if (armiCmd != null) {
            ComandoArmi handler = new ComandoArmi(this);
            armiCmd.setExecutor(handler);
            armiCmd.setTabCompleter(handler);
        }

        PluginCommand tpCmd = getCommand("cherryguns_tp");
        if (tpCmd != null) {
            tpCmd.setExecutor(new ComandoTeletrasporto());
        }
    }

    private void registraAscoltatori() {
        Bukkit.getPluginManager().registerEvents(new AscoltatoreArmi(), this);
        Bukkit.getPluginManager().registerEvents(new AscoltatoreServer(), this);
        Bukkit.getPluginManager().registerEvents(new AscoltatoreLog(), this);
    }
}