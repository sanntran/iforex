//+----------------------------------------------------------------------------+
//|                                                              mql4-http.mqh |
//+----------------------------------------------------------------------------+
//|                                                      Built by Sergey Lukin |
//|                                                    contact@sergeylukin.com |
//|                                                                            |
//| This libarry is highly based on following:                                 |
//|                                                                            |
//| - HTTP Wininet sample: http://codebase.mql4.com/8115                       |
//| - EasyXML parser: http://www.mql5.com/code/1998                            |
//|                                                                            |
//+----------------------------------------------------------------------------+
#include <JAson.mqh>

#import "shell32.dll"
int ShellExecuteW(
    int hwnd,
    string Operation,
    string File,
    string Parameters,
    string Directory,
    int ShowCmd
);
#import

#import "wininet.dll"
int InternetOpenW(
    string     sAgent,
    int        lAccessType,
    string     sProxyName="",
    string     sProxyBypass="",
    int     lFlags=0
);
int InternetOpenUrlW(
    int     hInternetSession,
    string     sUrl,
    string     sHeaders="",
    int     lHeadersLength=0,
    uint     lFlags=0,
    int     lContext=0
);
int InternetReadFile(
    int     hFile,
    uchar  &   sBuffer[],
    int     lNumBytesToRead,
    int&     lNumberOfBytesRead
);
int InternetCloseHandle(
    int     hInet
);
#import

#define INTERNET_FLAG_RELOAD            0x80000000
#define INTERNET_FLAG_NO_CACHE_WRITE    0x04000000
#define INTERNET_FLAG_PRAGMA_NOCACHE    0x00000100

int HSession_IEType;
int HSession_Direct;
int Internet_Open_Type_Preconfig = 0;
int Internet_Open_Type_Direct = 1;

int HSession(bool Direct)
{
    string InternetAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; Q312461)";

    if (Direct)
    {
        if (HSession_Direct == 0)
        {
            HSession_Direct = InternetOpenW(InternetAgent, Internet_Open_Type_Direct, "0", "0", 0);
        }

        return(HSession_Direct);
    }
    else
    {
        if (HSession_IEType == 0)
        {
           HSession_IEType = InternetOpenW(InternetAgent, Internet_Open_Type_Preconfig, "0", "0", 0);
        }

        return(HSession_IEType);
    }
}

string HttpGET(string strUrl)
{
   int handler = HSession(false);
   int response = InternetOpenUrlW(handler, strUrl, NULL, 0,
        INTERNET_FLAG_NO_CACHE_WRITE |
        INTERNET_FLAG_PRAGMA_NOCACHE |
        INTERNET_FLAG_RELOAD, 0);
   if (response == 0)
        return(false);

   uchar ch[100]; string toStr=""; int dwBytes, h=-1;
   while(InternetReadFile(response, ch, 100, dwBytes))
  {
    if (dwBytes<=0) break; toStr=toStr+CharArrayToString(ch, 0, dwBytes);
  }

  InternetCloseHandle(response);
  return toStr;
}

void HttpOpen(string strUrl)
{
  Shell32::ShellExecuteW(0, "open", strUrl, "", "", 3);
}

CJAVal SendHttpGET(string url)
   {
      string decision = HttpGET(url);
      CJAVal json;
      json.Deserialize(decision);
      return json;
   }