package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private Hero hero;
    private BotController botController;
    private Vector2 tempVec;
    private Stage stage;
    private boolean pause;
    private int level;
    private float timer;
    private Music music;
    private StringBuilder sb;

    public float getTimer() {
        return timer;
    }

    public int getLevel() {
        return level;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Stage getStage() {
        return stage;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public Hero getHero() {
        return hero;
    }

    public BotController getBotController() {
        return botController;
    }

    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.botController = new BotController(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.infoController = new InfoController();
        this.tempVec = new Vector2();
        this.level = 1;
        this.sb = new StringBuilder();
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.setVolume(0.4f);
        this.music.play();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);

        generateBigAsteroids(1);
        generateBots(1);
    }

    public void generateBigAsteroids(int n) {
        for (int i = 0; i < n; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200),
                    MathUtils.random(-200, 200), 1.0f);
        }
    }

    public void generateBots(int n) {
        for (int i = 0; i < n; i++) {
            botController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    0, 0, 90.0f, 2.0f);
        }
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        timer += dt;
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        powerUpsController.update(dt);
        infoController.update(dt);
        botController.update(dt);
        checkCollisions();
        botBehavior();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        if (asteroidController.getActiveList().size() == 0) {
            level++;
            generateBigAsteroids(level <= 3 ? level : 3);
            timer = 0.0f;
        }
        stage.act(dt);
    }

    private void checkCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2;
                tempVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tempVec, halfOverLen);
                a.getPosition().mulAdd(tempVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius + a.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVec, a.getHitArea().radius / sumScl * 100);
                a.getVelocity().mulAdd(tempVec, -hero.getHitArea().radius / sumScl * 100);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 50);
                }
                hero.takeDamage(level * 2);
                sb.setLength(0);
                sb.append("HP -").append(level * 2);
                infoController.setup(hero.getPosition().x, hero.getPosition().y, sb.toString(), Color.RED);
            }
        }


        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition()) && b.getOwner() == Bullet.Owner.HERO) {

                    particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f, 2.2f, 1.5f,
                            1.0f, 1.0f, 1.0f, 1,
                            0, 0, 1, 0);


                    b.deactivate();
                    if (a.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        hero.addScore(a.getHpMax() * 100);
                        if(MathUtils.random(0, 100) >= 95){
                            botController.setup(a.getPosition().x, a.getPosition().y, 0, 0, MathUtils.random(0.0f, 360.0f), 2.0f);
                        }
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y,
                                    a.getScale() * 0.25f);
                        }
                    }
                    break;
                }
            }
            for (int k = 0; k < botController.getActiveList().size(); k++) {
                Bot bot = botController.getActiveList().get(k);
                if (bot.getHitArea().contains(b.getPosition()) && b.getOwner() == Bullet.Owner.HERO) {
                    particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f, 2.2f, 1.5f,
                            1.0f, 1.0f, 1.0f, 1,
                            0, 0, 1, 0);
                    b.deactivate();
                    bot.takeDamage(hero.getCurrentWeapon().getDamage());
                    if (!bot.isAlive()) {
                        bot.deactivate();
                        particleController.getEffectBuilder().takeBotDestroyEffect(bot.getPosition().x, bot.getPosition().y);
                    }
                    break;
                }

                if (hero.getHitArea().contains(b.getPosition()) && b.getOwner() == Bullet.Owner.BOT) {
                    particleController.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f, 2.2f, 1.5f,
                            1.0f, 1.0f, 1.0f, 1,
                            0, 0, 1, 0);
                    b.deactivate();
                    hero.takeDamage(bot.getCurrentWeapon().getDamage());
                    break;
                }
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getMagneticField().contains(p.getPosition())) {
                tempVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tempVec, 100);
            }

            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
            }
        }

    }

    public void botBehavior() {
        for (int i = 0; i < botController.getActiveList().size(); i++) {
            Bot b = botController.getActiveList().get(i);
            if (b.getGuardArea().contains(hero.getPosition())) {
                b.setHeroInTarget(true);
            }

            if (b.isHeroInTarget() == true) {
                float dst = b.getPosition().dst(hero.getPosition());
                Vector2 tempBotVec = new Vector2();
                b.setMoveToHero(true);
                float angleToHero = tempBotVec.set(hero.getPosition()).sub(b.getPosition()).angleDeg();
                b.setAngleToHero(angleToHero);

                if (dst >= 300.0f) {
                    b.setMoveToHero(true);
                } else {
                    b.setMoveToHero(false);
                }

                if (angleToHero - b.getAngle() >= -2.0f && angleToHero - b.getAngle() <= 2.0f ) {
                    b.setFireToHero(true);
                } else {
                    b.setFireToHero(false);
                }
            }
        }
    }

    public void dispose() {
        background.dispose();
    }
}
