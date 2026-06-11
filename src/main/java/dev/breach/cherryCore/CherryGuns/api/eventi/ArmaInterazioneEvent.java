package dev.breach.cherryCore.CherryGuns.api.eventi;

import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmaInterazioneEvent extends Event implements Cancellable {

    public enum Tipo {
        CLICK_SINISTRO,
        CLICK_DESTRO,
        SHIFT,
        CAMBIO_VERSO,
        CAMBIO_DA
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final OggettoBase oggetto;
    private final Tipo tipo;
    private boolean cancellato = false;

    public ArmaInterazioneEvent(Player player, OggettoBase oggetto, Tipo tipo) {
        this.player = player;
        this.oggetto = oggetto;
        this.tipo = tipo;
    }

    public Player getPlayer() {
        return player;
    }

    public OggettoBase getOggetto() {
        return oggetto;
    }

    public Tipo getTipo() {
        return tipo;
    }

    @Override
    public boolean isCancelled() {
        return cancellato;
    }

    @Override
    public void setCancelled(boolean cancellato) {
        this.cancellato = cancellato;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}