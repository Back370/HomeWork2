

import java.util.*;

public class ImprovedInformedSolver {
	//ここで評価関数を差し替え
	Evaluator eval;
	public long visited = 0;
	public long maxLen = 0;

	public ImprovedInformedSolver(Evaluator eval) {
		this.eval = eval;
	}

	public void solve(World world) {
		var root = new State(null, null, world);
		var goal = search(root);

		if (goal != null)
			printSolution(goal);
		else
			System.out.println("No solution found");

		System.out.printf("visited: %d, max length: %d\n", this.visited, this.maxLen);
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

			this.maxLen = Math.max(this.maxLen, openList.size());
		}

		return null;
	}

	void sort(List<State> list) {
		list.sort(Comparator.comparing(s -> this.eval.f(s)));
	}

	void printSolution(State goal) {
		var list = new ArrayList<State>();

		while (goal != null) {
			list.add(0, goal);
			goal = goal.parent;
		}

		list.forEach(System.out::println);
	}
}
