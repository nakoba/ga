package com.etc9.ga;

import javax.inject.Provider;

/**
 * Implementation of {@code injector}.
 *
 * @author Naotsugu Kobayashi
 */
public class InjectorImpl implements Injector {

    /** context of injection. */
    private final InjectionContext context;


    /**
     * Construct injector.
     * @param context context
     */
    public InjectorImpl(InjectionContext context) {
        this.context = context;
    }


    @Override
    public <T> T getInstance(Class<T> clazz) {
        Provider<?> provider = context.mapOf(InjectionPoint.of(clazz));
        return clazz.cast(provider.get());
    }

}
