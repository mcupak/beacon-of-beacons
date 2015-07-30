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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Data holder.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class DataProvider {

    private static final String FILE = "/queries.json";
    private static final List<QueryEntry> queries;
    private static final Logger logger = Logger.getLogger(DataProvider.class.getName());

    static {
        Type collectionType = new TypeToken<Collection<QueryEntry>>() {
        }.getType();

        List<QueryEntry> res = new ArrayList<>();
        try {
            try (BufferedReader b = new BufferedReader(new InputStreamReader(DataProvider.class.getResourceAsStream(FILE), StandardCharsets.UTF_8))) {
                res = new Gson().fromJson(b, collectionType);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DataProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        queries = res;
    }

    public static List<QueryEntry> getQueries() {
        return Collections.unmodifiableList(queries);
    }

    public static Set<QueryEntry> getQueries(String beacon) {
        return queries.stream().filter((QueryEntry q) -> ((q.getBeacon() == null && beacon == null) || q.getBeacon().equals(beacon))).collect(Collectors.toSet());
    }

    public static Set<String> getBeacons() {
        return queries.stream().filter((QueryEntry q) -> (q.getBeacon() != null && !q.getBeacon().isEmpty() && !isQueryForMultipleBeacons(q))).map((QueryEntry q) -> q.getBeacon()).collect(Collectors.toSet());
    }

    public static boolean isQueryForMultipleBeacons(QueryEntry q) {
        return q.getBeacon().startsWith("[");
    }
}
