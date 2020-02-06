package com.epam.jdi.mobile.common;

import com.epam.jdi.mobile.settings.WebSettings;
import com.epam.jdi.tools.Safe;

import static com.epam.jdi.mobile.common.Exceptions.exception;

/**
 * Created by Roman Iovlev on 26.09.2019
 * Email: roman.iovlev.jdi@gmail.com; Skype: roman.iovlev
 */
public class Timeout {
    private int INITIAL;
    private Safe<Integer> DEFAULT;
    public Safe<Integer> current;
    private Safe<Integer> freeze;

    public Timeout(int timeoutSec) {
        INITIAL = timeoutSec;
        DEFAULT = new Safe<>(timeoutSec);
        current = new Safe<>(timeoutSec);
        freeze = new Safe<>(0);
    }
    public int get() {
        int result = current.get();
        reset();
        return result;
    }
    public void freeze() {
        freeze.update(v->v+1);
    }
    public void unfreeze() {
        if (freeze.get() > 0)
            freeze.update(v->v-1);
        reset();
    }
    public void setUp(int seconds) {
        DEFAULT.set(seconds);
        current.set(seconds);
        WebSettings.logger.info("Setup timeout " + seconds + " seconds");
    }
    public void set(int seconds) {
        if (freeze.get() > 0) return;
        if (freeze.get() < 0) throw exception("Timeout freeze broken");
        current.set(seconds);
        WebSettings.logger.info("Set timeout " + seconds + " seconds");
    }
    public void reset() {
        if (freeze.get() > 0) return;
        if (freeze.get() < 0) throw exception("Timeout freeze broken");
        current.set(DEFAULT.get());
    }
    public void drop() {
        freeze.set(0);
        current.set(INITIAL);
        DEFAULT.set(INITIAL);
    }
}