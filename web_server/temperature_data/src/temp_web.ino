#include <Arduino.h>
#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>
#include <U8g2lib.h>

const char* ssid = "swcore";
const char* password = "inha123*";
//OLED_U8G2 oled; // create oled object
U8G2_SH1106_128X64_NONAME_F_HW_I2C oled(U8G2_R0, /* reset=*/U8X8_PIN_NONE);
WebServer server(80);  // create WebServer object, port
int tempSensor = A2; // temperature sensor
// formula
int Vo;
double R1 = 10000;
double logR2, R2, T, Tc;
double c1 = 1.009249522e-03, c2 = 2.378405444e-04, c3 = 2.019202697e-07;
double Tf = 0;


void setup(void) {
  Serial.begin(115200);  // ESP32 baud rate
  oled.begin();
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");
  
  while (WiFi.status() != WL_CONNECTED) { // wait for connection
    delay(500);
    Serial.print("-> ");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP addr: ");
  Serial.println(WiFi.localIP()); // print assigned ip address
  
  server.on("/", handleRootEvent);  // root, event handling function
  server.begin();
  Serial.println("Web server started!");
}


void loop(void) {
  oled.setFont(u8g2_font_ncenB08_tr);
  oled.clearBuffer();
  oled.setCursor(0, 16);
  oled.println("Inha Factory");
  oled.setCursor(0, 32);
  oled.println("Web Server");
  oled.sendBuffer();
  server.handleClient();
  delay(5);
}


void handleRootEvent() {
  Serial.println("main page");
  Serial.print("main page from ");

  String clientIP = server.client().remoteIP().toString();  // client's ip addr.

  Vo = analogRead(tempSensor); // read from temperature sensing value
  R2 = R1 * (4095.0 / (float)Vo - 1.0);
  logR2 = log(R2);
  T = (1.0 / (c1 + c2*logR2 + c3*logR2*logR2*logR2));
  Tc = T - 273.15;  // celsius
  Tf = (Tc * 9.0/5.0) + 32.0;  // fahrenheit

  String message = "Welcome Inha SmartFactory WebServer!\n\n";
  message = message + "Temperature: " + String(Tc) + "C " + "(" + String(Tf) + "F)";
  message += "Your IP address: " + clientIP;
  message = message + "\nTemperature: " + String(Tc) + "C " + "(" + String(Tf) + "F)";
  server.send(200, "text/plain", message);  // status code 200(OK), format, message
  Serial.println(message); // monitoring

  Serial.println(clientIP);
  Serial.print(Tc);
  Serial.print("C (");
  Serial.print(Tf);
  Serial.println("F)");
}