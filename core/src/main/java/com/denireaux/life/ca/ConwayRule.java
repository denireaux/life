package com.denireaux.life.ca;

public class ConwayRule implements Rule {
    @Override
    public boolean next(boolean alive, int n) {
        return (alive && (n == 2 || n == 3)) || (!alive && n == 3);
    }
}
