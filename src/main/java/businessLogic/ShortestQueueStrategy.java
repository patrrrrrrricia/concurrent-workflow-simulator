package businessLogic;

import model.Server;
import model.Task;
import java.util.List;

///---------------------CLASA SHORTESTQUEUESTRATEGY-----------------
///
/// strategie: clientul se uita la nr de pers
/// si merge la coada cu nr de peers mai mic(nu cont cate cump au)
///
//implementeaza strategia de adaugare a clientilor
//in coada cu cei mai putini oameni
public class ShortestQueueStrategy implements Strategy
{
    //metoda cauta serverul cu cel mai mic nr de task uri
    //si adauga noul client acolo
    @Override
    public void addTask(List<Server> servers, Task t)
    {
        //pp ca primul server este cel mai bun la inceput
        Server bestServer = servers.get(0);

        //parcurgem lista de servere pt a gasi coada cea mai scurta
        for (Server s : servers)
        {
            //daca gasim o coada cu mai putini clienti decat cea curenta
            if (s.getTasks().size() < bestServer.getTasks().size())
            {
                //actualizam serverul ales
                bestServer = s;
            }
        }

        //add clientul in cea mai scurta coada gasita
        bestServer.addTask(t);
    }
}