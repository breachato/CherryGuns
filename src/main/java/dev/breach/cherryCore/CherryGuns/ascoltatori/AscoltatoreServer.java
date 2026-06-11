package dev.breach.cherryCore.CherryGuns.ascoltatori;

import dev.breach.cherryCore.CherryGuns.armi.motore.MotoreMira;
import dev.breach.cherryCore.CherryGuns.armi.motore.MotoreRicarica;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class AscoltatoreServer implements Listener {

    @EventHandler
    public void alQuit(PlayerQuitEvent evento) {
        MotoreMira.disattivaZoom(evento.getPlayer(), null);
        MotoreRicarica.annulla(evento.getPlayer());
    }

    @EventHandler
    public void alRespawn(PlayerRespawnEvent evento) {
        MotoreMira.disattivaZoom(evento.getPlayer(), null);
        MotoreRicarica.annulla(evento.getPlayer());
    }
}