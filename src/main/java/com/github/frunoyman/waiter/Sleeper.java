package com.github.frunoyman.waiter;

import java.time.Duration;

public interface Sleeper {
   Sleeper SYSTEM_SLEEPER = (duration) -> {
        Thread.sleep(duration.toMillis());
    };

    void sleep(Duration var1) throws InterruptedException;
}
