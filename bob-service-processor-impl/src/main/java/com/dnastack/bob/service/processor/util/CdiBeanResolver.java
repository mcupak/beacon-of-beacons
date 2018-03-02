/* 
 * Copyright 2018 DNAstack.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dnastack.bob.service.processor.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Set;

/**
 * Computes string identifier for a processor and vice-versa.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class CdiBeanResolver implements Serializable {

    private static final long serialVersionUID = 8760528174400816670L;

    @Inject
    private BeanManager beanManager;

    public String getClassId(Class<?> clazz) {
        return (clazz == null) ? null : clazz.getCanonicalName();
    }

    public Object resolve(String id) throws ClassNotFoundException {
        if (id == null) {
            return null;
        }

        Class<?> c = Class.forName(id);
        Set<Bean<?>> beans = beanManager.getBeans(c, new AnnotationLiteral<Any>() {
            private static final long serialVersionUID = 3109256773218160485L;
        });
        if (beans.size() == 1) {
            Bean<?> b = (Bean<?>) beans.toArray()[0];
            return beanManager.getReference(b, c, beanManager.createCreationalContext(b));
        }

        throw new ClassNotFoundException("Could not resolve class");
    }
}
