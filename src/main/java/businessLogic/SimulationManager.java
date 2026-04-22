package businessLogic;

import model.Task;
import model.Server;
import GUI.SimulationFrame;
import java.util.*;
import java.io.*;

///---------------------CLASA SIMULATIONMANAGER-----------------
///
/// creierul aplicatiei
/// server-casa marcat, task-client
/// -> SIMULATIONMANAGER e managerul magazinului
/// implementeaza runnable->ruleaza pe un fir de executie separat
///
///
///
//gestioneaza firul principal al simularii si calculeaza
//statisticile finale(waiting time, service time, peak hour)
public class SimulationManager implements Runnable
{
    public int timeLimit, maxArrival, minArrival, maxService, minService, numberOfServers, numberOfClients;
    public SelectionPolicy selectionPolicy;
    private Scheduler scheduler;
    private List<Task> generatedTasks;
    private SimulationFrame frame;

    //constructorul managerului de simulare
    //initializeaza scheduler ul si genereaza clientii random
    public SimulationManager(int time, int n, int q, int minA, int maxA, int minS, int maxS, SelectionPolicy policy, SimulationFrame frame)
    {
        this.timeLimit = time;
        this.numberOfClients = n;
        this.numberOfServers = q;
        this.minArrival = minA;
        this.maxArrival = maxA;
        this.minService = minS;
        this.maxService = maxS;
        this.selectionPolicy = policy;
        this.frame = frame;

        //planificatorul
        //initializeaza obiectul scheduler cu un numar specific de servere (cozi de asteptare)
        this.scheduler = new Scheduler(numberOfServers);
        //seteaza strategia de repartizare a task urilor(ex: shortest queue sau shortest time)
        this.scheduler.changeStrategy(selectionPolicy);

        //apeleaza metoda care creeaza task uri cu date aleatorii pentru a incepe simularea
        generateRandomTasks();
    }

