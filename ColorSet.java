
public class ColorSet {
    private String color;
    private int count;
    private int maxNumber;

    public ColorSet(String color, int count, int maxNumber) {
        this.color = color;
        this.count = count;
        this.maxNumber = maxNumber;
    }

	public String getColor() {
		return color;
	}

	public int getCount() {
		return count;
	}

	public int getMaxNumber() {
		return maxNumber;
	}
}