package dev.breach.cherryCore.CherryGuns.interfaccia.elementi;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.StaticGuiElement;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class ElementoIndietro {

    private final GestoreInterfaccia gestore;

    public ElementoIndietro(GestoreInterfaccia gestore) {
        this.gestore = gestore;
    }

    public StaticGuiElement crea() {
        String tipoStr = gestore.getMenuConfig().getString("elementi.indietro.materiale", "BARRIER");
        Material materiale = XMaterial.matchXMaterial(tipoStr).orElse(XMaterial.BARRIER).get();

        ItemStack item = new ItemStack(materiale);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Colori.traduci(gestore.getMenuConfig().getString("elementi.indietro.nome", "&cTorna alle Categorie")));
            List<String> lore = gestore.getMenuConfig().getStringList("elementi.indietro.lore");
            if (!lore.isEmpty()) meta.setLore(Colori.traduci(lore));
            item.setItemMeta(meta);
        }

        return new StaticGuiElement('B', item, click -> {
            Player player = (Player) click.getWhoClicked();
            player.closeInventory();
            gestore.apriMenuArmi(player);
            return true;
        });
    }
}