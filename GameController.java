import javafx.stage.Stage;

public class GameController {

	private final Stage stage;
	private final InputHandler inputHandler;
	private GameState gameState;
	
	public GameController(Stage stage) {
		this.stage = stage;
		this.inputHandler = new InputHandler();
	}
	
	private void moveCardToPalette(Player player, int index) {
        Card card = player.removeCardFromHand(index);
        player.addCardToPalette(card);
    }

    private String moveCardToCanvas(Player player, int index) {
        Card card = player.removeCardFromHand(index);
        return card.getColor();
    }

    
    public void startGame() {
        this.gameState = new GameState();
        System.out.println("Player " + gameState.getCurrentPlayerName() + " goes first!");
        
        runGameLoop();
        
        System.out.println("Player " + gameState.getCurrentPlayerName() + " loses!");
    }
    
    private void runGameLoop() {
    	boolean gameIsRunning = true;
        while (gameIsRunning) {
            GameUI.displayAll(stage, gameState);

            if (gameState.getCurrentPlayer().getHand().isEmpty()) {
	            break;
	        }
	        
	        if (!processPlayerTurn(gameState)) {
	            break;
	        }
	    }
    }
    
    private boolean processPlayerTurn(GameState gameState) {
	    Player currentPlayer = gameState.getCurrentPlayer();
	    String currentPlayerName = gameState.getCurrentPlayerName();
	    
	    String playChoice = inputHandler.promptPlayChoice(currentPlayerName, currentPlayer.getHand().size());
	    if (playChoice.equals("Concede")) {
	        return false;
	    }
	    
	    return executePlayerMove(gameState, playChoice, currentPlayerName);
	}
    
    private boolean executePlayerMove(GameState gameState, String playChoice, String playerName) {
	    Player currentPlayer = gameState.getCurrentPlayer();
	    Player opponent = gameState.getOpponentPlayer();
	    
	    Player newPlayer = currentPlayer.clonePlayer();
	    String newCanvasColor = gameState.getCanvasColor();
	    
	    boolean playToPalette = playChoice.contains("Palette");
	    boolean playToCanvas = playChoice.contains("Canvas");
	    
	    if (playToPalette) {
	        int paletteCardIndex = inputHandler.promptCardChoice(playerName, newPlayer.getHand(), "Palette");
	        moveCardToPalette(newPlayer, paletteCardIndex);
	    }
	    
	    if (playToCanvas) {
	        int canvasCardIndex = inputHandler.promptCardChoice(playerName, newPlayer.getHand(), "Canvas");
	        newCanvasColor = moveCardToCanvas(newPlayer, canvasCardIndex);
	    }
	    
	    if (GameLogic.playerWinning(newCanvasColor, newPlayer.getPalette(), opponent.getPalette())) {
	        System.out.println("That move works!");
	        currentPlayer.copyFrom(newPlayer);
	        gameState.setCanvasColor(newCanvasColor);
	        gameState.switchPlayer();
	        return true;
	    } else {
	        System.out.println("That move doesn't work!");
	        return true;
	    }
	}
}
