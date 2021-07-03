
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
//|                                                SpeedMomentumEA.mq4 |
//|                                                         SannTran |
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
//+------------------------------------------------------------------+a
void OnDeinit(const int reason) {
//--- Destroy timer
   EventKillTimer();

}
//+------------------------------------------------------------------+
//| Expert tick function                                            |
//+------------------------------------------------------------------+
void OnTick() {
   MqlTick lastTick;
   if (Volume[0]==1 ) {
      //new candle created
   }
   //---
   if (SymbolInfoTick(Symbol(), lastTick)) {
      string url = "http://localhost:8080/iforex/ticks?method=POST"
                  + "&symbol=" + Symbol()
                  + "&time=" + FormatOffsetDateTime(lastTick.time)
                  + "&bid=" + lastTick.bid
                  + "&ask=" + lastTick.ask;
      CJAVal json = SendHttpGET(url);
      if (json["code"].ToInt() == 0) { // nothing
         // candle update
      } else if (json["code"].ToInt() == 1) { // place order
         // candle closed
         getActionOnCandleClosed();
      }
   }
}

// format ofsetdate time to url encoded
string FormatOffsetDateTime(datetime time) {
   int hourOffset = (TimeHour(TimeCurrent()) - TimeHour(TimeGMT()));
   int minuteOffset = (TimeMinute(TimeCurrent()) - TimeMinute(TimeGMT()));
   if (minuteOffset < 0) {
      minuteOffset = 60 + minuteOffset;
      hourOffset = hourOffset - 1;
   }
   string timeZoneOffset = StringFormat("%s%02i:%02i", hourOffset >=0 ? "%2B" : "-", hourOffset, minuteOffset);
   string dateString = TimeToStr(time, TIME_DATE);
   StringReplace(dateString, ".", "-") ;
   return dateString  + "T" + TimeToStr(time, TIME_SECONDS) + timeZoneOffset;
}

//+------------------------------------------------------------------+
//| Timer function                                                   |
//+------------------------------------------------------------------+
void OnTimer() {

}

void getActionOnCandleClosed() {
   string url = "http://localhost:8080/iforex/actions?method=GET"
         + "&symbol=" + OrderSymbol();

   int openOrders = 0;
   for( int i = 0 ; i < OrdersTotal() ; i++ ) {
      // select the order of index i selecting by position and from the pool of market/pending trades
      if (OrderSelect(i, SELECT_BY_POS, MODE_TRADES) && (OrderType()==OP_BUY || OrderType()==OP_SELL)) {
         string url = "http://localhost:8080/iforex/actions?method=GET"
                        + "&symbol=" + OrderSymbol()
                        + "&ticket=" + OrderTicket()
                        + "&type=" + OrderType()
                        + "&lots=" + OrderLots()
                        + "&openPrice=" + OrderOpenPrice()
                        + "&openTime=" + FormatOffsetDateTime(OrderOpenTime())
                        + "&profit=" + OrderProfit()
                        + "&stopLoss=" + OrderStopLoss()
                        + "&takeProfit=" + OrderTakeProfit()
                        + "&swap=" + OrderSwap()
                        + "&comment=" + OrderComment();
         openOrders ++;
         CJAVal json = SendHttpGET(url);
         handleActionResponse(json);
      }
   }
   if (openOrders == 0) {
      string url = "http://localhost:8080/iforex/actions?method=GET"
            + "&symbol=" + OrderSymbol();
      Print("URL ", url);
      CJAVal json = SendHttpGET(url);
      handleActionResponse(json);
   }
}

void handleActionResponse(CJAVal &json) {
   Print("Action response ", json["code"].ToStr());
   if (json["code"].ToInt() == 0) { // nothing
      // nothing to do
      // Print("No action to do");
   } else if (json["code"].ToInt() == 1) { // place order
      Print("Place new order");
      PlaceOrder(json["order"]["type"].ToInt(), json["order"]["lots"].ToDbl(),
                  json["body"]["stopLoss"].ToDbl(), json["order"]["takeProfit"].ToDbl());
   } else if (json["code"].ToInt() == 2) { // close order
      CloseOrder(json["order"]["ticket"].ToInt());
   } else if (json["code"].ToInt() == 3) { // modify order
      ModifyOrder(json["order"]["ticket"].ToInt(),
                  json["order"]["stopLoss"].ToDbl(), json["order"]["takeProfit"].ToDbl());
   }
}

bool PlaceOrder(int type, double lots, double stopLoss, double takeProfit) {
   if (type == OP_BUY) {
      return OrderSend(Symbol(), OP_BUY, 0.1, Ask, 3, 0, 0, "", 0, 0, Blue);
      // return OrderSend(Symbol(), OP_BUY, lots, Bid, 2*Point, stopLoss, takeProfit);
   } else if (type == OP_SELL) {
      OrderSend(Symbol(), OP_SELL, 0.1, Bid, 3, 0, 0, "" , 0, 0, Red);
      // return OrderSend(Symbol(), OP_SELL, lots, Ask, 2*Point, stopLoss, takeProfit);
   }
   return false;
}

bool ModifyOrder(int ticket, double stopLoss, double takeProfit) {
   bool selected = OrderSelect(ticket, SELECT_BY_TICKET);
   if (!selected) {
      return false;
   }
   return OrderModify(ticket, OrderOpenPrice(), stopLoss, takeProfit, 0, Blue);
}

bool CloseOrder(int ticket) {
   bool selected = OrderSelect(ticket, SELECT_BY_TICKET);
   if (!selected) {
      return false;
   }
   return OrderClose(ticket, OrderLots(), OrderType() == OP_BUY ? Bid : Ask, 2*Point);
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