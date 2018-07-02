#include <Wire.h>
#include <SSD1306.h>
#include <WiFi.h>
#include <esp_wifi.h>
#include <WiFiClient.h>
#include <WiFiUdp.h>

//WiFi-SoftAP credentials
#define ssid "TestWlan"
#define password "12345678"

WiFiUDP Udp;
SSD1306 display(0x3c, 5, 4);

void setup() {

  Serial.begin(115200);

  WiFi.mode(WIFI_AP);
  WiFi.softAP(ssid, password);

  display.init();
  display.flipScreenVertically();
  display.setFont(ArialMT_Plain_16);
  display.setTextAlignment(TEXT_ALIGN_LEFT);
  display.clear();
  display.drawStringMaxWidth(0, 0, 128, "Boot" );
  display.display();

  Udp.begin(1234);

  display.clear();
  display.drawStringMaxWidth(0, 0, 128, "Connected!" );
  display.display();

}

String recev;
unsigned long lastMillis = 0;

void loop() {

  if (millis() - lastMillis > 20) {
    lastMillis = millis();
    char incomingPacket[255];
    int packetSize = Udp.parsePacket();
    if (packetSize) {
      int len = Udp.read(incomingPacket, 255);
      if (len > 0)
        incomingPacket[len] = 0;
      recev = String(incomingPacket);

      int semindex = recev.indexOf(";");
      String gas = recev.substring(semindex + 1);
      int pos = recev.substring(0, semindex).toInt();
      //void fillRect(int16_t x, int16_t y, int16_t width, int16_t height);
      //void drawProgressBar(uint16_t x, uint16_t y, uint16_t width, uint16_t height, uint8_t progress);

      display.clear();
      display.drawProgressBar(10, 50, 108, 10, gas.toInt());
      display.drawStringMaxWidth(0, 0, 128, recev);
      if (pos == 0) {
        display.drawString(61, 20, "^");
      } if (pos == 1) {
        display.drawString(61, 20, "<");
      } if (pos == 2) {
        display.drawString(61, 20, ">");
      }
      display.display();
    }
  }
}
