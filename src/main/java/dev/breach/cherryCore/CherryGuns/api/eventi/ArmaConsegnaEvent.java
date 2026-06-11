package dev.breach.cherryCore.CherryGuns.api.eventi;

import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmaConsegnaEvent extends Event implements Cancellable {

    public enum Causa {
        COMANDO,
        GUI,
        API
    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player destinatario;
    private OggettoBase oggetto;
    private final Causa causa;
    private boolean cancellato = false;

    public ArmaConsegnaEvent(Player destinatario, OggettoBase oggetto, Causa causa) {
        this.destinatario = destinatario;
        this.oggetto = oggetto;
        this.causa = causa;
    }

    public Player getDestinatario() {
        return destinatario;
    }

    public OggettoBase getOggetto() {
        return oggetto;
    }

    public void setOggetto(OggettoBase oggetto) {
        this.oggetto = oggetto;
    }

    public Causa getCausa() {
        return causa;
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