    //genereaza un numar de n clienti cu parametri incadrati intre limitele date
    private void generateRandomTasks()
    {
        //initializam lista in care vom stoca toti clientii creati
        generatedTasks = new ArrayList<>();
        Random r = new Random();
        //pt fiecare client
        for (int i = 1; i <= numberOfClients; i++)
        {
            //calculeaza un timp de sosire (arrival) aleatoriu intre min si max
            int arr = r.nextInt(maxArrival - minArrival + 1) + minArrival;
            //-----------------------------(service)----------------------------
            int ser = r.nextInt(maxService - minService + 1) + minService;

            //creeaza obiectul task cu id ul i si valorile gasite mai sus, apoi il pune in lista
            generatedTasks.add(new Task(i, arr, ser));
        }
        //sortam clientii dupa timpul de sosire
        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    //metoda run care ruleaza procesul de simulare secunda cu secunda
    @Override
    public void run()
    {
        int currentTime = 0;

        //variabile pt calculul statisticilor
        double totalServiceTime = 0;
        double totalWaitTime = 0;
        int peakHour = 0;
        int maxClientsAtOnce = -1;

        //parcurgem lista de task uri generate pt a calcula suma totala a timpilor de procesare
        for (Task t : generatedTasks)
        {
            totalServiceTime += t.getServiceTime();
        }

        try (PrintWriter log = new PrintWriter(new FileWriter("simulation_log.txt")))
        {
            //bucla principala a simularii care ruleaza secunda cu secunda pana la limita de timp
            while (currentTime <= timeLimit)
            {
                ///pasul 1: verificam lista de task-uri care asteapta sa intre in sistem
                Iterator<Task> it = generatedTasks.iterator();
                while (it.hasNext())
                {
                    Task t = it.next();
                    // daca timpul de sosire al task ului coincide cu secunda curenta
                    if (t.getArrivalTime() == currentTime)
                    {
                        //trimitem taskul catre scheduler pentru a fi repartizat la o coada
                        scheduler.dispatchTask(t);
                        //il scoatem din lista de asteptare(waiting list) deoarece a intrat la server
                        it.remove();
                    }
                }

                ///pasul 2: analizam starea actuala a cozilor pentru statistici
                int currentClientsInQueues = 0;
                //parcurgem fiecare server/coada din sistem
                for (Server s : scheduler.getServers())
                {
                    //numaram cati clienti sunt in acest moment in coada curenta
                    int size = s.getTasks().size();
                    currentClientsInQueues += size;

                    //la fiecare secunda, adunam timpul de asteptare total cumulat de server
                    //acesta reprezinta suma timpilor de procesare ramasi pentru toti clientii din acea coada
                    totalWaitTime += s.getWaitingPeriod();
                }

                ///verificam daca momentul curent este cel mai aglomerat de pana acum(peak hour)
                if (currentClientsInQueues > maxClientsAtOnce)
                {
                    //actualizam numarul maxim de clienti detectati simultan
                    maxClientsAtOnce = currentClientsInQueues;
                    //salvam secunda curenta ca fiind ora de varf
                    peakHour = currentTime;
                }

                ///pasul 3: generam raportul vizual pentru interfata si fisierul log
                String status = getStatus(currentTime);
                //scriem starea curenta in fisierul simulation_log.txt
                log.println(status);
                //daca interfata grafica exista, o actualizam cu noile date
                if (frame != null)
                {
                    frame.updateOutput(status);
                }

                //conditie de oprire prematura: daca nu mai avem clienti de trimis si serverele au terminat treaba
                if (generatedTasks.isEmpty() && allserversempty()) break;

                //incrementam timpul si punem firul de executie pe pauza o secunda (simulam trecerea timpului real)
                currentTime++;
                Thread.sleep(1000);
            }

            ///pasul 4: calculul mediilor dupa ce simularea s-a incheiat sau a atins limita
            //timpul mediu de asteptare = suma timpilor de asteptare din fiecare secunda / numar total clienti
            double avgWait = totalWaitTime / numberOfClients;
            // timpul mediu de service = suma timpilor de procesare initiali / numar total clienti
            double avgService = totalServiceTime / numberOfClients;

            if (frame != null)
            {
                // trimitem rezultatele finale catre interfata grafica pentru afisare
                frame.updateStatistics(avgWait, avgService, peakHour);
            }

            //scriem concluziile finale in fisierul log
            log.println("\n--- final statistics ---");
            log.printf("average waiting time: %.2f\n", avgWait);
            log.printf("average service time: %.2f\n", avgService);
            log.println("peak hour: " + peakHour);

        }
        catch (Exception e)
        {
            //afisam eroarea in consola in caz ca scrierea in fisier sau thread ul esueaza
            e.printStackTrace();
        }
    }

    //verifica daca toate cozile din sistem sunt goale
    private boolean allserversempty()
    {
        //parcurgem fiecare server din lista gestionata de scheduler
        for (Server s : scheduler.getServers())
        {
            //daca gasim macar un singur server care inca mai are task-uri in coada
            if (!s.getTasks().isEmpty())
            {
                //false -> "nu, nu sunt toate goale"
                return false;
            }
        }
        //daca am verificat toate serverele si niciunul nu avea task uri -> true
        return true;
    }

    //construieste un string care descrie starea curenta a simularii(cozi, clienti)
    private String getStatus(int time)
    {
        //cream un stringbuilder pentru eficienta si add timpul curent si lista de asteptare
        StringBuilder sb = new StringBuilder("Time " + time + "\nWaiting: " + generatedTasks + "\n");

        //parcurgem fiecare server din lista pentru a le afisa continutul cozilor
        for (int i = 0; i < scheduler.getServers().size(); i++)
        {
            //adaugam numele cozii (ex: queue 1, queue 2...)
            sb.append("Queue ").append(i + 1).append(": ")
                    //verificam: daca serverul nu are task uri, scriem "closed", altfel afisam lista de task uri
                    .append(scheduler.getServers().get(i).getTasks().isEmpty() ? "closed" : scheduler.getServers().get(i).getTasks())
                    //adaugam o linie noua pentru a trece la urmatorul server
                    .append("\n");
        }
        //rez final sub forma de sir
        return sb.toString();
    }
}