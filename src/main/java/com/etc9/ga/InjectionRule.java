package com.etc9.ga;

import javax.inject.Provider;

/**
 * Rule of Injection.
 *
 * @param <T>
 * @author Naotsugu Kobayashi
 */
public class InjectionRule<T> {

    /** injection point. */
    private final InjectionPoint<T> point;

    /** provider for injection point. */
    private final Provider<? extends T> provider;

    /**
     * Constructor.
     * @param point injection point
     * @param provider provider for injection point
     */
    public InjectionRule(InjectionPoint<T> point, Provider<? extends T> provider) {
        this.point = point;
        this.provider = provider;
    }

    /**
     * Gets injection point.
     * @return injection point
     */
    public InjectionPoint<T> getPoint() {
        return point;
    }

    /**
     * Gets provider for injection point.
     * @return provider for injection point
     */
    public Provider<? extends T> getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return "InjectionRule{" +
                "point=" + point +
                ", provider=" + provider +
                '}';
    }
}
