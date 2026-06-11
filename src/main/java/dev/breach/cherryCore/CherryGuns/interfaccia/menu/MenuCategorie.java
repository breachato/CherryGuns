package dev.breach.cherryCore.CherryGuns.interfaccia.menu;

import de.themoep.inventorygui.GuiElementGroup;
import dev.breach.cherryCore.CherryGuns.interfaccia.CategoriaArma;
import dev.breach.cherryCore.CherryGuns.interfaccia.GestoreInterfaccia;
import dev.breach.cherryCore.CherryGuns.interfaccia.elementi.ElementoCategoria;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class MenuCategorie extends MenuBase {

    public MenuCategorie(JavaPlugin plugin, GestoreInterfaccia gestore) {
        super(plugin, gestore, "categorie");
    }

    @Override
    protected void aggiungiElementi() {
        List<CategoriaArma> categorie = gestore.getCategorie();
        GuiElementGroup gruppo = new GuiElementGroup('.');

        for (CategoriaArma categoria : categorie) {
            ElementoCategoria elemento = new ElementoCategoria(plugin, gestore, categoria);
            gruppo.addElement(elemento.crea('c'));
        }

        gui.addElement(gruppo);
    }
}