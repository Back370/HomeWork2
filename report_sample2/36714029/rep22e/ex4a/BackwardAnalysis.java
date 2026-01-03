import java.util.*;
import java.io.*;

public class BackwardAnalysis {
	// Outcome values
	enum Outcome { WIN, LOSS, DRAW, UNKNOWN }

	// Store all reachable states
	Map<String, State> allStates = new HashMap<>();
	Map<String, Outcome> outcomes = new HashMap<>();

	public static void main(String[] args) throws IOException {
		var analysis = new BackwardAnalysis();

		// 1. Build state space without symmetry
		System.out.println("Building state space without symmetry consideration...");
		analysis.buildStateSpace(false);
		int totalWithoutSymmetry = analysis.allStates.size();
		System.out.println("Total states (without symmetry): " + totalWithoutSymmetry);

		// 2. Build state space with symmetry
		System.out.println("\nBuilding state space with symmetry consideration...");
		analysis.allStates.clear();
		analysis.buildStateSpace(true);
		int totalWithSymmetry = analysis.allStates.size();
		System.out.println("Total states (with symmetry): " + totalWithSymmetry);

		// 3. Perform backward analysis
		System.out.println("\nPerforming backward analysis...");
		analysis.performBackwardAnalysis();

		// 4. Count and report results
		analysis.reportResults();

		// 5. Write results to file
		analysis.writeResultsToFile("analysis_results.txt", totalWithoutSymmetry, totalWithSymmetry);

		System.out.println("\nResults written to analysis_results.txt");
	}

	void buildStateSpace(boolean considerSymmetry) {
		allStates.clear();
		Queue<State> queue = new LinkedList<>();
		State initial = new State();

		String key = considerSymmetry ? getCanonicalKey(initial) : getStateKey(initial);
		allStates.put(key, initial);
		queue.add(initial);

		while (!queue.isEmpty()) {
			State current = queue.poll();

			if (current.isGoal()) {
				continue;
			}

			for (Move move : current.getMoves()) {
				State next = current.perform(move);
				String nextKey = considerSymmetry ? getCanonicalKey(next) : getStateKey(next);

				if (!allStates.containsKey(nextKey)) {
					allStates.put(nextKey, next);
					queue.add(next);
				}
			}
		}
	}

	String getStateKey(State state) {
		StringBuilder sb = new StringBuilder();
		for (Color c : state.board) {
			sb.append(c.getSign());
		}
		return sb.toString();
	}

	String getCanonicalKey(State state) {
		List<String> symmetries = getAllSymmetries(state);
		Collections.sort(symmetries);
		return symmetries.get(0);
	}

	List<String> getAllSymmetries(State state) {
		List<String> result = new ArrayList<>();

		// Original
		result.add(boardToString(state.board));

		// 90 degree rotation
		result.add(boardToString(rotate90(state.board)));

		// 180 degree rotation
		result.add(boardToString(rotate180(state.board)));

		// 270 degree rotation
		result.add(boardToString(rotate270(state.board)));

		// Horizontal flip
		result.add(boardToString(flipHorizontal(state.board)));

		// Vertical flip
		result.add(boardToString(flipVertical(state.board)));

		// Diagonal flip (main diagonal)
		result.add(boardToString(flipDiagonal1(state.board)));

		// Diagonal flip (anti-diagonal)
		result.add(boardToString(flipDiagonal2(state.board)));

		return result;
	}

	String boardToString(Color[] board) {
		StringBuilder sb = new StringBuilder();
		for (Color c : board) {
			sb.append(c.getSign());
		}
		return sb.toString();
	}

	Color[] rotate90(Color[] board) {
		Color[] result = new Color[9];
		// 0 1 2    6 3 0
		// 3 4 5 -> 7 4 1
		// 6 7 8    8 5 2
		result[0] = board[6];
		result[1] = board[3];
		result[2] = board[0];
		result[3] = board[7];
		result[4] = board[4];
		result[5] = board[1];
		result[6] = board[8];
		result[7] = board[5];
		result[8] = board[2];
		return result;
	}

