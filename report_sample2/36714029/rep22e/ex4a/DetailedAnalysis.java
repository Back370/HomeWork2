package ex4a;
import java.util.*;
import java.io.*;

public class DetailedAnalysis {
	public static void main(String[] args) throws IOException {
		var analysis = new BackwardAnalysis();

		System.out.println("=== DETAILED TIC-TAC-TOE ANALYSIS ===\n");

		// Build state space with symmetry
		System.out.println("Building complete state space with symmetry consideration...");
		analysis.buildStateSpace(true);
		System.out.println("Total unique states: " + analysis.allStates.size());

		// Perform backward analysis
		System.out.println("\nPerforming backward analysis...");
		analysis.performBackwardAnalysis();

		// Display initial state analysis
		State initial = new State();
		String initialKey = analysis.getCanonicalKey(initial);
		BackwardAnalysis.Outcome initialOutcome = analysis.outcomes.get(initialKey);

		System.out.println("\n=== INITIAL STATE ANALYSIS ===");
		System.out.println("Initial empty board:");
		System.out.println(initial);
		System.out.println("\nOutcome for first player (BLACK): " + initialOutcome);

		// Analyze first moves
		System.out.println("\n=== FIRST MOVE ANALYSIS ===");
		System.out.println("Analyzing outcomes after BLACK's first move:\n");

		Map<Integer, BackwardAnalysis.Outcome> firstMoveOutcomes = new HashMap<>();
		for (Move move : initial.getMoves()) {
			State next = initial.perform(move);
			String nextKey = analysis.getCanonicalKey(next);
			BackwardAnalysis.Outcome outcome = analysis.outcomes.get(nextKey);
			firstMoveOutcomes.put(move.index, outcome);
		}

		// Categorize first moves by board position type
		System.out.println("Position types:");
		System.out.println("- Center (4): " + firstMoveOutcomes.get(4));
		System.out.println("- Corners (0,2,6,8): " + firstMoveOutcomes.get(0));
		System.out.println("- Edges (1,3,5,7): " + firstMoveOutcomes.get(1));

		// Count statistics
		System.out.println("\n=== STATISTICS ===");
		int winCount = 0, lossCount = 0, drawCount = 0;
		for (BackwardAnalysis.Outcome outcome : analysis.outcomes.values()) {
			switch (outcome) {
				case WIN: winCount++; break;
				case LOSS: lossCount++; break;
				case DRAW: drawCount++; break;
				case UNKNOWN: break;
			}
		}

		System.out.println("Total evaluated positions: " + (winCount + lossCount + drawCount));
		System.out.println("Winning positions: " + winCount + " (" + String.format("%.1f%%", 100.0 * winCount / (winCount + lossCount + drawCount)) + ")");
		System.out.println("Losing positions: " + lossCount + " (" + String.format("%.1f%%", 100.0 * lossCount / (winCount + lossCount + drawCount)) + ")");
		System.out.println("Draw positions: " + drawCount + " (" + String.format("%.1f%%", 100.0 * drawCount / (winCount + lossCount + drawCount)) + ")");

		// Show some example positions
		System.out.println("\n=== EXAMPLE WINNING POSITIONS ===");
		int exampleCount = 0;
		for (Map.Entry<String, State> entry : analysis.allStates.entrySet()) {
			if (exampleCount >= 3) break;
			State state = entry.getValue();
			BackwardAnalysis.Outcome outcome = analysis.outcomes.get(entry.getKey());
			if (outcome == BackwardAnalysis.Outcome.WIN && !state.isGoal()) {
				System.out.println("Example " + (exampleCount + 1) + ":");
				System.out.println(state);
				System.out.println("Current player: " + state.color + " (can force a win)\n");
				exampleCount++;
			}
		}
	}
}
