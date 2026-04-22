package businessLogic;

///---------------------ENUM SELECTIONPOLICY-----------------
///
/// pt implementarea strategy design pattern
/// pt logica de distributie a clientilor
///
///
//defineste politicile de selectie disponibile
//pT distribuirea clientilor in cozi
public enum SelectionPolicy
{
    //strategia care alege coada cu numarul minim de persoane
    SHORTEST_QUEUE,
    //strategia care alege coada cu timpul minim de asteptare
    SHORTEST_TIME
}