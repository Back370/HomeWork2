package ex4a;

import java.util.Scanner;

public class HumanPlayer extends Player {
	Scanner scanner;

	public HumanPlayer() {
		super("Human");
		this.scanner = new Scanner(System.in);
	}

	Move search(State state) {
		System.out.println("Current board:");
		printBoardWithCoordinates(state);

		while (true) {
			System.out.print("Enter position (row col): ");
			try {
				int row = scanner.nextInt();
				int col = scanner.nextInt();

				if (row < 0 || row >= State.SIZE || col < 0 || col >= State.SIZE) {
					System.out.println("Error: Coordinates must be between 0 and 2");
					continue;
				}

				int index = row * State.SIZE + col;

				if (state.board[index] != Color.NONE) {
					System.out.println("Error: That position is already occupied");
					continue;
				}

				return new Move(index, this.color);
			} catch (Exception e) {
				System.out.println("Error: Please enter numbers");
				scanner.nextLine();
			}
		}
	}

	private void printBoardWithCoordinates(State state) {
		System.out.println("  0 1 2");
		for (int row = 0; row < State.SIZE; row++) {
			System.out.print(row + " ");
			for (int col = 0; col < State.SIZE; col++) {
				int index = row * State.SIZE + col;
				System.out.print(state.board[index]);
				if (col < State.SIZE - 1) {
					System.out.print("|");
				}
			}
			System.out.println();
			if (row < State.SIZE - 1) {
				System.out.println("  -+-+-");
			}
		}
	}
}
