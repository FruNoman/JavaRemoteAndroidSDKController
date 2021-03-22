package com.github.frunoyman.waiter;

import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Function<F, T> extends java.util.function.Function<F, T> {

    T apply(F var1);

    boolean equals(Object var1);
}