set http = CreateObject("microsoft.xmlhttp")
http.open "GET","https://www.tzb360.com/tzb-api/api/public/index/getSwitchAndHoldrate",false
http.send
htmlpage = http.responsetext 
WScript.Echo htmlpage