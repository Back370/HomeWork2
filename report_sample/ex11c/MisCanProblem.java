package report_sample.ex11c;

import java.util.*;

/**
 * 宣教師と人食い人種の問題（Missionaries and Cannibals Problem）
 * 3人の宣教師と3人の人食い人種が川を渡る問題を解く
 * 制約：ボートには最大2人まで乗れる、どちらの岸でも人食い人種の数が宣教師の数を上回ってはならない
 */
public class MisCanProblem {
	/**
	 * メインメソッド
	 * 初期状態（左岸に宣教師3人、人食い人種3人、ボート1隻）から探索を開始
	 * 横型探索（幅優先探索）と縦型探索（深さ優先探索）の両方を実行して性能を比較
	 */
	public static void main(String[] args) {
		// 初期状態：宣教師3人、人食い人種3人、ボート1隻（左岸）
		var initialWorld = new MisCanWorld(3, 3, 1);

		System.out.println("\n\n");
		System.out.println("################################################################################");
		System.out.println("##                                                                            ##");
		System.out.println("##              BREADTH-FIRST SEARCH (Horizontal Search)                      ##");
		System.out.println("##                                                                            ##");
		System.out.println("################################################################################");
		System.out.println();

		// 横型探索（幅優先探索）
		var bfsSolver = new Solver();
		bfsSolver.solve(new MisCanWorld(3, 3, 1), "Breadth-First Search (BFS)");

		System.out.println("\n\n");
		System.out.println("################################################################################");
		System.out.println("##                                                                            ##");
		System.out.println("##                DEPTH-FIRST SEARCH (Vertical Search)                        ##");
		System.out.println("##                                                                            ##");
		System.out.println("################################################################################");
		System.out.println();

		// 縦型探索（深さ優先探索）
		var dfsSolver = new DepthFirstSolver();
		dfsSolver.solve(new MisCanWorld(3, 3, 1), "Depth-First Search (DFS)");

		System.out.println("\n\n");
		System.out.println("################################################################################");
		System.out.println("##                                                                            ##");
		System.out.println("##                       PERFORMANCE COMPARISON                               ##");
		System.out.println("##                                                                            ##");
		System.out.println("################################################################################");
		System.out.println("\nBoth search algorithms successfully found a solution.");
		System.out.println("See the statistics above for detailed performance comparison.");
		System.out.println();
	}
}

/**
 * 宣教師と人食い人種の移動アクションを表すクラス
 * 左岸から右岸、または右岸から左岸への移動を表現
 */
class MisCanAction implements Action {
	int missionary;  // 移動する宣教師の数（負の値は左岸から右岸、正の値は右岸から左岸）
	int cannibal;    // 移動する人食い人種の数（負の値は左岸から右岸、正の値は右岸から左岸）
	int boat;        // ボートの移動（-1：左岸から右岸、+1：右岸から左岸）

	// 可能なすべてのアクション（ボートには1人または2人が乗る）
	static List<Action> all = List.of(
			new MisCanAction(-2, 0, -1),   // 宣教師2人が左から右へ
			new MisCanAction(-1, -1, -1),  // 宣教師1人、人食い人種1人が左から右へ  
			new MisCanAction(0, -2, -1),   // 人食い人種2人が左から右へ
			new MisCanAction(-1, 0, -1),   // 宣教師1人が左から右へ
			new MisCanAction(0, -1, -1),   // 人食い人種1人が左から右へ
			new MisCanAction(+2, 0, +1),   // 宣教師2人が右から左へ
			new MisCanAction(+1, +1, +1),  // 宣教師1人、人食い人種1人が右から左へ
			new MisCanAction(0, +2, +1),   // 人食い人種2人が右から左へ
			new MisCanAction(+1, 0, +1),   // 宣教師1人が右から左へ
			new MisCanAction(0, +1, +1));  // 人食い人種1人が右から左へ

	/**
	 * コンストラクタ
	 * @param missionary 移動する宣教師の数
	 * @param cannibal 移動する人食い人種の数
	 * @param boat ボートの移動方向
	 */
	MisCanAction(int missionary, int cannibal, int boat) {
		this.missionary = missionary;
		this.cannibal = cannibal;
		this.boat = boat;
	}

