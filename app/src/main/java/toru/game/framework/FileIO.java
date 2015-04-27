package toru.game.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Toru on 2015/04/05.
 * This interface is responsible for creating <pre>InputStream</pre> or <pre>OutputStream</pre>.
 */
public interface FileIO {

    public InputStream readAsset(String fileName) throws IOException;
    public InputStream readFile(String fileName) throws IOException;
    public OutputStream writeFile(String fileName) throws IOException;
}
