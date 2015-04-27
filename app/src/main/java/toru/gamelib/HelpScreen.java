package toru.gamelib;

import java.util.List;

import toru.game.framework.Game;
import toru.game.framework.Graphics;
import toru.game.framework.Input.TouchEvent;
import toru.game.framework.Screen;

/**
 * Created by Toru on 2015/04/18.
 */
public class HelpScreen extends Screen {
    private enum Help {
        HELP1, HELP2, HELP3
    }
    private Help help = Help.HELP1;

    HelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();

        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 256 && event.y > 416) {
                    switch (help) {
                        case HELP1:
                            help = Help.HELP2;
                            break;
                        case HELP2:
                            help = Help.HELP3;
                            break;
                        case HELP3:
                            game.setScreen(new MainMenuScreen(game));
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);
        switch (help) {
            case HELP1:
                g.drawPixmap(Assets.help1, 64, 100);
                break;
            case HELP2:
                g.drawPixmap(Assets.help2, 64, 100);
                break;
            case HELP3:
                g.drawPixmap(Assets.help3, 64, 100);
                break;
        }
        g.drawPixmap(Assets.buttons, 256, 416, 0, help != Help.HELP3 ? 64 : 128, 64, 64); //Help3の場合ボタンをCloseボタンに。
    }

    @Override
    public void pause() {
        //N.A.
    }

    @Override
    public void resume() {
        //N.A.
    }

    @Override
    public void dispose() {
        //N.A.
    }
}
