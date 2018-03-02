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
package com.dnastack.bob.rest.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

/**
 * List wrapper used for serialization into XML.
 *
 * @param <T> type
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@XmlRootElement(name = "list")
public class JaxbList<T> {

    private List<T> collection;

    public JaxbList() {
    }

    public JaxbList(List<T> list) {
        this.collection = list;
    }

    public JaxbList(T[] list) {
        this.collection = Arrays.asList(list);
    }

    @XmlElement(name = "item")
    public List<T> getCollection() {
        return collection;
    }

}
