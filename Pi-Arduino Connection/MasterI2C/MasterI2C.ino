#include <Wire.h>

void getBonData();
void sendData(String temp);
void getDataForArray(char arrayName[]);

void setup(){
  Wire.begin();
  Serial.begin(9600);
  pinMode(10, OUTPUT);
}

char getData(){
  delay(100);
  while(Serial.available() == 0){}
  delay(100);
  if(Serial.available() > 0){
    char temp = Serial.read();
    return temp;
  }
}

void loop(){
  char temp = getData();
  if(temp == '1'){
    char c = '-';
    while(c == '-'){
      Wire.requestFrom(8,1);
      while(Wire.available()){
        c = Wire.read();
      }
    }
    digitalWrite(10, HIGH);
    Serial.write(c);
  }
  else if(temp == '2'){
    getBonData();
  }
  temp = 'X';
}

void getBonData(){
  for(int i = 0; i < 12; i++){
    getDataForArray();
  }
}

void getDataForArray(){
  char str[30] = "";
  int index = 0;
  while(Serial.available() != 0){}
  Serial.write("+");
  delay(50);
  while(Serial.available() > 0){
    str[index] = Serial.read();
    index++;
  }
  Wire.beginTransmission(9);
  Wire.write(str);
  Wire.endTransmission();
  delay(50);
}
