#!flask/bin/python

'''
The MIT License

Copyright 2014 DNAstack.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
'''

from flask import Flask, jsonify, request

app = Flask(__name__)

#--------------- Information endpont (start) --------------------#

# TODO: override with the details of your beacon

########### DataSetResource for beacon details ############

# required field(s): name
DataUseRequirementResource = {
    'name': u'example name',
    'description': u'example description'
}

# required field(s): variants
DataSizeResource = {
    'variants': 1, # integer
    'samples': 1 # integer
}

# required field(s): category
DataUseResource = {
    'category': u'example use category',
    'description': u'example description',
    'requirements': [
        DataUseRequirementResource
    ]
}

# required field(s): id
DataSetResource = {
    'id': u'example Id',
    'description': u'dataset description',
    'reference': u'reference genome',
    'size': DataSizeResource,  # Dimensions of the data set (required if the beacon reports allele frequencies)
    'data_uses': [
        DataUseResource # Data use limitations
    ]
}

########### QueryResource for beacon details ###############

# required field(s): allele, chromosome, position, reference
QueryResource = {
    'allele': u'allele string',
    'chromosome': u'chromosome Id',
    'position': 1, # integer
    'reference': u'genome Id',
    'dataset_id': u'dataset Id'
}

################### Beacon details #########################

# required field(s): id, name, organization, api
beacon = {
    'id': u'foo',
    'name': u'bar',
    'organization': u'org',
    'api': u'0.1/0.2',
    'description': u'beacon description',
    'datasets': [
        DataSetResource  # Datasets served by the beacon
    ],
    'homepage': u'http://dnastack.com/ga4gh/bob/',
    'email': u'beacon@dnastack.com',
    'auth': u'oauth2',  # OAUTH2, defaults to none
    'queries': [
        QueryResource  # Examples of interesting queries
    ]
}

#--------------- Information endpoint (end) ----------------------#

# info function
@app.route('/beacon-python/info', methods=['GET'])
def info():
    return jsonify(beacon)

# query function
# TODO: plug in the functionality of your beacon
@app.route('/beacon-python/query', methods=['GET'])
def query():
    # parse query
    chromosome = request.args.get('chrom')
    position = long(request.args.get('pos'))
    allele = request.args.get('allele')
    reference = request.args.get('ref')
    dataset = request.args.get('dataset') if 'dataset' in request.args else beacon['datasets'][0]['id']

    
#---- TODO: override with the necessary response details  ----#

############## AlleleResource for response ###############

    # required field(s): allele
    AlleleResource = {
        'allele': allele,
        'frequency': 0.5 # double between 0 & 1
    }

############# ErrorResource for response #################

    # required field(s): name
    ErrorResource = {
        'name': u'error name/code',
        'description': u'error message'
    }

################### Response object #########################
    
    # generate response
    # required field(s): exists
    response = {
        'exists': True,
        'observed': 0,  # integer, min 0
        'alleles': [
            AlleleResource
        ],
        'info': u'response information', 
        'error': ErrorResource
    }

#--------------------------------------------------------------#
    
    return jsonify({ "beacon" : beacon['id'], "query" : { 'chromosome' : chromosome, 'position' : position, 'allele' : allele, 'reference' : reference, 'dataset_id': dataset }, 'response' : response })

# info function
@app.route('/beacon-python', methods=['GET'])
def welcome():
    return 'WELCOME!!! Beacon of Beacons Project (BoB) provides a unified REST API to publicly available GA4GH Beacons. BoB standardizes the way beacons are accessed and aggregates their results, thus addressing one of the missing parts of the Beacon project itself. BoB was designed with ease of programmatic access in mind. It provides XML, JSON and plaintext responses to accommodate needs of all the clients across all the programming languages. The API to use is determined using the header supplied by the client in its GET request, e.g.: "Accept: application/json".'

# page not found
@app.errorhandler(404)
def not_found(error):
    return 'Page not found (Bad URL)', 404

if __name__ == '__main__':
    app.run()

