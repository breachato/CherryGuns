package dev.breach.cherryCore.CherryGuns.armi.motore;

import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class MotoreMira {

    private static final Set<UUID> ZOOMATI = new HashSet<>();

    private MotoreMira() {}

    public static boolean staMirando(Player player) {
        return player != null && ZOOMATI.contains(player.getUniqueId());
    }

    public static void attivaZoom(Player player, Arma arma) {
        if (player == null || arma == null) return;
        UUID uuid = player.getUniqueId();

        if (arma.getLivelloZoom() > 0) {
            ZOOMATI.add(uuid);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1200, arma.getLivelloZoom(), false, false));
        }

        if (arma.haVisioneNotturna()) {
            ZOOMATI.add(uuid);
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1200, 0, false, false));
        }
    }

    public static void disattivaZoom(Player player, Arma arma) {
        if (player == null) return;
        UUID uuid = player.getUniqueId();
        if (!ZOOMATI.contains(uuid)) return;

        if (player.hasPotionEffect(PotionEffectType.SLOWNESS)) {
            player.removePotionEffect(PotionEffectType.SLOWNESS);
        }

        if (arma == null || arma.haVisioneNotturna()) {
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }
        }

        ZOOMATI.remove(uuid);
    }

    public static void rimuoviTutti() {
        ZOOMATI.clear();
    }
}