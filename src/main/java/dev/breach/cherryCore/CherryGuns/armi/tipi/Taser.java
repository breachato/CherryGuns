package dev.breach.cherryCore.CherryGuns.armi.tipi;

import dev.breach.cherryCore.CherryGuns.armi.base.Identificatore;

public class Taser extends Arma {

    private int durataStunSecondi = 5;
    private int forzaLentezza = 4;
    private boolean applicaCecita = true;

    public Taser(String nome, Identificatore identificatore) {
        super(nome, identificatore);
        configuraValoriPredefiniti();
    }

    public Taser(String nome, Identificatore identificatore, String nomeVisualizzato) {
        super(nome, identificatore, nomeVisualizzato);
        configuraValoriPredefiniti();
    }

    private void configuraValoriPredefiniti() {
        setTipo(TipoArma.TASER);
        setDanno(0.5f);
        setCaricatoreMassimo(3);
        setAutomatica(false);
        setRitardoTraColpi(2.0);
        setRinculo(0);
        setKnockbackSparo(0);
        setDistanzaMassima(8);
        setSuonoSparo("entity.lightning_bolt.thunder");
        setFumoCanna(false);
    }

    public int getDurataStunSecondi() { return durataStunSecondi; }
    public void setDurataStunSecondi(int v) { this.durataStunSecondi = Math.max(1, v); }

    public int getForzaLentezza() { return forzaLentezza; }
    public void setForzaLentezza(int v) { this.forzaLentezza = Math.max(0, v); }

    public boolean applicaCecita() { return applicaCecita; }
    public void setApplicaCecita(boolean v) { this.applicaCecita = v; }
}