package report_sample.ex12c.ex2c;

import java.util.*;

import report_sample.ex12a.search.*;

class EightPuzzleProblem {
	// 学生番号を設定してください
	static final int STUDENT_ID = 12345678; // ここに実際の学生番号を入れてください

	public static void main(String[] args) {
		// 元の例
		int[] example = { 2, 3, 5, 7, 1, 6, 4, 8, 0 };

		// ランダム生成された初期状態
		int[] init100 = generateRandomState(100);
		int[] init200 = generateRandomState(200);
		int[] init300 = generateRandomState(300);

		// 全ての初期状態
		int[][] initialStates = { example, init100, init200, init300 };
		String[] labels = { "Example", "Random n=100", "Random n=200", "Random n=300" };

		var h = new EightPuzzleHeuristic();

		for (int i = 0; i < initialStates.length; i++) {
			System.out.println("========================================");
			System.out.println("Initial state: " + labels[i]);
			System.out.println("========================================");
			var puzzle = new EightPuzzleWorld(initialStates[i]);
			System.out.println(puzzle);
			System.out.println();

			// 最小コスト優先探索（繰り返し回避ありのみ）
			System.out.println("=== Minimum Cost Search ===");
			runWithCycleAvoidanceOnly(Evaluator.minCost(), new EightPuzzleWorld(initialStates[i]));

			// 最良優先探索
			System.out.println("\n=== Best-First Search ===");
			runComparison(Evaluator.bestFirst(h), new EightPuzzleWorld(initialStates[i]));

			// A*アルゴリズム
			System.out.println("\n=== A* Search ===");
			runComparison(Evaluator.aStar(h), new EightPuzzleWorld(initialStates[i]));

			System.out.println();
		}
	}

	private static void runWithCycleAvoidanceOnly(Evaluator eval, EightPuzzleWorld puzzle) {
		System.out.println("With cycle avoidance:");
		var solver = new ImprovedInformedSolver(eval);
		var goal = solver.search(new State(null, null, puzzle.clone()));
		if (goal != null) {
			System.out.printf("Solution found (path length: %d steps)\n", (int)goal.cost());
		} else {
			System.out.println("No solution found");
		}
		System.out.printf("visited: %d, max length: %d\n", solver.visited, solver.maxLen);
		System.out.println("Note: Without cycle avoidance is too slow for minimum cost search, skipped.");
	}

	private static void runComparison(Evaluator eval, EightPuzzleWorld puzzle) {
		final long MAX_VISITS = 50000; // 最大訪問ノード数

		// 繰り返し回避なし
		System.out.println("Without cycle avoidance:");
		var solverWithout = new LimitedInformedSolver(eval, MAX_VISITS);
		var goalWithout = solverWithout.search(new State(null, null, puzzle.clone()));
		long visitedWithout = solverWithout.visited;
		if (goalWithout != null) {
			System.out.printf("Solution found (path length: %d steps)\n", (int)goalWithout.cost());
		} else if (solverWithout.limitReached) {
			System.out.printf("Search stopped (limit reached: %d visits)\n", MAX_VISITS);
		} else {
			System.out.println("No solution found");
		}
		System.out.printf("visited: %d, max length: %d\n", solverWithout.visited, solverWithout.maxLen);

		// 繰り返し回避あり
		System.out.println("\nWith cycle avoidance:");
		var solverWith = new ImprovedInformedSolver(eval);
		var goalWith = solverWith.search(new State(null, null, puzzle.clone()));
		long visitedWith = solverWith.visited;
		if (goalWith != null) {
			System.out.printf("Solution found (path length: %d steps)\n", (int)goalWith.cost());
		} else {
			System.out.println("No solution found");
		}
		System.out.printf("visited: %d, max length: %d\n", solverWith.visited, solverWith.maxLen);

		// 削減率を計算
		if (visitedWithout >= MAX_VISITS) {
			System.out.printf("\n削減率の計算不可（繰り返し回避なしが上限に達しました）\n");
			System.out.printf("参考: 訪問ノード数は %d から %d に削減されました\n", visitedWithout, visitedWith);
		} else {
			double reductionRate = 1.0 - ((double) visitedWith / (double) visitedWithout);
			System.out.printf("\n削減率 = 1 - %d / %d = %.4f (%.2f%%)\n",
				visitedWith, visitedWithout, reductionRate, reductionRate * 100);
		}
	}

	private static int[] generateRandomState(int n) {
		Random rn = new Random(STUDENT_ID + n);
		int[] board = { 1, 2, 3, 4, 5, 6, 7, 8, 0 }; // ゴール状態

		for (int i = 0; i < n; i++) {
			int operation = rn.nextInt(4); // 0-3の整数値を生成
			board = applyOperation(board, operation);
		}

		return board;
	}

	private static int[] applyOperation(int[] board, int operation) {
		int[] newBoard = Arrays.copyOf(board, board.length);

		// 空白マスの位置を見つける
		int blankPos = -1;
		for (int i = 0; i < board.length; i++) {
			if (board[i] == 0) {
				blankPos = i;
				break;
			}
		}

		int blankRow = blankPos / 3;
		int blankCol = blankPos % 3;

		int targetRow = blankRow;
		int targetCol = blankCol;

		switch (operation) {
			case 0: // 上のタイルを下に
				targetRow = blankRow - 1;
				break;
			case 1: // 右のタイルを左に
				targetCol = blankCol + 1;
				break;
			case 2: // 下のタイルを上に
				targetRow = blankRow + 1;
				break;
			case 3: // 左のタイルを右に
				targetCol = blankCol - 1;
				break;
		}

		// 範囲チェック
		if (targetRow >= 0 && targetRow < 3 && targetCol >= 0 && targetCol < 3) {
			int targetPos = targetRow * 3 + targetCol;
			// タイルを交換
			newBoard[blankPos] = newBoard[targetPos];
			newBoard[targetPos] = 0;
		}
		// 範囲外の場合は何もしない（1回の操作として数える）

		return newBoard;
	}
}


class LimitedInformedSolver extends InformedSolver {
	long maxVisits;
	boolean limitReached = false;

	LimitedInformedSolver(Evaluator eval, long maxVisits) {
		super(eval);
		this.maxVisits = maxVisits;
	}

	@Override
	public State search(State root) {
		var openList = toMutable(List.of(root));

		while (!openList.isEmpty()) {
			if (visited >= maxVisits) {
				limitReached = true;
				return null;
			}

			var state = get(openList);
			visited += 1;

			if (state.isGoal())
				return state;

			var children = state.children();
			openList = concat(openList, children);

			sort(openList);

			maxLen = Math.max(maxLen, openList.size());
		}

		return null;
	}

	@Override
	protected List<State> toMutable(List<State> list) {
		return new ArrayList<State>(list);
	}

	@Override
	protected State get(List<State> list) {
		return list.remove(0);
	}

	@Override
	protected List<State> concat(List<State> xs, List<State> ys) {
		return toMutable(java.util.stream.Stream.concat(xs.stream(), ys.stream()).toList());
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

	public int hashCode() {
		return Arrays.hashCode(this.board);
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
