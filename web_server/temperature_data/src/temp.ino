#include <Arduino.h>
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
#include <WiFiClient.h>
#include <WiFi.h> 
#include <stdio.h>

//wifi info
const char* ssid = "SmartFactory";
const char* password = "inha4885";

IPAddress server_addr(127,20,10,7);
char user[] = "root";
char password_[] = "ejk127ekqls$";

WiFiClient client;
MySQL_Connection conn(&client);
MySQL_Cursor* cursor;

char INSERT_SQL[100] = "INSERT INTO temperature_data (temperature) VALUES (%f)";

int tempsensor = A2; 
int Vo;
float R1 = 10000;
float logR2, R2, T, Tc;
float c1 = 1.009249522e-03, c2 = 2.378405444e-04, c3 = 2.019202697e-07;

void setup() {
  Serial.begin(115200);
  Wifi_connect();
  //getLocalTime();

   Serial.print("Connecting to SQL...  ");
  if (conn.connect(server_addr, 3306, user, password_)) {
    Serial.println("OK.");
  }
  else {
    Serial.println("FAILED.");
  }
  // create MySQL cursor object
  cursor = new MySQL_Cursor(&conn);
}

void loop() {
  Vo = analogRead(tempsensor);
  R2 = R1 * (4095.0 / (float)Vo - 1.0);
  logR2 = log(R2);
  T = (1.0 / (c1 + c2*logR2 + c3*logR2*logR2*logR2));
  Tc = T - 273.15;

  if (WiFi.status() != WL_CONNECTED) { //재접속
    Serial.println("try reconnect");
    Wifi_connect();
  }

  sprintf(INSERT_SQL, "INSERT INTO temperature_data (temperature) VALUES (%f)", Tc);

  if (conn.connected()) {
    cursor->execute(INSERT_SQL); //실제 excute되는 시점
  }
  Serial.println(Tc);
  delay(500);

}

void Wifi_connect() {

  Serial.print("WiFi : ");
  Serial.println(ssid);
  WiFi.begin(ssid, password); 
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print("--------------");
  }

  Serial.println();
  Serial.println("Wifi connected!");
  Serial.println("\nConnected to network");
  Serial.print("My IP address : ");
  Serial.println(WiFi.localIP());
}
