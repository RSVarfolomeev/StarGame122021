package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.utils.Assets;

public class Weapon {
    private GameController gc;
    private Ship ship;

    private String title;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;
    private int bulletRate;
    private Sound shootSound;

    // x - растояние от центра
    // y угол от основного направления корабля
    // z угол стрельбы
    private Vector3[] slots;

    public float getFirePeriod() {
        return firePeriod;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public String getTitle() {
        return title;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public Weapon(GameController gc, Ship ship, String title, float firePeriod, int bulletRate, int damage,
                  float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.ship = ship;
        this.title = title;
        this.bulletRate = bulletRate;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.slots = slots;
        this.curBullets = maxBullets;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/shoot.mp3");
    }

    public void fire() {
        if (curBullets - bulletRate >= 0) {
            curBullets = curBullets - bulletRate;
            shootSound.play(0.7f);
            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = ship.getPosition().x + MathUtils.cosDeg(ship.getAngle() + slots[i].y) * slots[i].x;
                y = ship.getPosition().y + MathUtils.sinDeg(ship.getAngle() + slots[i].y) * slots[i].x;
                vx = ship.getVelocity().x + bulletSpeed * MathUtils.cosDeg(ship.getAngle() + slots[i].z);
                vy = ship.getVelocity().y + bulletSpeed * MathUtils.sinDeg(ship.getAngle() + slots[i].z);
                gc.getBulletController().setup(ship, x, y, vx, vy);
            }
        }
    }

    public void addAmmos(int amount) {
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
    }
}
