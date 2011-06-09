import org.shinkirou.minesweeper.Constraint;
import org.shinkirou.minesweeper.MapSquare;
import org.shinkirou.util.SetOperations;
import java.util.*;

/**
 * The class for the minesweeper solving algorithm.
 * @author SHiNKiROU
 */
public class SmartSolver {
	
	private byte[][] problem;
	private char[][] solvedMap;
	private int numRows;
	private int numCols;
	private Set<Constraint> sets;
	private int count;
	private boolean inspected = false;
	public static int U = 10;
	public Solver solver;

	/**
	 * Constructs an instance of <code>MinesweeperSolver</code>
	 * @param board The board to be solved.
	 */
	public SmartSolver(Solver s, int[][] mfield, char[][] sMap,int rows, int cols) {
		this.numRows = rows;
		this.numCols = cols;
		this.sets = new HashSet<Constraint>();
		this.count = 0;
		this.solvedMap = sMap;
		this.solver = s;
		
		int r = numRows;
		int c = numCols;
		
		problem = new byte[r][c];
		
		for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
            	byte x;
            	if (mfield[i][j] == 10) {
            		x = 10;
            	} else if (mfield[i][j] == 9) {
            		x = 9;
            	} else {
            		x = (byte) mfield[i][j];
            	}
            	problem[i][j] = x;
            }
        }		
	}
	
	public void inspect() {
		sets.clear();
		for (int y = 0, w = numCols, h = numRows; y < h; y ++) {
			for (int x = 0; x < w; x ++) {
				byte n = problem[x][y];
				if (n > 0 && n < 9) {
					Constraint e = new Constraint();
					// look around
					// top-left
					if (y - 1 > -1 && x - 1 > -1) {
						// get the known information of the square
						byte i = problem[x - 1][y - 1];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x - 1, y - 1));
						}
					}
					// top
					if (y - 1 > -1) {
						// get the known information of the square
						byte i = problem[x][y - 1];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x, y - 1));
						}
					}
					// top-right
					if (y - 1 > -1 && x + 1 < numCols) {
						// get the known information of the square
						byte i = problem[x + 1][y - 1];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x + 1, y - 1));
						}
					}
					// mid-left
					if (x - 1 > -1) {
						// get the known information of the square
						byte i = problem[x - 1][y];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x - 1, y));
						}
					}
					// mid-right
					if (x + 1 < numCols) {
						// get the known information of the square
						byte i = problem[x + 1][y];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x + 1, y));
						}
					}
					// lower-left
					if (y + 1 < numRows && x - 1 > -1) {
						// get the known information of the square
						byte i = problem[x - 1][y + 1];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x - 1, y + 1));
						}
					}
					// lower
					if (y + 1 < numRows) {
						// get the known information of the square
						byte i = problem[x][y + 1];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x, y + 1));
						}
					}
					// lower-right
					if (y + 1 < numRows && x + 1 < numCols) {
						// get the known information of the square
						byte i = problem[x + 1][y + 1];
						if (i == 9) {
							// if it's already marked: decrease the number
							n --;
						} else if (i == 10) {
							// if it's unknown: add the coordinate to the set
							e.add(new MapSquare(x + 1, y + 1));
						}
					}
					e.setMines(n);
					if ( ! e.isEmpty()) {
						sets.add(e);
					}
				}
			}
		}

		// 2: keep using the subset rule until exhausted
		boolean changed = true;
		while (changed) {
			changed = false;
			Constraint[] list = {};
			list = sets.toArray(list);
			// using Iterators causes ConcurrentModificationException
			for (Constraint e1 : list) {
				for (Constraint e2 : list) {
					if (!e1.equals(e2)) {
						// if e1 proper subset e2, c = e2 diff e1,
						//    mines of c = mines of e2 - mines of e1
						if (SetOperations.properSubset(e1.getSet(), e2.getSet())) {
							Set<MapSquare> set = SetOperations.difference(e2, e1);
							Constraint c = new Constraint(
								(byte) (e2.getMines() - e1.getMines()),
								set);
							changed = changed || sets.add(c);
						}
					}
				}
			}
		}
		inspected = true;
	}

	/**
	 * Perform an iteration of the solving process.
	 */
	public void iteration() {
		count ++;

		// find all constraints
		if ( ! inspected) {
			inspect();
		}

		// mark or probe the squares
		for (Constraint e : sets) {
			byte m = e.getMines();
			if (m == 0) {
				// if there are 0 mines, all the squares are safe
				for (MapSquare c : e) {
					//board.probe(c.x, c.y);
					problem[c.x][c.y] = Byte.parseByte(solvedMap[c.x][c.y] + "");
					solver.uncover(c.x, c.y);
				}
			} else if (m == e.size()) {
				// if the no. of mines is same as no. of squares, all the
				// squares are mines
				for (MapSquare c : e) {
					problem[c.x][c.y] = 9;
					solver.flagMine(c.x, c.y);
				}
			}
		}
		
		int r = numRows;
		int c = numCols;
		int n = 0;
		
		for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
            	if(problem[i][j] == 9 || problem[i][j] == 10) {
            		n++;
            	}
            }
        }
			
		if (solver.minesList.size() == n) {
			for(int i = 0; i < r; i++) {
	            for(int j = 0; j < c; j++) {
	            	if(problem[i][j] == 10) {
	            		solver.flagMine(i, j);
	            	}
	            }
	        }
		}
		
		inspected = false;
	}

	public char[][] getMinefield() {
		int r = numRows;
		int c = numCols;
		
		char[][] solvedField = new char[r][c];
		
		for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
            	char x;
            	if (problem[i][j] == 10) {
            		x = '9';
            	} else if (problem[i][j] == 9) {
            		x = 'F';
            	} else {
            		x = Character.forDigit(problem[i][j],10);
            	}
            	solvedField[i][j] = x;
            	System.out.print(solvedField[i][j] + " ");
            }
            System.out.println();
        }
		
		return solvedField;
	}
	
	public int[][] getIntMinefield() {
		int r = numRows;
		int c = numCols;
		
		int[][] solvedField = new int[r][c];
		
		for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
            	int x = -1;
            	if (problem[i][j] == 10) {
            		x = U;
            	} else if (problem[i][j] == 9) {
            		x = 9;
            	} else {
            		x = (int) problem[i][j];
            	}
            	solvedField[i][j] = x;
            }
        }
		
		return solvedField;
	}

	public void update() {
		int r = numRows;
		int c = numCols;
		
		problem = new byte[r][c];
		
		for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
            	byte x;
            	if (solver.problem[i][j] == 10) {
            		x = 10;
            	} else if (solver.problem[i][j] == 9) {
            		x = 9;
            	} else {
            		x = (byte) solver.problem[i][j];
            	}
            	problem[i][j] = x;
            }
        }
		
	}
}
