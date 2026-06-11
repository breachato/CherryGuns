package dev.breach.cherryCore.CherryGuns.hook;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.flag.WrappedState;

import java.util.Optional;

public final class HookProtezione {

    private static HookProtezione istanza;
    private final JavaPlugin plugin;
    private boolean worldGuardDisponibile = false;
    private IWrappedFlag<WrappedState> flagPvp;

    private HookProtezione(JavaPlugin plugin) {
        this.plugin = plugin;
        inizializza();
    }

    public static void registra(JavaPlugin plugin) {
        if (istanza == null) istanza = new HookProtezione(plugin);
    }

    public static HookProtezione get() {
        return istanza;
    }

    private void inizializza() {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            plugin.getLogger().info("WorldGuard non trovato. Protezione PvP disabilitata.");
            return;
        }
        try {
            Optional<IWrappedFlag<WrappedState>> flag = WorldGuardWrapper.getInstance().getFlag("pvp", WrappedState.class);
            flag.ifPresent(f -> {
                flagPvp = f;
                worldGuardDisponibile = true;
            });
            if (worldGuardDisponibile) {
                plugin.getLogger().info("Hook WorldGuard registrato correttamente.");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Impossibile registrare hook WorldGuard: " + e.getMessage());
        }
    }

    public boolean puoUsareArmi(Location location) {
        if (!worldGuardDisponibile || location == null) return true;
        try {
            Optional<WrappedState> stato = WorldGuardWrapper.getInstance().queryFlag(null, location, flagPvp);
            return stato.map(s -> s == WrappedState.ALLOW).orElse(true);
        } catch (Exception e) {
            return true;
        }
    }

    public boolean puoColpire(Player tiratore, Player bersaglio) {
        if (tiratore == null || bersaglio == null) return false;
        if (!puoUsareArmi(tiratore.getLocation())) return false;
        return puoUsareArmi(bersaglio.getLocation());
    }
}