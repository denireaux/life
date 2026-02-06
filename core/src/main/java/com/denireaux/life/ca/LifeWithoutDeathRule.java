package com.denireaux.life.ca;

public class LifeWithoutDeathRule implements Rule {
    @Override
    public boolean next(boolean alive, int neighbors) {
        return alive || neighbors == 3;
    }
}
