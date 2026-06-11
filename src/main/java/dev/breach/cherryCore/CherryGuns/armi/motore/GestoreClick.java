package dev.breach.cherryCore.CherryGuns.armi.motore;

import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class GestoreClick {

    private GestoreClick() {}

    public static boolean gestisciClick(Arma arma, Player player, ItemStack stack, boolean clickSinistro) {
        if (arma == null || player == null || stack == null) return false;

        if (clickSinistro) {
            MotoreSparo.spara(arma, player, stack);
        } else {
            MotoreRicarica.ricarica(arma, player);
        }
        return true;
    }

    public static boolean gestisciCambioVerso(Arma arma, Player player, ItemStack stack) {
        if (arma == null || player == null) return false;

        if (arma.getSuonoEquipaggia() != null) {
            player.getWorld().playSound(player.getLocation(), arma.getSuonoEquipaggia(), arma.getVolumeSuonoEquipaggia(), 1);
        }

        if (arma.getLentezzaEquipaggia() > 0) {
            applicaLentezza(player, arma.getLentezzaEquipaggia());
        }

        return false;
    }

    public static boolean gestisciCambioDa(Arma arma, Player player, ItemStack stack) {
        if (arma == null || player == null) return false;

        if (arma.getLentezzaEquipaggia() > 0) {
            rimuoviLentezzaSeCorrisponde(player, arma.getLentezzaEquipaggia());
        }

        MotoreMira.disattivaZoom(player, arma);
        return false;
    }

    private static void applicaLentezza(Player player, int forza) {
        if (player.hasPotionEffect(PotionEffectType.SLOWNESS)) {
            if (player.getPotionEffect(PotionEffectType.SLOWNESS).getAmplifier() != forza) {
                player.removePotionEffect(PotionEffectType.SLOWNESS);
            }
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, forza, false, false));
    }

    private static void rimuoviLentezzaSeCorrisponde(Player player, int forza) {
        if (player.hasPotionEffect(PotionEffectType.SLOWNESS) &&
                player.getPotionEffect(PotionEffectType.SLOWNESS).getAmplifier() == forza) {
            player.removePotionEffect(PotionEffectType.SLOWNESS);
        }
    }
}