package com.example.demo.vaildator;

public interface CustValidator<I, R> {
    boolean support(Class<?> clazz);
    R validate(I target);
    default String getName() {
        return this.getClass().getSimpleName();
    }
}
