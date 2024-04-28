package it.univr.wordautomata.model;

import io.github.mimoguz.customwindow.WindowStyler;
import it.univr.wordautomata.utils.Utils.Theme;
import static it.univr.wordautomata.utils.Utils.Theme.DARK;
import static it.univr.wordautomata.utils.Utils.Theme.LIGHT;
import javafx.stage.Stage;

/**
 *
 */
public class Model {

    private static Model instance;

    private Theme theme;

    private Model() {
        theme = Theme.DEFAULT; //needed in advance for MainPanel;

    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }

        return instance;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
    
    public Theme cycleTheme(){
        Theme next = theme.next();
        instance.setTheme(next);
        return next;
    }
}
