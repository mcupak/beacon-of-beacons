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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;

/**
 * EJB lookup executor.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@ApplicationScoped
public class EjbResolver implements Serializable {

    private static final long serialVersionUID = 8760528174400816670L;
    private static final String LOOKUP_PREFIX = "java:app/bob-rest/";

    private InitialContext ctx;

    @PostConstruct
    private void init() {
        try {
            ctx = new InitialContext();
        } catch (NamingException ex) {
            ctx = null;
        }
    }

    public String getClassId(Class<?> clazz) {
        return (clazz == null) ? null : LOOKUP_PREFIX + clazz.getSimpleName();
    }

    public Object resolve(String id) throws NamingException {
        if (ctx == null) {
            throw new NullPointerException("ctx");
        }
        return id == null ? null : ctx.lookup(id);
    }
}
