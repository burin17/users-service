import flask
import psycopg2
import py_eureka_client.eureka_client as eureka_client
import dbConfig

from flask import request, jsonify

app = flask.Flask(__name__)
eureka_client.init(eureka_server="http://localhost:8761/eureka",
                   app_name="admin-deletion-service",
                   instance_port=9021)

@app.route('/api/users/is-allowed/<id>', methods=['GET'])
def isAllowed(id):
    print('python instance')
    connection = psycopg2.connect(user=dbConfig.USER,
                                  password=dbConfig.PASSWORD,
                                  host=dbConfig.HOST,
                                  port=dbConfig.PORT,
                                  database=dbConfig.DBNAME)
    cursor = connection.cursor()
    cursor.execute('select * from usr where id = %s', id)
    return jsonify(cursor.rowcount != 0)

app.run(port = 9021)
