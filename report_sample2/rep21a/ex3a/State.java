package ex3a;
import java.util.*;

class State {
    static Map<String, List<String>> childNodeLists = Map.of(
        "A", List.of("B", "C"),
        "B", List.of("D", "E"),
        "C", List.of("F", "G"));
    static Map<String, Float> values = Map.of(
        "D", 3.0f,
        "E", 2.0f,
        "F", 1.0f,
        "G", 4.0f
    );
    String current;
    State(String current) {
        this.current = current;
    }
    public String toString() {
        return this.current.toString(); 
    }
    boolean isGoal() {
        return getMoves().isEmpty();
    }
    List<String> getMoves() {
        return State.childNodeLists.getOrDefault(this.current, new ArrayList<>());
    }
    State perform(String move) {
        return new State(move);
    }
}
class Eval {
    float value(State state) {
        return State.values.getOrDefault(state.current, Float.NaN);
    }
}