	Color[] rotate180(Color[] board) {
		Color[] result = new Color[9];
		for (int i = 0; i < 9; i++) {
			result[i] = board[8 - i];
		}
		return result;
	}

	Color[] rotate270(Color[] board) {
		return rotate90(rotate180(board));
	}

	Color[] flipHorizontal(Color[] board) {
		Color[] result = new Color[9];
		// 0 1 2    2 1 0
		// 3 4 5 -> 5 4 3
		// 6 7 8    8 7 6
		result[0] = board[2];
		result[1] = board[1];
		result[2] = board[0];
		result[3] = board[5];
		result[4] = board[4];
		result[5] = board[3];
		result[6] = board[8];
		result[7] = board[7];
		result[8] = board[6];
		return result;
	}

	Color[] flipVertical(Color[] board) {
		Color[] result = new Color[9];
		// 0 1 2    6 7 8
		// 3 4 5 -> 3 4 5
		// 6 7 8    0 1 2
		result[0] = board[6];
		result[1] = board[7];
		result[2] = board[8];
		result[3] = board[3];
		result[4] = board[4];
		result[5] = board[5];
		result[6] = board[0];
		result[7] = board[1];
		result[8] = board[2];
		return result;
	}

	Color[] flipDiagonal1(Color[] board) {
		Color[] result = new Color[9];
		// 0 1 2    0 3 6
		// 3 4 5 -> 1 4 7
		// 6 7 8    2 5 8
		result[0] = board[0];
		result[1] = board[3];
		result[2] = board[6];
		result[3] = board[1];
		result[4] = board[4];
		result[5] = board[7];
		result[6] = board[2];
		result[7] = board[5];
		result[8] = board[8];
		return result;
	}

	Color[] flipDiagonal2(Color[] board) {
		Color[] result = new Color[9];
		// 0 1 2    8 5 2
		// 3 4 5 -> 7 4 1
		// 6 7 8    6 3 0
		result[0] = board[8];
		result[1] = board[5];
		result[2] = board[2];
		result[3] = board[7];
		result[4] = board[4];
		result[5] = board[1];
		result[6] = board[6];
		result[7] = board[3];
		result[8] = board[0];
		return result;
	}

	void performBackwardAnalysis() {
		outcomes.clear();
		Queue<String> queue = new LinkedList<>();

		// Initialize terminal states
		for (Map.Entry<String, State> entry : allStates.entrySet()) {
			String key = entry.getKey();
			State state = entry.getValue();

			if (state.isGoal()) {
				Color winner = state.winner();
				if (winner == Color.BLACK) {
					// BLACK won, so the previous player (WHITE) lost
					// Current turn is WHITE's (after BLACK placed winning move)
					outcomes.put(key, Outcome.LOSS);
				} else if (winner == Color.WHITE) {
					// WHITE won, so the previous player (BLACK) lost
					// Current turn is BLACK's (after WHITE placed winning move)
					outcomes.put(key, Outcome.LOSS);
				} else {
					// Draw
					outcomes.put(key, Outcome.DRAW);
				}
				queue.add(key);
			}
		}

		// Backward propagation
		while (!queue.isEmpty()) {
			String key = queue.poll();

			// Find parent states (states that can reach this state)
			for (Map.Entry<String, State> entry : allStates.entrySet()) {
				String parentKey = entry.getKey();
				State parent = entry.getValue();

				if (outcomes.containsKey(parentKey)) {
					continue; // Already evaluated
				}

				if (parent.isGoal()) {
					continue;
				}

				// Check if parent can reach current state
				boolean canReach = false;
				for (Move move : parent.getMoves()) {
					State next = parent.perform(move);
					String nextKey = getCanonicalKey(next);
					if (nextKey.equals(key)) {
						canReach = true;
						break;
					}
				}

				if (!canReach) {
					continue;
				}

				// Evaluate parent based on children
				Outcome parentOutcome = evaluateParent(parent);
				if (parentOutcome != Outcome.UNKNOWN) {
					outcomes.put(parentKey, parentOutcome);
					queue.add(parentKey);
				}
			}
		}
	}

