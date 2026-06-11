package dev.breach.cherryCore.CherryGuns.interfaccia.elementi;

import de.themoep.inventorygui.StaticGuiElement;
import dev.breach.cherryCore.CherryGuns.interfaccia.CategoriaArma;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.interfaccia.menu.MenuArmi;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class ElementoCategoria {

    private final JavaPlugin plugin;
    private final GestoreInterfaccia gestore;
    private final CategoriaArma categoria;

    public ElementoCategoria(JavaPlugin plugin, GestoreInterfaccia gestore, CategoriaArma categoria) {
        this.plugin = plugin;
        this.gestore = gestore;
        this.categoria = categoria;
    }

    public StaticGuiElement crea(char slot) {
        ItemStack item = new ItemStack(categoria.getMaterialeIcona());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(Colori.traduci(categoria.getNomeIcona()));
            List<String> lore = categoria.getLoreIcona();
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(Colori.traduci(lore));
            }
            item.setItemMeta(meta);
        }

        return new StaticGuiElement(slot, item, click -> {
            Player player = (Player) click.getWhoClicked();
            player.closeInventory();
            new MenuArmi(plugin, gestore, categoria).apri(player);
            return true;
        });
    }
}