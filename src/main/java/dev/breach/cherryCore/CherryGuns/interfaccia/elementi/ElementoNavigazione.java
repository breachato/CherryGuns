package dev.breach.cherryCore.CherryGuns.interfaccia.elementi;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiPageElement;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class ElementoNavigazione {

    private final GestoreInterfaccia gestore;
    private final boolean avanti;

    public ElementoNavigazione(GestoreInterfaccia gestore, boolean avanti) {
        this.gestore = gestore;
        this.avanti = avanti;
    }

    public GuiPageElement crea() {
        GuiPageElement.PageAction azione = avanti
                ? GuiPageElement.PageAction.NEXT
                : GuiPageElement.PageAction.PREVIOUS;

        List<String> text = new ArrayList<>();
        ItemStack item = costruisciItem();

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName()) text.add(meta.getDisplayName());
            if (meta != null && meta.hasLore() && meta.getLore() != null) text.addAll(meta.getLore());
        }

        return new GuiPageElement(avanti ? '>' : '<', item, azione, text.toArray(new String[0]));
    }

    private ItemStack costruisciItem() {
        String chiave = avanti ? "avanti" : "indietro";
        String tipoStr = gestore.getMenuConfig().getString("elementi.navigazione." + chiave + ".materiale", "ARROW");
        Material materiale = XMaterial.matchXMaterial(tipoStr).orElse(XMaterial.ARROW).get();

        ItemStack item = new ItemStack(materiale);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Colori.traduci(gestore.getMenuConfig().getString(
                    "elementi.navigazione." + chiave + ".nome",
                    avanti ? "&aPagina Successiva" : "&cPagina Precedente"
            )));
            List<String> lore = gestore.getMenuConfig().getStringList("elementi.navigazione." + chiave + ".lore");
            if (!lore.isEmpty()) meta.setLore(Colori.traduci(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}