package toru.game.framework;

/**
 * Created by Toru on 2015/04/06.
 */
public interface Pixmap {
    public int getWidth();
    public int getHeight();
    public Graphics.PixmapFormat getFormat();
    public void dispose();

}
