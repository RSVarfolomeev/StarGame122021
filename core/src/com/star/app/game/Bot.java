package com.star.app.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {
    private boolean active;
    private Vector2 tempVec;
    private float tempBotAngle;
    protected Circle guardArea;
    private boolean heroInTarget;
    protected float angleToHero;
    private float accuracy;

    @Override
    public boolean isActive() {
        return active;
    }

    public Bot(GameController gc) {
        super(gc, 10, 100);
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.tempVec = new Vector2(0, 0);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.hitArea = new Circle(position, 29);
        this.ownerType = OwnerType.BOT;
        this.guardArea = new Circle(position, (ScreenManager.SCREEN_HEIGHT + ScreenManager.SCREEN_WIDTH) / 4);
        this.active = false;
        this.heroInTarget = false;
        this.angleToHero = 90.0f;
        this.tempBotAngle = 0.0f;
        this.accuracy = 1.0f;
    }

    public void activate(float x, float y, float angle) {
        position.set(x, y);
        this.angle = angle;

        int maxWeaponlvl;
        if (gc.getLevel() < 9) {
            enginePower = 50 * (gc.getLevel() + 1);
            maxWeaponlvl = (int) ((gc.getLevel() - 1) * 0.5f);
        } else {
            enginePower = 500;
            maxWeaponlvl = 4;
        }

        int randomWeaponType = MathUtils.random(0, maxWeaponlvl);
        currentWeapon = weapons[randomWeaponType];
        accuracy = MathUtils.random(0.2f, 2.0f);
        currentWeapon.addAmmos(currentWeapon.getMaxBullets());

        float lvlHpScale = 0.125f;
        if (gc.getLevel() >= 11){
            lvlHpScale = 0.250f;
        }
        if (gc.getLevel() >= 15){
            lvlHpScale = 0.5f;
        }
        if (gc.getLevel() >= 19){
            currentWeapon.setDamage(currentWeapon.getDamage() + 1);
        }
        if (gc.getLevel() >= 23){
            lvlHpScale = 1.0f;
        }

        hpMax = (int) (10 * gc.getLevel() * ((float)(4 - randomWeaponType) * lvlHpScale + 1));
        hp = hpMax;
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        super.update(dt);
        guardArea.setPosition(position);

        if (!isAlive()) {
            deactivate();
            gc.getParticleController().getEffectBuilder().botDestroyEffect(position.x, position.y);
            if (gc.getLevel() <= 4) {
                gc.getPowerUpsController().setupWithType(position.x, position.y, 0.67f, PowerUp.Type.AMMOS);
            }
            for (int i = 0; i < 3; i++) {
                gc.getPowerUpsController().setup(position.x, position.y, 0.5f);
            }
        }

        if (guardArea.contains(gc.getHero().getPosition())) {
            heroInTarget = true;
        }

        if (heroInTarget == true && gc.getHero().getPosition().dst(position) > 300) {
            accelerate(dt);

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f, 1.2f, 0.2f,
                        1.0f, 0.0f, 0, 1,
                        1, 0.5f, 0, 0);
            }
        }

        if (heroInTarget == true) {
            tempVec.set(gc.getHero().getPosition()).sub(position).mulAdd(gc.getHero().getVelocity(), accuracy).nor();
            angleToHero = tempVec.angleDeg();

            if (angle >= 0) {
                tempBotAngle = angle % 360;
            } else {
                tempBotAngle = 360 + angle % 360;
            }

            if ((angleToHero - tempBotAngle < 180.0f && angleToHero - tempBotAngle > 0.0f) || tempBotAngle - angleToHero > 180.0f) {
                angle += 210.0f * dt;
            }

            if ((tempBotAngle - angleToHero < 180.0f && tempBotAngle - angleToHero > 0.0f) || angleToHero - tempBotAngle > 180.0f) {
                angle -= 210.0f * dt;
            }

            if (angleToHero - tempBotAngle >= -2.0f && angleToHero - tempBotAngle <= 2.0f) {
                tryToFire();
            }
        }
    }

}
