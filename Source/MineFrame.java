// MineFrame.java
//

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

public class MineFrame extends JFrame
{
	public static final long serialVersionUID = 10001L;
	public Minesweeper minesweeper;
    private MinesweeperMenuBar minesweeperMenuBar;
    private char[][] minefield;
    private Generator gen;
    private Solver sol;
    int flag = 0;
    
    public MineFrame(Minesweeper minesweeper)
    {
        super("Minesweeper AI Project");  // window title

		this.minesweeper = minesweeper;
		setSize(500, 600);

		// Make sure the window can be closed
        addWindowListener(new WindowAdapter() {   // anonymous inner class
            public void windowClosing(WindowEvent e) {
                exitSystem();
            }
        });
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Create menu bar and add it to the JFrame
        minesweeperMenuBar = new MinesweeperMenuBar(this);
        setJMenuBar(minesweeperMenuBar);

        // Add a content pane, which is empty for now
		JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(500, 400));
        setContentPane(contentPane);

		setSize(500, 400 + minesweeperMenuBar.getHeight());
		setLocationRelativeTo(null);
        setVisible(true);

    }

    public void updateCaption()
    {
        if(flag == 1)
        	setTitle("FLAG");
        else
        	setTitle("UNCOVER");
        
    }
	
	public void generateGame()
	{
		GenerateParameters p = new GenerateParameters(this);
		int rows = p.getNumRows();
		int cols = p.getNumCols();
		int mines = p.getNumMines();
		int gameType = p.getGameType();
		
		
		
		//cleanse input
		if (gameType < 1 || gameType > 3)
			gameType = 2;
		if (mines > rows*cols || mines < 0)
			mines = (int)(rows*cols*.25);
		
		if(gameType == 3)
			this.gen =  new Generator(rows,cols,gameType);
		else
			this.gen =  new Generator(rows,cols,mines,gameType);
		
		
		this.minefield = gen.getMineField();
		
		//create a solver
		sol = new Solver(minefield, rows, cols, gen.getMinesListOfCoordinates());
		
		drawGame();
	}
	
	public void solveGame(int type)
	{
		//1 = smart
		//2 = partial
		//3 = brute
		
		switch(type)
		{
		case 1:
			sol.useSmartSolver();
			this.minefield = sol.getProblemMinefield();
			drawGame();
			break;
		/*case 2:
			while(!sol.isSolved())
			{
				sol.randomMove();
				sol.useSmartSolver();
			}
			this.minefield = sol.getProblemMinefield();
			drawGame();
			break;
		case 3:
			break;*/
		default:
			
		}
		
		
	}
	
	public void randomMove()
	{
		
		sol.randomMove();
		this.minefield = sol.getProblemMinefield();
		drawGame();
	}
	
	public void bruteSolve()
	{
		sol.bruteSolver(true);
		this.minefield = sol.getProblemMinefield();
		drawGame();
		
	}
	/*
	public void cover()
	{
		for (char[] row : minefield)
	        Arrays.fill(row, ' ');
		
		drawGame();
	}*/
	
	public void uncover(int r, int c)
	{
		if(flag == 0)
		{
			sol.uncover(r, c);
			this.minefield = sol.getProblemMinefield();
			drawGame();
		}
		else
		{
			sol.flagMine(r, c);
			this.minefield = sol.getProblemMinefield();
			drawGame();
		}
	
	}

	public void drawGame()
	{
		ImageIcon blank = new ImageIcon("img/j0.gif");
		ImageIcon one = new ImageIcon("img/j1.gif");
		ImageIcon two = new ImageIcon("img/j2.gif");
		ImageIcon three = new ImageIcon("img/j3.gif");
		ImageIcon four = new ImageIcon("img/j4.gif");
		ImageIcon five = new ImageIcon("img/j5.gif");
		ImageIcon six = new ImageIcon("img/j6.gif");
		ImageIcon seven = new ImageIcon("img/j7.gif");
		ImageIcon eight = new ImageIcon("img/j8.gif");
		ImageIcon mine = new ImageIcon("img/j9.gif");
		ImageIcon unopened = new ImageIcon("img/j10.gif");
		ImageIcon flag = new ImageIcon("img/j11.gif");
		
		int rows = minefield.length;
		int cols = minefield[0].length;
		
		JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(cols*15,rows*15));

        

	    contentPane.setLayout(new GridLayout(rows, cols));
	    
	    
	    for(int r=0; r < rows; ++r)
			{
				for(int c=0; c < cols; ++c)
				{ 
					if(minefield[r][c] == '0')
						contentPane.add(new JLabel(blank));
					else if(minefield[r][c] == '1')
						contentPane.add(new JLabel(one));
					else if(minefield[r][c] == '2')
						contentPane.add(new JLabel(two));
					else if(minefield[r][c] == '3')
						contentPane.add(new JLabel(three));
					else if(minefield[r][c] == '4')
						contentPane.add(new JLabel(four));
					else if(minefield[r][c] == '5')
						contentPane.add(new JLabel(five));
					else if(minefield[r][c] == '6')
						contentPane.add(new JLabel(six));
					else if(minefield[r][c] == '7')
						contentPane.add(new JLabel(seven));
					else if(minefield[r][c] == '8')
						contentPane.add(new JLabel(eight));
					else if(minefield[r][c] == 'X')
						contentPane.add(new JLabel(mine));
					else if(minefield[r][c] == 'F')
					{
						JButton uButton = new JButton(flag);
						UncoverMineListener um = new UncoverMineListener();
						um.x = r;
						um.y = c;
						uButton.addActionListener(um);
						contentPane.add(uButton);
					}
					else if(minefield[r][c] == ' ')
					{
						JButton uButton = new JButton(unopened);
						UncoverMineListener um = new UncoverMineListener();
						um.x = r;
						um.y = c;
						uButton.addActionListener(um);
						contentPane.add(uButton);
					}
						
					//contentPane.add(new JLabel(unopened));
						//contentPane.add(new JButton(minefield[r][c] + " "));
				}
			}
		
		setContentPane(contentPane);
        pack();
		repaint();
	}

	public void exitSystem()
	{
		minesweeper.exitSystem();
	}

	class UncoverMineListener implements ActionListener
	{
		int x, y;
		
		public void actionPerformed(ActionEvent event)
		{
			
			JButton object = (JButton)event.getSource();

			int row = x;
			int col = y;
			uncover(row, col);
		}
	}

}



