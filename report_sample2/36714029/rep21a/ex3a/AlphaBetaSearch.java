package ex3a;
import static java.lang.Float.*;
class AlphaBetaSearch {
    public static void main(String[] args) {
        var player = new AlphaBetaSearch(new Eval(), 3);
        var value = player.search(new State("Root"));
        System.out.println("AlphaBeta evaluation: " + value);
    }

    Eval eval;
    int depthLimit;
    AlphaBetaSearch(Eval eval, int deapthLimit) {
        this.eval = eval;
        this.depthLimit = deapthLimit;
    }

    float search(State state) {
        return maxSearch(state, NEGATIVE_INFINITY, POSITIVE_INFINITY, 0);
    }
    float maxSearch(State state, float alpha, float beta, int depth) {
        if (isTerminal(state, depth)) {
            float val = this.eval.value(state);
            System.out.println("  ".repeat(depth) + "Leaf " + state + " = " + val);
            return val;
        }
        var v = NEGATIVE_INFINITY;
        System.out.println("  ".repeat(depth) + "MAX at " + state + " [alpha=" + alpha + ", beta=" + beta + "]");
        for (var move: state.getMoves()) {
            var next = state.perform(move);
            var v0 = minSearch(next, alpha, beta, depth + 1);
            v = Math.max(v, v0);
            if (beta <= v0) {
                System.out.println("  ".repeat(depth) + "PRUNED (beta cutoff) at " + state);
                break;
            }
            alpha = Math.max(alpha, v0);
        }
        return v;
    }
    float minSearch(State state, float alpha, float beta, int depth) {
        if (isTerminal(state, depth)) {
            float val = this.eval.value(state);
            System.out.println("  ".repeat(depth) + "Leaf " + state + " = " + val);
            return val;
        }
        var v = POSITIVE_INFINITY;
        System.out.println("  ".repeat(depth) + "MIN at " + state + " [alpha=" + alpha + ", beta=" + beta + "]");
        for (var move: state.getMoves()) {
            var next = state.perform(move);
            var v0 = maxSearch(next, alpha, beta, depth + 1);
            v = Math.min(v, v0);
            if (alpha >= v0) {
                System.out.println("  ".repeat(depth) + "PRUNED (alpha cutoff) at " + state + " after evaluating " + next);
                break;
            }
            beta = Math.min(beta, v0);
        }
        return v;
    }
    boolean isTerminal(State state, int depth) {
        return state.isGoal() || depth >= this.depthLimit;
    }
}
