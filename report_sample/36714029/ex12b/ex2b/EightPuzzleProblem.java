package ex12b.ex2b;

import java.util.*;
import ex12a.search.*;

class EightPuzzleProblem {
	public static void main(String[] args) {
		int[] b = { 2, 3, 5, 7, 1, 6, 4, 8, 0 };
		var puzzle = new EightPuzzleWorld(b);
		var h = new EightPuzzleHeuristic();

		System.out.println("Initial state:");
		System.out.println(puzzle);
		System.out.println();

		System.out.println("=== Minimum Cost Search ===");
		runSearch(new ImprovedInformedSolver(Evaluator.minCost()), new EightPuzzleWorld(b));

		System.out.println("\n=== Best-First Search ===");
		runSearch(new ImprovedInformedSolver(Evaluator.bestFirst(h)), new EightPuzzleWorld(b));

		System.out.println("\n=== A* Search ===");
		runSearch(new ImprovedInformedSolver(Evaluator.aStar(h)), new EightPuzzleWorld(b));
	}

	private static void runSearch(ImprovedInformedSolver solver, EightPuzzleWorld puzzle) {
		long startTime = System.currentTimeMillis();
		solver.solve(puzzle);
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		System.out.printf("Execution time: %d ms\n", executionTime);
	}
}

class EightPuzzleAction implements Action {
	int dRow;
	int dCol;
	String direction;

	static List<Action> all = List.of(
			new EightPuzzleAction(-1, 0, "up"),
			new EightPuzzleAction(1, 0, "down"),
			new EightPuzzleAction(0, -1, "left"),
			new EightPuzzleAction(0, 1, "right"));

	EightPuzzleAction(int dRow, int dCol, String direction) {
		this.dRow = dRow;
		this.dCol = dCol;
		this.direction = direction;
	}

	public float cost() {
		return 1;
	}

	public String toString() {
		return "move " + direction;
	}
}

class EightPuzzleWorld implements World {
	static int[] GOAL = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
	static EightPuzzleWorld goal = new EightPuzzleWorld(GOAL);

	int[] board;
	int blankPos;

	EightPuzzleWorld(int[] board) {
		this.board = Arrays.copyOf(board, board.length);
		this.blankPos = findBlankPosition();
	}

	private int findBlankPosition() {
		for (int i = 0; i < board.length; i++) {
			if (board[i] == 0) return i;
		}
		return -1;
	}

	public boolean equals(Object otherObj) {
		if (!(otherObj instanceof EightPuzzleWorld)) return false;
		var other = (EightPuzzleWorld) otherObj;
		return Arrays.equals(this.board, other.board);
	}

	public EightPuzzleWorld clone() {
		return new EightPuzzleWorld(this.board);
	}

	public boolean isValid() {
		return blankPos != -1;
	}

	public boolean isGoal() {
		return this.equals(goal);
	}

	public List<Action> actions() {
		return EightPuzzleAction.all;
	}

	public World successor(Action action) {
		var a = (EightPuzzleAction) action;
		var next = clone();

		int blankRow = next.blankPos / 3;
		int blankCol = next.blankPos % 3;
		int newRow = blankRow + a.dRow;
		int newCol = blankCol + a.dCol;

		if (newRow < 0 || newRow >= 3 || newCol < 0 || newCol >= 3) {
			next.blankPos = -1;
			return next;
		}

		int newPos = newRow * 3 + newCol;
		next.board[next.blankPos] = next.board[newPos];
		next.board[newPos] = 0;
		next.blankPos = newPos;

		return next;
	}

	public String toString() {
		var sb = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			sb.append(board[i]);
			if (i % 3 == 2) {
				sb.append("\n");
			} else {
				sb.append(" ");
			}
		}
		return sb.toString().trim();
	}
}

//誤タイルの個数を評価値とするヒューリスティック
class EightPuzzleHeuristic implements Heuristic {
	public float eval(State s) {
		var w = (EightPuzzleWorld) s.world();
		int misplaced = 0;
		for (int i = 0; i < 9; i++) {
			if (w.board[i] != 0 && w.board[i] != EightPuzzleWorld.GOAL[i]) {
				misplaced++;
			}
		}
		return misplaced;
	}
}
