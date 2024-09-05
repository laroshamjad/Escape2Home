package planet.puzzlemania;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.util.Duration;
import planet.Game;
import views.AdventureGameView;

import java.io.*;
import java.util.ArrayList;
import javafx.scene.transform.Scale;


public class PuzzleMania implements Game {
    AdventureGameView adventureGameView;
    GridPane gamePane;
    String currPiece;
    int left;
    ArrayList<String> pieceTrack;
    ArrayList<String> gridTrack;
    int wrong;
    Boolean result;

    public Boolean isVisited = false;

    Boolean instruct;

    public PuzzleMania(AdventureGameView game) {
        this.adventureGameView = game;
        this.left = 16;
        this.result = false;
        this.pieceTrack = new ArrayList<>() {{
            add("13");
            add("2");
            add("3");
            add("14");
            add("5");
            add("6");
            add("1");
            add("12");
            add("16");
            add("10");
            add("11");
            add("8");
            add("7");
            add("4");
            add("15");
            add("9");
        }};
        this.gridTrack = new ArrayList<>() {{
            add("13");
            add("2");
            add("3");
            add("14");
            add("5");
            add("6");
            add("1");
            add("12");
            add("16");
            add("10");
            add("11");
            add("8");
            add("7");
            add("4");
            add("15");
            add("9");
        }};
        this.currPiece = "";
        this.gamePane = new GridPane(4, 4);
        this.wrong = 0;
        this.instruct = false;
    }

    /**
     * play
     * __________________________
     * plays the audio of the instructions for the player.
     */
    public void play() {
        String musicFile = "planet/puzzlemania/instructions.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        this.adventureGameView.mediaPlayer = new MediaPlayer(sound);
        this.adventureGameView.mediaPlayer.play();
    }

    public void showInstructions() throws IOException {
        if (this.instruct) {
            this.instruct = false;
        } else {
            String text = "";
            String fileName = "planet/puzzlemania/instructions.txt";
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
            ScrollPane n = new ScrollPane(roomPane);
            n.setPadding(new Insets(10));
            n.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
            this.adventureGameView.gridPane.add(n,1, 1);
            this.instruct = true;
        }

    }

    /**
     * startGame
     * __________________________
     * starts game for player
     */
    @Override
    public void startGame() {
        this.isVisited = true;
        this.adventureGameView.textEntry.setVisible(false);
        Image bg = new Image("images/puzzlemania/background.jpeg");
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
        this.adventureGameView.updateTools("");
        this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
        try {
            showInstructions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PauseTransition pauseInstruct = new PauseTransition(Duration.seconds(8));
        pauseInstruct.setOnFinished(event -> {
            try {
                showInstructions();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ArrayList<Node> cell = new ArrayList<>();
            for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer columnIndex = GridPane.getColumnIndex(node);

                if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                    cell.add(node);
                }
            }
            this.adventureGameView.gridPane.getChildren().removeAll(cell);
            makeGrid();
            makeScroll();
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event1 -> {
                gridTrack = new ArrayList<>();
                makeGrid();
                String musicFile = "planet/puzzlemania/puzzlemaniadescription.mp3";
                Media sound = new Media(new File(musicFile).toURI().toString());
                this.adventureGameView.mediaPlayer = new MediaPlayer(sound);
                this.adventureGameView.mediaPlayer.play();
            });
            pause.play();
        });
        pauseInstruct.play();


    }

    public void updateCurrentPiece(String i) {
        currPiece = i;
    }


    public void checkCorrect(String i) {
        if (i.equals(currPiece)) {
            pieceTrack.remove(i);
            gridTrack.add(i);
            makeGrid();
            makeScroll();
            left--;
        } else {
            wrong++;
            if (wrong == 3) {
                this.adventureGameView.model.player.updateLives(false);
                this.adventureGameView.updateLives();
                wrong = 0;
            }
        }
        updateResult();
        currPiece = "";
    }

    public void makeScroll() {
        HBox c = new HBox();
        for (int i = 0; i < pieceTrack.size(); i++) {
            String curr = pieceTrack.get(i);
            Image r = new Image("planet/puzzlemania/images/" + curr + ".jpg");
            ImageView m = new ImageView(r);
            m.setFitWidth(100);
            m.setFitHeight(100);
            Button b = new Button();
            Scale change = new Scale(1.0, 1.0);
            b.getTransforms().add(change);
            b.setOnMouseEntered(event -> {
                change.setX(1.4);
                change.setY(1.4);
            });
            b.setOnMouseExited(event -> {
                change.setX(1.0);
                change.setY(1.0);
            });
            b.setGraphic(m);
            b.setPadding(new Insets(0, 0, 0, 0));
            b.setId(String.valueOf(curr));
            b.setOnAction(event -> {
                updateCurrentPiece(b.getId());
            });
            c.getChildren().addAll(b);
        }
        c.setSpacing(10);
        ScrollPane scO = new ScrollPane(c);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scO.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scO.setFitToWidth(true);
        scO.setFitToHeight(true);
        this.adventureGameView.gridPane.add(scO, 1, 2);
//        if (pieceTrack.isEmpty()) {
//            updateResult();
//        }
    }

    public void makeGrid() {
        gamePane.getChildren().clear();
        gamePane = new GridPane(4, 4);
        int track = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Image create = new Image("planet/puzzlemania/images/outline.png");
                Image createAlternative = new Image("planet/puzzlemania/images/" +track + ".jpg");
                ImageView make = new ImageView(create);
                if (gridTrack.contains(String.valueOf(track))) {
                    make = new ImageView(createAlternative);
                }
                make.setFitHeight(110);
                make.setFitWidth(110);
                Button a = new Button();
                a.setGraphic(make);
                a.setBackground(Background.fill(new Color(0, 0, 0, 0)));
                a.setPadding(new Insets(0, 0, 0, 0));
                a.setId(String.valueOf(track));
                a.setOnAction(event -> {
                    checkCorrect(a.getId());
                });
                gamePane.add(a, j, i);
                track++;
            }
        }
        gamePane.setHgap(0);
        gamePane.setVgap(0);
        gamePane.setAlignment(Pos.CENTER);
        this.adventureGameView.gridPane.add(gamePane, 1, 1);
    }

    @Override
    public Boolean getResult() {
        return this.result;
    }

    @Override
    public void updateResult() {
        if (this.adventureGameView.model.player.getLives() > 0) {
            if (pieceTrack.isEmpty()) {
                this.result = true;
                updateObject();
                ArrayList<Node> cell_1_1 = new ArrayList<>();
                for (javafx.scene.Node node : this.adventureGameView.gridPane.getChildren()) {
                    Integer rowIndex = GridPane.getRowIndex(node);
                    Integer columnIndex = GridPane.getColumnIndex(node);

                    if (rowIndex != null && columnIndex != null && rowIndex == 1 && columnIndex == 1) {
                        cell_1_1.add(node);
                    }
                }
                this.adventureGameView.gridPane.getChildren().removeAll(cell_1_1);
                this.adventureGameView.updateScene("YOU HAVE WON.", "planet/cosmiccode/Images/victory.png");

                PauseTransition pause2 = new PauseTransition(Duration.seconds(2.0));
                pause2.setOnFinished((event2) -> {
                    // after 2 seconds of displaying their loss, start round 1
                    this.adventureGameView.textEntry.setVisible(true);
                    this.adventureGameView.displayChoice();
                });
                pause2.play();
            }
        } else {
            this.result = false;
        }
    }

    @Override
    public void updateObject() {
        this.adventureGameView.updateTools("iman");
    }
}





