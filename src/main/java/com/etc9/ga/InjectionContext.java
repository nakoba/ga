package com.etc9.ga;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Injection context.
 *
 * @author Naotsugu Kobayashi
 */
public class InjectionContext {

    /** Mapping rules. */
    private final ConcurrentMap<InjectionPoint<?>, InjectionRule<?>> rules = new ConcurrentHashMap<>();

    /** Cache of instance. */
    private final ConcurrentMap<Class<?>, Object> instanceCache = new ConcurrentHashMap<>();


    /**
     * Start to create new injection rule.
     * @param pointClass point of injection
     * @param annotations qualifiers
     * @param <T> type
     * @return rule builder
     */
    public <T> InjectionRuleBuilder<T> ruleOf(Class<T> pointClass, Annotation...annotations) {
        return new InjectionRuleBuilder<>(this, pointClass, annotations);
    }


    /**
     * Start to create new injection rule of provider.
     * @param typeLiteral point of injection
     * @param annotations qualifiers
     * @param <T> target type of provider
     * @return rule builder
     */
    public <T> ProviderRuleBuilder<T> ruleOf(TypeLiteral<Provider<T>> typeLiteral, Annotation... annotations) {
        return new ProviderRuleBuilder<>(this, typeLiteral, annotations);
    }

    /**
     * Gets mapped provider from rules.
     * @param point injection point
     * @param <T> type
     * @return mapped provider
     */
    @SuppressWarnings("unchecked")
    public <T> Provider<? extends T> mapOf(InjectionPoint<T> point) {

        if (!rules.containsKey(point)) {
            throw new RuntimeException("Undefined rule. [" + point + "]");
        }

        return (Provider<? extends T>) rules.get(point).getProvider();
    }

    /**
     * Add injection rule.
     * @param rule rule
     */
    void add(InjectionRule<?> rule) {
        rules.put(rule.getPoint(), rule);
    }


    /**
     * Gets instance from cache if a class marked singleton.
     * @param type type
     * @param mappingFunction function of create instance
     * @return instance
     */
    Object fromCache(Class<?> type, Function<Class<?>, Object> mappingFunction) {
        if (!type.isAnnotationPresent(Singleton.class)) {
            return mappingFunction.apply(type);
        }
        Object obj = instanceCache.computeIfAbsent(type, mappingFunction);
        return obj == null ? instanceCache.get(type) : obj;
    }


}
