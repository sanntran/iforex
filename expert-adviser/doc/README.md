1. 	Setup HTTP library:
	MetaEditor Navigator -> Include -> Right click -> Open Folder
	Copy file 'Wininet.mqh' and paste to folder 'Include'

2.	Setup Expert Advisors:
	MetaEditor Navigator -> Experts -> Right click -> Open Folder
	Copy 'strategy.mq4' and paste to folder 'Experts'
	
3. 	In MetaTrader Tools -> Options -> Expert Advisors
	- Allow automate trading
	- Allow DLL imports
	- Allow WebRequest for listed URLs

4. 	Start Expert Advidsors:
	Metatrader Navigator -> Expert Advisor -> Attach to chart

5. 	Stop Expert Advisors:
	Find Expert Advisor at top-right corner of chart -> Right click -> Expert Advisors -> Remove

6.  Run back testing
    Menu View -> Strategy Tester -> Select Expert Adviser -> ... -> Start 
    
