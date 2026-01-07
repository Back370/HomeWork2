package ex3b;

import static ex3b.Color.*;

import java.util.*;
import java.util.stream.*;

public class State implements Cloneable {
int numStones;
Color color = BLACK;
Move move;

public State(int numStones) {
this.numStones = numStones;
}

public State clone() {
State other = new State(this.numStones);
other.color = this.color;
other.move = this.move;
return other;
}

public String toString() {
return String.format("%2d", this.numStones);
}

public boolean isGoal() {
return this.numStones == 0;
}

public Color winner() {
return isGoal() ? this.color : NONE;
}

public List<Move> getMoves() {
var n = Math.min(3, this.numStones);
return IntStream.rangeClosed(1, n)
.mapToObj(i -> new Move(i, this.color))
.toList();
}

public State perform(Move move) {
        var next = clone();
        next.numStones -= move.removal;
        next.color = this.color.flipped();
        next.move = move;
        return next;
    }
}