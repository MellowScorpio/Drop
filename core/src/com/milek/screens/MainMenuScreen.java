package com.milek.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.milek.drop.Drop;

public class MainMenuScreen implements Screen {

    final Drop game;

    OrthographicCamera camera;

    public MainMenuScreen(final Drop game){
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Drop.WIDTH, Drop.HEIGHT);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.spriteBatch.begin();
        game.font.draw(game.spriteBatch,"Welcome to Drop!!!", 100, 150);
        game.font.draw(game.spriteBatch, "Tap anywhere to begin!", 100, 100);
        game.spriteBatch.end();

        if(Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose(){

    }
}
