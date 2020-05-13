import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
import javax.swing.border.*;
import javax.imageio.ImageIO;

public class PlayerUI {

	private JTextArea outputArea;
	private JTextField inputArea;
    private ButtonGroup buttonGroup;
    private ButtonGroup buttonGroup2;
	private JLabel headerLabel;
	
	private GameClient client;
	private boolean mainGameActive = false;
	private boolean joiningGame = false;
	private int portNumber;
	private String hostAddress;
	
    private boolean waitingOnInput = false;
    private boolean inputSent = false;
    
    private int numOfPlayers = 0;
    private int numOfRounds = 0;
    private String warName = "";
    
    JFrame frame = new JFrame("History Arcade");
    JFrame frame2 = new JFrame("Main Game");
    
	/* Constructs the JFrame interface. */
	public PlayerUI() {
		buttonGroup = new ButtonGroup();
		buttonGroup2 = new ButtonGroup();
		headerLabel = new JLabel("History Arcade", SwingConstants.CENTER);
		headerLabel.setFont(new Font("Serif", Font.BOLD, 50));
		//this.add(headerLabel, BorderLayout.NORTH);
		
		//Creating the main Panel
		JPanel mainCenterPanel = new JPanel();  //Panel of 6 rows, 1 column 
		mainCenterPanel.setLayout(new GridLayout(6,1));
		
		//Creating the player count selection panel
		JPanel playerCountButtonsPanel = new JPanel();
		//playerCountButtonsPanel.setLayout(new GridLayout(1,2));
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagConstraints gbc2 = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.VERTICAL;
	    gbc.anchor = GridBagConstraints.CENTER;
	    gbc.weightx = 0.5;
	    gbc.gridy = 0; 
	    gbc.gridx = 1; 
	    gbc.insets = new Insets(10, 10, 10, 10);
	    gbc.gridwidth = 1;
	    gbc.gridheight = 1;

	    gbc2.fill = GridBagConstraints.VERTICAL;
	    gbc2.anchor = GridBagConstraints.CENTER;
	    gbc2.weightx = 0.5;
	    gbc2.gridy = 0; 
	    gbc2.gridx = 2; 
	    gbc2.insets = new Insets(10, 10, 10, 10);
	    gbc2.gridwidth = 1;
	    gbc2.gridheight = 1;
	    playerCountButtonsPanel.setLayout(new GridBagLayout());
		
		JToggleButton singlePlayerButton = new JToggleButton("Single Player");
		JToggleButton TwoPlayersButton = new JToggleButton("Multiplayer");
		singlePlayerButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  numOfPlayers = 1;
			  System.out.println(numOfPlayers);
		    // display/center the dialog when the button is pressed
				/*
				 * JDialog d = new JDialog(frame, "Hello", true);
				 * d.setLocationRelativeTo(frame); d.setVisible(true);
				 */
		  }
		});
		TwoPlayersButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  numOfPlayers = 2;
			  System.out.println(numOfPlayers);
		    // display/center the dialog when the button is pressed
				/*
				 * JDialog d = new JDialog(frame, "Hello", true);
				 * d.setLocationRelativeTo(frame); d.setVisible(true);
				 */
		  }
		});
		
		//Border line = new LineBorder(Color.BLACK);
		//Border margin = new EmptyBorder(20, 15, 20, 15);
		//Border compound = new CompoundBorder(line, margin);
		singlePlayerButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		//singlePlayerButton.setBorder(compound);
		//singlePlayerButton.setBorder(new RoundedBorder(10));
		//TwoPlayersButton.setBorder(compound);
		buttonGroup.add(singlePlayerButton);
		buttonGroup.add(TwoPlayersButton);
		playerCountButtonsPanel.add(singlePlayerButton,gbc);
		playerCountButtonsPanel.add(TwoPlayersButton,gbc2);
		//playerCountButtonsPanel.add(buttonGroup);
		mainCenterPanel.add(playerCountButtonsPanel);
		
		//Creating the round setting input panel
		JPanel roundSettingInputPanel = new JPanel();
		GroupLayout roundLayout = new GroupLayout(roundSettingInputPanel);
		
		roundLayout.setAutoCreateGaps(true);
		roundLayout.setAutoCreateContainerGaps(true);
		JLabel roundsLabel = new JLabel("Number of Rounds:", SwingConstants.CENTER);
		JTextField roundsInput = new JTextField("Enter rounds here!");
		roundsInput.setPreferredSize(new Dimension(150,30));  //new
		roundLayout.setHorizontalGroup(
				   roundLayout.createSequentialGroup()
				      .addComponent(roundsLabel)
				      .addGroup(roundLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				           .addComponent(roundsInput))
				);
		roundLayout.setVerticalGroup(
				   roundLayout.createSequentialGroup()
				      .addGroup(roundLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(roundsLabel)
				           .addComponent(roundsInput))
				);
		
		roundSettingInputPanel.setLayout(roundLayout);  //(new GridLayout(1,2));  new
		//roundSettingInputPanel.add(roundsLabel);
		//roundSettingInputPanel.add(roundsInput);
		mainCenterPanel.add(roundSettingInputPanel);
		
		//Creating the game map selection panel
		JPanel gameModeButtonsPanel = new JPanel();
		GroupLayout gameModeButtonsLayout = new GroupLayout(gameModeButtonsPanel);
		
		gameModeButtonsLayout.setAutoCreateGaps(true);
		gameModeButtonsLayout.setAutoCreateContainerGaps(true);
		

		//Image path checks
		//File f = new File("Group-30-Spring-2020-master/Code/HistoryArcade/src/ww1.gif");
		//System.out.println(f.exists());
		
		ImageIcon ic = new ImageIcon(
				new ImageIcon("src/ww1.gif").getImage().getScaledInstance(
						100, 100, java.awt.Image.SCALE_SMOOTH));
		ImageIcon ic2 = new ImageIcon(
				new ImageIcon("src/US civil war.jpg").getImage().getScaledInstance(
						100, 100, java.awt.Image.SCALE_SMOOTH));
		
		JToggleButton GameMode1 = new JToggleButton("WWI", ic);
		//GameMode1.setPreferredSize(new Dimension(900,900));
		JToggleButton GameMode2 = new JToggleButton("WWII", ic);
		JToggleButton GameMode3 = new JToggleButton("Civil War", ic2);
	
		gameModeButtonsLayout.setHorizontalGroup(
				   gameModeButtonsLayout.createSequentialGroup()
				      .addComponent(GameMode1,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				      .addComponent(GameMode2,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				      .addGroup(gameModeButtonsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				           .addComponent(GameMode3,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		gameModeButtonsLayout.setVerticalGroup(
				gameModeButtonsLayout.createSequentialGroup()
				      .addGroup(gameModeButtonsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(GameMode1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				           .addComponent(GameMode2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				           .addComponent(GameMode3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		
		
		GameMode1.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e){
			  warName = "WWI";
		  }
		});
		GameMode2.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e){
			  warName = "WWII";
		  }
		});
		GameMode3.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e){
			  warName = "US Civil War";
		  }
		});
		buttonGroup2.add(GameMode1);
		buttonGroup2.add(GameMode2);
		buttonGroup2.add(GameMode3);
		gameModeButtonsPanel.setLayout(gameModeButtonsLayout);  //(new GridLayout(1,3));  new
		//gameModeButtonsPanel.add(GameMode1);
		//gameModeButtonsPanel.add(GameMode2);
		//gameModeButtonsPanel.add(GameMode3);
		mainCenterPanel.add(gameModeButtonsPanel);

		
		//Creating game start panel
		JPanel gameStartOptionsPanel = new JPanel();
		gameStartOptionsPanel.setLayout(new FlowLayout());  //(new GridLayout(1,2));  new
		JButton startGameButton = new JButton("Start Game");
		startGameButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(warName == "") {  //War is not chosen
				  JOptionPane.showMessageDialog(frame, "Please select a war");	
				  return;
			  }
			  
			  if(roundsInput.getText().equals("") || roundsInput.getText().equals("Enter rounds here!")) {  //Rounds not entered
				  numOfRounds = 2;  //change to randomNumber
				  if(TwoPlayersButton.isSelected()) {  //Instance a two player game
					  numOfPlayers = 2;
					  frame.setVisible(false);  //Hide old frame
						  
					  //JFrame frame2 = new JFrame("Main Game");  //Create a new frame
					  if(warName == "WWI") {
						  frame2.getContentPane().add(makeGameScreen2("src/ww1 gamePlay.png"));
					  }
					  if(warName == "WWII") {
						  frame2.getContentPane().add(makeGameScreen2("src/ww2 gp.jpg"));
					  }
					  if(warName == "US Civil War") {
						  frame2.getContentPane().add(makeGameScreen2("src/US civil war.jpg"));
					  }

					  frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					  frame2.setSize(800,800);
					  frame2.setVisible(true);
					  
					  mainGameActive = true;	  
				  }
				  else if (singlePlayerButton.isSelected()) {  //Instance a one player game
					  numOfPlayers = 1;
					  frame.setVisible(false);  //Hide old frame
						  
					  //JFrame frame2 = new JFrame("Main Game");  //Create a new frame
					  if(warName == "WWI") {
						  frame2.getContentPane().add(makeGameScreen2("src/ww1 gamePlay.png"));
					  }
					  else if(warName == "WWII") {
						  frame2.getContentPane().add(makeGameScreen2("src/ww2 gp.jpg"));
					  }
					  else if(warName == "US Civil War") {
						  frame2.getContentPane().add(makeGameScreen2("src/US civil war.jpg"));
					  }
					  frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					  frame2.setSize(800,800);
					  frame2.setVisible(true);
						  
					  mainGameActive = true;
				  }
				  else {  //No option has been selected for player count
					  JOptionPane.showMessageDialog(frame, "Please select number of players");	         
				  }
			  }
			  else {  //Input for number of rounds has been given
				  try {
					  numOfRounds = Integer.parseInt(roundsInput.getText());
					  
					  if(numOfRounds < 101 && numOfRounds > 9) {
						  if(TwoPlayersButton.isSelected()) {
							  numOfPlayers = 2;
							  frame.setVisible(false);  //Hide old frame
							  
							  //JFrame frame2 = new JFrame("Main Game");  //Create a new frame
							  if(warName == "WWI") {
								  frame2.getContentPane().add(makeGameScreen2("src/ww1 gamePlay.png"));
							  }
							  else if(warName == "WWII") {
								  frame2.getContentPane().add(makeGameScreen2("src/ww2 gp.jpg"));
							  	}
							  else if(warName == "Civil War") {
								  frame2.getContentPane().add(makeGameScreen2("src/US civil war.jpg"));
							  }
							  
							  frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							  frame2.setSize(800,800);
							  frame2.setVisible(true);
							  
							  mainGameActive = true;	 
						  }
						  else if (singlePlayerButton.isSelected()) {
							  numOfPlayers = 1;
							  frame.setVisible(false);  //Hide old frame
								  
							  //JFrame frame2 = new JFrame("Main Game");  //Create a new frame
							  if(warName == "WWI") {
								  frame2.getContentPane().add(makeGameScreen2("src/ww1 gamePlay.png"));
							  }
							  if(warName == "WWII") {
								  frame2.getContentPane().add(makeGameScreen2("src/ww2 gp.jpg"));
							  }
							  if(warName == "Civil War") {
								  frame2.getContentPane().add(makeGameScreen2("src/US civil war.jpg"));
							  }

							  frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							  frame2.setSize(800,800);
							  frame2.setVisible(true);
							  
							  mainGameActive = true;	 
						  }
						  else {
							  JOptionPane.showMessageDialog(frame, "Please select number of players");		         
						  }
						
					  }
					  else {
						  JOptionPane.showMessageDialog(frame, "Please type in a valid number of rounds between 10-100");	
					  }
						
				  }
				  catch(Exception e1){
					  JOptionPane.showMessageDialog(frame, "Please type in a valid number of rounds between 10-100");		
				  }
			  }
			  
			  //mainCenterPanel.removeAll();
			  //mainCenterPanel.repaint();
			  // display/center the dialog when the button is pressed
			  /*
			   * JDialog d = new JDialog(frame, "Hello", true);
			   * d.setLocationRelativeTo(frame); d.setVisible(true);
			   */
		  }
		});
			
		gameStartOptionsPanel.add(startGameButton);
		//gameStartOptionsPanel.add(orJoinLabel);  old
		mainCenterPanel.add(gameStartOptionsPanel);
		
		
		//Creating IP and Port Join Input Panel
		JPanel joinOptionPanel = new JPanel();
		joinOptionPanel.setLayout(new FlowLayout());  //(new GridLayout(1,2));  new
		JLabel orJoinLabel = new JLabel("Or Join a Game:", SwingConstants.CENTER);	

		JPanel ipPortPanel = new JPanel();
		ipPortPanel.setLayout(new GridLayout(2,2));
		
		JLabel ipLabel = new JLabel("IP Address:", SwingConstants.CENTER);
		JLabel portLabel = new JLabel("Port:", SwingConstants.CENTER);
		JTextField ipTextField = new JTextField("");
		JTextField portTextField = new JTextField("");
		joinOptionPanel.add(orJoinLabel);
		ipPortPanel.add(ipLabel);
		ipPortPanel.add(ipTextField);
		ipPortPanel.add(portLabel);
		ipPortPanel.add(portTextField);
		joinOptionPanel.add(ipPortPanel);
		
		JButton joinButton = new JButton("Join Game");
		joinButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			if(ipTextField.getText().equals("") || portTextField.getText().equals("")) {  //join button clicked without valid inputs
				JOptionPane.showMessageDialog(frame, "Please enter an ip and port number.");	
				return;
			}
			
			//Retrieve IP and port number from input 
			hostAddress = ipTextField.getText();
			try {
				portNumber = Integer.parseInt(portTextField.getText());
			}
			catch(Exception e2) {
				JOptionPane.showMessageDialog(frame, "Please enter a valid port number.");	
				return;
			}

			numOfPlayers = 2;
			frame.setVisible(false);  //Hide old frame
			  
		    //JFrame frame2 = new JFrame("Main Game");  //Create a new frame
		    if(warName == "WWI") {
			  frame2.getContentPane().add(makeGameScreen2("src/ww1 gamePlay.png"));
		    }
		    else if(warName == "WWII") {
			  frame2.getContentPane().add(makeGameScreen2("src/ww2 gp.jpg"));
		  	}
		    else if(warName == "Civil War") {
			  frame2.getContentPane().add(makeGameScreen2("src/US civil war.jpg"));
		    }
		  
		    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame2.setSize(800,800);
		    frame2.setVisible(true);
		    
		    mainGameActive = true;
		    joiningGame = true;
		  }
		});
		joinOptionPanel.add(joinButton);
		mainCenterPanel.add(joinOptionPanel);

		//Adding the main panel to the center position
		//this.add(mainCenterPanel, BorderLayout.CENTER);
		
		//Creating output scroll area for testing communication
		TitledBorder panelTitle = new TitledBorder("Output Window");
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setBorder( new CompoundBorder( new EmptyBorder(10, 1, 10, 1), panelTitle) );
		mainCenterPanel.add(scrollPane);
		
		
		//Adjust UI settings
		frame.setVisible(true);
		frame.setSize(800,800);
		frame.getContentPane().add(mainCenterPanel, BorderLayout.CENTER);
		frame.getContentPane().add(headerLabel, BorderLayout.NORTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	public JPanel makeGameScreen2(String fileName) {
		JPanel mainGameJPanel = new JPanel();
	    mainGameJPanel.setLayout(new GridLayout(2,1));
		
	    ImageIcon icon = new ImageIcon(
				new ImageIcon(fileName).getImage().getScaledInstance(
						490, 350, java.awt.Image.SCALE_SMOOTH));
		JLabel picLabel = new JLabel(icon);
		
		/*  This doesn't work with GridLayout. It only works for GridBagLayout.
		GridBagConstraints picLabelConstraints = new GridBagConstraints();
		picLabelConstraints.fill = GridBagConstraints.VERTICAL;
		picLabelConstraints.anchor = GridBagConstraints.CENTER;
		picLabelConstraints.weightx = 0.5;
		picLabelConstraints.gridy = 0; 
	    picLabelConstraints.gridx = 2; 
	    picLabelConstraints.insets = new Insets(10, 10, 10, 10);
	    picLabelConstraints.gridwidth = 1;
	    picLabelConstraints.gridheight = 1;
	    mainGameJPanel.add(picLabel, picLabelConstraints);
	    */
	    
		JPanel scoreJPanelAndMovesJPanel = new JPanel();
	    scoreJPanelAndMovesJPanel.setLayout(new GridLayout(1,2));
	    
	    JPanel scoreBoardJPanel = new JPanel();
	    scoreBoardJPanel.setLayout(new GridLayout(4,1));
	    
	    JButton quitButton = new JButton("Quit");
	    quitButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  frame2.setVisible(false);
			  //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			  frame.setVisible(true);
			  inputArea.setText("quit");
			  //client.sendOutgoingMsg("quit");
			  if(waitingOnInput)
	    			inputSent = true;
			  
			  try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			  inputArea.setText("Enter your answer here");
			  
		  }
		});
	    JLabel scoreJLabel = new JLabel("Score Board", SwingConstants.CENTER);
	    scoreJLabel.setFont(new Font("Serif", Font.BOLD, 55));
	    scoreJLabel.setForeground(Color.CYAN);
	    
	    JLabel playerOneP = new JLabel("Player 1 Points: 0");  //Placeholder for now will have player data later
	    playerOneP.setFont(new Font("Serif", Font.BOLD, 23));
	    playerOneP.setForeground(Color.BLUE);
	    
	    JLabel playerTwoP = new JLabel("Player 2 Points: 0");  //Placeholder for now will have player data later
	    playerTwoP.setFont(new Font("Serif", Font.BOLD, 23));
	    playerTwoP.setForeground(Color.RED);
	    
	    scoreBoardJPanel.add(scoreJLabel);
	    scoreBoardJPanel.add(playerOneP);
	    scoreBoardJPanel.add(playerTwoP);
	    scoreBoardJPanel.add(quitButton);
	    
	  	/* Unused currently	    
	  	JPanel playerCButtonsPanel = new JPanel();
	  	playerCButtonsPanel.setLayout(new GridLayout(5,1));
	  	playerCButtonsPanel.add(testButton);
	  	playerCButtonsPanel.add(testButton3);
	  	playerCButtonsPanel.add(testButton);
	  	playerCButtonsPanel.add(testButton);
	  	playerCButtonsPanel.add(testButton);
	  	scoreJPanelAndMovesJPanel.add(playerCButtonsPanel);
	  	*/
	  	
	  	JPanel playerCAndOutputWindowPanel = new JPanel();
	  	playerCAndOutputWindowPanel.setLayout(new GridLayout(2,1));

	  	JPanel playerCAndSubmit = new JPanel();
	  	playerCAndSubmit.setLayout(new GridLayout(1,2));
	  	
	    JToggleButton submitButton = new JToggleButton("Submit Answer");
	    submitButton.addActionListener(new ActionListener() { 
	    	public void actionPerformed(ActionEvent e) { 
	    		if(waitingOnInput)
	    			inputSent = true;
			} 
		});
	    
	  	inputArea = new JTextField("Enter your answer here");
	  	
	  	playerCAndSubmit.add(inputArea);
	  	playerCAndSubmit.add(submitButton);

		TitledBorder panelTitle = new TitledBorder("Output Window");
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(outputArea);
		scrollPane.setBorder( new CompoundBorder( new EmptyBorder(10, 1, 10, 1), panelTitle) );
		
	  	playerCAndOutputWindowPanel.add(playerCAndSubmit);
	  	playerCAndOutputWindowPanel.add(scrollPane);
	  	
	    /* Unused currently
	    JPanel outputPanel = new JPanel();
		outputPanel.add(scrollPane);
	    outputWindowConstraints.fill = GridBagConstraints.VERTICAL;
	    outputWindowConstraints.fill = GridBagConstraints.HORIZONTAL;
	    outputWindowConstraints.anchor = GridBagConstraints.CENTER;
	    outputWindowConstraints.weightx = 0.5;
	    outputWindowConstraints.gridy = 0; 
	    outputWindowConstraints.gridx = 1; 
	    outputWindowConstraints.insets = new Insets(10, 10, 10, 10);
	    outputWindowConstraints.gridwidth = 1;
	    outputWindowConstraints.gridheight = 1;
	    mainGameJPanel.add(outputPanel, outputWindowConstraints);
	    */
	  	
	  	scoreJPanelAndMovesJPanel.add(playerCAndOutputWindowPanel);
	  	scoreJPanelAndMovesJPanel.add(scoreBoardJPanel);
	  	
	  	mainGameJPanel.add(picLabel);
	  	mainGameJPanel.add(scoreJPanelAndMovesJPanel);
	  	
		return mainGameJPanel;
	}
	
	/* Connects to an existing Game waiting for a second player. */
	public void connectToServer(String ip, int port) {
		System.out.println("Reached beginning of connectToServer()");
		client = new GameClient(ip,port);  //Create game client to handle communication with server
		String rec;
		String msg;
		
		while(true) {  //Loop that processes communication between client and server
			rec = client.getIncomingMsg();  //Get the incoming message
			
			if(rec.equals("QUIT")) {  //Check if it's a quit command
				break;
			}
			else if(rec.equals("INPUT")) {  //Check if it's requesting input
				msg = this.getInput();  //Get and send input
				client.sendOutgoingMsg(msg);
			}
			else  //Otherwise display to output
				this.display(rec);
		}
		
		client.closeClient();
	}
	
	/* Begins setup for the game by connecting to the GameServer and sending map type and player count information to it. */
	public void startGame(String ip, int port) {
		System.out.println("Reached beginning of startGame()");
		client = new GameClient(ip,port);  //Create game client to handle communication with server
		String rec;
		String msg;
		
		//Send GameServer map type and player count
		client.sendOutgoingMsg(Integer.toString(numOfPlayers));
		client.sendOutgoingMsg(warName);
		
		while(true) {  //Loop that processes communication between client and server
			//System.out.println("Reached client.getIncomingMsg");
			rec = client.getIncomingMsg();  //Get the incoming message
			//System.out.println("Completed client.getIncomingMsg");
			
			if(rec.equals("QUIT")) {  //Check if it's a quit command
				break;
			}
			else if(rec.equals("INPUT")) {  //Check if it's requesting input
				msg = this.getInput();  //Get and send input
				client.sendOutgoingMsg(msg);
			}
			else  //Otherwise display to output
				this.display(rec);
		}
		
		client.closeClient();
	}
	
	
	public void display(String str) {
		outputArea.append(str + "\n");
		outputArea.setCaretPosition(outputArea.getDocument().getLength());
	}
	
	public String getInput() {
		waitingOnInput = true;
		String input;
		
		while(true) {  //Loop waiting until user gives input
			if(inputSent) {  //When submit button is clicked retrieve user input
				input = inputArea.getText().trim();
				inputArea.setText("");  //Clear text box before progressing
				this.display(input);  //Show input to user
				inputSent = false;  //Toggle the input check variables
				waitingOnInput = false;
				return input;
			}
			
			//Just to slow the infinite loop down a bit
			try {  //Wait a second before continuing
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		PlayerUI ui = new PlayerUI();
		
		while(!ui.mainGameActive) {  //Waits for the main game menu to be instanced
			//Just to slow the infinite loop down a bit
			try {  //Wait a second before continuing
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(ui.joiningGame)
			ui.connectToServer(ui.hostAddress, ui.portNumber);
		else
			ui.startGame("127.0.0.1",5555);
	}
	
}
