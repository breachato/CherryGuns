package dev.breach.cherryCore.CherryGuns.armi.motore;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteItemNBT;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Munizione;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public final class GestoreMunizioni {

    private static final String CHIAVE_MUNIZIONI = "cherryguns_ammo";

    private GestoreMunizioni() {}

    public static int leggiMunizioniNelloStack(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) return 0;
        return NBT.get(stack, nbt -> nbt.hasTag(CHIAVE_MUNIZIONI) ? nbt.getInteger(CHIAVE_MUNIZIONI) : 0);
    }

    public static void scriviMunizioniNelloStack(ItemStack stack, int quantita) {
        if (stack == null || stack.getType() == Material.AIR) return;
        NBT.modify(stack, (Consumer<ReadWriteItemNBT>) nbt -> nbt.setInteger(CHIAVE_MUNIZIONI, Math.max(0, quantita)));
    }

    public static int leggiMunizioniInMano(Player player) {
        if (player == null) return 0;
        return leggiMunizioniNelloStack(player.getInventory().getItemInMainHand());
    }

    public static void scriviMunizioniInMano(Player player, int quantita) {
        if (player == null) return;
        ItemStack stack = player.getInventory().getItemInMainHand();
        scriviMunizioniNelloStack(stack, quantita);
    }

    public static int conteggioMunizioniNellInventario(Player player, Munizione munizione) {
        if (player == null || munizione == null) return 0;
        if (player.getGameMode() == GameMode.CREATIVE) return Integer.MAX_VALUE;

        int totale = 0;
        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack == null) continue;
            if (Registro.get().getMunizione(munizione.getIdentificatore()) == null) continue;
            if (Registro.get().eMunizione(stack)) {
                Munizione m = (Munizione) Registro.get().getQualsiasi(stack);
                if (m != null && m.getIdentificatore().equals(munizione.getIdentificatore())) {
                    totale += stack.getAmount();
                }
            }
        }
        return totale;
    }

    public static boolean rimuoviMunizioniDallInventario(Player player, Munizione munizione, int quantita) {
        if (player == null || munizione == null || quantita <= 0) return false;
        if (player.getGameMode() == GameMode.CREATIVE) return true;

        int rimanenti = quantita;
        for (int i = 0; i < player.getInventory().getSize() && rimanenti > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack == null || !Registro.get().eMunizione(stack)) continue;

            Munizione m = (Munizione) Registro.get().getQualsiasi(stack);
            if (m == null || !m.getIdentificatore().equals(munizione.getIdentificatore())) continue;

            int quantitaStack = stack.getAmount();
            if (quantitaStack > rimanenti) {
                stack.setAmount(quantitaStack - rimanenti);
                rimanenti = 0;
            } else {
                player.getInventory().setItem(i, null);
                rimanenti -= quantitaStack;
            }
        }

        return rimanenti <= 0;
    }

    public static boolean playerPuoRicaricare(Arma arma, Player player) {
        if (arma == null || player == null) return false;
        if (player.getGameMode() == GameMode.CREATIVE) return true;
        if (arma.haMunizioniInfinite()) return true;
        if (arma.getMunizione() == null) return true;
        return conteggioMunizioniNellInventario(player, arma.getMunizione()) > 0;
    }
}