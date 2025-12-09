package ex3a;
import static java.lang.Float.*;
class AlphaBetaSearch {
    public static void main(String[] args) {
        var player = new AlphaBetaSearch(new Eval(), 2);
        var value = player.search(new State("A"));
        System.out.println(value);
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
            return this.eval.value(state);
        }
        var v = NEGATIVE_INFINITY;
        for (var move: state.getMoves()) {
            var next = state.perform(move);
            var v0 = minSearch(next, alpha, beta, depth + 1);
            v = Math.max(v, v0);
            if (beta <= v0) {
            break;
            }
            alpha = Math.max(alpha, v0);
        }
        return v;
    }
    float minSearch(State state, float alpha, float beta, int depth) {
        if (isTerminal(state, depth)) {
            return this.eval.value(state);
        }
        var v = POSITIVE_INFINITY;
        for (var move: state.getMoves()) {
            var next = state.perform(move);
            var v0 = maxSearch(next, alpha, beta, depth + 1);
            v = Math.min(v, v0);
            if (alpha >= v0) {
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
