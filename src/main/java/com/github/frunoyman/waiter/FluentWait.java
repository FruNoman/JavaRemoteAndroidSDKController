package com.github.frunoyman.waiter;


import com.github.frunoyman.exceptions.TimeoutException;
import com.google.common.collect.ImmutableList;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

public class FluentWait<T> implements Wait<T> {
    protected static final long DEFAULT_SLEEP_TIMEOUT = 1000L;
    /** @deprecated */
    @Deprecated
    public static final Duration FIVE_HUNDRED_MILLIS = Duration.ofMillis(1000L);
    private static final Duration DEFAULT_WAIT_DURATION = Duration.ofMillis(1000L);
    private final T input;
    private final Clock clock;
    private final Sleeper sleeper;
    private Duration timeout;
    private Duration interval;
    private Supplier<String> messageSupplier;
    private List<Class<? extends Throwable>> ignoredExceptions;

    public FluentWait(T input) {
        this(input, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER);
    }

    public FluentWait(T input, Clock clock, Sleeper sleeper) {
        this.timeout = DEFAULT_WAIT_DURATION;
        this.interval = DEFAULT_WAIT_DURATION;
        this.messageSupplier = () -> {
            return null;
        };
        this.ignoredExceptions = new ArrayList();
        this.input = Objects.requireNonNull(input);
        this.clock = (Clock)Objects.requireNonNull(clock);
        this.sleeper = (Sleeper)Objects.requireNonNull(sleeper);
    }

    /** @deprecated */
    @Deprecated
    public FluentWait<T> withTimeout(long duration, TimeUnit unit) {
        return this.withTimeout(Duration.of(duration, this.toChronoUnit(unit)));
    }

    public FluentWait<T> withTimeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    public FluentWait<T> withMessage(String message) {
        this.messageSupplier = () -> {
            return message;
        };
        return this;
    }

    public FluentWait<T> withMessage(Supplier<String> messageSupplier) {
        this.messageSupplier = messageSupplier;
        return this;
    }

    /** @deprecated */
    @Deprecated
    public FluentWait<T> pollingEvery(long duration, TimeUnit unit) {
        return this.pollingEvery(Duration.of(duration, this.toChronoUnit(unit)));
    }

    public FluentWait<T> pollingEvery(Duration interval) {
        this.interval = interval;
        return this;
    }

    public <K extends Throwable> FluentWait<T> ignoreAll(Collection<Class<? extends K>> types) {
        this.ignoredExceptions.addAll(types);
        return this;
    }

    public FluentWait<T> ignoring(Class<? extends Throwable> exceptionType) {
        return this.ignoreAll(ImmutableList.of(exceptionType));
    }

    public FluentWait<T> ignoring(Class<? extends Throwable> firstType, Class<? extends Throwable> secondType) {
        return this.ignoreAll(ImmutableList.of(firstType, secondType));
    }

    public <V> V until(Function<? super T, V> isTrue) {
        Instant end = this.clock.instant().plus(this.timeout);

        while(true) {
            Throwable lastException;
            try {
                V value = isTrue.apply(this.input);
                if (value != null && (Boolean.class != value.getClass() || Boolean.TRUE.equals(value))) {
                    return value;
                }

                lastException = null;
            } catch (Throwable var7) {
                lastException = this.propagateIfNotIgnored(var7);
            }

            if (end.isBefore(this.clock.instant())) {
                String message = this.messageSupplier != null ? (String)this.messageSupplier.get() : null;
                String timeoutMessage = String.format("Expected condition failed: %s (tried for %d second(s) with %d milliseconds interval)", message == null ? "waiting for " + isTrue : message, this.timeout.getSeconds(), this.interval.toMillis());
                throw this.timeoutException(timeoutMessage, lastException);
            }

            try {
                this.sleeper.sleep(this.interval);
            } catch (InterruptedException var6) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(var6);
            }
        }
    }

    private Throwable propagateIfNotIgnored(Throwable e) {
        Iterator var2 = this.ignoredExceptions.iterator();

        Class ignoredException;
        do {
            if (!var2.hasNext()) {
//                Throwables.throwIfUnchecked(e);
                throw new RuntimeException(e);
            }

            ignoredException = (Class)var2.next();
        } while(!ignoredException.isInstance(e));

        return e;
    }

    protected RuntimeException timeoutException(String message, Throwable lastException) {
        throw new TimeoutException(message, lastException);
    }

    private ChronoUnit toChronoUnit(TimeUnit timeUnit) {
        switch(timeUnit) {
            case NANOSECONDS:
                return ChronoUnit.NANOS;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case SECONDS:
                return ChronoUnit.SECONDS;
            case MINUTES:
                return ChronoUnit.MINUTES;
            case HOURS:
                return ChronoUnit.HOURS;
            case DAYS:
                return ChronoUnit.DAYS;
            default:
                throw new IllegalArgumentException("No ChronoUnit equivalent for " + timeUnit);
        }
    }
}
