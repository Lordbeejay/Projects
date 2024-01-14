import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class BookwormAdventureGUI {
    private static int playerHealth = 100;
    private static int enemyHealth = 50;
    private static String randomLetters;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Bookworm Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel);
        placeComponents(panel);

        frame.setSize(400, 200);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Set background color
        panel.setBackground(new Color(255, 255, 200));  // You can choose your own RGB values

        JLabel healthLabel = new JLabel("Player Health: " + playerHealth + " | Enemy Health: " + enemyHealth);
        healthLabel.setBounds(10, 20, 300, 25);
        panel.add(healthLabel);

        JLabel lettersLabel = new JLabel();
        lettersLabel.setBounds(10, 50, 300, 25);
        panel.add(lettersLabel);

        JTextField wordField = new JTextField(20);
        wordField.setBounds(10, 80, 165, 25);
        panel.add(wordField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(10, 110, 80, 25);
        panel.add(submitButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(100, 110, 80, 25);
        panel.add(quitButton);

        // Initialize random letters
        randomLetters = generateRandomLetters(12);
        lettersLabel.setText("Enter a word using the following letters: " + randomLetters);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userWord = wordField.getText().toLowerCase();

                if (areCharactersInRandomSet(userWord, randomLetters)) {
                    if (isWordInDictionary(userWord)) {
                        performPlayerAction();
                        updateStats(healthLabel);
                        enemyTurn();
                        updateStats(healthLabel);

                        if (playerHealth <= 0 || enemyHealth <= 0) {
                            endGame(playerHealth);
                        }

                        // Generate a new set of random letters for the next round
                        randomLetters = generateRandomLetters(12);
                        lettersLabel.setText("Enter a word using the following letters: " + randomLetters);
                    } else {
                        JOptionPane.showMessageDialog(null, "Word not found in the dictionary. Try again.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid characters in the word. Try again.");
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private static void performPlayerAction() {
        Object[] options = {"Attack", "Heal"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose an action:", "Player Action",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                attackEnemy();
                break;
            case 1:
                healPlayer();
                break;
            default:
                break;
        }
    }

    private static void updateStats(JLabel healthLabel) {
        healthLabel.setText("Player Health: " + playerHealth + " | Enemy Health: " + enemyHealth);
    }

    private static void endGame(int playerHealth) {
        if (playerHealth <= 0) {
            JOptionPane.showMessageDialog(null, "Game over! You were defeated by the enemy.");
        } else {
            JOptionPane.showMessageDialog(null, "Congratulations! You defeated the enemy!");
        }
        System.exit(0);
    }

    private static void attackEnemy() {
        int damage = (int) (Math.random() * 10) + 1;
        enemyHealth -= damage;
        JOptionPane.showMessageDialog(null, "You attacked the enemy and dealt " + damage + " damage!");
        int enemyDamage = (int) (Math.random() * 8) + 1;
        playerHealth -= enemyDamage;
        JOptionPane.showMessageDialog(null, "The enemy attacked you and dealt " + enemyDamage + " damage!");
    }

    private static void healPlayer() {
        int healing = (int) (Math.random() * 10) + 1;
        playerHealth = Math.min(100, playerHealth + healing);
        JOptionPane.showMessageDialog(null, "You healed yourself for " + healing + " health!");
    }

    private static void enemyTurn() {
        int enemyDamage = (int) (Math.random() * 8) + 1;
        playerHealth -= enemyDamage;
        JOptionPane.showMessageDialog(null, "The enemy attacked you and dealt " + enemyDamage + " damage!");
    }

    private static String generateRandomLetters(int length) {
        Random random = new Random();
        StringBuilder randomLetters = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomLetters.append((char) (random.nextInt(26) + 'a'));
        }
        return randomLetters.toString();
    }

    private static boolean areCharactersInRandomSet(String word, String randomLetters) {
        for (char c : word.toCharArray()) {
            if (randomLetters.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isWordInDictionary(String word) {
        try (Scanner wordScanner = new Scanner(new File("words.txt"))) {
            while (wordScanner.hasNextLine()) {
                if (wordScanner.nextLine().equalsIgnoreCase(word)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
