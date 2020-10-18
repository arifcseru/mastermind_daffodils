/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package app.quiz.presentationtier;

import app.quiz.businesstier.QuestionTO;
import static app.quiz.presentationtier.ScreensFramework.scene;
import com.app.persistencetier.QuestionsService;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Angie
 */
public class QuizScreenController implements ActionListener, Initializable, ControlledScreen {

    Boolean fileEnabled = false;
    File questionFile = null;
    Timer timer = new Timer(5000, (ActionListener) this);
    String correctAnswerAudio = "mediaFiles/settings/correctanswer.wav";
    String wrongAnswerAudio = "mediaFiles/settings/openingwrong.wav";
    String quizSession_continuous = "mediaFiles/settings/quizSession_continuous.wav";
    String mouseOver_audio = "mediaFiles/settings/mouseOver_audio.wav";
    String mouseClick_audio = "mediaFiles/settings/mouseClick.wav";
    String fileLocation = null;
    public static int counter = 0;
    ScreensController myController;
    @FXML
    private Button optionA_btn;
    @FXML
    private Button optionB_btn;
    @FXML
    private Button optionC_btn;
    @FXML
    private Button optionD_btn;
    @FXML
    private Label questionLabel;
    private static String correctOption = "";
    public static String questionType = "";
    public static Boolean mediaVisited = false;
    @FXML
    private ImageView quizScreenImageView;
    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    private Media media;
    @FXML
    private Label correctAnswerLabel;
    @FXML
    private Label wrongAnswerLabel;
    QuestionTO questionTO;
    @FXML
    private HBox questionSerialHBox;
    List<Label> labels = null;
    Map<Integer, Boolean> results = new LinkedHashMap();
    @FXML
    private GridPane optionHolderGridPane;
    @FXML
    private VBox quizScreenVBox;
    @FXML
    private ImageView fiftyFiftyImageView;
    @FXML
    private ImageView phoneIconImageView;
    @FXML
    private ImageView peopleCallImageView;
    @FXML
    private ImageView quizTypeImageView;
    @FXML
    private Label quizRoundNameLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quizRoundNameLabel.setText(ScreensFramework.quizRoundName);
        quizRoundNameLabel.setVisible(false);
        quizTypeImageView.setVisible(false);

        fiftyFiftyImageView.setVisible(true);
        phoneIconImageView.setVisible(true);
        peopleCallImageView.setVisible(true);

        ScreensFramework.correctAnswers = 0;
        timer.start();
        for (int i = 0; i < 100; i++) {
            results.put(i, true);
        }
        commonLoadNextQuestion();
        commonPlayAction(quizSession_continuous);

        correctAnswerLabel.setVisible(false);
        wrongAnswerLabel.setVisible(false);
        initializeComponents();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        quizScreenVBox.setMaxSize(bounds.getWidth(), bounds.getHeight());
        
