# <img src="https://github.com/iulian-b/qrscanner/blob/master/img/icon.png " width=5% height=5%> qrscanner 

Aceasta aplicatie este un sumplu scanner de coduri QR si barcode scris in Kotlin.

Foloseste ca baza libreria Code scanner[^1] 2.1.0 pentru citirea si procesarea codurilor, QRGenerator[^2] 1.0.4 si ZXing[^3] 3.4.0
pentru generarea acestora.

Vor fi presente aceleasi functionalitati ca in orice alta aplicatie scanner care se gaseste pe play store, precum :

*	Folosirea flash-ului telefonului
*	Autofocus
*	Tap to focus
*	Posibilitatea de a schimba intre camera frontala si posterioara
*	Salvarea in galerie a codurilor QR generate


### Scancare
Aplicatia foloseste una dintre cele doua camere pentru a individua un cod QR/Barcode. 
O data gasit, va afisa pe ecran textul decifrat, iar daca acesta este un link, se poate apasa pe text pentru a fi redirectionati direct pe pagina web aferenta linkului scanat.

<img src="https://github.com/iulian-b/qrscanner/blob/master/img/scan.png" width=20% height=20%>


### Generare
Aplicatia permite si generarea codurilor QR.
O data introdus un text in casuta centrala, se apasa pe butonul "Generati cod QR" si va fi generat un bitmap continant codul QR.
Acest cod poate fi salvat in galeria telefonului apasant pe action button-ul din dreapta.

<img src="https://github.com/iulian-b/qrscanner/blob/master/img/gen.png" width=20% height=20%>



[^1]: [code-scanner](https://github.com/yuriy-budiyev/code-scanner)
[^2]: [QRGenerator](https://github.com/androidmads/QRGenerator)
[^3]: [ZXing](https://github.com/zxing/zxing)
