package com.sumain.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConvertUtils {

    public static  <T> T newIfNull(T t, Supplier<T> supplier, Consumer<T> consumer) {
        if (t == null) {
            T t2 = supplier.get();
            consumer.accept(t2);
            return t2;
        }
        return t;
    }

    public static <T> T newIfEmpty(List<T> list, Supplier<T> supplier) {
        if (list.isEmpty())
            list.add(supplier.get());
        return list.getLast();
    }

    public static <T> void setDefaultIfEmpty(T t, Supplier<T> supplier, Consumer<T> consumer) {
        T t1 = null;
        try {
            t1 = supplier.get();

        } catch (Exception ignored) {
        }
        if (t1 == null){
            consumer.accept(t);
            return;
        }
        if (t1 instanceof String && StringUtils.isEmpty((String) t1)){
            consumer.accept(t);
            return;
        }
        if (t1 instanceof Collection && ((Collection<?>) t).isEmpty()){
            consumer.accept(t);
            return;
        }
        if (t1 instanceof Number && ((Number) t1).doubleValue() == 0){
            consumer.accept(t);
            return;
        }
        consumer.accept(t1);
    }

    public static <T> void setIfNotNull(Supplier<T> supplier, Consumer<T> consumer) {
        T t = null;
        try {
            t = supplier.get();
        } catch (Exception ignored) {
        }
        if (t == null)
            return;
        consumer.accept(t);
    }

    public static <F, T> void setIfNotNull(Supplier<F> supplier, Consumer<T> consumer, Function<F, T> parse) {
        F f = null;
        try {
            f = supplier.get();
        } catch (Exception ignored) {
        }
        if (f == null)
            return;
        consumer.accept(parse.apply(f));
    }

    public static <T> void setIfNotEmpty(Supplier<T> supplier, Consumer<T> consumer) {
        T t = null;
        try {
            t = supplier.get();
        } catch (Exception ignored) {
        }
        if (t == null)
            return;
        if (t instanceof String && StringUtils.isEmpty((String) t))
            return;
        if (t instanceof Collection && ((Collection<?>) t).isEmpty())
            return;
        consumer.accept(t);
    }

    public static <T> void setIfNotEmptyAndNotZero(Supplier<T> supplier, Consumer<T> consumer) {
        T t = null;
        try {
            t = supplier.get();
        } catch (Exception ignored) {
        }
        if (t instanceof Number && ((Number) t).doubleValue() == 0) return;
        setIfNotEmpty(supplier, consumer);
    }

    public static <T,P> void setIfNotEmptyAndNotZero(Supplier<T> supplier, Consumer<P> consumer, Function<T, P> parse) {
        T t = null;
        try {
            t = supplier.get();
        } catch (Exception ignored) {
        }
        if (t instanceof Number && ((Number) t).doubleValue() == 0) return;
        setIfNotEmpty(supplier, consumer, parse);
    }

    public static <T, P> void setIfNotEmpty(Supplier<T> supplier, Consumer<P> consumer, Function<T, P> parse) {
        T t = null;
        try {
            t = supplier.get();
        } catch (Exception ignored) {
        }
        if (t == null)
            return;
        if (t instanceof String && StringUtils.isEmpty((String) t))
            return;
        if (t instanceof Collection && ((Collection<?>) t).isEmpty())
            return;
        if (t instanceof Number && ((Number) t).doubleValue() == 0)
            return;
        consumer.accept(parse.apply(t));
    }

    public static int toNullBoolInt(Boolean bool) {
        return bool == null ? 0 : (bool ? 1 : 255);
    }

}
