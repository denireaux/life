package com.denireaux.life.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.denireaux.life.ca.Automaton;

public class PaintController {
    private final Viewport viewport;
    private final int cellSize;
    private final Vector3 world = new Vector3();

    private int brushRadius = 2;

    public PaintController(Viewport viewport, int cellSize) {
        this.viewport = viewport;
        this.cellSize = cellSize;
    }

    public void setBrushRadius(int r) {
        brushRadius = Math.max(0, r);
    }

    public void paintFromMouse(Automaton automaton, boolean makeAlive) {
        world.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(world);

        int cx = (int)(world.x / cellSize);
        int cy = (int)(world.y / cellSize);

        for (int dx = -brushRadius; dx <= brushRadius; dx++) {
            for (int dy = -brushRadius; dy <= brushRadius; dy++) {
                int x = cx + dx;
                int y = cy + dy;

                if (x >= 0 && x < automaton.getWidth() && y >= 0 && y < automaton.getHeight()) {
                    automaton.setAlive(x, y, makeAlive);
                }
            }
        }
    }

    public boolean isLeftDown() {
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    }
}
