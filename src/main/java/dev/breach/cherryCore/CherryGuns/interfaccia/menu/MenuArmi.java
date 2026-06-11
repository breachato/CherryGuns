package dev.breach.cherryCore.CherryGuns.interfaccia.menu;

import de.themoep.inventorygui.GuiElementGroup;
import dev.breach.cherryCore.CherryGuns.armi.base.OggettoBase;
import dev.breach.cherryCore.CherryGuns.interfaccia.CategoriaArma;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.interfaccia.elementi.ElementoArma;
import dev.breach.cherryCore.CherryGuns.interfaccia.elementi.ElementoIndietro;
import dev.breach.cherryCore.CherryGuns.interfaccia.elementi.ElementoNavigazione;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MenuArmi extends MenuBase {

    private final CategoriaArma categoria;

    public MenuArmi(JavaPlugin plugin, GestoreInterfaccia gestore, CategoriaArma categoria) {
        super(plugin, gestore, "armi");
        this.categoria = categoria;
    }

    @Override
    protected void aggiungiElementi() {
        List<OggettoBase> oggetti = gestore.oggettiPerCategoria(categoria);
        GuiElementGroup gruppo = new GuiElementGroup('.');

        for (OggettoBase oggetto : oggetti) {
            ElementoArma elemento = new ElementoArma(oggetto);
            gruppo.addElement(elemento.crea('i'));
        }

        gui.addElement(gruppo);

        ElementoNavigazione precedente = new ElementoNavigazione(gestore, false);
        gui.addElement(precedente.crea());

        ElementoNavigazione successivo = new ElementoNavigazione(gestore, true);
        gui.addElement(successivo.crea());

        ElementoIndietro indietro = new ElementoIndietro(gestore);
        gui.addElement(indietro.crea());
    }

    @Override
    protected String getTitolo(Player player) {
        String raw = gestore.getMenuConfig().getString("menu.armi.titolo", "&d&lArmeria &7- &f%categoria%");
        return raw.replace("%categoria%", categoria.getNome());
    }
}