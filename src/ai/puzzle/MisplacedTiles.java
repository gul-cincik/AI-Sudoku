package ai.puzzle;

import sac.State;
import sac.StateFunction;

public class MisplacedTiles extends StateFunction {
	public double calculate(State state) {
		Puzzle puzzle = (Puzzle) state;
		int sum = 0;
		int k = 0;
		for(int i = 0; i < puzzle.board.length; i++)
			for(int j = 0; j < puzzle.board.length; j++) {
				if(puzzle.board[i][j] != k && puzzle.board[i][j] != 0)
					sum ++;
				k++;
			}
		return sum;	
	}

}
