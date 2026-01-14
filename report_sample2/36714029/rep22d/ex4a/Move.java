package ex4a;

public class Move {
	int index;
	Color color;

	public Move(int index, Color color) {
		this.index = index;
		this.color = color;
	}

	public Move flipped() {
		return new Move(this.index, this.color.flipped());
	}
}