package dev.breach.cherryCore.CherryGuns.armi.base;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Comportamento {

    boolean alClickSinistro(Player player, ItemStack stack);

    boolean alClickDestro(Player player, ItemStack stack);

    boolean alloShift(Player player, ItemStack stack, boolean accovacciato);

    boolean alCambioVerso(Player player, ItemStack stack);

    boolean alCambioDa(Player player, ItemStack stack);
}