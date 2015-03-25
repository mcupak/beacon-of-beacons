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
package com.dnastack.bob.service.processor.util;

import com.dnastack.bob.service.processor.impl.AbstractBeaconProcessor;
import com.dnastack.bob.service.processor.api.BeaconProcessor;
import java.io.Serializable;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

/**
 * Computes string identifier for a processor and vice-versa.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class ProcessorResolver implements Serializable {

    private static final long serialVersionUID = 8760528174400816670L;

    @Inject
    private BeanManager beanManager;

    public String getProcessorId(Class<? extends AbstractBeaconProcessor> clazz) {
        return (clazz == null) ? null : clazz.getCanonicalName();
    }

    public BeaconProcessor getProcessor(String id) throws ClassNotFoundException {
        Class<?> c = Class.forName(id);
        Set<Bean<?>> beans = beanManager.getBeans(c, new AnnotationLiteral<Any>() {
            private static final long serialVersionUID = 3109256773218160485L;
        });
        if (beans.size() == 1) {
            Bean<? extends AbstractBeaconProcessor> b = (Bean<? extends AbstractBeaconProcessor>) beans.toArray()[0];
            return (BeaconProcessor) beanManager.getReference(b, c, beanManager.createCreationalContext(b));
        }

        throw new ClassNotFoundException("Could not resolve this implementation of BeaconProcessor");
    }
}
