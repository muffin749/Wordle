import java.util.Arrays;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Wordle extends Application {
    private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private int letterIndex = 0; // Index to track the current letter
    private GridPane wordleGrid; // GridPane for Wordle panels
    private String[] wordBank = {"HELLO", "WORLD", "BRUTE", "ADIEU", "GREED"}; 
    private String targetWord; // The word to be guessed
    private Label answerLabel; // Label to display the answer
    private VBox buttonBox; // Box to hold buttons

    private boolean isDarkMode = false; // Flag for Dark Mode

    @Override
    public void start(Stage stage) {
        chooseTargetWord(); // Choose a random word from the word bank

        wordleGrid = createWordleGrid();

        GridPane lettersGrid = createLettersGrid();

        Button deleteButton = createStyledButton("Delete");
        deleteButton.setOnAction(event -> clearWordlePanels());

        Button enterButton = createStyledButton("Enter");
        enterButton.setOnAction(event -> checkWord());

        Button answerButton = createStyledButton("Answer");
        answerButton.setOnAction(event -> displayAnswer());

        Button darkModeButton = createStyledButton("Dark Mode");
        darkModeButton.setOnAction(event -> toggleDarkMode());

        Label titleLabel = new Label("Wordle by: David O");
        titleLabel.getStyleClass().add("title-label");

        buttonBox = new VBox(10); // Initialize buttonBox
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(deleteButton, enterButton, answerButton, darkModeButton);

        answerLabel = new Label(); // Initialize answerLabel
        answerLabel.getStyleClass().add("answer-label");

        VBox rootPane = new VBox(20);
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().addAll(titleLabel, wordleGrid, lettersGrid, buttonBox, answerLabel);

        Scene scene = new Scene(rootPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styl1.css").toExternalForm()); // Load styl1.css

        stage.setTitle("JavaFX Wordle");
        stage.setScene(scene);
        stage.show();
    }

    private void chooseTargetWord() {
        Random random = new Random();
        int randomIndex = random.nextInt(wordBank.length);
        targetWord = wordBank[randomIndex]; 
        targetWord = "ARRAY";// Choose a random word from the word bank
    }
        private GridPane createWordleGrid() {
    GridPane gridPane = new GridPane();
    gridPane.setAlignment(Pos.CENTER);
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    for (int row = 0; row < 6; row++) { // Changed condition to iterate up to 6 rows
        for (int col = 0; col < 5; col++) {
            Pane wordlePanel = createWordlePanel();
            gridPane.add(wordlePanel, col, row);
        }
    }

    return gridPane;
}

    private Pane createWordlePanel() {
        Pane wordlePanel = new Pane();
        wordlePanel.getStyleClass().add("wordle-panel");

        return wordlePanel;
    }

    private GridPane createLettersGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        for (int i = 0; i < alphabet.length; i++) {
            Button letterButton = new Button(String.valueOf(alphabet[i]));
            letterButton.getStyleClass().add("letter-button");
            letterButton.setOnAction(event -> placeLetterInWordle(letterButton.getText()));
            gridPane.add(letterButton, i % 10, i / 10);
        }

        return gridPane;
    }

    private void placeLetterInWordle(String letter) {
        int currentRow = letterIndex / 5;
        int currentCol = letterIndex % 5;
        
        Pane wordlePanel = (Pane) wordleGrid.getChildren().get(currentRow * 5 + currentCol);
        Label letterLabel = new Label(letter);
        letterLabel.getStyleClass().add("letter-label");
        letterLabel.setAlignment(Pos.CENTER); // Clear previous content if any
        wordlePanel.getChildren().add(letterLabel);
        letterIndex = (letterIndex + 1) % (wordBank.length * 6); // Move to the next panel

      
    }

    private void clearWordlePanels() {
        for (int i = 0; i < wordleGrid.getChildren().size(); i++) {
            Pane wordlePanel = (Pane) wordleGrid.getChildren().get(i);
            wordlePanel.getChildren().clear();
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        return button;
    }

    private void checkWord() {
        StringBuilder enteredWordBuilder = new StringBuilder();
        for (int i = 0; i < wordleGrid.getChildren().size(); i++) {
            Pane wordlePanel = (Pane) wordleGrid.getChildren().get(i);
            if (!wordlePanel.getChildren().isEmpty()) {
                Label letterLabel = (Label) wordlePanel.getChildren().get(0);
                enteredWordBuilder.append(letterLabel.getText());
            } else {
                enteredWordBuilder.append("-");
            }
        }
        String enteredWord = enteredWordBuilder.toString();

        if (enteredWord.equalsIgnoreCase(targetWord)) {
            System.out.println("Correct word entered: " + enteredWord);
        } else {
            System.out.println("Incorrect word entered: " + enteredWord);
        }
         markWordlePanels();
    }

    private void markWordlePanels() {
        String[] letters = targetWord.split(""); // Split the target word into individual letters
        for (int i = 0; i < wordleGrid.getChildren().size(); i++) {
            Pane wordlePanel = (Pane) wordleGrid.getChildren().get(i);
            if (!wordlePanel.getChildren().isEmpty()) {
                Label letterLabel = (Label) wordlePanel.getChildren().get(0);
                String letter = letterLabel.getText();
                int index = i % 5; // Calculate the index of the letter in the target word
                if (targetWord.contains(letter)) {
                    if (letter.equals(letters[index])) {
                        wordlePanel.setStyle("-fx-background-color: green;");
                    } else {
                        wordlePanel.setStyle("-fx-background-color: yellow;");
                    }
                }
            }
        }
    }

    private void displayAnswer() {
        answerLabel.setText("Answer: " + targetWord); // Update answerLabel text with the target word
    }

    private void toggleDarkMode() {
        isDarkMode = !isDarkMode; // Toggle Dark Mode flag
        if (isDarkMode) {
            wordleGrid.setStyle("-fx-background-color: #222222;"); // Dark background color
            buttonBox.setStyle("-fx-background-color: #333333;"); 
            buttonBox.setStyle("-fx-background-color: #333333;"); // Dark background color for buttons
        } else { GridPane lettersGrid = createLettersGrid();

            wordleGrid.setStyle("-fx-background-color: #FFFFFF;"); // Light background color
            buttonBox.setStyle("-fx-background-color: #FFFFFF;"); // Light background color for buttons
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
