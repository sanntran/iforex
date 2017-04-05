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
   //---

   MqlTick lastTick;
   string url = StringConcatenate("http://localhost:8008/iforex/api/decisions?symbol=", Symbol());
   //---
   if (SymbolInfoTick(Symbol(), lastTick)) {
      url = StringConcatenate(url, 
                              "&ticktime=", lastTick.time,
                              "&tickbid=", lastTick.bid, 
                              "&tickask=", lastTick.ask,
                              "&ticklast=", lastTick.last);
   }
   else {
      url = StringConcatenate(url, 
                              "&error=", GetLastError());
   }

   url = StringConcatenate(url, 
                              "&bartime=", iTime(Symbol(),PERIOD_M1,0),
                              "&baropen=", iOpen(Symbol(),PERIOD_M1,0),
                              "&barhigh=", iHigh(Symbol(),PERIOD_M1,0),
                              "&barlow=", iLow(Symbol(),PERIOD_M1,0),
                              "&barclose=", iClose(Symbol(),PERIOD_M1,0),
                              "&barvolume=", iVolume(Symbol(),PERIOD_M1,0));
   
   string decision = httpGET(url);

   Print("My machine's IP is ", decision);

}
//+------------------------------------------------------------------+
//| Timer function                                                   |
//+------------------------------------------------------------------+
void OnTimer() {
//---
   
}
//+------------------------------------------------------------------+
//| Tester function                                                  |
//+------------------------------------------------------------------+
double OnTester() {
//---
   double ret=0.0;
//---

//---
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
