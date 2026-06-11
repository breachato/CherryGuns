package dev.breach.cherryCore.CherryGuns.armi.motore;

import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class MotoreRicarica {

    private static JavaPlugin plugin;
    private static final Set<UUID> IN_RICARICA = new HashSet<>();

    private MotoreRicarica() {}

    public static void inizializza(JavaPlugin pluginIstanza) {
        plugin = pluginIstanza;
    }

    public static boolean staRicaricando(Player player) {
        return player != null && IN_RICARICA.contains(player.getUniqueId());
    }

    public static void ricarica(Arma arma, Player player) {
        if (plugin == null || arma == null || player == null) return;
        UUID uuid = player.getUniqueId();
        if (IN_RICARICA.contains(uuid)) return;

        int munizioniInArma = GestoreMunizioni.leggiMunizioniInMano(player);
        int massimo = arma.getCaricatoreMassimo();
        if (munizioniInArma >= massimo) return;

        if (!GestoreMunizioni.playerPuoRicaricare(arma, player)) return;

        IN_RICARICA.add(uuid);

        if (arma.getSuonoRicarica() != null) {
            player.getWorld().playSound(player.getLocation(), arma.getSuonoRicarica(), 1.0f, 1.0f);
        }

        long durataTick = (long) (arma.getTempoRicarica() * 20L);

        new BukkitRunnable() {
            @Override
            public void run() {
                IN_RICARICA.remove(uuid);
                if (!player.isOnline()) return;

                int attuale = GestoreMunizioni.leggiMunizioniInMano(player);
                int necessari = arma.getCaricatoreMassimo() - attuale;
                if (necessari <= 0) return;

                int disponibili = arma.haMunizioniInfinite()
                        ? necessari
                        : GestoreMunizioni.conteggioMunizioniNellInventario(player, arma.getMunizione());

                int daCaricare = Math.min(necessari, disponibili);
                if (daCaricare <= 0) return;

                if (!arma.haMunizioniInfinite()) {
                    GestoreMunizioni.rimuoviMunizioniDallInventario(player, arma.getMunizione(), daCaricare);
                }

                GestoreMunizioni.scriviMunizioniInMano(player, attuale + daCaricare);
            }
        }.runTaskLater(plugin, durataTick);
    }

    public static void annulla(Player player) {
        if (player != null) IN_RICARICA.remove(player.getUniqueId());
    }
}