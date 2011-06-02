// MineFrame.java
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Iterator;

public class MineFrame extends JFrame
{
		public static final long serialVersionUID = 10001L;
		public Minesweeper minesweeper;
    private MinesweeperMenuBar minesweeperMenuBar;
    

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

    private void updateCaption()
    {
        setTitle("Minesweeper AI Project");
    }

/*	public void addNewCardholder()
	{
		JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(500, 300));
		final JTextField chName = new JTextField(30);
		JButton chAdd = new JButton("Add CardHolder");
		JButton chClear = new JButton("Clear");

		chAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
				if (chName.getText().length() > 0)
				{
					unsavedChanges = true;
					minesweeper.addCardholder(chName.getText());
				}
				else
					Toolkit.getDefaultToolkit().beep();  // signal an error
            }
        });

		chClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
				chName.setText("");
            }
        });


		contentPane.add(new JLabel("Enter CardHolder name (Last, First):"));
		contentPane.add(chName);
		contentPane.add(chAdd);
		contentPane.add(chClear);
		setContentPane(contentPane);
        pack();
		repaint();
	}

	public void selectCardholder()
	{
        // Create the Cardholder list
        DefaultListModel chListModel = new DefaultListModel();
		Iterator<Cardholder> itx = minesweeper.getChList().getCardholderList();
		while (itx.hasNext())
			chListModel.addElement(itx.next().toString());

        chList = new JList(chListModel);
        chList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chList.setSelectedIndex(0);

		JButton selectButton = new JButton("Select CardHolder");
		selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (chList.getFirstVisibleIndex() == -1)
                     return; // no item was selected or list is empty
                String name = (String)(chList.getSelectedValue());
				if (name == null)
				    return;
		        Iterator<Cardholder> it1 = minesweeper.getChList().getCardholderList();
		        while (it1.hasNext())
		        {
		            Cardholder ch = it1.next();
		            if (name.equals(ch.toString()))
			        {
			            currentCardholder = ch;
			            updateCaption();
			            return;
			        }
				}
            }
        });


		JScrollPane listScroller = new JScrollPane(chList);
		listScroller.setPreferredSize(new Dimension(250, 400));
		listScroller.setMinimumSize(new Dimension(250, 400));
		listScroller.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
		listPane.add(new JLabel("Current CardHolders:"));
		listPane.add(Box.createRigidArea(new Dimension(0, 5)));
		listPane.add(listScroller);
		listPane.add(Box.createRigidArea(new Dimension(0, 5)));
		listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(selectButton);

		JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(500, 300));
        contentPane.setLayout(new BorderLayout());
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		setContentPane(contentPane);

        pack();
		repaint();
	}*/
	
	public void generateGame()
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
		
		Generator g = new Generator(20,20,2);
		char[][] minefield = g.getMineField();
		
		int rows = minefield.length;
		int cols = minefield[0].length;
		
		JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(rows*16, cols*16));

		

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
}

class MinesweeperMenuBar extends JMenuBar
{
    private static final long serialVersionUID = 11001L;
    private MineFrame parentFrame;

    // constructor
    MinesweeperMenuBar(MineFrame pf)
    {
        parentFrame = pf;

        JMenu fileMenu, chMenu, bkMenu;
        JMenuItem menuItem;

        // Build the File menu.
        fileMenu = new JMenu("Actions");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        add(fileMenu);

        // attach to it a group of JMenuItems
        menuItem = new JMenuItem("Generate Game", KeyEvent.VK_G);
        menuItem.addActionListener(new GenerateGameListener());
        fileMenu.add(menuItem);

        fileMenu.addSeparator();
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new FileExitListener());
        fileMenu.add(menuItem);

    }

    // inner classes for handling menu selections
    class GenerateGameListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            parentFrame.generateGame();
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

    class CardholderAddNewListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
			//parentFrame.addNewCardholder();
        }
    }

    class CardholderSelectListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
		//	parentFrame.selectCardholder();
        }
    }

    class BooksListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
			String c = e.getActionCommand();
		/*	if (c.equals("ISBN"))
				parentFrame.selectByISBN();
			else if (c.equals("Author"))
				parentFrame.selectByAuthor();
			else if (c.equals("Title"))
				parentFrame.selectByTitle();
			else if (c.equals("Keywords"))
				parentFrame.selectByKeywords();*/
	    }
    }

}
