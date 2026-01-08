package ex3b;

public class Eval {
    float value(State state) {
    float s = state.winner().getSign(); // 勝者の色に応じて +1, -1, 0 を取得
    return state.isGoal() ? Float.POSITIVE_INFINITY * s : s / state.numStones; // 引き分けは 0、勝者がいる場合は無限大、そうでなければ石の数で割った値
    }
}
