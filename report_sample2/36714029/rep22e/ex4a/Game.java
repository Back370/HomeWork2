package ex4a;
import java.util.*;

public class Game {
	public static void main(String[] args) {
		System.out.println("=== Comparison Test: MyPlayerV1 (without Move Ordering) vs MyPlayerV2 (with Move Ordering) ===\n");

		// Test 1: MyPlayerV1 (without Move Ordering)
		System.out.println("--- Game 1: RandomPlayer vs MyPlayerV1 (without Move Ordering) ---");
		var p0_1 = new RandomPlayer();
		var p1_1 = new MyPlayerV1(new Eval(), 6);
		var g1 = new Game(p0_1, p1_1);
		g1.play();
		g1.printResult();

		System.out.println("\n\n--- Game 2: RandomPlayer vs MyPlayerV2 (with Move Ordering) ---");
		// Test 2: MyPlayerV2 (with Move Ordering)
		var p0_2 = new RandomPlayer();
		var p1_2 = new MyPlayerV2(new Eval(), 6);
		var g2 = new Game(p0_2, p1_2);
		g2.play();
		g2.printResult();
	}

	State state;
	Map<Color, Player> players;

	public Game(Player black, Player white) {
		this.state = new State();
		black.color = Color.BLACK;
		white.color = Color.WHITE;
		this.players = Map.of(Color.BLACK, black, Color.WHITE, white, Color.NONE, new Player("draw"));
	}

	void play() {
		while (this.state.isGoal() == false) {
			var player = this.players.get(this.state.color);
			var move = player.think(this.state.clone());

			this.state = this.state.perform(move);
			System.out.println(this.state);
			System.out.println("--------------------");
		}
	}

	void printResult() {
		System.out.println("winner: " + this.players.get(this.state.winner()));
	}
}