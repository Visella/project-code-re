import java.util.ArrayList; 

public class Player{
    private ArrayList<String> handColors = new ArrayList<String>();
    private ArrayList<Integer> handNumbers = new ArrayList<Integer>();
    private ArrayList<String> paletteColors = new ArrayList<String>();
    private ArrayList<Integer> paletteNumbers = new ArrayList<Integer>();

	public Player clonePlayer() {
        Player copy = new Player();
		for(String color : this.handColors){
			copy.handColors.add(color);
		}
		for(Integer number : this.handNumbers){
			copy.handNumbers.add(number);
		}
		for(String color : this.paletteColors){
			copy.paletteColors.add(color);
		}
		for(Integer number : this.paletteNumbers){
			copy.paletteNumbers.add(number);
		}
        return copy;
    }

	public final ArrayList<String> getHandColors() {
		return handColors;
	}
	
	public final void setHandColors(ArrayList<String> handColors) {
		this.handColors = handColors;
	}
	public final ArrayList<Integer> getHandNumbers() {
		return handNumbers;
	}
	public final void setHandNumbers(ArrayList<Integer> handNumbers) {
		this.handNumbers = handNumbers;
	}
	public final ArrayList<String> getPaletteColors() {
		return paletteColors;
	}
	public final void setPaletteColors(ArrayList<String> paletteColors) {
		this.paletteColors = paletteColors;
	}
	public final ArrayList<Integer> getPaletteNumbers() {
		return paletteNumbers;
	}
	public final void setPaletteNumbers(ArrayList<Integer> paletteNumbers) {
		this.paletteNumbers = paletteNumbers;
	}

	public void removeHandColors(int index) {
		handColors.remove(index);
	}
	public void removeHandNumbers(int index) {
		handNumbers.remove(index);
	}
	public void addPaletteColors(String color) {
        paletteColors.add(color);
    }
	public void addPaletteNumbers(int number) {
        paletteNumbers.add(number);
    }
}