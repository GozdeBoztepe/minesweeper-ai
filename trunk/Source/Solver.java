

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


import JaCoP.constraints.Sum;
import JaCoP.constraints.XeqC;
import JaCoP.core.BooleanVar;
import JaCoP.core.IntVar;
import JaCoP.core.Store;
import JaCoP.search.DepthFirstSearch;
import JaCoP.search.IndomainMin;
import JaCoP.search.Search;
import JaCoP.search.SelectChoicePoint;
import JaCoP.search.SimpleMatrixSelect;
import JaCoP.search.SmallestDomain;


public class Solver {

	public char[][] minefield;
	private int numRows;
	private	int numCols;
	private boolean GAME_OVER;
	public ArrayList<Coordinate> minesList;
	public ArrayList<Coordinate> movesList = new ArrayList<Coordinate>();
	public ArrayList<Coordinate> flagList = new ArrayList<Coordinate>();
	private static Random randomNumberGenerator = new Random();
	
	// JaCoP
	Store store;
	public static int U = 10; // Unknown Square
    IntVar[][] map; // 0 through 8
    IntVar[][] mines; // MINE or NOT MINE
    int[][] problem = null;

	public Solver(char[][] mfield, int r, int c, ArrayList<Coordinate> mlist) {
		minefield = mfield;
		numRows = r;
		numCols = c;
		minesList = mlist;
		
		buildModel();
		printProblem();
	}
	
