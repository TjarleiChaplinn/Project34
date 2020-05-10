#include <Wire.h>
#include <Keypad.h>

char pressedKey;

const byte ROWS = 4; //four rows
const byte COLS = 4; //four columns
//define the cymbols on the buttons of the keypads
char hexaKeys[ROWS][COLS] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};
byte colPins[ROWS] = {5, 4, 3, 2}; //connect to the row pinouts of the keypad
byte rowPins[COLS] = {9, 8, 7, 6}; //connect to the column pinouts of the keypad

//initialize an instance of class NewKeypad
Keypad customKeypad = Keypad( makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS); 

void setup() {
  Wire.begin(8);
  Wire.onRequest(requestEvent);
}

void loop() {
  pressedKey = '-';
  char key = customKeypad.getKey();
  while(key == NO_KEY){
    key = customKeypad.getKey();
  }
  pressedKey = key;
  delay(10);
}

void requestEvent() {
  Wire.write(pressedKey);
}
