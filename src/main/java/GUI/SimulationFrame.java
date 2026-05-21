package GUI;

import businessLogic.SelectionPolicy;
import businessLogic.SimulationManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;


///---------------------CLASA SIMULATIONFRAME-----------------
///
/// interfata grafica
///
//defineste interfata grafica (gui) a aplicatiei
public class SimulationFrame extends JFrame
{
    //definirea paletei de culori utilizate in interfata (alb, negru si accente roz)
    private final Color BG_WHITE = Color.WHITE;               //fundal alb
    private final Color PINK_ACCENT = new Color(218, 131, 147); //roz aprins pt elemente vizibile
    private final Color TEXT_BLACK = new Color(30, 30, 30);    //culoare text inchisa
    private final Color BORDER_LIGHT = new Color(220, 220, 220); //margini gri deschis

    //campurile de text pentru introducerea parametrilor simularii
    private JTextField tN, tQ, tSim, tMinArrival, tMaxArrival, tMinService, tMaxService;
    //selector pt strategia de planificare(policy)
    private JComboBox<SelectionPolicy> strategyChooser;
    //zona de afisare a log ului in timp real
    private JTextArea logArea;
    //etichete pt afisarea rezultatelor statistice finale
    private JLabel lblAvgWait, lblAvgSer, lblPeak;

    public SimulationFrame()
    {
        //configurarea ferestrei: titlu, dimensiune si operatia de inchidere
        setTitle("Queues Management System");
        setSize(700, 850);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_WHITE); //setare fundal alb general
        setLayout(new BorderLayout(15, 15));

        //1.panelul de input(zona de sus) - organizat sub forma de tabel (grid)
        JPanel pInput = new JPanel(new GridLayout(8, 2, 12, 12));
        pInput.setBackground(BG_WHITE);
        pInput.setBorder(new EmptyBorder(25, 40, 10, 40));

        //initializarea campurilor de text cu valori implicite
        tN = createStyledField("4");
        tQ = createStyledField("2");
        tSim = createStyledField("60");
        tMinArrival = createStyledField("2");
        tMaxArrival = createStyledField("30");
        tMinService = createStyledField("2");
        tMaxService = createStyledField("4");

        //configurarea selectorului de strategii cu valorile din enum-ul selectionpolicy
        strategyChooser = new JComboBox<>(SelectionPolicy.values());
        styleComboBox(strategyChooser);

        //adaugarea randurilor de input (eticheta + camp text) in panel
        addInputRow(pInput, "Clients (N):", tN);
        addInputRow(pInput, "Queues (Q):", tQ);
        addInputRow(pInput, "Simulation Time:", tSim);
        addInputRow(pInput, "Min Arrival:", tMinArrival);
        addInputRow(pInput, "Max Arrival:", tMaxArrival);
        addInputRow(pInput, "Min Service:", tMinService);
        addInputRow(pInput, "Max Service:", tMaxService);

        //adaugarea selectorului de strategie la finalul panelului de input
        JLabel lblStrat = new JLabel("Strategy:");
        lblStrat.setForeground(TEXT_BLACK);
        pInput.add(lblStrat);
        pInput.add(strategyChooser);

        //2.zona de log (centru) - afiseaza starea sistemului secunda cu secunda
        logArea = new JTextArea();
        logArea.setEditable(false); //utilizatorul nu poate modifica textul din log
        logArea.setBackground(BG_WHITE);
        logArea.setForeground(TEXT_BLACK);
        logArea.setFont(new Font("Monospaced", Font.BOLD, 13));
        logArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        //adaugarea unui scrollpane pentru a putea naviga prin log uri lungi
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(new LineBorder(BORDER_LIGHT, 1));

