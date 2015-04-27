package toru.gamelib;

import toru.game.framework.Screen;
import toru.game.framework.impl.AndroidGame;

/**
 * Created by Toru on 2015/04/12.
 */
public class MrNon extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}
