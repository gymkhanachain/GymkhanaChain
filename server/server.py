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
        conn = db_connect.connect()
        query = conn.execute("select * from GYMKHANABEANS") # Falta pillar el GPS
        return {'gymkhanas': query.cursor.fetchall()}
    def post(self):
        #Pillar las movidas del cuerpo
        gymkhana = request.get_json()
	#gymk_id = gymkhana['id']
	#imagen = gymkhana['image']	
	#nombre = gymkhana['name']
	#desc = gymkhana['desc']
	#lat = gymkhana['lat']
	#lng = gymkhana['lng']
	#typ = gymkhana['type']
	#a11y = gymkhana['a11y']
	#priv = gymkhana['priv']
	#acc_cod = gymkhana['acc_cod']
	#creator = gymkhana['creator']
	#crea_time = gymkhana['crea_time']
	#aper_time = gymkhana['aper_time']
	#close_time = gymkhana['close_time']
	#query = 'INSERT INTO GYMKHANAS(GYMK_ID, IMAGE, NAME, DESC, LAT, LNG, TYPE, A11Y, PRIV, ACC_COD, CREATOR, CREA_DATE, APER_TIME, CLOSE_TIME) VALUES (' + gymk_id + ',\'' + imagen + '\',\'' + nombre + '\',\'' + desc + '\',' + lat + ',' + lng + ',' + typ + ',\'' + a11y + '\',\'' + priv + '\',\'' + acc_cod + '\',\'' + creator + '\',' + crea_time + ',' + aper_time + ',' + close_time + ')'
	#curl -d '{"id":"2", "image":"IMAGENNN", "name":"NOMBRE GYM", "desc":"DESCRIPTION", "lat":"0000000000.00000000", "lng":"00000000000.00000000", "type":"0", "a11y":"FALSE", "priv":"FALSE", "acc_cod":"", "creator":"CREATOR", "crea_time":"100", "aper_time":"100", "close_time":"100"}' -H "Content-Type: application/json" -X POST http://127.0.0.1:5002/gymkhanas
        #conn = db_connect.connect()
        #query = conn.execute("INSERT INTO GYMKHANAS(GYMK_ID, IMAGE, NAME, DESC, LAT, LNG, TYPE, A11Y, PRIV, ACC_COD, CREATOR, CREA_DATE, APER_TIME, CLOSE_TIME) VALUES ()") 
        return gymkhana

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


# CREATE TABLE GYMKHANAS (GYMK_ID INT NOT NULL PRIMARY KEY, IMAGE BLOB, NAME VARCHAR(255), DESC VARCHAR(512), LAT DECIMAL(10,8), LNG DECIMAL (11,8), TYPE INT, A11Y BOOLEAN, PRIV BOOLEAN, ACC_COD VARCHAR(255), CREATOR VARCHAR(255), CREA_DATE INT, APER_TIME INT, CLOSE_TIME INT);
# INSERT INTO GYMKHANAS(GYMK_ID, IMAGE, NAME, DESC, LAT, LNG, TYPE, A11Y, PRIV, ACC_COD, CREATOR, CREA_DATE, APER_TIME, CLOSE_TIME) VALUES(1, 'IMAGENN', 'NOMBRE GYMKHANA', 'DESCRIPCION',  1111111111.11111111, 11111111111.11111111, 0, 'FALSE', 'FALSE', '', 'CREADOR', 1,1,1);
#curl -d '{"key1":"value1", "key2":"value2"}' -H "Content-Type: application/json" -X POST http://localhost:5002

