package report_sample2.rep21b.ex3b;

public class Move {
int removal;
Color color;

public Move(int removal, Color color) {
this.removal = removal;
this.color = color;
}

public String toString() {
return String.format("took %d stone(s)", this.removal);
}
}
