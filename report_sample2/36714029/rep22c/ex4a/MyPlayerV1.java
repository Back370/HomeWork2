package ex4a;

import static java.lang.Float.*;

public class MyPlayerV1 extends Player {
	Eval eval;
	int depthLimit;
	Move move;

	public MyPlayerV1(Eval eval, int depthLimit) {
		super("MyPlayerV1_" + depthLimit);
		this.eval = eval;
		this.depthLimit = depthLimit;
	}

	Move search(State state) {
		negamax(state, NEGATIVE_INFINITY, POSITIVE_INFINITY, this.depthLimit, 1);
		return this.move;
	}

	float negamax(State state, float alpha, float beta, int depthLimit, int color) {
		if (isTerminal(state, depthLimit)) {
			return color * this.eval.value(state);
		}

		var v = NEGATIVE_INFINITY;

		for (var move: state.getMoves()) {
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

	boolean isTerminal(State state, int depthLimit) {
		return state.isGoal() || depthLimit <= 0;
	}
}
