/*
 * The MIT License
 *
 * Copyright 2015 DNAstack.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dnastack.bob.rest.util;

import org.jboss.arquillian.container.test.api.Deployment;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * JUnit rule.
 *
 * @param <T>
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class ParameterRule<T> implements MethodRule {

    private final List<T> params;

    public ParameterRule(List<T> params) {
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("'params' must be specified and have more then zero length!");
        }
        this.params = params;
    }

    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                boolean runInContainer = getDeploymentMethod(target).getAnnotation(Deployment.class).testable();
                if (runInContainer) {
                    evaluateParametersInContainer(base, target);
                } else {
                    evaluateParametersInClient(base, target);
                }
            }
        };
    }

    private Method getDeploymentMethod(Object target) {
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getAnnotation(Deployment.class) != null) {
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new IllegalStateException("No method with @Deployment annotation found!");
    }

    private void evaluateParametersInContainer(Statement base, Object target) throws Throwable {
        if (ArquillianUtils.isRunningInContainer()) {
            evaluateParamsToTarget(base, target);
        } else {
            ignoreStatementExecution(base);
        }
    }

    private void evaluateParametersInClient(Statement base, Object target) throws Throwable {
        if (ArquillianUtils.isRunningInContainer()) {
            ignoreStatementExecution(base);
        } else {
            evaluateParamsToTarget(base, target);
        }
    }

    private void evaluateParamsToTarget(Statement base, Object target) throws Throwable {
        for (Object param : params) {
            Field targetField = getTargetField(target);
            if (!targetField.isAccessible()) {
                targetField.setAccessible(true);
            }
            targetField.set(target, param);
            base.evaluate();
        }
    }

    private List<Class<?>> getClasses(Object o) {
        List<Class<?>> classes = new ArrayList<>();
        Class current = o.getClass();
        while (current.getSuperclass() != null) {
            classes.add(current);
            current = current.getSuperclass();
        }

        return classes;
    }

    private String getTargetTypeName() {
        return params.stream().findAny().get().getClass().getTypeName();
    }

    private Field getTargetField(Object target) {
        Optional<Field> field = getClasses(target).stream()
                                                  .map(c -> Arrays.asList(c.getDeclaredFields()))
                                                  .flatMap(l -> l.stream())
                                                  .filter(f -> (f.getAnnotation(Parameter.class) != null && f.getType()
                                                                                                             .getTypeName()
                                                                                                             .equals(getTargetTypeName())))
                                                  .findFirst();

        return field.get();
    }

    private void ignoreStatementExecution(Statement base) {
        try {
            base.evaluate();
        } catch (Throwable ignored) {
        }
    }
}
