package dev.breach.cherryCore.CherryGuns.api.eventi;

import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmaHeadshotEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player tiratore;
    private final Arma arma;
    private final LivingEntity bersaglio;
    private boolean cancellato = false;

    public ArmaHeadshotEvent(Player tiratore, Arma arma, LivingEntity bersaglio) {
        this.tiratore = tiratore;
        this.arma = arma;
        this.bersaglio = bersaglio;
    }

    public Player getTiratore() {
        return tiratore;
    }

    public Arma getArma() {
        return arma;
    }

    public LivingEntity getBersaglio() {
        return bersaglio;
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