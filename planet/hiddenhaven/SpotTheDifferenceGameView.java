package planet.hiddenhaven;

import javafx.animation.PauseTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Duration;
import views.AdventureGameView;
import javafx.scene.transform.Scale;
import planet.Game;

import java.io.File;
import java.util.Objects;
import java.util.ArrayList;


public class SpotTheDifferenceGameView implements Game {
    AdventureGameView adventureGameView;
    GridPane gamePane = new GridPane();
    Boolean result;
    public Boolean isVisited;
    Boolean roundOne;
    Boolean showInstructions;
    Integer gameCounter;
    Integer loses;
    ArrayList<String> gameImages;

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public SpotTheDifferenceGameView (AdventureGameView game) {
        this.adventureGameView = game;
        this.result = false;
        this.gameCounter = 0;
        this.loses = 0;
        this.isVisited = false;
        this.roundOne = true;
        this.showInstructions = true;
        this.gameImages = new ArrayList<>();
        this.gameImages.add("ship1");
        this.gameImages.add("ship2");
        this.gameImages.add("alien1");
        this.gameImages.add("alien2");
        this.gameImages.add("star1");
        this.gameImages.add("star2");
    }
    /**
     * play
     * __________________________
     * plays the audio of the instructions for the player.
     */
    public void play() {
        String musicFile = "audio/Welcome to Hidden Ha.m4a";
        Media sound = new Media(new File(musicFile).toURI().toString());
        this.adventureGameView.mediaPlayer = new MediaPlayer(sound);
        this.adventureGameView.mediaPlayer.play();
    }

