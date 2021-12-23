package com.star.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {

    private StarGame starGame;
    private Texture textureAsteroid;
    Vector2 position;
    Vector2 velocity;


    public Asteroid(StarGame starGame) {
        this.starGame = starGame;
        this.textureAsteroid = new Texture("asteroid.png");
        this.position = new Vector2(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                MathUtils.random(0, ScreenManager.SCREEN_HEIGHT));
        this.velocity = new Vector2(MathUtils.random(-100, -50), MathUtils.random(50, 100));
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureAsteroid, position.x - 128, position.y - 128, 128, 128, 256, 256,
                1, 1, 0, 0, 0, 256, 256, false, false);
    }

    public void update(float dt) {
        position.x += (velocity.x - starGame.getHero().getLastDisplacement().x * 30) * dt;
        position.y += (velocity.y - starGame.getHero().getLastDisplacement().y * 30) * dt;

        if (position.x < -200) {
            position.x = ScreenManager.SCREEN_WIDTH + 200;
            position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + 200) {
            position.x = MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200);
            position.y = -200;
        }
    }
}
