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
package com.dnastack.bob.service.lrg;

/**
 * Reference specification for LRG queries. Currently a single value - LRG.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public enum LrgReference {

    LRG("LRG");

    private final String ref;

    private LrgReference(String ref) {
        this.ref = ref;
    }

    public static LrgReference fromString(String text) {
        if (text != null) {
            for (LrgReference b : LrgReference.values()) {
                if (text.equalsIgnoreCase(b.toString())) {
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return ref;
    }
}
