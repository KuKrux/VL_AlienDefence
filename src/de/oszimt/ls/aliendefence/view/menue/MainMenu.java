package de.oszimt.ls.aliendefence.view.menue;

import de.oszimt.ls.aliendefence.controller.AlienDefenceController;
import de.oszimt.ls.aliendefence.controller.GameController;
import de.oszimt.ls.aliendefence.model.Level;
import de.oszimt.ls.aliendefence.toDo.User;
import de.oszimt.ls.aliendefence.view.game.GameGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainMenu {

    private JPanel main;
    private JTextField loginTextField;
    private JComboBox level;
    private JButton playButton;
    private JButton testButton;
    private JButton highscoreButton;
    private JButton levelEditorButton;
    private JButton exitButton;
    private JPasswordField passwordTextField;

    public MainMenu(AlienDefenceController alienDefenceController) {

        //fill level chooser
        // Levelliste für die ComboBox abrufen
        List<Level> arrLevel = alienDefenceController.getLevelController().readAllLevels();
        String[] arrLevelNames = getLevelNames(arrLevel);
        /*level.setModel(new DefaultComboBoxModel<String>(arrLevelNames));*/

        // Button Spielen - ActionListener
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // User aus Datenbank holen
                User user = alienDefenceController.getAlienDefenceModel().getUserPersistance().readUser(loginTextField.getText());

                // Spielstarten, wenn Nutzer existiert und Passwort übereinstimmt
                if (user != null && user.getPassword().equals(new String(passwordTextField.getPassword()))) {

                    Thread t = new Thread("GameThread") {
                        @Override
                        public void run() {

                            GameController gameController = alienDefenceController.startGame(arrLevel.get(level.getSelectedIndex()), user);
                            new GameGUI(gameController).start();
                        }
                    };
                    t.start();
                } else {
                    // Fehlermeldung - Zugangsdaten fehlerhaft
                    JOptionPane.showMessageDialog(null, "Zugangsdaten nicht korrekt", "Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Button Testen - ActionListener
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Erstellt Modell von aktuellen Nutzer
                User user = new User(1, "test", "pass");

                Thread t = new Thread("GameThread") {

                    @Override
                    public void run() {
                        //GameController gameController = alienDefenceController.startGame(arrLevel.get(level.getSelectedIndex()), user);
                        //new GameGUI(gameController).start();
                        LeveldesignWindow leveldesignWindow = new LeveldesignWindow(alienDefenceController, user, "TestenKnopf");
                        new LevelChoice(alienDefenceController, leveldesignWindow, user, "TestenKnopf");
                    }
                };
                t.start();
            }
        });

        // Button Highscore
        highscoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Highscore(alienDefenceController.getAttemptController(), arrLevel.get(level.getSelectedIndex()));
            }
        });

        // Button Leveleditor
        levelEditorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LeveldesignWindow(alienDefenceController, null, "LevelEditorKnopf");
            }
        });

        // Button Beenden
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


    private String[] getLevelNames(List<Level> arrLevel) {
        String[] arrLevelNames = new String[arrLevel.size()];

        for (int i = 0; i < arrLevel.size(); i++) {
            arrLevelNames[i] = arrLevel.get(i).getName(); // Array aus Arraylist erstellt
        }

        return arrLevelNames;
    }

    public static void show(AlienDefenceController alienDefenceController) {
        JFrame frame = new JFrame("AlienDefence");
        frame.setContentPane(new MainMenu(alienDefenceController).main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
