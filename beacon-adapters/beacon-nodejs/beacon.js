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

//--------------- Information endpont (start) --------------------//

// TODO: override with the details of your beacon

//############# DataSetResource for beacon details ############//

// required field(s): name
var DataUseRequirementResource = {
    'name': 'example name',
    'description': 'example description'
};

// required field(s): variants
var DataSizeResource = {
    'variants': 1, // integer
    'samples': 1 // integer
};

// required field(s): category
var DataUseResource = {
    'category': 'example use category',
    'description': 'example description',
    'requirements': [
        DataUseRequirementResource
    ]
};

// required field(s): id
var DataSetResource = {
    'id': 'example Id',
    'description': 'dataset description',
    'reference': 'reference genome',
    'size': DataSizeResource,  // Dimensions of the data set (required if the beacon reports allele frequencies)
    'data_uses': [
        DataUseResource // Data use limitations
    ]
};

//########## QueryResource for beacon details ###############//

// required field(s): allele, chromosome, position, reference
var QueryResource = {
    'allele': 'allele string',
    'chromosome': 'chromosome Id',
    'position': 1, // integer
    'reference': 'genome Id',
    'dataset_id': 'dataset Id'
};

//################## Beacon details #########################//

// required field(s): id, name, organization, api
var beacon = {
    'id': 'foo',
    'name': 'bar',
    'organization': 'org',
    'api': '0.1/0.2',
    'description': 'beacon description',
    'datasets': [
        DataSetResource  // Datasets served by the beacon
    ],
    'homepage': 'http://dnastack.com/ga4gh/bob/',
    'email': 'beacon@dnastack.com',
    'auth': 'oauth2',  // OAUTH2, defaults to none
    'queries': [
        QueryResource  // Examples of interesting queries
    ]
};

//--------------- Information endpoint (end) ----------------------//

// info function
function info(req, res, next) {
    res.send(beacon);
    next();
}

// query function
// TODO: plug in your data/functionality here
function query(req, res, next) {
    
    // parse query
    var chromosome = req.query.chrom;
    var position = parseInt(req.query.pos);
    var allele = req.query.allele;
    var reference = req.query.ref;
    var dataset = req.query.dataset;

    if (dataset == undefined) {
        dataset = beacon['datasets'][0]['id'];
    }

//---- TODO: override with the necessary response details  ----//

//############# AlleleResource for response ##############//

    // required field(s): allele
    var AlleleResource = {
        'allele': allele,
        'frequency': 0.5 // double between 0 & 1
    };

//############ ErrorResource for response #################//

    // required field(s): name
    var ErrorResource = {
        'name': 'error name/code',
        'description': 'error message'
    };

//################## Response object ########################//
    
    // generate response
    // required field(s): exists
    var response = {
        'exists': 'True',
        'observed': 0,  // integer, min 0
        'alleles': [
            AlleleResource
        ],
        'info': 'response information', 
        'error': ErrorResource
    };

//--------------------------------------------------------------//

    var query = {'chromosome': chromosome, 'position': position, 'allele': allele, 'reference': reference, 'dataset': dataset};

    res.send({"beacon": beacon, "query": query, 'response': response});
    next();
}

function welcome(req, res, next) {
    var message = 'WELCOME!!! Beacon of Beacons Project (BoB) provides a unified REST API to publicly available GA4GH Beacons. BoB standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself. BoB was designed with ease of programmatic access in mind. It provides XML, JSON and plaintext responses to accommodate needs of all the clients across all the programming languages. The API to use is determined using the header supplied by the client in its GET request, e.g.: "Accept: application/json".';
    res.send(message);
    next();
}

var server = restify.createServer({name: 'beacon'});
server.use(restify.queryParser());
server.get('/beacon-nodejs/info', info);
server.head('/beacon-nodejs/info', info);
server.get('/beacon-nodejs/query', query);
server.head('/beacon-nodejs/query', query);
server.get('/beacon-nodejs', welcome);

server.listen(8080, function () {
    console.log('%s listening at %s', server.name, server.url);
});