    /**
     * removeMiddle
     * _________________
     * removes whatever is in the gridPane at 1, 1
     */
    public void removeMiddle(){
        //clear anything that is in cell 1, 1 in the gridPane
        ArrayList<Node> cell_1_1 = new ArrayList<>();
        for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                cell_1_1.add(node);
            }
        }
        this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
    }
    /**
     * instructions
     * _________________
     * displays the mini-game instructions to the user
     */
    public void instructions(){
        //need to display the instructions to the user on how to play the mini-game
        String instr = "You see a tool that can help fix the rocket in the far distance. \n" +
                "In order to reach it, you must conquer a challenge." +
                "\n\nHOW TO PLAY THE GAME: \nYou will be presented with a grid that contains several images that are" +
                "the same. However, there will be one image that is different. Select the different image by clicking" +
                        "on it. There wil be 3 rounds. Each time you select 3 wrong images, you will lose a life. \nSO BE CAREFUL!";
        this.adventureGameView.roomDescLabel.setFont(new Font("Arial", 18));
        this.adventureGameView.roomDescLabel.setText(instr);
        Button b = new Button("Hear Audio");
        b.setPrefSize(130,50);
        b.setFont(new Font("Arial", 16));
        b.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        b.setId("instruct");
        b.setOnAction(event -> {
            play();
        });
        VBox roomPane = new VBox();
        roomPane.getChildren().addAll(this.adventureGameView.roomDescLabel, b);
        //VBox roomPane = new VBox(this.adventureGameView.roomDescLabel);
        roomPane.setPadding(new Insets(20));
        roomPane.setAlignment(Pos.CENTER);
        roomPane.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        ScrollPane n = new ScrollPane(roomPane);
        n.setPadding(new Insets(10));
        n.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        n.setFitToWidth(true);
        //add the instructions to the grid pane
        this.adventureGameView.gridPane.add(n,1, 1);
//        String introAudio = "audio/Welcome to Hidden Ha.m4a";
//        Media sound = new Media(new File(introAudio).toURI().toString());
//        this.adventureGameView.mediaPlayer = new MediaPlayer(sound);
//        this.adventureGameView.mediaPlayer.play();
    }
    /**
     * startGame
     * _________________
     * displays the game to the user and makes if functional
     */
    public void startGame() {
        this.isVisited = true;
        removeMiddle();
        this.adventureGameView.textEntry.setVisible(false);
        //there are 3 different rounds of this mini-game
        String objRoomImage = "Images/HiddenHaven/background.png";
        Image objImage = new Image(objRoomImage);
        BackgroundImage curr = new BackgroundImage(objImage, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                false, false, true, true));
        this.adventureGameView.gridPane.setBackground(new Background(curr));
        //call method to display instructions
        if (this.showInstructions) {
            instructions();
        }
        //wait for a second and then start the game
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished((event) -> {

            this.showInstructions = false;
            //there are three rounds to the game
            updateResult();
        });
        pause.play();
        this.adventureGameView.updateTools("");
    }
    /**
     * createGrid
     * _________________
     * creates a grid for the functionality of the game
     */
    public void createGrid(){
        removeMiddle();
        //clear the gamePane and set it up
        gamePane.getChildren().clear();
        // GridPane, anyone?
        gamePane.setPadding(new Insets(20));
        gamePane.setHgap(0);
        gamePane.setVgap(0);

        //columnn contraints
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.CENTER);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.CENTER);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setHalignment(HPos.CENTER);
        ColumnConstraints column4 = new ColumnConstraints();
        column4.setHalignment(HPos.CENTER);
        ColumnConstraints column5 = new ColumnConstraints();
        column5.setHalignment(HPos.CENTER);


        // Row constraints
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setVgrow(Priority.SOMETIMES);
        RowConstraints row2 = new RowConstraints();
        row2.setValignment(VPos.CENTER);
        row2.setVgrow(Priority.SOMETIMES);
        RowConstraints row3 = new RowConstraints();
        row3.setValignment(VPos.CENTER);
        row3.setVgrow(Priority.SOMETIMES);
        RowConstraints row4 = new RowConstraints();
        row4.setValignment(VPos.CENTER);
        row4.setVgrow(Priority.SOMETIMES);
        RowConstraints row5= new RowConstraints();
        row5.setValignment(VPos.CENTER);
        row5.setVgrow(Priority.SOMETIMES);

        //add rows and columns to the gridPane
        gamePane.getColumnConstraints().addAll(column1, column2, column3, column4, column5);
        gamePane.getRowConstraints().addAll(row1, row2, row3, row4, row5);
    }
    /**
     * game
     * _________________
     * add the buttons with images to the grib for the user to select and interact with the game
     */
    public void game(String image1, String image2){
        //declare attributes
        //Arraylist will hold all the buttons being displayed on the screen
        ArrayList<Button> colorDisplay= new ArrayList<>();
        String colorImageLoc;
        Image colorImage;
        ImageView colorImageView;
        //get random i and j values so the different color is placed randomly on the grid
        int i_val = (int) ((Math.random() * 5) + 0);
        int j_val = (int) ((Math.random() * 5) + 0);
        int counter = 1;
        String diffButton = "";

        //Add the current games colors to each of the buttons
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++) {
                //assigns the different color to the button at i,j
                if (i == i_val && j == j_val){
                    colorImageLoc = "planet/hiddenhaven/Images/" + image2 + ".jpg";
                    diffButton = "blue" + counter;
                }
                //assigns the regular color to the rest of the buttons
                else{
                    colorImageLoc = "planet/hiddenhaven/Images/" + image1 + ".jpg";
                }
                colorImage = new Image(colorImageLoc);
                colorImageView = new ImageView(colorImage);
                //display image specifications
                colorImageView.setFitWidth(105);
                colorImageView.setFitHeight(100);
                colorImageView.setPreserveRatio(false);
                Button curr = new Button();
                Scale modify = new Scale(1.0, 1.0);
                curr.getTransforms().add(modify);
                curr.setOnMouseEntered(keyEvent ->{
                    modify.setX(1.1);
                    modify.setY(1.1);
                });
                curr.setOnMouseExited(keyEvent ->{
                    modify.setX(1.0);
                    modify.setY(1.0);
                });
                curr.setGraphic(colorImageView);
                curr.setStyle("-fx-background: #000000; -fx-background-color: black;");
                curr.setId("blue" + counter); //set button id
                counter += 1; //increase the button number by 1 for the next iteration
                gamePane.add(curr, j, i); //add the button to the grid
                colorDisplay.add(curr); //add the button to the array of buttons
            }
        }
        //add the grid to the main gridPane
        this.adventureGameView.gridPane.add(gamePane, 1, 1);
        String finalDiffButton = diffButton;
        //images for if the person correctly selects the different color or not
        String correct = "planet/hiddenhaven/Images/correct.jpg";
        String incorrect = "planet/hiddenhaven/Images/notCorrect.jpg";
        //Set what happens when each of the buttons are clicked
        for (Button color: colorDisplay) {
            color.setOnAction(keyEvent -> {
                //if the button clicked contains the different color, they display the checkmark image
                if (Objects.equals(color.getId(), finalDiffButton)) {
                    Image correctImage = new Image(correct);
                    ImageView correctImageView = new ImageView(correctImage);
                    //display image specifications
                    correctImageView.setFitWidth(105);
                    correctImageView.setFitHeight(100);
                    correctImageView.setPreserveRatio(false);
                    color.setGraphic(correctImageView);
                    this.result = true; //player has won the round
                    this.adventureGameView.model.player.updateLives(this.result); //update player lives
                    this.gameCounter += 1; //keeps track of when the round is done, and we can move on to the next
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    //pause for one sec before starting the next round
                    pause.setOnFinished(event ->{
                        startGame();
                    });
                    pause.play();
                }
                //if the button clicked contains a regular color, then display an X
                else {
                    Image inCorrectImage = new Image(incorrect);
                    ImageView inCorrectImageView = new ImageView(inCorrectImage);
                    //display image specifications
                    inCorrectImageView.setFitWidth(105);
                    inCorrectImageView.setFitHeight(100);
                    inCorrectImageView.setPreserveRatio(false);
                    color.setGraphic(inCorrectImageView);
                    this.loses += 1;
                    this.result = false; //player guessed incorrectly
                    //if the player has answered incorrectly 3 consecutive times, remove a life
                    if (this.loses % 3 == 0) {
                        this.adventureGameView.model.player.updateLives(this.result); //update player lives
                    }
                    //update the players lives
                    this.adventureGameView.updateLives(); //update the lives images on the screen
                    //if the player has no more lives, display the losing mini-game screen
                    if (this.adventureGameView.model.player.getLives() == 0){
                        gamePane.getChildren().clear();
                        //this.adventureGameView.losingMiniGame();
                    }
                }
            });
        }
    }
    /**
     * getResult
     * _________________
     * returns whether the user has played the game
     */
    public Boolean getResult() {
        //returns true if player has won the game, false otherwise.
        return this.result;
    }
    /**
     * updateResult
     * _________________
     * keeps result as false until the game is completed, then changes it to true
     */
    public void updateResult() {
        //if the player has won the game, they lose no lives and move on to next planet.
        //if the player lost the game, they retry and lose a life. if they have no life, game over.
        if (this.gameCounter != 3 && this.adventureGameView.model.player.getLives() != 0) {
            // create new gamePane
            createGrid();
            //set up buttons for the game
            //game(gameImages.removeFirst(), gameImages.removeFirst());
            game(gameImages.remove(0), gameImages.remove(0));
            this.adventureGameView.stage.sizeToScene();
        }
        else {
            //clear gamePane to display victory
            // mini-game screen
            gamePane.getChildren().clear();
            this.result = true;
            //this.adventureGameView.winningMiniGame();
            //update the players object inventory
            updateObject();
            this.adventureGameView.updateScene("YOU HAVE WON.", "planet/cosmiccode/Images/victory.png");
            PauseTransition pause2 = new PauseTransition(Duration.seconds(2.0));
            pause2.setOnFinished((event2) -> {
                // after 2 seconds of displaying their loss, start round 1
                this.adventureGameView.textEntry.setVisible(true);
                this.adventureGameView.displayChoice();
            });
            pause2.play();
        }
    }
    /**
     * updateObject
     * _________________
     * calls update tools to add the tool received upon completion of the game
     */
    public void updateObject() {
        //if the player collected any new objects, update them.
        this.adventureGameView.updateTools("rhea");
    }

}
