from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
from flask_jsonpify import jsonify
import json

db_connect = create_engine('sqlite:///gymkhana.db')
app = Flask(__name__)
api = Api(app)

#GET -> coge todas las gymkhanas (hay que cambiarlo para que coja solo las de un rango de latitud y longitud)
#POST -> crea una nueva gymkhana
class Gymkhanas(Resource):
    def get(self):
        conn = db_connect.connect()
        lat_sup = request.args.get('lat_sup')
        long_sup = request.args.get('long_sup')
        lat_inf = request.args.get('lat_inf')
        long_inf = request.args.get('long_inf')
        query = conn.execute("select * from GYMKHANAS WHERE LAT BETWEEN " + str(lat_sup) + " AND " + str(lat_inf) + " AND LNG BETWEEN " + str(long_sup) + " AND " + str(long_inf)) # Falta pillar el GPS
        result = [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]
        print result
        return jsonify(result)
    def post(self):
        gymkhana = request.get_json()
        print gymkhana
        imagen = gymkhana['IMAGE']	
        nombre = gymkhana['NAME']
        desc = gymkhana['DESCRIPCION']
        lat = gymkhana['LAT']
        lng = gymkhana['LNG']
        typ = gymkhana['TYPE']
        a11y = gymkhana['A11Y']
        priv = gymkhana['PRIV']
        acc_cod = gymkhana['ACC_COD']
        creator = gymkhana['CREATOR']
        crea_date = gymkhana['CREA_DATE']
        aper_time = gymkhana['APER_TIME']
        close_time = gymkhana['CLOSE_TIME']
        query_gymk = 'INSERT INTO GYMKHANAS(IMAGE, NAME, DESC, LAT, LNG, TYPE, A11Y, PRIV, ACC_COD, CREATOR, CREA_DATE, APER_TIME, CLOSE_TIME) VALUES (\'' + imagen + '\',\'' + nombre + '\',\'' + desc + '\',' + str(lat) + ',' + str(lng) + ',\'' + typ + '\',\'' + str(a11y) + '\',\'' + str(priv) + '\',\'' + acc_cod + '\',\'' + creator + '\',' + str(crea_date) + ',' + str(aper_time) + ',' + str(close_time) + ')'
        # print query_gymk
        conn = db_connect.connect()
        conn.execute(query_gymk)
        id_gym = conn.execute('SELECT GYMK_ID FROM GYMKHANAS WHERE NAME = \"' + nombre + '\" AND CREA_DATE = ' + str(crea_date))
        result_set = id_gym.cursor.fetchall()
        for row in result_set:
            if gymkhana['POINTS'] is not None:
                gymk_id = row[0]
                # print gymk_id
                for punto in gymkhana['POINTS']:
                    punt_img = punto['IMAGE']
                    punt_lat = punto['LAT']
                    punt_lng = punto['LNG']
                    punt_name = punto['NAME']
                    punt_short = punto['SHORT_DESC']
                    query_point = 'INSERT INTO POINT(IMAGE, NAME, SHORT_DESC, LAT, LNG, GYMK_ID) VALUES(\''+ punt_img + '\',\'' + punt_name + '\',\'' + punt_short + '\',' + str(punt_lat) + ',' + str(punt_lng) + ',' + str(gymk_id) + ')'
                    conn.execute(query_point)
                    point_id = conn.execute('SELECT POINT_ID FROM POINT WHERE NAME = \'' + punt_name + '\' AND GYMK_ID = ' + str(gymk_id))
                    if 'LONG_DESC' in punto:
                        punt_long = punto['LONG_DESC']
                        query_text = 'INSERT INTO TEXT(LONG_DESC) VALUES(\'' + punt_long + '\')'
                        conn.execute(query_text)
                        text_id = conn.execute('SELECT TEXT_ID FROM TEXT WHERE LONG_DESC = \'' + punt_long +'\'')
                        query_union = 'INSERT INTO POINT_ACT(POINT_ID, TEXT) VALUES (' + str(point_id.cursor.fetchone()[0]) + ',' + str(text_id.cursor.fetchone()[0]) + ')'
                        conn.execute(query_union)
                    else:
                        # print punto
                        punt_qt = punto['QUIZ_TEXT']
                        punt_sol1 = punto['SOL1']
                        punt_sol2 = punto['SOL2']
                        punt_sol3 = punto['SOL3']
                        punt_sol4 = punto['SOL4']
                        punt_cor = punto['CORRECT']
                        query_quizz = 'INSERT INTO QUIZZ(QUIZ_TEXT, SOL1, SOL2, SOL3, SOL4, CORRECT) VALUES(\'' + punt_qt + '\',\'' + punt_sol1 + '\',\'' + punt_sol2 + '\',\'' + punt_sol3 + '\',\'' + punt_sol4 + '\',' + str(punt_cor) + ')'
                        conn.execute(query_quizz)
                        quizz_id = conn.execute('SELECT QUIZZ_ID FROM QUIZZ WHERE QUIZ_TEXT = \'' + punt_qt + '\'')
                        query_union = 'INSERT INTO POINT_ACT(POINT_ID, QUIZZ) VALUES (' + str(point_id.cursor.fetchone()[0]) + ',' + str(quizz_id.cursor.fetchone()[0]) + ')'
                        conn.execute(query_union)

