/*  NETPIE ESP32 basic sample                            */
/*  More information visit : https://netpie.io             */

#include <WiFi.h>
#include <MicroGear.h>
#include <DHTesp.h>
#include <Ticker.h>

const char* ssid     = "TW";
const char* password = "tawan0011";

#define APPID   "TemperatureMonitoring"
#define KEY     "qsl7mPMfXo4gbkG"
#define SECRET  "dGbb12DP6F3PngaDno0nEZkIe"
#define ALIAS   "esp32_node1"
#define TARGET  "android"

String ldr;

WiFiClient client;
int timer = 0;
MicroGear microgear(client);


/* If a new message arrives, do this */
void onMsghandler(char *topic, uint8_t* msg, unsigned int msglen) {
    Serial.print("Incoming message --> ");
    msg[msglen] = '\0';
    Serial.println((char *)msg);
}

void onFoundgear(char *attribute, uint8_t* msg, unsigned int msglen) {
    Serial.print("Found new member --> ");
    for (int i=0; i<msglen; i++)
        Serial.print((char)msg[i]);
    Serial.println();  
}

void onLostgear(char *attribute, uint8_t* msg, unsigned int msglen) {
    Serial.print("Lost member --> ");
    for (int i=0; i<msglen; i++)
        Serial.print((char)msg[i]);
    Serial.println();
}

/* When a microgear is connected, do this */
void onConnected(char *attribute, uint8_t* msg, unsigned int msglen) {
    Serial.println("Connected to NETPIE...");
    /* Set the alias of this microgear ALIAS */
    microgear.setAlias(ALIAS);
}


void setup() {
  Serial.begin(115200); 
  Serial.println("LDR ESP32 is Start now !");
  Serial.println();


  ldr ="ldr$";
    /* Add Event listeners */

    /* Call onMsghandler() when new message arraives */
    microgear.on(MESSAGE,onMsghandler);

    /* Call onFoundgear() when new gear appear */
    microgear.on(PRESENT,onFoundgear);

    /* Call onLostgear() when some gear goes offline */
    microgear.on(ABSENT,onLostgear);

    /* Call onConnected() when NETPIE connection is established */
    microgear.on(CONNECTED,onConnected);

    Serial.begin(115200);
    Serial.println("Starting...");

    /* Initial WIFI, this is just a basic method to configure WIFI on ESP8266.                       */
    /* You may want to use other method that is more complicated, but provide better user experience */
    if (WiFi.begin(ssid, password)) {
        while (WiFi.status() != WL_CONNECTED) {
            delay(500);
            Serial.print(".");
        }
    }

    Serial.println("WiFi connected");  
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

    /* Initial with KEY, SECRET and also set the ALIAS here */
    microgear.init(KEY,SECRET,ALIAS);

    /* connect to NETPIE to a specific APPID */
    microgear.connect(APPID);
}

void loop() {
  int sensorValue = analogRead(A0); 
  Serial.println(sensorValue); 
  delay(2000);


     /* To check if the microgear is still connected */
    if (microgear.connected()) {
        Serial.println("connected");

        
        /* Call this method regularly otherwise the connection may be lost */
        microgear.loop();
        microgear.chat(TARGET,ldr+sensorValue);
       
    }
    else {
       
         microgear.connect(APPID);
          
    }
    
}
