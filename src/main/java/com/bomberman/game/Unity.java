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
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("Unity Launcher");
        setSize(510,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel1);


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


}
