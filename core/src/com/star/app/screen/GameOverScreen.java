package com.star.app.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;


public class GameOverScreen extends AbstractScreen {
    private BitmapFont font72;
    private BitmapFont font24;
    private StringBuilder sb;
    private Hero defeatedHero;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        this.sb = new StringBuilder();
    }

    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    @Override
    public void show() {
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
    }

    public void update(float dt) {
        if (Gdx.input.justTouched()){
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        font72.draw(batch, "Game over", 0, 600, 1280, Align.center, false);
        sb.setLength(0);
        sb.append("SCORE: ").append(defeatedHero.getScoreView()).append("\n");
        sb.append("MONEY: ").append(defeatedHero.getMoney()).append("\n");
        font72.draw(batch, sb, 0, 400, 1280, Align.center, false);
        font24.draw(batch, "Touch screen to return to main menu", 0, 60, 1280, Align.center, false);

        batch.end();
    }

    @Override
    public void dispose() {

    }
}
