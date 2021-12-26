package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.ScreenManager;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Texture asteroidTexture;
    private GameController gc;
    private float asteroidTimer;

    @Override
    protected Asteroid newObject() {
        return new Asteroid(gc);
    }

    public AsteroidController(GameController gc) {
        this.asteroidTexture = new Texture("asteroid.png");
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = activeList.get(i);
            batch.draw(asteroidTexture, a.getPosition().x - 128, a.getPosition().y - 128);
        }
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }

    public void update(float dt) {
        asteroidTimer += dt;
        int randomBorderPosition;
        if (asteroidTimer > 1.5f) {
            asteroidTimer = 0.0f;
            randomBorderPosition = MathUtils.random(0, ScreenManager.SCREEN_WIDTH + ScreenManager.SCREEN_HEIGHT);
            if (randomBorderPosition > ScreenManager.SCREEN_WIDTH) {
                setup(ScreenManager.SCREEN_WIDTH + 200,
                        randomBorderPosition - ScreenManager.SCREEN_WIDTH - 200,
                        MathUtils.random(-100, -50), MathUtils.random(50, 100));
            }
            if (randomBorderPosition <= ScreenManager.SCREEN_WIDTH) {
                setup(randomBorderPosition + 200, -200,
                        MathUtils.random(-100, -50), MathUtils.random(50, 100));
            }
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
