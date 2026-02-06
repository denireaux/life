package com.denireaux.life;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.denireaux.life.ca.Automaton;
import com.denireaux.life.ca.ConwayRule;
import com.denireaux.life.input.PaintController;
import com.denireaux.life.render.GridRenderer;

public class LifeItself extends ApplicationAdapter {

    private static final int GRID_W = 300;
    private static final int GRID_H = 200;
    private static final int CELL_SIZE = 4;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private Texture pixel;

    private Automaton automaton;
    private GridRenderer renderer;
    private PaintController painter;

    private boolean running = true;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GRID_W * CELL_SIZE, GRID_H * CELL_SIZE, camera);
        viewport.apply();
        camera.position.set((GRID_W * CELL_SIZE) / 2f, (GRID_H * CELL_SIZE) / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        pixel = make1x1Pixel();

        automaton = new Automaton(GRID_W, GRID_H, new ConwayRule());
        renderer = new GridRenderer(pixel, CELL_SIZE, new Color(0.0f, 0.9f, 1.0f, 1f));
        painter = new PaintController(viewport, CELL_SIZE);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                automatonPaint();
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                automatonPaint(button == Input.Buttons.LEFT);
                return true;
            }
        });
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) running = !running;
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) automaton.clear();
        if (!running && Gdx.input.isKeyJustPressed(Input.Keys.N)) automaton.step();

        if (running) automaton.step();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderer.draw(batch, automaton);
        batch.end();
    }

    private void automatonPaint() {
        // drag uses “left if down else erase”
        automatonPaint(painter.isLeftDown());
    }

    private void automatonPaint(boolean makeAlive) {
        painter.paintFromMouse(automaton, makeAlive);
    }

    private Texture make1x1Pixel() {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    @Override
    public void dispose() {
        batch.dispose();
        pixel.dispose();
    }
}
