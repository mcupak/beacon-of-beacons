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