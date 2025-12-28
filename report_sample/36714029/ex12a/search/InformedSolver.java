package ex12a.search;

import java.util.*;
import java.util.stream.*;

public class InformedSolver {
	Evaluator eval;
	public long visited = 0;
	public long maxLen = 0;

	public InformedSolver(Evaluator eval) {
		this.eval = eval;
	}

	public void solve(World world) {
		var root = new State(null, null, world);
		var goal = search(root);

		if (goal != null)
			printSolution(goal);

		System.out.printf("visited: %d, max length: %d\n", this.visited, this.maxLen);
	}

	public State search(State root) {
		var openList = toMutable(List.of(root));

		while (openList.isEmpty() == false) {
			var state = get(openList);
			this.visited += 1;

			if (state.isGoal())
				return state;

			var children = state.children();
			openList = concat(openList, children);

			sort(openList);

			this.maxLen = Math.max(this.maxLen, openList.size());
		}

		return null;
	}

	protected State get(List<State> list) {
		return list.remove(0);
	}

	protected List<State> concat(List<State> xs, List<State> ys) {
		return toMutable(Stream.concat(xs.stream(), ys.stream()).toList());
	}

	protected void sort(List<State> list) {
		list.sort(Comparator.comparing(s -> this.eval.f(s)));
	}

	protected List<State> toMutable(List<State> list) {
		return new ArrayList<State>(list);
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
