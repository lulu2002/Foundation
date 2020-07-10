package org.mineacademy.fo.model.timer;

public abstract class SecondTimer implements Timer {

    @Override
    public int runTick() {
        return 20;
    }
}
