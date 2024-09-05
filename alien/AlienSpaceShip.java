package alien;

import AdventureModel.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import views.AdventureGameView;

public class AlienSpaceShip{

    AdventureGameView adventureGameView;
    GridPane gamePane;
    int curr;
    Boolean result;

    Label invLabel;
    AlienIterator a;

    Boolean instruct;

    public AlienSpaceShip(AdventureGameView game) {
        this.a = new AlienIterator();
        this.adventureGameView = game;
        this.gamePane = new GridPane(2, 2);
        this.curr = 0;
        this.result = false;
        this.instruct = false;
        setUp();

    }

    /**
     * play
     * __________________________
     * plays the audio of the instructions for the player.
     */
    public void play() {
        String musicFile = "alien/instructions.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        this.adventureGameView.mediaPlayer = new MediaPlayer(sound);
        this.adventureGameView.mediaPlayer.play();
    }

    /**
     * instructions
     * _________________________
     * adds instructions for alien
     */
    public void instructions() throws IOException {
        if (this.instruct) {
            ArrayList<Node> cell_1_1 = new ArrayList<>();
            for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer columnIndex = GridPane.getColumnIndex(node);

                if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                    cell_1_1.add(node);
                }
            }
            this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
            this.instruct = false;
        } else {
            String text = "";
            String fileName = "alien/instructions.txt";
            BufferedReader buff = new BufferedReader(new FileReader(fileName));
            String line = buff.readLine();
            while (line != null) { //
                text += line+"\n";
                line = buff.readLine();
            }
            this.adventureGameView.roomDescLabel.setFont(new Font("Arial", 18));
            this.adventureGameView.roomDescLabel.setText(text);
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
            javafx.scene.control.ScrollPane n = new ScrollPane(roomPane);
            n.setPadding(new Insets(10));
            n.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            n.setFitToWidth(true);
            ArrayList<Node> cell_1_1 = new ArrayList<>();
            for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer columnIndex = GridPane.getColumnIndex(node);

                if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                    cell_1_1.add(node);
                }
            }
            this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
            this.adventureGameView.gridPane.add(n,1, 1);
            this.instruct = true;
        }

    }

    /**
     * setUp
     * __________________________
     * sets up the game screen for the player with alien
     */
    public void setUp() {
        try {
            instructions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            try {
                instructions();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Image bg = new Image("alien/spaceship/jpg");
            BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
            BackgroundImage background = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
            this.adventureGameView.gridPane.setBackground(new Background(background));

            ArrayList<Node> cell_1_1 = new ArrayList<>();
            for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer columnIndex = GridPane.getColumnIndex(node);

                if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                    cell_1_1.add(node);
                }
            }
            this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
            playGame();
        });
        pause.play();
    }

    /**
     * playGame
     * __________________________
     * the game keeps running if there are enough lives, if not, the game is over.
     */
    public void playGame() {
        if (this.adventureGameView.model.player.getLives() < 0) {
            this.result = false;
            gameOver();
        }
        else if (!a.hasNext()) {
            if (this.adventureGameView.model.player.getLives() > 0) {
                this.result = true;
            } else {
                this.result = false;
            }
            gameOver();

        }
        else {
            Alien b = a.next();
            setUpGrid(b);

        }
    }

    /**
     * update
     * __________________________
     * every time the player clicks on a button the game is update.
     * If the player clicks the right answer, the game continues, if not a live is lost.
     */
    public void update(String s) {
        if (s.equals(String.valueOf(curr + 1))) {
            curr++;
            String musicFile = "alien/correct.mp3";
            Media sound = new Media(new File(musicFile).toURI().toString());
            this.adventureGameView.mediaPlayer = new MediaPlayer(sound);
            this.adventureGameView.mediaPlayer.play();
            playGame();
        } else {
            String musicFile = "alien/incorrect.mp3";
            Media sound = new Media(new File(musicFile).toURI().toString());
            this.adventureGameView.mediaPlayer = new MediaPlayer(sound);
            this.adventureGameView.mediaPlayer.play();
            this.adventureGameView.model.player.updateLives(false);
            this.adventureGameView.updateLives();
            if (this.adventureGameView.model.player.getLives() < 0) {
                this.result = false;
                gameOver();
            }
        }
    }

    /**
     * setUpGrid
     * __________________________
     * sets up the grid for the player to start playing the game.
     */
    public void setUpGrid(Alien curr) {
        int track = 1;
        invLabel = new Label("Question: " + curr.getQuestion());
        Scale change1 = new Scale(1.0, 1.0);
        invLabel.getTransforms().add(change1);
        invLabel.setOnMouseEntered(event -> {
            change1.setX(1.4);
            change1.setY(1.4);
        });
        invLabel.setOnMouseExited(event -> {
            change1.setX(1.0);
            change1.setY(1.0);
        });
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 30));
        gamePane.getChildren().clear();
        gamePane = new GridPane(2, 2);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Image create = new Image("alien/alien.jpg");
                ImageView make = new ImageView(create);
                make.setFitHeight(130);
                make.setFitWidth(130);

                VBox ma = new VBox();
                ma.setPrefSize(170,140);
                Label l = new Label(String.valueOf(curr.randomAnswers()));
                if (track == this.curr + 1) {
                    l = new Label(String.valueOf(curr.getAnswer()));
                }
                ma.setStyle("-fx-background-color: transparent");
                l.setFont(new javafx.scene.text.Font("Arial", 26));
                l.setTextFill(Color.BLACK);
                l.setAlignment(Pos.CENTER);
                ma.setAlignment(Pos.CENTER);
                ma.getChildren().addAll(make, l);

                Button a = new Button();
                Scale change = new Scale(1.0, 1.0);
                a.getTransforms().add(change);
                a.setOnMouseEntered(event -> {
                    change.setX(1.4);
                    change.setY(1.4);
                });
                a.setOnMouseExited(event -> {
                    change.setX(1.0);
                    change.setY(1.0);
                });
                a.setGraphic(ma);
                //a.setBackground(Background.fill(new Color(0, 0, 0, 0)));
                a.setPadding(new Insets(0, 0, 0, 0));
                a.setId(String.valueOf(track));
                a.setOnAction(event -> {
                    update(a.getId());
                });
                a.setContentDisplay(ContentDisplay.TOP);
                gamePane.add(a, j, i);
                track++;
            }
        }
        gamePane.setStyle("-fx-background-color: transparent");
        gamePane.setHgap(10);
        gamePane.setVgap(10);
        gamePane.setAlignment(Pos.CENTER);
        VBox hold = new VBox();
        hold.setAlignment(Pos.CENTER);
        invLabel.setPadding(new Insets(15));
        hold.getChildren().addAll(invLabel, this.gamePane);
        this.adventureGameView.gridPane.add(hold, 1, 1);
    }

    /**
     * gameOver
     * __________________________
     * The player has either won the game, or lost.
     */
    public boolean gameOver() {
        if (this.result) {
            invLabel.setText("You beat the aliens!");
        } else {
            invLabel.setText("You lost to the aliens!");
        }
        ArrayList<Node> cell_1_1 = new ArrayList<>();
        for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                cell_1_1.add(node);
            }
        }
        //this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
        return this.result;
    }

}
