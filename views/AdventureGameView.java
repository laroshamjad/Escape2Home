package views;

import AdventureModel.AdventureGame;
import Visitor.PlanetVisitor;
import javafx.scene.Node;
import javafx.scene.Parent;
import planet.cosmiccode.CosmicCode;
import planet.hiddenhaven.SpotTheDifferenceGameView;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.AccessibleRole;
import planet.memorymaze.MemoryGame;
import planet.hiddenhaven.SpotTheDifferenceGameView;
import planet.puzzlemania.PuzzleMania;

import javax.accessibility.AccessibleComponent;
import java.util.ArrayList;

import java.io.File;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * SHAREPOINT LINK: https://utoronto-my.sharepoint.com/:v:/g/personal/rhea_girdhar_mail_utoronto_ca/ET0c_rMAXsZFr7i8PQep4nUB0I_BuEEovQ104SN9DarhbQ?e=dWzSeG&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZyIsInJlZmVycmFsQXBwUGxhdGZvcm0iOiJXZWIiLCJyZWZlcnJhbE1vZGUiOiJ2aWV3In19
 * PASSWORD: No password required
 */
public class AdventureGameView {

    public AdventureGame model; //model of the game
    public Stage stage; //stage on which all is rendered
    public VBox textEntry = new VBox(); // NEW
    Button saveButton, loadButton, helpButton; //buttons
    Boolean helpToggle = false; //is help on display?

    public GridPane gridPane = new GridPane(); //to hold images and buttons
    GridPane startGridPane = new GridPane(); //to hold images and buttons
    public Label roomDescLabel = new Label(); //to hold room description and/or instructions
    public VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    public TextField inputTextField; //for user input
    VBox playerLives = new VBox(); //to hold player lives
    public MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing
    public boolean superHeart = true;

    public ArrayList<String> toolsAcquired = new ArrayList<String>(); //to hold the tools acquired so far

    VBox toolsInInventory = new VBox(); //holds the tools in the room
    GridPane introGridPane = new GridPane(); //to hold images and buttons

    public CosmicCode c = new CosmicCode(this); // a cosmic code planet

    public PuzzleMania p = new PuzzleMania(this); // a puzzle mania planet

    public SpotTheDifferenceGameView h = new SpotTheDifferenceGameView(this); // a hidden haven  planet

    public MemoryGame m = new MemoryGame(this); // a memory maze planet

