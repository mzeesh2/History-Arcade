import javafx.scene.text.Font;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

import animatefx.animation.*;
import animatefx.util.*;

public class PlayerUI_FX extends Application {
	private Integer STARTTIME = 15;
	private Timeline timeline;
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

	private static GameClient client = null;
	private static int portNumber;
	private static String hostAddress;
	
	private static int numOfPlayers = 0;
	private static int numOfRounds = 0;
	private static boolean gameOver = false;
	private static boolean joiningGame = false;
    
	private static TextArea outputArea;
	private static TextField inputArea;
    private static String warNameString = "";
    
    Timer timer;
	HashMap<String, Scene> sceneMap;
	Button startButton, joinGame, createGame, quitGame,submitButton, quitButton;
	String playerChoiceList[] = {"Single-Player", "Multi-Player"};
	String warChoiceList[] = {"World War I", "World War II", "US Civil War"};
	ComboBox<String> numPlayers = new ComboBox<String>(FXCollections.observableArrayList(playerChoiceList)); 
	ComboBox<String> warChoice = new ComboBox<String>(FXCollections.observableArrayList(warChoiceList)); 
	String playerOneLocationString;
	String playerTwoLocationString;
	Stage myStg;
	Scene scene, scene2, scene3, scene4;
	Label welcomeText = new Label("Welcome to History Arcade!");
	Label numPlayersText = new Label("Please Select the Number of Players:");
	Label numRounds = new Label("Please Select the Number of Rounds:");
	Label selectWarText = new Label("Please Select the War:");
	Label ipText = new Label("Please Enter the IP:");
	Label portText = new Label("Please Enter the Port Number:");
	Label timerText = new Label("Time Remaining: ");
	Alert a = new Alert(AlertType.NONE); 
	TextField portInfo, ipInfo, numOfRoundsTextField;	
	Boolean filledInIp, filledInPort;
	VBox playerPointsVbox = new VBox(15);
	VBox timerVbox = new VBox(15, timerText, timerLabel);
	Label player1points= new Label();
	Label player2points = new Label();
	Pane playerPane = new Pane();
	Image playerImage1= new Image("player1.jpg");
	Image playerImage2= new Image("player2.jpg");
	ImageView playerOneView = new ImageView(playerImage1);
	ImageView playerTwoView = new ImageView(playerImage2);
	
	VBox outputBoxAndTitle;
	HBox inputAndSubmitBox;
	ArrayList<Integer> playerOneArrayList = new ArrayList<Integer>();
	ArrayList<Integer> playerTwoArrayList = new ArrayList<Integer>();
	Location portugal = new Location(1,"Portugal");
	Location spain = new Location(2,"Spain");
	Player player1 = new Player("player1","red", portugal);
	Player player2 = new Player("player2","blue", spain);
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		welcomeText.setId("welcomeText");
		filledInIp = false;
		filledInPort = false;
		sceneMap = new HashMap<String, Scene>();
		
		ipInfo = new TextField();
		portInfo = new TextField();
		ipInfo.setVisible(false);
		portInfo.setVisible(false);
		ipText.setVisible(false);
		portText.setVisible(false);
		
		numOfRoundsTextField = new TextField();
		numOfRoundsTextField.setPromptText("Set to Five Rounds by Default");
		numRounds.setVisible(false);
		numOfRoundsTextField.setVisible(false);
		selectWarText.setVisible(false);
		warChoice.setVisible(false);	
		
		Image ww1 = new Image("ww1.gif");
		Image ww2 = new Image("ww1.gif");
		Image civImage = new Image("US civil war.jpg");

		ImageView ww1ImageView = new ImageView(ww1);
		ImageView ww2ImageView = new ImageView(ww2);
		ImageView civilImageView = new ImageView(civImage);
		
		ww1ImageView.setFitHeight(200);
		ww1ImageView.setFitWidth(100);
		ww1ImageView.setPreserveRatio(true);
		
		ww2ImageView.setFitHeight(200);
		ww2ImageView.setFitWidth(100);
		ww2ImageView.setPreserveRatio(true);
		
		civilImageView.setFitHeight(200);
		civilImageView.setFitWidth(100);
		civilImageView.setPreserveRatio(true);
		
		ww1ImageView.setVisible(false);
		ww2ImageView.setVisible(false);
		civilImageView.setVisible(false);
		
		startButton = new Button("Start Game");
		startButton.setVisible(false);
		joinGame = new Button("Join Game");
		joinGame.setVisible(false);
		joinGame.setDisable(true);
		createGame = new Button("Create Game");
		createGame.setVisible(false);
		createGame.setDisable(true);
		quitGame = new Button("Quit");
		quitGame.setId("quitButton");
		
		welcomeText.setId("welcomeText");
		BorderPane mainPage = new BorderPane();
        // set background 
		mainPage.setId("mainPage");
        mainPage.getStylesheets().add("CSSStyler.css");
		HBox numberOfPlayers = new HBox(50, numPlayersText, numPlayers);
		HBox ipBox = new HBox(15, ipText, ipInfo);
		HBox portBox = new HBox(10, portText, portInfo);
		VBox ipAndPortBox = new VBox(10, ipBox, portBox);
		HBox ipPortAndJoinGameBox = new HBox(20, ipAndPortBox, new VBox(20, createGame, joinGame, quitGame));
		HBox countryChoice = new HBox(10, selectWarText, warChoice);
		HBox warPics = new HBox(20, ww1ImageView, ww2ImageView, civilImageView);
		VBox main = new VBox(20, welcomeText,numberOfPlayers,new HBox(20, numRounds, numOfRoundsTextField), countryChoice, warPics, startButton, ipPortAndJoinGameBox);
		
