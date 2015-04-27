package toru.game.framework;

/**
 * Created by Toru on 2015/04/05.
 * This interface is responsible for instantiating Music or Sound objects.
 */
public interface Audio {
    /**
     *
     * @param filename
     * @return
     */
    public Music newMusic(String filename);
    public Sound newSound(String filename);
}
