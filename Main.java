import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import java.awt.Font;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.lang.reflect.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import java.awt.CardLayout;

//SE206 Assignment 2
//Author: Helen Zhao || UID:6913580 || UPI:hzha587
public class Main {
	private String liUsr = ""; //liUsr -> logged in username
	private boolean loggedIn = false;
	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTabbedPane tabbedPane;
	private String fileName = ".usernames.txt";
	JTable statsTab;
	private HashMap<String, String> hm = new HashMap<String, String>();
	//hm stores the PIDs of processes hashed to username

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		BackgroundTask bt = new BackgroundTask();
		bt.execute();
		//bt simply runs the initial greeting message
		initialiseFile();
		initialize();
	}

	private void initialiseFile() {
		//This function initialises the username file if it doesn't exist
		//Reference: http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void appendUsername(String name) {
		//This function appends a username to the username file
		//Reference: http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
		FileWriter fw;
		try {
			fw = new FileWriter(fileName, true);
			fw.write(name + "\n");
			fw.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} 
	}

	private boolean checkUsername(String name) {
		//This function checks if a username is in the username file and returns boolean found
		//as true if it is in the file and false if it isn't
		boolean found = false;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line = br.readLine();

			while (line != null) {
				if (line.equalsIgnoreCase(name)) {
					found = true;
				}
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return found;
	}

	private void createStatsFile(String name) {
		//This function creates a blank stats file for a new user
		//Stats file format = .UsernameStats.txt
		//Stats file will be saved to current directory
		File file = new File("." + name.toLowerCase() + "Stats.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("0:0");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateStats(boolean win) {
		//This function will update the stats file of a user after a game has been played
		//Stats files have format wins:times
		String userFile = "." + liUsr.toLowerCase() + "Stats.txt";
		
		File file = new File(userFile);
		try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
			String line = br.readLine();

			String[] split = line.split(":");
			//Split by delimiter and then increment
			int times = Integer.parseInt(split[1]) + 1;
			split[1] = "" + times;
			if (win) {
				int wins = Integer.parseInt(split[0]) + 1;
				split[0] = "" + wins;
			}
			
			String whole = split[0] + ":" + split[1];
			//Rewrite file with new values
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(whole);
			bw.close();
			fw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Initialise GUI
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 450, 0 };
		gridBagLayout.rowHeights = new int[] { 272, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JPanel mainScreen = new JPanel();
		GridBagConstraints gbc_mainScreen = new GridBagConstraints();
		gbc_mainScreen.insets = new Insets(0, 0, 5, 0);
		gbc_mainScreen.fill = GridBagConstraints.BOTH;
		gbc_mainScreen.gridx = 0;
		gbc_mainScreen.gridy = 0;
		frame.getContentPane().add(mainScreen, gbc_mainScreen);
		GridBagLayout gbl_mainScreen = new GridBagLayout();
		gbl_mainScreen.columnWidths = new int[] { 404, 0, 0 };
		gbl_mainScreen.rowHeights = new int[] { 24, 0, 0 };
		gbl_mainScreen.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_mainScreen.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		mainScreen.setLayout(gbl_mainScreen);

		JLabel label = new JLabel("Welcome to the Magic Voice Game!");
		label.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridwidth = 2;
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		mainScreen.add(label, gbc_label);

		// --------------------------------------------------Bottom Pane------------------------------------------

		JPanel bottomBar = new JPanel();
		bottomBar.setLayout(new BoxLayout(bottomBar, BoxLayout.Y_AXIS));
		GridBagConstraints gbc_bottomBar = new GridBagConstraints();
		gbc_bottomBar.insets = new Insets(0, 0, 5, 0);
		gbc_bottomBar.fill = GridBagConstraints.BOTH;
		gbc_bottomBar.gridx = 0;
		gbc_bottomBar.gridy = 1;
		frame.getContentPane().add(bottomBar, gbc_bottomBar);

		JLabel lblTodo = new JLabel("Welcome!");
		lblTodo.setAlignmentX(Component.CENTER_ALIGNMENT);
		bottomBar.add(lblTodo);
		JLabel lblTodo2 = new JLabel(" ");
		lblTodo.setAlignmentX(Component.CENTER_ALIGNMENT);
		bottomBar.add(lblTodo2);

		// ----------------------------------------------Tabbed pane initialise--------------------------------------

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 2;
		gbc_tabbedPane.insets = new Insets(0, 0, 0, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;

		mainScreen.add(tabbedPane, gbc_tabbedPane);

		// --------------------------------------------Login---------------------------------------------------------------------

		JPanel loginTab = new JPanel(new CardLayout());
		tabbedPane.addTab("Login", null, loginTab, null);

		// ---------------------------------------- Login when not logged in---------------------------------------------------------
		JPanel login1 = new JPanel();
		loginTab.add(login1);
		login1.setLayout(null);
		login1.setVisible(true);

		JLabel lblEnterAValid = new JLabel("Enter a valid username to log in.");
		lblEnterAValid.setBounds(12, 12, 416, 15);
		login1.add(lblEnterAValid);

		JLabel lblLoginUser = new JLabel("Login User:");
		lblLoginUser.setBounds(12, 37, 89, 15);
		login1.add(lblLoginUser);

		textField = new JTextField();
		textField.setBounds(107, 36, 321, 19);
		login1.add(textField);
		textField.setColumns(10);

		JButton btnRegister1 = new JButton("Register this name");
		btnRegister1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				appendUsername(textField.getText());
				createStatsFile(textField.getText());
				liUsr = textField.getText();
				loggedIn = true;
				tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Play"));
				lblTodo.setText("Welcome " + liUsr + ", let's play!");
				BackgroundTask bt = new BackgroundTask() {
					@Override
					protected Object doInBackground() throws Exception {
						String cmd = "echo \"Welcome " + liUsr + ", let's play!\" | festival --tts";
						bashCall(cmd);
						return null;
					}
				};
				bt.execute();
				lblTodo.setText("Successfully registered and logged in!");
				lblTodo2.setText("You can play now.");
			}
		});
		btnRegister1.setBounds(166, 143, 180, 25);
		login1.add(btnRegister1);
		btnRegister1.setVisible(false);

		JButton btnLogIn = new JButton("Log in!");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField.getText();
				if (name.equals("") || !isAlpha(name)) {
					lblTodo.setText("Sorry, this username is invalid.");
					lblTodo2.setText(" ");
				} else if (checkUsername(textField.getText())) {
					liUsr = textField.getText();
					loggedIn = true;
					tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Play"));
					lblTodo.setText("Welcome " + liUsr + ", let's play!");
					BackgroundTask bt = new BackgroundTask() {
						@Override
						protected Object doInBackground() throws Exception {
							String cmd = "echo \"Welcome " + liUsr + ", let's play!\" | festival --tts";
							bashCall(cmd);
							return null;
						}
					};
					bt.execute();
				} else {
					btnRegister1.setVisible(true);
					lblTodo.setText("Sorry, username does not exist.");
					lblTodo2.setText(" Please try again or register a this username.");
				}
			}
		});
		btnLogIn.setBounds(23, 143, 117, 25);
		login1.add(btnLogIn);
		loginTab.add(login1);

		// ---------------------------------------------Login when logged in--------------------------------------------------------
		JPanel login2 = new JPanel();
		login2.setLayout(null);
		login2.setVisible(false);

		JLabel lblAUserIs = new JLabel("A user is already logged in! Log out?");
		lblAUserIs.setBounds(22, 12, 281, 15);
		login2.add(lblAUserIs);

		JButton btnLogOut = new JButton("Log out");
		btnLogOut.setBounds(22, 163, 117, 25);
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				liUsr = "";
				loggedIn = false;
				tabbedPane.setSelectedIndex(-1);
				tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Login"));
			}
		});
		login2.add(btnLogOut);
		loginTab.add(login2);

		// --------------------------------------------------Register--------------------------------------------------------

		JPanel registerTab = new JPanel();
		tabbedPane.addTab("Register", null, registerTab, "Register a new player");
		registerTab.setLayout(null);

		JLabel lblRegisterANew = new JLabel("Register a new player.");
		lblRegisterANew.setBounds(12, 12, 174, 15);
		registerTab.add(lblRegisterANew);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(12, 35, 95, 15);
		registerTab.add(lblUsername);

		textField_1 = new JTextField();
		textField_1.setBounds(98, 36, 305, 17);
		registerTab.add(textField_1);
		textField_1.setColumns(10);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Login"));
			}
		});
		btnLogin.setBounds(166, 143, 117, 25);
		registerTab.add(btnLogin);
		btnLogin.setVisible(false);

		JButton btnRegister = new JButton("Register!");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField_1.getText();
				if (name.equals("")) {
					lblTodo.setText("Sorry, this username is invalid.");
				} else if (!isAlpha(name)) {
					lblTodo.setText("Sorry, usernames can only be alphabetical letters.");
				} else if (checkUsername(name)) {
					lblTodo.setText("Sorry, username already exists. Please choose another.");
				} else {
					lblTodo.setText("Successfully registered! Please log in.");
					appendUsername(name);
					createStatsFile(name);
					btnLogin.setVisible(true);
				}

			}

		});
		btnRegister.setBounds(23, 143, 117, 25);
		registerTab.add(btnRegister);

		// -----------------------------------------------------Play-------------------------------------------------
		JLayeredPane playTab = new JLayeredPane();
		tabbedPane.addTab("Play", null, playTab, "Play the game");
		playTab.setLayout(new CardLayout());

		// --------------------------------------------------Play while logged in--------------------------------------

		JPanel play1 = new JPanel();
		play1.setLayout(null);
		play1.setVisible(true);
		playTab.add(play1, 0);

		JLabel lblGuessANumber = new JLabel("Guess a number between 1 and 6: ");
		lblGuessANumber.setBounds(12, 62, 260, 15);
		play1.add(lblGuessANumber);

		JComboBox<Integer> comboBox = new JComboBox<Integer>();
		comboBox.setBounds(270, 57, 48, 24);
		comboBox.addItem(1);
		comboBox.addItem(2);
		comboBox.addItem(3);
		comboBox.addItem(4);
		comboBox.addItem(5);
		comboBox.addItem(6);
		play1.add(comboBox);

		JLabel lblLetsPlayThe = new JLabel("Let's play the game!");
		lblLetsPlayThe.setBounds(12, 45, 145, 15);
		play1.add(lblLetsPlayThe);

		JButton btnPlay = new JButton("Play!");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean win = false;
				int guess = (int) comboBox.getSelectedItem();
				if (guess == (1 + Math.round(Math.random() * 5))) {
					// Win game
					lblTodo.setText("Well done " + liUsr + ", you guessed correctly!");
					lblTodo2.setText("");
					win = true;
					BackgroundTask bt = new BackgroundTask() {
						@Override
						protected Object doInBackground() throws Exception {
							String cmd = "echo \"Well done " + liUsr + ", you guessed correctly!\" | festival --tts";
							bashCall(cmd);
							return null;
						}
					};
					bt.execute();
				} else {
					//Lose game
					lblTodo.setText("Sorry " + liUsr + ", you guessed wrong.");
					lblTodo2.setText("");
					BackgroundTask bt = new BackgroundTask() {
						@Override
						protected Object doInBackground() throws Exception {
							String cmd = "echo \"Sorry " + liUsr + ", you guessed wrong.\" | festival --tts";
							bashCall(cmd);
							return null;
						}
					};
					bt.execute();
				}
				updateStats(win);
			}
		});

		btnPlay.setBounds(12, 134, 117, 25);
		play1.add(btnPlay);

		// ------------------------------------------Play while not logged in----------------------------------

		JPanel play2 = new JPanel();
		play2.setLayout(null);
		play2.setVisible(true);
		playTab.add(play2);

		JLabel lblSorryYouMust = new JLabel("Sorry, you must be logged in to play.");
		lblSorryYouMust.setBounds(26, 12, 308, 15);
		play2.add(lblSorryYouMust);

		JLabel lblPleaseLogIn = new JLabel("Please log in or register as a new user.");
		lblPleaseLogIn.setBounds(26, 34, 349, 15);
		play2.add(lblPleaseLogIn);

		JButton btnLogIn2 = new JButton("Log in");
		btnLogIn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Login"));
			}
		});
		btnLogIn2.setBounds(28, 154, 117, 25);
		play2.add(btnLogIn2);

		JButton btnRegister2 = new JButton("Register");
		btnRegister2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Register"));
			}
		});
		btnRegister2.setBounds(189, 154, 117, 25);
		play2.add(btnRegister2);

		// ----------------------------------------------------Stats-------------------------------------------------

		makeTable();

		// ------------------------------------------ Change Tab Actions------------------------------------------
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// LOGIN
				if (tabbedPane.getSelectedIndex() == (tabbedPane.indexOfTab("Login"))) {
					textField.setText("");
					btnRegister1.setVisible(false);
					if (loggedIn == true) {
						login2.setVisible(true);
						login1.setVisible(false);
						lblTodo.setText("Only one user may be logged in at a time, sorry.");
						lblTodo2.setText(" ");
					} else {
						login1.setVisible(true);
						login2.setVisible(false);
						lblTodo.setText("Hi there! Please log in.");
						lblTodo2.setText(" ");
					}
					// PLAY
				} else if (tabbedPane.getSelectedIndex() == tabbedPane.indexOfTab("Play")) {
					if (loggedIn == true) {
						lblTodo.setText("Welcome " + liUsr + ", let's play!");
						lblTodo2.setText(" ");
						play1.setVisible(true);
						play2.setVisible(false);
					} else {
						play2.setVisible(true);
						play1.setVisible(false);
						lblTodo.setText(" ");
						lblTodo2.setText(" ");
					}
					// REGISTER
				} else if (tabbedPane.getSelectedIndex() == tabbedPane.indexOfTab("Register")) {
					textField_1.setText("");
					lblTodo.setText("Please register a username.");
					lblTodo2.setText(" ");
					btnLogin.setVisible(false);
					// STATS
				} else if (tabbedPane.getSelectedIndex() == tabbedPane.indexOfTab("Stats")) {
					lblTodo.setText("The leaderboards!");
					lblTodo2.setText("Click column headers to sort by them.");
					makeTable();
				}
			}
		});
	}

	private boolean isAlpha(String name) {
		//This function checks if the given name is only alphabetical, and returns a boolean
		//Reference: http://stackoverflow.com/questions/5238491/check-if-string-contains-only-letters
		char[] chars = name.toCharArray();

		for (char c : chars) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}

		return true;
	}

	private void makeTable() {
		//This functions makes a JTable called statsTab from data files
		DefaultTableModel dm = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				//Ensure only the columns with the buttons are editable
				if (column == 4 || column == 5) {
					return true;
				} else {
					return false;
				}
			}
		};
		//Add columns
		dm.addColumn("Username");
		dm.addColumn("Wins");
		dm.addColumn("Tries");
		dm.addColumn("Win rate");
		dm.addColumn("Speak!");
		dm.addColumn("Stop");
		//Make table
		statsTab = new JTable(dm);
		getSort(dm); //Calls getSort which sets up sorting
		
		//This code sets up the cells for speak and stop as JButtons
		//Reference: http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
		statsTab.getColumn("Speak!").setCellRenderer(new ButtonRenderer());
		statsTab.getColumn("Speak!").setCellEditor(new CustomButton(new JCheckBox()) {
			@Override
			protected void doSomething() {
				BackgroundTask bt = new BackgroundTask() {
					@Override
					protected Object doInBackground() throws Exception {
						String cmd = "echo \"" + statsTab.getValueAt(statsTab.getSelectedRow(), 0) + " has "
								+ statsTab.getValueAt(statsTab.getSelectedRow(), 1) + " wins and is in position "
								+ (statsTab.getSelectedRow() + 1) + ", on the leader board with "
								+ statsTab.getRowCount()
								+ " players in total registered for the magic voice game.\" | festival --tts";
						bashCall(cmd);
						return null;
					}
				};
				bt.execute();
			}
		});
		statsTab.getColumn("Stop").setCellRenderer(new ButtonRenderer());
		statsTab.getColumn("Stop").setCellEditor(new CustomButton(new JCheckBox()) {
			@Override
			protected void doSomething() {
				String pid = hm.get(statsTab.getValueAt(statsTab.getSelectedRow(), 0).toString());
				BackgroundTask bt = new BackgroundTask();

				String cmd = "echo `pstree -lp | grep " + pid + " | grep play`";
				String output = bt.bashCall(cmd);
				if (!output.isEmpty()) {
					String sub1 = output.substring(output.indexOf("play(") + 5, output.length()-1);
					String sub2 = sub1.substring(0, sub1.indexOf(")"));
					cmd = "kill -STOP " + sub2;
					bt.bashCall(cmd);
				}
			}
		});
		
		//This code reads in the data from the files and adds it to the data model
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String line = br.readLine();

			while (line != null) {
				BufferedReader br1 = new BufferedReader(new FileReader("." + line.toLowerCase() + "Stats.txt"));
				String data = br1.readLine();
				String[] split = data.split(":");
				int wins = Integer.parseInt(split[0]);
				int times = Integer.parseInt(split[1]);
				int winRate = 0;
				if (times != 0) {
					winRate = wins * 100 / times;
				}
				Object[] obj = { line, "" + wins, "" + times, winRate + "%", "Talk", "Stop" };
				dm.addRow(obj);
				line = br.readLine();
			}
			br.close();
			
			//This code configures the table so the user can not reorder or resize the JTable
			statsTab.getTableHeader().setReorderingAllowed(false);
			statsTab.getTableHeader().setResizingAllowed(false);
			
			//If stats tab doesn't exist, make it. Otherwise just set the component again
			if (tabbedPane.indexOfTab("Stats") == -1) {
				tabbedPane.addTab("Stats", null, new JScrollPane(statsTab), "View Stats");
			} else {
				tabbedPane.setComponentAt(tabbedPane.indexOfTab("Stats"), new JScrollPane(statsTab));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getSort(DefaultTableModel dm) {
		//This function sorts the data in the data model
		//Reference: http://www.codejava.net/java-se/swing/6-techniques-for-sorting-jtable-you-should-know
		TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<DefaultTableModel>(dm);
		
		//The following code sets the comparators for the wins/times strings and win rate string
		rowSorter.setComparator(2, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (Integer.parseInt(o1) > Integer.parseInt(o2)) {
					return 1;
				} else if (Integer.parseInt(o1) < Integer.parseInt(o2)) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		rowSorter.setComparator(1, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (Integer.parseInt(o1) > Integer.parseInt(o2)) {
					return 1;
				} else if (Integer.parseInt(o1) < Integer.parseInt(o2)) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		rowSorter.setComparator(3, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (Integer.parseInt(o1.substring(0, o1.indexOf("%"))) > Integer
						.parseInt(o2.substring(0, o2.indexOf("%")))) {
					return 1;
				} else if (Integer.parseInt(o1.substring(0, o1.indexOf("%"))) < Integer
						.parseInt(o2.substring(0, o2.indexOf("%")))) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		statsTab.setRowSorter(rowSorter);
		//Ensure only data rows can be sorted
		rowSorter.setSortable(0, false);
		rowSorter.setSortable(4, false);
		rowSorter.setSortable(5, false);
		
		//Ensure on the table is already sorted by highest # of wins
		List<SortKey> sortKeys = new ArrayList<SortKey>();
		sortKeys.add(new SortKey(1, SortOrder.DESCENDING));
		rowSorter.setSortKeys(sortKeys);
		rowSorter.sort();
	}

	class BackgroundTask extends SwingWorker {
		//This class is the SwingWorker class for bash calls nested inside Main() for access
		//to Main() fields
		protected String bashCall(String input) {
			//This function executes a bash call
			//Reference: Nasser's slides
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", input);
			Process process;
			try {
				process = builder.start();
				if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
					Field f;
					f = process.getClass().getDeclaredField("pid");
					f.setAccessible(true); 
					int pid;
					pid = f.getInt(process);
					hm.put(statsTab.getValueAt(statsTab.getSelectedRow(), 0).toString(), "" + pid);
				}
				InputStream stdout = process.getInputStream();
				InputStream stderr = process.getErrorStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line = null;
				while ((line = stdoutBuffered.readLine()) != null) {
					return line;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected Object doInBackground() throws Exception {
			//This method specifies what is done in background by SwingWorker
			//Normally overidden, this call is default on startup
			bashCall(
					"echo \"Welcome to the Magic Voice Game. It is now `date +\"%_I:%M%p %A\"`. Please log in or register to play.\" | festival --tts");
			return null;
		}

	}

	class CustomButton extends ButtonEditor {
		//This class inherits from ButtonEditor, a class used in JTable buttons
		//Used to override the doSomething() method in ButtonEditor to customise button pushed actions
		//Reference: http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
		public CustomButton(JCheckBox checkBox) {
			super(checkBox);
		}
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {
		//This class renders JTable cells into JButtons
		//Reference: http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {
		//This class detects and manages the JTable button presses
		//Reference: http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
		protected JButton button;
		private String label;
		private boolean isPushed;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			label = (value == null) ? "" : value.toString();
			button.setText(label);
			isPushed = true;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				//Calls the doSomething method
				doSomething();
			}
			isPushed = false;
			return label;
		}

		protected void doSomething() {
		};

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}
}
