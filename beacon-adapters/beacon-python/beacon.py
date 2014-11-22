#!flask/bin/python
from flask import Flask, jsonify, request

app = Flask(__name__)

# beacon details
# TODO: override with the details of your beacon
beacon = {
    'id': u'foo',
    'name': u'bar',
    'organization': u'org', 
    'description': u'sample beacon'
}

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

    # generate response
    response = True

    return jsonify({ "beacon" : beacon, "query" : { 'chromosome' : chromosome, 'position' : position, 'allele' : allele, 'reference' : reference }, 'response' : response })

# errors in JSON
@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.run()