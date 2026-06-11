package dev.breach.cherryCore.CherryGuns.api.eventi;

import dev.breach.cherryCore.CherryGuns.armi.motore.ParteCorpo;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmaColpoEntitaEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player tiratore;
    private final Arma arma;
    private final LivingEntity bersaglio;
    private final ParteCorpo parteCorpo;
    private final Location puntoImpatto;
    private double danno;
    private boolean cancellato = false;

    public ArmaColpoEntitaEvent(Player tiratore, Arma arma, LivingEntity bersaglio,
                                ParteCorpo parteCorpo, double danno, Location puntoImpatto) {
        this.tiratore = tiratore;
        this.arma = arma;
        this.bersaglio = bersaglio;
        this.parteCorpo = parteCorpo;
        this.danno = danno;
        this.puntoImpatto = puntoImpatto;
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

    public ParteCorpo getParteCorpo() {
        return parteCorpo;
    }

    public Location getPuntoImpatto() {
        return puntoImpatto;
    }

    public double getDanno() {
        return danno;
    }

    public void setDanno(double danno) {
        this.danno = Math.max(0, danno);
    }

    public boolean eHeadshot() {
        return parteCorpo == ParteCorpo.TESTA;
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