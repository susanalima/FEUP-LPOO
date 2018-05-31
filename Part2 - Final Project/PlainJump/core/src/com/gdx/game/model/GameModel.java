package com.gdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class GameModel {

    private static final String HS_DATA_FILE = "data/pj-v1.json";

    private static GameModel instance = null;

    private Integer highscore;
    private Color ballColor;
    private Color plainColor;

    public GameModel() {
        highscore = 0;
        ballColor = Color.BLUE;
        plainColor = Color.BROWN;
    }

    public static GameModel getInstance() {
        if(instance == null)
            instance = new GameModel();
        return instance;
    }

    public int getHighscore() {
        return highscore;
    }

    public Color getBallColor() {
        return ballColor;
    }

    public Color getPlainColor() {
        return plainColor;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
        saveSettings();
    }

    public void setBallColor(Color ballColor) {
        this.ballColor = ballColor;
        saveSettings();
        System.out.println("\tBall set");
    }

    public void setPlainColor(Color plainColor) {
        this.plainColor = plainColor;
        saveSettings();
        System.out.println("\tPlain set");
    }

    public void saveSettings() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle file = Gdx.files.local(HS_DATA_FILE);
        System.out.println(json.toJson(this));
        file.writeString(Base64Coder.encodeString(json.toJson(this)), false);
    }

    public void loadSettings() {
        Json json = new Json();
        FileHandle file = Gdx.files.local(HS_DATA_FILE);
        GameModel tmp;
        try {
            tmp = json.fromJson(GameModel.class, Base64Coder.decodeString(file.readString()));
            if(tmp != null) {
                highscore = tmp.getHighscore();
                ballColor = tmp.getBallColor();
                plainColor = tmp.getPlainColor();
            }
        } catch (GdxRuntimeException rt) {
        }
    }

}
