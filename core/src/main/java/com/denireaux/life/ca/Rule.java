package com.denireaux.life.ca;

public interface Rule {
    boolean next(boolean alive, int neighbors);
}
