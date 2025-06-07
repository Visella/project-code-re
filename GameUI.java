import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameUI {
	
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
    
    private static void addPlayerSection(VBox board, String title, List<Card> cards) {
        board.getChildren().add(new Text(title));
        board.getChildren().add(getCardRow(cards));
    }
    
    private static void addCanvasSection(VBox board, String canvasColor) {
        board.getChildren().add(new Text("Canvas:"));
        Rectangle canvasRectangle = createCanvasRectangle(canvasColor);
        board.getChildren().add(canvasRectangle);
    }
    
    private static Rectangle createCanvasRectangle(String canvasColor) {
        Color color = getJavaFXColor(canvasColor);
        return new Rectangle(175, 125, color);
    }
    
    private static Color getJavaFXColor(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red": return Color.RED;
            case "yellow": return Color.YELLOW;
            case "violet": return Color.VIOLET;
            default: return Color.VIOLET;
        }
    }
    
    private static void showStage(Stage stage, VBox board) {
        stage.setScene(new Scene(board));
        stage.show();
        stage.sizeToScene();
    }
    
    private static HBox getCardRow(List<Card> cards) {
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
    
    private static Rectangle createCardBackground(String color) {
        Color bgColor = getJavaFXColor(color);
        return new Rectangle(125, 175, bgColor);
    }
    
    private static Text createCardText(String color, int number) {
        Text cardNumberText = new Text("" + number);
        cardNumberText.setFont(Font.font("System", FontWeight.BOLD, 50.0));
        
        Color textColor = getTextColor(color);
        cardNumberText.setFill(textColor);
        
        return cardNumberText;
    }
    
    private static Color getTextColor(String cardColor) {
        return cardColor.equals("Yellow") ? Color.BLACK : Color.WHITE;
    }

}
