package com.star.app.game;

import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Asteroid implements Poolable {
    private GameController gc;
    Vector2 position;
    Vector2 velocity;
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Asteroid(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void update(float dt) {
        position.x += (velocity.x - gc.getHero().getVelocity().x * 0.5) * dt;
        position.y += (velocity.y - gc.getHero().getVelocity().y * 0.5) * dt;

        if (position.x <= -300 || position.y <= -300 || position.x >= ScreenManager.SCREEN_WIDTH + 300 ||
                position.y >= ScreenManager.SCREEN_HEIGHT + 300) {
            deactivate();
        }
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }
}
