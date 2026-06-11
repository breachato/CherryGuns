package dev.breach.cherryCore.CherryGuns.armi.motore;

public enum ParteCorpo {

    TESTA("Testa"),
    CORPO("Corpo"),
    GAMBE("Gambe"),
    BRACCIA("Braccia");

    private final String nomeVisualizzato;

    ParteCorpo(String nomeVisualizzato) {
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }
}