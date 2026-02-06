package com.denireaux.life.ca;

public class Automaton {
    private final int width;
    private final int height;

    private boolean[][] current;
    private boolean[][] next;

    private Rule rule;

    public Automaton(int width, int height, Rule rule) {
        this.width = width;
        this.height = height;
        this.rule = rule;

        this.current = new boolean[width][height];
        this.next = new boolean[width][height];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isAlive(int x, int y) { return current[x][y]; }
    public void setAlive(int x, int y, boolean alive) { current[x][y] = alive; }

    public void clear() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                current[x][y] = false;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public void step() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int n = countNeighbors(x, y);
                next[x][y] = rule.next(current[x][y], n);
            }
        }

        // swap
        boolean[][] tmp = current;
        current = next;
        next = tmp;

        // clear next
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                next[x][y] = false;
    }

    private int countNeighbors(int x, int y) {
        int c = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && current[nx][ny]) c++;
            }
        }
        return c;
    }
}
