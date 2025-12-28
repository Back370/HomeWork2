package ex11d.ex1d;

import java.util.*;
import java.util.stream.*;

interface World extends Cloneable {
	boolean isValid();
	boolean isGoal();
	List<Action> actions();
	World successor(Action action);
}

interface Action {
}

class State {
	State parent;
	Action action;
	World world;

	State(State parent, Action action, World child) {
		this.action = action;
		this.parent = parent;
		this.world = child;
	}

	public boolean isGoal() {
		return this.world.isGoal();
	}

	List<State> children() {
		return this.world.actions().stream()
				.map(a -> new State(this, a, this.world.successor(a)))
				.filter(s -> s.world.isValid())
				.toList();
	}

	public String toString() {
		if (this.action != null) {
			return String.format("%s (%s)", this.world, this.action);
		} else {
			return this.world.toString();
		}
	}
}

public class Solver {
	// 統計情報
	protected int visitedNodes = 0;      // 訪問ノード数
	protected int maxOpenListSize = 0;   // オープンリストの最大長
	protected long executionTime = 0;    // 実行時間（ミリ秒）
	protected String searchType = "";    // 探索タイプ

	public void solve(World world) {
		solve(world, "Breadth-First");  // デフォルトは横型探索
	}

	public void solve(World world, String type) {
		this.searchType = type;
		resetStatistics();

		var root = new State(null, null, world);
		long startTime = System.currentTimeMillis();
		var goal = search(root);
		this.executionTime = System.currentTimeMillis() - startTime;

		if (goal != null) {
			printSolution(goal);
			printStatistics();
		}
	}

	public void solveQuiet(World world) {
		this.searchType = "BFS";
		resetStatistics();

		var root = new State(null, null, world);
		long startTime = System.currentTimeMillis();
		var goal = search(root);
		this.executionTime = System.currentTimeMillis() - startTime;
	}

	public int getVisitedNodes() {
		return this.visitedNodes;
	}

	public int getMaxOpenListSize() {
		return this.maxOpenListSize;
	}

	public long getExecutionTime() {
		return this.executionTime;
	}

	void resetStatistics() {
		this.visitedNodes = 0;
		this.maxOpenListSize = 0;
		this.executionTime = 0;
	}

	State search(State root) {
		var openList = toMutable(List.of(root));
		var closedSet = new HashSet<String>();  // 訪問済み状態を記録

		while (openList.isEmpty() == false) {
			// オープンリストの最大長を更新
			if (openList.size() > maxOpenListSize) {
				maxOpenListSize = openList.size();
			}

			var state = get(openList);
			visitedNodes++;  // 訪問ノード数をカウント

			if (state.isGoal())
				return state;

			// 訪問済みチェック
			String stateKey = state.world.toString();
			if (closedSet.contains(stateKey))
				continue;

			closedSet.add(stateKey);

			var children = state.children();
			openList = concat(openList, children);
		}

		return null;
	}

	State get(List<State> list) {
		return list.remove(0);
	}

	List<State> concat(List<State> xs, List<State> ys) {
		return toMutable(Stream.concat(xs.stream(), ys.stream()).toList());
	}

	List<State> toMutable(List<State> list) {
		return new ArrayList<>(list);
	}

	void printSolution(State goal) {
		var list = new ArrayList<State>();

		while (goal != null) {
			list.add(0, goal);
			goal = goal.parent;
		}

		// タイトル表示
		System.out.println("=".repeat(80));
		System.out.println("           Missionaries and Cannibals Problem - Solution");
		System.out.println("           Search Type: " + this.searchType);
		System.out.println("=".repeat(80));
		System.out.println("Legend: M=Missionary, C=Cannibal, <boat>=Boat Position");
		System.out.println("        Left Bank         |    River    |         Right Bank");
		System.out.println("=".repeat(80));
		System.out.println();

		// 各ステップを表示
		for (int i = 0; i < list.size(); i++) {
			System.out.printf("Step %2d: %s\n", i, list.get(i));
			if (i < list.size() - 1) {
				System.out.println();
			}
		}

		System.out.println();
		System.out.println("=".repeat(80));
		System.out.printf("Goal Reached! Total steps: %d\n", list.size() - 1);
		System.out.println("=".repeat(80));
	}

	void printStatistics() {
		System.out.println();
		System.out.println("=".repeat(80));
		System.out.println("                        Search Statistics");
		System.out.println("=".repeat(80));
		System.out.printf("Search Type           : %s\n", this.searchType);
		System.out.printf("Visited Nodes         : %d\n", this.visitedNodes);
		System.out.printf("Max Open List Size    : %d\n", this.maxOpenListSize);
		System.out.printf("Execution Time        : %d ms\n", this.executionTime);
		System.out.println("=".repeat(80));
	}
}

class DepthFirstSolver extends Solver {
	@Override
	State get(List<State> list) {
		return list.remove(list.size() - 1);  // 末尾から取り出す（LIFO）
	}
}

class SolverWithoutClosedList extends Solver {
	@Override
	State search(State root) {
		var openList = toMutable(List.of(root));

		while (openList.isEmpty() == false) {
			// オープンリストの最大長を更新
			if (openList.size() > maxOpenListSize) {
				maxOpenListSize = openList.size();
			}

			var state = get(openList);
			visitedNodes++;  // 訪問ノード数をカウント

			if (state.isGoal())
				return state;

			// クローズドリストを使用しない（訪問済みチェックなし）
			var children = state.children();
			openList = concat(openList, children);
		}

		return null;
	}
}