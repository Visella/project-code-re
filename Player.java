import java.util.ArrayList; 

public class Player{
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Card> palette = new ArrayList<>();

	public Player clonePlayer() {
        Player copy = new Player();
        for (Card card : this.hand) {
            copy.hand.add(new Card(card.getColor(), card.getNumber()));
        }
        for (Card card : this.palette) {
            copy.palette.add(new Card(card.getColor(), card.getNumber()));
        }
        return copy;
    }

	public static <T> void replaceContentsWithAnother(ArrayList<T> toReplace, ArrayList<T> newContents) {
        toReplace.clear();
        toReplace.addAll(newContents);
    }

    public void copyFrom(Player other) {
        replaceContentsWithAnother(this.hand, other.hand);
        replaceContentsWithAnother(this.palette, other.palette);
    }

	public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Card> getPalette() {
        return palette;
    }

	public void setHand(ArrayList<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }

	public void setPalette(ArrayList<Card> palette) {
        this.palette = new ArrayList<>(palette);
    }

    public Card removeCardFromHand(int index) {
        return hand.remove(index);
    }

    public void addCardToPalette(Card card) {
        palette.add(card);
    }
}



	
