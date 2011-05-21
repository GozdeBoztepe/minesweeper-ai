// **************************************
// Class: Solver.java
// Code author: Allen Luo, David Nguyen
// Last modified: 05/10/2011
// **************************************

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import jp.ac.kobe_u.cs.cream.*;



public class Solver {

	public char[][] minefield;
	private int numRows;
	private	int numCols;
	private boolean GAME_OVER;
	public ArrayList<Coordinate> minesList;
	public ArrayList<Coordinate> movesList = new ArrayList<Coordinate>();
	public ArrayList<Coordinate> flagList = new ArrayList<Coordinate>();
	private static Random randomNumberGenerator = new Random();

	Network net;
	IntDomain d;
	IntDomain d1;
	IntVariable[][] variableMap;
	IntVariable[][] valueMap;

	public Solver(char[][] mfield, int row, int col, ArrayList<Coordinate> mlist) {
		minefield = mfield;
		numRows = row;
		numCols = col;
		minesList = mlist;

		net = new Network();
		d = new IntDomain(0,1);
		d1 = new IntDomain(0,8);
		variableMap = new IntVariable[numRows][numCols];
		valueMap = new IntVariable[numRows][numCols];
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				variableMap[i][j] = new IntVariable(net, d1);
			}
		}
		
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				valueMap[i][j] = new IntVariable(net, d);
			}
		}
		
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if (i != 0 && i != numRows-1 && j != 0 && j != numCols-1) {
					variableMap[i][j+1].add(
							variableMap[i][j-1]).add(
									variableMap[i+1][j+1]).add(
											variableMap[i+1][j-1]).add(
													variableMap[i+1][j]).add(
															variableMap[i-1][j+1]).add(
																	variableMap[i-1][j-1]).add(
																			variableMap[i-1][j]).equals(valueMap[i][j]);
				}
			}
		}
		
	}

	public void uncover(int row, int col) {
		Coordinate move = new Coordinate(row,col);
		if (!movesList.contains(move)) 
		{
			char value = minefield[row][col];
			movesList.add(move);
			if (value == 'X') {
				System.out.println("Selected (" + row + ", " + col + "): " + value);
				printCurrentMap();
				GAME_OVER = true;
				System.out.println("GAME OVER");
				System.exit(0);
			} 
			else if (value == '0') {
				if (IsIndexValid(row, col+1)) {
					//System.out.println("recurse right");
					uncover(row, col+1); // right
				}
				if (IsIndexValid(row, col-1)) {
					//System.out.println("recurse left");
					uncover(row,col-1); // left
				}
				if (IsIndexValid(row+1, col+1)) {
					//System.out.println("recurse down right");
					uncover(row+1, col+1); // down right
				}
				if (IsIndexValid(row+1, col-1)) {
					//System.out.println("recurse down left");
					uncover(row+1, col-1); // down left
				}
				if (IsIndexValid(row+1, col)) {
					//System.out.println("recurse down");
					uncover(row+1, col); // down	
				}
				if (IsIndexValid(row-1, col+1)) {
					//System.out.println("recurse up right");
					uncover(row-1, col+1); // up right
				}
				if (IsIndexValid(row-1, col-1)) {
					//System.out.println("recurse up left");
					uncover(row-1, col-1); // up left
				}
				if (IsIndexValid(row-1, col)) {
					//System.out.println("recurse up");
					uncover(row-1, col); // up
				}
			}
			System.out.println("Selected (" + row + ", " + col + "): " + value);
			printCurrentMap();
		}
	} // end of uncover

	public void flagMine(int row, int col) {
		Coordinate flag = new Coordinate(row,col);
		if (!flagList.contains(flag)) {
			flagList.add(flag);
			System.out.println("Flagged (" + row + ", " + col + ")");
			printCurrentMap();
		} else {
			flagList.remove(flag);
			System.out.println("Unflagged (" + row + ", " + col + ")");
			printCurrentMap();
		}

	}

	public void randomMove() {
		int x = GetRandomInt(0, numRows - 1);
		int y = GetRandomInt(0, numCols - 1);
		uncover(x,y);
	}

	public void solver() {
		int currentMoveIndex = 0;

		int row = -1;
		int col = -1;

		//randomMove();
		//while (movesList.size() > currentMoveIndex && GAME_OVER == false) {
		while (movesList.size() > currentMoveIndex) {
			Coordinate move = movesList.get(currentMoveIndex);
			row = move.getRow();
			col = move.getCol();
			if (minefield[row][col] != 'X' && row != 0 && row != numRows-1 && col != 0 && col != numCols-1) {
				int sum = minefield[row][col];
				variableMap[row][col].equals(0);
				valueMap[row][col].equals(sum);
			}
			currentMoveIndex++;
			//System.out.println("LOOP");
			//randomMove();
		}

		DefaultSolver solver = new DefaultSolver(net);
    	for (solver.start(); solver.waitNext(); solver.resume()) {
    		Solution solution = solver.getSolution();
    		for (int i = 0; i < numRows; i++) {
    			for (int j = 0; j < numCols; j++) {
    				System.out.print(solution.getIntValue(variableMap[i][j]) + " ");
    			}
    			System.out.println();	
    		}
    		System.out.println();
    	}
    	solver.stop();
  
    	
    	System.out.println(net.getVariables());
		System.out.println(net.getConstraints());
	}

	public void printCurrentMap()
	{
		System.out.println("Current Minesweeper Field");
		System.out.println("***************************");

		// print the mine field
		System.out.print("    ");
		for(int c=0; c < numCols; ++c)
			System.out.print("[" + c + "] ");
		System.out.println();

		for(int r=0; r < numRows; ++r)
		{
			System.out.print("[" + r + "]: ");
			for(int c=0; c < numCols; ++c)
			{ 
				if (movesList.contains(new Coordinate(r,c))) {
					System.out.print(minefield[r][c] + "   ");
				} else if (flagList.contains(new Coordinate(r,c))) {
					System.out.print("F   ");
				} else {
					System.out.print("    ");
				}
			}
			System.out.println("");
		}		
		System.out.println("");
	}

	public boolean IsIndexValid(int row, int col)
	{
		int maxRowIndex = numRows-1;	// base 0
		int maxColIndex = numCols-1;	// base 0

		if(((row >= 0) && (row <= maxRowIndex)) && ((col >=0) && (col <= maxColIndex)))
			return true;
		else
			return false;
	}

	//	private ArrayList<IntVariable> findAdjacentSquares(int row, int col) {
	//		ArrayList<IntVariable> adjacentSquares = new ArrayList<IntVariable>();
	//		if (IsIndexValid(row, col+1)) {
	//			adjacentSquares.add(variableMap[row][col+1]); // right
	//		}
	//		if (IsIndexValid(row, col-1)) {
	//			adjacentSquares.add(variableMap[row][col-1]); // left
	//		}
	//		if (IsIndexValid(row+1, col+1)) {
	//			adjacentSquares.add(variableMap[row+1][col+1]); // down right
	//		}
	//		if (IsIndexValid(row+1, col-1)) {
	//			adjacentSquares.add(variableMap[row+1][col-1]); // down left
	//		}
	//		if (IsIndexValid(row+1, col)) {
	//			adjacentSquares.add(variableMap[row+1][col]); // down	
	//		}
	//		if (IsIndexValid(row-1, col+1)) {
	//			adjacentSquares.add(variableMap[row-1][col+1]); // up right
	//		}
	//		if (IsIndexValid(row-1, col-1)) {
	//			adjacentSquares.add(variableMap[row-1][col-1]); // up left
	//		}
	//		if (IsIndexValid(row-1, col)) {
	//			adjacentSquares.add(variableMap[row-1][col]); // up
	//		}
	//		return adjacentSquares;
	//	}

	private int GetRandomInt(int start, int finish)
	{
		int n = finish - start + 1;
		int i = randomNumberGenerator.nextInt() % n;

		if (i < 0)
			i = -i;

		return start + i;
	}

	private void play() throws NumberFormatException, IOException {
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));

		while (!GAME_OVER) {
			System.out.println("Press '1' to uncover a square");
			System.out.println("Press '2' to flag a mine");
			System.out.println("Press '3' to uncover a random square");
			System.out.println("Press '4' to use the solver");
			System.out.print("Select Choice: ");
			int choice = Integer.parseInt(reader.readLine());

			int r;
			int c;

			if (choice == 1) {
				System.out.print("Select Row: ");
				r = Integer.parseInt(reader.readLine());
				System.out.print("Select Column: ");
				c = Integer.parseInt(reader.readLine());
				uncover(r,c);
			} else if (choice == 2){
				System.out.print("Select Row: ");
				r = Integer.parseInt(reader.readLine());
				System.out.print("Select Column: ");
				c = Integer.parseInt(reader.readLine());
				flagMine(r,c);
			} else if (choice == 3) {
				randomMove();
			} else {
				solver();
			}
		}
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		Generator g = new Generator(1);
		//Generator g = new Generator("sampleGame.txt");

		Solver s = new Solver(g.GetMineField(), g.GetNumRows(), g.GetNumCols(), g.GetMinesListOfCoordinates());
		g.PrintMineField();

		s.play();
	}
}