#GET -> Coge una gymkhana especifica 
#POST -> Actualiza la gymkhana especifica (CASI LO MISMO QUE EL ANADIR GYMKHANA)
class Gymkhana(Resource):
    def get(self, GYMK_ID):
        conn = db_connect.connect()
    	# Cojo la gymkhana (lo que es unico)
    	query = conn.execute("select * from GYMKHANAS WHERE GYMK_ID=%d" %int(GYMK_ID))
    	result_part1 = [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor][0]
    	# Cojo un tipo de puntos
    	query2 = conn.execute("select P.POINT_ID, P.IMAGE, P.NAME, P.SHORT_DESC, P.LAT, P.LNG, T.LONG_DESC from GYMKHANAS G JOIN POINT P ON G.GYMK_ID = P.GYMK_ID JOIN POINT_ACT PA ON PA.POINT_ID = P.POINT_ID JOIN TEXT T ON T.TEXT_ID = PA.TEXT WHERE G.GYMK_ID=%d" %int(GYMK_ID))
    	result_part2 = [dict(zip(tuple (query2.keys()) ,i)) for i in query2.cursor]
    	# Cojo el otro tipo de puntos
    	query3 = conn.execute("select P.POINT_ID, P.IMAGE, P.NAME, P.SHORT_DESC, P.LAT, P.LNG, Q.QUIZ_TEXT, Q.SOL1, Q.SOL2, Q.SOL3, Q.SOL4, CORRECT from GYMKHANAS G JOIN POINT P ON G.GYMK_ID = P.GYMK_ID JOIN POINT_ACT PA ON PA.POINT_ID = P.POINT_ID JOIN QUIZZ Q ON Q.QUIZZ_ID = PA.QUIZZ WHERE G.GYMK_ID=%d" %int(GYMK_ID))
    	result_part3 = [dict(zip(tuple (query3.keys()) ,i)) for i in query3.cursor]
    	result_part1['POINTS'] = result_part2 + result_part3
    	return jsonify(result_part1)

    def post(self, GYMK_ID):
        gymk = request.get_json()
        print gymk
	#query = 'UPDATE INTO GYMKHANAS(GYMK_ID, IMAGE, NAME, DESC, LAT, LNG, TYPE, A11Y, PRIV, ACC_COD, CREATOR, CREA_DATE, APER_TIME, CLOSE_TIME) VALUES (' + gymk_id + ',\'' + imagen + '\',\'' + nombre + '\',\'' + desc + '\',' + lat + ',' + lng + ',' + typ + ',\'' + a11y + '\',\'' + priv + '\',\'' + acc_cod + '\',\'' + creator + '\',' + crea_time + ',' + aper_time + ',' + close_time + ')'
    def delete(self, GYMK_ID):
        conn = db_connect.connect()
        query = conn.execute("DELETE FROM GYMKHANAS WHERE GYMK_ID=%d" %int(GYMK_ID))

class Gymkhanas_user(Resource):
    def get(self, USER):
        conn = db_connect.connect()
        query = conn.execute("select * from GYMKHANAS WHERE CREATOR=\'" + USER + "\'")
        gymkhanas = [dict(zip(tuple (query.keys()) ,i)) for i in query.cursor]
        for gymkhana in gymkhanas:
            gymk_id = gymkhana["GYMK_ID"]
            query2 = conn.execute("select P.POINT_ID, P.IMAGE, P.NAME, P.SHORT_DESC, P.LAT, P.LNG, T.LONG_DESC from GYMKHANAS G JOIN POINT P ON G.GYMK_ID = P.GYMK_ID JOIN POINT_ACT PA ON PA.POINT_ID = P.POINT_ID JOIN TEXT T ON T.TEXT_ID = PA.TEXT WHERE G.GYMK_ID=" + str(gymk_id))
            result_part2 = [dict(zip(tuple (query2.keys()) ,i)) for i in query2.cursor]
            query3 = conn.execute("select P.POINT_ID, P.IMAGE, P.NAME, P.SHORT_DESC, P.LAT, P.LNG, Q.QUIZ_TEXT, Q.SOL1, Q.SOL2, Q.SOL3, Q.SOL4, CORRECT from GYMKHANAS G JOIN POINT P ON G.GYMK_ID = P.GYMK_ID JOIN POINT_ACT PA ON PA.POINT_ID = P.POINT_ID JOIN QUIZZ Q ON Q.QUIZZ_ID = PA.QUIZZ WHERE G.GYMK_ID=" + str(gymk_id))
            result_part3 = [dict(zip(tuple (query3.keys()) ,i)) for i in query3.cursor]
            gymkhana['POINTS'] = result_part2 + result_part3
        return jsonify(gymkhanas)

