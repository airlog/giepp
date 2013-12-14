package pl.pisz.airlog.giepp.desktop.util;

import pl.pisz.airlog.giepp.game.Game;

public class GameUtilities {

    private static Game instance = null;
    
    public static Game getInstance() {
        if (instance == null) instance = HelperTools.newGame();
        return instance;
    }
    
    private GameUtilities() {}
        
}
