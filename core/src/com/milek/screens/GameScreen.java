package com.milek.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.milek.drop.Drop;

import java.util.Iterator;

public class GameScreen implements Screen {

    private final Drop game;

    private final int SPRITE_SIZE = 64;
    private final int BUCKET_SPEED = 250;

    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;
    private Music rainMusic;

    private Rectangle bucket;

    private Array<Rectangle> raindrops;
    private long lastDropTime;
    private int dropsGathered;

    private OrthographicCamera camera;

    private Vector3 touchPos;


    public GameScreen(final Drop game) {

        this.game = game;

        //load the images 64x64
        dropImage = new Texture(Gdx.files.internal("img/droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("img/bucket.png"));

        //load the sound
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));

        //start playback
        rainMusic.setLooping(true);

        //set camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Drop.WIDTH, Drop.HEIGHT);

        //create bucket rectangle
        bucket = new Rectangle();
        bucket.x = Drop.WIDTH / 2 - SPRITE_SIZE / 2;
        bucket.y = 20;
        bucket.width = SPRITE_SIZE;
        bucket.height = SPRITE_SIZE;

        //create raindrops array and spawn first one
        raindrops = new Array<>();
        spawnRaindrop();

        touchPos = new Vector3();
        dropsGathered = 0;
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, Drop.WIDTH - SPRITE_SIZE);
        raindrop.y = Drop.HEIGHT;
        raindrop.width = SPRITE_SIZE;
        raindrop.height = SPRITE_SIZE;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        rainMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.spriteBatch.setProjectionMatrix(camera.combined);

        game.spriteBatch.begin();
        game.font.draw(game.spriteBatch, "Drops Collected: " + dropsGathered, 0, 480);
        game.spriteBatch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
        for (Rectangle raindrop : raindrops) {
            game.spriteBatch.draw(dropImage, raindrop.x, raindrop.y);
        }
        game.spriteBatch.end();

        // move bucket using touch or mouse hold
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - SPRITE_SIZE / 2;
        }
        //move bucket using arrow keys
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= BUCKET_SPEED * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += BUCKET_SPEED * Gdx.graphics.getDeltaTime();

        //respect screen bounds
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > Drop.WIDTH - SPRITE_SIZE) bucket.x = Drop.WIDTH - SPRITE_SIZE;

        //raindrops spawn every second
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        //raindrops animation
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= BUCKET_SPEED * Gdx.graphics.getDeltaTime();
            if (raindrop.y + SPRITE_SIZE < 0) iter.remove();
            if (raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
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
    public void dispose() {
        dropImage.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
    }

}