api.add_resource(Gymkhanas, '/gymkhanas')
api.add_resource(Gymkhana, '/gymkhanas/<GYMK_ID>')
api.add_resource(Gymkhanas_user, '/gymkhanas/user/<USER>')

if __name__ == '__main__':
     app.run(port='5002')


# CREATE TABLE GYMKHANA (GYMK_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, IMAGE BLOB, NAME VARCHAR(255), DESC VARCHAR(512), LAT DECIMAL(10,8), LNG DECIMAL (11,8), TYPE VARCHAR(255), A11Y BOOLEAN, PRIV BOOLEAN, ACC_COD VARCHAR(255), CREATOR VARCHAR(255), CREA_DATE INT, APER_TIME INT, CLOSE_TIME INT);
# INSERT INTO GYMKHANAS(IMAGE, NAME, DESC, LAT, LNG, TYPE, A11Y, PRIV, ACC_COD, CREATOR, CREA_DATE, APER_TIME, CLOSE_TIME) VALUES('IMAGENN', 'NOMBRE GYMKHANA', 'DESCRIPCION',  1111111111.11111111, 11111111111.11111111, 'desordenada', 'FALSE', 'FALSE', '', 'CREADOR', 1,1,1);
#curl -d '{"key1":"value1", "key2":"value2"}' -H "Content-Type: application/json" -X POST http://localhost:5002
#curl -d '{"id":"2", "image":"IMAGENNN", "name":"NOMBRE GYM", "desc":"DESCRIPTION", "lat":"0000000000.00000000", "lng":"00000000000.00000000", "type":"0", "a11y":"FALSE", "priv":"FALSE", "acc_cod":"", "creator":"CREATOR", "crea_time":"100", "aper_time":"100", "close_time":"100"}' -H "Content-Type: application/json" -X POST http://127.0.0.1:5002/gymkhanas

#CREATE TABLE QUIZZ(QUIZZ_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, QUIZ_TEXT VARCHAR(255), SOL1 VARCHAR(255), SOL2 VARCHAR(255), SOL3 VARCHAR(255), SOL4 VARCHAR(255), CORRECT INT);
#INSERT INTO QUIZZ(QUIZ_TEXT, SOL1, SOL2, SOL3, SOL4, CORRECT) VALUES ("PRUEBA", "1", "2", "3", "4", 1);

#CREATE TABLE TEXT(TEXT_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, LONG_DESC VARCHAR(255));
#INSERT INTO TEXT(LONG_DESC) VALUES ('PRUEBA');

#CREATE TABLE POINT_ACT(POINT_ID INT NOT NULL, TEXT INT, QUIZZ INT, CONSTRAINT 'FK_ACCION_PUNTO' FOREIGN KEY (POINT_ID) REFERENCES POINT(POINT_ID) ON DELETE CASCADE ON UPDATE RESTRICT, CONSTRAINT 'FK_ACCION_PREGUNTA' FOREIGN KEY (QUIZZ) REFERENCES PREGUNTA(QUIZZ_ID) ON DELETE CASCADE ON UPDATE RESTRICT, CONSTRAINT 'FK_ACCION_TEXTO' FOREIGN KEY (TEXT) REFERENCES TEXT(TEXT_ID) ON DELETE CASCADE ON UPDATE RESTRICT);
#INSERT INTO POINT_ACT(POINT_ID, TEXT) VALUES (1,1);
#INSERT INTO POINT_ACT(POINT_ID, QUIZZ) VALUES (2,1);

#CREATE TABLE POINT(POINT_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, IMAGE BLOB, NAME VARCHAR(255), SHORT_DESC VARCHAR(512), LAT DECIMAL(10,8), LNG DECIMAL (11,8), GYMK_ID INT NOT NULL, CONSTRAINT 'FK_PUNTO_GYMK' FOREIGN KEY (GYMK_ID) REFERENCES GYMKHANAS(GYMK_ID) ON DELETE CASCADE ON UPDATE RESTRICT);
#INSERT INTO POINT(IMAGE, NAME, SHORT_DESC, LAT, LNG, GYMK_ID) VALUES ('AA','NOMBRE','MINI',1.1,1.1,1);
#INSERT INTO POINT(IMAGE, NAME, SHORT_DESC, LAT, LNG, GYMK_ID) VALUES ('AA','NOMBRE','MINI2',1.1,1.1,1);
