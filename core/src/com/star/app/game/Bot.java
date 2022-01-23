package com.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {

    private boolean active;
    protected Circle guardArea;
    private boolean heroInTarget;
    private boolean moveToHero;
    private boolean fireToHero;
    protected float angleToHero;

    public Circle getGuardArea() {
        return guardArea;
    }

    public boolean isHeroInTarget() {
        return heroInTarget;
    }

    public boolean isFireToHero() {
        return fireToHero;
    }

    public void setFireToHero(boolean fireToHero) {
        this.fireToHero = fireToHero;
    }

    public void setMoveToHero(boolean moveToHero) {
        this.moveToHero = moveToHero;
    }

    public void setHeroInTarget(boolean heroInTarget) {
        this.heroInTarget = heroInTarget;
    }

    public void setAngleToHero(float angleToHero) {
        this.angleToHero = angleToHero;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Bot(GameController gc) {
        super(gc, 100, 500);
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.hitArea = new Circle(position, 29);
        this.guardArea = new Circle(position, (ScreenManager.SCREEN_HEIGHT + ScreenManager.SCREEN_WIDTH) / 4);
        this.active = false;
        this.heroInTarget = false;
        this.moveToHero = false;
        this.fireToHero = false;
        this.angleToHero = 90.0f;
    }

    public void update(float dt) {
        super.update(dt);
        guardArea.setPosition(position);

        if (moveToHero == true) {
            moveToHero(dt);
        }

        if (fireToHero == true) {
            tryToFire();
        }

        if (angleToHero > angle) {
            angle += 180.0f * dt;
        }
        if (angleToHero < angle) {
            angle -= 180.0f * dt;
        }
    }

    public void moveToHero(float dt) {
        velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
        velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;

        float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
        float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
        for (int i = 0; i < 3; i++) {
            gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                    velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                    0.5f, 1.2f, 0.2f,
                    1.0f, 0.5f, 0, 1,
                    1, 1, 1, 0);
        }
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy, float angle, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
        hpMax = (int) (10 * scale * gc.getLevel());
        hp = hpMax;
        currentWeapon = weapons[MathUtils.random(0, (int)((gc.getLevel() - 1) * 0.5f))];
        this.angle = angle;
    }
}
