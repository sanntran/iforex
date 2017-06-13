
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
//|                                                       XForexEA.mq4 |
//|                                                         SannTran |
//|                                           https://www.xapxinh.net |
//+------------------------------------------------------------------+
#property version   "1.00"
#property strict
#include <mql4-http.mqh>
//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit() {
   //--- Create timer
   EventSetTimer(60);
   //---
   return(INIT_SUCCEEDED);
}
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason) {
//--- Destroy timer
   EventKillTimer();
      
}
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+
void OnTick() {
   MqlTick lastTick;
   string url = StringConcatenate("http://localhost:8008/iforex/api/ticks?symbol=", Symbol());
   //---
   if (SymbolInfoTick(Symbol(), lastTick)) {
      url = StringConcatenate(url, 
                              "&ticktime=", lastTick.time,
                              "&tickbid=", lastTick.bid, 
                              "&tickask=", lastTick.ask,
                              "&ticklast=", lastTick.last);
   }
   url = StringConcatenate(url, 
                              "&m5BarTime=", iTime(Symbol(),PERIOD_M5,0),
                              "&m5BarOpen=", iOpen(Symbol(),PERIOD_M5,0),
                              "&m5BarHigh=", iHigh(Symbol(),PERIOD_M5,0),
                              "&m5BarLow=", iLow(Symbol(),PERIOD_M5,0),
                              "&m5BarClose=", iClose(Symbol(),PERIOD_M5,0),
                              "&m5BarVolume=", iVolume(Symbol(),PERIOD_M5,0),
                              
                              "&m15BarTime=", iTime(Symbol(),PERIOD_M15,0),
                              "&m15BarOpen=", iOpen(Symbol(),PERIOD_M15,0),
                              "&m15BarHigh=", iHigh(Symbol(),PERIOD_M15,0),
                              "&m15BarLow=", iLow(Symbol(),PERIOD_M15,0),
                              "&m15BarClose=", iClose(Symbol(),PERIOD_M15,0),
                              "&m15BarVolume=", iVolume(Symbol(),PERIOD_M15,0),
                              
                              "&m30BarTime=", iTime(Symbol(),PERIOD_M30,0),
                              "&m30BarOpen=", iOpen(Symbol(),PERIOD_M30,0),
                              "&m30BarHigh=", iHigh(Symbol(),PERIOD_M30,0),
                              "&m30BarLow=", iLow(Symbol(),PERIOD_M30,0),
                              "&m30BarClose=", iClose(Symbol(),PERIOD_M30,0),
                              "&m30BarVolume=", iVolume(Symbol(),PERIOD_M30,0),
                              
                              "&h1BarTime=", iTime(Symbol(),PERIOD_H1,0),
                              "&h1BarOpen=", iOpen(Symbol(),PERIOD_H1,0),
                              "&h1BarHigh=", iHigh(Symbol(),PERIOD_H1,0),
                              "&h1BarLow=", iLow(Symbol(),PERIOD_H1,0),
                              "&h1BarClose=", iClose(Symbol(),PERIOD_H1,0),
                              "&h1BarVolume=", iVolume(Symbol(),PERIOD_H1,0),
                              
                              "&h4BarTime=", iTime(Symbol(),PERIOD_H4,0),
                              "&h4BarOpen=", iOpen(Symbol(),PERIOD_H4,0),
                              "&h4BarHigh=", iHigh(Symbol(),PERIOD_M5,0),
                              "&h4BarLow=", iLow(Symbol(),PERIOD_H4,0),
                              "&h4BarClose=", iClose(Symbol(),PERIOD_H4,0),
                              "&h4BarVolume=", iVolume(Symbol(),PERIOD_H4,0),
                              
                              "&d1BarTime=", iTime(Symbol(),PERIOD_D1,0),
                              "&d1BarOpen=", iOpen(Symbol(),PERIOD_D1,0),
                              "&d1BarHigh=", iHigh(Symbol(),PERIOD_D1,0),
                              "&d1BarLow=", iLow(Symbol(),PERIOD_D1,0),
                              "&d1BarClose=", iClose(Symbol(),PERIOD_D1,0),
                              "&d1BarVolume=", iVolume(Symbol(),PERIOD_D1,0));
   
   string decision = httpGET(url);

   Print("My machine's IP is ", decision);
   
}
//+------------------------------------------------------------------+
//| Timer function                                                   |
//+------------------------------------------------------------------+
void OnTimer() {
   
}

//+------------------------------------------------------------------+
//| Tester function                                                  |
//+------------------------------------------------------------------+

double OnTester() {
   double ret=0.0;
   return(ret);
}
//+------------------------------------------------------------------+
//| ChartEvent function                                              |
//+------------------------------------------------------------------+
void OnChartEvent(const int id,
                  const long &lparam,
                  const double &dparam,
                  const string &sparam) {
//---
   
}
//+------------------------------------------------------------------+