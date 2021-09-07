import flask
import psycopg2

import dbConfig

from flask import request, jsonify

app = flask.Flask(__name__)

@app.route('/api/users/is-allowed/<id>', methods=['GET'])
def isAllowed(id):
    connection = psycopg2.connect(user=dbConfig.USER,
                                  password=dbConfig.PASSWORD,
                                  host=dbConfig.HOST,
                                  port=dbConfig.PORT,
                                  database=dbConfig.DBNAME)
    cursor = connection.cursor()
    cursor.execute('select * from usr where id = %s', id)
    return jsonify(cursor.rowcount != 0)

app.run(port = 9001)