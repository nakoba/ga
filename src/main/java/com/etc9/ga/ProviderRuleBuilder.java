package com.etc9.ga;

import javax.inject.Provider;
import java.lang.annotation.Annotation;

/**
 * Builder of InjectionRule for provider.
 * @param <T> type
 * @author Naotsugu Kobayashi
 */
public class ProviderRuleBuilder<T> {

    /** context. */
    private final InjectionContext context;
    /** typeLiteral. */
    private final TypeLiteral<Provider<T>> typeLiteral;
    /** annotations. */
    private final Annotation[] annotations;
    /** provider class. */
    private Class<? extends Provider<T>> providerClass;

    /**
     * Constructor.
     * @param context context
     * @param typeLiteral typeLiteral
     * @param annotations qualifiers
     */
    ProviderRuleBuilder(InjectionContext context, TypeLiteral<Provider<T>> typeLiteral, Annotation...annotations) {
        this.context = context;
        this.annotations = annotations;
        this.typeLiteral = typeLiteral;
    }

    /**
     * Build injection rule with specification provider class.
     * @param providerClass provider class
     */
    public void map(Class<? extends Provider<T>> providerClass) {

        this.providerClass = providerClass;

        InjectionPoint<Provider<T>> point = new InjectionPoint<>(typeLiteral, annotations);
        InjectionRule<Provider<T>> rule = new InjectionRule<>(point, providerProxy());

        context.add(rule);

    }

    /**
     * Create provider of provider.
     * @return provider of provider
     */
    private Provider<Provider<T>> providerProxy() {
        return () -> () -> {
            final Provider<T> provider = () -> {
                Object o = new InstanceBuilder(context).newInstance(providerClass);
                return providerClass.cast(o).get();
            };
            return provider.get();
        };
    }

}