    public String currentPlanet = ""; // the current planet the user is at

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        introScreen();
    }

    public void introScreen(){
        // setting up the stage
        this.stage.setTitle("Escape From Home");
        PauseTransition introPause = new PauseTransition(Duration.seconds(5));
        //Intro Screen
        String objRoomImage = "Images/introScreen.jpg";
        Image objImage = new Image(objRoomImage);
        Background curr = new Background(new BackgroundImage(objImage, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                true, true, true, false)));
        startGridPane.setBackground(curr);

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow(Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow(Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints(550);
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        startGridPane.getColumnConstraints().addAll(column1, column2, column1);
        startGridPane.getRowConstraints().addAll(row1, row2, row1 );

        Button start = new Button("Start Game");
        start.setFont(new Font("Arial", 30));
        start.setMinWidth(200);
        start.setAlignment(Pos.BOTTOM_CENTER);
        makeButtonAccessible(start, "Start Button", "This button starts the game.", "This button moves the introduction screen.");
        startGridPane.add(start, 2, 2, 1, 1);  // Add buttons

        // Render everything
        var scene = new Scene(startGridPane , 1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
        //add audio for accessibility to read the name of the game and instruct the user to click the start button
        String introAudio = "audio/introScreen.m4a";
        Media sound = new Media(new File(introAudio).toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);
        this.mediaPlayer.play();
        introPause.play();

        start.setOnMouseClicked(e ->{
            //call intro
            introStory();
            intiUI();
        });

    }
    /**
     * introStory
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    public void introStory(){
        PauseTransition intropause = new PauseTransition(Duration.seconds(5));
        intropause.setOnFinished((event) -> {
            ArrayList<Node> cell_1_1 = new ArrayList<>();
            for (javafx.scene.Node node : this.gridPane.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer columnIndex = GridPane.getColumnIndex(node);

                // add any node that is found at the center
                if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                    cell_1_1.add(node);
                }
            }
            // remove anything in the center of the gridpane
            this.gridPane.getChildren().removeAll(cell_1_1);
            displayChoice();
        });
        updateScene("", "Images/intro.png");
        String introaudio = "audio/introsound.m4a";
        this.roomDescLabel.setText("Amidst the voyage home, a malfunction crippled your spaceship. \n" +
                "Determined to repair it, you must embark on a planetary odyssey. \n" +
                "Each planet will present to you a unique challenge: games to conquer and tools to claim. "); //format the text to display
        Media sound = new Media(new File(introaudio).toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);
        this.mediaPlayer.play();
        intropause.play();

    }

    /**
     * Initialize the UI
     */
    public void intiUI() {
        startGridPane.getChildren().clear();
        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
//        objectsInRoom.setSpacing(10);
//        objectsInRoom.setAlignment(Pos.TOP_CENTER);
        playerLives.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550);
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, helpButton, loadButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("           Lives");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        Label invLabel =  new Label("Objects in Toolkit");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        //updateScene(""); //method displays an image and whatever text is supplied
        //updateItems(); //update items shows inventory and objects in rooms
        updateTools("");
        updateLives();
        // adding the text area and submit button to a VBox
        //VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

    }

    /**
     * updateTools
     * ________________________________
     * This method is called when the player wins a game on their planet.
     * This method should update the tools that the player has acquried so far on the screen.
     * The toolsAcquired VBox should contain images of each tool.
     * */
    public void updateTools(String s) {
        if (s.equals("iman") && (!this.toolsAcquired.contains("flashlight"))) {
            this.toolsAcquired.add("flashlight");
        }
        if (s.equals("sofia") && (!this.toolsAcquired.contains("helmet"))) {
            this.toolsAcquired.add("helmet");
        }
        if (s.equals("rhea") && (!this.toolsAcquired.contains("telescope"))) {
            this.toolsAcquired.add("telescope");
        }
        if (s.equals("larosh") && (!this.toolsAcquired.contains("wrench"))) {
            this.toolsAcquired.add("wrench");
        }
        toolsInInventory.getChildren().clear();
        int total = this.toolsAcquired.size();
        for (int i = 0; i < total; i++) {
            String r = "Games/TinyGame/Toolkit/" + this.toolsAcquired.get(i) + ".jpg";
            Image add = new Image(r);
            ImageView make = new ImageView(add);
            make.setFitWidth(100);
            make.setFitHeight(100);

            VBox ma = new VBox();
            Label l = new Label(this.toolsAcquired.get(i));
            l.setTextFill(Color.BLACK);
            l.setStyle("-fx-text-fill: black;");
            l.setAlignment(Pos.CENTER);
            ma.setAlignment(Pos.CENTER);
            ma.getChildren().addAll(make, l);

            Button a = new Button();
            a.setGraphic(ma);
            a.setId(this.toolsAcquired.get(i));
            a.setContentDisplay(ContentDisplay.TOP);
            makeButtonAccessible(a, this.toolsAcquired.get(i), "This" + this.toolsAcquired.get(i) + " is a tool for your spaceship", "This is one of the four tools you must collect to repair you spaceship");
            toolsInInventory.getChildren().add(a);
        }
        toolsInInventory.setAlignment(Pos.CENTER);
        toolsInInventory.setSpacing(10);
        ScrollPane sc0 = new ScrollPane(toolsInInventory);
        sc0.setPadding(new Insets(10));
        sc0.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        sc0.setFitToWidth(true);
        gridPane.add(sc0, 2,1);


    }

    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute 
     *
     * Your event handler should respond when users 
     * hits the ENTER or TAB KEY. If the user hits 
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped 
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus 
     * of the scene onto any other node in the scene 
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        this.inputTextField.setOnKeyPressed(keyEvent -> {
            //when user clicks enter
            if (keyEvent.getCode() == KeyCode.ENTER) {
                String userInput = inputTextField.getText().trim();
                submitEvent(userInput);
                this.inputTextField.clear();
                this.mediaPlayer.stop();
            }
            //when user click tab
            else if(keyEvent.getCode() == KeyCode.TAB){
                gridPane.requestFocus();
                this.inputTextField.clear();
            }

        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("Puzzle Mania")) {
            PlanetVisitor.visit(p, this);
        }
        if (text.equalsIgnoreCase("Memory Maze")) {
            PlanetVisitor.visit(m, this);
        }
        if (text.equalsIgnoreCase("Hidden Haven")) {
            PlanetVisitor.visit(h, this);
        } if (text.equalsIgnoreCase("Cosmic Code")) {
            PlanetVisitor.visit(c, this);
        }
        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            //articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }
//        //try to move!
//        String output = this.model.interpretAction(text); //process the command!
//
//        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
//            updateScene(output);
//            //updateItems();
//        } else if (output.equals("GAME OVER")) {
//            updateScene("");
//            //updateItems();
//            PauseTransition pause = new PauseTransition(Duration.seconds(10));
//            pause.setOnFinished(event -> {
//                Platform.exit();
//            });
//            pause.play();
//        } else if (output.equals("FORCED")) {
//            //write code here to handle "FORCED" events!
//            //Your code will need to display the image in the
//            //current room and pause, then transition to
//            //the forced room.
//            //update scene and items first
//            updateScene("");
//            //updateItems();
//            //pause for 5 seconds
//            PauseTransition pause = new PauseTransition(Duration.seconds(5));
//            pause.setOnFinished(event ->{
//                submitEvent("FORCED");
//                //update scene to forced room
//                updateScene(this.model.player.getCurrentRoom().getRoomDescription());
//            });
//            pause.play();
//        }
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the 
     * current room.
     */
    private void showCommands() {
        //get the current room and its commands
        String curr = this.model.getPlayer().getCurrentRoom().getCommands();
        //update the text to the current rooms commands
        roomDescLabel.setText("You can move in these directions:\n\n" + curr);
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     * 
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {

        getRoomImage(); //get the image of the current room
        this.roomDescLabel.setText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        //finally, articulate the description
        //if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }

    /**
     * updateScene
     * __________________________
     *
     * Show the image that imageView leads to, alongside a label underneath it with
     * the text from textToDisplay
     *
     * @param textToDisplay the text to display below the image.
     * @param imageView the path for the image.
     */
    public void updateScene(String textToDisplay, String imageView) {
        // create image and set the size
        Image roomImageFile = new Image(imageView);
        ImageView roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(500);
        roomImageView.setFitHeight(500);

        this.roomDescLabel.setText(textToDisplay); //format the text to display
        this.roomDescLabel.setWrapText(true);
        this.roomDescLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox roomPane = new VBox(10);
        roomPane.setAlignment(Pos.CENTER);
        roomPane.getChildren().addAll(roomImageView, this.roomDescLabel);
        roomPane.setStyle("-fx-background: #000000; -fx-background-color:transparent;");

        // add image to the center of the gridpane
        this.gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        //finally, articulate the description
        //if (textToDisplay == null || textToDisplay.isBlank()) this.articulateRoomDescription();
    }

    /**
     * displayVictory()
     * __________________________
     *
     * Display that the user has won the game.
     */
    public void displayVictory() {
        updateScene("CONGRATULATIONS! YOU HAVE WON THE GAME AND CAN RETURN BACK 2 HOME.", "Images/finalScreen/finalWin.jpg");
    }

    /**
     * displayDefeat()
     * __________________________
     *
     * Display that the user has lost the game.
     */
    public void displayDefeat() {
        // change background to black
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        updateScene("YOU HAVE LOST THE GAME AND ARE STUCK IN SPACE FOREVER.", "Images/finalScreen/finalLose.jpg");
    }

    /**
     * displayChoice()
     * __________________________
     *
     * Display the planets the player can go to. Allow them to enter into the textfield.
     */
    public void displayChoice() {
        // change background to black
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        // check if they have already completed all planets
        if (c.getResult() && p.getResult() && c.getResult() && h.getResult()) {
            displayVictory();
        } else {
            // show the text entry and allow user to go to the next planet
            this.textEntry.setVisible(true);
            updateScene("CHOOSE THE PLANET YOU WANT TO GO TO.", "Images/choosePlanet.png");
        }
    }

//    /**
//     * formatText
//     * __________________________
//     *
//     * Format text for display.
//     *
//     * @param textToDisplay the text to be formatted for display.
//     */
//    public void formatText(String textToDisplay) {
//        if (textToDisplay == null || textToDisplay.isBlank()) {
//            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
//            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
//            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
//            else roomDescLabel.setText(roomDesc);
//        } else roomDescLabel.setText(textToDisplay);
//        roomDescLabel.setStyle("-fx-text-fill: white;");
//        roomDescLabel.setFont(new Font("Arial", 16));
//        roomDescLabel.setAlignment(Pos.CENTER);
//    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place 
     * it in the roomImageView 
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";
        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateLives
     * _____________________________
     * This method should populate the players lives
     * The playerLives VBox should contain a collection of nodes (ImageViews)
     * Each node represents a player's life.
     */
    public void updateLives(){
        //clear the Vbox to redisplay the lives
        playerLives.getChildren().clear();
        //add super heart stuff
        if(superHeart){
            updateSuperHeart();
        }
        int currLives = this.model.getPlayer().getLives();
        //display all the lives the player current has
        for (int i = 0; i < currLives; i++) {
            //get the image of the object
            String objRoomImage = "Images/heart.jpeg";
            Image objImage = new Image(objRoomImage);
            ImageView objImageView = new ImageView(objImage);
            //display image specifications
            objImageView.setFitWidth(50);
            objImageView.setPreserveRatio(true);
            playerLives.getChildren().add(objImageView);
            //make lives appear on the screen
            ScrollPane scO = new ScrollPane(playerLives);
            scO.setPadding(new Insets(10));
            scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            scO.setFitToWidth(true);
            gridPane.add(scO,0,1);
        }
        //when player has no lives, we must remove the last life from the screen
        if (currLives == 0){
            playerLives.getChildren().clear();
            //make no lives appear on the screen
            ScrollPane scO = new ScrollPane(playerLives);
            scO.setPadding(new Insets(10));
            scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            scO.setFitToWidth(true);
            gridPane.add(scO,0,1);
        }
    }

    /**
     * updateSuperHeart()
     * _____________________________
     * This method adds a super heart.
     */
    public void updateSuperHeart(){
        if(this.superHeart){
            //get super heart image
            String heart = "Images/superHeart.png";
            Image superImage = new Image(heart);
            ImageView heartImage = new ImageView(superImage);
            //display image specifications
            heartImage.setFitWidth(50);
            heartImage.setPreserveRatio(true);
            playerLives.getChildren().add(heartImage);
        }
        else{
            // this will do everything in update lives but not add the super heart at the top
            updateLives();
        }

    }

    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        //remove whatever is in the cell
        gridPane.getChildren().remove(1, 1);
        if (!helpToggle){
            //create a label that will hold all the instructions
            Label helpLabel = new Label(this.model.getInstructions());
            helpLabel.setWrapText(true);
            helpLabel.setPrefWidth(600);
            helpLabel.setFont(new Font("Times New Roman", 16));
            //create scroll pane and set the content to the label holding the instructions
            ScrollPane help = new ScrollPane();
            help.setContent(helpLabel);
            help.setStyle("-fx-background: #000000;");
            //add it to the appropriate cell
            gridPane.add(help,1,1);
            helpToggle = true;
            String musicFile = "audio/instructionssound.m4a";
            Media sound = new Media(new File(musicFile).toURI().toString());
            this.mediaPlayer = new MediaPlayer(sound);
            this.mediaPlayer.play();
        }
        else{
            //redraw the scene
            updateScene(roomDescLabel.getText());
            helpToggle = false;
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }


    /**
     * This method articulates Room Descriptions
     */
//    public void articulateRoomDescription() {
//        String musicFile;
//        String adventureName = this.model.getDirectoryName();
//        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();
//
//        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
//        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
//        musicFile = musicFile.replace(" ","-");
//
//        Media sound = new Media(new File(musicFile).toURI().toString());
//
//        mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.play();
//        mediaPlaying = true;
//
//    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }
}
