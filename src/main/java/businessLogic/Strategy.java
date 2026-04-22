package businessLogic;

import model.Server;
import model.Task;
import java.util.List;

///---------------------INTERFATA STRATEGY-----------------
//interfata care defineste sablonul pentru algoritmii de selectie a cozilor
public interface Strategy {
    //metoda obligatorie pe care orice strategie(time sau queue) trebuie sa o implementeze
    //primeste lista de servere disponibile si task ul care trebuie repartizat
    public void addTask(List<Server> servers, Task t);
}