package ex3b;

import static java.lang.Float.*;

public class MyAlphaBetaPlayer extends Player {
    Eval eval;
    int depthLimit;
    Move move;
    int visitedNodes;
    int alphaCutCount;
    int betaCutCount;

    public MyAlphaBetaPlayer(Eval eval, int depthLimit) {
        super("MinMax" + depthLimit);
        this.eval = eval;
        this.depthLimit = depthLimit;
    }

    Move search(State state) {
        this.move = new Move(Math.min(3, state.numStones), state.color);
        this.visitedNodes = 0;
        this.alphaCutCount = 0;
        this.betaCutCount = 0;

        if (this.color == Color.BLACK) {
            maxSearch(state, NEGATIVE_INFINITY, POSITIVE_INFINITY, 0);
        } else {
            minSearch(state, NEGATIVE_INFINITY, POSITIVE_INFINITY, 0);
        }

        System.out.println("=== 探索統計情報 ===");
        System.out.println("訪問ノード数: " + this.visitedNodes);
        System.out.println("枝刈り回数: " + (this.alphaCutCount + this.betaCutCount));
        System.out.println("  αカット: " + this.alphaCutCount);
        System.out.println("  βカット: " + this.betaCutCount);
        System.out.println();

        return this.move;
    }

    float maxSearch(State state, float alpha, float beta,int depth) {
        this.visitedNodes++;
        if (isTerminal(state, depth)) {
            return this.eval.value(state);
        }
        float v = NEGATIVE_INFINITY;
        for (var move : state.getMoves()) {
            var next = state.perform(move);
            float v0 = minSearch(next, alpha, beta, depth + 1);
            if (v0 > v) {
                v = v0;
                if (depth == 0) {
                    this.move = move;
                }
            }
            if (beta <= v0) {
                this.betaCutCount++;
                System.out.println("  ".repeat(depth) + "[βカット] 枝刈り発生");
                System.out.println("  ".repeat(depth) + "  箇所: " + state);
                System.out.println("  ".repeat(depth) + "  理由: v0(" + v0 + ") >= β(" + beta + ")");
                break;
            }
            alpha = Math.max(alpha, v0);
        }
        return v;
    }

    float minSearch(State state, float alpha, float beta, int depth) {
        this.visitedNodes++;
        if (isTerminal(state, depth)) {
            return this.eval.value(state);
        }

        float v = POSITIVE_INFINITY;

        for (var move : state.getMoves()) {
            var next = state.perform(move);
            float v0 = maxSearch(next,alpha, beta, depth + 1);
            if (v0 < v) {
                v = v0;
                if (depth == 0) {
                    this.move = move;
                }
            }
            if (alpha >= v0) {
                this.alphaCutCount++;
                System.out.println("  ".repeat(depth) + "[αカット] 枝刈り発生");
                System.out.println("  ".repeat(depth) + "  箇所: " + state);
                System.out.println("  ".repeat(depth) + "  評価後: " + next);
                System.out.println("  ".repeat(depth) + "  理由: v0(" + v0 + ") <= α(" + alpha + ")");
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
