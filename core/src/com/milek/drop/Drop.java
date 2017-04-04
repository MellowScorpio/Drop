package com.milek.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.milek.screens.MainMenuScreen;

public class Drop extends Game {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

    public SpriteBatch spriteBatch;
    public BitmapFont font;

    @Override
    public void create() {
        // create sprite batch
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }
}
