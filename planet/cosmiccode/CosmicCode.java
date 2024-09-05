package planet.cosmiccode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import planet.Game;
import views.AdventureGameView;
import javafx.animation.PauseTransition;

import java.io.File;
import java.util.ArrayList;

public class CosmicCode implements Game {
    private AdventureGameView adventureGameView; // the game view

    public static String userAnswer = ""; // the sequence of buttons that user pressed

    static public int count = 0;  // a count to determine how many buttons user has pressed

    static public boolean round1 = true; // true if round 1 is currently happening, else false

    static public boolean round2 = false; // true if round 2 is currently happening, else false

    static public boolean round3 = false; // true if round 3 is currently happening, else false

    GridPane planetGridPane = new GridPane(); // gridpane that contains the buttons for the minigame
    private MediaPlayer mediaPlayer; // to play the sound when user presses button

    public Boolean isVisited = false; // true if user has visited the planet, else false

    public Boolean result = false; // true if user won the mini-game, false otherwise

    Button b1 = new Button(); // first button
    Button b2 = new Button(); // second button
    Button b3 = new Button(); // third button
    Button b4 = new Button(); // fourth button
    Button b5 = new Button(); // fifth button
    Button b = new Button("Hear Instruction"); // hear instruction button

    public Boolean restart = false; // true if game has restarted, else false
    Label turnLabel = new Label(); // indicate that its player's turn

    /**
     * Planet Constructor
     * __________________________
     * Initializes attributes
     */
    public CosmicCode(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
    }

    /**
     * startGame()
     * __________________________
     * Introduces character to the planet, presents the rules and starts the mini-game.
     */
    public void startGame() {
        // remove anything in the middle of the gridPane
        // this.adventureGameView.updateTools("");
        isVisited = true;
        removeMiddle();
        this.adventureGameView.updateTools("");

        // display the planet
        updateScene("Welcome to Cosmic Code!", "Images/CosmicCode/background.png");
        PauseTransition pause = new PauseTransition(Duration.seconds(5.0));
        pause.setOnFinished((event) -> {
            // after pause, display the rules
            removeMiddle();
            Label l = new Label("\tYou see a tool that can help fix the rocket in the far distance. \n" +
                    "\tIn order to reach it, you must first survive a shower of meteorites. \n\n" +
                    "\tHOW TO PLAY THE GAME: \n\tYou will be presented with a video showing the sequence of buttons to press. \n" +
                    "\tCorrectly follow the sequence to advance to a harder round. Failing loses a life. \n" +
                    "\tWin all three rounds to pass through the meteorite shower and retrieve the tool. \n");

            // change the background
            Image bg = new Image("planet/cosmiccode/Images/planet.png");
            BackgroundImage background = new BackgroundImage(
                    bg, BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            this.adventureGameView.gridPane.setBackground(new Background(background));

            PauseTransition pause2 = new PauseTransition(Duration.seconds(8.0));
            pause2.setOnFinished((event2) -> {
                // after 8 seconds of displaying the rules, start round 1
                removeMiddle();
                round("1");
            });

            // customize the label and add it to the center of the gridPane
            l.setWrapText(true);
            l.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
            l.setAlignment(Pos.CENTER);

            addbEvent();
            VBox roomPane = new VBox();
            roomPane.getChildren().addAll(l, b);
            roomPane.setPadding(new Insets(20));
            roomPane.setAlignment(Pos.CENTER);
            roomPane.setStyle("-fx-background: #000000; -fx-background-color:transparent;");

            this.adventureGameView.gridPane.add(roomPane,1, 1);

            pause2.play();
        });
        pause.play();
    }

    /**
     * defeatScreen()
     * __________________________
     * If player has 0 lives, display that they have lost the game and return true.
     * Otherwise, return false.
     */
    public Boolean defeatScreen() {
        if (this.adventureGameView.model.player.getLives() == 0) {
            this.adventureGameView.displayDefeat();
            return true;
        }
        return false;
    }


    /**
     * getResult()
     * __________________________
     * Returns true if player has won the game, false otherwise.
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * getResult()
     * __________________________
     * If the player has won the game, they lose no lives and move on to next planet.
     * If the player lost the game, they retry and lose a life. if they have no life, game over.
     */
    public void updateResult(){
        if (this.adventureGameView.model.player.getLives() > 0) {
            if (round3) {
                this.result = true;
                updateObject(); }
        } else {
            this.result = false;
        }
    }

    /**
     * updateObject()
     * __________________________
     * If the player collected any new objects, update them.
     */
    public void updateObject() {
        this.adventureGameView.updateTools("larosh");
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
        roomImageView.setFitWidth(600);
        roomImageView.setFitHeight(1000);

        //this.adventureGameView.formatText(textToDisplay); //format the text to display
        this.adventureGameView.roomDescLabel.setWrapText(true);
        this.adventureGameView.roomDescLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox roomPane = new VBox(10);
        roomPane.setAlignment(Pos.CENTER);
        roomPane.getChildren().addAll(roomImageView, this.adventureGameView.roomDescLabel);
        roomPane.setStyle("-fx-background-color: #000000;");

        // add to the center of the gridpane
        this.adventureGameView.gridPane.add(roomPane, 1, 1);
        this.adventureGameView.stage.sizeToScene();

        // finally, articulate the description
        //if (textToDisplay == null || textToDisplay.isBlank()) this.adventureGameView.articulateRoomDescription();
    }

    /**
     * removeMiddle()
     * __________________________
     *
     * Removes all nodes in the center of this.adventureGameView.gridPane
     */
    public void removeMiddle() {
        // hide the text entry
        this.adventureGameView.textEntry.setVisible(false);

        ArrayList<Node> cell_1_1 = new ArrayList<>();
        for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            // add any node that is found at the center
            if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                cell_1_1.add(node);
            }
        }

