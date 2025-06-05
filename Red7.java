import java.util.Random;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * An implementation of the game Red7, by Asmadi Games.  Only includes two card colors: violet and indigo.  Thanks to Asmadi Games for granting me permission to use their game rules for educational purposes.  This implementation is intended to be terrible, and will be updated during the semester in a Software Engineering course.
 *
 * @author Kyle Burke <kwburke@plymouth.edu>
 */
public class Red7 extends Application {
    
    /**
     * Main method to run the game.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Removes a random card from the deck and puts it in the appropriate collection.
     */
    public static void dealCard(boolean[] redsInDeck, boolean[] yellowsInDeck, boolean[] violetsInDeck, ArrayList<String> targetColors, ArrayList<Integer> targetNumbers) {
        
        boolean workingCard = false;
        
        //keep trying to generate a working card
        while (!workingCard) {
        
            //generate a random index
            Random randGen = new Random();
            int cardIndex = randGen.nextInt(21); //21 because we only have three colors so far
            int colorIndex = cardIndex / 7;
            int numberIndex = cardIndex % 7;
            
            boolean[] cardArray = redsInDeck;
            if (colorIndex == 1) {
                cardArray = yellowsInDeck;
            } else if (colorIndex == 2) {
                cardArray = violetsInDeck;
            }
            
            workingCard = cardArray[numberIndex];
            if (workingCard) {
                //add the card to the output lists
                if (colorIndex == 0) {
                    targetColors.add("Red");
                } else if (colorIndex == 1) {
                    targetColors.add("Yellow");
                } else {
                    targetColors.add("Violet");
                }
                targetNumbers.add(numberIndex + 1);
                
                //remove the card from the deck
                cardArray[numberIndex] = false;
            }
        }
    }
    
    /**
     * Gets a HBox for a single row of cards.
     */
    public static HBox getCardRow(ArrayList<String> colors, ArrayList<Integer> numbers) {
        HBox cardsRow = new HBox();
        for (int i = 0; i < colors.size(); i++) {
            StackPane cardGraphics = getCardGraphics(colors.get(i), numbers.get(i));
            cardsRow.getChildren().add(cardGraphics);
        }
        return cardsRow;
    }
    
    
    /**
     * Gets a StackPane with the graphics for a single card.
     */
    public static StackPane getCardGraphics(String color, int number) {
        StackPane cardPane = new StackPane();
        
        //add the color as a background rectangle
        Rectangle cardBase = new Rectangle(125, 175, Color.VIOLET);
        if (color.equals("Red")) {
            cardBase = new Rectangle(125, 175, Color.RED);
        } else if (color.equals("Yellow")) {
            cardBase = new Rectangle(125, 175, Color.YELLOW);
        }
        cardPane.getChildren().add(cardBase);
        
        //now add the text above that
        Text cardNumberText = new Text("" + number);
        cardNumberText.setFont(Font.font("System", FontWeight.BOLD, 50.0));
        if (color.equals("Yellow")) {
            cardNumberText.setFill(Color.BLACK);
        } else {
            cardNumberText.setFill(Color.WHITE);
        }
        cardPane.getChildren().add(cardNumberText);
        
        return cardPane;
    }
    
    /**
     * Clones an ArrayList.
     */
    public static <T> ArrayList<T>  cloneAL(ArrayList<T> toClone) {
        ArrayList<T> clone = new ArrayList<T>();
        for (T item : toClone) {
            clone.add(item);
        }
        return clone;
    }
    
    /**
     * Replaces an array list's contents with another.
     */
    public static <T> void replaceContentsWithAnother(ArrayList<T> toReplace, ArrayList<T> newContents) {
        toReplace.clear();
        for (T item : newContents) {
            toReplace.add(item);
        }
    }

