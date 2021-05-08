package ai.puzzle;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import sac.StateFunction;
import sac.examples.slidingpuzzle.HFunctionLinearConflicts;
import sac.graph.AStar;
import sac.graph.BestFirstSearch;
import sac.graph.GraphSearchAlgorithm;
import sac.graph.GraphSearchConfigurator;
import sac.graph.GraphState;
import sac.graph.GraphStateImpl;
import java.util.List;



public class Puzzle extends GraphStateImpl{
	
	public static final byte n1 = (byte) 3;
	public static final byte n2 = (byte) n1 * n1;
	public byte[][] board;
	public int zeroRow;
	public int zeroCol;
	
	Random r = new Random(0);
	
	public Puzzle(){
		board = new byte[n1][n1];
		byte count = 0;
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n1; j++) {
				board[i][j] = count;
				count++;
			}
		}
	}

	public Puzzle(Puzzle parent) {
		board = new byte[n1][n1];
		for (int i = 0; i < n1; i++)
			for (int j = 0; j < n1; j++)
				board[i][j] = parent.board[i][j];
		zeroRow = parent.zeroRow;
		zeroCol = parent.zeroCol;

	}
	
	private void zeros() {
		int row = findZeroRow();
		int col = findZeroCol();
		zeroRow = row;
		zeroCol = col;
	}
	
	public int findZeroRow() {
		int index = 0;
		for(int i = 0; i < n1; i++)
			for(int j = 0; j < n1; j++) {
				if(board[i][j] == 0)
					index = i;
			}
		return index;
	}
	
	public int findZeroCol() {
		int index = 0;
		for(int i = 0; i < n1; i++)
			for(int j = 0; j < n1; j++) {
				if(board[i][j] == 0)
					index = j;
		
			}
		return index;
	}
	
	public void shuffle(int times) {
		boolean move = true;
		for (int i = 0; i != times; i++) {
			int value = r.nextInt(4);
			if (value == 0)
				move = moveUp();
			else if (value == 1)
				move = moveRight();
			else if (value == 2)
				move = moveLeft();
			else if (value == 3)
				move = moveDown();
			if (move == false)
				i--;
		}

	}
		
		public boolean moveDown() {
			if (zeroRow < n1 - 1) {
				board[zeroRow][zeroCol] = board[zeroRow + 1][zeroCol];
				board[zeroRow + 1][zeroCol] = 0;
				zeroRow++;
				return true;
			} else
				return false;
		}

		public boolean moveUp() {
			if (zeroRow > 0) {
				board[zeroRow][zeroCol] = board[zeroRow - 1][zeroCol];
				board[zeroRow - 1][zeroCol] = 0;
				zeroRow--;
				return true;
			} else
				return false;
		}

		public boolean moveRight() {
			if (zeroCol < n1 - 1) {
				board[zeroRow][zeroCol] = board[zeroRow][zeroCol + 1];
				board[zeroRow][zeroCol + 1] = 0;
				zeroCol++;
				return true;
			} else
				return false;
		}
		
		public boolean moveLeft() {
			if (zeroCol > 0) {
				board[zeroRow][zeroCol] = board[zeroRow][zeroCol - 1];
				board[zeroRow][zeroCol - 1] = 0;
				zeroCol--;
				return true;
			} else
				return false;
		}
		
		public void toBoard(String s) {
			int k = 0;
			for (int i = 0; i < n1; i++) {
				for (int j = 0; j < n1; j++) {
					board[i][j] = Byte.valueOf(s.substring(k, k + 1));
					k++;
				}
			}
			zeros();
		}
		
		public String toString() {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < n1; i++) {
				for (int j = 0; j < n1; j++) {
					result.append(board[i][j] + ((j < n1 - 1) ? "," : ""));

				}
				result.append("\n");
			}
			return result.toString();
		}
		
		public static String randPuzzle () {
			String [] arr = new String[9];
			int number;
			for(int i = 0 ; i < 9; i++) {
				number = (int) (Math.random() * 9);
				for(int j = 0; j <= i; j++) {
					String a = String.valueOf(number);
					//String b = Integer.toString(number);
					//String c = number + " ";
					if(arr[j] == a) {
						number = (int) (Math.random() * 9);
						j = 0;
					}
				}
			}
			return arr.toString();
	
		}
		


		
		public static void main(String[] args) {
			String input = "823506417";
			//String input = randPuzzle();
			//input = randomPuzzle(input);
			//Puzzle.setHFunction(new Manhattan());
			Puzzle.setHFunction(new MisplacedTiles());
			Puzzle x = new Puzzle();
			//x.toBoard(input);
			System.out.println(x);
			x.shuffle(1000);
			System.out.println(x);
			GraphSearchAlgorithm algorithm = new AStar(x);
			GraphSearchConfigurator configurator = new GraphSearchConfigurator();
			configurator.setWantedNumberOfSolutions(1);
			algorithm.setConfigurator(configurator);
			algorithm.execute();
			List<GraphState> solutions = algorithm.getSolutions();
			for(GraphState sols : solutions) {
				System.out.println(sols);
				System.out.println("Path:");
				System.out.println(sols.getMovesAlongPath());
				System.out.println(sols.getMovesAlongPath().size());
			}
			System.out.println("Done");
			System.out.println("Time: " + algorithm.getDurationTime());
			System.out.println("Closed State: " + algorithm.getClosedStatesCount());
			System.out.println("Open State: " + algorithm.getOpenSet().size());
		}
		


		@Override
		public int hashCode() { // required by the Close set
			return toString().hashCode();
		}

		@Override
		public List<GraphState> generateChildren() {
			List<GraphState> list = new LinkedList<GraphState>();

			for (int i = 0; i != 4; i++) {
				if (i == 0) {
					Puzzle child = new Puzzle(this);
					if (child.moveUp()){
						list.add(child);
						child.setMoveName("U");
					}
						
				} else if (i == 1) {
					Puzzle child = new Puzzle(this);
					if (child.moveRight()){
						list.add(child);
						child.setMoveName("R");
					}
						

				} else if (i == 2) {
					Puzzle child = new Puzzle(this);
					if (child.moveLeft()){
						list.add(child);
						child.setMoveName("L");
					}
						
				} else if (i == 3) {
					Puzzle child = new Puzzle(this);
					if (child.moveDown()){
						list.add(child);
						child.setMoveName("D");
					}
						
				}

			}

			return list;
		}
		
		public boolean isCorrect() {
			byte correct = 0;
			for (byte i = (byte) 0; i != n1; i++) {
				for (byte j = (byte) 0; j != n1; j++) {
					if (board[i][j] != correct)
						return false;
					correct++;
				}
			}
			return true;
		}

		@Override
		public boolean isSolution() {
			return (isCorrect());
		}
		
		static {
			setHFunction(new HFunctionLinearConflicts());
		}

}
