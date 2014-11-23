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
var restify = require('restify');

// beacon details
// TODO: override with the details of your beacon
var beacon = {'description': 'sample beacon', 'id': 'foo', 'name': 'bar', 'organization': 'org'};

// info function
function info(req, res, next) {
    res.send(beacon);
    next();
}

// query function
// TODO: plug in your data/functionality here
function query(req, res, next) {
    // parse query
    chromosome = req.query.chrom;
    position = parseInt(req.query.pos);
    allele = req.query.allele;
    reference = req.query.ref;
    query = {'chromosome': chromosome, 'position': position, 'allele': allele, 'reference': reference};

    // generate response
    response = true;

    res.send({"beacon": beacon, "query": query, 'response': response});
    next();
}

var server = restify.createServer({name: 'beacon'});
server.use(restify.queryParser());
server.get('/beacon-nodejs/rest/info', info);
server.head('/beacon-nodejs/rest/info', info);
server.get('/beacon-nodejs/rest/query', query);
server.head('/beacon-nodejs/rest/query', query);

server.listen(8080, function () {
    console.log('%s listening at %s', server.name, server.url);
});