from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
from flask_jsonpify import jsonify

db_connect = create_engine('sqlite:///gymkhana.db')
app = Flask(__name__)
api = Api(app)

#GET -> coge todas las gymkhanas (hay que cambiarlo para que coja solo las de un rango de latitud y longitud)
#POST -> crea una nueva gymkhana
class Gymkhanas(Resource):
    def get(self):
        conn = db_connect.connect() # connect to database
        query = conn.execute("select * from GYMKHANABEANS") # This line performs query and returns json result
        return {'gymkhanas': query.cursor.fetchall()} # Fetches first column that is Employee ID        
    def post(self):
        return 'holi'

#GET -> Coge una gymkhana especifica 
#POST -> Actualiza la gymkhana especifica
class Gymkhana(Resource):
    def get(self, GYMK_ID):
        conn = db_connect.connect()
        query = conn.execute("select * from GYMKHANABEANS WHERE GYMK_ID=%d" %int(GYMK_ID))
        return {'data': query.cursor.fetchall()}
    def post(self, GYMK_ID):
        return 'holi'

#Falta un GET a /gymkhanas?user=user para coger las gymkhanas de un usuario

api.add_resource(Gymkhanas, '/gymkhanas')
api.add_resource(Gymkhana, '/gymkhanas/<GYMK_ID>')

if __name__ == '__main__':
     app.run(port='5002')