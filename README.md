# Queue Management System - Simulation

## 💻 Overview
A robust Java desktop simulation tool designed to model and analyze multi-server queue dynamics. By leveraging **multithreading** and a **Layered Architecture**, this system provides a highly responsive environment to observe real-time task evolution, customer flow, and statistical bottlenecks. It features configurable scheduling strategies and concurrent processing engines.

---

## 🤍 Tech Stack
* **Language:** Java 21
* **Frontend:** Java Swing (Concurrent UI Updates)
* **Concurrency:** `Runnable`, `Threads`, `BlockingQueue`, `AtomicInteger`
* **Patterns:** * **Strategy Pattern** (Dynamic Task Dispatching)
    * **MVC Pattern** (Separation of logic, model, and UI)

![Java](https://img.shields.io/badge/java-21+-%23FF69B4.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Concurrency](https://img.shields.io/badge/Concurrency-Multithreading-%234F4F4F.svg?style=for-the-badge&logo=java&logoColor=white) 

---

## 🎀 Core Functionality
* **Dynamic Simulation:** Real-time generation of tasks with configurable arrival and service time distributions.
* **Scheduling Strategies:** * **Shortest Queue Strategy:** Prioritizes servers with the fewest active tasks.
    * **Shortest Time Strategy:** Prioritizes servers with the minimum cumulative wait time.
* **Concurrent Execution:** Every server operates on an independent thread, allowing parallel simulation and accurate synchronization via `AtomicInteger` and thread-safe collections.
* **Data Logging:** Automatic generation of execution logs and final statistics (Average Wait Time, Service Time, Peak Hour).

---

## 📂 Project Structure
* **`model`**: Contains core entities (`Task`, `Server`) managed by concurrent primitives.
* **`businessLogic`**: Implements the `SimulationManager`, `Scheduler`, and the `Strategy` pattern logic.
* **`GUI`**: Handles the Swing interface and real-time state visualization.

---

### Visual Documentation
| UML Class Diagram | Layered Architecture |
| :---: | :---: |
| ![UML Diagram]() | <div align="center"> <img width="400" src="https://github.com/user-attachments/assets/09a55a45-e9ec-4635-bd16-165fd791ab27"> <br><br> <img width="853" height="1020" alt="image" src="https://github.com/user-attachments/assets/25a3796c-1dea-4fb3-be46-d055757a870a" />
|

---

© 2026 Queue Management Simulation | Developed by [**𝐋𝐞𝐨𝐧𝐭𝐞 𝐏𝐚𝐭𝐫𝐢𝐜𝐢𝐚-𝐌𝐢𝐫𝐚𝐛𝐞𝐥𝐚**](https://patrrrrrrricia.github.io/glowing-button/)