        // remove anything in the center of the gridpane
        this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
    }

    /**
     * updateScene
     * __________________________
     *
     * Plays the video for the respective round and displays
     * the meteors so user can begin pressing buttons.
     *
     * @param input the round that is currently being played.
     */
    public void round(String input){
        // remove anything in the center of the gridpane at the start of round 1
//        if (input.equals("1")) {
//            removeMiddle();
//        }

        // creates the media for the video
        Media media = new Media(new File("planet/cosmiccode/Video/round" + input + ".mp4").toURI().toString());
        MediaPlayer videoPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(videoPlayer);

        // set size of the MediaView
        mediaView.setFitWidth(700);
        mediaView.setFitHeight(525);

        // add video to stackpane and center it
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(mediaView);
        StackPane.setAlignment(mediaView, Pos.CENTER);

        // add stackpane to center of the gridpane
        this.adventureGameView.gridPane.add(stackPane, 1, 1);
        this.adventureGameView.stage.sizeToScene();

        // play the video
        videoPlayer.play();

        videoPlayer.setOnEndOfMedia(() -> {
            // after the video finishes playing, stop showing the video and display the meteors
            if (input.equals("1") && !restart) {
                stackPane.setVisible(false);
                displayMeteors();
            } else {
                stackPane.setVisible(false);
            }

        });

    }

    /**
     * roundEnd()
     * __________________________
     * Stops the round.
     * If the player has lost the round, their loss will be displayed and the mini-game will end.
     * If the player has won all three rounds, their victory will be displayed and the mini-game will end.
     */
    public void roundEnd() {
        // if it is currently round 1
        if (round1) {
            // check if user has pressed 2 buttons
            if (count == 2) {
                // check if user pressed the correct sequence of buttons, if so move on to the next round
                if (userAnswer.equals("AB")) {
                    // reset count and userAnswer
                    round1 = false;
                    round2 = true;
                    count = 0;
                    userAnswer = "";
                    round("2");
              } else {
                    restart = true;
                    count = 0;
                    userAnswer = "";
                    turnLabel.setText("YOU ARE INCORRECT. TRY AGAIN.");
                    if (!defeatScreen()) {
                        PauseTransition pause2 = new PauseTransition(Duration.seconds(2.0));
                        pause2.setOnFinished((event2) -> {
                            // after 2 seconds of displaying their loss, start round 1
                            this.adventureGameView.model.player.updateLives(false);
                            this.adventureGameView.updateLives();
                            turnLabel.setText("YOUR TURN");
                        });
                        pause2.play();
                    }
              }
            }
        // if it is round 2
        } else if (round2) {
            // check if user has pressed 3 buttons
            if (count == 3) {
                // check if user has pressed the correct sequence of buttons
                if (userAnswer.equals("ABD")) {
                    // reset count and userAnswer
                    round2 = false;
                    round3 = true;
                    count = 0;
                    userAnswer = "";
                    round("3");
                } else {
                    // display the loss and change the background to black
                    restart = true;
                    count = 0;
                    userAnswer = "";
                    turnLabel.setText("YOU ARE INCORRECT. TRY AGAIN.");
                    if (!defeatScreen()) {
                        PauseTransition pause2 = new PauseTransition(Duration.seconds(2.0));
                        pause2.setOnFinished((event2) -> {
                            // after 2 seconds of displaying their loss, start round 1
                            this.adventureGameView.model.player.updateLives(false);
                            this.adventureGameView.updateLives();
                            turnLabel.setText("YOUR TURN");
                        });
                        pause2.play();
                    }
                }
            }
        // if it is round 3
        } else if (round3) {
            // check if user has pressed 5 buttons
            if (count == 5) {
                // check if user has pressed the correct order of buttons
                // change the background to black
                if (userAnswer.equals("ABDCE")) {
                    // display their victory and reset count and userAnswer
                    updateScene("YOU HAVE WON.", "planet/cosmiccode/Images/victory.png");
                    //round3 = false;
                    result = true;
                    count = 0;
                    userAnswer = "";
                    updateObject();
                    PauseTransition pause2 = new PauseTransition(Duration.seconds(2.0));
                    pause2.setOnFinished((event2) -> {
                        // after 2 seconds of displaying their loss, start round 1
                        this.adventureGameView.textEntry.setVisible(true);
                        this.adventureGameView.displayChoice();
                    });
                    pause2.play();
                } else {
                    // display the loss
                    restart = true;
                    count = 0;
                    userAnswer = "";
                    turnLabel.setText("YOU ARE INCORRECT. TRY AGAIN.");
                    if (!defeatScreen()) {
                        PauseTransition pause2 = new PauseTransition(Duration.seconds(2.0));
                        pause2.setOnFinished((event2) -> {
                            // after 2 seconds of displaying their loss, start round 1
                            this.adventureGameView.model.player.updateLives(false);
                            this.adventureGameView.updateLives();
                            turnLabel.setText("YOUR TURN");
                        });
                        pause2.play();
                    }
                }
            }
        }
    }

    /**
     * updateScene
     * __________________________
     *
     * Displays the buttons that appear as meteors at the center of the adventureGameView.gridPane.
     */
    public void displayMeteors(){
        // customize each button, add a unique ID, and add them to planetGridPane in its respective position
        b1.setMinSize(190, 150);
        b1.setId("A");
        b1.setStyle("-fx-background-image: url('planet/cosmiccode/Images/meteor3.png');" +
                "-fx-background-size: cover;");
        planetGridPane.add(b1, 1, 1);

        b2.setMinSize(190, 150);
        b2.setId("B");
        b2.setStyle("-fx-background-image: url('planet/cosmiccode/Images/meteor3.png');" +
                "-fx-background-size: cover;");
        planetGridPane.add(b2, 3, 1);

        b3.setMinSize(190, 150);
        b3.setId("C");
        b3.setStyle("-fx-background-image: url('planet/cosmiccode/Images/meteor3.png');" +
                "-fx-background-size: cover;");
        planetGridPane.add(b3, 2, 2);

        b4.setMinSize(190, 150);
        b4.setId("D");
        b4.setStyle("-fx-background-image: url('planet/cosmiccode/Images/meteor3.png');" +
                "-fx-background-size: cover;");
        planetGridPane.add(b4, 1, 3);

        b5.setMinSize(190, 150);
        b5.setId("E");
        b5.setStyle("-fx-background-image: url('planet/cosmiccode/Images/meteor3.png');" +
                "-fx-background-size: cover;");
        planetGridPane.add(b5, 3, 3);

        // make planetGridPane align to the center
        planetGridPane.setAlignment(Pos.CENTER);

        // a label that allows user to know that its their turn
        turnLabel.setText("YOUR TURN");
        turnLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;"); // Style the label as needed

        // create a vBox to hold the buttons and the label
        VBox container = new VBox(40); // Adjust spacing between elements if needed
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(planetGridPane, turnLabel);

        this.adventureGameView.gridPane.add(container, 1, 1);
        this.adventureGameView.stage.sizeToScene();

        // add the sound to media player
        Media sound = new Media(new File("planet/cosmiccode/Sounds/beep.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);

        // allow button events to occur
        addb1Event();
        addb2Event();
        addb3Event();
        addb4Event();
        addb5Event();
    }

    /**
     * This method handles the event related to the
     * b button.
     */
    public void addbEvent() {
        b.setOnAction(e -> {
            // stop any sound
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            // play the instructions
            mediaPlayer.play();

        });
    }


    /**
     * This method handles the event related to the
     * b1 button.
     */
    public void addb1Event() {
        b1.setOnAction(e -> {
            // count is increased by one to signify that user has pressed count number of buttons
            count += 1;
            // adds its ID to the userAnswer
            userAnswer += b1.getId();
            // adds the sound to the button.
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
            // checks if round should end
            roundEnd();
        });
    }

    /**
     * This method handles the event related to the
     * b2 button.
     */
    public void addb2Event() {
        b2.setOnAction(e -> {
            // count is increased by one to signify that user has pressed count number of buttons
            count += 1;
            // adds its ID to the userAnswer
            userAnswer += b2.getId();
            // adds the sound to the button.
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
            // checks if round should end
            roundEnd();
        });
    }

    /**
     * This method handles the event related to the
     * b3 button.
     */
    public void addb3Event() {
        b3.setOnAction(e -> {
            // count is increased by one to signify that user has pressed count number of buttons
            count += 1;
            // adds its ID to the userAnswer
            userAnswer += b3.getId();
            // adds the sound to the button.
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
            // checks if round should end
            roundEnd();
        });
    }

    /**
     * This method handles the event related to the
     * b4 button.
     */
    public void addb4Event() {
        b4.setOnAction(e -> {
            // count is increased by one to signify that user has pressed count number of buttons
            count += 1;
            // adds its ID to the userAnswer
            userAnswer += b4.getId();
            // adds the sound to the button.
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
            // checks if round should end
            roundEnd();
        });
    }

    /**
     * This method handles the event related to the
     * b2 button.
     */
    public void addb5Event() {
        b5.setOnAction(e -> {
            // count is increased by one to signify that user has pressed count number of buttons
            count += 1;
            // adds its ID to the userAnswer
            userAnswer += b5.getId();
            // adds the sound to the button.
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
            // checks if round should end
            roundEnd();
        });
    }
}
