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
		JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(500, 300));

    contentPane.setLayout(new GridLayout(7, 2));
    contentPane.add(new JButton("Button 1"));
    contentPane.add(new JButton("Button 2"));
    contentPane.add(new JButton("Button 3"));
    contentPane.add(new JButton("Button 4"));
    contentPane.add(new JButton("Button 5"));
    contentPane.add(new JButton("Button 6"));
    contentPane.add(new JButton("Button 7"));
    contentPane.add(new JButton("Button 8"));
    contentPane.add(new JButton("Button 1"));
    contentPane.add(new JButton("Button 2"));
    contentPane.add(new JButton("Button 3"));
    contentPane.add(new JButton("Button 4"));
    contentPane.add(new JButton("Button 5"));
    contentPane.add(new JButton("Button 6"));
    contentPane.add(new JButton("Button 7"));
    contentPane.add(new JButton("Button 8"));
    contentPane.add(new JButton("Button 1"));
    contentPane.add(new JButton("Button 2"));
    contentPane.add(new JButton("Button 3"));
    contentPane.add(new JButton("Button 4"));
    contentPane.add(new JButton("Button 5"));
    contentPane.add(new JButton("Button 6"));
    contentPane.add(new JButton("Button 7"));
    contentPane.add(new JButton("Button 8"));
		
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
        fileMenu = new JMenu("File");
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
