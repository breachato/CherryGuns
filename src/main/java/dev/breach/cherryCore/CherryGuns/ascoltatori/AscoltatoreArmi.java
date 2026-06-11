package dev.breach.cherryCore.CherryGuns.ascoltatori;

import dev.breach.cherryCore.CherryGuns.api.CherryAPI;
import dev.breach.cherryCore.CherryGuns.armi.accesso.GestoreAccessi;
import dev.breach.cherryCore.CherryGuns.armi.base.Comportamento;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.configurazione.Configurazione;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class AscoltatoreArmi implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void alClick(PlayerInteractEvent evento) {
        if (evento.getHand() == EquipmentSlot.OFF_HAND) return;

        Player player = evento.getPlayer();
        ItemStack stack = evento.getItem();
        if (stack == null) return;

        OggettoBase oggetto = Registro.get().getQualsiasi(stack);
        if (oggetto == null) return;

        if (oggetto instanceof Arma) {
            if (!GestoreAccessi.get().puoUsare(player, oggetto.getNome())) {
                evento.setCancelled(true);
                String prefisso = Configurazione.get() != null ? Configurazione.get().getPrefisso() : "";
                player.sendMessage(prefisso + ChatColor.RED + " Non puoi usare quest'arma.");
                return;
            }
        }

        if (!(oggetto instanceof Comportamento)) return;
        Comportamento comp = (Comportamento) oggetto;

        Action azione = evento.getAction();
        if (azione == Action.LEFT_CLICK_AIR || azione == Action.LEFT_CLICK_BLOCK) {
            if (comp.alClickSinistro(player, stack)) {
                evento.setCancelled(true);
            }
        } else if (azione == Action.RIGHT_CLICK_AIR || azione == Action.RIGHT_CLICK_BLOCK) {
            if (comp.alClickDestro(player, stack)) {
                evento.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void alloShift(PlayerToggleSneakEvent evento) {
        Player player = evento.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        OggettoBase oggetto = Registro.get().getQualsiasi(stack);
        if (!(oggetto instanceof Comportamento)) return;
        ((Comportamento) oggetto).alloShift(player, stack, evento.isSneaking());
    }

    @EventHandler
    public void alCambioSlot(PlayerItemHeldEvent evento) {
        Player player = evento.getPlayer();
        ItemStack precedente = player.getInventory().getItem(evento.getPreviousSlot());
        ItemStack nuovo = player.getInventory().getItem(evento.getNewSlot());

        if (precedente != null) {
            OggettoBase obj = Registro.get().getQualsiasi(precedente);
            if (obj instanceof Comportamento) ((Comportamento) obj).alCambioDa(player, precedente);
        }
        if (nuovo != null) {
            OggettoBase obj = Registro.get().getQualsiasi(nuovo);
            if (obj instanceof Comportamento) ((Comportamento) obj).alCambioVerso(player, nuovo);
        }
    }

    @EventHandler
    public void alRompiBlocco(BlockBreakEvent evento) {
        ItemStack stack = evento.getPlayer().getInventory().getItemInMainHand();
        if (CherryAPI.eOggettoCherry(stack)) {
            evento.setCancelled(true);
        }
    }

    @EventHandler
    public void alPiazzaBlocco(BlockPlaceEvent evento) {
        if (CherryAPI.eOggettoCherry(evento.getItemInHand())) {
            evento.setCancelled(true);
        }
    }

    @EventHandler
    public void alPickupHopper(InventoryPickupItemEvent evento) {
        if (Configurazione.get() != null && !Configurazione.get().isPrevenisciArmiHopper()) return;
        if (evento.getInventory().getType() != InventoryType.HOPPER) return;
        if (CherryAPI.eOggettoCherry(evento.getItem().getItemStack())) {
            evento.setCancelled(true);
        }
    }

    @EventHandler
    public void alMoveHopper(InventoryMoveItemEvent evento) {
        if (Configurazione.get() != null && !Configurazione.get().isPrevenisciArmiHopper()) return;
        InventoryType source = evento.getSource().getType();
        if (source != InventoryType.HOPPER && source != InventoryType.DISPENSER && source != InventoryType.DROPPER) return;
        if (CherryAPI.eOggettoCherry(evento.getItem())) {
            evento.setCancelled(true);
        }
    }
}