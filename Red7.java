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
    private static final int CARDS_PER_COLOR = 7;
    private static final int TOTAL_COLORS = 3;
    private static final int TOTAL_CARDS = CARDS_PER_COLOR * TOTAL_COLORS;
    private static final int INITIAL_HAND_SIZE = 4;

    private boolean[] redsInDeck;
    private boolean[] yellowsInDeck;
    private boolean[] violetsInDeck;

    public static void main(String[] args) {
        launch(args);
    }

    public void dealCard(boolean[] redsInDeck, boolean[] yellowsInDeck, boolean[] violetsInDeck,
            ArrayList<Card> targetList) {
        Card newCard = generateRandomCard();
        targetList.add(newCard);
    }

    private Card generateRandomCard() {
        while (true) {
            Random randGen = new Random();
            int cardIndex = randGen.nextInt(TOTAL_CARDS);
            CardLocation location = getCardLocation(cardIndex);

            if (isCardAvailable(location)) {
                markCardAsUsed(location);
                return createCard(location);
            }
        }
    }

    private CardLocation getCardLocation(int cardIndex) {
        int colorIndex = cardIndex / CARDS_PER_COLOR;
        int numberIndex = cardIndex % CARDS_PER_COLOR;
        return new CardLocation(colorIndex, numberIndex);
    }

    private boolean isCardAvailable(CardLocation location) {
        boolean[] deck = getDeckByColorIndex(location.colorIndex);
        return deck[location.numberIndex];
    }

    private void markCardAsUsed(CardLocation location) {
        boolean[] deck = getDeckByColorIndex(location.colorIndex);
        deck[location.numberIndex] = false;
    }

    private Card createCard(CardLocation location) {
        String color = getColorByIndex(location.colorIndex);
        int number = location.numberIndex + 1;
        return new Card(color, number);
    }

    private boolean[] getDeckByColorIndex(int colorIndex) {
        switch (colorIndex) {
            case 0:
                return redsInDeck;
            case 1:
                return yellowsInDeck;
            case 2:
                return violetsInDeck;
            default:
                throw new IllegalArgumentException("Invalid color index: " + colorIndex);
        }
    }

    private String getColorByIndex(int colorIndex) {
        switch (colorIndex) {
            case 0:
                return "Red";
            case 1:
                return "Yellow";
            case 2:
                return "Violet";
            default:
                throw new IllegalArgumentException("Invalid color index: " + colorIndex);
        }
    }

    public static void displayAll(Stage stage, GameState gameState) {
        VBox board = createGameBoard(gameState);
        showStage(stage, board);
    }

    private static VBox createGameBoard(GameState gameState) {
        VBox board = new VBox();
        board.setSpacing(2);

        addPlayerSection(board, "Player A's Hand", gameState.getPlayerA().getHand());
        addPlayerSection(board, "Player A's Palette:", gameState.getPlayerA().getPalette());
        addCanvasSection(board, gameState.getCanvasColor());
        addPlayerSection(board, "Player B's Palette:", gameState.getPlayerB().getPalette());
        addPlayerSection(board, "Player B's Hand", gameState.getPlayerB().getHand());

        return board;
    }

    private static void addPlayerSection(VBox board, String title, ArrayList<Card> cards) {
        board.getChildren().add(new Text(title));
        board.getChildren().add(getCardRow(cards));
    }

    private static void addCanvasSection(VBox board, String canvasColor) {
        board.getChildren().add(new Text("Canvas:"));
        Rectangle canvasRectangle = createCanvasRectangle(canvasColor);
        board.getChildren().add(canvasRectangle);
    }

    private static void showStage(Stage stage, VBox board) {
        stage.setScene(new Scene(board));
        stage.show();
        stage.sizeToScene();
    }

    public static HBox getCardRow(ArrayList<Card> cards) {
        HBox cardsRow = new HBox();
        for (int i = 0; i < cards.size(); i++) {
            StackPane cardGraphics = getCardGraphics(cards.get(i).getColor(), cards.get(i).getNumber());
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
            case "red":
                return Color.RED;
            case "yellow":
                return Color.YELLOW;
            case "violet":
                return Color.VIOLET;
            default:
                return Color.VIOLET;
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

    private static Card getMaxCardFromList(ArrayList<Card> cards) {
        if (cards.isEmpty()) {
            return new Card("Nothing", 0);
        }

        Card maxCard = cards.get(0);
        for (Card card : cards) {
            if (isCardHigher(card, maxCard)) {
                maxCard = card;
            }
        }
        return new Card(maxCard.getColor(), maxCard.getNumber());
    }

    private static boolean isCardHigher(Card card1, Card card2) {
        if (card1.getNumber() > card2.getNumber()) {
            return true;
        }
        if (card1.getNumber() == card2.getNumber()) {
            return isColorHigherPriority(card1.getColor(), card2.getColor());
        }
        return false;
    }

    private static boolean isColorHigherPriority(String c1, String c2) {
        if (c1.equals("Red"))
            return true;
        return c1.equals("Yellow") && c2.equals("Violet");
    }

    public static boolean playerWinning(String canvasColor, ArrayList<Card> playerPalette,
            ArrayList<Card> opponentPalette) {
        if (canvasColor.equals("Red")) {
            Card maxPlayer = getMaxCardFromList(playerPalette);
            Card maxOpponent = getMaxCardFromList(opponentPalette);

            System.out.println("Player's max card: " + maxPlayer.getColor() + " " + maxPlayer.getNumber());
            System.out.println("Opponent's max card: " + maxOpponent.getColor() + " " + maxOpponent.getNumber());

            if (maxPlayer.getNumber() > maxOpponent.getNumber()) {
                return true;
            } else if (maxPlayer.getNumber() == maxOpponent.getNumber()) {
                if (maxPlayer.getColor().equals("Red")
                        || (maxPlayer.getColor().equals("Yellow") && maxOpponent.getColor().equals("Violet"))) {
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
            for (int i = 0; i < playerPalette.size(); i++) {
                Card card = playerPalette.get(i);
                if (card.getColor().equals("Red")) {
                    playerReds.add(card.getNumber());
                } else if (card.getColor().equals("Yellow")) {
                    playerYellows.add(card.getNumber());
                } else if (card.getColor().equals("Violet")) {
                    playerViolets.add(card.getNumber());
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
            for (int i = 0; i < opponentPalette.size(); i++) {
                Card card = opponentPalette.get(i);

                if (card.getColor().equals("Red")) {
                    opponentReds.add(card.getNumber());
                } else if (card.getColor().equals("Yellow")) {
                    opponentYellows.add(card.getNumber());
                } else if (card.getColor().equals("Violet")) {
                    opponentViolets.add(card.getNumber());
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
                    if (playerMaxColor.equals("Red")
                            || (playerMaxColor.equals("Yellow") && opponentMaxColor.equals("Violet"))) {
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
            for (int i = 0; i < playerPalette.size(); i++) {
                Card card = playerPalette.get(i);
                if (card.getNumber() < 4) {
                    playerColorsBelow4.add(card.getColor());
                    playerNumbersBelow4.add(card.getNumber());
                    if (card.getNumber() > playerMaxNumberBelow4) {
                        playerMaxColorBelow4 = card.getColor();
                        playerMaxNumberBelow4 = card.getNumber();
                    } else if (card.getNumber() == playerMaxNumberBelow4) {
                        if (card.getColor().equals("Red")
                                || (card.getColor().equals("Yellow") && playerMaxColorBelow4.equals("Violet"))) {
                            playerMaxColorBelow4 = card.getColor();
                            playerMaxNumberBelow4 = card.getNumber();
                        }
                    }
                }
            }

            ArrayList<String> opponentColorsBelow4 = new ArrayList<String>();
            ArrayList<Integer> opponentNumbersBelow4 = new ArrayList<Integer>();
            String opponentMaxColorBelow4 = "Nothing";
            int opponentMaxNumberBelow4 = 0;
            for (int i = 0; i < opponentPalette.size(); i++) {
                Card card = opponentPalette.get(i);
                if (card.getNumber() < 4) {
                    opponentColorsBelow4.add(card.getColor());
                    opponentNumbersBelow4.add(card.getNumber());
                    if (card.getNumber() > opponentMaxNumberBelow4) {
                        opponentMaxColorBelow4 = card.getColor();
                        opponentMaxNumberBelow4 = card.getNumber();
                    } else if (card.getNumber() == opponentMaxNumberBelow4) {
                        if (card.getColor().equals("Red")
                                || (card.getColor().equals("Yellow") && opponentMaxColorBelow4.equals("Violet"))) {
                            opponentMaxColorBelow4 = card.getColor();
                            opponentMaxNumberBelow4 = card.getNumber();
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
                    return (playerMaxColorBelow4.equals("Red")
                            || (playerMaxColorBelow4.equals("Yellow") && opponentMaxColorBelow4.equals("Violet")));
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static <T> ArrayList<T> cloneAL(ArrayList<T> toClone) {
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

        GameState gameState = new GameState();
        String playerName = gameState.getCurrentPlayerName();
        System.out.println("Player " + playerName + " goes first!");

        while (true) {

            displayAll(primaryStage, gameState);

            Player currPlayer = gameState.getCurrentPlayer();
            Player opponent = gameState.getOpponentPlayer();

            if (currPlayer.getHand().size() == 0) {
                break;
            }

            ArrayList<String> playChoices = new ArrayList<String>();
            playChoices.add("Play only to Palette");
            playChoices.add("Play only to Canvas");
            if (currPlayer.getHand().size() > 1) {
                playChoices.add("Play to Palette and Canvas");
            }
            playChoices.add("Concede");
            ArrayList<String> choiceChosen = new ArrayList<String>();
            ChoiceDialog<String> dialog = new ChoiceDialog<String>(playChoices.get(0), playChoices);
            dialog.setTitle("Player " + playerName + "'s turn.");
            dialog.setHeaderText("Player " + playerName + ", Choose your move.");
            dialog.setContentText("Options:");

            System.out.println("Player " + playerName + "'s turn...");
            while (true) {
                dialog.showAndWait().ifPresent((response) -> {
                    choiceChosen.add(response);
                });
                if (choiceChosen.size() == 1) {
                    break;
                }
            }
            String choice = choiceChosen.get(0);

            Player newPlayer = currPlayer.clonePlayer();

            String newCanvasColor = gameState.getCanvasColor();
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
                int paletteCardIndex = promptCardChoice(playerName, newPlayer.getHand(), "Palette");
                moveCardToPalette(newPlayer, paletteCardIndex);
            }

            if (playToCanvas) {
                int canvasCardIndex = promptCardChoice(playerName, newPlayer.getHand(), "Canvas");
                newCanvasColor = moveCardToCanvas(newPlayer, canvasCardIndex);
            }

            if (playerWinning(newCanvasColor, newPlayer.getPalette(), opponent.getPalette())) {
                System.out.println("That move works!");
                currPlayer.copyFrom(newPlayer);
                gameState.setCanvasColor(newCanvasColor);
                gameState.switchPlayer();
            } else {
                System.out.println("That move doesn't work!");
            }

        }
        System.out.println("Player " + playerName + " loses!");
    }

    public int promptCardChoice(String playerName, ArrayList<Card> cards, String action) {
        ArrayList<String> choices = createCardChoices(cards, action);
        String result = showChoiceDialog(
                action + " card",
                "Player " + playerName + ", pick your card",
                "Options:",
                choices);
        return Integer.parseInt(result.split(":")[0]);
    }

    private ArrayList<String> createCardChoices(ArrayList<Card> cards, String action) {
        ArrayList<String> choices = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            choices.add(i + ": Play " + c.getColor() + " " + c.getNumber() + " to the " + action.toLowerCase() + ".");
        }
        return choices;
    }

    private String showChoiceDialog(String title, String header, String content, ArrayList<String> choices) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        ArrayList<String> choiceChosen = new ArrayList<>();
        while (choiceChosen.isEmpty()) {
            dialog.showAndWait().ifPresent(choiceChosen::add);
        }
        return choiceChosen.get(0);
    }

    private void moveCardToPalette(Player player, int index) {
        Card card = player.removeCardFromHand(index);
        player.addCardToPalette(card);
    }

    private String moveCardToCanvas(Player player, int index) {
        Card card = player.removeCardFromHand(index);
        return card.getColor();
    }
}