		mainPage.setPadding(new Insets(75));
		mainPage.setCenter(main);
		
		numPlayers.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->{
        	newValue = numPlayers.getSelectionModel().getSelectedItem();
            if (newValue == "Single-Player") {
    			numRounds.setVisible(true);
    			numOfRoundsTextField.setVisible(true);
    			warChoice.setVisible(true);
    			selectWarText.setVisible(true);
    			ww1ImageView.setVisible(false);
    			ww2ImageView.setVisible(false);
    			civilImageView.setVisible(false);
    			ipInfo.setVisible(false);
    			ipText.setVisible(false);
    			portInfo.setVisible(false);
    			portText.setVisible(false);
    			joinGame.setVisible(false);
    			createGame.setVisible(false);
    			
    			//set animation
    			new Jello(numRounds).play();
    			new Jello(numOfRoundsTextField).play();
    			new Jello(warChoice).play();
    			new Jello(selectWarText).play();
    			
    			warChoice.getSelectionModel().clearSelection();
    			warChoice.setValue(null);
    			warChoice.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) ->{
    		        newVal = warChoice.getSelectionModel().getSelectedItem();
    		        if (newVal == "World War I"){
    		          	warNameString = "WWI";  
    		          	ww1ImageView.setVisible(true);
    		           	ww2ImageView.setVisible(false);
    		           	civilImageView.setVisible(false);
    		           	startButton.setVisible(true);
    	    			ipInfo.setVisible(false);
    	    			ipText.setVisible(false);
    	    			portInfo.setVisible(false);
    	    			portText.setVisible(false);
    	    			joinGame.setVisible(false);
    	    			createGame.setVisible(false);
    	    			
    	    			//set animations
    	    			new BounceInDown(ww1ImageView).play();
    	    			new Swing(startButton).play();
    		        }
    		    	else if(newVal == "World War II") {
    		    		warNameString = "WWII";  
    		           	ww1ImageView.setVisible(false);
    		           	ww2ImageView.setVisible(true);
    		           	civilImageView.setVisible(false);
    		    		startButton.setVisible(true);
    	    			ipInfo.setVisible(false);
    	    			ipText.setVisible(false);
    	    			portInfo.setVisible(false);
    	    			portText.setVisible(false);
    	    			joinGame.setVisible(false);
    	    			createGame.setVisible(false);
    	    			
    	    			//set animations
    	    			new BounceInDown(ww2ImageView).play();
    	    			new Swing(startButton).play();
    		    	}
    		    	else if(newVal == "US Civil War") {
    		    		warNameString = "Civil War";  
    		           	ww1ImageView.setVisible(false);
    		           	ww2ImageView.setVisible(false);
    		           	civilImageView.setVisible(true);
    		           	startButton.setVisible(true);
    	    			ipInfo.setVisible(false);
    	    			ipText.setVisible(false);
    	    			portInfo.setVisible(false);
    	    			portText.setVisible(false);
    	    			joinGame.setVisible(false);
    	    			createGame.setVisible(false);
    	    			
    	    			//set animations
    	    			new BounceInDown(civilImageView).play();
    	    			new Swing(startButton).play();
    		    	}
    			});
            }
            else if(newValue == "Multi-Player") {
    			numRounds.setVisible(true);
    			numOfRoundsTextField.setVisible(true);
    			warChoice.setVisible(true);
    			selectWarText.setVisible(true);
            	ww1ImageView.setVisible(false);
            	ww2ImageView.setVisible(false);
            	civilImageView.setVisible(false);
            	startButton.setVisible(false);
            	
            	//set animations
    			new Shake(numRounds).play();
    			new Shake(numOfRoundsTextField).play();
    			new Shake(warChoice).play();
    			new Shake(selectWarText).play();
            	
    			warChoice.getSelectionModel().clearSelection();
    			warChoice.setValue(null);
    			warChoice.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) ->{
    		       	newVal = warChoice.getSelectionModel().getSelectedItem();
    		        if (newVal == "World War I"){
    		           	warNameString = "WWI";  
    		           	ww1ImageView.setVisible(true);
    		           	ww2ImageView.setVisible(false);
    		           	civilImageView.setVisible(false);
    	                startButton.setVisible(false);
    		           	ipText.setVisible(true);
    		    		portText.setVisible(true);
    		    		ipInfo.setVisible(true);
    		    		portInfo.setVisible(true);
    		    		joinGame.setVisible(true);
    		    		createGame.setVisible(true);
    		    		
    	    			//set animations
    	    			new BounceInDown(ww1ImageView).play();
    	    			new RollIn(ipText).play();
    	    			new RollIn(portText).play();
    	    			new Flash(ipInfo).play();
    	    			new Flash(portInfo).play();
    	    		
    		    	}
    		    	else if(newVal == "World War II") {
    		    		warNameString = "WWII";  
    		            ww1ImageView.setVisible(false);
    		            ww2ImageView.setVisible(true);
    		            civilImageView.setVisible(false);
    	                startButton.setVisible(false);
    		    		ipText.setVisible(true);
    		    		portText.setVisible(true);
    		    		ipInfo.setVisible(true);
    		    		portInfo.setVisible(true);
    		    		joinGame.setVisible(true);
    		    		createGame.setVisible(true);
    		    		
    	    			//set animations
    	    			new BounceInDown(ww2ImageView).play();
    	    			new RollIn(ipText).play();
    	    			new RollIn(portText).play();
    	    			new Flash(ipInfo).play();
    	    			new Flash(portInfo).play();
    		    	}
    		    	else if(newVal == "US Civil War") {
    		    		warNameString = "Civil War";  
    		           	ww1ImageView.setVisible(false);
    		           	ww2ImageView.setVisible(false);
    		           	civilImageView.setVisible(true);
    	                startButton.setVisible(false);
    		    		ipText.setVisible(true);
    		    		portText.setVisible(true);
    		    		ipInfo.setVisible(true);
    		    		portInfo.setVisible(true);
    		    		joinGame.setVisible(true);
    		    		createGame.setVisible(true);
    		    		
    	    			//set animations
    	    			new BounceInDown(civilImageView).play();
    	    			new RollIn(ipText).play();
    	    			new RollIn(portText).play();
    	    			new Flash(ipInfo).play();
    	    			new Flash(portInfo).play();
    		    	}
    			});
    		}
		});
		
		
		startButton.setOnAction((ActionEvent event)->{ 
			if(warNameString == "") {  //War is not chosen
				a.setAlertType(AlertType.ERROR);
				a.setTitle("War was not selected");
				a.setContentText("Please select a war!");
				a.show();
				return;
			}
			if(numOfRoundsTextField.getText().equals("")) {
				numOfRounds = 5;//deafualt is 5 rounds
				if(numPlayers.getValue() == "Multi-Player") {
					numOfPlayers = 2;  //Set player count
					if(warNameString == "WWI") {
						makeGameScreen("ww1 gamePlay.png", primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
					if(warNameString == "WWII") {
						makeGameScreen("ww2 gp.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
					if(warNameString == "Civil War") {
						makeGameScreen("US civil war.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
				}
				else if (numPlayers.getValue() == "Single-Player") {
					numOfPlayers = 1;  //Set player count					
					if(warNameString == "WWI") {	
						makeGameScreen("ww1 gamePlay.png",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
						
					}
					if(warNameString == "WWII") {
						makeGameScreen("ww2 gp.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
					if(warNameString == "Civil War") {
						makeGameScreen("US civil war.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
				}
				else {
					a.setAlertType(AlertType.ERROR);
					a.setTitle("Number of players was not selected");
					a.setContentText("Please select number of Players!");
					a.show();		         
				}
			}
			else {
				try {
					
					numOfRounds = Integer.parseInt(numOfRoundsTextField.getText());
					//System.out.println("The number of rounds input is: " + numOfRounds);
					if(numOfRounds < 2|| numOfRounds >= 101){
						a.setAlertType(AlertType.ERROR);
						a.setTitle("Invalid number of rounds");
						a.setContentText("Input a number of rounds between 2 and 100");
						a.show();
						return;
					}
					
					new FadeOut(mainPage).play();
					
					if(numPlayers.getValue() == "Multi-Player") {
						numOfPlayers = 2;					
						if(warNameString == "WWI") {	
							makeGameScreen("ww1 gamePlay.png",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
							
						}
						if(warNameString == "WWII") {
							makeGameScreen("ww2 gp.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						if(warNameString == "Civil War") {
							makeGameScreen("US civil war.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						//myStg.setScene(sceneMap.get("playGame"));
					}
					else if (numPlayers.getValue() == "Single-Player") {
						numOfPlayers = 1;					
						if(warNameString == "WWI") {	
							makeGameScreen("ww1 gamePlay.png",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
							
						}
						if(warNameString == "WWII") {
							makeGameScreen("ww2 gp.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						if(warNameString == "Civil War") {
							makeGameScreen("US civil war.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						//myStg.setScene(sceneMap.get("playGame"));
					}
					else {
						a.setAlertType(AlertType.ERROR);
						a.setTitle("Number of players was not selected");
						a.setContentText("Please select number of Players!");
						a.show();		         
					}
				}
				catch(NumberFormatException e){
					a.setAlertType(AlertType.ERROR);
					a.setTitle("Please enter a number");
					a.setContentText("Please enter a correct number of rounds!");
					a.show();	
				}
			}
		});
		
		ipInfo.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(arg2.contentEquals("")) {
					filledInIp = false;
				}
				else if(filledInPort == false)
				{
					filledInIp = true;
					joinGame.setDisable(true);
					createGame.setDisable(true);
				}
				else if(filledInPort == true) {
					joinGame.setDisable(false);
					createGame.setDisable(false);
				}
				else {
					filledInIp = true;
				}
			}
		});
		
		portInfo.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(arg2.contentEquals("")) {
					filledInPort = false;
				}
				else if(filledInIp == false) {
					filledInPort = true;
					joinGame.setDisable(true);
					createGame.setDisable(true);
				}
				else if (filledInIp == true) {
					joinGame.setDisable(false);
					createGame.setDisable(false);
				}
				else {
					filledInPort = true;
				}
			}
		});
		
		createGame.setOnAction((ActionEvent event)->{ 
			if(warNameString == "") {  //War is not chosen
				a.setAlertType(AlertType.ERROR);
				a.setTitle("War was not selected");
				a.setContentText("Please select a war!");
				a.show();
				return;
			}
			if(numOfRoundsTextField.getText().equals("")) {
				numOfRounds = 5;//change to randomNumber
				if(numPlayers.getValue() == "Multi-Player") {
					numOfPlayers = 2;  //Set player count
					if(warNameString == "WWI") {
						makeGameScreen("ww1 gamePlay.png", primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
					if(warNameString == "WWII") {
						makeGameScreen("ww2 gp.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
					if(warNameString == "Civil War") {
						makeGameScreen("US civil war.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
				}
				else if (numPlayers.getValue() == "Single-Player") {
					numOfPlayers = 1;  //Set player count					
					if(warNameString == "WWI") {	
						makeGameScreen("ww1 gamePlay.png",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
						
					}
					if(warNameString == "WWII") {
						makeGameScreen("ww2 gp.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
					if(warNameString == "Civil War") {
						makeGameScreen("US civil war.jpg",primaryStage);
						primaryStage.setScene(sceneMap.get("gameScene"));
					}
				}
				else {
					a.setAlertType(AlertType.ERROR);
					a.setTitle("Number of players was not selected");
					a.setContentText("Please select number of Players!");
					a.show();		         
				}
			}
			else {
				try {
					numOfRounds = Integer.parseInt(numOfRoundsTextField.getText());
					//System.out.println("The number of rounds input is: " + numOfRounds);
					if(numOfRounds < 2|| numOfRounds >= 101){
						a.setAlertType(AlertType.ERROR);
						a.setTitle("Invalid number of rounds");
						a.setContentText("Input a number of rounds between 2 and 100");
						a.show();
						return;
					}
					
					if(numPlayers.getValue() == "Multi-Player") {
						numOfPlayers = 2;					
						if(warNameString == "WWI") {	
							makeGameScreen("ww1 gamePlay.png",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
							
						}
						if(warNameString == "WWII") {
							makeGameScreen("ww2 gp.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						if(warNameString == "Civil War") {
							makeGameScreen("US civil war.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						//myStg.setScene(sceneMap.get("playGame"));
					}
					else if (numPlayers.getValue() == "Single-Player") {
						numOfPlayers = 1;					
						if(warNameString == "WWI") {	
							makeGameScreen("ww1 gamePlay.png",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
							
						}
						if(warNameString == "WWII") {
							makeGameScreen("ww2 gp.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						if(warNameString == "Civil War") {
							makeGameScreen("US civil war.jpg",primaryStage);
							primaryStage.setScene(sceneMap.get("gameScene"));
						}
						//myStg.setScene(sceneMap.get("playGame"));
					}
					else {
						a.setAlertType(AlertType.ERROR);
						a.setTitle("Number of players was not selected");
						a.setContentText("Please select number of Players!");
						a.show();		         
					}
				}
				catch(NumberFormatException e){
					a.setAlertType(AlertType.ERROR);
					a.setTitle("Could not convert number");
					a.setContentText("Please enter a correct number of rounds!");
					a.show();	
				}
			}
		});
		
		joinGame.setOnAction((ActionEvent event)->{
			if(ipInfo.getText().equals("") || portInfo.getText().equals("")) {  //join button clicked without valid inputs
				a.setAlertType(AlertType.ERROR);
				a.setTitle("Port info and ip info was blank");
				a.setContentText("Please fill in the ip info and port info");
				a.show();	
			}
			
			//Retrieve IP and port number from input 
			hostAddress = ipInfo.getText();
			try {
				portNumber = Integer.parseInt(portInfo.getText());
			    joiningGame = true;

				if(warNameString == "WWI") {
					makeGameScreen("ww1 gamePlay.png", primaryStage);
					primaryStage.setScene(sceneMap.get("gameScene"));
				}
				if(warNameString == "WWII") {
					makeGameScreen("ww2 gp.jpg",primaryStage);
					primaryStage.setScene(sceneMap.get("gameScene"));
				}
				if(warNameString == "Civil War") {
					makeGameScreen("US civil war.jpg",primaryStage);
					primaryStage.setScene(sceneMap.get("gameScene"));
				}
				
			} catch(Exception e2) {
				a.setAlertType(AlertType.ERROR);
				a.setTitle("Port number was invalid");
				a.setContentText("Please enter a correct port number!");
				a.show();	
			}
		});
		
		/*
		 * //Add client window pop-up created by join button EventHandler<ActionEvent>
		 * eve = new EventHandler<>() { public void handle(ActionEvent e) { } };
		 * joinGame.setOnMouseReleased(eve);
		 */
			
		quitGame.setOnAction((ActionEvent event)->{
			Platform.exit();
			new FadeOut(mainPage).play();
		});

		scene = new Scene(mainPage, 650, 600);
		primaryStage.setTitle("Welcome to History Arcade");
		sceneMap.put("welcome", scene);
		
		primaryStage.setScene(sceneMap.get("welcome"));
		primaryStage.show();
		
		// set animations using AnimateFX library
		new FadeIn(mainPage).play();
		new BounceIn(welcomeText).play();
		return;
	}
	
	
	/**
	 * Sends quit message before quitting the program
	 */
	@Override
	public void stop() throws Exception {
	    //System.out.println("Called stop().");
	    if(!gameOver && client != null) {
			client.sendOutgoingMsg("quit");
			client.closeClient();
	    }
	    System.exit(0);
	}
	
	/**
	 * Restarts the timer for player input
	 */
	public void setTimer() {
	    timer = new Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	            if(STARTTIME > 5)
	            {
	            	timerLabel.setStyle("-fx-font-size: 4em;");
	                Platform.runLater(() -> timerLabel.setText("" + STARTTIME));
	                ////System.out.println(STARTTIME);
	                STARTTIME--;
	            }
	            else if(STARTTIME > 0) {
	            	timerLabel.setStyle("-fx-font-size: 5em;");
	            	new Pulse(timerLabel).play();
	            	Platform.runLater(() -> timerLabel.setText("" + STARTTIME));
	                ////System.out.println(STARTTIME);
	                STARTTIME--;
	            }
	            else {
	            	new RollOut(timerLabel).play();
	            	timer.cancel();
	            	timer = new Timer();
	        	    timer.scheduleAtFixedRate(new TimerTask() {
	        	        public void run() {
			            	STARTTIME = 15;
			            	timerLabel.setStyle("-fx-font-size: 4em;");
			                timer.cancel();  //Ran out of time! send a random selection
			            	new RollIn(timerLabel).play();
			                client.sendOutgoingMsg("No Answer Received");
			            	submitButton.setDisable(true);
	        	        }
	        	    }, 1000, 1000);
	            }
	        }
	    }, 1000,1000);
	}
	
	/**
	 * @param warString the war name of the map to be displayed
	 * @param primaryStage the stage for the 
	 */
	public void makeGameScreen(String warString, Stage primaryStage) {	
		try {
			if(!joiningGame) {  //Player is initializing the game
				connectToServer("127.0.0.1", 5555);
				//Send GameServer map type and player count
				client.sendOutgoingMsg(Integer.toString(numOfPlayers));
				client.sendOutgoingMsg(Integer.toString(numOfRounds));
				client.sendOutgoingMsg(warNameString);
			}
			else {  //Player is joining
				connectToServer(hostAddress, portNumber);			
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		Screen screen = Screen.getPrimary();
	    Rectangle2D bounds = screen.getVisualBounds();
		primaryStage.setWidth(bounds.getWidth());
	    primaryStage.setHeight(bounds.getHeight());
	    primaryStage.setMaximized(true);
		
		timerLabel.setId("timerLabel");
		timerText.setId("timerText");
       // VBox timerVbox = new VBox(5, timerText, timerLabel);
        
		Image warImage= new Image(warString);
		ImageView warView = new ImageView(warImage);
		//warView.setPreserveRatio(true);
		warView.setFitHeight(575);
		warView.setFitWidth(675);
		warView.relocate(50, 50);
		Scene s;
		Pane mainGamePane = new Pane();
	    //mainGamePane.getStylesheets().add("CSSStyler.css");
		submitButton = new Button("Submit Answer");
	    submitButton.setOnAction((ActionEvent event)->{
	    	STARTTIME = 15;
	    	timer.cancel();
	    	try {
				client.sendOutgoingMsg(inputArea.getText());
			} catch (Exception e) {
				//System.out.println("No Connection");
				e.printStackTrace();
			}
	    	submitButton.setDisable(true);
	    	inputArea.clear();
	    	inputArea.setPromptText("Enter Your Answer Here:");
		});
	    submitButton.setDisable(true);
	    
	    //Not sure what's going on here, this should be also telling server to quit
	    quitButton = new Button("Quit");
	    quitButton.setId("quitButton");
	    quitButton.setOnAction((ActionEvent event)->{
	    	inputArea.setText("quit");
	    	if(!gameOver) {
	    		client.sendOutgoingMsg("quit");
				gameOver = true;  //Ensures other actions will not try using client socket
				client.closeClient();
	    	}
	    	primaryStage.setMaximized(false);
	    	primaryStage.setTitle("Welcome to History Arcade");
			primaryStage.setScene(sceneMap.get("welcome"));
			mainGamePane.getChildren().clear();
	    });
	   
	    updatePoints(playerPointsVbox, "0", "0");  //Set score board values to zero
	    
	    //playerPointsVbox.getChildren().addAll( scoreText,playerOneP,playerTwoP);
	    playerPointsVbox.relocate(primaryStage.getWidth() - 600, 50);
	    timerVbox.relocate(primaryStage.getWidth() - 250, 65);
	    
	    outputArea = new TextArea();
		outputArea.setEditable(false);
		outputArea.setPrefHeight(425);
		outputArea.setPrefWidth(550);
		outputArea.setWrapText(true);

		outputBoxAndTitle= new VBox(5, outputArea);
		outputBoxAndTitle.relocate(primaryStage.getWidth() - 600, playerPointsVbox.getHeight() + 200);
		
	 	inputArea = new TextField();
	 	inputArea.setPrefWidth(450);
	 	inputArea.setPromptText("Enter Your Answer Here:");
	 	inputAndSubmitBox = new HBox(5, inputArea,submitButton);
	 	inputAndSubmitBox.relocate(primaryStage.getWidth() - 600, primaryStage.getHeight() - 125);
	 	quitButton.relocate(primaryStage.getWidth() - 145, primaryStage.getHeight() - 80);
	 	
	    inputArea.setOnKeyPressed((event)->{
	    	if(event.getCode() == KeyCode.ENTER) {
		    	STARTTIME = 15;
		    	timer.cancel();
		    	try {
					client.sendOutgoingMsg(inputArea.getText());
				} catch (Exception e) {
					//System.out.println("No Connection");
					e.printStackTrace();
				}
		    	submitButton.setDisable(true);
		    	inputArea.clear();
		    	inputArea.setPromptText("Enter Your Answer Here:");
	    	}
		});
	    submitButton.setDisable(true);
	 	
		mainGamePane.getChildren().addAll(warView, timerVbox);
		mainGamePane.setId("mainGamePane");
		StackPane stackPane = new StackPane();
		playerPane = new Pane();
		
		playerPane.getChildren().addAll(outputBoxAndTitle, quitButton, inputAndSubmitBox, playerPointsVbox);
		stackPane.getChildren().addAll( mainGamePane,playerPane);
		new FadeIn(stackPane).play();
		stackPane.getStylesheets().add("CSSStyler.css");
		s = new Scene(stackPane);
		sceneMap.put("gameScene",s);
		
		primaryStage.setTitle("History Arcade");		
		primaryStage.setScene(sceneMap.get("gameScene"));
		primaryStage.show();
		
		//set animations
		new BounceInDown(warView).play();
		new SlideInDown(outputBoxAndTitle).play();
		new SlideInDown(inputAndSubmitBox).play();
		new SlideInDown(quitButton).play();
		new SlideInDown(playerPointsVbox).play();
		
		//Thread that will run all the UI elements
		new Thread(()-> {			
			while (!gameOver) {  //Stop queuing actions if game over
				String str;
				if(!gameOver)
					str = client.getIncomingMsg();
				else
					str = "QUIT";
				Platform.runLater(()->{
					//System.out.println("Action being performed on msg: " + str);  //Log string being observed
					if(!gameOver) {  //Only respond to output if game isn't over
						if(str.equals("QUIT")) {  //Check if it's a quit command
							gameOver = true;
							client.closeClient();  //Deal with this later
						}
						else if(str.equals("INPUT")) {  //Check if it's requesting input
							submitButton.setDisable(false);
							//System.out.println("Received question.");
							if(timer != null) {
								timer.cancel();
							}
							setTimer();
						}
						else if(str.contains("SCORES")) {  //Gets player scores and updates score board
							//System.out.println("Starting to collect scores.");
							Scanner sc = new Scanner(str.substring(6));   //Store info after the SCORES keyword
							String pts1 = sc.next();  //Pull information from string
							String pts2 = sc.next();
							sc.close();  //Close scanner now that it's no longer needed
							updatePoints(playerPointsVbox, pts1, pts2);
							new Pulse(playerPointsVbox).play();
						}
						else if(str.contains("POSITION")) {  //Player icon location updates
							System.out.print("War string is:" + warString);
							if(warNameString.equals("WWI")) {
								//Handle updating map position
								if(str.contains("POSITION Player 1 ")) {  //If informing of player 1 position update
									playerOneLocationString = str.substring(18, str.length());
									//System.out.println("P1 location is: " + playerOneLocationString);
									playerOneArrayList = getLocationCoordinates(playerOneLocationString);
									updatePlayerLocation("Player1", playerOneArrayList);
								}
								else if(str.contains("POSITION Player 2 ")) {  //If informing of player 2 position update
									playerTwoLocationString = str.substring(18, str.length());
									//System.out.println("P2 location is: " + playerTwoLocationString);
									playerTwoArrayList = getLocationCoordinates(playerTwoLocationString);
									updatePlayerLocation("Player2", playerTwoArrayList);
								}
								else if(str.contains("POSITION Glados ")) {  //If informing of NPC position update
									playerTwoLocationString = str.substring(16, str.length());
									//System.out.println("Glados location is: " + playerTwoLocationString);
									playerTwoArrayList = getLocationCoordinates(playerTwoLocationString);
									updatePlayerLocation("Player2", playerTwoArrayList);
								}
							}
							else if (warNameString.equals("WWII")) {
								//Handle updating map position
								if(str.contains("POSITION Player 1 ")) {  //If informing of player 1 position update
									playerOneLocationString = str.substring(18, str.length());
									//System.out.println("P1 location is: " + playerOneLocationString);
									playerOneArrayList = getLocationCoordinatesWWII(playerOneLocationString);
									updatePlayerLocation("Player1", playerOneArrayList);
								}
								else if(str.contains("POSITION Player 2 ")) {  //If informing of player 2 position update
									playerTwoLocationString = str.substring(18, str.length());
									//System.out.println("P2 location is: " + playerTwoLocationString);
									playerTwoArrayList = getLocationCoordinatesWWII(playerTwoLocationString);
									updatePlayerLocation("Player2", playerTwoArrayList);
								}
								else if(str.contains("POSITION Glados ")) {  //If informing of NPC position update
									playerTwoLocationString = str.substring(16, str.length());
									//System.out.println("Glados location is: " + playerTwoLocationString);
									playerTwoArrayList = getLocationCoordinatesWWII(playerTwoLocationString);
									updatePlayerLocation("Player2", playerTwoArrayList);
								}
							}
						}
						else {  //Display messages received
							//System.out.println("Displaying msg: " + str);
							outputArea.appendText(str + "\n");
							outputArea.selectPositionCaret(outputArea.getLength());
						}
					}
				});
			}
		}).start();
	}
	
	/**
	 * @param player the string telling which player has been moved
	 * @param locationArrayList the array holding the coordinates to be processed for placing player icon
	 */
	public void updatePlayerLocation(String player, ArrayList<Integer> locationArrayList) {
		//System.out.println("Reached beginning of updatePlayerLocation()");
		
		playerPane.getChildren().clear();
		playerTwoView.setPreserveRatio(true);
		playerTwoView.setFitHeight(50);
		playerOneView.setPreserveRatio(true);
		playerOneView.setFitHeight(50);
		
		//System.out.println("Reached middle of updatePlayerLocation()");
		
		if(player.equals("Player1")) {
			//System.out.println("Reached case1 of updatePlayerLocation()");
			playerOneView.relocate(locationArrayList.get(0), locationArrayList.get(1));
			new Pulse(playerOneView).play();
			if(playerTwoArrayList.size() < 2) {
				//System.out.println("Reached case1-1 of updatePlayerLocation()");
				playerPane.getChildren().addAll(outputBoxAndTitle, quitButton, inputAndSubmitBox, playerPointsVbox, playerOneView);
			}
			else {
				//System.out.println("Reached case1-2 of updatePlayerLocation()");
				playerPane.getChildren().addAll(outputBoxAndTitle, quitButton, inputAndSubmitBox, playerPointsVbox, playerOneView, playerTwoView);
			}
		}
		else if(player.equals("Player2")) {
			//System.out.println("Reached case2 of updatePlayerLocation()");
			playerTwoView.relocate(locationArrayList.get(0), locationArrayList.get(1));
			new Pulse(playerTwoView).play();
			if(playerOneArrayList.size() < 2) {
				////System.out.println("Reached case2-1 of updatePlayerLocation()");
				playerPane.getChildren().addAll(outputBoxAndTitle, quitButton, inputAndSubmitBox, playerPointsVbox, playerTwoView);
			}
			else {
				//System.out.println("Reached case2-2 of updatePlayerLocation()");
				playerPane.getChildren().addAll(outputBoxAndTitle, quitButton, inputAndSubmitBox, playerPointsVbox, playerOneView, playerTwoView);
			}
		}
		
		//System.out.println("Reached end of updatePlayerLocation()");
	}
	
	/**
	 * @param location the name of the location a player is at in WWII map
	 */
	public ArrayList<Integer> getLocationCoordinatesWWII(String location) {
        //System.out.println("Reached beginning of getLocationCoords()");
        ArrayList<Integer> coorIntegers = new ArrayList<Integer>();
        if(location.equals("Portugal")) {
            coorIntegers.add(0, 100);
            coorIntegers.add(1, 400);
            return coorIntegers;
        }
        else if(location.equals("Spain")) {
            coorIntegers.add(0, 150);
            coorIntegers.add(1, 420);
            return coorIntegers;
        }
        else if(location.equals("France")) {
            coorIntegers.add(0, 200);
            coorIntegers.add(1, 370);
            return coorIntegers;
        }
        else if(location.equals("Great Britain")) {
            coorIntegers.add(0, 200);
            coorIntegers.add(1, 270);
            return coorIntegers;
        }
        else if(location.equals("Ireland")) {
            coorIntegers.add(0, 150);
            coorIntegers.add(1, 250);
            return coorIntegers;
        }
        else if(location.equals("Belgium")) {
            coorIntegers.add(0, 240);
            coorIntegers.add(1, 285);
            return coorIntegers;
        }
        else if(location.equals("Netherlands")) {
            coorIntegers.add(0, 260);
            coorIntegers.add(1, 260);
            return coorIntegers;
        }
        else if(location.equals("Luxembourg")) {
            coorIntegers.add(0, 250);
            coorIntegers.add(1, 300);
            return coorIntegers;
        }
        else if(location.equals("Switzerland")) {
            coorIntegers.add(0, 250);
            coorIntegers.add(1, 340);
            return coorIntegers;
        }
        else if(location.equals("Italy")) {
            coorIntegers.add(0, 280);
            coorIntegers.add(1, 380);
            return coorIntegers;
        }
        else if(location.equals("Austria-Hungary")) {
            coorIntegers.add(0, 295);
            coorIntegers.add(1, 350);
            return coorIntegers;
        }
        else if(location.equals("Germany")) {
            coorIntegers.add(0, 290);
            coorIntegers.add(1, 300);
            return coorIntegers;
        }
        else if(location.equals("Denmark")) {
            coorIntegers.add(0, 290);
            coorIntegers.add(1, 250);
            return coorIntegers;
        }
        else if(location.equals("Norway")) {
            coorIntegers.add(0, 290);
            coorIntegers.add(1, 180);
            return coorIntegers;
        }
        else if(location.equals("Sweden")) {
            coorIntegers.add(0, 335);
            coorIntegers.add(1, 180);
            return coorIntegers;
        }
        else if(location.equals("Czechoslovakia")) {
            coorIntegers.add(0, 325);
            coorIntegers.add(1, 330);
            return coorIntegers;
        }
        else if(location.equals("Poland")) {
            coorIntegers.add(0, 365);
            coorIntegers.add(1, 300);
            return coorIntegers;
        }
        else if(location.equals("East Prussia")) {
            coorIntegers.add(0, 365);
            coorIntegers.add(1, 270);
            return coorIntegers;
        }
        else if(location.equals("Lithuania")) {
            coorIntegers.add(0, 390);
            coorIntegers.add(1, 255);
            return coorIntegers;
        }
        else if(location.equals("Latvia")) {
            coorIntegers.add(0, 410);
            coorIntegers.add(1, 235);
            return coorIntegers;
        }
        else if(location.equals("Estonia")) {
            coorIntegers.add(0, 410);
            coorIntegers.add(1, 210);
            return coorIntegers;
        }
        else if(location.equals("Finland")) {
            coorIntegers.add(0, 400);
            coorIntegers.add(1, 160);
            return coorIntegers;
        }
        else if(location.equals("USSR")) {
            coorIntegers.add(0, 450);
            coorIntegers.add(1, 240);
            return coorIntegers;
        }
        else if(location.equals("Romania")) {
            coorIntegers.add(0, 400);
            coorIntegers.add(1, 350);
            return coorIntegers;
        }
        else if(location.equals("Hungary")) {
            coorIntegers.add(0, 360);
            coorIntegers.add(1, 350);
            return coorIntegers;
        }
        else if(location.equals("Yugoslavia")) {
            coorIntegers.add(0, 365);
            coorIntegers.add(1, 400);
            return coorIntegers;
        }
        else if(location.equals("Albania")) {
            coorIntegers.add(0, 365);
            coorIntegers.add(1, 425);
            return coorIntegers;
        }
        else if(location.equals("Greece")) {
            coorIntegers.add(0, 375);
            coorIntegers.add(1, 460);
            return coorIntegers;
        }
        else if(location.equals("Bulgaria")) {
            coorIntegers.add(0, 400);
            coorIntegers.add(1, 405);
            return coorIntegers;
        }
        else if(location.equals("Turkey")) {
            coorIntegers.add(0, 440);
            coorIntegers.add(1, 450);
            return coorIntegers;
        }
        //System.out.println("Reached end of getLocationCoords()");
        coorIntegers.add(0, 80);
        coorIntegers.add(1, 230);
        return coorIntegers;
    }
	
	/**
	 * @param location the name of the location a player is at in WWI map
	 */
	public ArrayList<Integer> getLocationCoordinates(String location) {
		//System.out.println("Reached beginning of getLocationCoords()");
		ArrayList<Integer> coorIntegers = new ArrayList<Integer>();
		
		//System.out.println("Reached beginning of if-else cases");
		if(location.equals("Portugal")) {
			coorIntegers.add(0, 70);
			coorIntegers.add(1, 365);
			return coorIntegers;
		}
		else if(location.equals("Spain")) {
			coorIntegers.add(0, 130);
			coorIntegers.add(1, 370);
			return coorIntegers;
		}
		else if(location.equals("France")) {
			coorIntegers.add(0, 205);
			coorIntegers.add(1, 310);
			return coorIntegers;
		}
		else if(location.equals("United Kingdom")) {
			coorIntegers.add(0, 190);
			coorIntegers.add(1, 190);
			return coorIntegers;
		}
		else if(location.equals("Belgium")) {
			coorIntegers.add(0, 235);
			coorIntegers.add(1, 240);//
			return coorIntegers;
		}
		else if(location.equals("Netherlands")) {
			coorIntegers.add(0, 255);
			coorIntegers.add(1, 230);//
			return coorIntegers;
		}
		else if(location.equals("Luxembourg")) {
			coorIntegers.add(0, 245);
			coorIntegers.add(1, 275);//
			return coorIntegers;
		}
		else if(location.equals("Switzerland")) {
			coorIntegers.add(0, 260);
			coorIntegers.add(1, 305);//
			return coorIntegers;
		}
		else if(location.equals("Germany")) {
			coorIntegers.add(0, 300);//
			coorIntegers.add(1, 235);
			return coorIntegers;
		}
		else if(location.equals("Italy")) {
			coorIntegers.add(0, 285);
			coorIntegers.add(1, 375);//
			return coorIntegers;
		}
		else if(location.equals("Austria-Hungary")) {
			coorIntegers.add(0, 345);//
			coorIntegers.add(1, 300);
			return coorIntegers;
		}
		else if(location.equals("Denmark")) {
			coorIntegers.add(0, 290);
			coorIntegers.add(1, 200);//
			return coorIntegers;
		}
		else if(location.equals("Norway")) {
			coorIntegers.add(0, 294);
			coorIntegers.add(1, 105);//
			return coorIntegers;
		}
		else if(location.equals("Sweden")) {
			coorIntegers.add(0, 335);
			coorIntegers.add(1, 145);
			return coorIntegers;
		}
		else if(location.equals("Russia")) {
			coorIntegers.add(0, 545);//
			coorIntegers.add(1, 170);
			return coorIntegers;
		}
		else if(location.equals("Romania")) {
			coorIntegers.add(0, 425);
			coorIntegers.add(1, 350);//
			return coorIntegers;
		}
		else if(location.equals("Serbia")) {
			coorIntegers.add(0, 380);
			coorIntegers.add(1, 385);//
			return coorIntegers;
		}
		else if(location.equals("Montenegro")) {
			coorIntegers.add(0, 345);
			coorIntegers.add(1, 375);//
			return coorIntegers;
		}
		else if(location.equals("Albania")) {
			coorIntegers.add(0, 365);
			coorIntegers.add(1, 410);//
			return coorIntegers;
		}
		else if(location.equals("Bulgaria")) {
			coorIntegers.add(0, 425);
			coorIntegers.add(1, 395);//
			return coorIntegers;
		}
		else if(location.equals("Greece")) {
			coorIntegers.add(0, 385);
			coorIntegers.add(1, 455);
			return coorIntegers;
		}
		else if(location.equals("Turkey")) {
			coorIntegers.add(0, 495);
			coorIntegers.add(1, 445);//
			return coorIntegers;
		}

		//System.out.println("Reached end of getLocationCoords()");
		coorIntegers.add(0, 80);
		coorIntegers.add(1, 230);
		return coorIntegers;
	}
	
	
	/**
	 * @param playersPoints the VBox holding the score board information
	 * @param p1Points the amount of points for player1
	 * @param p2Points the amounts of points for player2
	 */
	public void updatePoints(VBox playersPoints, String p1Points, String p2Points) {
	    Label scoreText = new Label("ScoreBoard:");
	    scoreText.setId("scoreBoardTitle");
	    player1points.setText("Player 1 Points: " + p1Points);  //Placeholder for now will have player data later
	    player2points.setText("Player 2 Points: " + p2Points);  //Placeholder for now will have player data later
	    playersPoints.setId("playersPoints");
		playersPoints.getChildren().clear();
		playersPoints.getChildren().addAll(scoreText, player1points, player2points);
	}
	
	
	/**
	 * @param ip the host address to connect to
	 * @param portNum the port number of the host for connecting
	 */
	public static void connectToServer(String ip, int portNum) throws IOException {
		//System.out.println("Reached beginning of connectToServer()");
		client = new GameClient(ip, portNum);  //Create game client to handle communication with server
		//System.out.println("Connected");
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}