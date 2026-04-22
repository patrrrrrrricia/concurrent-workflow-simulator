package model;

///---------------------CLASA TASK-----------------
///
/// clientul
/// descriere client, inf clienti
///
///
//defineste structura unui client/proces din cadrul simularii
public class Task
{
    //identificatorul unic al task-ului (ex: numarul de ordine)
    private int id;
    //momentul de timp la care task-ul apare in sistem (secunda de start)
    private int arrivalTime;
    //durata necesara pentru procesarea acestui task la un server
    private int serviceTime;

    //constructorul clasei: initializeaza atributele de baza ale clientului
    public Task(int id, int arrivalTime, int serviceTime)
    {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    //returneaza id ul unic al task ului
    public int getId()
    {
        return id;
    }

    //returneaza timpul de sosire in sistem
    public int getArrivalTime()
    {
        return arrivalTime;
    }

    //returneaza timpul de procesare necesar
    public int getServiceTime()
    {
        return serviceTime;
    }

    //permite modificarea timpului de procesare (folosit de server cand scade secunda de lucru)
    public void setServiceTime(int serviceTime)
    {
        this.serviceTime = serviceTime;
    }

    //returneaza o reprezentare textuala a task ului pentru a fi afisata in gui si log uri
    //formatul returnat este: (id,arrivaltime,servicetime)
    @Override
    public String toString()
    {
        return "(" + id + "," + arrivalTime + "," + serviceTime + ")";
    }
}
