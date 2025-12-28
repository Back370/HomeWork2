package report_sample.ex12d.ex2d;

import java.util.*;
import java.io.*;

import report_sample.ex12a.search.*;

class EightPuzzleProblem {
	// 学生番号を設定してください
	static final int STUDENT_ID = 12345678; // ここに実際の学生番号を入れてください

	public static void main(String[] args) throws IOException {
		// 元の例
		int[] example = { 2, 3, 5, 7, 1, 6, 4, 8, 0 };

		// ランダム生成された初期状態
		int[] init100 = generateRandomState(100);
		int[] init200 = generateRandomState(200);
		int[] init300 = generateRandomState(300);

		// 難しい8パズルの例
		int[] difficult = { 8, 6, 7, 5, 0, 4, 2, 3, 1 };

		// 全ての初期状態
		int[][] initialStates = { example, init100, init200, init300, difficult };
		String[] labels = { "Example", "Random n=100", "Random n=200", "Random n=300", "Difficult Example" };

		// 3つのヒューリスティック関数
		var h1 = new HeuristicH1(); // h'1: 弱い（ミスプレースタイル数 / 2）
		var h2 = new HeuristicH2(); // h'2: 中程度（ミスプレースタイル数）
		var h3 = new HeuristicH3(); // h'3: 強い（マンハッタン距離）

		Heuristic[] heuristics = { h1, h2, h3 };
		String[] heuristicNames = { "h'1 (Misplaced/2)", "h'2 (Misplaced)", "h'3 (Manhattan)" };

		// 出力ファイルの準備
		PrintWriter writer = new PrintWriter(new FileWriter("8-puzzle-ex12d.txt"));

		for (int i = 0; i < initialStates.length; i++) {
			System.out.println("========================================");
			System.out.println("Initial state: " + labels[i]);
			System.out.println("========================================");
			var puzzle = new EightPuzzleWorld(initialStates[i]);
			System.out.println(puzzle);
			System.out.println();

			writer.println("========================================");
			writer.println("Initial state: " + labels[i]);
			writer.println("========================================");
			writer.println(puzzle);
			writer.println();

			// 各ヒューリスティック関数でA*探索を実行
			for (int j = 0; j < heuristics.length; j++) {
				System.out.println("\n=== A* Search with " + heuristicNames[j] + " ===");
				writer.println("=== A* Search with " + heuristicNames[j] + " ===");
				runAStarWithMetrics(heuristics[j], heuristicNames[j], new EightPuzzleWorld(initialStates[i]), writer);
			}

			System.out.println();
			writer.println();
		}

		writer.close();
		System.out.println("Results written to 8-puzzle-ex12d.txt");
	}

	private static void runAStarWithMetrics(Heuristic h, String hName, EightPuzzleWorld puzzle, PrintWriter writer) {
		var eval = Evaluator.aStar(h);
		var solver = new MetricInformedSolver(eval);

		long startTime = System.currentTimeMillis();
		var goal = solver.search(new State(null, null, puzzle.clone()));
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;

		if (goal != null) {
			int pathLength = (int) goal.cost();
			System.out.printf("Solution found (path length: %d steps)\n", pathLength);
			System.out.printf("Visited nodes: %d\n", solver.visited);
			System.out.printf("Max open list size: %d\n", solver.maxOpenLen);
			System.out.printf("Max closed list size: %d\n", solver.maxClosedLen);
			System.out.printf("Execution time: %d ms\n", executionTime);

			writer.printf("Solution found (path length: %d steps)\n", pathLength);
			writer.printf("Visited nodes: %d\n", solver.visited);
			writer.printf("Max open list size: %d\n", solver.maxOpenLen);
			writer.printf("Max closed list size: %d\n", solver.maxClosedLen);
			writer.printf("Execution time: %d ms\n", executionTime);
			writer.println();
			writer.println("Solution path:");
			printSolutionToWriter(goal, writer);
			writer.println();
		} else {
			System.out.println("No solution found");
			writer.println("No solution found");
		}
		System.out.println();
	}

	private static void printSolutionToWriter(State goal, PrintWriter writer) {
		var list = new ArrayList<State>();
		while (goal != null) {
			list.add(0, goal);
			goal = goal.parent();
		}

		for (int i = 0; i < list.size(); i++) {
			State s = list.get(i);
			writer.printf("Step %d (cost=%.0f):\n", i, s.cost());
			writer.println(s.world());
			if (s.action() != null) {
				writer.println("Action: " + s.action());
			}
			writer.println();
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

class HeuristicH1 implements Heuristic {
	public float eval(State s) {
		var w = (EightPuzzleWorld) s.world();
		int misplaced = 0;
		for (int i = 0; i < 9; i++) {
			if (w.board[i] != 0 && w.board[i] != EightPuzzleWorld.GOAL[i]) {
				misplaced++;
			}
		}
		return misplaced / 2.0f;
	}
}

class HeuristicH2 implements Heuristic {
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

class HeuristicH3 implements Heuristic {
	public float eval(State s) {
		var w = (EightPuzzleWorld) s.world();
		int totalDistance = 0;

		for (int i = 0; i < 9; i++) {
			int tile = w.board[i];
			if (tile != 0) {
				// 現在位置
				int currentRow = i / 3;
				int currentCol = i % 3;

				// 目標位置
				int goalPos = -1;
				for (int j = 0; j < 9; j++) {
					if (EightPuzzleWorld.GOAL[j] == tile) {
						goalPos = j;
						break;
					}
				}

				int goalRow = goalPos / 3;
				int goalCol = goalPos % 3;

				// マンハッタン距離を加算
				totalDistance += Math.abs(currentRow - goalRow) + Math.abs(currentCol - goalCol);
			}
		}

		return totalDistance;
	}
}


class MetricInformedSolver {
	Evaluator eval;
	public long visited = 0;
	public long maxOpenLen = 0;
	public long maxClosedLen = 0;

	public MetricInformedSolver(Evaluator eval) {
		this.eval = eval;
	}

	public State search(State root) {
		var openList = new ArrayList<State>();
		var closedSet = new HashSet<String>();
		openList.add(root);

		while (!openList.isEmpty()) {
			var state = openList.remove(0);

			String stateKey = state.world().toString();
			if (closedSet.contains(stateKey))
				continue;

			this.visited += 1;
			closedSet.add(stateKey);

			if (state.isGoal())
				return state;

			var children = state.children();
			for (var child : children) {
				String childKey = child.world().toString();
				if (!closedSet.contains(childKey)) {
					openList.add(child);
				}
			}

			sort(openList);

			this.maxOpenLen = Math.max(this.maxOpenLen, openList.size());
			this.maxClosedLen = Math.max(this.maxClosedLen, closedSet.size());
		}

		return null;
	}

	void sort(List<State> list) {
		list.sort(Comparator.comparing(s -> this.eval.f(s)));
	}
}
