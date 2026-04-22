package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

///---------------------CLASA SERVER-----------------
//pt a putea rula pe un fir de executie (thread) separat
public class Server implements Runnable
{
    //coada de task uri care asteapta sa fie procesate de acest server
    //se foloseste blockingqueue pt a gestiona accesul concurent in siguranta
    private BlockingQueue<Task> tasks;

    //variabila atomica pentru a tine evidenta timpului total de asteptare la aceasta coada
    //atomicinteger asigura ca operatiile de adunare/scadere sunt thread safe
    private AtomicInteger waitingPeriod;

    public Server()
    {
        //initializam coada ca o lista blocanta fara limita de capacitate
        this.tasks = new LinkedBlockingQueue<>();
        //timpul de asteptare initial este zero
        this.waitingPeriod = new AtomicInteger(0);
    }

    //metoda apelata de strategie pentru a adauga un client nou in coada acestui server
    public void addTask(Task newTask)
    {
        //adaugam task ul in coada de procesare
        tasks.add(newTask);
        //actualizam timpul total de asteptare prin adunarea timpului de service al noului client
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    //bucla principala de executie a serverului
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                //verificam care este primul task din coada fara a-l scoate inca (peek)
                Task currentTask = tasks.peek();

                if (currentTask != null)
                {
                    //simulam trecerea unei secunde de timp real prin punerea thread ului pe pauza
                    Thread.sleep(1000);

                    //decrementam timpul de procesare ramas pentru task ul curent
                    currentTask.setServiceTime(currentTask.getServiceTime() - 1);
                    //scadem o secunda si din timpul total de asteptare al cozii
                    waitingPeriod.decrementAndGet();

                    //daca timpul de procesare a ajuns la zero, inseamna ca task ul este finalizat
                    if (currentTask.getServiceTime() == 0)
                    {
                        //scoatem definitiv task ul din coada (take)
                        tasks.take();
                    }
                }
            }
            catch (InterruptedException e)
            {
                //daca firul de executie este intrerupt, oprim bucla principala
                break;
            }
        }
    }

    //returneaza lista actuala de task uri pentru a fi afisata in interfata grafica
    public BlockingQueue<Task> getTasks()
    {
        return tasks;
    }

    //returneaza valoarea curenta a timpului de asteptare la aceasta coada
    public int getWaitingPeriod()
    {
        return waitingPeriod.get();
    }
}
