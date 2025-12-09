package report_sample.ex11b;

import java.util.*;

/**
 * 迷路探索問題のメインクラス
 * 幅優先探索を使用してゴール（"E"）までの経路を探索する
 */
public class MazeProblem {
	/**
	 * メインメソッド
	 * Solverを使用して迷路探索を実行する
	 */
	public static void main(String[] args) {
		var solver = new Solver();
		// スタート地点"A"から探索を開始
		solver.solve(new MazeWorld("A"));
	}
}

/**
 * 迷路での移動アクションを表すクラス
 * Actionインターフェースを実装
 */
class MazeAction implements Action {
	String next; // 移動先の位置

	/**
	 * コンストラクタ
	 * @param next 移動先の位置
	 */
	MazeAction(String next) {
		this.next = next;
	}

	/**
	 * アクションの文字列表現を返す
	 * @return "move to [移動先]"の形式の文字列
	 */
	public String toString() {
		return "move to " + this.next;
	}
}

/**
 * 迷路の世界状態を表すクラス
 * Worldインターフェースを実装
 */
class MazeWorld implements World {
	// 迷路のマップ構造（各位置から移動可能な位置のリスト）
	Map<String, List<String>> map = Map.of(
			"A", List.of("B", "C", "D"),
			"B", List.of("E", "F"),
			"C", List.of("G", "H"),
			"D", List.of("I", "J"));

	String current; // 現在の位置

	MazeWorld(String current) {
		this.current = current;
	}

	public boolean isValid() {
		return true;
	}

	public boolean isGoal() {
		return "E".equals(this.current);
	}

	public List<Action> actions() {
		return this.map.getOrDefault(current, Collections.emptyList()).stream()
				.map(p -> (Action) new MazeAction(p))
				.toList();
	}

	public World successor(Action action) {
		var a = (MazeAction) action;
		return new MazeWorld(a.next);
	}

	public String toString() {
		return this.current;
	}
}
