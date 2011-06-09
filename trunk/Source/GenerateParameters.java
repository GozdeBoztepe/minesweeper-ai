// EnterISBNDialog.java

import javax.swing.*;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.*;

public class GenerateParameters extends JDialog
{
    private static final long serialVersionUID = 6001L;
    JTextField numRows;
    JTextField numCols;
    JTextField numMines;
    JTextField gameType;
		JButton okButton;
		JButton cancelButton;
		
	public GenerateParameters(JFrame parent)
	{
		super(parent, "Generator Parameters", true);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new FlowLayout());
		setSize(340, 120);

		JLabel label1 = new JLabel("Rows:");
    contentPane.add(label1);
    numRows = new JTextField("10");
    contentPane.add(numRows);
    
    JLabel label2 = new JLabel("Cols:");
    contentPane.add(label2);
    numCols = new JTextField("10");
    contentPane.add(numCols);
    
    JLabel label3 = new JLabel("Mines:");
    contentPane.add(label3);
    numMines = new JTextField("10");
    contentPane.add(numMines);
    
    JLabel label4 = new JLabel("Game Type:");
    contentPane.add(label4);
    gameType = new JTextField("2");
    contentPane.add(gameType);
    
    JLabel label5 = new JLabel("Game Type 3 has a set number of mines (18%)");
    contentPane.add(label5);

		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		contentPane.add(okButton);
		contentPane.add(cancelButton);

		setContentPane(contentPane);

		this.addWindowListener( new MyWindowAdapter() );

		ButtonListener buttonListener = new ButtonListener();
		cancelButton.addActionListener(buttonListener);
		okButton.addActionListener(buttonListener);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public int getNumRows()
	{
	    return Integer.parseInt(numRows.getText());
	}
	
	public int getNumCols()
	{
	    return Integer.parseInt(numCols.getText());
	}
	
	public int getNumMines()
	{
	    return Integer.parseInt(numMines.getText());
	}
	
	public int getGameType()
	{
	    return Integer.parseInt(gameType.getText());
	}

    // The following two classes are inner classes.

    // This class listens for window related events.  It extends
    // java.awt.event.WindowAdapter, which provides empty "stub"
    // implementations for each method in the interface
    // java.awt.event.WindowListener.
    class MyWindowAdapter extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
		    setVisible(false);
			dispose();
		}
	}

    // Since this class listens for events from two classes (buttons),
    // it uses the getSource() method to find out which button
    // sent the event message.
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object object = event.getSource();
			GenerateParameters.this.dispose();
		}
	}
}
