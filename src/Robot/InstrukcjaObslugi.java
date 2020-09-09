package Robot;

import javax.swing.*;
import java.awt.*;
/** Klasa tworząca okienko wyswietlajace instrukcje obslugi */

public class InstrukcjaObslugi extends JFrame {



    public InstrukcjaObslugi() {
        super("Instrukcja Obslugi");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setPreferredSize(new Dimension(370,200));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(panel);
        JLabel tekst = new JLabel("Instrukcja obsługi do programu");
        panel.add(tekst, BorderLayout.PAGE_START);
        JLabel instrukcja1 = new JLabel("Sterowanie podstawa - Strzałki prawo, lewo");
        JLabel instrukcja2 = new JLabel("Sterowanie 1 ramie - Strzałki góra, dół");
        JLabel instrukcja3 = new JLabel("Sterowanie 2 ramie - W , S");
        JLabel instrukcja4 = new JLabel("Sterowanie chwytakiem NUM 8,NUM 5 , NUM 4,NUM 6");
        JLabel instrukcja5 = new JLabel("Łapanie i puszczanie prymitywu - Spacja ");
        JLabel instrukcja6 = new JLabel("Obracanie kamery za pomoca myszki");
        panel.add(instrukcja1,BorderLayout.LINE_START);
        panel.add(instrukcja2,BorderLayout.LINE_START);
        panel.add(instrukcja3,BorderLayout.LINE_START);
        panel.add(instrukcja4,BorderLayout.LINE_START);
        panel.add(instrukcja5,BorderLayout.LINE_START);
        panel.add(instrukcja6,BorderLayout.LINE_START);

        pack();
        setVisible(true);
    }
}
