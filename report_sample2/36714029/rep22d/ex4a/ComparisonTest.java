
package ex4a;

public class ComparisonTest {
	public static void main(String[] args) {
		System.out.println("=== Move Ordering Performance Comparison ===\n");

		// Test from initial position
		testFromInitialState();

		// Test from various game positions
		testFromMidgamePositions();
	}

	static void testFromInitialState() {
		System.out.println("--- Test 1: Search from initial empty board ---");

		var state = new State();

		// Test MyPlayerV1 (without Move Ordering)
		var v1 = new MyPlayerV1(new Eval(), 6);
		v1.color = Color.BLACK;
		v1.search(state.clone());

		// Test MyPlayerV2 (with Move Ordering)
		var v2 = new MyPlayerV2(new Eval(), 6);
		v2.color = Color.BLACK;
		v2.search(state.clone());

		System.out.println();
	}

	static void testFromMidgamePositions() {
		System.out.println("--- Test 2: Search from midgame position 1 ---");
		// Create a midgame position
		var state1 = new State();
		state1 = state1.perform(new Move(4, Color.BLACK));  // center
		state1 = state1.perform(new Move(0, Color.WHITE));  // top-left corner
		state1 = state1.perform(new Move(8, Color.BLACK));  // bottom-right corner

		var v1_1 = new MyPlayerV1(new Eval(), 6);
		v1_1.color = Color.WHITE;
		v1_1.search(state1.clone());

		var v2_1 = new MyPlayerV2(new Eval(), 6);
		v2_1.color = Color.WHITE;
		v2_1.search(state1.clone());

		System.out.println();

		System.out.println("--- Test 3: Search from midgame position 2 ---");
		// Another midgame position
		var state2 = new State();
		state2 = state2.perform(new Move(2, Color.BLACK));  // top-right
		state2 = state2.perform(new Move(4, Color.WHITE));  // center

		var v1_2 = new MyPlayerV1(new Eval(), 6);
		v1_2.color = Color.BLACK;
		v1_2.search(state2.clone());

		var v2_2 = new MyPlayerV2(new Eval(), 6);
		v2_2.color = Color.BLACK;
		v2_2.search(state2.clone());

		System.out.println();
	}
}
