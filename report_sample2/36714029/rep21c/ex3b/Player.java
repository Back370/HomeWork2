package ex3b;

public abstract class Player {
String name;
Color color;

public Player(String name) {
this.name = name;
}

public String toString() {
return String.format("%s(%s)", this.name, this.color);
}

public Move think(State state) {
Move move = search(state);
move.color = this.color;
return move;
}

abstract Move search(State state);
}