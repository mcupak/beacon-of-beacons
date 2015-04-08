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
    'name': 'name',
    'description': 'description'
};

// required field(s): variants
var DataSetSizeResource = {
    'variants': 1, // integer
    'samples': 1 // integer
};

// required field(s): category
var DataUseResource = {
    'category': 'category',
    'description': 'description',
    'requirements': [
        DataUseRequirementResource
    ]
};

// required field(s): id, reference, multiple, datasets, data_use
var DataSetResource = {
    'id': 'dataset',
    'description': 'description',
    'reference': 'reference',
    'size': DataSetSizeResource, // Dimensions of the data set (required if the beacon reports allele frequencies)
    'multiple': false,
    'datasets': [
    ],
    'data_use': [
        DataUseResource // Data use limitations
    ]
};

//########## QueryResource for beacon details ###############//

// required field(s): referenceBases, alternateBases, chromosome, position, reference
var QueryResource = {
    'referenceBases': 'reference_bases',
    'alternateBases': 'alternate_bases',
    'chromosome': 'chromosome',
    'position': 1, // integer
    'reference': 'hg19',
    'dataset': 'dataset'
};

//################## Beacon details #########################//

// required field(s): id, organization, description, api
var BeaconInformationResource = {
    'id': 'beacon',
    'organization': 'organization',
    'api': '0.1/0.2',
    'description': 'description',
    'datasets': [
        DataSetResource  // Datasets served by the beacon
    ],
    'homepage': 'http://dnastack.com/ga4gh/bob/',
    'email': 'beacon@dnastack.com',
    'auth': 'OAUTH2', // OAUTH2, defaults to none
    'queries': [
        QueryResource  // Examples of interesting queries
    ]
};

//--------------- Information endpoint (end) ----------------------//

// info function
function info(req, res, next) {
    res.send(BeaconInformationResource);
    next();
}

// query function
// TODO: plug in your data/functionality here
function query(req, res, next) {

    // parse query
    var referenceBases = req.query.referenceBases;//tentative
    var alternateBases = req.query.alternateBases;//tentative
    var chromosome = req.query.chromosome;
    var position = parseInt(req.query.position);
    var reference = req.query.reference;
    var dataset = req.query.dataset;

    if (dataset == undefined) {
        dataset = beacon['datasets'][0]['id'];
    }

//---- TODO: override with the necessary response details  ----//

//############ ErrorResource for response #################//

    // required field(s): name
    var ErrorResource = {
        'name': 'error_code',
        'description': 'error_message'
    };

//################## Response object ########################//

    // generate response
    // required field(s): exists
    var response = {
        'exists': true,
        'observed': 1, // integer, min 0
        'info': 'info',
        'frequency': 1, //double
        'err': ErrorResource
    };

//--------------------------------------------------------------//

    var query = {
        'referenceBases': referenceBases,
        'alternateBases': alternateBases,
        'chromosome': chromosome,
        'position': position,
        'reference': reference,
        'dataset': dataset
    };

    if (query['chromosome'] == undefined || query['position'] == undefined || query['referenceBases'] == undefined ||
            query['alternateBases'] == undefined || query['reference'] == undefined) {

        ErrorResource['name'] = 'Incomplete Query';
        ErrorResource['description'] = 'Required parameters are missing';

        response = {
            'exists': null,
            'error': ErrorResource
        };

    }

    res.send({"beacon": BeaconInformationResource["id"], "query": query, 'response': response});
    next();
}

function welcome(req, res, next) {
    var message = 'Welcome to a sample beacon. Check out its /info and /query endpoints, e.g. /info and /query?referenceBases=A&alternateBases=C&chromosome=1&position=1&reference=hg19&dataset=dataset.';
    res.send(message);
    next();
}

var server = restify.createServer({name: 'beacon'});
server.use(restify.queryParser());
server.get('/beacon-nodejs/info', info);
server.head('/beacon-nodejs/info', info);
server.get('/beacon-nodejs/query', query);
server.head('/beacon-nodejs/query', query);
server.get('/beacon-nodejs/', welcome);

server.listen(8080, function () {
    console.log('%s listening at %s', server.name, server.url);
});