package dev.breach.cherryCore.CherryGuns.armi.tipi;

import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;

import java.util.List;

public class Munizione extends OggettoBase {

    private double dannoPerforante = 1.0;
    private double gravita = 0.05;
    private int velocita = 50;

    public Munizione(String nome, Identificatore identificatore) {
        super(nome, identificatore, nome, null);
        setStackMassimo(64);
    }

    public Munizione(String nome, Identificatore identificatore, String nomeVisualizzato, List<String> lore) {
        super(nome, identificatore, nomeVisualizzato, lore);
        setStackMassimo(64);
    }

    public double getDannoPerforante() { return dannoPerforante; }
    public void setDannoPerforante(double v) { this.dannoPerforante = Math.max(0, v); }

    public double getGravita() { return gravita; }
    public void setGravita(double v) { this.gravita = v; }

    public int getVelocita() { return velocita; }
    public void setVelocita(int v) { this.velocita = Math.max(1, v); }
}