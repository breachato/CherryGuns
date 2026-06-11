package dev.breach.cherryCore.CherryGuns.log;

import dev.breach.cherryCore.CherryGuns.api.eventi.ArmaColpoEntitaEvent;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Taser;
import dev.breach.cherryCore.CherryGuns.configurazione.Configurazione;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public final class AscoltatoreLog implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void alColpo(ArmaColpoEntitaEvent evento) {
        if (evento.isCancelled()) return;
        if (Configurazione.get() != null && !Configurazione.get().isAbilitaLog()) return;

        Player tiratore = evento.getTiratore();
        LivingEntity bersaglio = evento.getBersaglio();
        if (!(bersaglio instanceof Player)) return;

        Player bersaglioPlayer = (Player) bersaglio;
        if (tiratore.getUniqueId().equals(bersaglioPlayer.getUniqueId())) return;

        String nomeArma = ChatColor.stripColor(evento.getArma().getNomeVisualizzato());

        DatiSparo dati = new DatiSparo(
                tiratore.getUniqueId(),
                tiratore.getName(),
                bersaglioPlayer.getUniqueId(),
                bersaglioPlayer.getName(),
                nomeArma,
                evento.getParteCorpo(),
                evento.getDanno(),
                tiratore.getLocation(),
                bersaglioPlayer.getLocation(),
                evento.getArma() instanceof Taser
        );

        GestoreLog.get().inviaLogSparo(dati);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void allaMorte(PlayerDeathEvent evento) {
        if (Configurazione.get() != null && !Configurazione.get().isAbilitaLog()) return;

        Player vittima = evento.getEntity();
        UUID vittimaUuid = vittima.getUniqueId();
        String vittimaNome = vittima.getName();

        DatiSparo ultimoSparo = GestoreLog.get().getTracker().getUltimoSparoVerso(vittimaUuid);

        if (ultimoSparo != null) {
            DatiSparo datiMorte = new DatiSparo(
                    ultimoSparo.getTiratoreUuid(),
                    ultimoSparo.getTiratoreNome(),
                    vittimaUuid,
                    vittimaNome,
                    ultimoSparo.getArmaNomeVisualizzato(),
                    ultimoSparo.getParteCorpo(),
                    ultimoSparo.getDanno(),
                    ultimoSparo.getPosizioneTiratore(),
                    vittima.getLocation(),
                    ultimoSparo.eTaser()
            );
            GestoreLog.get().inviaLogMorteConArma(datiMorte);
        } else {
            GestoreLog.get().inviaLogMorteGenerica(vittimaUuid, vittimaNome);
        }
    }
}