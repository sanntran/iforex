
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
//|                                                       XForexEA.mq4 |
//|                                                         SannTran |
//|                                           https://www.xapxinh.net |
//+------------------------------------------------------------------+
#property version   "1.00"
#property strict
#include <Wininet.mqh>
#include <JAson.mqh>
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
   string url = "http://localhost:8080/iforex/ticks?symbol=" + Symbol();
   //---
   if (SymbolInfoTick(Symbol(), lastTick)) {
      url = StringConcatenate(url, 
                              "&ticktime=", lastTick.time,
                              "&tickbid=", lastTick.bid, 
                              "&tickask=", lastTick.ask,
                              "&ticklast=", lastTick.last);
   }   
   string decision = HttpGET(url);
   CJAVal json;
   json.Deserialize(decision);
   Print("Decistion action ", json["action"].ToStr());
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