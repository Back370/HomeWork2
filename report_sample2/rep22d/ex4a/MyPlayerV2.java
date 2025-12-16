import static java.lang.Float.*;
import java.util.*;

public class MyPlayerV2 extends Player {
	Eval eval;
	int depthLimit;
	Move move;
	long visitedNodes = 0;

	public MyPlayerV2(Eval eval, int depthLimit) {
		super("MyPlayerV2_" + depthLimit);
		this.eval = eval;
		this.depthLimit = depthLimit;
	}

	Move search(State state) {
		visitedNodes = 0;
		negamax(state, NEGATIVE_INFINITY, POSITIVE_INFINITY, this.depthLimit, 1);
		System.out.println("Visited nodes (with Move Ordering): " + visitedNodes);
		return this.move;
	}

	float negamax(State state, float alpha, float beta, int depthLimit, int color) {
		visitedNodes++;

		if (isTerminal(state, depthLimit)) {
			return color * this.eval.value(state);
		}

		var v = NEGATIVE_INFINITY;
		var moves = orderMoves(state, color);

		for (var move: moves) {
			var next = state.perform(move);
			var v0 = -negamax(next, -beta, -alpha, depthLimit - 1, -color);

			if (depthLimit == this.depthLimit && v0 > v) {
				this.move = move;
			}

			if (beta <= v0) {
				return v0;
			}

			v = Math.max(v, v0);
			alpha = Math.max(alpha, v0);
		}

		return v;
	}

	List<Move> orderMoves(State state, int color) {
		var moves = state.getMoves();

		var scoredMoves = moves.stream()
			.map(m -> new ScoredMove(m, evaluateMove(state, m, color)))
			.sorted((a, b) -> Float.compare(b.score, a.score))
			.map(sm -> sm.move)
			.toList();

		return scoredMoves;
	}

	float evaluateMove(State state, Move move, int color) {
		var next = state.perform(move);
		return color * this.eval.value(next);
	}

	boolean isTerminal(State state, int depthLimit) {
		return state.isGoal() || depthLimit <= 0;
	}

	static class ScoredMove {
		Move move;
		float score;

		ScoredMove(Move move, float score) {
			this.move = move;
			this.score = score;
		}
	}
}
