package toru.game.framework.impl;

import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import toru.game.framework.FileIO;

/**
 * Created by Toru on 2015/04/06.
 * Implementations of FileIO interface as Android games.
 * @author Toru
 */
public class AndroidFileIO implements FileIO {
    protected AssetManager assets;
    protected final String externalStoragePath;

    /**
     * Initialize assets field and external storage's absolute path.
     * @param assets AssetManager object.
     */
    public AndroidFileIO(AssetManager assets) {
        this.assets = assets;
        this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * open asset's file.
     * @param fileName relative path in assets.
     * @return InputStream instance.
     * @throws IOException if path isn't exist, throw IOException.
     */
    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return assets.open(fileName);
    }

    /**
     * open external storage file as input stream.
     * @param fileName relative path under external storage.
     * @return InputStream instance.
     * @throws IOException if path isn't exist, throw IOException.
     */
    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    /**
     * open external storage file as output stream.
     * @param fileName relative path under external storage.
     * @return OutputStream instance.
     * @throws IOException if path isn't exist, throw IOException.
     */
    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }
}
