package dev.breach.cherryCore.CherryGuns.armi.tipi;

import dev.breach.cherryCore.CherryGuns.armi.base.Comportamento;
import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Armatura extends OggettoBase implements Comportamento {

    private int protezione = 0;
    private boolean annullaHeadshot = false;

    public Armatura(String nome, Identificatore identificatore) {
        super(nome, identificatore, nome, null);
    }

    public Armatura(String nome, Identificatore identificatore, String nomeVisualizzato, List<String> lore) {
        super(nome, identificatore, nomeVisualizzato, lore);
    }

    public int getProtezione() { return protezione; }
    public void setProtezione(int v) { this.protezione = Math.max(0, v); }

    public boolean annullaHeadshot() { return annullaHeadshot; }
    public void setAnnullaHeadshot(boolean v) { this.annullaHeadshot = v; }

    @Override
    public boolean alClickSinistro(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public boolean alClickDestro(Player player, ItemStack stack) {
        ItemStack elmettoAttuale = player.getInventory().getHelmet();
        player.getInventory().setItemInMainHand(elmettoAttuale);
        player.getInventory().setHelmet(stack);
        try {
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 2.0f, 1.0f);
        } catch (Error | Exception ignored) {
        }
        return true;
    }

    @Override
    public boolean alloShift(Player player, ItemStack stack, boolean accovacciato) {
        return false;
    }

    @Override
    public boolean alCambioVerso(Player player, ItemStack stack) {
        if (getSuonoEquipaggia() != null) {
            player.getWorld().playSound(player.getLocation(), getSuonoEquipaggia(), getVolumeSuonoEquipaggia(), 1);
        }
        return false;
    }

    @Override
    public boolean alCambioDa(Player player, ItemStack stack) {
        return false;
    }
}