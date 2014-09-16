/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
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
package com.dnastack.beacon.dao;

import com.dnastack.beacon.dto.QueryTo;
import com.dnastack.beacon.entity.Chromosome;
import com.dnastack.beacon.entity.Query;
import com.dnastack.beacon.entity.Reference;
import com.dnastack.beacon.util.QueryUtils;
import java.io.Serializable;
import javax.inject.Inject;
import javax.validation.Validator;

/**
 * Basic provider of queries.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class QueryDaoImpl implements QueryDao, Serializable {

    private static final long serialVersionUID = 35L;

    @Inject
    private Validator validator;

    @Override
    public QueryTo getQuery(String chrom, Long pos, String allele, String ref) {
        Chromosome c = QueryUtils.normalizeChromosome(chrom);
        Reference r = QueryUtils.normalizeReference(ref);

        return new QueryTo(new Query(c == null ? null : c, pos, QueryUtils.normalizeAllele(allele), r == null ? null : r));
    }

    @Override
    public boolean checkIfQuerySuccessfullyNormalizedAndValid(QueryTo q, String ref) {
        Query query = new Query(q.getChromosome(), q.getPosition(), q.getAllele(), q.getReference());

        return (!(ref == null || ref.isEmpty()) && q.getReference() == null) || !validator.validate(query).isEmpty();
    }
}
