package ex3a;
import java.util.*;

class State {
    static Map<String, List<String>> childNodeLists = Map.of(
        "Root", List.of("L1", "L2", "L3"),
        "L1", List.of("LL1", "LL2", "LL3"),
        "L2", List.of("ML1", "ML2", "ML3"),
        "L3", List.of("RL1", "RL2"));
    static Map<String, Float> values = Map.of(
        "LL1", -1.0f,
        "LL2", -31.0f,
        "LL3", -16.0f,
        "ML1", -38.0f,
        "ML2", -23.0f,
        "ML3", -50.0f,
        "RL1", -9.0f,
        "RL2", 6.0f
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