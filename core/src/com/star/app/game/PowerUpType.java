package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.screen.utils.Assets;

public enum PowerUpType {
    COINS(50, 0, 0, Assets.getInstance().getAtlas().findRegion("powerupcoin")),
    HP(0, 50, 0, Assets.getInstance().getAtlas().findRegion("poweruphp")),
    AMMO(0, 0, 100, Assets.getInstance().getAtlas().findRegion("powerupammo"));

    private int coins;
    private int hp;
    private int ammo;
    private TextureRegion texture;

    PowerUpType(int coins, int hp, int ammo, TextureRegion texture) {
        this.coins = coins;
        this.hp = hp;
        this.ammo = ammo;
        this.texture = texture;
    }

    public int getCoins() {
        return coins;
    }

    public int getHp() {
        return hp;
    }

    public int getAmmo() {
        return ammo;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
