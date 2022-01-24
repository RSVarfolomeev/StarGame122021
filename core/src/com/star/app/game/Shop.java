package com.star.app.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.screen.utils.Assets;

public class Shop extends Group {
    private Hero hero;
    private BitmapFont font24;
    private int ammoCostScale;
    private int damageCostScale;
    private int weaponCostScale;
    private int magnetCostScale;

    public Shop(final Hero hero) {
        this.hero = hero;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888);
        pixmap.setColor(0, 0, 0.5f, 1);
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);


        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        final TextButton btnClose = new TextButton("X", textButtonStyle);
        final Shop thisShop = this;

        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
                hero.setPause(false);
            }
        });

        btnClose.setTransform(true);
        btnClose.setScale(0.5f);
        btnClose.setPosition(340, 340);
        this.addActor(btnClose);


        final TextButton btnHpMax = new TextButton("HpMax", textButtonStyle);
        btnHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP_MAX.cost)) {
                    if (hero.upgrade(Hero.Skill.HP_MAX)) {
                        hero.decreaseMoney(Hero.Skill.HP_MAX.cost);
                    }
                }
            }
        });

        btnHpMax.setPosition(20, 300);
        this.addActor(btnHpMax);

        final TextButton btnHp = new TextButton("Hp", textButtonStyle);
        btnHp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP.cost)) {
                    if (hero.upgrade(Hero.Skill.HP)) {
                        hero.decreaseMoney(Hero.Skill.HP.cost);
                    }
                }
            }
        });

        btnHp.setPosition(20, 200);
        this.addActor(btnHp);


        final TextButton btnWeapon = new TextButton("Weapon", textButtonStyle);
        btnWeapon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.WEAPON.cost + weaponCostScale)) {
                    if (hero.upgrade(Hero.Skill.WEAPON)) {
                        hero.decreaseMoney(Hero.Skill.WEAPON.cost + weaponCostScale);
                        weaponCostScale = weaponCostScale + 300;
                    }
                }
            }
        });

        btnWeapon.setPosition(20, 100);
        this.addActor(btnWeapon);

        final TextButton btnDamage = new TextButton("+1 Damage", textButtonStyle);
        btnDamage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.DAMAGE.cost + damageCostScale)) {
                    if (hero.upgrade(Hero.Skill.DAMAGE)) {
                        hero.decreaseMoney(Hero.Skill.DAMAGE.cost + damageCostScale);
                        damageCostScale = damageCostScale + 100;
                    }
                }
            }
        });

        btnDamage.setPosition(120, 100);
        this.addActor(btnDamage);

        final TextButton btnAmmo = new TextButton("Ammo", textButtonStyle);
        btnAmmo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.AMMO.cost + ammoCostScale)) {
                    if (hero.upgrade(Hero.Skill.AMMO)) {
                        hero.decreaseMoney(Hero.Skill.AMMO.cost + ammoCostScale);
                        ammoCostScale = ammoCostScale + 2;
                    }
                }
            }
        });

        btnAmmo.setPosition(120, 300);
        this.addActor(btnAmmo);


        final TextButton btnMagnet = new TextButton("Magnet", textButtonStyle);
        btnMagnet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.MAGNET.cost + magnetCostScale)) {
                    if (hero.upgrade(Hero.Skill.MAGNET)) {
                        hero.decreaseMoney(Hero.Skill.MAGNET.cost + magnetCostScale);
                        magnetCostScale = magnetCostScale + 5;
                    }
                }
            }
        });

        btnMagnet.setPosition(120, 200);
        this.addActor(btnMagnet);


        this.setPosition(20, 20);
        this.setVisible(false);
        skin.dispose();
    }
}
