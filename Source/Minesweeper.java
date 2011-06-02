// Lib.java -- ICS 52 -- Lib system
//test 3

class Minesweeper
{
	
	public static void main(String[] commandLineArguments)
	{
		String asciiFileName = null;
		if (commandLineArguments.length > 0)
			asciiFileName = commandLineArguments[0];
		
		Minesweeper minesweeper = new Minesweeper(asciiFileName);
		
		MineFrame mf = new MineFrame(minesweeper);
	}


	public Minesweeper(String fileToLoad)
	{
		
		
		
	}

	public void exitSystem()
	{
		// A good place to do clean-up, such as writing to files.
		System.exit(0);
	}
	
}
