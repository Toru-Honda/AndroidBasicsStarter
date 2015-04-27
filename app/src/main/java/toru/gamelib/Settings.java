package toru.gamelib;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import toru.game.framework.FileIO;

/**
 * Created by Toru on 2015/04/16.
 * Reads settings file ".mrnon".
 * Writes settings file under external storage as ".mrnon".
 */
public class Settings {
    public static boolean soundEnabled = true;
    public static int[] highscores = new int[] {100, 80, 50, 30, 10};

    public Settings() {
    }

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(".mrnon")));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for(int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e){
            //エラー時はデフォルト
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            }catch (IOException e) {
                Log.d("Settings", e.getMessage());
            }
        }
    }

    public static boolean save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".mrnon")));
            out.write(Boolean.toString(soundEnabled));
            for(int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
            }
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e){
                Log.d("Settings", e.getMessage());
                return false;
            }
            return true;
        }
    }

    public static void addScore(int score) {
        for(int i = 0; i < 5; i++) {
            if(highscores[i] < score) {
                for(int j = 4; j > i; j--) {
                    highscores[j] = highscores[j-1];
                }
                highscores[i] = score;
                break;
            }
        }
    }
}
