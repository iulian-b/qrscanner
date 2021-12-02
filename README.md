# qrscanner
Aceasta aplicatie este un sumplu scanner de coduri QR si barcode scrisa in Kotlin.

Va folosii ca baza libreria Code scanner[^1] 2.1.0 pentru citirea si procesarea codurilor, si QRGenerator[^2]
1.0.4	pentru generarea acestora.

Vor fi presente aceleasi functionalitati ca in orice alta aplicatie scanner care se gaseste pe play store, precum :

*	Folosirea flash-ului telefonului
*	Autofocus
*	Tap to focus
*	Posibilitatea de a schimba intre camera frontala si posterioara
*	Salvarea in galerie a codurilor QR generate


Aplicatia foloseste una dintre cele doua camere pentru a individua un cod QR/Barcode. 
O data gasit, va afisa pe ecran textul decifrat, iar daca acesta este un link, se poate apasa pe text pentru a fi redirectionati direct pe pagina web aferenta linkului scanat.





[^1]: [code-scanner](https://github.com/yuriy-budiyev/code-scanner)
[^2]: [QRGenerator](https://github.com/androidmads/QRGenerator)
