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
    'name': u'string',
    'description': u'string'
}

# required field(s): variants
DataSizeResource = {
    'variants': 1, # integer
    'samples': 1 # integer
}

#required field(s): category
DataUseResource = {
    'category': u'string',
    'description': u'string',
    'requirements': [
        DataUseRequirementResource
    ]
}

# required field(s): id
DataSetResource = {
    'id': u'string',
    'description': u'string',
    'reference': u'string',
    'size': DataSizeResource,  # Dimensions of the data set (required if the beacon reports allele frequencies)
    'multiple': False,
    'datasets': [
        u'string'
    ],
    'data_uses': [
        DataUseResource # Data use limitations
    ]
}

########### QueryResource for beacon details ###############

# required field(s): allele, chromosome, position, reference
QueryResource = {
    'allele': u'string',
    'chromosome': u'string',
    'position': 1, # integer
    'reference': u'string',
    'dataset_id': u'string'
}

################### Beacon details #########################

# required field(s): id, name, organization, api
beacon = {
    'id': u'foo',
    'name': u'bar',
    'organization': u'org',
    'api': u'0.1/0.2',
    'description': u'sample beacon',
    'datasets': [
        DataSetResource  # Datasets served by the beacon
    ],
    'homepage': u'http://dnastack.com/ga4gh/bob/',
    'email': u'beacon@dnastack.com',
    'auth': u'string',  # OAUTH2, defaults to none
    'queries': [
        QueryResource  # Examples of interesting queries
    ]
}

#--------------- Information endpoint (end) ----------------------#

# info function
@app.route('/beacon-python/rest/info', methods=['GET'])
def info():
    return jsonify(beacon)

# query function
# TODO: plug in the functionality of your beacon
@app.route('/beacon-python/rest/query', methods=['GET'])
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
        'allele': u'string',
        'frequency': 0.5 # double between 0 & 1
    }

############# ErrorResource for response #################

    # required field(s): name
    ErrorResource = {
        'name': u'string',
        'description': u'string'
    }

################### Response object #########################
    
    # generate response
    # required field(s): exists
    response = {
        'exists': True,
        'observed': '0',  # min 0
        'alleles': [
            AlleleResource
        ],
        'info': u'string',                
        'error': ErrorResource
    }

#--------------------------------------------------------------#
    
    return jsonify({ "beacon" : beacon, "query" : { 'chromosome' : chromosome, 'position' : position, 'allele' : allele, 'reference' : reference, 'dataset_id': dataset }, 'response' : response })

# errors in JSON
@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.run()

