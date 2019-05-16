# -*- coding: utf-8 -*-

import requests, base64, time
from datetime import datetime

URL = "http://localhost:5002/gymkhanas"

def create_text_point(image_url, lat, lng, name, short_desc, long_desc):
	return {
		'IMAGE': image_url,
		'LAT': lat,
		'LNG': lng,
		'NAME': name,
		'SHORT_DESC': short_desc,
		'LONG_DESC': long_desc
	}

def create_quizz_point(image_url, lat, lng, name, short_desc, quiz_text, sol1, sol2, sol3, sol4, correct):
	return {
		'IMAGE': image_url,
		'LAT': lat,
		'LNG': lng,
		'NAME': name,
		'SHORT_DESC': short_desc,
		'QUIZ_TEXT': quiz_text,
		'SOL1': sol1,
		'SOL2': sol2,
		'SOL3': sol3,
		'SOL4': sol4,
		'CORRECT': correct
	}

def create_gymk(image_url, name, desc, lat, lng, gymk_type, a11y, priv, acc_cod, creator, close_time, points):
	return {
		'IMAGE': image_url,
		'NAME': name,
		'DESCRIPCION': desc,
		'LAT': lat,
		'LNG': lng,
		'TYPE': gymk_type,
		'A11Y': a11y,
		'PRIV': priv,
		'ACC_COD': acc_cod,
		'CREATOR': creator,
		'CREA_DATE': int(time.time()),
		'APER_TIME': int(time.time()),
		'CLOSE_TIME': int(time.mktime(close_time.timetuple())),
		'POINTS': points
	}

points1 = [
		create_text_point('https://i.avoz.es/default/2014/12/07/00121417989423745121334/Foto/H05M2043.jpg', 43.333035, -8.409033, u'Área Científica', u'Área Científica', u'Edificio donde se desarrolló GymkhanaChain'),
		create_text_point('https://www.fic.udc.es/sites/default/files/styles/banner_inicio/public/banner/facultad-informatica-coruna.jpg', 43.332685, -8.410568, u'Facultade de Informática', u'Facultade de Informática', u'En esta facultad estudiarion el grupo de GymkhanaChain'),
		create_text_point('https://fotos02.laopinioncoruna.es/2016/01/23/318x200/universidade-abre.jpg', 43.332606, -8.412421, 'Plaza del campus', 'Plaza del campus', 'Disfruta del aire libre')
	]

gymk1 = create_gymk('http://consellosocial.udc.es/uploadedFiles/CSUDC.b7psr/fileManager/universidad_300112_68_LQ.jpg', 'Gymkhana de pruebas', 'Gymkhana de la UDC', 43.333516, -8.410707, '1', False, False, '', 'GymkhanaChain', datetime(2019, 6, 25, 0, 0), points1)
response = requests.post(url = URL, json = gymk1)
print response.text

points2 = [
		create_text_point('http://3.bp.blogspot.com/-OkWv5N7q7SI/VK7UwysnHbI/AAAAAAAABr0/4NC15Jubw8U/s1600/PlazaDelHumor.jpg', 43.371417, -8.397840, 'Plaza del Humor', 'Plaza del Humor', u'Esta es una plaza dedicada a las figuras del humor. Allí el visitante se encontrará con personajes inmortales del género, tanto creadores como figuras de series de cómics, libros y televisión.'),
		create_quizz_point('https://upload.wikimedia.org/wikipedia/commons/thumb/d/d2/Plaza_A_Coru%C3%B1a.JPG/1200px-Plaza_A_Coru%C3%B1a.JPG', 43.370897, -8.395806, u'Plaza de María Píta', u'Plaza de María Píta', u'Por esta plaza céntrica, dedicada a la heroína María Pita, pasa un meridiano. ¿Cúal es?', '8º24\'56"', '9º23\'39"', '8º24\'38"', '8º24\'39"', 3),
		create_text_point('http://www.turismo.gal/imaxes/mdaw/mduw/~edisp/~extract/TURGA050853~1~staticrendition/tg_carrusel_cabecera_grande.jpg', 43.369305, -8.407861, u'Playas de Coruña ', u'Playas de Coruña ', u'Coruña tiene dos quilómetros de playas urbanas repartidas en 6 localizaciones. Todas tiene bandera azul.'),
		create_text_point('https://saposyprincesas.elmundo.es/wp-content/uploads/2016/03/mendez_3.jpg', 43.367022, -8.403461, u'Jardines de Mendez Núñez ', u'Jardines de Mendez Núñez ', u'Los Jardines de Méndez Núñez son unos jardines de la ciudad de la Coruña situados entre Los Cantones y el puerto. Están dedicados al marino Casto Méndez Núñez, héroe de la Primera Guerra del Pacífico.')
	]

gymk2 = create_gymk('http://www.turismocoruna.com/web/galeria/Paseo_Maritimo_(Stephane_Lutier_2011)2.jpg', u'Conociendo la Coruña', '', 43.368673, -8.4023537, '2', False, False, '', 'GymkhanaChain', datetime(2019, 6, 25, 0, 0), points2)
response = requests.post(url = URL, json = gymk2)
print response.text

points3 = [
		create_text_point('', 43.370289, -8.393641, u'Praza de Azcárraga', u'Praza de Azcárraga', ''),
		create_text_point('', 43.369808, -8.394701, u'Igrexa de Santiago', u'Igrexa de Santiago', ''),
		create_text_point('', 43.369904, -8.392332, u'Ruteiros da Cidade Vella', u'Ruteiros da Cidade Vella', ''),
		create_text_point('', 43.369296, -8.391211, u'Arquivo do Reino de Galiza', u'Arquivo do Reino de Galiza', ''),
		create_text_point('', 43.370221, -8.391513, u'Colexio Santo Domingo', u'Colexio Santo Domingo', '')
	]

gymk3 = create_gymk('', 'Caminata pola Cidade Vella', u'Dende a colegiata de Santa María, imos pasar polos sitios máis recónditos desta fermosa cidade', 43.370821, -8.392935, '0', False, False, '', 'GymkhanaChain', datetime(2019, 6, 25, 0, 0), points3)
response = requests.post(url = URL, json = gymk3)
print response.text