        //panel central care contine zona de scroll
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_WHITE);
        centerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
        centerPanel.add(scroll, BorderLayout.CENTER);

        //3.panelul de jos - contine butonul de start si statisticile finale
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(BG_WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 40, 30, 40));

        //configurarea butonului de start
        JButton btnStart = new JButton("START SIMULATION");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnStart.setForeground(Color.WHITE);
        btnStart.setBackground(PINK_ACCENT);
        btnStart.setFocusPainted(false);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStart.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        //panel pentru afisarea statisticilor
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        statsPanel.setBackground(BG_WHITE);

        lblAvgWait = createStatsLabel("Average waiting time: -");
        lblAvgSer = createStatsLabel("Average service time: -");
        lblPeak = createStatsLabel("Peak hour: -");

        statsPanel.add(lblAvgWait);
        statsPanel.add(lblAvgSer);
        statsPanel.add(lblPeak);

        //atasarea actiunii de pornire la click pe buton
        btnStart.addActionListener(e -> startSimulation());

        //asamblarea panelului de jos
        bottomPanel.add(statsPanel, BorderLayout.NORTH);
        bottomPanel.add(btnStart, BorderLayout.CENTER);

        //pozitionarea celor trei zone principale in fereastra
        add(pInput, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        //centrarea ferestrei pe ecran si afisarea ei
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //metoda utilitara pentru a adauga un label si un field intr un panel
    private void addInputRow(JPanel p, String labelText, JTextField field) {
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(TEXT_BLACK);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        p.add(lbl);
        p.add(field);
    }

    //creeaza si stilizeaza campurile de text pentru input
    private JTextField createStyledField(String text) {
        JTextField f = new JTextField(text);
        f.setBackground(new Color(250, 250, 250));
        f.setForeground(TEXT_BLACK);
        f.setFont(new Font("SansSerif", Font.BOLD, 13));
        f.setHorizontalAlignment(JTextField.CENTER);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return f;
    }

    //creeaza si stilizeaza etichetele pentru statistici cu culoarea roz
    private JLabel createStatsLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(PINK_ACCENT);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        return lbl;
    }

    //aplica stilul vizual pentru combobox (selectorul de strategie)
    private void styleComboBox(JComboBox<SelectionPolicy> cb) {
        cb.setBackground(Color.WHITE);
        cb.setForeground(TEXT_BLACK);
        cb.setBorder(new LineBorder(BORDER_LIGHT, 1));
    }

    //citeste datele introduse si initiaza thread-ul de simulare
    private void startSimulation() {
        logArea.setText(""); // curatarea log-ului anterior
        try {
            //extragerea datelor din gui si crearea managerului de simulare
            SimulationManager sim = new SimulationManager(
                    Integer.parseInt(tSim.getText()),
                    Integer.parseInt(tN.getText()),
                    Integer.parseInt(tQ.getText()),
                    Integer.parseInt(tMinArrival.getText()),
                    Integer.parseInt(tMaxArrival.getText()),
                    Integer.parseInt(tMinService.getText()),
                    Integer.parseInt(tMaxService.getText()),
                    (SelectionPolicy) strategyChooser.getSelectedItem(),
                    this
            );
            //pornirea simularii pe un fir de executie nou pentru a nu bloca interfata grafica
            new Thread(sim).start();
        } catch (Exception ex) {
            //afisarea unei ferestre de eroare in cazul in care datele introduse nu sunt corecte
            JOptionPane.showMessageDialog(this, "Eroare la datele de intrare!");
        }
    }

    //actualizeaza zona de log (apelata de simulationmanager prin intermediul swingutilities)
    public void updateOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + "\n" + "------------------------------------------------" + "\n");
            //auto scroll catre finalul textului
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    //actualizeaza etichetele de statistici cu rezultatele finale
    public void updateStatistics(double avgWait, double avgSer, int peakHour) {
        SwingUtilities.invokeLater(() -> {
            lblAvgWait.setText(String.format("Average waiting time: %.2f", avgWait));
            lblAvgSer.setText(String.format("Average service time: %.2f", avgSer));
            lblPeak.setText("Peak hour: " + peakHour);
        });
    }
}