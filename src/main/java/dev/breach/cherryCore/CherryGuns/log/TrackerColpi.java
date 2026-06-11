package dev.breach.cherryCore.CherryGuns.log;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TrackerColpi {

    private final Map<UUID, DatiSparo> ultimoSparoPerVittima = new ConcurrentHashMap<>();
    private final long resetDopoMs;

    public TrackerColpi(long resetDopoSecondi) {
        this.resetDopoMs = resetDopoSecondi * 1000L;
    }

    public void registra(DatiSparo dati) {
        if (dati == null || dati.getBersaglioUuid() == null) return;
        ultimoSparoPerVittima.put(dati.getBersaglioUuid(), dati);
    }

    public DatiSparo getUltimoSparoVerso(UUID vittima) {
        if (vittima == null) return null;
        DatiSparo dati = ultimoSparoPerVittima.get(vittima);
        if (dati == null) return null;
        if (System.currentTimeMillis() - dati.getTimestamp() > resetDopoMs) {
            ultimoSparoPerVittima.remove(vittima);
            return null;
        }
        return dati;
    }

    public void rimuovi(UUID vittima) {
        if (vittima != null) ultimoSparoPerVittima.remove(vittima);
    }

    public void svuota() {
        ultimoSparoPerVittima.clear();
    }
}