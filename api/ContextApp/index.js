const express = require('express');
const mysql = require('mysql');
const bodyParser = require('body-parser');
const db = mysql.createConnection({
  host     : 'localhost',
  user     : 'context',
  password : '123456',
  database : 'context',
  multipleStatements : true
});
// Connect
db.connect((err) => {
    if(err){
        throw err;
    }
    console.log('MySql Connected...');
});

const app = express();

app.use(express.json());
//app.use(bodyParser.json()); // support json encoded bodies
//app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

app.get('/', function (req, res) {
	let sql = 'SELECT 1 + 1 AS solution';
    let query = db.query(sql, (err, rows, fields) => {
        if(err) throw err;
        res.send("Hallo" + rows[0].solution);
    });
})

/*
Funktion: Startet die Messung
erforderliche Felder: AndroidID
Fehlercodes:	400 Bad Request: androidID nicht angegeben
				409 Conflict: Messung wurde schon gestartet
				500 Internal Server Error: SQL Statement falsch oder Fehler in der DB
Erfolgscodes: 	201 Created: Messung wurde angelegt

Todo: 			
*/
app.post('/starteMessung', function (req, res) {
	
	if (req.body.AndroidID != null)
	{
		var androidID = mysql.escape(req.body.AndroidID);
		let sql = 'SELECT COUNT(*) AS count FROM Datensatz WHERE Android_ID=' + androidID + ' AND ISNULL(Messung_Ende);';
		let query = db.query(sql, (err, rows, fields) => {
			if (err)
			{
				res.status(500).send('SQL Statement fehler:' + err);
			}
			else
			{
				if (rows[0].count)
				{
					res.status(409).send();
				}
				else
				{				
					let sql = 'INSERT INTO Datensatz (Android_ID, Messung_Start) VALUES (' + androidID + ', NOW());';
					let query = db.query(sql, (err, result) => {
						if(err)
						{
							res.status(500).send();
						}
						else
						{
							if (result.affectedRows)
							{
								// Datensatz erfolgreich erstellt
								res.status(201).send();
							}
							else
							{
								res.status(500).send();
							}
						}
					});
				}
			}
		});
	}
	else
	{
		res.status(400).send();
	}
})
/*
Funktion: Beendet die Messung
erforderliche Felder: AndroidID
Fehlercodes:	400 Bad Request: androidID nicht angegeben
				409 Conflict: Es gibt keine Messung die beendet werden kann
				500 Internal Server Error: SQL Statement falsch oder Fehler in DB
Erfolgscodes: 	200 OK: Messung beendet

Todo: 			
*/
app.post('/beendeMessung', function (req, res) {
	if (req.body.AndroidID != null)
	{	
		var androidID = mysql.escape(req.body.AndroidID);
		let sql = 'UPDATE Datensatz SET Messung_Ende=NOW() WHERE Android_ID=' + androidID + ' AND ISNULL(Messung_Ende);';
		let query = db.query(sql, (err, result) => {
			if(err)
			{
				res.status(500).send();
			}
			else
			{
				if (result.changedRows)
				{
					// Datensatz geändert
					res.status(200).send();
				}
				else
				{
					res.status(409).send();
				}
			}
		});
	}
	else
	{
		res.status(400).send();
	}
})
/*
Aufruf (Sensoren sind optional es muss aber mindestens einer vorhanden sein):
[
{
"AndroidID":"2541525"
},
{
"Sensortyp":"Accelometer",
"Zeitstempel":"0.12515",
"X":"0.548412",
"Y":"4.4555",
"Z":"8.752"
},
{
"Sensortyp":"Gyroscope",
"Zeitstempel":"0.12515",
"X":"0.548412",
"Y":"4.4555",
"Z":"8.752"
},
{
"Sensortyp":"Location",
"Zeitstempel":"0.12515",
"Long":"0.548412",
"Lat":"4.4555"
},
{
"Sensortyp":"Licht",
"Zeitstempel":"0.12515",
"CD":"64.87"
}
]
Funktion: Schreibt die Sensordaten
erforderliche Felder: 	AndroidID, Sensortyp, Zeitstempel
						Abhängig von Sensor:
						Accelometer: X, Y, Z
						Gyroscope: X, Y, Z
						Location: Long, Lat,
						Licht: CD.
Fehlercodes:	400 Bad Request: Erforderliche Felder nicht angegeben oder Format falsch
				409 Conflict: Es gibt keine Messung die gestartet ist.
				500 Internal Server Error: SQL Statement falsch oder Fehler in DB
Erfolgscodes: 	201 Created: Sensorwert wurde gespeichert.

Todo: 
*/
app.post('/schreibeSensor', function (req, res) {
	if (req.body[0].AndroidID != null)
	{
		var androidID = mysql.escape(req.body[0].AndroidID);
		var sql = 'SELECT COUNT(*) AS count FROM Datensatz WHERE Android_ID=' + androidID + ' AND ISNULL(Messung_Ende);';
		let query = db.query(sql, (err, rows, fields) => {
			if (err)
			{
				res.status(500).send();
			}
			else
			{
				if (rows[0].count)
				{
					let sqlquery ="";

					// Baue SQL String auf
					for (var i = 1; i < req.body.length; i++)
					{
						// Variablen
						let X;
						let Z;
						let Y;
						if (req.body[i].Sensortyp != null && req.body[i].Zeitstempel != null)
						{
							let Zeitstempel = mysql.escape(req.body[i].Zeitstempel);
							switch (req.body[i].Sensortyp)
							{
								case "Accelometer":
									if (req.body[i].X != null && req.body[i].Y != null && req.body[i].Z != null)
									{
										X = mysql.escape(req.body[i].X);
										Z = mysql.escape(req.body[i].Y);
										Y = mysql.escape(req.body[i].Z);
										sqlquery += 'INSERT INTO Sensor_Accel (Android_ID, Messung_Start, zeit, X, Y, Z) VALUES (' + androidID + ', (SELECT Messung_Start FROM Datensatz WHERE Android_ID=' + androidID + ' AND ISNULL(Messung_Ende)), ' + Zeitstempel + ', ' + X + ', ' + Y + ', ' + Z + ');';
									}
									else
									{
										res.status(400).send();
									}
								break;
								case "Gyroscope":
									if (req.body[i].X != null && req.body[i].Y != null && req.body[i].Z != null)
									{
										X = mysql.escape(req.body[i].X);
										Z = mysql.escape(req.body[i].Y);
										Y = mysql.escape(req.body[i].Z);
										sqlquery += 'INSERT INTO Sensor_Gyro (Android_ID, Messung_Start, zeit, X, Y, Z) VALUES (' + androidID + ', (SELECT Messung_Start FROM Datensatz WHERE Android_ID=' + androidID + ' AND ISNULL(Messung_Ende)), ' + Zeitstempel + ', ' + X + ', ' + Y + ', ' + Z + ');';
									}
									else
									{
										res.status(400).send();
									}
								break;
								case "Rotation":
									if (req.body[i].X != null && req.body[i].Y != null && req.body[i].Z != null)
									{
										X = mysql.escape(req.body[i].X);
										Z = mysql.escape(req.body[i].Y);
										Y = mysql.escape(req.body[i].Z);
										sqlquery += 'INSERT INTO Sensor_Rota (Android_ID, Messung_Start, zeit, X, Y, Z) VALUES (' + androidID + ', (SELECT Messung_Start FROM Datensatz WHERE Android_ID=' + androidID + ' AND ISNULL(Messung_Ende)), ' + Zeitstempel + ', ' + X + ', ' + Y + ', ' + Z + ');';
									}
									else
									{
										res.status(400).send();
									}
								break;
								case "Magnetfeld":
									if (req.body[i].X != null && req.body[i].Y != null && req.body[i].Z != null)
									{
										X = mysql.escape(req.body[i].X);
										Z = mysql.escape(req.body[i].Y);
										Y = mysql.escape(req.body[i].Z);
										sqlquery += 'INSERT INTO Sensor_Mag (Android_ID, Messung_Start, zeit, X, Y, Z) VALUES (' + androidID + ', (SELECT Messung_Start FROM Datensatz WHERE Android_ID=' + androidID + ' AND ISNULL(Messung_Ende)), ' + Zeitstempel + ', ' + X + ', ' + Y + ', ' + Z + ');';
									}
									else
									{
										res.status(400).send();
									}
								break;
								default:
									res.status(400).send();
								break;				
							}
						}
						else
						{
							res.status(400).send();
						}
					}
					//console.log(sqlquery);
					let query = db.query(sqlquery, (err, result) => {
						if(err)
						{
							res.status(500).send(err);
						}
						else
						{
								// Datensatz erfolgreich erstellt
								res.status(201).send();
						}
					});
				}
				else
				{	
					// Keine Messung gestartet
					res.status(409).send();
				}
			}
		});
	}
	else
	{
		res.status(400).send();
	}
})

app.listen(80, function () {
})