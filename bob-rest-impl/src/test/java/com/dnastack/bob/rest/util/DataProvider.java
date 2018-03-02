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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data holder.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Log
public class DataProvider {

    private static final String QUERIES_FULL = "/queries.json";
    private static final String QUERIES_QUICK = "/queries-quick.json";
    private static final List<QueryEntry> queries;

    static {
        // use small test file if quick system property is passed
        String fileName = Boolean.parseBoolean(System.getProperty("quick", "true")) ? QUERIES_QUICK : QUERIES_FULL;

        Type collectionType = new TypeToken<Collection<QueryEntry>>() {
        }.getType();

        List<QueryEntry> res = new ArrayList<>();
        try {
            try (BufferedReader b = new BufferedReader(new InputStreamReader(DataProvider.class.getResourceAsStream(
                    fileName), StandardCharsets.UTF_8))) {
                res = new Gson().fromJson(b, collectionType);
            } catch (FileNotFoundException ex) {
                log.info("Could not find file: " + fileName);
            }
        } catch (IOException ex) {
            log.info("Could not open file: " + fileName);
        }

        queries = res;
    }

    public static List<QueryEntry> getQueries() {
        return Collections.unmodifiableList(queries);
    }

    public static Set<QueryEntry> getQueries(String beacon) {
        return queries.stream()
                      .filter((QueryEntry q) -> ((q.getBeacon() == null && beacon == null) || q.getBeacon()
                                                                                               .equals(beacon)))
                      .collect(Collectors.toSet());
    }

    public static Set<String> getBeacons() {
        return queries.stream()
                      .filter((QueryEntry q) -> (q.getBeacon() != null && !q.getBeacon()
                                                                            .isEmpty() && !isQueryForMultipleBeacons(q)))
                      .map((QueryEntry q) -> q.getBeacon())
                      .collect(Collectors.toSet());
    }

    public static boolean isQueryForMultipleBeacons(QueryEntry q) {
        return q.getBeacon().startsWith("[");
    }

}
