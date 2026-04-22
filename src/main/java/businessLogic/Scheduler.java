package businessLogic;

import model.*;
import java.util.*;

///---------------------CLASA SCHEDULER-----------------
//pt a gestiona cozile (servere)
//si distribuirea clientilor (tasks) catre acestea conform unei strategii
public class Scheduler
{
    //lista de cozi (servere) pe care le gestioneaza scheduler ul
    private List<Server> servers;
    //strategia curenta de distribuire (time sau queue)
    private Strategy strategy;

    //constructorul scheduler ului.
    //maxNoServers-nr cozi de creat
    public Scheduler(int maxNoServers)
    {
        this.servers = new ArrayList<>();

        //creare nr de servere cerut
        for (int i = 0; i < maxNoServers; i++)
        {
            Server s = new Server();
            servers.add(s);

            //pornire fir de executie(thread) pt fiecare coada
            //fiecare server este un runnable -> va incepe sa proceseze clientii imediat
            new Thread(s).start();
        }
    }

    //schimba strategia de distribuire in timpul executiei (design pattern: strategy).
    //policy-politica de selectie aleasa (shortest_queue sau shortest_time)
    public void changeStrategy(SelectionPolicy policy)
    {
        //daca alegem shortest_queue, trimitem clientii la coada cu cei mai putini oameni
        if (policy == SelectionPolicy.SHORTEST_QUEUE)
        {
            strategy = new ShortestQueueStrategy();
        }
        //altfel, alegem shortest_time (trimitem unde timpul total de asteptare e mai mic)
        else
        {
            strategy = new ShortestTimeStrategy();
        }
    }

    //primeste un client nou de la simulationmanager si il deleaga strategiei pentru a fi plasat intr o coada
    //t clientul (task) sosit
    public void dispatchTask(Task t)
    {
        //strategia concreta decide in care server din lista intra task ul 't'
        strategy.addTask(servers, t);
    }

    //@return lista de servere pentru a fi afisata in interfata grafica
    public List<Server> getServers()
    {
        return servers;
    }
}
