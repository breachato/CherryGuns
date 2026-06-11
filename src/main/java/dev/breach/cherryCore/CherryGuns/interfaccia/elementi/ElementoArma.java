package dev.breach.cherryCore.CherryGuns.interfaccia.elementi;

import de.themoep.inventorygui.StaticGuiElement;
import dev.breach.cherryCore.CherryGuns.api.CherryAPI;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Arma;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Armatura;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Granata;
import dev.breach.cherryCore.CherryGuns.armi.tipi.Munizione;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class ElementoArma {

    private final OggettoBase oggetto;

    public ElementoArma(OggettoBase oggetto) {
        this.oggetto = oggetto;
    }

    public StaticGuiElement crea(char slot) {
        try {
            ItemStack stack = CherryAPI.creaItemStack(oggetto);
            if (stack == null) return creaItemErrore(slot);

            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                lore.addAll(costruisciInfoLore());
                meta.setLore(Colori.traduci(lore));
                stack.setItemMeta(meta);
            }

            return new StaticGuiElement(slot, stack, click -> {
                click.getRawEvent().setCancelled(true);
                Player player = (Player) click.getWhoClicked();

                if (!player.hasPermission("cherryguns.armi.prendi")) {
                    player.sendMessage(ChatColor.RED + "Non hai il permesso.");
                    return true;
                }

                CherryAPI.dai(player, oggetto);
                player.sendMessage(ChatColor.GREEN + "Hai ricevuto: " + ChatColor.WHITE + oggetto.getNomeVisualizzato());
                return true;
            });
        } catch (Exception e) {
            return creaItemErrore(slot);
        }
    }

    private StaticGuiElement creaItemErrore(char slot) {
        ItemStack errore = new ItemStack(Material.BARRIER);
        ItemMeta meta = errore.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Errore caricamento oggetto");
            errore.setItemMeta(meta);
        }
        return new StaticGuiElement(slot, errore, click -> {
            click.getRawEvent().setCancelled(true);
            return true;
        });
    }

    private List<String> costruisciInfoLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&8&m                              ");

        if (oggetto instanceof Arma) {
            Arma a = (Arma) oggetto;
            lore.add("&7Tipo: &d" + a.getTipo().getNomeVisualizzato());
            lore.add("&7Danno: &c" + a.getDanno());
            lore.add("&7Caricatore: &e" + a.getCaricatoreMassimo());
            if (a.getMunizione() != null) {
                lore.add("&7Munizione: &b" + a.getMunizione().getNomeVisualizzato());
            }
            lore.add("&7Tempo Ricarica: &a" + a.getTempoRicarica() + "s");
        } else if (oggetto instanceof Munizione) {
            Munizione m = (Munizione) oggetto;
            lore.add("&7Tipo: &6Munizione");
            lore.add("&7Stack Massimo: &e" + m.getStackMassimo());
            lore.add("&7Danno Perforante: &c" + m.getDannoPerforante());
        } else if (oggetto instanceof Armatura) {
            Armatura ar = (Armatura) oggetto;
            lore.add("&7Tipo: &bArmatura");
            lore.add("&7Protezione: &a" + ar.getProtezione());
            if (ar.annullaHeadshot()) lore.add("&7Annulla Headshot: &aSì");
        } else if (oggetto instanceof Granata) {
            Granata g = (Granata) oggetto;
            lore.add("&7Tipo: &5Granata");
            lore.add("&7Raggio: &e" + g.getRaggioEsplosione());
            lore.add("&7Danno: &c" + g.getDannoEsplosione());
        }

        lore.add("&8&m                              ");
        lore.add("&e▸ Click per prendere");
        lore.add("&8&m                              ");
        return lore;
    }
}