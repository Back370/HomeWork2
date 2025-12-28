
public class Main {
	public static void main(String[] args) {
		var h = new MisCanHeuristic();

		var minCostSearch = new InformedSolver(Evaluator.minCost());
		minCostSearch.solve(new MisCanWorld(3, 3, 1));

		var bestFirstSearch = new InformedSolver(Evaluator.bestFirst(h));
		bestFirstSearch.solve(new MisCanWorld(3, 3, 1));

		var aStarSearch = new InformedSolver(Evaluator.aStar(h));
		aStarSearch.solve(new MisCanWorld(3, 3, 1));
	}
}
