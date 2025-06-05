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

public class Red7 extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void dealCard(boolean[] redsInDeck, boolean[] yellowsInDeck, boolean[] violetsInDeck, ArrayList<String> targetColors, ArrayList<Integer> targetNumbers) {
        
        boolean workingCard = false;
        
        while (!workingCard) {
    
            Random randGen = new Random();
            int cardIndex = randGen.nextInt(21);
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
                if (colorIndex == 0) {
                    targetColors.add("Red");
                } else if (colorIndex == 1) {
                    targetColors.add("Yellow");
                } else {
                    targetColors.add("Violet");
                }
                targetNumbers.add(numberIndex + 1);
                
                cardArray[numberIndex] = false;
            }
        }
    }
    
    public static void displayAll(Stage stage, String canvasColor, ArrayList<String> playerAPaletteColors, ArrayList<Integer> playerAPaletteNumbers, ArrayList<String> playerBPaletteColors, ArrayList<Integer> playerBPaletteNumbers, ArrayList<String> playerAHandColors, ArrayList<Integer> playerAHandNumbers, ArrayList<String> playerBHandColors, ArrayList<Integer> playerBHandNumbers) {
        VBox board = new VBox();
        board.setSpacing(2);

        Text handText = new Text("Player A's Hand");
        board.getChildren().add(handText);
        board.getChildren().add(getCardRow(playerAHandColors, playerAHandNumbers));
        
        board.getChildren().add(new Text("Player A's Palette:"));
        board.getChildren().add(getCardRow(playerAPaletteColors, playerAPaletteNumbers));
        
        board.getChildren().add(new Text("Canvas:"));
        Color color = getJavaFXColor(canvasColor);
        Rectangle canvasRectangle =  new Rectangle(175, 125, color);
        
        board.getChildren().add(canvasRectangle);
        
        board.getChildren().add(new Text("Player B's Palette:"));
        board.getChildren().add(getCardRow(playerBPaletteColors, playerBPaletteNumbers));

        board.getChildren().add(new Text("Player B's Hand"));
        board.getChildren().add(getCardRow(playerBHandColors, playerBHandNumbers));

        stage.setScene(new Scene(board));
        stage.show();
        stage.sizeToScene();
    }
    
    public static HBox getCardRow(ArrayList<String> colors, ArrayList<Integer> numbers) {
        HBox cardsRow = new HBox();
        for (int i = 0; i < colors.size(); i++) {
            StackPane cardGraphics = getCardGraphics(colors.get(i), numbers.get(i));
            cardsRow.getChildren().add(cardGraphics);
        }
        return cardsRow;
    }
    
    
    public static StackPane getCardGraphics(String color, int number) {
        StackPane cardPane = new StackPane();
        
        Rectangle cardBase = createCardBackground(color);
        Text cardNumberText = createCardText(color, number);
        
        cardPane.getChildren().add(cardBase);
        cardPane.getChildren().add(cardNumberText);
        
        return cardPane;
    }
    
    private static Color getJavaFXColor(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red": return Color.RED;
            case "yellow": return Color.YELLOW;
            case "violet": return Color.VIOLET;
            default: return Color.VIOLET;
        }
    }
    
    public static Rectangle createCardBackground(String color) {
        Color bgColor = getJavaFXColor(color);
        return new Rectangle(125, 175, bgColor);
    }
    
    public static Text createCardText(String color, int number) {
        Text cardNumberText = new Text("" + number);
        cardNumberText.setFont(Font.font("System", FontWeight.BOLD, 50.0));
        
        Color textColor = getTextColor(color);
        cardNumberText.setFill(textColor);
        
        return cardNumberText;
    }
    
    public static Color getTextColor(String cardColor) {
        return cardColor.equals("Yellow") ? Color.BLACK : Color.WHITE;
    }
    
    public static Rectangle createCanvasRectangle(String canvasColor) {
        Color color = getJavaFXColor(canvasColor);
        return new Rectangle(175, 125, color);
    }
    
    public static boolean playerWinning(String canvasColor, ArrayList<String> playerPaletteColors, ArrayList<Integer> playerPaletteNumbers, ArrayList<String> opponentPaletteColors, ArrayList<Integer> opponentPaletteNumbers) {
        if (canvasColor.equals("Red")) {
            Card maxPlayerCard = new Card("Nothing",0);
            for (int i = 0; i < playerPaletteColors.size(); i++) {
                String color = playerPaletteColors.get(i);
                int number = playerPaletteNumbers.get(i);
                if (number > maxPlayerCard.getNumber()) {
                    maxPlayerCard.setColor(color);
                    maxPlayerCard.setNumber(number);
                } else if (number == maxPlayerCard.getNumber())
     {
                    if (color.equals("Red") || (color.equals("Yellow") && maxPlayerCard.getColor().equals("Violet"))) {
                        maxPlayerCard.setColor(color);
                        maxPlayerCard.setNumber(number);
                    }
                }
            }
            String maxOpponentColor = "Nothing";
            int maxOpponentNumber = 0;
            for (int i = 0; i < opponentPaletteColors.size(); i++) {
                String color = opponentPaletteColors.get(i);
                int number = opponentPaletteNumbers.get(i);
                if (number > maxOpponentNumber) {
                    maxOpponentColor = color;
                    maxOpponentNumber = number;
                } else if (number == maxOpponentNumber) {
                    if (color.equals("Red") || (color.equals("Yellow") && maxPlayerCard.getColor().equals("Violet"))) {
                        maxOpponentColor = color;
                        maxOpponentNumber = number;
                    }
                }
            }
            
            System.out.println("Player's max card: " + maxPlayerCard.getColor() + " " + maxPlayerCard.getNumber());
            System.out.println("Opponent's max card: " + maxOpponentColor + " " + maxOpponentNumber);
            
            if (maxPlayerCard.getNumber() > maxOpponentNumber) {
                return true;
            } else if (maxPlayerCard.getNumber() == maxOpponentNumber) {
                if (maxPlayerCard.getColor() .equals("Red") || (maxPlayerCard.getColor() .equals("Yellow") && maxOpponentColor.equals("Violet"))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (canvasColor.equals("Yellow")) {
            ArrayList<Integer> playerReds = new ArrayList<Integer>();
            ArrayList<Integer> playerYellows = new ArrayList<Integer>();
            ArrayList<Integer> playerViolets = new ArrayList<Integer>();
            for (int i = 0; i < playerPaletteColors.size(); i++) {
                String color = playerPaletteColors.get(i);
                int number = playerPaletteNumbers.get(i);
                if (color.equals("Red")) {
                    playerReds.add(number);
                } else if (color.equals("Yellow")) {
                    playerYellows.add(number);
                } else if (color.equals("Violet")) {
                    playerViolets.add(number);
                }
            }
            ArrayList<Integer> maxPlayerList = playerReds;
            String playerMaxColor = "Red";

            if (playerYellows.size() > maxPlayerList.size()) {
                maxPlayerList = playerYellows;
                playerMaxColor = "Yellow";
            } else if (playerYellows.size() == maxPlayerList.size()) {

                int maxHighest = 0;
                for (int i = 0; i < maxPlayerList.size(); i++) {
                    maxHighest = Math.max(maxHighest, maxPlayerList.get(i));
                }
                int yellowHighest = 0;
                for (int i = 0; i < playerYellows.size(); i++) {
                    yellowHighest = Math.max(yellowHighest, playerYellows.get(i));
                }
                if (yellowHighest > maxHighest) {
                    maxPlayerList = playerYellows;
                    playerMaxColor = "Yellow";
                }
            }
            if (playerViolets.size() > maxPlayerList.size()) {
                maxPlayerList = playerViolets;
                playerMaxColor = "Violet";
            } else if (playerViolets.size() == maxPlayerList.size()) {

                int maxHighest = 0;
                for (int i = 0; i < maxPlayerList.size(); i++) {
                    maxHighest = Math.max(maxHighest, maxPlayerList.get(i));
                }
                int violetHighest = 0;
                for (int i = 0; i < playerViolets.size(); i++) {
                    violetHighest = Math.max(violetHighest, playerViolets.get(i));
                }
                if (violetHighest > maxHighest) {
                    maxPlayerList = playerViolets;
                    playerMaxColor = "Violet";
                }
            }
            
            ArrayList<Integer> opponentReds = new ArrayList<Integer>();
            ArrayList<Integer> opponentYellows = new ArrayList<Integer>();
            ArrayList<Integer> opponentViolets = new ArrayList<Integer>();
            for (int i = 0; i < opponentPaletteColors.size(); i++) {
                String color = opponentPaletteColors.get(i);
                int number = opponentPaletteNumbers.get(i);
                if (color.equals("Red")) {
                    opponentReds.add(number);
                } else if (color.equals("Yellow")) {
                    opponentYellows.add(number);
                } else if (color.equals("Violet")) {
                    opponentViolets.add(number);
                }
            }
            ArrayList<Integer> maxOpponentList = opponentReds;
            String opponentMaxColor = "Red";
            if (opponentYellows.size() > maxOpponentList.size()) {
                maxOpponentList = opponentYellows;
                opponentMaxColor = "Yellow";
            } else if (opponentYellows.size() == maxOpponentList.size()) {

                int maxHighest = 0;
                for (int i = 0; i < maxOpponentList.size(); i++) {
                    maxHighest = Math.max(maxHighest, maxOpponentList.get(i));
                }
                int yellowHighest = 0;
                for (int i = 0; i < opponentYellows.size(); i++) {
                    yellowHighest = Math.max(yellowHighest, opponentYellows.get(i));
                }
                if (yellowHighest > maxHighest) {
                    maxOpponentList = opponentYellows;
                    opponentMaxColor = "Yellow";
                }
            }
            if (opponentViolets.size() > maxOpponentList.size()) {
                maxOpponentList = opponentViolets;
                opponentMaxColor = "Violet";
            } else if (opponentViolets.size() == maxOpponentList.size()) {
                int maxHighest = 0;
                for (int i = 0; i < maxOpponentList.size(); i++) {
                    maxHighest = Math.max(maxHighest, maxOpponentList.get(i));
                }
                int violetHighest = 0;
                for (int i = 0; i < opponentViolets.size(); i++) {
                    violetHighest = Math.max(violetHighest, opponentViolets.get(i));
                }
                if (violetHighest > maxHighest) {
                    maxOpponentList = opponentViolets;
                    opponentMaxColor = "Violet";
                }
            }

            if (maxPlayerList.size() > maxOpponentList.size()) {
                return true;
            } else if (maxPlayerList.size() == maxOpponentList.size()) {
                int playerMaxNumber = 0;
                for (int i = 0; i < maxPlayerList.size(); i++) {
                    playerMaxNumber = Math.max(playerMaxNumber, maxPlayerList.get(i));
                }
                int opponentMaxNumber = 0;
                for (int i = 0; i < maxOpponentList.size(); i++) {
                    opponentMaxNumber = Math.max(opponentMaxNumber, maxOpponentList.get(i));
                }
                
                if (playerMaxNumber > opponentMaxNumber) {
                    return true;
                } else if (playerMaxNumber == opponentMaxNumber) {
                    if (playerMaxColor.equals("Red") || (playerMaxColor.equals("Yellow") && opponentMaxColor.equals("Violet"))) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            ArrayList<String> playerColorsBelow4 = new ArrayList<String>();
            ArrayList<Integer> playerNumbersBelow4 = new ArrayList<Integer>();
            String playerMaxColorBelow4 = "Nothing";
            int playerMaxNumberBelow4 = 0;
            for (int i = 0; i < playerPaletteColors.size(); i++) {
                String color = playerPaletteColors.get(i);
                int number = playerPaletteNumbers.get(i);
                if (number < 4) {
                    playerColorsBelow4.add(color);
                    playerNumbersBelow4.add(number);
                    if (number > playerMaxNumberBelow4) {
                        playerMaxColorBelow4 = color;
                        playerMaxNumberBelow4 = number;
                    } else if (number == playerMaxNumberBelow4) {
                        if (color.equals("Red") || (color.equals("Yellow") && playerMaxColorBelow4.equals("Violet"))) {
                            playerMaxColorBelow4 = color;
                            playerMaxNumberBelow4 = number;
                        }
                    }
                }
            }
            
            ArrayList<String> opponentColorsBelow4 = new ArrayList<String>();
            ArrayList<Integer> opponentNumbersBelow4 = new ArrayList<Integer>();
            String opponentMaxColorBelow4 = "Nothing";
            int opponentMaxNumberBelow4 = 0;
            for (int i = 0; i < opponentPaletteColors.size(); i++) {
                String color = opponentPaletteColors.get(i);
                int number = opponentPaletteNumbers.get(i);
                if (number < 4) {
                    opponentColorsBelow4.add(color);
                    opponentNumbersBelow4.add(number);
                    if (number > opponentMaxNumberBelow4) {
                        opponentMaxColorBelow4 = color;
                        opponentMaxNumberBelow4 = number;
                    } else if (number == opponentMaxNumberBelow4) {
                        if (color.equals("Red") || (color.equals("Yellow") && opponentMaxColorBelow4.equals("Violet"))) {
                            opponentMaxColorBelow4 = color;
                            opponentMaxNumberBelow4 = number;
                        }
                    }
                }
            }
            if (playerColorsBelow4.size() > opponentColorsBelow4.size()) {
                return true;
            } else if (playerColorsBelow4.size() == opponentColorsBelow4.size()) {
                if (playerMaxNumberBelow4 > opponentMaxNumberBelow4) {
                    return true;
                } else if (playerMaxNumberBelow4 == opponentMaxNumberBelow4) {
                    return (playerMaxColorBelow4.equals("Red") || (playerMaxColorBelow4.equals("Yellow") && opponentMaxColorBelow4.equals("Violet")));
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }  
    }

    public static <T> ArrayList<T>  cloneAL(ArrayList<T> toClone) {
        ArrayList<T> clone = new ArrayList<T>();
        for (T item : toClone) {
            clone.add(item);
        }
        return clone;
    }

    public static <T> void replaceContentsWithAnother(ArrayList<T> toReplace, ArrayList<T> newContents) {
        toReplace.clear();
        for (T item : newContents) {
            toReplace.add(item);
        }
    }
    public void start(Stage primaryStage) {
    
        primaryStage.setTitle("Red 7");
        
        boolean[] violetsInDeck = new boolean[] {true, true, true, true, true, true, true};
        boolean[] yellowsInDeck = new boolean[] {true, true, true, true, true, true, true};
        boolean[] redsInDeck = new boolean[] {true, true, true, true, true, true, true};
        
        String canvasColor = "Red";
        
        Player playerA = new Player();
        
        dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerA.getPaletteColors(), playerA.getPaletteNumbers());
        for (int i = 0; i < 4; i++) {
            dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerA.getHandColors(), playerA.getHandNumbers());
        }
        
        Player playerB = new Player();

        dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerB.getPaletteColors(), playerB.getPaletteNumbers());
        for (int i = 0; i < 4; i++) {
            dealCard(redsInDeck, yellowsInDeck, violetsInDeck, playerB.getHandColors(), playerB.getHandNumbers());
        }

        int currentPlayer;
        if (playerWinning(canvasColor, playerA.getPaletteColors(), playerA.getPaletteNumbers(), playerB.getPaletteColors(), playerB.getPaletteNumbers())) {
            currentPlayer = 1;
        } else {
            currentPlayer = 0;
        }
        
        String[] players = new String[] {"A", "B"};
        String player = players[currentPlayer];
        
        System.out.println("Player " + player + " goes first!");
        
        while (true) {
        
            displayAll(primaryStage, canvasColor, playerA.getPaletteColors(), playerA.getPaletteNumbers(), playerB.getPaletteColors(), playerB.getPaletteNumbers(), playerA.getHandColors(), playerA.getHandNumbers(), playerB.getHandColors(), playerB.getHandNumbers());
            
            Player currPlayer = new Player();
            Player opponent = new Player();

            if (currentPlayer == 0) {
                currPlayer.setHandColors(playerA.getHandColors());
                currPlayer.setHandNumbers(playerA.getHandNumbers());
                currPlayer.setPaletteColors(playerA.getPaletteColors());
                currPlayer.setPaletteNumbers(playerA.getPaletteNumbers());
                opponent.setHandColors(playerB.getHandColors());
                opponent.setHandNumbers(playerB.getHandNumbers());
                opponent.setPaletteColors(playerB.getPaletteColors());
                opponent.setPaletteNumbers(playerB.getPaletteNumbers());
            } else {
                currPlayer.setHandColors(playerB.getHandColors());
                currPlayer.setHandNumbers(playerB.getHandNumbers());
                currPlayer.setPaletteColors(playerB.getPaletteColors());
                currPlayer.setPaletteNumbers(playerB.getPaletteNumbers());
                opponent.setHandColors(playerA.getHandColors());
                opponent.setHandNumbers(playerA.getHandNumbers());
                opponent.setPaletteColors(playerA.getPaletteColors());
                opponent.setPaletteNumbers(playerA.getPaletteNumbers());
            }
            
            if (currPlayer.getHandColors().size() == 0) {
                break;
            }
            
            ArrayList<String> playChoices = new ArrayList<String>();
            playChoices.add("Play only to Palette");
            playChoices.add("Play only to Canvas");
            if (currPlayer.getHandColors().size() > 1) {
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
            
            Player newPlayer = currPlayer.clonePlayer();

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
                break;
            }
            
            if (playToPalette) {
                playChoices.clear();
                choiceChosen.clear();
                for (int i = 0; i < currPlayer.getHandColors().size(); i++) {
                    String color = currPlayer.getHandColors().get(i);
                    int number = currPlayer.getHandNumbers().get(i);
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
                String color = currPlayer.getHandColors().get(cardIndex);
                int number = currPlayer.getHandNumbers().get(cardIndex);
                newPlayer.removeHandColors(cardIndex);
                newPlayer.removeHandNumbers(cardIndex);
                newPlayer.addPaletteColors(color);
                newPlayer.addPaletteNumbers(number);
            }
            
            if (playToCanvas) {
                playChoices.clear();
                choiceChosen.clear();
                for (int i = 0; i < newPlayer.getHandColors().size(); i++) {
                    String color = newPlayer.getHandColors().get(i);
                    int number = newPlayer.getHandNumbers().get(i);
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
                String color = newPlayer.getHandColors().get(cardIndex);
                int number = newPlayer.getHandNumbers().get(cardIndex);
                newPlayer.getHandColors().remove(cardIndex);
                newPlayer.getHandNumbers().remove(cardIndex);
                newCanvasColor = color;
            }
                
            if (playerWinning(newCanvasColor, newPlayer.getPaletteColors(), newPlayer.getPaletteNumbers(), opponent.getPaletteColors(), opponent.getPaletteNumbers())) {
                System.out.println("That move works!");
                replaceContentsWithAnother(currPlayer.getHandColors(), newPlayer.getHandColors());
                replaceContentsWithAnother(currPlayer.getHandNumbers(), newPlayer.getHandNumbers());
                replaceContentsWithAnother(currPlayer.getPaletteColors(), newPlayer.getPaletteColors());
                replaceContentsWithAnother(currPlayer.getPaletteNumbers(), newPlayer.getPaletteNumbers());
                canvasColor = newCanvasColor;
                
                currentPlayer = (currentPlayer + 1) % 2; //move to the next player
                player = players[currentPlayer];
            } else {
                System.out.println("That move doesn't work!");
            }
            
            
            
        }
        System.out.println("Player " + player + " loses!");  
    }
}
