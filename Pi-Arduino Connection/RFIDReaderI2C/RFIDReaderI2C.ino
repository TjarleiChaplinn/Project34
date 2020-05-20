#include <MFRC522.h>
#include <SPI.h>
#include <Wire.h>

#define RST_PIN 5
#define SS_PIN 10

#define ARRAY_LENGTH 18
#define ARRAY_CHAR_LENGTH 16

bool requestData = false;

MFRC522 mfrc522(SS_PIN, RST_PIN);

MFRC522::MIFARE_Key key;

byte dataArray[ARRAY_LENGTH];

char dataArrayChar[ARRAY_CHAR_LENGTH];

bool rfid_tag_present_prev = false;
bool rfid_tag_present = false;
int _rfid_error_counter = 0;
bool _tag_found = false;

void setup() {
  Wire.begin(0x3C);
  Wire.onRequest(requestEvent);
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();
  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }
  resetArray();
}

void loop() {
  if (!requestData) {
    return;
  }
  rfid_tag_present_prev = rfid_tag_present;

  _rfid_error_counter += 1;
  if(_rfid_error_counter > 2){
    _tag_found = false;
  }

  // Detect Tag without looking for collisions
  byte bufferATQA[2];
  byte bufferSize = sizeof(bufferATQA);

  // Reset baud rates
  mfrc522.PCD_WriteRegister(mfrc522.TxModeReg, 0x00);
  mfrc522.PCD_WriteRegister(mfrc522.RxModeReg, 0x00);
  // Reset ModWidthReg
  mfrc522.PCD_WriteRegister(mfrc522.ModWidthReg, 0x26);

  MFRC522::StatusCode result = mfrc522.PICC_RequestA(bufferATQA, &bufferSize);

  if(result == mfrc522.STATUS_OK){
    if ( ! mfrc522.PICC_ReadCardSerial()) { //Since a PICC placed get Serial and continue   
      return;
    }
    _rfid_error_counter = 0;
    _tag_found = true;        
  }
  
  rfid_tag_present = _tag_found;
  if (rfid_tag_present && !rfid_tag_present_prev){
    readBlock(1, dataArray);
    dataArrayToChar();
    requestData = false;
  }
}

void dataArrayToChar(){
  for(int i = 0; i < ARRAY_CHAR_LENGTH; i++){
    dataArrayChar[i] = (char) dataArray[i];
  }
}

void resetArray(){
  for (int i = 0; i < ARRAY_LENGTH; i++) {
    dataArray[i] = 0x2D;
  }
  for (int i = 0; i < ARRAY_CHAR_LENGTH; i++){
    dataArrayChar[i] = '-';
  }
}

void requestEvent() {
  requestData = true;
  delay(10);
  Wire.write(dataArrayChar, ARRAY_LENGTH);
  while(Wire.available() > 0){}
  resetArray();
}

int readBlock(int blockNumber, byte arrayAddress[])
{
  int largestModulo4Number = blockNumber / 4 * 4;
  int trailerBlock = largestModulo4Number + 3; //determine trailer block for the sector

  //authentication of the desired block for access
  byte status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));

  if (status != MFRC522::STATUS_OK) {
//    Serial.print("PCD_Authenticate() failed (read): ");
//    Serial.println(mfrc522.GetStatusCodeName(status));
    return 3;//return "3" as error message
  }

  //reading a block
  byte buffersize = 18;//we need to define a variable with the read buffer size, since the MIFARE_Read method below needs a pointer to the variable that contains the size...
  status = mfrc522.MIFARE_Read(blockNumber, arrayAddress, &buffersize);//&buffersize is a pointer to the buffersize variable; MIFARE_Read requires a pointer instead of just a number
  if (status != MFRC522::STATUS_OK) {
//    Serial.print("MIFARE_read() failed: ");
//    Serial.println(mfrc522.GetStatusCodeName(status));
    return 4;//return "4" as error message
  }
//  Serial.println("block was read");
}
