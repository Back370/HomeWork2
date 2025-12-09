package ex3a;
import static java.lang.Float.*;
class MinMaxSearch {
    public static void main(String[] args) {
        var player = new MinMaxSearch(new Eval(), 2);
        var value = player.search(new State("A"));
        System.out.println(value);
    }

    Eval eval;
    int depthLimit;
    MinMaxSearch(Eval eval, int deapthLimit) {
        this.eval = eval;
        this.depthLimit = deapthLimit;
    }
    float search(State state) {
        return maxSearch(state, 0);
    }
    float maxSearch(State state, int depth) {
        if (isTerminal(state, depth))
            return this.eval.value(state);
        var v = NEGATIVE_INFINITY;
        for (var move: state.getMoves()) {
            var next = state.perform(move);
            var v0 = minSearch(next, depth + 1);
            v = Math.max(v, v0);
        }
        return v;
    }
    float minSearch(State state, int depth) {
        if (isTerminal(state, depth))
            return this.eval.value(state);
        var v = POSITIVE_INFINITY;
        for (var move: state.getMoves()) {
                var next = state.perform(move);
                var v0 = maxSearch(next, depth + 1);
                v = Math.min(v, v0);
        }
        return v;
    }

    boolean isTerminal(State state, int depth) {
        return state.isGoal() || depth >= this.depthLimit;
    }
}