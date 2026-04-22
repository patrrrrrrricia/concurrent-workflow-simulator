package businessLogic;

import model.Server;
import model.Task;
import java.util.List;

///---------------------CLASA SHORTESTTIMESTRATEGY-----------------
//implementeaza strategia de add a clientilor
//in coada cu cel mai mic timp de asteptare
public class ShortestTimeStrategy implements Strategy
{
    //metoda cauta serverul care are timpul de asteptare minim
    //pana la finalizarea tuturor task urilor deja prezente
    @Override
    public void addTask(List<Server> servers, Task t)
    {
        //initializam cel mai bun server ca fiind primul din lista
        Server bestServer = servers.get(0);

        //parcurgem lista pentru a gasi serverul cel mai rapid
        for (Server s : servers)
        {
            //daca timpul de asteptare al serverului curent e mai mic
            if (s.getWaitingPeriod() < bestServer.getWaitingPeriod())
            {
                //actualizam serverul ales
                bestServer = s;
            }
        }

        //add clientul in coada cu cel mai mic timp de asteptare
        bestServer.addTask(t);
    }
}