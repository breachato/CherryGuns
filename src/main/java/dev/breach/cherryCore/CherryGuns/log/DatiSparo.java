package dev.breach.cherryCore.CherryGuns.log;

import dev.breach.cherryCore.CherryGuns.armi.motore.ParteCorpo;
import org.bukkit.Location;

import java.util.UUID;

public final class DatiSparo {

    private final UUID tiratoreUuid;
    private final String tiratoreNome;
    private final UUID bersaglioUuid;
    private final String bersaglioNome;
    private final String armaNomeVisualizzato;
    private final ParteCorpo parteCorpo;
    private final double danno;
    private final Location posizioneTiratore;
    private final Location posizioneBersaglio;
    private final long timestamp;
    private final boolean eTaser;

    public DatiSparo(UUID tiratoreUuid, String tiratoreNome,
                     UUID bersaglioUuid, String bersaglioNome,
                     String armaNomeVisualizzato, ParteCorpo parteCorpo, double danno,
                     Location posizioneTiratore, Location posizioneBersaglio,
                     boolean eTaser) {
        this.tiratoreUuid = tiratoreUuid;
        this.tiratoreNome = tiratoreNome;
        this.bersaglioUuid = bersaglioUuid;
        this.bersaglioNome = bersaglioNome;
        this.armaNomeVisualizzato = armaNomeVisualizzato;
        this.parteCorpo = parteCorpo;
        this.danno = danno;
        this.posizioneTiratore = posizioneTiratore;
        this.posizioneBersaglio = posizioneBersaglio;
        this.timestamp = System.currentTimeMillis();
        this.eTaser = eTaser;
    }

    public UUID getTiratoreUuid() { return tiratoreUuid; }
    public String getTiratoreNome() { return tiratoreNome; }
    public UUID getBersaglioUuid() { return bersaglioUuid; }
    public String getBersaglioNome() { return bersaglioNome; }
    public String getArmaNomeVisualizzato() { return armaNomeVisualizzato; }
    public ParteCorpo getParteCorpo() { return parteCorpo; }
    public double getDanno() { return danno; }
    public Location getPosizioneTiratore() { return posizioneTiratore; }
    public Location getPosizioneBersaglio() { return posizioneBersaglio; }
    public long getTimestamp() { return timestamp; }
    public boolean eTaser() { return eTaser; }
}