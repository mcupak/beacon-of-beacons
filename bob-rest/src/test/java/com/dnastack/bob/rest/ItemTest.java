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
package com.dnastack.bob.rest;

import com.dnastack.bob.rest.util.ItemWrapper;
import com.dnastack.bob.dto.ChromosomeTo;
import com.dnastack.bob.dto.ReferenceTo;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Test of chromosome, reference and allele info.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ItemTest extends BasicTest {

    public static final String CHROMOSOMES_TEMPLATE = "rest/chromosomes";
    public static final String REFERENCES_TEMPLATE = "rest/references";
    public static final String ALLELES_TEMPLATE = "rest/alleles";

    @SuppressWarnings("unchecked")
    public static List<ItemWrapper> readItems(String url) throws JAXBException, MalformedURLException {
        return (List<ItemWrapper>) readObject(ItemWrapper.class, url);
    }

    @Test
    public void testAllChromosomes(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        List<ItemWrapper> bs = readItems(url.toExternalForm() + CHROMOSOMES_TEMPLATE);
        assertEquals(bs.size(), ChromosomeTo.values().length);
    }

    @Test
    public void testAllReferences(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        List<ItemWrapper> bs = readItems(url.toExternalForm() + REFERENCES_TEMPLATE);
        assertEquals(bs.size(), ReferenceTo.values().length);
    }

    @Test
    public void testAllAlleles(@ArquillianResource URL url) throws JAXBException, MalformedURLException {
        List<ItemWrapper> bs = readItems(url.toExternalForm() + ALLELES_TEMPLATE);
        assertEquals(bs.size(), "ACGTDI".length());
    }

}
