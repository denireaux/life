package com.denireaux.life.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.denireaux.life.ca.Automaton;

public class GridRenderer {
    private final Texture pixel;
    private final int cellSize;
    private final Color aliveColor;

    public GridRenderer(Texture pixel, int cellSize, Color aliveColor) {
        this.pixel = pixel;
        this.cellSize = cellSize;
        this.aliveColor = aliveColor;
    }

    public void draw(SpriteBatch batch, Automaton automaton) {
        batch.setColor(aliveColor);

        int w = automaton.getWidth();
        int h = automaton.getHeight();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (automaton.isAlive(x, y)) {
                    batch.draw(pixel, x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }
    }
}