	Outcome evaluateParent(State parent) {
		boolean hasWinningMove = false;
		boolean hasDrawMove = false;
		boolean allChildrenEvaluated = true;

		for (Move move : parent.getMoves()) {
			State child = parent.perform(move);
			String childKey = getCanonicalKey(child);

			if (!outcomes.containsKey(childKey)) {
				allChildrenEvaluated = false;
				continue;
			}

			Outcome childOutcome = outcomes.get(childKey);

			// From parent's perspective, child's LOSS is parent's WIN
			if (childOutcome == Outcome.LOSS) {
				hasWinningMove = true;
			} else if (childOutcome == Outcome.DRAW) {
				hasDrawMove = true;
			}
		}

		// If we can force opponent to lose, we win
		if (hasWinningMove) {
			return Outcome.WIN;
		}

		// If all children are evaluated and no winning move, but has draw move
		if (allChildrenEvaluated && hasDrawMove) {
			return Outcome.DRAW;
		}

		// If all children are evaluated and all are opponent's wins, we lose
		if (allChildrenEvaluated && !hasDrawMove) {
			return Outcome.LOSS;
		}

		return Outcome.UNKNOWN;
	}

	void reportResults() {
		State initial = new State();
		String initialKey = getCanonicalKey(initial);
		Outcome initialOutcome = outcomes.get(initialKey);

		System.out.println("\n=== BACKWARD ANALYSIS RESULTS ===");
		System.out.println("Initial state outcome for first player (BLACK): " + initialOutcome);

		int winCount = 0;
		int lossCount = 0;
		int drawCount = 0;
		int unknownCount = 0;

		for (Outcome outcome : outcomes.values()) {
			switch (outcome) {
				case WIN: winCount++; break;
				case LOSS: lossCount++; break;
				case DRAW: drawCount++; break;
				case UNKNOWN: unknownCount++; break;
			}
		}

		System.out.println("\nPosition counts:");
		System.out.println("Winning positions: " + winCount);
		System.out.println("Losing positions: " + lossCount);
		System.out.println("Draw positions: " + drawCount);
		System.out.println("Unknown positions: " + unknownCount);
	}

	void writeResultsToFile(String filename, int totalWithoutSymmetry, int totalWithSymmetry) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
			writer.println("=== TIC-TAC-TOE COMPLETE ANALYSIS RESULTS ===");
			writer.println();

			writer.println("1. STATE SPACE SIZE");
			writer.println("Total states (without symmetry consideration): " + totalWithoutSymmetry);
			writer.println("Total states (with symmetry consideration): " + totalWithSymmetry);
			writer.println();

			State initial = new State();
			String initialKey = getCanonicalKey(initial);
			Outcome initialOutcome = outcomes.get(initialKey);

			writer.println("2. INITIAL STATE OUTCOME");
			writer.println("First player (BLACK) outcome: " + initialOutcome);
			writer.println();

			int winCount = 0;
			int lossCount = 0;
			int drawCount = 0;
			int unknownCount = 0;

			for (Outcome outcome : outcomes.values()) {
				switch (outcome) {
					case WIN: winCount++; break;
					case LOSS: lossCount++; break;
					case DRAW: drawCount++; break;
					case UNKNOWN: unknownCount++; break;
				}
			}

			writer.println("3. POSITION COUNTS (from current player's perspective)");
			writer.println("Winning positions: " + winCount);
			writer.println("Losing positions: " + lossCount);
			writer.println("Draw positions: " + drawCount);
			writer.println();

			writer.println("=== EXPLANATION ===");
			writer.println("- Winning position: Current player can force a win with optimal play");
			writer.println("- Losing position: Current player will lose with opponent's optimal play");
			writer.println("- Draw position: Both players playing optimally leads to a draw");
		}
	}
}
