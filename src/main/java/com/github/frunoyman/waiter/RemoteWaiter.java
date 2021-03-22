package com.github.frunoyman.waiter;

import com.github.frunoyman.controllers.BaseSdk;
import com.github.frunoyman.exceptions.TimeoutException;


import java.time.Clock;
import java.time.Duration;

public class RemoteWaiter extends FluentWait<BaseSdk> {
    private final BaseSdk remoteSdk;

    public RemoteWaiter(BaseSdk remoteSdk, long timeOutInSeconds) {
        this(remoteSdk, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, 1000L);
    }

    public RemoteWaiter(BaseSdk remoteSdk, long timeOutInSeconds, long sleepInMillis) {
        this(remoteSdk, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER, timeOutInSeconds, sleepInMillis);
    }

    public RemoteWaiter(BaseSdk remoteSdk, Clock clock, Sleeper sleeper, long timeOutInSeconds, long sleepTimeOut) {
        super(remoteSdk, clock, sleeper);
        this.withTimeout(Duration.ofSeconds(timeOutInSeconds));
        this.pollingEvery(Duration.ofMillis(sleepTimeOut));
        this.remoteSdk = remoteSdk;
    }

    protected RuntimeException timeoutException(String message, Throwable lastException) {
        BaseSdk exceptionDriver = this.remoteSdk;
        TimeoutException ex = new TimeoutException(message, lastException);
        throw ex;
    }




}
