package ex11d.ex1d;
import java.util.*;

public class MisCanProblem {

	public static void main(String[] args) {
		// コマンドライン引数で単一のkを指定するか、範囲測定を行う
		if (args.length > 0) {
			try {
				int k = Integer.parseInt(args[0]);
				if (k < 1) {
					System.out.println("Error: k must be at least 1");
					return;
				}
				runSingleTest(k);
			} catch (NumberFormatException e) {
				System.out.println("Error: Invalid number format");
				return;
			}
		} else {
			// デフォルト: k = 3～10の範囲で測定
			runBenchmark();
		}
	}


	static void runSingleTest(int k) {
		int boatCapacity = k / 2 + 1;

		System.out.println("\n\n");
		System.out.println("################################################################################");
		System.out.println("##                                                                            ##");
		System.out.println("##       Missionaries and Cannibals Problem (k = " + k + ", boat = " + boatCapacity + ")              ##");
		System.out.println("##              BREADTH-FIRST SEARCH (Horizontal Search)                      ##");
		System.out.println("##                                                                            ##");
		System.out.println("################################################################################");
		System.out.println();

		// 横型探索（幅優先探索）
		var bfsSolver = new Solver();
		bfsSolver.solve(new MisCanWorld(k, k, 1, k, boatCapacity), "Breadth-First Search (BFS)");

		System.out.println("\n\n");
		System.out.println("################################################################################");
		System.out.println("##                                                                            ##");
		System.out.println("##                DEPTH-FIRST SEARCH (Vertical Search)                        ##");
		System.out.println("##                                                                            ##");
		System.out.println("################################################################################");
		System.out.println();

		// 縦型探索（深さ優先探索）
		var dfsSolver = new DepthFirstSolver();
		dfsSolver.solve(new MisCanWorld(k, k, 1, k, boatCapacity), "Depth-First Search (DFS)");
	}


	static void runBenchmark() {
		System.out.println("\n\n");
		System.out.println("################################################################################");
		System.out.println("##                                                                            ##");
		System.out.println("##       Missionaries and Cannibals Problem - Benchmark (k = 3-10)           ##");
		System.out.println("##                   Breadth-First Search Performance                         ##");
		System.out.println("##                                                                            ##");
		System.out.println("################################################################################");
		System.out.println();
		System.out.println("Boat Capacity Formula: k/2 + 1");
		System.out.println("Constraint: M >= C on both banks and on the boat");
		System.out.println();
		System.out.println("=".repeat(80));
		System.out.printf("| %2s | %13s | %14s | %16s | %14s |\n",
			"k", "Boat Capacity", "Visited Nodes", "Max Open List", "Time (ms)");
		System.out.println("=".repeat(80));

		for (int k = 3; k <= 10; k++) {
			int boatCapacity = k / 2 + 1;
			var solver = new Solver();
			var world = new MisCanWorld(k, k, 1, k, boatCapacity);

			// 探索を実行（出力を抑制）
			solver.solveQuiet(world);

			// 結果を表示
			System.out.printf("| %2d | %13d | %14d | %16d | %14d |\n",
				k, boatCapacity, solver.getVisitedNodes(),
				solver.getMaxOpenListSize(), solver.getExecutionTime());
		}

		System.out.println("=".repeat(80));
		System.out.println();

		// クローズドリストの効果を検証
		runClosedListComparison();
	}


