#include <Wire.h>

void getBonData();
void sendData(String temp);
void getDataForArray(char arrayName[]);

void setup() {
  Wire.begin();
  Serial.begin(9600);
  pinMode(5, OUTPUT);
}

char getData() {
  delay(100);
  while (Serial.available() == 0) {}
  delay(100);
  if (Serial.available() > 0) {
    char temp = Serial.read();
    return temp;
  }
}

void loop() {
  char temp = getData();
  if (temp == '1') {
    digitalWrite(5, HIGH);
    char c = '-';
    while (c == '-') {
      Wire.requestFrom(8, 1);
      while (Wire.available()) {
        c = Wire.read();
      }
    }
    digitalWrite(5, LOW);
    Serial.write(c);
  }
  else if (temp == '2') {
    getBonData();
  }
  else if (temp == '3') {
    getRFIDData();
  }
  temp = 'X';
}

void getBonData() {
  for (int i = 0; i < 12; i++) {
    getDataForArray();
  }
}

void getRFIDData() {
  char dataArray[16];
  dataArray[0] = '-';
  while (dataArray[0] == '-') {
    int index = 0;
    Wire.requestFrom(0x3C, 16);
    while (Wire.available()) {
      dataArray[index] = Wire.read();
      index++;
    }
    delay(10);
  }
  Serial.write(dataArray, 16);
}

void getDataForArray() {
  char str[30] = "";
  int index = 0;
  while (Serial.available() != 0) {}
  Serial.write("+");
  delay(50);
  while (Serial.available() > 0) {
    str[index] = Serial.read();
    index++;
  }
  Wire.beginTransmission(9);
  Wire.write(str);
  Wire.endTransmission();
  delay(50);
}
