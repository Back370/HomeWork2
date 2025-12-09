package report_sample2.rep21b.ex3b;

import static java.lang.Float.*;

public class MinMaxPlayer extends Player {
Eval eval;
int depthLimit;
Move move;

public MinMaxPlayer(Eval eval, int depthLimit) {
super("MinMax" + depthLimit);
this.eval = eval;
this.depthLimit = depthLimit;
}

Move search(State state) {
this.move = new Move(Math.min(3, state.numStones), state.color);

if (this.color == Color.BLACK) {
maxSearch(state, 0);
} else {
minSearch(state, 0);
}

return this.move;
}

float maxSearch(State state, int depth) {
if (isTerminal(state, depth)) {
return this.eval.value(state);
}

var v = NEGATIVE_INFINITY;

for (var move : state.getMoves()) {
var next = state.perform(move);
var v0 = minSearch(next, depth + 1);

if (depth == 0 && v0 > v) {
this.move = move;
}

v = Math.max(v, v0);
}

return v;
}

float minSearch(State state, int depth) {
if (isTerminal(state, depth)) {
return this.eval.value(state);
}

var v = POSITIVE_INFINITY;

for (var move : state.getMoves()) {
var next = state.perform(move);
var v0 = maxSearch(next, depth + 1);

if (depth == 0 && v0 < v) {
this.move = move;
}

v = Math.min(v, v0);
}

return v;
}

boolean isTerminal(State state, int depth) {
return state.isGoal() || depth >= this.depthLimit;
}
}
