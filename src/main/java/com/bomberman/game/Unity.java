package com.bomberman.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/** Okno launchera gry.
 *
 */
public class Unity extends JFrame{
    /**
     * Panel okna
     */
    private JPanel panel1;
    /**
     * Przycisk wyjscia
     */
    private JButton exitButton;
    /**
     * Przycisk uruchomienia gry
     */
    private JButton startGameButton;
    private JTabbedPane tabbedPane1;
    private JComboBox comboBox1;
    /**
     * Wlaczanie i wylaczanie krwi
     */
    private JCheckBox krewCheckBox;
    /**
     * Wybor rozdzielczosci
     */
    private JComboBox comboBox2;
    /**
     * Wybor trybu graficznego
     */
    private JComboBox comboBox3;
    /**
     * Wlaczanie i wylaczenie ray trackingu
     */
    private JCheckBox rayTracingCheckBox;

    /** Konstruktor klasy Unity, tworzy on okno launchera.
     *
     */
    Unity(){
        setupUI();
        setTitle("Unity Launcher");
        setSize(510,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel1);
        setLocationRelativeTo(null);
        setVisible(true);


        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rayTracingCheckBox.isSelected()){
                    JOptionPane.showMessageDialog(null, "Ray tracing jeszcze nie został dodany, " +
                            "odznacz aby uruchomić gre");
                }
                else if (krewCheckBox.isSelected()){
                    JOptionPane.showMessageDialog(null, "Krew jeszcze nie została dodana, " +
                            "odznacz aby uruchomić gre");
                }
                else if (comboBox3.getSelectedIndex() == 1){
                    JOptionPane.showMessageDialog(null, "DirectX 9 nie jest już wspierany," +
                            "wybierz nowszą wersję DirectX, lub zmien na OpenGL");
                }
                else if (comboBox3.getSelectedIndex() == 2){
                    JOptionPane.showMessageDialog(null, "DirectX 10 nie jest już wspierany," +
                            "wybierz nowszą wersję DirectX, lub zmien na OpenGL");
                }
                else if (comboBox3.getSelectedIndex() == 3){
                    JOptionPane.showMessageDialog(null, "DirectX 11 nie jest już wspierany," +
                            "wybierz nowszą wersję DirectX, lub zmien na OpenGL");
                }
                else if (comboBox3.getSelectedIndex() == 4){
                    JOptionPane.showMessageDialog(null, "DirectX 12 nie został jeszcze " +
                            "zaimplementowany, zmien na OpenGL");
                }
                else if (comboBox2.getSelectedIndex() != 0){
                    JOptionPane.showMessageDialog(null, "Antyaliasing nie został jeszcze " +
                            "zaimplementowany");

                }
                else{
                    dispose();
                    new Main();
                }


            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


    }



    /**
     * Buduje interfejs launchera (odpowiednik dawnego pliku Unity.form z IntelliJ GUI Designer).
     */
    private void setupUI() {
        panel1 = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;

        java.net.URL logoUrl = getClass().getResource("/textures/logo.png");
        JLabel logo = new JLabel(logoUrl != null ? new ImageIcon(logoUrl) : null);
        c.gridy = 0;
        c.insets = new Insets(10, 0, 10, 0);
        panel1.add(logo, c);

        tabbedPane1 = new JTabbedPane();

        JPanel graphics = new JPanel(new GridBagLayout());
        comboBox1 = new JComboBox<>(new String[]{"1280x720"});
        comboBox2 = new JComboBox<>(new String[]{"Brak", "2x", "4x", "8x", "16x", "32x", "64x",
                "128x", "256x", "512x", "1024x"});
        comboBox3 = new JComboBox<>(new String[]{"OpenGL", "DirectX 9", "DirectX 10", "DirectX 11", "DirectX 12"});
        rayTracingCheckBox = new JCheckBox("Ray tracing");
        krewCheckBox = new JCheckBox("Krew");
        addRow(graphics, 0, new JLabel("Rozdzielczosc"), comboBox1);
        addRow(graphics, 1, new JLabel("Antyaliasing"), comboBox2);
        addRow(graphics, 2, new JLabel("Tryb graficzny"), comboBox3);
        addRow(graphics, 3, null, rayTracingCheckBox);
        addRow(graphics, 4, null, krewCheckBox);
        tabbedPane1.addTab("Graphics", graphics);

        JPanel input = new JPanel(new GridBagLayout());
        String[] keys = {"W, A, S, D - wiadomo", "Spacja - plantowanie paki", "F5 - szybki zapis",
                "F8 - wczytanie szybkiego zapisu", "F9 - ScreenShot", "E - interakcja"};
        for (int i = 0; i < keys.length; i++) addRow(input, i, new JLabel(keys[i]), null);
        tabbedPane1.addTab("Input", input);

        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        panel1.add(tabbedPane1, c);

        startGameButton = new JButton("Start Game");
        exitButton = new JButton("Exit");
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        panel1.add(startGameButton, c);
        c.gridy = 3;
        panel1.add(exitButton, c);
    }

    /**
     * Dodaje wiersz (etykieta + kontrolka) do panelu z GridBagLayout.
     */
    private static void addRow(JPanel panel, int row, JComponent label, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(4, 10, 4, 10);
        if (label != null) {
            c.gridx = 0;
            panel.add(label, c);
        }
        if (field != null) {
            c.gridx = 1;
            panel.add(field, c);
        }
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridx = 2;
        filler.gridy = row;
        filler.weightx = 1;
        panel.add(Box.createHorizontalGlue(), filler);
    }

}