    /**
     * Start the game.
     */
    public void start(Stage primaryStage) {
    
        primaryStage.setTitle("Red 7");
        
        //cards that are still in the deck
        boolean[] violetsInDeck = new boolean[] {true, true, true, true, true, true, true};
        boolean[] yellowsInDeck = new boolean[] {true, true, true, true, true, true, true};
        boolean[] redsInDeck = new boolean[] {true, true, true, true, true, true, true};
        
        //canvas
        String canvasColor = "Red";
        
        //playerA setup
        ArrayList<String> playerAHandColors = new ArrayList<String>();
        ArrayList<Integer> playerAHandNumbers = new ArrayList<Integer>();
        ArrayList<String> playerAPaletteColors = new ArrayList<String>();
        ArrayList<Integer> playerAPaletteNumbers = new ArrayList<Integer>();
        
        //deal cards to playerA
        //1 to the palette
        dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerAPaletteColors, playerAPaletteNumbers);
        //4 to the hand
        for (int i = 0; i < 4; i++) {
            dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerAHandColors, playerAHandNumbers);
        }
        
        //playerB setup
        ArrayList<String> playerBHandColors = new ArrayList<String>();
        ArrayList<Integer> playerBHandNumbers = new ArrayList<Integer>();
        ArrayList<String> playerBPaletteColors = new ArrayList<String>();
        ArrayList<Integer> playerBPaletteNumbers = new ArrayList<Integer>();
        
        //deal cards to playerB
        //1 to the palette
        dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerBPaletteColors, playerBPaletteNumbers);
        //4 to the hand
        for (int i = 0; i < 4; i++) {
            dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerBHandColors, playerBHandNumbers);
        }
        
        //set the first player based on who is winning.
        int currentPlayer;
        if (playerWinning(canvasColor, playerAPaletteColors, playerAPaletteNumbers, playerBPaletteColors, playerBPaletteNumbers)) {
            currentPlayer = 1;
        } else {
            currentPlayer = 0;
        }
        
        String[] players = new String[] {"A", "B"};
        String player = players[currentPlayer];
        
        System.out.println("Player " + player + " goes first!");
        
        /* */
        while (true) {
        
            displayAll(primaryStage, canvasColor, playerAPaletteColors, playerAPaletteNumbers, playerBPaletteColors, playerBPaletteNumbers, playerAHandColors, playerAHandNumbers, playerBHandColors, playerBHandNumbers);
            
            ArrayList<String> currentPlayerHandColors;
            ArrayList<Integer> currentPlayerHandNumbers;
            ArrayList<String> currentPlayerPaletteColors;
            ArrayList<Integer> currentPlayerPaletteNumbers;
            ArrayList<String> opponentHandColors;
            ArrayList<Integer> opponentHandNumbers;
            ArrayList<String> opponentPaletteColors;
            ArrayList<Integer> opponentPaletteNumbers;
            if (currentPlayer == 0) {
                currentPlayerHandColors = playerAHandColors;
                currentPlayerHandNumbers = playerAHandNumbers;
                currentPlayerPaletteColors = playerAPaletteColors;
                currentPlayerPaletteNumbers = playerAPaletteNumbers;
                opponentHandColors = playerBHandColors;
                opponentHandNumbers = playerBHandNumbers;
                opponentPaletteColors = playerBPaletteColors;
                opponentPaletteNumbers = playerBPaletteNumbers;
            } else {
                currentPlayerHandColors = playerBHandColors;
                currentPlayerHandNumbers = playerBHandNumbers;
                currentPlayerPaletteColors = playerBPaletteColors;
                currentPlayerPaletteNumbers = playerBPaletteNumbers;
                opponentHandColors = playerAHandColors;
                opponentHandNumbers = playerAHandNumbers;
                opponentPaletteColors = playerAPaletteColors;
                opponentPaletteNumbers = playerAPaletteNumbers;
            }
            
            if (currentPlayerHandColors.size() == 0) {
                break;
            }
            
            ArrayList<String> playChoices = new ArrayList<String>();
            playChoices.add("Play only to Palette");
            playChoices.add("Play only to Canvas");
            if (currentPlayerHandColors.size() > 1) {
                playChoices.add("Play to Palette and Canvas");
            }
            playChoices.add("Concede");
            ArrayList<String> choiceChosen = new ArrayList<String>();
            ChoiceDialog<String> dialog = new ChoiceDialog<String>(playChoices.get(0), playChoices);
            dialog.setTitle("Player " + player + "'s turn.");
            dialog.setHeaderText("Player " + player + ", Choose your move.");
            dialog.setContentText("Options:");
        
            System.out.println("Player " + player + "'s turn...");
            while (true) {
                dialog.showAndWait().ifPresent( (response) -> {
                    choiceChosen.add(response);
                });
                if (choiceChosen.size() == 1) {
                    break;
                }
            }
            String choice = choiceChosen.get(0);
            
            ArrayList<String> newPlayerPaletteColors = cloneAL(currentPlayerPaletteColors);
            ArrayList<Integer> newPlayerPaletteNumbers = cloneAL(currentPlayerPaletteNumbers);
            ArrayList<String> newPlayerHandColors = cloneAL(currentPlayerHandColors);
            ArrayList<Integer> newPlayerHandNumbers = cloneAL(currentPlayerHandNumbers);
            String newCanvasColor = canvasColor;
            boolean playToPalette = false;
            boolean playToCanvas = false;
            if (choice.equals("Play only to Palette")) {
                playToPalette = true;
            } else if (choice.equals("Play only to Canvas")) {
                playToCanvas = true;
            } else if (choice.equals("Play to Palette and Canvas")) {
                playToPalette = true;
                playToCanvas = true;
            } else {
                //concede
                break;
            }
            
            if (playToPalette) {
                playChoices.clear();
                choiceChosen.clear();
                for (int i = 0; i < currentPlayerHandColors.size(); i++) {
                    String color = currentPlayerHandColors.get(i);
                    int number = currentPlayerHandNumbers.get(i);
                    playChoices.add(i + ": Play " + color + " " + number + " to the palette.");
                }
                dialog = new ChoiceDialog<String>(playChoices.get(0), playChoices);
                dialog.setTitle("Palette card");
                dialog.setHeaderText("Player " + player + ", pick your card");
                dialog.setContentText("Options:");
                while (true) {
                    dialog.showAndWait().ifPresent( (response) -> {
                        choiceChosen.add(response);
                    });
                    if (choiceChosen.size() == 1) {
                        break;
                    }
                }
                choice = choiceChosen.get(0);
                int cardIndex = 0; 
                try {
                    cardIndex = Integer.parseInt(choice.substring(0, 1));
                } catch (Exception e) {
                    System.err.print("This shouldn't happen!");
                }
                String color = currentPlayerHandColors.get(cardIndex);
                int number = currentPlayerHandNumbers.get(cardIndex);
                newPlayerHandColors.remove(cardIndex);
                newPlayerHandNumbers.remove(cardIndex);
                newPlayerPaletteColors.add(color);
                newPlayerPaletteNumbers.add(number);
            }
            
            if (playToCanvas) {
                playChoices.clear();
                choiceChosen.clear();
                for (int i = 0; i < newPlayerHandColors.size(); i++) {
                    String color = newPlayerHandColors.get(i);
                    int number = newPlayerHandNumbers.get(i);
                    playChoices.add(i + ": Play " + color + " " + number + " to the canvas.");
                }
                dialog = new ChoiceDialog<String>(playChoices.get(0), playChoices);
                dialog.setTitle("Canvas card");
                dialog.setHeaderText("Player " + player + ", pick your card");
                dialog.setContentText("Options:");
                while (true) {
                    dialog.showAndWait().ifPresent( (response) -> {
                        choiceChosen.add(response);
                    });
                    if (choiceChosen.size() == 1) {
                        break;
                    }
                }
                choice = choiceChosen.get(0);
                int cardIndex = 0; 
                try {
                    cardIndex = Integer.parseInt(choice.substring(0, 1));
                } catch (Exception e) {
                    System.err.print("This shouldn't happen!");
                }
                String color = newPlayerHandColors.get(cardIndex);
                int number = newPlayerHandNumbers.get(cardIndex);
                newPlayerHandColors.remove(cardIndex);
                newPlayerHandNumbers.remove(cardIndex);
                newCanvasColor = color;
            }
                
            if (playerWinning(newCanvasColor, newPlayerPaletteColors, newPlayerPaletteNumbers, opponentPaletteColors, opponentPaletteNumbers)) {
                //great!  Apply the move!
                System.out.println("That move works!");
                replaceContentsWithAnother(currentPlayerHandColors, newPlayerHandColors);
                replaceContentsWithAnother(currentPlayerHandNumbers, newPlayerHandNumbers);
                replaceContentsWithAnother(currentPlayerPaletteColors, newPlayerPaletteColors);
                replaceContentsWithAnother(currentPlayerPaletteNumbers, newPlayerPaletteNumbers);
                canvasColor = newCanvasColor;
                
                //switch players
                currentPlayer = (currentPlayer + 1) % 2; //move to the next player
                player = players[currentPlayer];
            } else {
                System.out.println("That move doesn't work!");
            }
            
            
            
        }
        /* */
        
        System.out.println("Player " + player + " loses!");
        
        
    }
        


} //end of Red7
