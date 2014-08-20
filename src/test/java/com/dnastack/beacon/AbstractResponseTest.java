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
package com.dnastack.beacon;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBException;

/**
 * Test of a beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public abstract class AbstractResponseTest extends BasicTest {

    public abstract void testFound(URL url) throws JAXBException, MalformedURLException;

    public abstract void testDifferentGenome(URL url) throws JAXBException, MalformedURLException;

    public abstract void testStringAllele(URL url) throws JAXBException, MalformedURLException;

    public abstract void testDel(URL url) throws JAXBException, MalformedURLException;

    public abstract void testIns(URL url) throws JAXBException, MalformedURLException;

    public abstract void testNotFound(URL url) throws JAXBException, MalformedURLException;

    public abstract void testInvalidAllele(URL url) throws JAXBException, MalformedURLException;

    public abstract void testAlleleConversion(URL url) throws JAXBException, MalformedURLException;

    public abstract void testChromConversion(URL url) throws JAXBException, MalformedURLException;

    public abstract void testInvalidChrom(URL url) throws JAXBException, MalformedURLException;

    public abstract void testChromX(URL url) throws JAXBException, MalformedURLException;

    public abstract void testChromMT(URL url) throws JAXBException, MalformedURLException;
}