	public void buildModel() {
		int r = numRows;
		int c = numCols;
		problem = new int[r][c];
		
		for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
            	problem[i][j] = U;
            }
        }
		
		store = new Store();

        // Constraint Variables
        mines = new IntVar[r][c];
        map = new IntVar[r][c];
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                // 0: no mine, 1: mine
                mines[i][j] = new BooleanVar(store, "m(" + i + "," + j + ")");
                // mirrors the minefield matrix
                map[i][j] = new IntVar(store, "g_" + i + "_" + j, -1, 8);
            }
        }
        
        ArrayList<IntVar> mineSum = new ArrayList<IntVar>();
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
            	mineSum.add(mines[i][j]);
            }
        }
        IntVar mineTotal = new IntVar(store, "mineTotal", minesList.size(), minesList.size());
        store.impose(new Sum(mineSum, mineTotal));
	}

	public void propConstraints() {
		int r = numRows;
		int c = numCols;
		 // Add the constraints
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {

                // This is a known value of neighbors
                if (problem[i][j] > U) {

                    // mirroring the problem matrix.
                    store.impose(new XeqC(map[i][j], problem[i][j]));

                    // This could not be a mine.
                    store.impose(new XeqC(mines[i][j], 0));

                    ArrayList<IntVar> lst = new ArrayList<IntVar>();
                    for(int a = -1; a <= 1; a++) {
                        for(int b = -1; b <= 1; b++) {
                            if (i+a >= 0 && j+b >= 0 && i+a < r && j+b < c) {
                                lst.add(mines[i+a][j+b]);
                            }
                        }                        
                    }
                    store.impose(new Sum(lst, map[i][j]));
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
				if (isIndexValid(row, col+1)) {
					//System.out.println("recurse right");
					uncover(row, col+1); // right
				}
				if (isIndexValid(row, col-1)) {
					//System.out.println("recurse left");
					uncover(row,col-1); // left
				}
				if (isIndexValid(row+1, col+1)) {
					//System.out.println("recurse down right");
					uncover(row+1, col+1); // down right
				}
				if (isIndexValid(row+1, col-1)) {
					//System.out.println("recurse down left");
					uncover(row+1, col-1); // down left
				}
				if (isIndexValid(row+1, col)) {
					//System.out.println("recurse down");
					uncover(row+1, col); // down	
				}
				if (isIndexValid(row-1, col+1)) {
					//System.out.println("recurse up right");
					uncover(row-1, col+1); // up right
				}
				if (isIndexValid(row-1, col-1)) {
					//System.out.println("recurse up left");
					uncover(row-1, col-1); // up left
				}
				if (isIndexValid(row-1, col)) {
					//System.out.println("recurse up");
					uncover(row-1, col); // up
				}
			}
			problem[row][col] = Character.getNumericValue(value);
			propConstraints();
			System.out.println("Selected (" + row + ", " + col + "): " + value + "Problem: " + problem[row][col]);
			printCurrentMap();
			//printProblem();
		}
	} // end of uncover

	public void flagMine(int row, int col) {
		Coordinate flag = new Coordinate(row,col);
		if (!flagList.contains(flag)) {
			flagList.add(flag);
			problem[row][col] = 9;
			System.out.println("Flagged (" + row + ", " + col + ")");
			printCurrentMap();
		} else {
			//flagList.remove(flag);
			//problem[row][col] = U;
			//System.out.println("Unflagged (" + row + ", " + col + ")");
			printCurrentMap();
		}

	}

	public void randomMove() {
		int x = getRandomInt(0, numRows - 1);
		int y = getRandomInt(0, numCols - 1);
		uncover(x,y);
	}
	
	public void randomSolver() {
		int currentMoveIndex = 0;

		randomMove();
		while (movesList.size() > currentMoveIndex && GAME_OVER == false) {
			currentMoveIndex++;
			randomMove();
			System.out.println(currentMoveIndex);
		}  
	}

	public void bruteSolver(boolean recordSolutions) {
		
	    SelectChoicePoint select = new SimpleMatrixSelect (mines, new SmallestDomain(), new IndomainMin());
	    
	    Search search = new DepthFirstSearch();
	    //search.getSolutionListener().setSolutionLimit(1); // ??
	    search.getSolutionListener().searchAll(true);
	    search.getSolutionListener().recordSolutions(recordSolutions);        
	    
	    boolean result = search.labeling(store, select);
	    
	    int numSolutions = search.getSolutionListener().solutionsNo();
	    
	    if (result) {
	
	        if (numSolutions <= 20) {
	            search.printAllSolutions();
	        } else {
	            System.out.println("Too many solutions to print...");
	        }
	
	        if (numSolutions > 1)
	        	System.out.println("\nThe last solution:");
	        else
	        	System.out.println("\nThe solution:");
	        
	        for(int i = 0; i < numRows; i++) {
	            for(int j = 0; j < numCols; j++) {
	                System.out.print(mines[i][j].value() + " ");
	            }
	            System.out.println();
	        }
	
	        //System.out.println("numSolutions: " + numSolutions);
	
	    } else {
	
	        System.out.println("No solutions.");
	
	    } // end if result
	
	
	} // end search

	public void printCurrentMap() {
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
		
		printProblem();
	}
	
	public void printProblem() {
		System.out.println("Problem MATRIX");
		for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
            	System.out.print(problem[i][j] + " ");
            }
            System.out.println();
        }
	}
	
	public void printSolution() {
		int[][] solution = new int[numRows][numCols];
		for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
            	Coordinate x = new Coordinate(i,j);
            	if (minesList.contains(x)) {
            		solution[i][j] = 1;
            		System.out.print("1 ");
            	} else {
            		solution[i][j] = 0;
            		System.out.print("0 ");
            	}
            }
            System.out.println();
        }
		
		boolean correctSolution = true;
		
		for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
            	if (mines[i][j].value() != solution[i][j]) {
            		correctSolution = false;
            	}
            }
        }
		System.out.println("Solution: " + correctSolution);
	}

	public boolean isIndexValid(int row, int col)
	{
		int maxRowIndex = numRows-1;	// base 0
		int maxColIndex = numCols-1;	// base 0

		if(((row >= 0) && (row <= maxRowIndex)) && ((col >=0) && (col <= maxColIndex)))
			return true;
		else
			return false;
	}
	
	public boolean isSolved() {
		for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
            	if (problem[i][j] == U) {
            		return false;
            	}
            }
        }
		
		if (flagList.size() != minesList.size()) {
			return false;
		}
		
		for (Coordinate x : flagList) {
			if (!minesList.contains(x)) {
				return false;
			}
		}
		
		for (Coordinate x : minesList) {
			if (!flagList.contains(x)) {
				return false;
			}
		}
		
		return true;
	}

	private int getRandomInt(int start, int finish)
	{
		int n = finish - start + 1;
		int i = randomNumberGenerator.nextInt() % n;

		if (i < 0)
			i = -i;

		return start + i;
	}

	void play() throws NumberFormatException, IOException {
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));

		while (!GAME_OVER) {
			System.out.println("Press '1' to uncover a square");
			System.out.println("Press '2' to flag a mine");
			System.out.println("Press '3' to uncover a random square");
			System.out.println("Press '4' to use the random solver");
			System.out.println("Press '5' to print solution");
			System.out.println("Press '6' to use the smart solver");
			System.out.println("Press '7' to use the partial solver");
			System.out.println("Press '8' to use the brute force solver");
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
			} else if (choice == 4) {
				randomSolver();
			} else if (choice == 5) {
				printSolution();
			} else if (choice == 6) {
				SmartSolver s = new SmartSolver(this, problem, minefield, numRows, numCols);
				long T1, T2, T;
				T1 = System.currentTimeMillis();	
				int[][] prev = new int[numRows][numCols];
				s.iteration();
				int[][] cur = s.getIntMinefield();
				while (prev.equals(cur)) {
					prev = cur;
					s.update();
					s.iteration();
					cur = s.getIntMinefield();
				}
				T2 = System.currentTimeMillis();
				T = T2 - T1;
				System.out.println("\n\t*** Execution time = " + T + " ms");
				if (isSolved()) {
					System.out.println("Correct Solution!");
				} else {
					System.out.println("Incorrect or Incomplete Solution!");
				}
			} else if (choice == 7) {
				SmartSolver s = new SmartSolver(this, problem, minefield, numRows, numCols);
				s.iteration();
			} else {
				long T1, T2, T;
				T1 = System.currentTimeMillis();
				bruteSolver(true);
		        T2 = System.currentTimeMillis();
				T = T2 - T1;
				System.out.println("\n\t*** Execution time = " + T + " ms");
			}
		}
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		Generator g = new Generator(8,8,15,1);
		//Generator g = new Generator("sampleGame.txt");

		Solver s = new Solver(g.getMineField(), g.getNumRows(), g.getNumCols(), g.getMinesListOfCoordinates());
		g.PrintMineField();

		s.play();
	}
}
