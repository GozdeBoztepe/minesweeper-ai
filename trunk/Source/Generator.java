// **************************************
// Class: Generator.java
// Code author: David Nguyen
// Last modified: 05/10/2011
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
	static int NUMROWS = 10;
	static int NUMCOLS = 10;
	static int NUMMINES = 10;
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
		
		this.numRows = NUMROWS;
		this.numCols = NUMCOLS;
		this.numMines = NUMMINES;
		minesList = new ArrayList<Coordinate>();
		mineField = new char[numRows][numCols];	
		
		if(-1 == FillMineField())
		{
			System.out.println("Error in FillMineField");
			System.exit(1);
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
	
	// random mine configuration constructor
	public Generator(int numRows, int numCols, int numMines)
	{
		if(numMines > (numRows*numCols))
		{
			System.out.println("Illegal # of mines specified.");
			System.exit(1);
		}
		
		this.numRows = numRows;
		this.numCols = numCols;
		this.numMines = numMines;
		minesList = new ArrayList<Coordinate>();
		mineField = new char[numRows][numCols];
		
		if(-1 == FillMineField())
		{
			System.out.println("Error in FillMineField");
			System.exit(1);
		}
	}
	
	public int FillMineField()
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
			Coordinate temp = new Coordinate(GetRandomInt(0, maxRowIndex), GetRandomInt(0, maxColIndex));
			if(!(minesList.contains(temp)))
			{
				minesList.add(temp);
				mineField[temp.getRow()][temp.getCol()] = GetMineChar();
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
				if(true == IsIndexValid(r, c-1))
				{
					if(mineChar == mineField[r][c-1])
						++numMinesSurrounding;
				}
				// index to the left and up one
				if(true == IsIndexValid(r-1, c-1))
				{
					if(mineChar == mineField[r-1][c-1])
						++numMinesSurrounding;					
				}
				// index above
				if(true == IsIndexValid(r-1, c))
				{
					if(mineChar == mineField[r-1][c])
						++numMinesSurrounding;					
				}
				// index above and to the right one
				if(true == IsIndexValid(r-1, c+1))
				{
					if(mineChar == mineField[r-1][c+1])
						++numMinesSurrounding;					
				}
				// index to the right
				if(true == IsIndexValid(r, c+1))
				{
					if(mineChar == mineField[r][c+1])
						++numMinesSurrounding;					
				}
				// index to the right and down one
				if(true == IsIndexValid(r+1, c+1))
				{
					if(mineChar == mineField[r+1][c+1])
						++numMinesSurrounding;					
				}			
				// index below
				if(true == IsIndexValid(r+1, c))
				{
					if(mineChar == mineField[r+1][c])
						++numMinesSurrounding;					
				}				
				// index below and to the left one
				if(true == IsIndexValid(r+1, c-1))
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
        		
        		if(temp == GetMineChar())
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
				if(true == IsIndexValid(r, c-1))
				{
					if(mineChar == mineField[r][c-1])
						++numMinesSurrounding;
				}
				// index to the left and up one
				if(true == IsIndexValid(r-1, c-1))
				{
					if(mineChar == mineField[r-1][c-1])
						++numMinesSurrounding;					
				}
				// index above
				if(true == IsIndexValid(r-1, c))
				{
					if(mineChar == mineField[r-1][c])
						++numMinesSurrounding;					
				}
				// index above and to the right one
				if(true == IsIndexValid(r-1, c+1))
				{
					if(mineChar == mineField[r-1][c+1])
						++numMinesSurrounding;					
				}
				// index to the right
				if(true == IsIndexValid(r, c+1))
				{
					if(mineChar == mineField[r][c+1])
						++numMinesSurrounding;					
				}
				// index to the right and down one
				if(true == IsIndexValid(r+1, c+1))
				{
					if(mineChar == mineField[r+1][c+1])
						++numMinesSurrounding;					
				}			
				// index below
				if(true == IsIndexValid(r+1, c))
				{
					if(mineChar == mineField[r+1][c])
						++numMinesSurrounding;					
				}				
				// index below and to the left one
				if(true == IsIndexValid(r+1, c-1))
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
	private int GetRandomInt(int start, int finish)
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
	
	public char[][] GetMineField()
	{
		return mineField;
	}
	
	public ArrayList<Coordinate> GetMinesListOfCoordinates()
	{
		return minesList;
	}
	
	public char GetMineChar()
	{
		return mineChar;
	}
	
	public int GetNumRows()
	{
		return numRows;
	}
	
	public int GetNumCols()
	{
		return numCols;
	}
	
	public int GetNumMines()
	{
		return numMines;
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
				System.out.print(mineField[r][c] + "   ");
			}
			System.out.println("");
		}			
	}

	public static void main(String[] args) 
	{
		//Generator g = new Generator();
		//Generator g = new Generator("sampleGame.txt");
		Generator g = new Generator(10, 6, 20);
		
		g.PrintMineField();			
	} // end main

} // end class "Generator"
