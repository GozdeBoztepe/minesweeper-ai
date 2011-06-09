// **************************************
// Class: Generator.java
// Code author: David Nguyen
// Last modified: 05/24/2011
// **************************************

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class Generator 
{
	// default fields for default constructor
	static int DEFAULT_NUM_ROWS = 10;
	static int DEFAULT_NUM_COLS = 10;
	static int DEFAULT_NUM_MINES = 20;

	static int MIN_NUM_ROWS = 1;
	static int MAX_NUM_ROWS = 30;
	static int MIN_NUM_COLS = 1;
	static int MAX_NUM_COLS = 30;
	
	static double CLUSTERED_FIELD_MINE_RATIO = 0.20; 
	static double RANDOM_FIELD_MINE_RATIO = 0.20; 
	static double SCATTERED_FIELD_MINE_RATIO = 0.18; // must be <= 18% else hard mode hangs since it's possible to
										   		   // have n-1 mines on the board and not be able to add the final
										   		   // mine w/o ruining the hard property of a sparse mine field
	static final char mineChar = 'X'; 

	private BufferedReader inputFile;
	private static Random randomNumberGenerator = new Random();
	private ArrayList<Coordinate> minesList;
	private int numRows;
	private	int numCols;
	private int numMines;
	private	char[][] mineField;

	// default mine configuration constructor
	public Generator()
	{
		if(numMines > (numRows*numCols))
		{
			System.out.println("Illegal # of mines specified.");
			System.exit(1);
		}

		this.numRows = DEFAULT_NUM_ROWS;
		this.numCols = DEFAULT_NUM_COLS;
		this.numMines = DEFAULT_NUM_MINES;
		minesList = new ArrayList<Coordinate>();
		mineField = new char[numRows][numCols];	

		if(-1 == FillMineFieldRandomly())
		{
			System.out.println("Error in FillMineField");
			System.exit(1);
		}
	}

	public Generator(int gameMode)
	{
		this.numRows = getRandomInt(MIN_NUM_ROWS, MAX_NUM_ROWS);
		this.numCols = getRandomInt(MIN_NUM_COLS, MAX_NUM_COLS);
		minesList = new ArrayList<Coordinate>();
		mineField = new char[numRows][numCols];	

		if(gameMode == 1)
		{
			this.numMines = (int)(numRows*numCols*(CLUSTERED_FIELD_MINE_RATIO));
			if(-1 == FillMineFieldWithClusters())
			{
				System.out.println("Error in FillMineFieldWithClusters");
				System.exit(1);
			}		
		}
		else if (gameMode == 2)
		{
			this.numMines = (int)(numRows*numCols*(RANDOM_FIELD_MINE_RATIO));
			if(-1 == FillMineFieldRandomly())
			{
				System.out.println("Error in FillMineFieldRandomly");
				System.exit(1);
			}			
		}
		else if(gameMode == 3)
		{	
			this.numMines = (int)(numRows*numCols*(SCATTERED_FIELD_MINE_RATIO));
			if(-1 == FillMineFieldWithScatters())
			{
				System.out.println("Error in FillMineFieldWithScatters");
				System.exit(1);
			}		
		}
	}	

	// read mine configuration from file constructor
	public Generator(String fileName)
	{
		try
		{
			inputFile = new BufferedReader(new FileReader(fileName));
		}
		catch (IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}  

		if(-1 == FillMineFieldFromFile())
		{
			System.out.println("Error in FillMineFieldFromFile");
			System.exit(1);
		}
	}

	public Generator(int numRows, int numCols, int gameMode)
	{
		this.numRows = numRows;
		this.numCols = numCols;
		
		minesList = new ArrayList<Coordinate>();
		mineField = new char[numRows][numCols];	

		if(gameMode == 1)
		{
			this.numMines = (int)(numRows*numCols*(CLUSTERED_FIELD_MINE_RATIO));
			if(-1 == FillMineFieldWithClusters())
			{
				System.out.println("Error in FillMineFieldWithClusters");
				System.exit(1);
			}		
		}
		else if (gameMode == 2)
		{
			this.numMines = (int)(numRows*numCols*(RANDOM_FIELD_MINE_RATIO));
			if(-1 == FillMineFieldRandomly())
			{
				System.out.println("Error in FillMineFieldRandomly");
				System.exit(1);
			}			
		}
		else if(gameMode == 3)
		{	
			this.numMines = (int)(numRows*numCols*(SCATTERED_FIELD_MINE_RATIO));
			if(-1 == FillMineFieldWithScatters())
			{
				System.out.println("Error in FillMineFieldWithScatters");
				System.exit(1);
			}		
		}		
	}
	
	public Generator(int numRows, int numCols, int numMines, int gameMode)
	{
		this.numRows = numRows;
		this.numCols = numCols;
		
		minesList = new ArrayList<Coordinate>();
		mineField = new char[numRows][numCols];	

		if(gameMode == 1)
		{
			this.numMines = numMines;
			if(-1 == FillMineFieldWithClusters())
			{
				System.out.println("Error in FillMineFieldWithClusters");
				System.exit(1);
			}		
		}
		else if (gameMode == 2)
		{
			this.numMines = numMines;
			if(-1 == FillMineFieldRandomly())
			{
				System.out.println("Error in FillMineFieldRandomly");
				System.exit(1);
			}			
		}		
		else if(gameMode == 3)
		{	
			this.numMines = (int)(numRows*numCols*(SCATTERED_FIELD_MINE_RATIO));
			if(-1 == FillMineFieldWithScatters())
			{
				System.out.println("Error in FillMineFieldWithScatters");
				System.exit(1);
			}		
		}	
	}	
	
	public int FillMineFieldWithClusters()
	{
		// if there are no rows and no cols
		if((0 == numRows) && (0 == numCols))
			return -1;

		for(int r=0; r < numRows; ++r)
		{
			for(int c=0; c < numCols; ++c)
			{ 
				mineField[r][c] = '0';
			}
		}		

		// add the mine indexes to the mine field
		int maxRowIndex = numRows-1;	// base 0
		int maxColIndex = numCols-1;	// base 0

		while(minesList.size() != numMines)
		{
			int r,c;
			Coordinate temp = new Coordinate(r = getRandomInt(0, maxRowIndex), c = getRandomInt(0, maxColIndex));

			if(!(minesList.contains(temp)))
			{
				minesList.add(temp);
				mineField[temp.getRow()][temp.getCol()] = getMineChar();

				if(minesList.size() == numMines)
					break;

				// index to the left
				if(true == isIndexValid(r, c-1))
				{
					Coordinate tempAdj = new Coordinate(r, c-1);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r][c-1] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}
				}
				// index to the left and up one
				if(true == isIndexValid(r-1, c-1))
				{
					Coordinate tempAdj = new Coordinate(r-1, c-1);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r-1][c-1] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}			
				}
				// index above
				if(true == isIndexValid(r-1, c))
				{
					Coordinate tempAdj = new Coordinate(r-1, c);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r-1][c] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}			
				}
				// index above and to the right one
				if(true == isIndexValid(r-1, c+1))
				{
					Coordinate tempAdj = new Coordinate(r-1, c+1);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r-1][c+1] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}			
				}
				// index to the right
				if(true == isIndexValid(r, c+1))
				{
					Coordinate tempAdj = new Coordinate(r, c+1);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r][c+1] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}			
				}
				// index to the right and down one
				if(true == isIndexValid(r+1, c+1))
				{
					Coordinate tempAdj = new Coordinate(r+1, c+1);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r+1][c+1] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}			
				}			
				// index below
				if(true == isIndexValid(r+1, c))
				{
					Coordinate tempAdj = new Coordinate(r+1, c);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r+1][c] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}					
				}				
				// index below and to the left one
				if(true == isIndexValid(r+1, c-1))
				{
					Coordinate tempAdj = new Coordinate(r+1, c-1);
					if(!(minesList.contains(tempAdj)))
					{
						minesList.add(tempAdj);
						mineField[r+1][c-1] = getMineChar();

						if(minesList.size() == numMines)
							break;						
					}			
				}					
			}
		}		

		// add the helper indexes to the mine field
		for(int r=0; r < numRows; ++r)
		{
			for(int c=0; c < numCols; ++c)
			{
				Coordinate temp = new Coordinate(r,c);
				if(minesList.contains(temp))
				{
					continue;
				}

				int numMinesSurrounding = 0;

				// index to the left
				if(true == isIndexValid(r, c-1))
				{
					if(mineChar == mineField[r][c-1])
						++numMinesSurrounding;
				}
				// index to the left and up one
				if(true == isIndexValid(r-1, c-1))
				{
					if(mineChar == mineField[r-1][c-1])
						++numMinesSurrounding;					
				}
				// index above
				if(true == isIndexValid(r-1, c))
				{
					if(mineChar == mineField[r-1][c])
						++numMinesSurrounding;					
				}
				// index above and to the right one
				if(true == isIndexValid(r-1, c+1))
				{
					if(mineChar == mineField[r-1][c+1])
						++numMinesSurrounding;					
				}
				// index to the right
				if(true == isIndexValid(r, c+1))
				{
					if(mineChar == mineField[r][c+1])
						++numMinesSurrounding;					
				}
				// index to the right and down one
				if(true == isIndexValid(r+1, c+1))
				{
					if(mineChar == mineField[r+1][c+1])
						++numMinesSurrounding;					
				}			
				// index below
				if(true == isIndexValid(r+1, c))
				{
					if(mineChar == mineField[r+1][c])
						++numMinesSurrounding;					
				}				
				// index below and to the left one
				if(true == isIndexValid(r+1, c-1))
				{
					if(mineChar == mineField[r+1][c-1])
						++numMinesSurrounding;					
				}				

				mineField[r][c] = Character.forDigit(numMinesSurrounding, 10);
			}
		}
		return 0;
	}			

	public int FillMineFieldRandomly()
	{
		// if there are no rows and no cols
		if((0 == numRows) && (0 == numCols))
			return -1;

		for(int r=0; r < numRows; ++r)
		{
			for(int c=0; c < numCols; ++c)
			{ 
				mineField[r][c] = '0';
			}
		}		

		// add the mine indexes to the mine field
		int maxRowIndex = numRows-1;	// base 0
		int maxColIndex = numCols-1;	// base 0

		while(minesList.size() != numMines)
		{
			Coordinate temp = new Coordinate(getRandomInt(0, maxRowIndex), getRandomInt(0, maxColIndex));
			if(!(minesList.contains(temp)))
			{
				minesList.add(temp);
				mineField[temp.getRow()][temp.getCol()] = getMineChar();
			}
		}		

		// add the helper indexes to the mine field
		for(int r=0; r < numRows; ++r)
		{
			for(int c=0; c < numCols; ++c)
			{
				Coordinate temp = new Coordinate(r,c);
				if(minesList.contains(temp))
				{
					continue;
				}

				int numMinesSurrounding = 0;

				// index to the left
				if(true == isIndexValid(r, c-1))
				{
					if(mineChar == mineField[r][c-1])
						++numMinesSurrounding;
				}
				// index to the left and up one
				if(true == isIndexValid(r-1, c-1))
				{
					if(mineChar == mineField[r-1][c-1])
						++numMinesSurrounding;					
				}
				// index above
				if(true == isIndexValid(r-1, c))
				{
					if(mineChar == mineField[r-1][c])
						++numMinesSurrounding;					
				}
				// index above and to the right one
				if(true == isIndexValid(r-1, c+1))
				{
					if(mineChar == mineField[r-1][c+1])
						++numMinesSurrounding;					
				}
				// index to the right
				if(true == isIndexValid(r, c+1))
				{
					if(mineChar == mineField[r][c+1])
						++numMinesSurrounding;					
				}
				// index to the right and down one
				if(true == isIndexValid(r+1, c+1))
				{
					if(mineChar == mineField[r+1][c+1])
						++numMinesSurrounding;					
				}			
				// index below
				if(true == isIndexValid(r+1, c))
				{
					if(mineChar == mineField[r+1][c])
						++numMinesSurrounding;					
				}				
				// index below and to the left one
				if(true == isIndexValid(r+1, c-1))
				{
					if(mineChar == mineField[r+1][c-1])
						++numMinesSurrounding;					
				}				

				mineField[r][c] = Character.forDigit(numMinesSurrounding, 10);
			}
		}
		return 0;
	}	

	public int FillMineFieldWithScatters()
	{
		// if there are no rows and no cols
		if((0 == numRows) && (0 == numCols))
			return -1;

		for(int r=0; r < numRows; ++r)
		{
			for(int c=0; c < numCols; ++c)
			{ 
				mineField[r][c] = '0';
			}
		}		

		// add the mine indexes to the mine field
		int maxRowIndex = numRows-1;	// base 0
		int maxColIndex = numCols-1;	// base 0

		while(minesList.size() != numMines)
		{
			int r,c;
			Coordinate temp = new Coordinate(r = getRandomInt(0, maxRowIndex), c = getRandomInt(0, maxColIndex));

			if(!(minesList.contains(temp)))
			{
				// index to the left
				if(true == isIndexValid(r, c-1))
				{
					if(mineChar == mineField[r][c-1])
						continue;
				}
				// index to the left and up one
				if(true == isIndexValid(r-1, c-1))
				{
					if(mineChar == mineField[r-1][c-1])
						continue;					
				}
				// index above
				if(true == isIndexValid(r-1, c))
				{
					if(mineChar == mineField[r-1][c])
						continue;			
				}
				// index above and to the right one
				if(true == isIndexValid(r-1, c+1))
				{
					if(mineChar == mineField[r-1][c+1])
						continue;					
				}
				// index to the right
				if(true == isIndexValid(r, c+1))
				{
					if(mineChar == mineField[r][c+1])
						continue;					
				}
				// index to the right and down one
				if(true == isIndexValid(r+1, c+1))
				{
					if(mineChar == mineField[r+1][c+1])
						continue;					
				}			
				// index below
				if(true == isIndexValid(r+1, c))
				{
					if(mineChar == mineField[r+1][c])
						continue;					
				}				
				// index below and to the left one
				if(true == isIndexValid(r+1, c-1))
				{
					if(mineChar == mineField[r+1][c-1])
						continue;					
				}					
				
				minesList.add(temp);
				mineField[temp.getRow()][temp.getCol()] = getMineChar();
			}
		}		

		// add the helper indexes to the mine field
		for(int r=0; r < numRows; ++r)
		{
			for(int c=0; c < numCols; ++c)
			{
				Coordinate temp = new Coordinate(r,c);
				if(minesList.contains(temp))
				{
					continue;
				}

				int numMinesSurrounding = 0;

				// index to the left
				if(true == isIndexValid(r, c-1))
				{
					if(mineChar == mineField[r][c-1])
						++numMinesSurrounding;
				}
				// index to the left and up one
				if(true == isIndexValid(r-1, c-1))
				{
					if(mineChar == mineField[r-1][c-1])
						++numMinesSurrounding;					
				}
				// index above
				if(true == isIndexValid(r-1, c))
				{
					if(mineChar == mineField[r-1][c])
						++numMinesSurrounding;					
				}
				// index above and to the right one
				if(true == isIndexValid(r-1, c+1))
				{
					if(mineChar == mineField[r-1][c+1])
						++numMinesSurrounding;					
				}
				// index to the right
				if(true == isIndexValid(r, c+1))
				{
					if(mineChar == mineField[r][c+1])
						++numMinesSurrounding;					
				}
				// index to the right and down one
				if(true == isIndexValid(r+1, c+1))
				{
					if(mineChar == mineField[r+1][c+1])
						++numMinesSurrounding;					
				}			
				// index below
				if(true == isIndexValid(r+1, c))
				{
					if(mineChar == mineField[r+1][c])
						++numMinesSurrounding;					
				}				
				// index below and to the left one
				if(true == isIndexValid(r+1, c-1))
				{
					if(mineChar == mineField[r+1][c-1])
						++numMinesSurrounding;					
				}				

				mineField[r][c] = Character.forDigit(numMinesSurrounding, 10);
			}
		}
		return 0;
	}		

	public int FillMineFieldFromFile()
	{
		if(numMines > (numRows*numCols))
		{
			System.out.println("Illegal # of mines specified.");
			System.exit(1);
		}

		int numMinesDetected = 0;
		String line;
		StringTokenizer st;

		// first line of input file should contain: rows, cols, mines
		line = readLine();
		st = new StringTokenizer(line, ",");
		numRows = stringToInt(st.nextToken().trim());
		numCols = stringToInt(st.nextToken().trim());
		numMines = stringToInt(st.nextToken().trim());        

		mineField = new char[numRows][numCols];
		minesList = new ArrayList<Coordinate>();

		for(int r=0; r < numRows; ++r)
		{	
			line = readLine();
			st = new StringTokenizer(line, ",");
			for(int c=0; c < numCols; ++c)
			{
				char temp = st.nextToken().trim().charAt(0);
				mineField[r][c] = temp;

				if(temp == getMineChar())
				{
					minesList.add(new Coordinate(r,c));
					++numMinesDetected;
				}
				else
				{
					mineField[r][c] = '0';
				}
			}
		}

		// add the helper indexes to the mine field
		for(int r=0; r < numRows; ++r)
		{
			for(int c=0; c < numCols; ++c)
			{
				Coordinate temp = new Coordinate(r,c);
				if(minesList.contains(temp))
				{
					continue;
				}

				int numMinesSurrounding = 0;

				// index to the left
				if(true == isIndexValid(r, c-1))
				{
					if(mineChar == mineField[r][c-1])
						++numMinesSurrounding;
				}
				// index to the left and up one
				if(true == isIndexValid(r-1, c-1))
				{
					if(mineChar == mineField[r-1][c-1])
						++numMinesSurrounding;					
				}
				// index above
				if(true == isIndexValid(r-1, c))
				{
					if(mineChar == mineField[r-1][c])
						++numMinesSurrounding;					
				}
				// index above and to the right one
				if(true == isIndexValid(r-1, c+1))
				{
					if(mineChar == mineField[r-1][c+1])
						++numMinesSurrounding;					
				}
				// index to the right
				if(true == isIndexValid(r, c+1))
				{
					if(mineChar == mineField[r][c+1])
						++numMinesSurrounding;					
				}
				// index to the right and down one
				if(true == isIndexValid(r+1, c+1))
				{
					if(mineChar == mineField[r+1][c+1])
						++numMinesSurrounding;					
				}			
				// index below
				if(true == isIndexValid(r+1, c))
				{
					if(mineChar == mineField[r+1][c])
						++numMinesSurrounding;					
				}				
				// index below and to the left one
				if(true == isIndexValid(r+1, c-1))
				{
					if(mineChar == mineField[r+1][c-1])
						++numMinesSurrounding;					
				}				

				mineField[r][c] = Character.forDigit(numMinesSurrounding, 10);
			}
		}        

		if(numMines != numMinesDetected)
			return -1;
		else
			return 0;
	}

	// private methods
	private int getRandomInt(int start, int finish)
	{
		int n = finish - start + 1;
		int i = randomNumberGenerator.nextInt() % n;

		if (i < 0)
			i = -i;

		return start + i;
	}	

	private String readLine()
	{
		KEEP_READING: while (true)
		{
			String line="";
			try
			{
				line = inputFile.readLine();
				if (line == null)
				{
					System.out.println("**Unexpected end of file**");
					System.exit(1);
				}
			}
			catch (IOException e)
			{
				System.out.println("**" + e);
				System.exit(1);
			}

			// use // for comments, skips line with comments
			if (line.startsWith("//"))
				continue KEEP_READING;

			return line;
		}
	}

	private int stringToInt(String s)
	{
		int temp;
		try 
		{ 
			temp = Integer.parseInt(s); 
		}
		catch (NumberFormatException e)
		{
			System.out.println("Trying to convert " + s + "to int " + e);
			return 0;
		}
		return temp;
	}	

	public char[][] getMineField()
	{
		return mineField;
	}

	public ArrayList<Coordinate> getMinesListOfCoordinates()
	{
		return minesList;
	}

	public char getMineChar()
	{
		return mineChar;
	}

	public int getNumRows()
	{
		return numRows;
	}

	public int getNumCols()
	{
		return numCols;
	}

	public int getNumMines()
	{
		return numMines;
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

	public void PrintMineIndexCoordinates()
	{
		// print the coordinates of the mine indexes
		for(int i=0; i < minesList.size(); ++i)
		{
			System.out.print(minesList.get(i));
		}
		System.out.println();		
	}

	public void PrintMineField()
	{
		System.out.println("Minesweeper Field Generator");
		System.out.println("Rows = " + numRows);
		System.out.println("Cols = " + numCols);
		System.out.println("Mines = " + numMines);
		System.out.println("***************************");

		if((numRows <= 9) && (numCols <=9))
		{
			System.out.print("    ");
			for(int c=0; c < numCols; ++c)
				System.out.print("[" + c + "] ");
			System.out.println();

			for(int r=0; r < numRows; ++r)
			{
				System.out.print("[" + r + "]: ");
				for(int c=0; c < numCols; ++c)
				{ 
					System.out.print(mineField[r][c] + "   ");
				}
				System.out.println("");
			}			
		}
		else if ((numRows > 9) && (numCols <=9))
		{
			System.out.print("     ");
			for(int c=0; c < numCols; ++c)
				System.out.print("[" + c + "] ");
			System.out.println();

			for(int r=0; r <= 9; ++r)
			{
				System.out.print("[ " + r + "]: ");
				for(int c=0; c < numCols; ++c)
				{ 
					System.out.print(mineField[r][c] + "   ");
				}
				System.out.println("");
			}	
			for(int r=10; r < numRows; ++r)
			{
				System.out.print("[" + r + "]: ");
				for(int c=0; c < numCols; ++c)
				{ 
					System.out.print(mineField[r][c] + "   ");
				}
				System.out.println("");
			}				
		}
		else if ((numRows <=9) && (numCols > 9))
		{
			System.out.print("    ");
			for(int c=0; c <= 9; ++c)
				System.out.print("[ " + c + "] ");
			for(int c=10; c < numCols; ++c)
				System.out.print("[" + c + "] ");
			System.out.println();

			for(int r=0; r < numRows; ++r)
			{
				System.out.print("[" + r + "]:  ");
				for(int c=0; c < numCols; ++c)
				{ 
					System.out.print(mineField[r][c] + "    ");
				}
				System.out.println("");
			}				
		}
		else if ((numRows > 9) && (numCols > 9))
		{
			System.out.print("     ");
			for(int c=0; c <= 9; ++c)
				System.out.print("[ " + c + "] ");
			for(int c=10; c < numCols; ++c)
				System.out.print("[" + c + "] ");
			System.out.println();			
			
			for(int r=0; r <= 9; ++r)
			{
				System.out.print("[ " + r + "]:  ");
				for(int c=0; c < numCols; ++c)
				{ 
					System.out.print(mineField[r][c] + "    ");
				}
				System.out.println("");
			}	
			for(int r=10; r < numRows; ++r)
			{
				System.out.print("[" + r + "]:  ");
				for(int c=0; c < numCols; ++c)
				{ 
					System.out.print(mineField[r][c] + "    ");
				}
				System.out.println("");
			}				
		}		
	}

	public static void main(String[] args) 
	{
		//Generator g = new Generator();
		//Generator g = new Generator("sampleGame.txt");
		
		//Generator g = new Generator(1);		// easy
		//Generator g = new Generator(2);		// medium
		//Generator g = new Generator(3);		// hard
		
		//Generator g = new Generator(8, 5, 2);
		
		Generator g = new Generator(5, 10, 20, 1);
		//Generator g = new Generator(5, 10, 12, 2);
		//Generator g = new Generator(5,10,50, 3);

		g.PrintMineField();			
	} // end main

} // end class "Generator"
