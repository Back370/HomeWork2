
package ex4a;

public class Player {
	String name;
	Color color;

	public Player(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public Move think(State state) {
		var move = search(state);
		move.color = this.color;
		return move;
	}

	Move search(State state) {
		return null;
	}
}