        quizScreenVBox.setLayoutX((bounds.getMaxX() / 2 - quizScreenVBox.getWidth() / 2));
        quizScreenVBox.setLayoutY((bounds.getMaxY() / 2 - quizScreenVBox.getHeight() / 2));

    }

    private void initializeComponents() {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        questionLabel.setLayoutX(bounds.getMinX() + 50);
        // questionLabel.setLayoutY(bounds.getMaxY()/8 - 50);
        questionLabel.setPrefWidth(bounds.getMaxX() - 50);
        //optionHolderGridPane.setLayoutX(bounds.getMaxX()/3+50);
        //optionHolderGridPane.setLayoutY(bounds.getMaxY()/8);
        optionA_btn.setLayoutX(bounds.getMaxX() / 3 + 50);
        optionB_btn.setLayoutX(bounds.getMaxX() / 2 + 100);
        optionC_btn.setLayoutX(bounds.getMaxX() / 3 + 50);
        optionD_btn.setLayoutX(bounds.getMaxX() / 2 + 100);

        optionA_btn.setLayoutY(bounds.getMaxY() / 8);
        optionB_btn.setLayoutY(bounds.getMaxY() / 8);
        optionC_btn.setLayoutY(bounds.getMaxY() / 8 + optionA_btn.getPrefHeight() + 100);
        optionD_btn.setLayoutY(bounds.getMaxY() / 8 + optionB_btn.getPrefHeight() + 100);

        optionA_btn.setPrefWidth(bounds.getWidth() / 3);
        optionB_btn.setPrefWidth(bounds.getWidth() / 3);
        optionC_btn.setPrefWidth(bounds.getWidth() / 3);
        optionD_btn.setPrefWidth(bounds.getWidth() / 3);
    }

    public static String questionIntervalAudio = "mediaFiles/settings/question_interval.wav";

    public void drawAllQuestionNumbers(int n) {
        commonPlayAction(questionIntervalAudio);
        if (labels != null) {
            questionSerialHBox.getChildren().removeAll(labels);
        }
        List<QuestionTO> questions = ScreensFramework.questions;
        System.out.println("questions.size():" + questions.size());
        labels = new ArrayList();
        if (n > 15) {
            for (int i = 1; i <= n; i++) {
                Label label = new Label();
                String buttonText = String.valueOf(i);
                label.getStyleClass().add("init-questions");
                if (questions.get(i - 1).getFileLocation().contains("mp4")
                        || questions.get(i - 1).getFileLocation().contains("mpg")
                        || questions.get(i - 1).getFileLocation().contains("3gp")
                        || questions.get(i - 1).getFileLocation().contains("avi")) {
                    label.getStyleClass().add("video-type");
                } else if (questions.get(i - 1).getFileLocation().contains("wav")
                        || questions.get(i - 1).getFileLocation().contains("mp3")) {
                    label.getStyleClass().add("audio-type");
                } else if (questions.get(i - 1).getFileLocation().contains("jpeg")
                        || questions.get(i - 1).getFileLocation().contains("jpg")
                        || questions.get(i - 1).getFileLocation().contains("png")
                        || questions.get(i - 1).getFileLocation().contains("gif")
                        || questions.get(i - 1).getFileLocation().contains("bmp")
                        || questions.get(i - 1).getFileLocation().contains("JPG")
                        || questions.get(i - 1).getFileLocation().contains("BMP")
                        || questions.get(i - 1).getFileLocation().contains("PNG")) {
                    label.getStyleClass().add("image-type");
                }
                label.setText(buttonText);

                labels.add(label);
            }
            int i = 1;
            for (Label label : labels) {
                //if (button.getText().equalsIgnoreCase(ScreensFramework.currentQuestion.toString())) {
                if (Integer.valueOf(label.getText()) - 1 < ScreensFramework.currentQuestion) {
                    label.getStyleClass().remove("audio-type");
                    label.getStyleClass().remove("video-type");
                    label.getStyleClass().remove("image-type");

                    label.getStyleClass().remove("init-questions");
                    label.getStyleClass().add("answered-question");
                }
                if (Integer.valueOf(label.getText()) - 1 == ScreensFramework.currentQuestion) {
                    label.getStyleClass().remove("audio-type");
                    label.getStyleClass().remove("video-type");
                    label.getStyleClass().remove("image-type");

                    label.getStyleClass().remove("init-questions");
                    label.getStyleClass().add("current-question");
                }
                if (results.get(i - 1) == false) {
                    label.getStyleClass().remove("audio-type");
                    label.getStyleClass().remove("video-type");
                    label.getStyleClass().remove("image-type");

                    label.getStyleClass().remove("init-questions");
                    label.getStyleClass().add("wrong-question");
                }
                i++;
                questionSerialHBox.getChildren().addAll(label);
                questionSerialHBox.setAlignment(Pos.CENTER);
            }
        } else {// if less than 15 questions
            for (int i = 1; i <= n; i++) {
                Label label = new Label();
                String buttonText = String.valueOf(i);
                label.getStyleClass().add("init-questions");
                if (questions.get(i - 1).getFileLocation().contains("mp4")
                        || questions.get(i - 1).getFileLocation().contains("mpg")
                        || questions.get(i - 1).getFileLocation().contains("3gp")
                        || questions.get(i - 1).getFileLocation().contains("avi")) {
                    label.getStyleClass().add("video-type");
                } else if (questions.get(i - 1).getFileLocation().contains("wav")
                        || questions.get(i - 1).getFileLocation().contains("mp3")) {
                    label.getStyleClass().add("audio-type");
                } else if (questions.get(i - 1).getFileLocation().contains("jpeg")
                        || questions.get(i - 1).getFileLocation().contains("jpg")
                        || questions.get(i - 1).getFileLocation().contains("png")
                        || questions.get(i - 1).getFileLocation().contains("gif")
                        || questions.get(i - 1).getFileLocation().contains("bmp")
                        || questions.get(i - 1).getFileLocation().contains("JPG")
                        || questions.get(i - 1).getFileLocation().contains("BMP")
                        || questions.get(i - 1).getFileLocation().contains("PNG")) {
                    label.getStyleClass().add("image-type");
                }
                label.setText(buttonText);

                labels.add(label);
            }
            int i = 1;
            for (Label label : labels) {
                //if (button.getText().equalsIgnoreCase(ScreensFramework.currentQuestion.toString())) {
                if (Integer.valueOf(label.getText()) - 1 < ScreensFramework.currentQuestion) {
                    label.getStyleClass().remove("audio-type");
                    label.getStyleClass().remove("video-type");
                    label.getStyleClass().remove("image-type");

                    label.getStyleClass().remove("init-questions");
                    label.getStyleClass().add("answered-question");
                }
                if (Integer.valueOf(label.getText()) - 1 == ScreensFramework.currentQuestion) {
                    label.getStyleClass().remove("audio-type");
                    label.getStyleClass().remove("video-type");
                    label.getStyleClass().remove("image-type");

                    label.getStyleClass().remove("init-questions");
                    label.getStyleClass().add("current-question");
                }
                if (results.get(i - 1) == false) {
                    label.getStyleClass().remove("audio-type");
                    label.getStyleClass().remove("video-type");
                    label.getStyleClass().remove("image-type");

                    label.getStyleClass().remove("init-questions");
                    label.getStyleClass().add("wrong-question");
                }
                i++;
                questionSerialHBox.getChildren().addAll(label);
                questionSerialHBox.setAlignment(Pos.CENTER);
            }
        }

    }

    public void commonPlayAction(String fileAddressStr) {
        String fileAddress = new File(fileAddressStr).getAbsolutePath();
        media = new Media(new File(fileAddress).toURI().toString());
        player = new MediaPlayer(media);
        player.stop();
        player.play();

        mediaView.setMediaPlayer(player);
    }

    public void commonLoadNextQuestion() {
        ScreensFramework.questions = QuestionsService.getAllQuestions(ScreensFramework.questionSetId);
        Integer currentQ = ScreensFramework.currentQuestion;
        if (ScreensFramework.questions.size() > currentQ) {
            questionTO = ScreensFramework.questions.get(currentQ);
            //questionType = questionTO.getQuestionType();
            fileLocation = questionTO.getFileLocation();
            questionLabel.setText(questionTO.getQuestionDetail());
            counter = 0;

            drawAllQuestionNumbers(ScreensFramework.questions.size());
            hideAllButtons();
            visibleAllButtons();

            optionA_btn.setText("A:     " + questionTO.getOptionA());
            optionB_btn.setText("B:     " + questionTO.getOptionB());
            optionC_btn.setText("C:     " + questionTO.getOptionC());
            optionD_btn.setText("D:     " + questionTO.getOptionD());

            // System.out.println(questionType);
            correctOption = questionTO.getCorrectOption();
            ScreensFramework.correctAnswer = correctOption;
            nextQuestionTypeAction();
        }
    }

    public void loadFileQuestion() {
        try {
            File questionFile = ScreensFramework.questionFile;
            ScreensFramework.questions = QuizAppUtils.getQuestions(questionFile);
            questionTO = ScreensFramework.questions.get(ScreensFramework.currentQuestion);
            //questionType = questionTO.getQuestionType();
            fileLocation = questionTO.getFileLocation();
            questionLabel.setText(questionTO.getQuestionDetail());
            counter = 0;

            drawAllQuestionNumbers(ScreensFramework.questions.size());
            hideAllButtons();

            optionA_btn.setText("A:     " + questionTO.getOptionA());
            optionB_btn.setText("B:     " + questionTO.getOptionB());
            optionC_btn.setText("C:     " + questionTO.getOptionC());
            optionD_btn.setText("D:     " + questionTO.getOptionD());
            // System.out.println(questionType);
            correctOption = questionTO.getCorrectOption();
            ScreensFramework.correctAnswer = correctOption;
        } catch (IOException ex) {
            Logger.getLogger(QuizScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void laterSetOptions() {
        visibleAllButtons();
    }

    public void nextQuestionTypeAction() {
        if (fileLocation.contains(".") && ScreensFramework.currentQuestion <= ScreensFramework.questions.size()) {
            hideAllButtons();
            String extension = fileLocation.substring(fileLocation.indexOf("."), fileLocation.length());
            timer.stop();
            if (extension.contains("mp3") || extension.contains("wav")) {
                boolean returnResult = AlertBoxController.displayMedia("Media Container", "Please Listen to the Audio", fileLocation);
                if (returnResult) {
                    timer.start();
                }
            } else if (extension.contains("mp4")
                    || extension.contains("MP4")
                    || extension.contains("Mp4")
                    || extension.contains("mP4")) {
                boolean returnResult = AlertBoxController.displayMedia("Media Container", "Please See the video", fileLocation);
                if (returnResult) {
                    timer.start();
                }
            } else if (extension.contains("jpg")
                    || extension.contains("jpeg")
                    || extension.contains("gif")
                    || extension.contains("png")
                    || extension.contains("bmp")
                    || extension.contains("JPG")
                    || extension.contains("JPEG")
                    || extension.contains("GIF")
                    || extension.contains("PNG")
                    || extension.contains("BMP")) {
                boolean returnResult = AlertBoxController.displayMedia("Media Container", "Please look at the Image", fileLocation);
                if (returnResult) {
                    timer.start();
                }
            } else {
                timer.start();
            }

        }

    }

    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    public void disableAllButtons() {
        optionA_btn.setDisable(true);
        optionB_btn.setDisable(true);
        optionC_btn.setDisable(true);
        optionD_btn.setDisable(true);

    }

    public void enableAllButtons() {
        optionA_btn.setDisable(false);
        optionB_btn.setDisable(false);
        optionC_btn.setDisable(false);
        optionD_btn.setDisable(false);

    }

    public void visibleAllButtons() {
        if (!questionTO.getOptionA().trim().isEmpty()) {
            optionA_btn.setVisible(true);
        }
        if (!questionTO.getOptionB().trim().isEmpty()) {
            optionB_btn.setVisible(true);
        }
        if (!questionTO.getOptionC().trim().isEmpty()) {
            optionC_btn.setVisible(true);
        }
        if (!questionTO.getOptionD().trim().isEmpty()) {
            optionD_btn.setVisible(true);
        }

    }

    public void hideAllButtons() {
        optionA_btn.setVisible(false);
        optionB_btn.setVisible(false);
        optionC_btn.setVisible(false);
        optionD_btn.setVisible(false);

    }

    public void commonQuizAction(String answer) {
        Boolean correct = false;
        timer.stop();
        if (correctOption.equalsIgnoreCase(answer)) {
            correct = true;
            commonPlayAction(correctAnswerAudio);
            correctAnswerLabel.setVisible(true);
            results.put(ScreensFramework.currentQuestion, true);
            ScreensFramework.correctAnswers++;
        } else {
            commonPlayAction(wrongAnswerAudio);
            wrongAnswerLabel.setVisible(true);
            results.put(ScreensFramework.currentQuestion, false);
        }
        if (correct) {
            switch (answer) {
                case "A":
                    optionA_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                case "B":
                    optionB_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                case "C":
                    optionC_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                case "D":
                    optionD_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                default:
                    break;
            }
        } else {
            // for wrong answer
            switch (answer) {
                case "A":
                    optionA_btn.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00)");
                    break;
                case "B":
                    optionB_btn.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00)");
                    break;
                case "C":
                    optionC_btn.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00)");
                    break;
                case "D":
                    optionD_btn.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00)");
                    break;
                default:
                    break;
            }
            // for correct answer
            switch (correctOption.toLowerCase()) {
                case "a":
                    optionA_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                case "b":
                    optionB_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                case "c":
                    optionC_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                case "d":
                    optionD_btn.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00)");
                    break;
                default:
                    break;
            }
        }

        disableAllButtons();
    }

    @FXML
    public void opA_action(ActionEvent event) {
        //AlertBoxController.display("Hello", "This is alert box!");
        commonQuizAction("A");
    }

    @FXML
    public void opB_Action(ActionEvent event) {
        commonQuizAction("B");
    }

    @FXML
    public void opC_Action(ActionEvent event) {
        commonQuizAction("C");
    }

    @FXML
    public void opD_Action(ActionEvent event) {
        commonQuizAction("D");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        counter++;
        if (timer.isRunning()) {
            commonPlayAction(quizSession_continuous);
        }
    }

    @FXML
    private void nextQuestionAction(MouseEvent event) {
        nextQuestionCommonAction();
    }

    private void nextQuestionCommonAction() {
        commonPlayAction(quizSession_continuous);
        timer.start();
        enableAllButtons();
        ScreensFramework.currentQuestion++;

        if (ScreensFramework.currentQuestion >= ScreensFramework.questions.size()) {
            timer.stop();
            myController.setScreen(ScreensFramework.resultScreen);
        }
        commonLoadNextQuestion();
        correctAnswerLabel.setVisible(false);
        wrongAnswerLabel.setVisible(false);

        optionA_btn.setStyle("-fx-background-color: \n"
                + "        linear-gradient(#ffd65b, #e68400),\n"
                + "        linear-gradient(#ffef84, #f2ba44),\n"
                + "        linear-gradient(#ffea6a, #efaa22),\n"
                + "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n"
                + "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0))");
        optionB_btn.setStyle("-fx-background-color: \n"
                + "        linear-gradient(#ffd65b, #e68400),\n"
                + "        linear-gradient(#ffef84, #f2ba44),\n"
                + "        linear-gradient(#ffea6a, #efaa22),\n"
                + "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n"
                + "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0))");
        optionC_btn.setStyle("-fx-background-color: \n"
                + "        linear-gradient(#ffd65b, #e68400),\n"
                + "        linear-gradient(#ffef84, #f2ba44),\n"
                + "        linear-gradient(#ffea6a, #efaa22),\n"
                + "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n"
                + "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0))");
        optionD_btn.setStyle("-fx-background-color: \n"
                + "        linear-gradient(#ffd65b, #e68400),\n"
                + "        linear-gradient(#ffef84, #f2ba44),\n"
                + "        linear-gradient(#ffea6a, #efaa22),\n"
                + "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n"
                + "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0))");

    }

    @FXML
    private void mouseEnterAction(MouseEvent event) {
        commonPlayAction(mouseOver_audio);
    }

    @FXML
    private void keyboardEvent(KeyEvent event) {
        //System.out.println(event.getCharacter());
        if (event.getCode().isLetterKey()) {
            //System.out.println("Letter");
        }
        if (event.getCode().isWhitespaceKey()) {
            // System.out.println("Whitespace");
        }
        if (event.getCharacter().equalsIgnoreCase("A")) {
            commonQuizAction("A");
        } else if (event.getCharacter().equalsIgnoreCase("B")) {
            commonQuizAction("B");
        } else if (event.getCharacter().equalsIgnoreCase("C")) {
            commonQuizAction("C");
        } else if (event.getCharacter().equalsIgnoreCase("D")) {
            commonQuizAction("D");
        } else if (event.getCharacter().equalsIgnoreCase("N")) {
            nextQuestionCommonAction();
        } else if (event.getCharacter().equalsIgnoreCase("X")) {
            exitNowCommonAction();
        } else if (event.getCharacter().equalsIgnoreCase("o")) {
            laterSetOptions();
        } else if (event.getCharacter().equalsIgnoreCase("R")) {
            nextQuestionTypeAction();
        } else if (event.getCharacter().equalsIgnoreCase("F")) {
            //commonFiftyFiftyAction();
        } else if (event.getCharacter().equalsIgnoreCase("P")) {
            peopleCallImageView.setVisible(false);
        } else if (event.getCharacter().equalsIgnoreCase("L")) {
            phoneIconImageView.setVisible(false);
        }
    }

    public void commonFiftyFiftyAction() {
        commonPlayAction(mouseClick_audio);
        if (ScreensFramework.correctAnswer.equalsIgnoreCase("A")) {
            optionB_btn.setVisible(false);
            optionC_btn.setVisible(false);
        } else if (ScreensFramework.correctAnswer.equalsIgnoreCase("B")) {
            optionA_btn.setVisible(false);
            optionD_btn.setVisible(false);
        } else if (ScreensFramework.correctAnswer.equalsIgnoreCase("C")) {
            optionA_btn.setVisible(false);
            optionD_btn.setVisible(false);
        } else if (ScreensFramework.correctAnswer.equalsIgnoreCase("D")) {
            optionB_btn.setVisible(false);
            optionC_btn.setVisible(false);
        }
        fiftyFiftyImageView.setVisible(false);
    }

    @FXML
    private void exitNowAction(ActionEvent event) {
        exitNowCommonAction();
    }

    private void exitNowCommonAction() {
        Boolean result = AlertBoxController.confirmExit("Alert Box", "Do you want to exit?");
        if (result) {
            System.exit(0);
        }
    }

    @FXML
    private void loadQuestionAction(ActionEvent event) {
        ScreensFramework.currentQuestion = 0;
        if (fileEnabled) {
            String questionFileAddress = AlertBoxController.getSelectedQuestionFile();
            questionFile = new File(questionFileAddress);
            loadQuestionCommonAction(questionFile);
        } else {
            loadQuestionCommonAction();
        }

    }

    public void loadQuestionCommonAction(File questionFile) {
        ScreensFramework.currentQuestion = 0;
        timer.stop();
        loadFileQuestionCommonAction(questionFile);
    }

    public void loadQuestionCommonAction() {
        Boolean result = AlertBoxController.confirmExit("Alert Box", "Load New Question ?");
        if (result) {
            Integer questionSetId = AlertBoxController.getSelectedQuestionSet("Load Question For Quiz", "Load");
            if (questionSetId != null) {
                ScreensFramework.questionSetId = questionSetId;
                ScreensFramework.currentQuestion = 0;
                timer.stop();
                ScreensFramework.questions = QuestionsService.getAllQuestions(questionSetId);
                myController.setScreen(ScreensFramework.quizScreen);
            }

        }

    }

    public void loadFileQuestionCommonAction(File questionFile) {
        try {
            if (questionFile.exists()) {
                ScreensFramework.questionFile = questionFile;
            } else {
                questionFile = ScreensFramework.questionFile;
                System.out.println("The Question doesn't exists! loading default question.");
            }
            ScreensFramework.questions = QuizAppUtils.getQuestions(questionFile);
            myController.setScreen(ScreensFramework.quizScreen);
        } catch (IOException ex) {
            Logger.getLogger(HomeScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void editQuestionAction(ActionEvent event) {
        editQuestionCommonAction();
    }

    private void editQuestionCommonAction() {

        if (ScreensFramework.loggedInUser == "admin") {
            Boolean result = AlertBoxController.confirmExit("Alert Box", "Edit question ?");
            if (result) {
                timer.stop();
                Integer questionSetId = ScreensFramework.questionSetId;
                System.out.println(questionSetId);
                if (questionSetId != null) {
                    boolean returnResult = AlertBoxController.questionManager("Edit Question", questionSetId);
                    if (returnResult) {
                        timer.start();
                        commonLoadNextQuestion();
                    }

                } else {
                    timer.start();
                }
            }

        } else {
            //JOptionPane.showMessageDialog(null, "Operator is not authenticated to edit Question.");
        }

    }

    @FXML
    private void goHomScreenAction(ActionEvent event) {
        Boolean result = AlertBoxController.confirmExit("Alert Box", "Do you want stop quiz?");
        if (result) {
            ScreensFramework.currentQuestion = 0;
            timer.stop();
            myController.setScreen(ScreensFramework.homeScreen);
        }

    }

    @FXML
    private void fiftyFiftyAction(MouseEvent event) {
        commonFiftyFiftyAction();
    }

    @FXML
    private void phoneCallAction(MouseEvent event) {
        commonPlayAction(mouseClick_audio);
        phoneIconImageView.setVisible(false);
    }

    @FXML
    private void peopleCallAction(MouseEvent event) {
        commonPlayAction(mouseClick_audio);
        peopleCallImageView.setVisible(false);
    }
}
