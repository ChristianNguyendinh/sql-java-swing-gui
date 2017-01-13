package main;

import main.ServerConnection;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import javax.swing.JTabbedPane;
import java.awt.Insets;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerGUI {

	private JFrame frame;
	/* private static String[] columnNames = {"id", "name"};
	private static Object[][] data = {
			{new Integer(1), "me"}, 
			{new Integer(2), "you"},
			{new Integer(3), "fdsa"},
			{new Integer(4), "ff"},
			{new Integer(5), "dd"},
			{new Integer(6), "aa"},
			{new Integer(7), "ss"},
			{new Integer(8), "gg"},
			{new Integer(9), "rr"},
			{new Integer(10), "wwww"},
			{new Integer(11), "qq"}, 
			{new Integer(2), "you"},
			{new Integer(3), "fdsa"},
			{new Integer(4), "ff"},
			{new Integer(5), "dd"},
			{new Integer(6), "aa"},
			{new Integer(7), "ss"},
			{new Integer(8), "gg"},
			{new Integer(9), "rr"},
			{new Integer(10), "wwww"},
			{new Integer(11), "qq"}, 
			{new Integer(2), "you"},
			{new Integer(3), "fdsa"},
			{new Integer(4), "ff"},
			{new Integer(5), "dd"},
			{new Integer(6), "aa"},
			{new Integer(7), "ss"},
			{new Integer(8), "gg"},
			{new Integer(9), "rr"},
			{new Integer(10), "wwww"},
			{new Integer(11), "qq"},
	};*/
	
	private static String[] columnNames = null;
	private static Object[][] data = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Set up database. Test code. Move to DB panel after testing
					// parameters should be initially null, method will assign values to them
					// CURRENTLY ONLY WORKS WITH ONE TABLE
					//		- Have a separate method that returns the number of tables found
					//		- Then do this to get a dataset for each table
					//		- Then make a method HERE to add a table tab to the gui
					ServerConnection.accessDatabase(columnNames, data);
					
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 475, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 1;
		gbc_tabbedPane.gridy = 1;
		panel.add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("DB", null, panel_3, null);
		
		// Main Panel, Panel 1 -----------------------------------------
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Tables", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		JTable table = new JTable(data, columnNames);
		scrollPane.setViewportView(table);
		
		// Main Panel, Panel 2 ---------------------------------------
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Query", null, panel_2, null);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				data[0][0] = new Integer(50);
			}
		});
		panel_2.add(btnNewButton);
	}

}
