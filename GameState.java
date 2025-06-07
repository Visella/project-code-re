import java.util.List;
import java.util.Random;

public class GameState {

	private static final int CARDS_PER_COLOR = 7;
    private static final int TOTAL_COLORS = 3;
    private static final int TOTAL_CARDS = CARDS_PER_COLOR * TOTAL_COLORS;
    private static final int INITIAL_HAND_SIZE = 4;
    
	private boolean[] redsInDeck;
    private boolean[] yellowsInDeck;
    private boolean[] violetsInDeck;
    private String canvasColor;
    private Player playerA;
    private Player playerB;
    private int currentPlayerIndex; 
    private Random randGen;
    private final String[] playerNames = {"A", "B"};
    
    public GameState() {
        this.randGen = new Random();
        initializeGame();
    }
    
    private void initializeGame() {
        initializeDecks();
        initializePlayers();
        setInitialPlayer();
    }
    
    private boolean[] createFullDeck() {
        boolean[] deck = new boolean[CARDS_PER_COLOR];
        for (int i = 0; i < CARDS_PER_COLOR; i++) {
            deck[i] = true;
        }
        return deck;
    }
    
    private void initializeDecks() {
    	this.redsInDeck = createFullDeck();
    	this.yellowsInDeck = createFullDeck();
    	this.violetsInDeck = createFullDeck();
    }
    
    private void setInitialPlayer() {
	    this.currentPlayerIndex = GameLogic.playerWinning(canvasColor, playerA.getPalette(), playerB.getPalette()) ? 1 : 0;
    }
    
    private void initializePlayers() {
        this.canvasColor = "Red"; 
        this.playerA = new Player();
        this.playerB = new Player();
        
        setupPlayer(playerA);
        setupPlayer(playerB);
    }
    
    private void setupPlayer(Player player) {
        dealCardToPalette(player);
        dealCardsToHand(player, INITIAL_HAND_SIZE);
    }
    
    private void dealCardToPalette(Player player) {
        dealCard(redsInDeck, yellowsInDeck, violetsInDeck, player.getPalette());
    }
    
    private void dealCardsToHand(Player player, int count) {
        for (int i = 0; i < count; i++) {
            dealCard(redsInDeck, yellowsInDeck, violetsInDeck, player.getHand());
        }
    }
    
    public void dealCard(boolean[] redsInDeck, boolean[] yellowsInDeck, boolean[] violetsInDeck, List<Card> targetList) {
        Card newCard = generateRandomCard();
        targetList.add(newCard);
    }
    
    private Card generateRandomCard() {
        while (true) {
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
            case 0: return redsInDeck;
            case 1: return yellowsInDeck;
            case 2: return violetsInDeck;
            default: throw new IllegalArgumentException("Invalid color index: " + colorIndex);
        }
    }
    
    private String getColorByIndex(int colorIndex) {
        switch (colorIndex) {
            case 0: return "Red";
            case 1: return "Yellow";
            case 2: return "Violet";
            default: throw new IllegalArgumentException("Invalid color index: " + colorIndex);
        }
    }
    
    public String getCanvasColor() { return canvasColor; }
    public Player getPlayerA() { return playerA; }
    public Player getPlayerB() { return playerB; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public Player getCurrentPlayer() { return (currentPlayerIndex == 0) ? playerA : playerB; }
    public Player getOpponentPlayer() { return (currentPlayerIndex == 0) ? playerB : playerA; }
    public String getCurrentPlayerName() { return playerNames[currentPlayerIndex]; }
    public String getOpponentPlayerName() { return playerNames[(currentPlayerIndex + 1) % 2]; }
    public void setCanvasColor(String canvasColor) { this.canvasColor = canvasColor; }
    public void switchPlayer() { this.currentPlayerIndex = (this.currentPlayerIndex + 1) % 2; }
}
