import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameLogic {

	private static final int THRESHOLD = 4;

    private static ArrayList<Card> getCardsBelowThreshold(List<Card> cards, int threshold) {
        ArrayList<Card> result = new ArrayList<>();
        for (Card card : cards) {
            if (card.getNumber() < threshold) {
                result.add(new Card(card.getColor(), card.getNumber()));
            }
        }
        return result;
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
    
    private static ColorSet getColorSet(ArrayList<Card> cards) {
        HashMap<String, ArrayList<Integer>> colorMap = createColorMap(cards);
        return findBestColorSet(colorMap);
    }

    private static HashMap<String, ArrayList<Integer>> createColorMap(ArrayList<Card> cards) {
        HashMap<String, ArrayList<Integer>> colorMap = new HashMap<>();
        for (Card card : cards) {
            colorMap.computeIfAbsent(card.getColor(), k -> new ArrayList<>()).add(card.getNumber());
        }
        return colorMap;
    }

    private static ColorSet findBestColorSet(HashMap<String, ArrayList<Integer>> colorMap) {
        String[] colorOrder = {"Red", "Yellow", "Violet"};
        String bestColor = "Red";
        ArrayList<Integer> bestList = colorMap.getOrDefault("Red", new ArrayList<>());
        int bestMax = getMaxFromList(bestList);

        for (String color : colorOrder) {
            ArrayList<Integer> currentList = colorMap.getOrDefault(color, new ArrayList<>());
            int currentMax = getMaxFromList(currentList);
            
            if (isColorSetBetter(currentList, currentMax, bestList, bestMax)) {
                bestList = currentList;
                bestColor = color;
                bestMax = currentMax;
            }
        }

        return new ColorSet(bestColor, bestList.size(), bestMax);
    }

    private static boolean isColorSetBetter(ArrayList<Integer> currentList, int currentMax, 
                                          ArrayList<Integer> bestList, int bestMax) {
        if (currentList.size() > bestList.size()) {
            return true;
        }
        return currentList.size() == bestList.size() && currentMax > bestMax;
    }

    private static int getMaxFromList(ArrayList<Integer> list) {
        int max = 0;
        for (int num : list) {
            max = Math.max(max, num);
        }
        return max;
    }

    private static boolean isColorHigherPriority(String c1, String c2) {
        if (c1.equals("Red")) return true;
        return c1.equals("Yellow") && c2.equals("Violet");
    }
    
    private static boolean compareCardsByNumberThenColor(Card playerCard, Card opponentCard) {
        if (playerCard == null && opponentCard == null) {
            return false; 
        }
        if (playerCard == null) {
            return false; 
        }
        if (opponentCard == null) {
            return true; 
        }

        if (playerCard.getNumber() > opponentCard.getNumber()) {
            return true;
        }
        if (playerCard.getNumber() == opponentCard.getNumber()) {
            return isColorHigherPriority(playerCard.getColor(), opponentCard.getColor());
        }
        return false;
    }

    private static boolean compareValuesAndThenColor(int playerValue, int opponentValue, String playerColor, String opponentColor) {
        if (playerValue > opponentValue) {
            return true;
        }
        if (playerValue == opponentValue) {
            return isColorHigherPriority(playerColor, opponentColor);
        }
        return false;
    }
    
    public static boolean playerWinning(String canvasColor, ArrayList<Card> playerPalette, ArrayList<Card> opponentPalette) {
        switch (canvasColor) {
            case "Red":
                return checkRedRule(playerPalette, opponentPalette);
            case "Yellow":
                return checkYellowRule(playerPalette, opponentPalette);
            default:
                return checkVioletRule(playerPalette, opponentPalette);
        }
    }
    
    private static boolean checkRedRule(ArrayList<Card> playerPalette, ArrayList<Card> opponentPalette) {
        Card maxPlayer = getMaxCardFromList(playerPalette);
        Card maxOpponent = getMaxCardFromList(opponentPalette);

        System.out.println("Player's max card: " + maxPlayer.getColor() + " " + maxPlayer.getNumber());
        System.out.println("Opponent's max card: " + maxOpponent.getColor() + " " + maxOpponent.getNumber());

        return compareCardsByNumberThenColor(maxPlayer, maxOpponent);
    }
    
    private static boolean checkYellowRule(ArrayList<Card> playerPalette, ArrayList<Card> opponentPalette) {
        ColorSet playerSet = getColorSet(playerPalette);
        ColorSet opponentSet = getColorSet(opponentPalette);

        if (playerSet.getCount() != opponentSet.getCount()) {
            return playerSet.getCount() > opponentSet.getCount();
        }
        
        return compareValuesAndThenColor(
            playerSet.getMaxNumber(), opponentSet.getMaxNumber(), 
            playerSet.getColor(), opponentSet.getColor()
        );
    }
    
    private static boolean checkVioletRule(ArrayList<Card> playerPalette, ArrayList<Card> opponentPalette) {
        ArrayList<Card> playerValidCards = getCardsBelowThreshold(playerPalette, THRESHOLD);
        ArrayList<Card> opponentValidCards = getCardsBelowThreshold(opponentPalette, THRESHOLD);

        if (playerValidCards.size() != opponentValidCards.size()) {
            return playerValidCards.size() > opponentValidCards.size();
        }

        Card maxPlayer = getMaxCardFromList(playerValidCards);
        Card maxOpponent = getMaxCardFromList(opponentValidCards);
        return compareCardsByNumberThenColor(maxPlayer, maxOpponent);
    }
    
}
