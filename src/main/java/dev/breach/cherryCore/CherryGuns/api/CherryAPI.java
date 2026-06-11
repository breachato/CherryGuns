package dev.breach.cherryCore.CherryGuns.api;

import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.base.Registro;
import dev.breach.cherryCore.CherryGuns.armi.motore.GestoreMunizioni;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Armatura;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Granata;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Munizione;
import dev.breach.cherryCore.CherryGuns.configurazione.CostruttoreItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public final class CherryAPI {

    private CherryAPI() {}

    public static OggettoBase trovaPerNome(String nome) {
        return Registro.get().getPerNome(nome);
    }

    public static OggettoBase trovaDaItem(ItemStack stack) {
        return Registro.get().getQualsiasi(stack);
    }

    public static Arma trovaArma(ItemStack stack) {
        OggettoBase obj = Registro.get().getQualsiasi(stack);
        return obj instanceof Arma ? (Arma) obj : null;
    }

    public static Munizione trovaMunizione(ItemStack stack) {
        OggettoBase obj = Registro.get().getQualsiasi(stack);
        return obj instanceof Munizione ? (Munizione) obj : null;
    }

    public static Armatura trovaArmatura(ItemStack stack) {
        OggettoBase obj = Registro.get().getQualsiasi(stack);
        return obj instanceof Armatura ? (Armatura) obj : null;
    }

    public static Granata trovaGranata(ItemStack stack) {
        OggettoBase obj = Registro.get().getQualsiasi(stack);
        return obj instanceof Granata ? (Granata) obj : null;
    }

    public static Arma armaInMano(HumanEntity entity) {
        if (entity == null) return null;
        return trovaArma(entity.getInventory().getItemInMainHand());
    }

    public static boolean eArma(ItemStack stack) {
        return Registro.get().eArma(stack);
    }

    public static boolean eMunizione(ItemStack stack) {
        return Registro.get().eMunizione(stack);
    }

    public static boolean eArmatura(ItemStack stack) {
        return Registro.get().eArmatura(stack);
    }

    public static boolean eGranata(ItemStack stack) {
        return Registro.get().eGranata(stack);
    }

    public static boolean eOggettoCherry(ItemStack stack) {
        return Registro.get().eQualsiasi(stack);
    }

    public static Collection<OggettoBase> tutteLeArmi() {
        return Registro.get().tutteLeArmi();
    }

    public static Collection<OggettoBase> tutteLeMunizioni() {
        return Registro.get().tutteLeMunizioni();
    }

    public static Collection<OggettoBase> tutteLeArmature() {
        return Registro.get().tutteLeArmature();
    }

    public static Collection<OggettoBase> tutteLeGranate() {
        return Registro.get().tutteLeGranate();
    }

    public static ItemStack creaItemStack(OggettoBase oggetto) {
        if (oggetto == null) return null;
        Identificatore id = oggetto.getIdentificatore();
        return CostruttoreItem.di(id.getMateriale())
                .nome(oggetto.getNomeVisualizzato())
                .lore(oggetto.getLorePersonalizzata())
                .customModelData(id.getCustomModelData())
                .irrompibile(true)
                .nascondiAttributi()
                .quantita(1)
                .costruisci();
    }

    public static int munizioniInArmaInMano(Player player) {
        return GestoreMunizioni.leggiMunizioniInMano(player);
    }

    public static int munizioniDisponibili(Player player, Munizione munizione) {
        return GestoreMunizioni.conteggioMunizioniNellInventario(player, munizione);
    }

    public static void dai(Player player, OggettoBase oggetto) {
        if (player == null || oggetto == null) return;
        ItemStack stack = creaItemStack(oggetto);
        if (stack == null) return;

        if (oggetto instanceof Arma) {
            GestoreMunizioni.scriviMunizioniNelloStack(stack, ((Arma) oggetto).getCaricatoreMassimo());
        }

        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(stack);
        } else {
            player.getWorld().dropItem(player.getLocation(), stack);
        }
    }
}