	/**
	 * アクションの文字列表現を返す
	 * @return 移動内容を説明した文字列
	 */
	public String toString() {
		var dir = this.boat < 0 ? "RIGHT" : "LEFT ";
		var m = Math.abs(this.missionary);
		var c = Math.abs(this.cannibal);

		// 移動する人の説明を作成
		List<String> people = new ArrayList<>();
		if (m > 0) people.add(m + "M");
		if (c > 0) people.add(c + "C");
		String whoMoves = String.join(" + ", people);

		return String.format("[Move %s to %s]", whoMoves, dir);
	}
}

/**
 * 宣教師と人食い人種問題の世界状態を表すクラス
 * 左岸の宣教師数、人食い人種数、ボートの位置を管理
 */
class MisCanWorld implements World {
	int missionary;  // 左岸にいる宣教師の数
	int cannibal;    // 左岸にいる人食い人種の数  
	int boat;        // ボートの位置（1：左岸、0：右岸）

	/**
	 * コンストラクタ
	 * @param missionary 左岸の宣教師数
	 * @param cannibal 左岸の人食い人種数
	 * @param boat ボートの位置
	 */
	public MisCanWorld(int missionary, int cannibal, int boat) {
		this.missionary = missionary;
		this.cannibal = cannibal;
		this.boat = boat;
	}

	/**
	 * 現在の状態をクローンする
	 * @return 同じ状態の新しいMisCanWorldオブジェクト
	 */
	public MisCanWorld clone() {
		return new MisCanWorld(this.missionary, this.cannibal, this.boat);
	}

	/**
	 * 現在の状態が有効かどうかを判定
	 * @return 制約条件を満たしている場合true
	 */
	public boolean isValid() {
		// 宣教師の数が範囲内（0-3）かチェック
		if (this.missionary < 0 || this.missionary > 3)
			return false;
		// 人食い人種の数が範囲内（0-3）かチェック
		if (this.cannibal < 0 || this.cannibal > 3)
			return false;
		// ボートの位置が正しい（0または1）かチェック
		if (this.boat < 0 || this.boat > 1)
			return false;
		// 左岸で宣教師がいて、かつ人食い人種の方が多い場合は無効
		if (this.missionary > 0 && this.missionary < this.cannibal)
			return false;
		// 右岸で宣教師がいて、かつ人食い人種の方が多い場合は無効
		if ((3 - this.missionary) > 0 && (3 - this.missionary) < (3 - this.cannibal))
			return false;
		return true;
	}

	/**
	 * ゴール状態かどうかを判定
	 * @return 全員が右岸に移動した場合（左岸に誰もいない）true
	 */
	public boolean isGoal() {
		return this.missionary == 0 && this.cannibal == 0;
	}

	/**
	 * 現在の状態から実行可能なアクションのリストを返す
	 * @return すべての移動パターンのリスト
	 */
	public List<Action> actions() {
		return MisCanAction.all;
	}

	/**
	 * 指定されたアクションを実行した後の新しい状態を返す
	 * @param action 実行するアクション
	 * @return アクション実行後の新しい世界状態
	 */
	public World successor(Action action) {
		var a = (MisCanAction) action;
		var next = clone();
		// 左岸の人数とボート位置を更新
		next.missionary += a.missionary;
		next.cannibal += a.cannibal;
		next.boat += a.boat;
		return next;
	}

	/**
	 * 状態の文字列表現を返す（視覚的に分かりやすい形式）
	 * @return 川の両岸の状態を視覚的に表現した文字列
	 */
	public String toString() {
		// 左岸の状態
		String leftM = "M".repeat(this.missionary);
		String leftC = "C".repeat(this.cannibal);
		String leftBoat = this.boat == 1 ? "<boat>" : "      ";

		// 右岸の状態
		int rightM = 3 - this.missionary;
		int rightC = 3 - this.cannibal;
		String rightM_str = "M".repeat(rightM);
		String rightC_str = "C".repeat(rightC);
		String rightBoat = this.boat == 0 ? "<boat>" : "      ";

		// 左岸と右岸を整形（幅を揃える）
		String left = String.format("%-6s %-6s %s", leftM, leftC, leftBoat);
		String right = String.format("%s %-6s %-6s", rightBoat, rightM_str, rightC_str);

		return String.format("%s | ~~~~~~~~ | %s", left, right);
	}
}
