
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
   EventSetTimer(5); //every 5 seconds
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

   //---
   if (SymbolInfoTick(Symbol(), lastTick)) {
      string url = "http://localhost:8080/iforex/ticks?method=post"
                  + "&symbol=" + Symbol()
                  + "&time=" + lastTick.time
                  + "&bid=" + lastTick.bid
                  + "&ask=" + lastTick.ask
                  + "&last=" + lastTick.last;
   }
   //string decision = HttpGET(url);
   //CJAVal json;
   //json.Deserialize(decision);
   //Print("Decistion action ", json["action"].ToStr());
}

//+------------------------------------------------------------------+
//| Timer function                                                   |
//+------------------------------------------------------------------+
void OnTimer() {
   int totalOrders = OrdersTotal();
   Print(totalOrders);
   for( int i = 0 ; i < OrdersTotal() ; i++ ) {
      // select the order of index i selecting by position and from the pool of market/pending trades
      OrderSelect( i, SELECT_BY_POS, MODE_TRADES ); // select active/opening order by index of order in order pool
      string url = "http://localhost:8080/iforex/orders?method=put"
            + "&symbol=" + OrderSymbol()
            + "&ticket=" + OrderTicket()
            + "&type=" + OrderType()
            + "&lots=" + OrderLots()
            + "&openPrice=" + OrderOpenPrice()
            + "&openTime=" + OrderOpenTime()
            + "&profit=" + OrderProfit()
            + "&stopLoss=" + OrderStopLoss()
            + "&takeProfit=" + OrderTakeProfit()
            + "&swap=" + OrderSwap()
            + "&comment=" + OrderComment()
            // + "&comission=" + OrderCommission()
            + "&expiration" + OrderExpiration();

   CJAVal json = SendHttpGET(url);
   Print("Response ", json["code"].ToStr());
   }

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