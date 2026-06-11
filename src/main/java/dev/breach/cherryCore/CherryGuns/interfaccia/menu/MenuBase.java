package dev.breach.cherryCore.CherryGuns.interfaccia.menu;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.utility.Colori;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MenuBase {

    protected final JavaPlugin plugin;
    protected final GestoreInterfaccia gestore;
    protected final String idMenu;
    protected InventoryGui gui;

    protected MenuBase(JavaPlugin plugin, GestoreInterfaccia gestore, String idMenu) {
        this.plugin = plugin;
        this.gestore = gestore;
        this.idMenu = idMenu;
    }

    public void costruisci(Player player) {
        String titolo = Colori.traduci(getTitolo(player));
        String[] struttura = getStruttura();

        gui = new InventoryGui(plugin, player, titolo, struttura);
        gui.setFiller(new ItemStack(Material.AIR));

        aggiungiDecoratori();
        aggiungiElementi();
    }

    protected void aggiungiDecoratori() {
        ConfigurationSection custom = gestore.getMenuConfig().getConfigurationSection("elementi.decoratori");
        if (custom == null) return;

        for (String key : custom.getKeys(false)) {
            ConfigurationSection section = custom.getConfigurationSection(key);
            if (section == null) continue;

            Material materiale = XMaterial.matchXMaterial(section.getString("materiale", "BLACK_STAINED_GLASS_PANE"))
                    .orElse(XMaterial.BLACK_STAINED_GLASS_PANE)
                    .get();

            ItemStack item = new ItemStack(materiale);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(Colori.traduci(section.getString("nome", " ")));
                List<String> lore = section.getStringList("lore");
                if (!lore.isEmpty()) meta.setLore(Colori.traduci(lore));
                item.setItemMeta(meta);
            }

            char marker = key.charAt(0);
            gui.addElement(new StaticGuiElement(marker, item, click -> {
                click.getRawEvent().setCancelled(true);
                return true;
            }));
        }
    }

    protected abstract void aggiungiElementi();

    public void apri(Player player) {
        if (gui == null) costruisci(player);
        gui.show(player);
    }

    public InventoryGui getGui() {
        return gui;
    }

    protected String getTitolo(Player player) {
        return gestore.getMenuConfig().getString("menu." + idMenu + ".titolo", "Menu");
    }

    protected String[] getStruttura() {
        List<String> raw = gestore.getMenuConfig()
                .getStringList("menu." + idMenu + ".struttura")
                .stream()
                .map(s -> s.replace(" ", ""))
                .collect(Collectors.toList());

        if (raw.isEmpty()) {
            return new String[]{
                    "#########",
                    "#.......#",
                    "#.......#",
                    "#.......#",
                    "#########"
            };
        }
        return raw.toArray(new String[0]);
    }
}