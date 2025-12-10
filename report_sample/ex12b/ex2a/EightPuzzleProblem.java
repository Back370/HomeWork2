package report_sample.ex12b.ex2a;

import java.util.*;

import report_sample.ex12a.search.*;

class EightPuzzleProblem {
	public static void main(String[] args) {
		var h = new EightPuzzleHeuristic();
		var search = new InformedSolver(Evaluator.aStar(h));
		int[] b = { 2, 3, 5, 7, 1, 6, 4, 8, 0 };
		var puzzle = new EightPuzzleWorld(b);
		search.solve(puzzle);
	}
}

class EightPuzzleAction implements Action {
	// 必要な変数を宣言

	static List<Action> all = List.of(
			new EightPuzzleAction(/* 空白上 */),
			new EightPuzzleAction(/* 空白下 */),
			new EightPuzzleAction(/* 空白左 */),
			new EightPuzzleAction(/* 空白右 */));

	EightPuzzleAction(int row, int col) {
		// 移動方向をセット
	}

	public float cost() {
		return 1;
	}

	public String toString() {
		return // 行為を文字列として返す
	}
}

class EightPuzzleWorld implements World {
	static int[] GOAL = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
	static EightPuzzleWorld goal = new EightPuzzleWorld(GOAL);

	int[] board;
	// その他必要な変数を宣言

	EightPuzzleWorld(int[] board) {
		this.board = Arrays.copyOf(board, board.length);
	}

	public boolean equals(Object otherObj) {
		return // このオブジェクトと otherObj が同値なら true を返す
	}

	public EightPuzzleWorld clone() {
		return // コピーを返す
	}

	public boolean isValid() {
		return // 合法な状態なら true を返す
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

		// 次の状態 next を生成する
		// 現在の状態に行為 a を適用した状態を生成すること

		return next;
	}

	public String toString() {
		// 1 2 3
		// 4 5 6
		// 7 8 0
		return // 上記のように盤面を表す文字列を返す
	}
}

class EightPuzzleHeuristic implements Heuristic {
	public float eval(State s) {
		return // h'(a) を返す
	}
}
