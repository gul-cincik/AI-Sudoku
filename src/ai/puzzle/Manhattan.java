package ai.puzzle;

import sac.State;
import sac.StateFunction;

public class Manhattan extends StateFunction {
	public double calculate(State state) {
			Puzzle puzzle = (Puzzle) state;
			int sum = 0;
			for(int i = 0; i < puzzle.board.length; i++)
				for(int j = 0; j < puzzle.board.length; j++)
					if(puzzle.board[i][j] != 0)
						sum += Math.abs(i - puzzle.board[i][j] / puzzle.board.length) + Math.abs(j - puzzle.board[i][j] % puzzle.board.length);
			return sum;		
		}

}
