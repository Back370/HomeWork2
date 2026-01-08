package ex3b;

import static ex3b.Color.*;
import java.util.*;

public class DepthComparison {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("MinMaxPlayer Depth Limit Comparison Experiment (N = 10)");
        System.out.println("============================================================");
        System.out.println();

        int numStones = 10;
        int numGames = 50; // Sufficient number of trials

        System.out.println("Experiment Settings:");
        System.out.println("  Number of stones: N = " + numStones);
        System.out.println("  Number of trials: " + numGames + " times");
        System.out.println("  Opponent: RandomPlayer");
        System.out.println();

        // Case when d=3
        System.out.println("[Experiment 1] MinMaxPlayer (d=3) vs RandomPlayer");
        System.out.println("------------------------------------------------------------");
        testDepth(3, numStones, numGames);
        System.out.println();

        // Case when d=unlimited
        System.out.println("[Experiment 2] MinMaxPlayer (d=unlimited) vs RandomPlayer");
        System.out.println("------------------------------------------------------------");
        testDepth(Integer.MAX_VALUE, numStones, numGames);
        System.out.println();

        // Direct confrontation
        System.out.println("[Experiment 3] MinMaxPlayer (d=3) vs MinMaxPlayer (d=unlimited)");
        System.out.println("------------------------------------------------------------");
        compareDirectly(3, Integer.MAX_VALUE, numStones, numGames);
    }

    static void testDepth(int depth, int numStones, int numGames) {
        int minmaxWins = 0;
        int randomWins = 0;
        int draws = 0;

        // When MinMax plays first
        for (int i = 0; i < numGames; i++) {
            var minmax = new MinMaxPlayer(new Eval(), depth);
            var random = new RandomPlayer();
            var game = new SilentGame(numStones, minmax, random);
            Color winner = game.play();

            if (winner == BLACK) minmaxWins++;
            else if (winner == WHITE) randomWins++;
            else draws++;
        }

        // When MinMax plays second
        for (int i = 0; i < numGames; i++) {
            var minmax = new MinMaxPlayer(new Eval(), depth);
            var random = new RandomPlayer();
            var game = new SilentGame(numStones, random, minmax);
            Color winner = game.play();

            if (winner == WHITE) minmaxWins++;
            else if (winner == BLACK) randomWins++;
            else draws++;
        }

        int totalGames = numGames * 2;
        double winRate = 100.0 * minmaxWins / totalGames;

        System.out.println("Results:");
        System.out.println("  MinMaxPlayer wins: " + minmaxWins + " times");
        System.out.println("  RandomPlayer wins: " + randomWins + " times");
        System.out.println("  Draws: " + draws + " times");
        System.out.println("  Win rate: " + String.format("%.1f%%", winRate));

        String depthStr = (depth == Integer.MAX_VALUE) ? "unlimited" : "d=" + depth;
        System.out.println();
        System.out.println("Evaluation: MinMaxPlayer (" + depthStr + ") achieved a "
            + String.format("%.1f%%", winRate) + " win rate against RandomPlayer.");
    }

    static void compareDirectly(int depth1, int depth2, int numStones, int numGames) {
        int wins1 = 0;
        int wins2 = 0;
        int draws = 0;

        // depth1が先手の場合
        for (int i = 0; i < numGames; i++) {
            var p1 = new MinMaxPlayer(new Eval(), depth1);
            var p2 = new MinMaxPlayer(new Eval(), depth2);
            var game = new SilentGame(numStones, p1, p2);
            Color winner = game.play();

            if (winner == BLACK) wins1++;
            else if (winner == WHITE) wins2++;
            else draws++;
        }

        // depth1が後手の場合
        for (int i = 0; i < numGames; i++) {
            var p1 = new MinMaxPlayer(new Eval(), depth1);
            var p2 = new MinMaxPlayer(new Eval(), depth2);
            var game = new SilentGame(numStones, p2, p1);
            Color winner = game.play();

            if (winner == WHITE) wins1++;
            else if (winner == BLACK) wins2++;
            else draws++;
        }

        int totalGames = numGames * 2;
        double winRate1 = 100.0 * wins1 / totalGames;
        double winRate2 = 100.0 * wins2 / totalGames;

        String depth1Str = (depth1 == Integer.MAX_VALUE) ? "無制限" : "d=" + depth1;
        String depth2Str = (depth2 == Integer.MAX_VALUE) ? "無制限" : "d=" + depth2;

        System.out.println("結果:");
        System.out.println("  MinMaxPlayer (" + depth1Str + ") 勝利: " + wins1 + " 回 (" + String.format("%.1f%%", winRate1) + ")");
        System.out.println("  MinMaxPlayer (" + depth2Str + ") 勝利: " + wins2 + " 回 (" + String.format("%.1f%%", winRate2) + ")");
        System.out.println("  引き分け: " + draws + " 回");
        System.out.println();

        if (wins1 > wins2) {
            System.out.println("評価: " + depth1Str + " の方が " + depth2Str + " よりも強いです。");
        } else if (wins2 > wins1) {
            System.out.println("評価: " + depth2Str + " の方が " + depth1Str + " よりも強いです。");
        } else {
            System.out.println("評価: 両者の強さはほぼ同等です。");
        }
    }
}
