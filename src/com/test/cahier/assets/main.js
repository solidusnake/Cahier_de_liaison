var req = new XMLHttpRequest();
req.open("GET", "http://localhost:8080/products/", true); 
req.onreadystatechange = monCode;   // la fonction de prise en charge
req.send(null);
console.log(req);


function monCode() 
{ 
   if (req.readyState == 4) 
   { 
        var doc = eval('(' + req.responseText + ')'); 
   }
} 