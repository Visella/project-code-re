import java.util.ArrayList;

import javafx.scene.control.ChoiceDialog;

public class InputHandler {


	public String promptPlayChoice(String player, int handSize) {
        ArrayList<String> choices = createPlayChoices(handSize);
        return showChoiceDialog(
            "Player " + player + "'s turn.",
            "Player " + player + ", choose your move.",
            "Options:",
            choices
        );
    }
    
    private ArrayList<String> createPlayChoices(int handSize) {
        ArrayList<String> choices = new ArrayList<>();
        choices.add("Play only to Palette");
        choices.add("Play only to Canvas");
        if (handSize > 1) {
            choices.add("Play to Palette and Canvas");
        }
        choices.add("Concede");
        return choices;
    }

    public int promptCardChoice(String playerName, ArrayList<Card> cards, String action) {
        ArrayList<String> choices = createCardChoices(cards, action);
        String result = showChoiceDialog(
            action + " card",
            "Player " + playerName + ", pick your card",
            "Options:",
            choices
        );
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
}