class MinesweeperMenuBar extends JMenuBar
{
    private static final long serialVersionUID = 11001L;
    private MineFrame parentFrame;

    // constructor
    MinesweeperMenuBar(MineFrame pf)
    {
        parentFrame = pf;

        JMenu fileMenu, solveMenu, playMenu;
        JMenuItem menuItem;

        // Build the File menu.
        fileMenu = new JMenu("Mine");
        fileMenu.setMnemonic(KeyEvent.VK_M);
        add(fileMenu);

        // attach to it a group of JMenuItems
        menuItem = new JMenuItem("Generate Game", KeyEvent.VK_G);
        menuItem.addActionListener(new GenerateGameListener());
        fileMenu.add(menuItem);

        fileMenu.addSeparator();
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new FileExitListener());
        fileMenu.add(menuItem);
        
        // Build the Solver menu.
        solveMenu = new JMenu("Solve");
        solveMenu.setMnemonic(KeyEvent.VK_S);
        add(solveMenu);

        // attach to it a group of JMenuItems
        menuItem = new JMenuItem("Random Move", KeyEvent.VK_R);
        menuItem.addActionListener(new RandomMoveListener());
        solveMenu.add(menuItem);
        
        // attach to it a group of JMenuItems
        menuItem = new JMenuItem("Smart Solve", KeyEvent.VK_S);
        menuItem.addActionListener(new SmartSolveOneListener());
        solveMenu.add(menuItem);
        
     // Build the Solver menu.
        playMenu = new JMenu("Play");
        playMenu.setMnemonic(KeyEvent.VK_P);
        add(playMenu);
        
        /*
        menuItem = new JMenuItem("Cover", KeyEvent.VK_C);
        menuItem.addActionListener(new CoverListener());
        playMenu.add(menuItem);*/
        
     // attach to it a group of JMenuItems
        menuItem = new JMenuItem("Uncover", KeyEvent.VK_U);
        menuItem.addActionListener(new SetUncoverListener());
        playMenu.add(menuItem);
        
        menuItem = new JMenuItem("Flag", KeyEvent.VK_F);
        menuItem.addActionListener(new SetFlagListener());
        playMenu.add(menuItem);
        
     /*
        
        menuItem = new JMenuItem("Brute Solve", KeyEvent.VK_B);
        menuItem.addActionListener(new BruteSolveListener());
        solveMenu.add(menuItem);*/
        
        /*menuItem = new JMenuItem("Smart Solve (All moves)", KeyEvent.VK_A);
        menuItem.addActionListener(new SmartSolveAllListener());
        solveMenu.add(menuItem);*/


    }

    // inner classes for handling menu selections
    class GenerateGameListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.generateGame();
        }
    }
    
    class SmartSolveOneListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.solveGame(1);
        }
    }
    
    class RandomMoveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.randomMove();
        }
    }
    
    class SmartSolveAllListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.solveGame(2);
        }
    }
    /*
    class CoverListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.cover();
        }
    }*/
    
    class SetUncoverListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.flag = 0;
            parentFrame.updateCaption();
        }
    }
    
    class SetFlagListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.flag = 1;
            parentFrame.updateCaption();
        }
    }
    
    
    /*class PartialSolveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.solveGame(2);
        }
    }*/
    
    class BruteSolveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.bruteSolve();
        }
    }

    class FileSaveListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
     //       parentFrame.saveData(false);
        }
    }

    
    class FileSaveAsListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
       //     parentFrame.saveData(true);
        }
    }

    class FileExitListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.exitSystem();
        }
    }
    
    

}
