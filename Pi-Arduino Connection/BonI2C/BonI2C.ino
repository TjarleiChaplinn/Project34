#include "logo.h"
#include "bedankt.h"
#include "Adafruit_Thermal.h"
#include "SoftwareSerial.h"
#include <Wire.h>

#define GEGEVENS_SIZE 12

#define TX_PIN 5
#define RX_PIN 6

/* Gegevens Array
 * 0: Address
 * 1: Plaats
 * 2: Bedrag
 * 3: Bericht
 * 4: Transnr
 * 5: Rekening
 * 6: Devicenr
 * 7: Dag
 * 8: Maand
 * 9: Jaar
 * 10:  tijdUren
 * 11:  tijdMinuten
 */
String gegevens[GEGEVENS_SIZE] = {"", "", "", "", "", "", "", "", "", "", "", ""};

bool printBonBool = false;

SoftwareSerial mySerial(RX_PIN, TX_PIN);
Adafruit_Thermal printer(&mySerial);

void setup() {
  Wire.begin(9);
  Wire.onReceive(receiveEvent);
  Serial.begin(9600);
  while (!Serial) {}
  mySerial.begin(9600);
  printer.begin();
}

void loop() {
  if(printBonBool){
    printBon();
    for(int i = 0; i < GEGEVENS_SIZE; i++){
      gegevens[i] = "";
    }
    printBonBool = false;
  }
}

int arrayNumber = 0;

void receiveEvent() {
  while (0 < Wire.available()) {
    char c = Wire.read();
    gegevens[arrayNumber] += c;
  }
  Serial.println(gegevens[arrayNumber]);
  if(arrayNumber == GEGEVENS_SIZE - 1){
    Serial.println("print bon");
    printBonBool = true;
    arrayNumber = 0;
  }
  else{
    arrayNumber++;
  }
}

void printBon() {
  printer.feed(1);

  printer.printBitmap(logo_width, logo_height, logo_data);

  printer.justify('C');
  printer.println(F("-------------------------------"));

  printer.boldOn();
  String tijd = gegevens[7] + "/" + gegevens[8] + "/"  + gegevens[9] + "  " + gegevens[10] + ":" + gegevens[11];
  printer.println(tijd);
  String addressFull = gegevens[0] + ", " + gegevens[2];
  printer.println(addressFull);
  String devicenrFull = "Automaat nummer: " + gegevens[6];
  printer.println(devicenrFull);
  printer.boldOff();

  printer.feed(1);
  printer.justify('L');
  String transFull = "Transactie nr.: " + gegevens[4];
  printer.println(transFull);
  String rekeningFull = "Rekening: " + gegevens[5] + "** **** **";
  printer.println(rekeningFull);

  printer.justify('C');
  printer.println(F("-------------------------------"));
  printer.feed(1);

  printer.setSize('M');
  printer.println(gegevens[3]);
  String bedragFull = "Bedrag: " + gegevens[2] + " roebel";
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