	static void runClosedListComparison() {
		System.out.println("\n\n");
		System.out.println("################################################################################");
		System.out.println("##                                                                            ##");
		System.out.println("##              Closed List Effect Comparison (k = 3-10)                     ##");
		System.out.println("##                                                                            ##");
		System.out.println("################################################################################");
		System.out.println();
		System.out.println("Comparing performance WITH and WITHOUT closed list");
		System.out.println();
		System.out.println("=".repeat(100));
		System.out.printf("| %2s | %20s | %20s | %23s | %20s |\n",
			"k", "With Closed List", "Without Closed List", "Visited Nodes Ratio", "Time Ratio");
		System.out.printf("| %2s | %9s | %9s | %9s | %9s | %11s | %9s |\n",
			"", "Visited", "Time(ms)", "Visited", "Time(ms)", "(Without/With)", "(W/o / W)");
		System.out.println("=".repeat(100));

		for (int k = 3; k <= 10; k++) {
			int boatCapacity = k / 2 + 1;
			var world1 = new MisCanWorld(k, k, 1, k, boatCapacity);
			var world2 = new MisCanWorld(k, k, 1, k, boatCapacity);

			// クローズドリストあり
			var solverWith = new Solver();
			solverWith.solveQuiet(world1);

			// クローズドリストなし（小さいkのみ実行、大きいkは時間がかかりすぎるため）
			var solverWithout = new SolverWithoutClosedList();
			long timeoutMs = 5000;  // 5秒のタイムアウト

			if (k <= 4) {  // k <= 4 のみ実行（k=5以上は計算時間が長すぎる）
				solverWithout.solveQuiet(world2);

				double visitedRatio = (double) solverWithout.getVisitedNodes() / solverWith.getVisitedNodes();
				double timeRatio = (double) solverWithout.getExecutionTime() / Math.max(1, solverWith.getExecutionTime());

				System.out.printf("| %2d | %9d | %9d | %9d | %9d | %11.2f | %9.2f |\n",
					k,
					solverWith.getVisitedNodes(), solverWith.getExecutionTime(),
					solverWithout.getVisitedNodes(), solverWithout.getExecutionTime(),
					visitedRatio, timeRatio);
			} else {
				System.out.printf("| %2d | %9d | %9d | %9s | %9s | %11s | %9s |\n",
					k,
					solverWith.getVisitedNodes(), solverWith.getExecutionTime(),
					"(too long)", "(too long)", "N/A", "N/A");
			}
		}

		System.out.println("=".repeat(100));
		System.out.println();
		System.out.println("Note: k > 4 skipped for 'Without Closed List' due to excessive computation time.");
		System.out.println();

		// 考察を出力
		printClosedListAnalysis();
	}


	static void printClosedListAnalysis() {
		System.out.println("=".repeat(80));
		System.out.println("                    Analysis: Effect of Closed List");
		System.out.println("=".repeat(80));
		System.out.println();
		System.out.println("【クローズドリストの効果】");
		System.out.println();
		System.out.println("1. 訪問ノード数の削減:");
		System.out.println("   - クローズドリストを使用することで、既に訪問した状態を再訪問しなくなる");
		System.out.println("   - これにより、訪問ノード数が大幅に削減される（特にkが大きい場合）");
		System.out.println("   - 上記の結果から、クローズドリストなしでは同じ状態を何度も訪問している");
		System.out.println();
		System.out.println("2. オープンリストのサイズ抑制:");
		System.out.println("   - 重複状態がオープンリストに追加されないため、メモリ使用量が削減される");
		System.out.println("   - オープンリストが肥大化すると、リストの操作コストも増加する");
		System.out.println();
		System.out.println("3. 実行時間の短縮:");
		System.out.println("   - 訪問ノード数の削減により、実行時間が大幅に短縮される");
		System.out.println("   - クローズドリストのチェックコスト < 重複探索のコスト");
		System.out.println();
		System.out.println("4. 問題の性質との関係:");
		System.out.println("   - この問題では、同じ状態に到達する経路が複数存在する");
		System.out.println("   - 例: (3M, 3C, 左岸) → (2M, 2C, 右岸) には複数の経路がある");
		System.out.println("   - そのため、クローズドリストの効果が特に顕著に現れる");
		System.out.println();
		System.out.println("5. トレードオフ:");
		System.out.println("   - クローズドリストは追加のメモリを必要とする（状態を記録）");
		System.out.println("   - しかし、このコストは重複探索を避けることで得られる利益に比べて小さい");
		System.out.println();
		System.out.println("【結論】");
		System.out.println("幅優先探索において、クローズドリストは必須の最適化手法である。");
		System.out.println("特に状態空間に循環や重複経路が存在する問題では、その効果は絶大である。");
		System.out.println("=".repeat(80));
		System.out.println();
	}
}


