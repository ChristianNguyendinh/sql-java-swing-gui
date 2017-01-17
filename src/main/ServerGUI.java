package main;

import main.ServerConnection;
import exceptions.NoDatabaseException;

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
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JTextField;
import javax.swing.JTextArea;

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
	
	//private static String[] columnNames = null;
	//private static Object[][] data = null;
	//private static int numRows = 0;
	//private static int numCols = 0;
	//private static String typesArrayString = null;
	
	// Array containing dataTable objects corresponding to each table in the DB
	private static DataTable[] tables = new DataTable[0];
	private JTextField textField_1;
	

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
					
					// Create the datatable(s) for display
					
					// Move this to a separate method, have the db panel call that method on submit
					// add param for the dB path to connect to. FULL PATH!!!!!!!
					// also have method reshow the custom query page
					tables = ServerConnection.accessDatabase();
					if (tables.length == 0) {
						throw new NoDatabaseException();
					}
					
					
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (NoDatabaseException ndbe) {
					System.err.println(ndbe.getMessage());
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
		gbl_panel.columnWidths = new int[]{0, 0, 475, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 4;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel.add(tabbedPane, gbc_tabbedPane);
		
		// Database Config Panel
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("DB", null, panel_3, null);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblTableName_1 = new JLabel("Table Name");
		GridBagConstraints gbc_lblTableName_1 = new GridBagConstraints();
		gbc_lblTableName_1.anchor = GridBagConstraints.WEST;
		gbc_lblTableName_1.insets = new Insets(5, 5, 5, 5);
		gbc_lblTableName_1.gridx = 1;
		gbc_lblTableName_1.gridy = 1;
		panel_3.add(lblTableName_1, gbc_lblTableName_1);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 10;
		gbc_textField_1.insets = new Insets(5, 5, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		panel_3.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JButton btnGo = new JButton("Go");
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.anchor = GridBagConstraints.WEST;
		gbc_btnGo.insets = new Insets(0, 0, 0, 5);
		gbc_btnGo.gridx = 1;
		gbc_btnGo.gridy = 10;
		panel_3.add(btnGo, gbc_btnGo);
		
		// Table(s) Panel -----------------------------------------
		
		for (int i = 0; i < tables.length; i++) {
			createTableTabs(tabbedPane, tables[i]);
		}
		
		JLabel lblRows = new JLabel("# Rows: ");
		GridBagConstraints gbc_lblRows = new GridBagConstraints();
		gbc_lblRows.anchor = GridBagConstraints.WEST;
		gbc_lblRows.insets = new Insets(0, 10, 3, 5);
		gbc_lblRows.gridx = 1;
		gbc_lblRows.gridy = 1;
		panel.add(lblRows, gbc_lblRows);
		// By default the tables wont be showing
		lblRows.setVisible(false);
		
		JLabel lblCols = new JLabel("# Cols: ");
		lblCols.setToolTipText("The types for each col are: ");
		GridBagConstraints gbc_lblCols = new GridBagConstraints();
		gbc_lblCols.anchor = GridBagConstraints.WEST;
		gbc_lblCols.insets = new Insets(0, 10, 3, 5);
		gbc_lblCols.gridx = 1;
		gbc_lblCols.gridy = 2;
		panel.add(lblCols, gbc_lblCols);
		// By default the tables wont be showing
		lblCols.setVisible(false);
		
		
		// Custom Query Panel ---------------------------------------
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Query", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		panel_2.setVisible(false);
		
		JLabel lblTableName = new JLabel("Table Name");
		GridBagConstraints gbc_lblTableName = new GridBagConstraints();
		gbc_lblTableName.anchor = GridBagConstraints.WEST;
		gbc_lblTableName.gridwidth = 3;
		gbc_lblTableName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTableName.gridx = 1;
		gbc_lblTableName.gridy = 1;
		panel_2.add(lblTableName, gbc_lblTableName);
		
		JTextField textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 5;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		panel_2.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblQuery = new JLabel("Query");
		GridBagConstraints gbc_lblQuery = new GridBagConstraints();
		gbc_lblQuery.anchor = GridBagConstraints.WEST;
		gbc_lblQuery.gridwidth = 2;
		gbc_lblQuery.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuery.gridx = 1;
		gbc_lblQuery.gridy = 4;
		panel_2.add(lblQuery, gbc_lblQuery);
		
		JTextArea textArea = new JTextArea();
		textArea.setTabSize(4);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridheight = 5;
		gbc_textArea.gridwidth = 13;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 5;
		panel_2.add(textArea, gbc_textArea);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(textField.getText().equals(""));
				System.out.println("=====================");
				System.out.println(textArea.getText());
			}
		});
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.insets = new Insets(0, 0, 0, 5);
		gbc_btnSubmit.gridx = 1;
		gbc_btnSubmit.gridy = 10;
		panel_2.add(btnSubmit, gbc_btnSubmit);
		
		
		// Event Handlers
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// May need to move to a range with multiple tables
				int index = tabbedPane.getSelectedIndex();
				// First and last indexes are not table tabs
				if (index != 0 && index != tabbedPane.getComponentCount() - 1) {
					lblRows.setVisible(true);
					// First tab is the DB tab, so table tabs will be ahead by 1
					lblRows.setText("# Rows: " + tables[index - 1].numRows);
					lblCols.setVisible(true);
					lblCols.setText("# Cols: " + tables[index - 1].numCols);
					lblCols.setToolTipText("The Types for each Column are: " + tables[index - 1].getTypesArrayString());
				} else {
					lblRows.setVisible(false);
					lblCols.setVisible(false);
				}
			}
		});
	}
	
	private void createTableTabs(JTabbedPane tabbedPane, DataTable dt) {
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab(dt.tableName, null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		JTable table = new JTable(dt.getData(), dt.getColumnNames());
		// FIX SORTING HERE
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
	}

}
