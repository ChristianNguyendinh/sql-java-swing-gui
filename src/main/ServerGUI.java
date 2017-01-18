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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.event.ChangeEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

/**
 * GUI interface using Java Swing that accesses a SQL DB (currently SQLite only),
 * and displays the data and info of each table, and allows the user to make queries
 * and see the effects.
 * @author christian
 */
public class ServerGUI {

	private JFrame frame;
	// Array containing dataTable objects corresponding to each table in the DB
	private static DataTable[] tables = new DataTable[0];
	private static JTable[] tableTabs = null;
	private JTabbedPane tabbedPane;	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// close connection on exit
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				ServerConnection.closeConnection();
			}
		});
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//String fullPath = "/Users/christian/Documents/workspace/SQLGUI/src/main/test.db";
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
	 * Connect to the SQLite DB file. Parameter fullPath must be the FULL
	 * path to the file. Loads the data from each table into a DataTable
	 * object, creates the tabs to display each table's data, then creates
	 * tab to allow custom queries.
	 * @param fullPath
	 */
	private void connectToDB(String fullPath) {
		try {
			ServerConnection.accessDatabase(fullPath);
			tables = ServerConnection.getTableInfo();
			
			if (tables.length == 0) {
				throw new NoDatabaseException();
			}
			
			tableTabs = new JTable[tables.length];
			// Create the table tab(s)
			for (int i = 0; i < tables.length; i++) {
				tableTabs[i] = createTableTabs(tables[i]);
			}
			// Create the custom query tab
			createQueryTab();
		} catch (NoDatabaseException ndbe) {
			System.err.println(ndbe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		
		// TabbedPane that will make up the applicaiton
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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
		
		JLabel lblTableName_1 = new JLabel("Database FULL Path");
		GridBagConstraints gbc_lblTableName_1 = new GridBagConstraints();
		gbc_lblTableName_1.anchor = GridBagConstraints.WEST;
		gbc_lblTableName_1.insets = new Insets(5, 5, 5, 5);
		gbc_lblTableName_1.gridx = 1;
		gbc_lblTableName_1.gridy = 1;
		panel_3.add(lblTableName_1, gbc_lblTableName_1);
		
		JTextField textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 10;
		gbc_textField_1.insets = new Insets(5, 5, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		panel_3.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JButton btnGo = new JButton("Go");
		btnGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Connect to the DB and create the table tabs
				if (tableTabs == null)
					connectToDB(textField_1.getText());
				else
					System.err.println("Already Connected to a Database!");
			}
		});
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.anchor = GridBagConstraints.WEST;
		gbc_btnGo.insets = new Insets(0, 0, 0, 5);
		gbc_btnGo.gridx = 1;
		gbc_btnGo.gridy = 10;
		panel_3.add(btnGo, gbc_btnGo);
		
		// Labels for the number of columns and rows for each table
		
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
		
		// Event Handlers
		
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int index = tabbedPane.getSelectedIndex();
				// First and last indexes are not table tabs
				if (index != 0 && index != tabbedPane.getComponentCount() - 1) {
					// Get the right values for the label depending on the table
					lblRows.setVisible(true);
					// First tab is the DB tab, so table tabs will be ahead by 1
					lblRows.setText("# Rows: " + tables[index - 1].numRows);
					lblCols.setVisible(true);
					lblCols.setText("# Cols: " + tables[index - 1].numCols);
					// Show the data type of each column as a tool tip
					lblCols.setToolTipText("The Types for each Column are: " + tables[index - 1].getTypesArrayString());
				} else {
					lblRows.setVisible(false);
					lblCols.setVisible(false);
				}
			}
		});
	}
	
	/**
	 * Create a tab that will display a table of data from a DataTable object,
	 * then add it to the tabbedPane
	 * @param dt
	 * @return
	 */
	private JTable createTableTabs(DataTable dt) {
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab(dt.tableName, null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		JTable table = new JTable();
		// Set the table model
		TableModel tm = new DefaultTableModel(dt.getData(), dt.getColumnNames()) {
			// Set the class of each column for sorting
			@Override
			public Class<?> getColumnClass(int column) {
				return dt.getData()[0][column].getClass();
			}
		};
		// Give integers and floating point values left alignment
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		
		table.setDefaultRenderer(Integer.class, leftRenderer);
		table.setDefaultRenderer(Double.class, leftRenderer);
		table.setModel(tm);
		// Create sorter
		table.setAutoCreateRowSorter(true);
		
		scrollPane.setViewportView(table);
		return table;
	}
	
	/**
	 * Create the custom query tab which will allow the user to make their own
	 * queries. Will be added to the main tabbedPane and should only be created
	 * after a database connection is sucessfully made
	 */
	private void createQueryTab() {
		// Create main layout
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Query", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{93, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblQuery = new JLabel("Query");
		GridBagConstraints gbc_lblQuery = new GridBagConstraints();
		gbc_lblQuery.anchor = GridBagConstraints.WEST;
		gbc_lblQuery.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuery.gridx = 0;
		gbc_lblQuery.gridy = 0;
		panel_2.add(lblQuery, gbc_lblQuery);
		
		// Create the scrollable text area for typing in a new custom query
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel_2.add(scrollPane, gbc_scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setTabSize(4);
		
		JLabel lblOutput = new JLabel("Output");
		GridBagConstraints gbc_lblOutput = new GridBagConstraints();
		gbc_lblOutput.anchor = GridBagConstraints.WEST;
		gbc_lblOutput.insets = new Insets(0, 0, 0, 0);
		gbc_lblOutput.gridx = 0;
		gbc_lblOutput.gridy = 3;
		panel_2.add(lblOutput, gbc_lblOutput);
		
		// Create the scrollable text area for any possible output
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 3;
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 0);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 4;
		panel_2.add(scrollPane_1, gbc_scrollPane_1);
		
		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);
		textArea_1.setTabSize(4);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea_1.setText("");
				if(textArea.getText().equals("")) {
					textArea_1.setText("Enter a query first...");
				} else {
					// Execute the SQL command and show the output
					textArea_1.setText(ServerConnection.customQuery(textArea.getText()));
					// Refresh the tables
					tables = ServerConnection.getTableInfo();
					for (int i = 0; i < tableTabs.length; i++) {
						TableModel tm = new DefaultTableModel(tables[i].getData(), tables[i].getColumnNames());
						tableTabs[i].setModel(tm);
					}
				}
			}
		});
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.gridx = 1;
		gbc_btnSubmit.gridy = 8;
		panel_2.add(btnSubmit, gbc_btnSubmit);
	}

}
