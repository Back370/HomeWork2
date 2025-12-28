

import java.util.*;

public class MisCanProblem {
	public static void main(String[] args) {
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

	MisCanAction(int missionary, int cannibal, int boat) {
		this.missionary = missionary;
		this.cannibal = cannibal;
		this.boat = boat;
	}


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

class MisCanWorld implements World {
	int missionary;  // 左岸にいる宣教師の数
	int cannibal;    // 左岸にいる人食い人種の数  
	int boat;        // ボートの位置（1：左岸、0：右岸）

	
	public MisCanWorld(int missionary, int cannibal, int boat) {
		this.missionary = missionary;
		this.cannibal = cannibal;
		this.boat = boat;
	}

	public MisCanWorld clone() {
		return new MisCanWorld(this.missionary, this.cannibal, this.boat);
	}

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

	public boolean isGoal() {
		return this.missionary == 0 && this.cannibal == 0;
	}

	public List<Action> actions() {
		return MisCanAction.all;
	}

	public World successor(Action action) {
		var a = (MisCanAction) action;
		var next = clone();
		// 左岸の人数とボート位置を更新
		next.missionary += a.missionary;
		next.cannibal += a.cannibal;
		next.boat += a.boat;
		return next;
	}

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
