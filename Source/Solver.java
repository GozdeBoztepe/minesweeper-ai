// **************************************
// Class: Solver.java
// Code author: Allen Luo, David Nguyen
// Last modified: 05/10/2011
// **************************************

import java.util.ArrayList;
import java.util.Random;

public class Solver {

	public char[][] minefield;
	private int numRows;
	private	int numCols;
	public ArrayList<Coordinate> minesList;
	public ArrayList<Coordinate> movesList = new ArrayList<Coordinate>();
	private static Random randomNumberGenerator = new Random();

	public Solver(char[][] mfield, int row, int col, ArrayList<Coordinate> mlist) {
		minefield = mfield;
		numRows = row;
		numCols = col;
		minesList = mlist;
	}

	public void uncover(int row, int col) {
		Coordinate move = new Coordinate(row,col);
		if (!movesList.contains(move)) 
		{
			char value = minefield[row][col];
			movesList.add(move);
			if (value == 'X') {
				System.out.println("Selected (" + row + ", " + col + "): " + value);
				System.out.println("GAME OVER");
				printCurrentMap();
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

	public void randomMove() {
		int x = GetRandomInt(0, numRows - 1);
		int y = GetRandomInt(0, numCols - 1);
		uncover(x,y);
	}

	public void solver() {

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
				} else {
					System.out.print("    ");
				}
			}
			System.out.println("");
		}			
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

	private int GetRandomInt(int start, int finish)
	{
		int n = finish - start + 1;
		int i = randomNumberGenerator.nextInt() % n;

		if (i < 0)
			i = -i;

		return start + i;
	}

	public static void main(String[] args) {
		//Generator g = new MineFieldGenerator(10,10,5);
		Generator g = new Generator("sampleGame.txt");

		Solver s = new Solver(g.GetMineField(), g.GetNumRows(), g.GetNumCols(), g.GetMinesListOfCoordinates());
		g.PrintMineField();
		s.randomMove();
		s.randomMove();
		s.randomMove();
		s.randomMove();
		s.randomMove();
	}
}
