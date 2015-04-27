package toru.gamelib;

import toru.game.framework.Game;
import toru.game.framework.Graphics;
import toru.game.framework.Screen;
import toru.game.framework.impl.AndroidSound;

/**
 * Created by Toru on 2015/04/16.
 * Loading assets. start screen.
 */
public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        Assets.background = g.newPixmap("pictures/background.png", Graphics.PixmapFormat.RGB565);
        Assets.logo = g.newPixmap("pictures/logo.png", Graphics.PixmapFormat.ARGB4444);
        Assets.mainMenu = g.newPixmap("pictures/mainmenu.png", Graphics.PixmapFormat.ARGB4444);
        Assets.buttons = g.newPixmap("pictures/buttons.png", Graphics.PixmapFormat.ARGB4444);
        Assets.help1 = g.newPixmap("pictures/help1.png", Graphics.PixmapFormat.ARGB4444);
        Assets.help2 = g.newPixmap("pictures/help2.png", Graphics.PixmapFormat.ARGB4444);
        Assets.help3 = g.newPixmap("pictures/help3.png", Graphics.PixmapFormat.ARGB4444);
        Assets.numbers = g.newPixmap("pictures/numbers.png", Graphics.PixmapFormat.ARGB4444);
        Assets.ready = g.newPixmap("pictures/ready.png", Graphics.PixmapFormat.ARGB4444);
        Assets.pause = g.newPixmap("pictures/pause.png", Graphics.PixmapFormat.ARGB4444);
        Assets.gameOver = g.newPixmap("pictures/gameover.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headUp = g.newPixmap("pictures/headup.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headDown = g.newPixmap("pictures/headdown.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headLeft = g.newPixmap("pictures/headleft.png", Graphics.PixmapFormat.ARGB4444);
        Assets.headRight = g.newPixmap("pictures/headright.png", Graphics.PixmapFormat.ARGB4444);
        Assets.tail = g.newPixmap("pictures/tail.png", Graphics.PixmapFormat.ARGB4444);
        Assets.stain1 = g.newPixmap("pictures/stain1.png", Graphics.PixmapFormat.ARGB4444);
        Assets.stain2 = g.newPixmap("pictures/stain2.png", Graphics.PixmapFormat.ARGB4444);
        Assets.stain3 = g.newPixmap("pictures/stain3.png", Graphics.PixmapFormat.ARGB4444);

        Assets.click = game.getAudio().newSound("sounds/click.mp3");
        Assets.eat = game.getAudio().newSound("sounds/eat.mp3");
        Assets.bitten = game.getAudio().newSound("sounds/bitten.mp3");

        Settings.load(game.getFileIO());
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