class MisCanAction implements Action {
	int missionary;  // 移動する宣教師の数（負の値は左岸から右岸、正の値は右岸から左岸）
	int cannibal;    // 移動する人食い人種の数（負の値は左岸から右岸、正の値は右岸から左岸）
	int boat;        // ボートの移動（-1：左岸から右岸、+1：右岸から左岸）


	MisCanAction(int missionary, int cannibal, int boat) {
		this.missionary = missionary;
		this.cannibal = cannibal;
		this.boat = boat;
	}

	static List<Action> generateActions(int boatCapacity) {
		List<Action> actions = new ArrayList<>();

		// 左岸から右岸への移動（負の値）
		for (int m = 0; m <= boatCapacity; m++) {
			for (int c = 0; c <= boatCapacity; c++) {
				// ボート上の制約: 宣教師がいる場合、M >= C でなければならない
				if (m + c >= 1 && m + c <= boatCapacity && (m == 0 || m >= c)) {
					actions.add(new MisCanAction(-m, -c, -1));
				}
			}
		}

		// 右岸から左岸への移動（正の値）
		for (int m = 0; m <= boatCapacity; m++) {
			for (int c = 0; c <= boatCapacity; c++) {
				// ボート上の制約: 宣教師がいる場合、M >= C でなければならない
				if (m + c >= 1 && m + c <= boatCapacity && (m == 0 || m >= c)) {
					actions.add(new MisCanAction(m, c, 1));
				}
			}
		}

		return actions;
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
	int missionary;    // 左岸にいる宣教師の数
	int cannibal;      // 左岸にいる人食い人種の数
	int boat;          // ボートの位置（1：左岸、0：右岸）
	int k;             // 各グループの総人数
	int boatCapacity;  // ボートの最大容量
	List<Action> allActions;  // 可能なアクションのキャッシュ

	public MisCanWorld(int missionary, int cannibal, int boat, int k) {
		this(missionary, cannibal, boat, k, 2);  // デフォルトのボート容量は2
	}


	public MisCanWorld(int missionary, int cannibal, int boat, int k, int boatCapacity) {
		this.missionary = missionary;
		this.cannibal = cannibal;
		this.boat = boat;
		this.k = k;
		this.boatCapacity = boatCapacity;
		this.allActions = MisCanAction.generateActions(boatCapacity);
	}

	
	public MisCanWorld clone() {
		var cloned = new MisCanWorld(this.missionary, this.cannibal, this.boat, this.k, this.boatCapacity);
		cloned.allActions = this.allActions;  // アクションリストを共有
		return cloned;
	}

	
	public boolean isValid() {
		// 宣教師の数が範囲内（0-k）かチェック
		if (this.missionary < 0 || this.missionary > this.k)
			return false;
		// 人食い人種の数が範囲内（0-k）かチェック
		if (this.cannibal < 0 || this.cannibal > this.k)
			return false;
		// ボートの位置が正しい（0または1）かチェック
		if (this.boat < 0 || this.boat > 1)
			return false;
		// 左岸で宣教師がいて、かつ人食い人種の方が多い場合は無効
		if (this.missionary > 0 && this.missionary < this.cannibal)
			return false;
		// 右岸で宣教師がいて、かつ人食い人種の方が多い場合は無効
		int rightM = this.k - this.missionary;
		int rightC = this.k - this.cannibal;
		if (rightM > 0 && rightM < rightC)
			return false;
		return true;
	}


	public boolean isGoal() {
		return this.missionary == 0 && this.cannibal == 0;
	}


	public List<Action> actions() {
		return this.allActions;
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
		int rightM = this.k - this.missionary;
		int rightC = this.k - this.cannibal;
		String rightM_str = "M".repeat(rightM);
		String rightC_str = "C".repeat(rightC);
		String rightBoat = this.boat == 0 ? "<boat>" : "      ";

		// 左岸と右岸を整形（幅を揃える）
		// kに応じて幅を動的に調整
		int width = Math.max(6, this.k + 1);
		String left = String.format("%-" + width + "s %-" + width + "s %s", leftM, leftC, leftBoat);
		String right = String.format("%s %-" + width + "s %-" + width + "s", rightBoat, rightM_str, rightC_str);

		return String.format("%s | ~~~~~~~~ | %s", left, right);
	}
}
