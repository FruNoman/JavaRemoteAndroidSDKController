package com.github.frunoyman.waiter;

import java.util.function.Function;

public interface Wait<F> {
    <T> T until(Function<? super F, T> var1);
}