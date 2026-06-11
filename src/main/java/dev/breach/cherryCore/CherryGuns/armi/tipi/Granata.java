package dev.breach.cherryCore.CherryGuns.armi.tipi;

import dev.breach.cherryCore.CherryGuns.armi.base.Comportamento;
import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Granata extends OggettoBase implements Comportamento {

    private double raggioEsplosione = 4.0;
    private double dannoEsplosione = 6.0;
    private int ritardoEsplosioneSecondi = 3;
    private double velocitaLancio = 1.5;
    private boolean autoArma = false;

    public Granata(String nome, Identificatore identificatore) {
        super(nome, identificatore, nome, null);
        setStackMassimo(3);
    }

    public Granata(String nome, Identificatore identificatore, String nomeVisualizzato, List<String> lore) {
        super(nome, identificatore, nomeVisualizzato, lore);
        setStackMassimo(3);
    }

    public double getRaggioEsplosione() { return raggioEsplosione; }
    public void setRaggioEsplosione(double v) { this.raggioEsplosione = v; }

    public double getDannoEsplosione() { return dannoEsplosione; }
    public void setDannoEsplosione(double v) { this.dannoEsplosione = v; }

    public int getRitardoEsplosioneSecondi() { return ritardoEsplosioneSecondi; }
    public void setRitardoEsplosioneSecondi(int v) { this.ritardoEsplosioneSecondi = Math.max(0, v); }

    public double getVelocitaLancio() { return velocitaLancio; }
    public void setVelocitaLancio(double v) { this.velocitaLancio = v; }

    public boolean autoArma() { return autoArma; }
    public void setAutoArma(boolean v) { this.autoArma = v; }

    @Override
    public boolean alClickSinistro(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public boolean alClickDestro(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public boolean alloShift(Player player, ItemStack stack, boolean accovacciato) {
        return false;
    }

    @Override
    public boolean alCambioVerso(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public boolean alCambioDa(Player player, ItemStack stack) {
        return false;
    }
}