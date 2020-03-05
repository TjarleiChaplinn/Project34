#include "logo.h"
#include "bedankt.h"
#include "settings.h"
#include "Adafruit_Thermal.h"

#include "SoftwareSerial.h"
#define TX_PIN 5
#define RX_PIN 6

SoftwareSerial mySerial(RX_PIN, TX_PIN);
Adafruit_Thermal printer(&mySerial);

void setup() {
  mySerial.begin(9600);
  printer.begin();
  pinMode(8, INPUT_PULLUP);
}

void loop() {
  while(digitalRead(8) == 1){}
  printer.feed(1);

  printer.printBitmap(logo_width, logo_height, logo_data);
  
  printer.justify('C');
  printer.println(F("-------------------------------"));

  printer.boldOn();
  String tijd = (String)tijdUren + ":" + (String) tijdMinuten;
  printer.println(tijd);
  String addressFull = address + ", " + plaats;
  printer.println(addressFull);
  String devicenrFull = "Automaat nummer: " + (String)devicenr;
  printer.println(devicenrFull);
  printer.boldOff();

  printer.feed(1);
  printer.justify('L');
  String naamFull = "Naam: " + anaam + ", " + initialen + ".";
  printer.println(naamFull);
  printer.println(F("Rekening: NL95 INGB 47** **** **"));
  
  printer.justify('C');
  printer.println(F("-------------------------------"));
  printer.feed(1);

  printer.setSize('M');
  printer.println(bericht);
  String bedragFull = "Bedrag: " + (String)bedrag + " roebel";
  printer.println(bedragFull);

  printer.feed(1);
  printer.justify('C');
  printer.println(F("-------------------------------"));

  printer.printBitmap(bedankt_width, bedankt_height, bedankt_data);

  printer.feed(2);

  printer.sleep();
  delay(3000L);
  printer.wake();
  printer.setDefault();
}
