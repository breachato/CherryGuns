package dev.breach.cherryCore.CherryGuns.armi.tipi;

public enum TipoArma {

    PISTOLA("Pistola"),
    MITRAGLIETTA("Mitraglietta"),
    FUCILE("Fucile"),
    FUCILE_ASSALTO("Fucile d'Assalto"),
    FUCILE_PRECISIONE("Fucile di Precisione"),
    SHOTGUN("Shotgun"),
    MITRAGLIATRICE("Mitragliatrice"),
    LANCIARAZZI("Lanciarazzi"),
    TASER("Taser"),
    LANCIAFIAMME("Lanciafiamme"),
    PERSONALIZZATA("Personalizzata");

    private final String nomeVisualizzato;

    TipoArma(String nomeVisualizzato) {
        this.nomeVisualizzato = nomeVisualizzato;
    }

    public String getNomeVisualizzato() {
        return nomeVisualizzato;
    }
}