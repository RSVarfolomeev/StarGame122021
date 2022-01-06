package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class PowerUp implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int hpMax;
    private int hp;
    private int coinsBonus;
    private int hpBonus;
    private int ammoBonus;
    private float angle;
    private float rotationSpeed;
    private Circle hitArea;
    private float scale;
    private PowerUpType powerUpType;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public int getHpMax() {
        return hpMax;
    }

    public int getHp() {
        return hp;
    }

    public int getCoinsBonus() {
        return coinsBonus;
    }

    public int getHpBonus() {
        return hpBonus;
    }

    public int getAmmoBonus() {
        return ammoBonus;
    }

    public Circle getHitArea() {
        return hitArea;
    }

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

    public PowerUp(GameController gc) {
        this.powerUpType = null;
        this.gc = gc;
        this.texture = null;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, scale, scale,
                angle);
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;
        if (position.x < -BASE_RADIUS) {
            position.x = ScreenManager.SCREEN_WIDTH + BASE_RADIUS;
        }
        if (position.y < -BASE_RADIUS) {
            position.y = ScreenManager.SCREEN_HEIGHT + BASE_RADIUS;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + BASE_RADIUS) {
            position.x = -BASE_RADIUS;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + BASE_RADIUS) {
            position.y = -BASE_RADIUS;
        }
        hitArea.setPosition(position);
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        int typeNumber = MathUtils.random(1, 3);
        if (typeNumber == 1){
            powerUpType = PowerUpType.COINS;
        }
        if (typeNumber == 2){
            powerUpType = PowerUpType.HP;
        }
        if (typeNumber == 3){
            powerUpType = PowerUpType.AMMO;
        }
        texture = powerUpType.getTexture();
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        hpMax = (int) (1 * scale);
        hp = hpMax;
        coinsBonus = (int) (powerUpType.getCoins() * 2 * scale);
        hpBonus = (int) (powerUpType.getHp() * 2 * scale);
        ammoBonus = (int) (powerUpType.getAmmo() * 2 * scale);
        angle = 0.0f;
        rotationSpeed = 0.0f;
        this.scale = scale;
        hitArea.setPosition(position);
        hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }
}
