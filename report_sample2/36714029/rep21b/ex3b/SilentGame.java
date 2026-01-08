package ex3b;

import static ex3b.Color.*;
import java.util.*;

public class SilentGame {
    State state;
    Map<Color, Player> players;

    public SilentGame(int numStones, Player black, Player white) {
        black.color = BLACK;
        white.color = WHITE;
        this.state = new State(numStones);
        this.players = Map.of(BLACK, black, WHITE, white);
    }

    Color play() {
        while (!this.state.isGoal()) {
            var player = this.players.get(this.state.color);
            var move = player.think(this.state.clone());
            this.state = this.state.perform(move);
        }
        return this.state.winner();
    }
}
