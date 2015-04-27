package toru.gamelib;

import java.util.List;

import toru.game.framework.Game;
import toru.game.framework.Graphics;
import toru.game.framework.Input;
import toru.game.framework.Input.TouchEvent;
import toru.game.framework.Screen;

/**
 * Created by Toru on 2015/04/16.
 */
public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents(); //KeyEventsは何もしないが、たまっていくので捨てていく。

        int len = touchEvents.size();

        for(int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)){
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                }
                if(inBounds(event, 64, 220, 192, 42)) {
                    game.setScreen(new GameScreen(game));
                    if(Settings.soundEnabled){
                        Assets.click.play(1);
                    }
                    return;
                }
                if(inBounds(event, 64, 220 + 42, 192, 42)) {
                    game.setScreen(new HighScoreScreen(game));
                    if(Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
                if(inBounds(event, 64, 220 + 84, 192, 42)) {
                    game.setScreen(new HelpScreen(game));
                    if(Settings.soundEnabled) {
                        Assets.click.play(1);
                    }
                    return;
                }
            }
        }
    }

    protected boolean inBounds(TouchEvent event, int x, int y, int width, int height){
        if(event.x > x && event.y > y && event.x < x + width - 1 && event.y < y + height - 1) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.background, 0, 0);
        g.drawPixmap(Assets.logo, 32, 20);
        g.drawPixmap(Assets.mainMenu, 64, 220);
        if(Settings.soundEnabled) {
            g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
        } else {
            g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
        }
    }

    @Override
    public void pause() {
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
