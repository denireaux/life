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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LifeItself extends ApplicationAdapter {

    // Start smaller; you can scale later
    private static final int GRID_W = 300;
    private static final int GRID_H = 200;
    private static final int CELL_SIZE = 4; // visible cells

    private OrthographicCamera camera;
    private Viewport viewport;
    private final Vector3 world = new Vector3();

    private SpriteBatch batch;
    private Texture pixel;

    private boolean[][] current;
    private boolean[][] next;

    private boolean running = true;

    private final Color alive = new Color(0.0f, 0.9f, 1.0f, 1f);

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GRID_W * CELL_SIZE, GRID_H * CELL_SIZE, camera);
        viewport.apply();
        camera.position.set((GRID_W * CELL_SIZE) / 2f, (GRID_H * CELL_SIZE) / 2f, 0);
        camera.update();

        batch = new SpriteBatch();

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        pixel = new Texture(pm);
        pm.dispose();

        current = new boolean[GRID_W][GRID_H];
        next = new boolean[GRID_W][GRID_H];

        // Mouse paint (LMB = alive, RMB = dead)
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                paint(Gdx.input.isButtonPressed(Input.Buttons.LEFT));
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                paint(button == Input.Buttons.LEFT);
                return true;
            }
        });
    }

    @Override
    public void render() {
        // controls
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) running = !running;
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) clear();
        if (!running && Gdx.input.isKeyJustPressed(Input.Keys.N)) step();

        if (running) step();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.setColor(alive);
        for (int x = 0; x < GRID_W; x++) {
            for (int y = 0; y < GRID_H; y++) {
                if (current[x][y]) {
                    batch.draw(pixel, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        batch.end();
    }

    private void paint(boolean makeAlive) {
        world.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(world);

        int cx = (int)(world.x / CELL_SIZE);
        int cy = (int)(world.y / CELL_SIZE);

        int brush = 2;
        for (int dx = -brush; dx <= brush; dx++) {
            for (int dy = -brush; dy <= brush; dy++) {
                int x = cx + dx;
                int y = cy + dy;
                if (x >= 0 && x < GRID_W && y >= 0 && y < GRID_H) {
                    current[x][y] = makeAlive;
                }
            }
        }
    }

    private void step() {
        for (int x = 0; x < GRID_W; x++) {
            for (int y = 0; y < GRID_H; y++) {
                int n = neighbors(x, y);
                boolean a = current[x][y];

                // Conway rules
                next[x][y] = (a && (n == 2 || n == 3)) || (!a && n == 3);
            }
        }

        // swap buffers
        boolean[][] tmp = current;
        current = next;
        next = tmp;

        // clear next
        for (int x = 0; x < GRID_W; x++) {
            for (int y = 0; y < GRID_H; y++) next[x][y] = false;
        }
    }

    private int neighbors(int x, int y) {
        int c = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < GRID_W && ny >= 0 && ny < GRID_H && current[nx][ny]) c++;
            }
        }
        return c;
    }

    private void clear() {
        for (int x = 0; x < GRID_W; x++) {
            for (int y = 0; y < GRID_H; y++) current[x][y] = false;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        pixel.dispose();
    }
}
