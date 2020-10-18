/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.presentationtier;

import app.quiz.businesstier.QuestionSetNameTO;
import app.quiz.businesstier.QuestionTO;
import app.quiz.businesstier.QuizUtils;
import com.app.persistencetier.QuestionSetNamesService;
import com.app.persistencetier.QuestionsService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * FXML Controller class
 *
 * @author Arif
 */
public class AlertBoxController implements ActionListener, Initializable {

    private static ObservableList<QuestionTO> getQuestions(Integer questionSetId) {
        ObservableList<QuestionTO> questions = null;
        List<QuestionTO> questionsList = QuestionsService.getAllQuestions(questionSetId);
        questions = FXCollections.observableArrayList(questionsList);

        return questions;
    }

    private static String getQuestionSetName(Integer questionSetId) {
        String questionSetName = null;
        QuestionSetNameTO questionSetNameTO = QuestionSetNamesService.getQuestionSetDetails(questionSetId);
        questionSetName = questionSetNameTO.getQuestionSetName();
        return questionSetName;
    }

    private static void getTablesRowValues(QuestionTO question) {
        if (question != null) {
            itemsListView.getSelectionModel().clearSelection();

            //qsSequenceIdTf.setText(question.getQsSequenceId().toString());
            questionDetailTa.setText(question.getQuestionDetail());

            optionATa.setText(question.getOptionA());
            optionBTa.setText(question.getOptionB());
            optionCTa.setText(question.getOptionC());
            optionDTa.setText(question.getOptionD());

            String correctAnswer = question.getCorrectOption();
            correctOptionTf.setText(correctAnswer);
            if (correctAnswer.equalsIgnoreCase("A")) {
                aRadio.setSelected(true);
            } else if (correctAnswer.equalsIgnoreCase("B")) {
                bRadio.setSelected(true);
            } else if (correctAnswer.equalsIgnoreCase("C")) {
                cRadio.setSelected(true);
            } else if (correctAnswer.equalsIgnoreCase("D")) {
                dRadio.setSelected(true);
            }
            String fLocation = question.getFileLocation();

            itemsListView.getSelectionModel().select(fLocation);
            fileLocationTf.setText(question.getFileLocation());
        } else {
            clearFields();
        }
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static void addButtonClicked() {
        //String sequenceId = qsSequenceIdTf.getText();
        if (questionDetailTa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Set Question.");
        } else if (optionATa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Set option A.");
        } else if (optionBTa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Set option B.");
        } else if (correctOptionTf.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Set Correct Option.");
        } else {
            QuestionTO question = new QuestionTO();
            question.setQuestionDetail(questionDetailTa.getText());
            question.setOptionA(optionATa.getText());
            question.setOptionB(optionBTa.getText());
            question.setOptionC(optionCTa.getText());
            question.setOptionD(optionDTa.getText());
            question.setCorrectOption(correctOptionTf.getText());
            String fileName = null;

            if (!itemsListView.getSelectionModel().isEmpty()) {
                fileName = itemsListView.getSelectionModel().getSelectedItem();
            }
            if (fileName != null) {
                question.setFileLocation(fileName);
            } else {
                question.setFileLocation("text");
            }

            myTable.getItems().add(question);
            /////////////
            List<QuestionTO> questionList = myTable.getItems();

            List<QuestionTO> newQsList = new ArrayList();

            int i = 0;
            for (QuestionTO questionTO : questionList) {
                questionTO.setQsSequenceId(i + 1);
                newQsList.add(questionTO);
                i++;
            }
            myTable.getItems().setAll(newQsList);

            clearFields();
        }

    }

    private static void clearButtonClicked() {
        clearFields();
    }

    private static void clearFields() {
        successfullLabel.setVisible(false);
        questionDetailTa.clear();
        optionATa.clear();
        optionBTa.clear();
        optionCTa.clear();
        optionDTa.clear();

        aRadio.setSelected(false);
        bRadio.setSelected(false);
        cRadio.setSelected(false);
        dRadio.setSelected(false);
        itemsListView.getSelectionModel().clearSelection();
        correctOptionTf.clear();
        myTable.getSelectionModel().clearSelection();
        if (cancelButton != null) {
            cancelButton.setText("Close");
        }

    }

    private static void updateButtonClicked(QuestionTO questionTO) {
        int rowId = myTable.getSelectionModel().getSelectedIndex();
        myTable.getItems().set(rowId, questionTO);

        /////////////
        List<QuestionTO> questionList = myTable.getItems();

        List<QuestionTO> newQsList = new ArrayList();

        int i = 0;
        for (QuestionTO newQuestionTO : questionList) {
            newQuestionTO.setQsSequenceId(i + 1);
            newQsList.add(newQuestionTO);
            i++;
        }
        myTable.getItems().setAll(newQsList);

        clearFields();
    }

    private static void deleteButtonClicked() {
        boolean result = confirmExit("Confirm", "You want to delete ?");
        if (result) {
            ObservableList<QuestionTO> allQuestions, questionSelected;
            allQuestions = myTable.getItems();
            questionSelected = myTable.getSelectionModel().getSelectedItems();
            questionSelected.forEach(allQuestions::remove);
            /////////////
            List<QuestionTO> questionList = myTable.getItems();

            List<QuestionTO> newQsList = new ArrayList();

            int i = 0;
            for (QuestionTO questionTO : questionList) {
                questionTO.setQsSequenceId(i + 1);
                newQsList.add(questionTO);
                i++;
            }
            myTable.getItems().setAll(newQsList);
            /////////////////////////
        }
    }

    private static void saveQuestions() {
        List<QuestionTO> questions = myTable.getItems();
        if (questionSetNameTf.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Enter Question Set Name");
        } else if (questions.size() < 1) {
            JOptionPane.showMessageDialog(null, "Please Enter At least one question.");
        } else if (!questionsIsNotEmpty(questions)) {
            JOptionPane.showMessageDialog(null, "Please Observe Questions Empty filled detected.");
        } else {
            QuestionSetNameTO questionSetNameTO = new QuestionSetNameTO();
            questionSetNameTO.setQuestionSetName(questionSetNameTf.getText());
            questionSetNameTO.setQuestionSetterName("Arif");
            QuestionsService.createQuestions(questions, questionSetNameTO);

            myTable.getItems().removeAll(questions);
            successfullLabel.setText("Question Saved Successfully");
            successfullLabel.getStyleClass().add("shiny-orange");
            successfullLabel.setVisible(true);
            if (cancelButton != null) {
                cancelButton.setText("Close");
            }
            //clearFields();
        }

    }

    private static boolean questionsIsNotEmpty(List<QuestionTO> questions) {
        boolean notEmpty = false;
        for (QuestionTO question : questions) {
            if (!question.getQsSequenceId().toString().isEmpty()
                    && !question.getQuestionDetail().toString().isEmpty()
                    && !question.getOptionA().toString().isEmpty()
                    && !question.getOptionB().toString().isEmpty()
                    && !question.getCorrectOption().toString().isEmpty()
                    && !question.getFileLocation().toString().isEmpty()) {
                notEmpty = true;
            }
        }
        return notEmpty;
    }

    private static void updateQuestions(Integer questionSetId) {
        List<QuestionTO> questions = myTable.getItems();
        if (questions.size() >= 1 && questionsIsNotEmpty(questions)) {
            QuestionSetNameTO questionSetNameTO = new QuestionSetNameTO();
            questionSetNameTO.setQuestionSetName(questionSetNameTf.getText());
            questionSetNameTO.setQuestionSetterName("Arif");

            QuestionsService.deleteSetQuestion(questionSetId);
            QuestionsService.createQuestions(questions, questionSetNameTO);
            //QuestionsService.saveQuestions(questions, questionSetId);
            myTable.getItems().removeAll(questions);
            successfullLabel.setText("Question Saved Successfully");
            successfullLabel.getStyleClass().add("shiny-orange");
            successfullLabel.setVisible(true);
            if (cancelButton != null) {
                cancelButton.setText("Close");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please Enter At least one question");
        }
    }

    private Timer timer = new Timer(5000, (ActionListener) this);
    private static MediaView mediaView;
    private static Image image;
    private static ImageView imageView;
    private static MediaPlayer player;
    private static Media media;
    public static String mediaFileAddress = "";
    static Boolean exitNow = false;
    public static Stage window;
    private static ListView<String> itemsListView;
    private static ListView<String> questionSetNameListView;
    static String questionName = null;
    static String fileAddress = null;
    static Integer questionSetId = null;
    static ObservableList<QuestionTO> myTableData = null;
    static TableView<QuestionTO> myTable = null;
    static Button cancelButton;

    static ToggleGroup group;
    static TextField questionSetNameTf = null;
    static Label successfullLabel;
    static RadioButton aRadio;
    static RadioButton bRadio;
    static RadioButton cRadio;
    static RadioButton dRadio;

    static TextField questionDetailTa;
    static TextField optionATa;
    static TextField optionBTa;
    static TextField optionCTa;
    static TextField optionDTa;
    static TextField correctOptionTf;
    static TextField fileLocationTf;

    static Button addButton;
    static Button updateButton;
    static Button clearButton;
    static Button deleteButton;

    public static String getSelectedQuestionFile() {

        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinWidth(250);
            window.setMinHeight(600);

            VBox layout = new VBox();

            Label titleLabel = new Label("Choose Question File");
            titleLabel.setOnMousePressed(e -> {
                window.close();
            });
            titleLabel.getStyleClass().add("title-area");
            /*titleLabel.setStyle("-fx-background-color:\n"
                    + "        linear-gradient(#f0ff35, #a9ff00),\n"
                    + "        radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);\n"
                    + "    -fx-background-radius: 6, 5;\n"
                    + "    -fx-background-insets: 0, 1;\n"
                    + "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n"
                    + "    -fx-text-fill: #395306;\n"
                    + "    -fx-font-family: \"Arial\";\n"
                    + "    -fx-font: 40px \"Arial\";");*/
            layout.getChildren().addAll(titleLabel);
            itemsListView = new ListView<>();
            //questionAddresses.getStyleClass().add("current-question");
            List<String> questionAddressList = new ArrayList();
            Map<String, Object> questionsFileMap = new LinkedHashMap();

            questionAddressList = QuizUtils.getFileNames(new File("Questions"));
            //if (!questionAddressList.isEmpty()) {
            for (String questionAddress : questionAddressList) {
                questionsFileMap.put(questionAddress, "Questions/" + questionAddress);
            }
            //}

            itemsListView.getItems().setAll(questionAddressList);
            itemsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            layout.getChildren().addAll(itemsListView);
            Button selectButton = new Button("Selecct");
            selectButton.getStyleClass().add("current-question");

            Button closeButton = new Button("Start Quiz");
            closeButton.getStyleClass().add("current-question");
            closeButton.setOnAction(e -> {
                window.close();
            });
            cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e -> {
                window.close();
            });

            Label resultLabel = new Label();
            //final String fileAddress2;
            selectButton.getStyleClass().add("current-question");
            selectButton.setOnAction(e -> {
                questionName = itemsListView.getSelectionModel().getSelectedItem();
                fileAddress = (String) questionsFileMap.get(questionName);
                /*if (fileAddress == null) {
                    System.out.println("Null value cannot be performed!");
                    resultLabel.setText("Null value cannot be performed!");
                } else {
                    System.out.println(fileAddress);
                    resultLabel.setText("Choosen Question is: " + fileAddress);
                    selectButton.setVisible(false);
                    layout.getChildren().addAll(closeButton);
                }*/
                window.close();
            });
            layout.getChildren().addAll(selectButton);
            layout.getChildren().addAll(resultLabel);
            layout.getChildren().addAll(cancelButton);

            layout.setAlignment(Pos.CENTER);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            Scene scene = new Scene(layout, Color.BLACK);

            window.setX(bounds.getWidth() / 10);
            window.setY(bounds.getHeight() / 20);
            window.setWidth(bounds.getWidth() - bounds.getWidth() / 4);
            window.setHeight(bounds.getHeight() - bounds.getHeight() / 8);

            window.initStyle(StageStyle.TRANSPARENT);
            window.setScene(scene);
            window.showAndWait();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return fileAddress;
    }
    static List<QuestionSetNameTO> questionSets = null;

    static Map<String, Integer> questionIdMap = null;

    public static Integer getSelectedQuestionSet(String title, String editOrLoad) {
        questionSetId = null;
        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinWidth(250);
            window.setMinHeight(600);

            VBox layout = new VBox();

            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("title-area");
            /*titleLabel.setStyle("-fx-background-color:\n"
                    + "        linear-gradient(#f0ff35, #a9ff00),\n"
                    + "        radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);\n"
                    + "    -fx-background-radius: 6, 5;\n"
                    + "    -fx-background-insets: 0, 1;\n"
                    + "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n"
                    + "    -fx-text-fill: #395306;\n"
                    + "    -fx-font-family: \"Arial\";\n"
                    + "    -fx-font: 40px \"Arial\";");*/

            questionSetNameListView = new ListView<>();
            List<String> questionSetNames = new ArrayList();

            questionIdMap = new LinkedHashMap();
            Integer limit = null;
            if (editOrLoad.equalsIgnoreCase("Edit")) {
                limit = 1;
                //deleteButton.setVisible(true);
            } else if (editOrLoad.equalsIgnoreCase("Load")) {
                limit = 3;
            }
            questionSets = QuestionSetNamesService.getAllQuestionSets(limit);

            for (QuestionSetNameTO questionSet : questionSets) {
                questionSetNames.add(questionSet.getQuestionSetName());
                questionIdMap.put(questionSet.getQuestionSetName(), questionSet.getQuestionSetId());
            }
            questionSetNameListView.getItems().setAll(questionSetNames);
            questionSetNameListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            Button loadButton = new Button("Load");
            loadButton.setOnAction(e -> {
                questionName = questionSetNameListView.getSelectionModel().getSelectedItem();
                questionSetId = questionIdMap.get(questionName);
                ScreensFramework.questionSetId = questionSetId;
                window.close();
            });
            loadButton.getStyleClass().add("current-question");

            deleteButton = new Button("Delete");
            deleteButton.setAlignment(Pos.BOTTOM_LEFT);
            deleteButton.setVisible(false);
            deleteButton.getStyleClass().add("current-question");
            deleteButton.setOnAction(e -> {
                boolean result = confirmExit("Confirm", "You want to delete ?");
                if (result) {
                    questionName = questionSetNameListView.getSelectionModel().getSelectedItem();
                    questionSetId = questionIdMap.get(questionName);
                    QuestionsService.deleteSetQuestion(questionSetId);
                    questionSetNameListView.getItems().remove(questionName);
                }
            });
            if (ScreensFramework.loggedInUser == "admin") {
                deleteButton.setVisible(true);
            }
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(e -> {
                questionSetId = null;
                window.close();
            });
            cancelButton.getStyleClass().add("current-question");

            //final String fileAddress2;
            HBox buttons = new HBox();
            buttons.setAlignment(Pos.CENTER);

            buttons.getChildren().addAll(deleteButton, loadButton, cancelButton);

            buttons.setPadding(new Insets(10, 10, 10, 10));
            buttons.setSpacing(5);

            Label blankLabel = new Label();
            Label blankLabel2 = new Label();
            Label blankLabel3 = new Label();

            layout.getChildren().addAll(titleLabel, blankLabel, questionSetNameListView, blankLabel2, buttons, blankLabel3);

            layout.setPadding(new Insets(10, 10, 10, 10));
            layout.setSpacing(5);

            layout.setAlignment(Pos.CENTER);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            layout.getStyleClass().add("windows7");
            Scene scene = new Scene(layout, Color.BLACK);
            scene.getStylesheets().add("app/quiz/presentationtier/quizApplicationCommonDesign.css");
            //window.setWidth(bounds.getWidth() - bounds.getWidth() / 2);
            //window.setHeight(bounds.getHeight() - bounds.getHeight() / 4);
            window.setWidth(600);
            window.setHeight(400);

            window.setX(bounds.getWidth() / 2 - window.getWidth() / 2);
            window.setY(bounds.getHeight() / 2 - window.getHeight() / 2);

            window.initStyle(StageStyle.TRANSPARENT);
            window.setScene(scene);
            window.showAndWait();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return questionSetId;
    }

    public static boolean displayMedia(String title, String message, String mediaFileAddress) {

        Stage window = new Stage();
        
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        window.setX(bounds.getWidth() / 10);
        window.setY(bounds.getHeight() / 20);

        window.setWidth(bounds.getWidth() - bounds.getWidth() / 4);
        window.setHeight(bounds.getHeight() - bounds.getHeight() / 8);
        window.initStyle(StageStyle.TRANSPARENT);

        AlertBoxController.mediaFileAddress = mediaFileAddress;

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(600);

        VBox layout = new VBox();

        Label titleLabel = new Label(message);
        titleLabel.getStyleClass().add("title-area");
        /*titleLabel.setStyle("-fx-background-color:\n"
                + "        linear-gradient(#f0ff35, #a9ff00),\n"
                + "        radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);\n"
                + "    -fx-background-radius: 6, 5;\n"
                + "    -fx-background-insets: 0, 1;\n"
                + "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n"
                + "    -fx-text-fill: #395306;\n"
                + "    -fx-font-family: \"Arial\";\n"
                + "    -fx-font: 40px \"Arial\";");*/
        layout.getChildren().addAll(titleLabel);
        String lowerCaseAddress = mediaFileAddress.toLowerCase();
        
        if (lowerCaseAddress.contains("mp3") || lowerCaseAddress.contains("wav")) {
            mediaFileAddress = "mediaFiles/" + mediaFileAddress;
            commonPlayAction(mediaFileAddress, window.getHeight(), window.getWidth());
            mediaView.setOnMousePressed(e -> {
                player.dispose();
                window.close();
            });
            commonViewAction("mediaFiles/settings/equalizer.gif", window.getHeight(), window.getWidth());
            imageView.setOnMousePressed(e -> {
                player.dispose();
                window.close();
            });
            layout.getChildren().addAll(imageView, mediaView);
        } else if (lowerCaseAddress.contains("mp4")) {
            mediaFileAddress = "mediaFiles/" + mediaFileAddress;
            commonPlayAction(mediaFileAddress, window.getHeight(), window.getWidth());
            mediaView.setOnMousePressed(e -> {
                player.dispose();
                window.close();
            });
            layout.getChildren().addAll(mediaView);
        } else if (lowerCaseAddress.contains("jpg")|| lowerCaseAddress.contains("jpeg")
                || lowerCaseAddress.contains("png")|| lowerCaseAddress.contains("bmp")
                || lowerCaseAddress.contains("gif")) {
            mediaFileAddress = "mediaFiles/" + mediaFileAddress;

            commonViewAction(mediaFileAddress, window.getHeight(), window.getWidth());
            imageView.setOnMousePressed(e -> {
                window.close();
            });
            layout.getChildren().addAll(imageView);
        }
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("windows7");
        layout.setOnKeyTyped(e -> {
            window.close();
            if (e.getCharacter().equalsIgnoreCase("x")) {
                if (player.isAutoPlay()) {
                    player.dispose();
                }
            }
        });

        Scene scene = new Scene(layout, Color.BLACK);
        scene.getStylesheets().add("app/quiz/presentationtier/quizApplicationCommonDesign.css");
        window.setScene(scene);
        window.showAndWait();
        return true;
    }

    public static Boolean confirmExit(String title, String message) {
        exitNow = false;
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);
        window.setMinHeight(100);
        Label messageLabel = new Label(message);
        /*messageLabel.setStyle("-fx-background-color: green;\n"
                + "    -fx-background-radius: 10;\n"
                + "    -fx-background-insets: 0;\n"
                + "    -fx-text-fill: white;\n"
                + "    -fx-font-family: \"Arial\";\n"
                + "    -fx-font: 35px \"Arial\";");*/
        messageLabel.getStyleClass().add("green-red-border");

        Label yesLabel = new Label("Yes");
        /*yesLabel.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);\n"
                + "    -fx-background-radius: 10;\n"
                + "    -fx-background-insets: 0;\n"
                + "    -fx-text-fill: white;\n"
                + "    -fx-font-family: \"Arial\";\n"
                + "    -fx-font: 30px \"Arial\";");*/
        
        yesLabel.getStyleClass().add("red-white-border");
        
        yesLabel.setOnMousePressed(e -> {
            exitNow = true;
            window.close();
            //System.exit(0);
        });

        Label noLabel = new Label("No");
        /*noLabel.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00);\n"
                + "    -fx-background-radius: 10;\n"
                + "    -fx-background-insets: 0;\n"
                + "    -fx-text-fill: white;\n"
                + "    -fx-font-family: \"Arial\";\n"
                + "    -fx-font: 30px \"Arial\";");*/
        
        noLabel.getStyleClass().add("green-white-border");
        noLabel.setOnMousePressed(e -> {
            exitNow = false;
            window.close();
        });
        VBox layout = new VBox();
        HBox hBox = new HBox();
        
        hBox.getChildren().addAll(yesLabel, noLabel);
        hBox.setPadding(new Insets(10, 30, 10, 30));
        hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER);
        
        layout.getChildren().addAll(messageLabel, hBox);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setSpacing(5);
            
        layout.setAlignment(Pos.CENTER);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Group root = new Group();
        layout.setOnKeyTyped(e -> {
            if (e.getCharacter().equalsIgnoreCase("y")) {
                exitNow = true;
                window.close();
            } else if (e.getCharacter().equalsIgnoreCase("n")) {
                exitNow = false;
                window.close();
            }
        });
        layout.setOnKeyPressed(e -> {
            if (e.getCharacter().equalsIgnoreCase("y")) {
                exitNow = true;
                window.close();
            } else if (e.getCharacter().equalsIgnoreCase("n")) {
                exitNow = false;
                window.close();
            }
        });
        root.getChildren().add(layout);

        window.setWidth(400);
        window.setHeight(200);

        Double width = (bounds.getWidth() / 2) - window.getWidth() / 2;
        Double height = (bounds.getHeight() / 2) - window.getHeight() / 2;
        root.getStyleClass().add("windows7");
        Scene scene = new Scene(root, 400, 200, Color.TRANSPARENT);
        scene.getStylesheets().add("app/quiz/presentationtier/quizApplicationCommonDesign.css");
        window.setX((bounds.getWidth() / 2) - window.getWidth() / 2);
        window.setY((bounds.getHeight() / 2) - window.getHeight() / 2);

        window.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.setAlwaysOnTop(true);
        //window.setMaximized(true);
        //window.setFullScreen(true);
        window.setScene(scene);
        window.showAndWait();

        return exitNow;
    }
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    public static boolean questionManager(String title, Integer questionSetId) {
        Stage window = new Stage();
        try {
            window.initModality(Modality.APPLICATION_MODAL);

            window.setMinWidth(250);
            window.setMinHeight(600);

            VBox layout = new VBox();
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("title-area");
            /*titleLabel.setStyle("-fx-background-color:\n"
                    + "        linear-gradient(#f0ff35, #a9ff00),\n"
                    + "        radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%);\n"
                    + "    -fx-background-radius: 6, 5;\n"
                    + "    -fx-background-insets: 0, 1;\n"
                    + "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n"
                    + "    -fx-text-fill: #395306;\n"
                    + "    -fx-font-family: \"Arial\";\n"
                    + "    -fx-font: 40px \"Arial\";");*/

            HBox questionSetterInfo = new HBox();
            Label label2 = new Label("Enter Question Set Name:");
            questionSetNameTf = new TextField();
            questionSetNameTf.setPromptText("Question Set Name");
            Button saveButton = new Button("Save");

            if (questionSetId != null) {
                saveButton.setText("Update");
                saveButton.setOnAction(e -> {
                    updateQuestions(questionSetId);
                });
            } else {
                saveButton.setOnAction(e -> {
                    saveQuestions();
                });
            }

            saveButton.setAlignment(Pos.TOP_RIGHT);
            cancelButton = new Button("Cancel");

            cancelButton.setAlignment(Pos.TOP_RIGHT);
            cancelButton.setOnAction(e -> {
                Boolean answer = confirmExit("Do you want to exit", "Do you Want to exit ?");
                if (answer) {
                    window.close();
                }
            });
            successfullLabel = new Label("Successfully Question Saved");
            successfullLabel.setVisible(false);
            questionSetterInfo.getChildren().addAll(label2, questionSetNameTf, saveButton, cancelButton, successfullLabel);
            questionSetterInfo.setPadding(new Insets(10, 10, 10, 10));
            questionSetterInfo.setSpacing(5);

            myTable = new TableView<QuestionTO>();
            if (questionSetId != null) {
                myTableData = getQuestions(questionSetId);
                myTable.getItems().setAll(myTableData);
                String questionSetName = getQuestionSetName(questionSetId);
                questionSetNameTf.setText(questionSetName);
            } else {

            }

            TableColumn qsSequenceId = new TableColumn("id");
            qsSequenceId.setPrefWidth(50);
            qsSequenceId.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("qsSequenceId"));
            /*qsSequenceId.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, Integer>() {

                        @Override
                        public void updateItem(Integer item, boolean empty) {
                            //super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data

                                setText(item.toString());
                            } else {
                                this.setFont(Font.font("Vrinda", 20));
                            }

                        }
                    };
                }
            });*/

            TableColumn questionDetail = new TableColumn("Question Detail");
            questionDetail.setPrefWidth(390);
            questionDetail.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("questionDetail"));
            /*questionDetail.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data
                                if (item.contains("@")) {
                                    this.setTextFill(Color.BLUEVIOLET);
                                }
                                setText(item);
                            }
                        }
                    };
                }
            });*/

            TableColumn optionA = new TableColumn("Option A");
            optionA.setPrefWidth(190);
            optionA.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("optionA"));
            /*optionA.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data
                                if (item.contains("@")) {
                                    this.setTextFill(Color.BLUEVIOLET);
                                }
                                setText(item);
                            }
                        }
                    };
                }
            });*/
            TableColumn optionB = new TableColumn("Option B");
            optionB.setPrefWidth(190);
            optionB.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("optionB"));
            /*optionB.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data
                                if (item.contains("@")) {
                                    this.setTextFill(Color.BLUEVIOLET);
                                }
                                setText(item);
                            }
                        }
                    };
                }
            });*/
            TableColumn optionC = new TableColumn("Option C");
            optionC.setPrefWidth(190);
            optionC.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("optionC"));
            /*optionC.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data
                                if (item.contains("@")) {
                                    this.setTextFill(Color.BLUEVIOLET);
                                }
                                setText(item);
                            }
                        }
                    };
                }
            });*/
            TableColumn optionD = new TableColumn("Option D");
            optionD.setPrefWidth(190);
            optionD.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("optionD"));
            /*optionD.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data
                                if (item.contains("@")) {
                                    this.setTextFill(Color.BLUEVIOLET);
                                }
                                setText(item);
                            }
                        }
                    };
                }
            });*/
            TableColumn fileLocation = new TableColumn("File");
            fileLocation.setPrefWidth(60);
            fileLocation.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("fileLocation"));
            /*correctOption.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data
                                if (item.contains("@")) {
                                    this.setTextFill(Color.BLUEVIOLET);
                                }
                                setText(item);
                            }
                        }
                    };
                }
            });*/
            TableColumn correctOption = new TableColumn("C/A");
            correctOption.setPrefWidth(40);
            correctOption.setCellValueFactory(new PropertyValueFactory<QuestionTO, String>("correctOption"));
            /*correctOption.setCellFactory(new Callback<TableColumn, TableCell>() {
                public TableCell call(TableColumn param) {
                    return new TableCell<QuestionTO, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            this.setFont(Font.font("Vrinda", 20));
                            if (!isEmpty()) {
                                this.setTextFill(Color.RED);
                                // Get fancy and change color based on data
                                if (item.contains("@")) {
                                    this.setTextFill(Color.BLUEVIOLET);
                                }
                                setText(item);
                            }
                        }
                    };
                }
            });*/
            //myTable.setItems(myTableData);
            myTable.getColumns().addAll(qsSequenceId, questionDetail, optionA, optionB, optionC, optionD, fileLocation, correctOption);

            layout.setPadding(new Insets(10, 10, 10, 10));
            layout.setSpacing(5);

            layout.getChildren().addAll(titleLabel);
            layout.getChildren().addAll(questionSetterInfo);
            layout.getChildren().addAll(myTable);

            questionDetailTa = new TextField();
            questionDetailTa.setPromptText("Enter Question");

            optionATa = new TextField();
            optionATa.setPromptText("Option A");

            optionBTa = new TextField();
            optionBTa.setPromptText("Option B");

            optionCTa = new TextField();
            optionCTa.setPromptText("Option C");

            optionDTa = new TextField();
            optionDTa.setPromptText("Option D");

            correctOptionTf = new TextField();
            correctOptionTf.setPromptText("Correct Option");

            fileLocationTf = new TextField();
            fileLocationTf.setFont(Font.font("Vrinda", 15));
            itemsListView = new ListView<>();
            //questionAddresses.getStyleClass().add("current-question");
            List<String> mediaFilesList = new ArrayList();
            Map<String, Object> questionsFileMap = new LinkedHashMap();

            mediaFilesList = QuizUtils.getMediaFileNames(new File("mediaFiles"));
            //if (!questionAddressList.isEmpty()) {
            for (String questionAddress : mediaFilesList) {
                questionsFileMap.put(questionAddress, "mediaFiles/" + questionAddress);
            }
            //}

            itemsListView.getItems().setAll(mediaFilesList);
            itemsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            MultipleSelectionModel<String> selectionModel = itemsListView.getSelectionModel();
            selectionModel.setSelectionMode(SelectionMode.SINGLE);

            itemsListView.setCellFactory(lv -> {
                ListCell<String> cell = new ListCell<>();
                cell.textProperty().bind(cell.itemProperty());
                cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    itemsListView.requestFocus();
                    if (!cell.isEmpty()) {
                        int index = cell.getIndex();
                        if (selectionModel.getSelectedIndices().contains(index)) {
                            selectionModel.clearSelection(index);
                        } else {
                            selectionModel.select(index);
                        }
                        event.consume();
                    }
                });
                return cell;
            });

            fileLocationTf.setPrefSize(200, 20);
            fileLocationTf.setPromptText("File Name");

            questionDetailTa.setPrefSize(900, 20);
            questionDetailTa.setFont(Font.font("Vrinda", 15));

            optionATa.setFont(Font.font("Vrinda", 15));
            optionATa.setPrefSize(400, 20);

            optionBTa.setFont(Font.font("Vrinda", 15));
            optionBTa.setPrefSize(400, 20);

            optionCTa.setFont(Font.font("Vrinda", 15));
            optionCTa.setPrefSize(400, 20);

            optionDTa.setFont(Font.font("Vrinda", 15));
            optionDTa.setPrefSize(400, 20);

            correctOptionTf.setFont(Font.font("Vrinda", 15));
            correctOptionTf.setPrefSize(30, 20);
            correctOptionTf.setEditable(false);
            group = new ToggleGroup();

            aRadio = new RadioButton();
            aRadio.setToggleGroup(group);
            aRadio.setOnAction(e -> {
                correctOptionTf.setText("A");
            });
            //aRadio.setSelected(true);

            bRadio = new RadioButton();
            bRadio.setOnAction(e -> {
                correctOptionTf.setText("B");
            });
            bRadio.setToggleGroup(group);

            cRadio = new RadioButton();
            cRadio.setOnAction(e -> {
                correctOptionTf.setText("C");
            });
            cRadio.setToggleGroup(group);

            dRadio = new RadioButton();
            dRadio.setOnAction(e -> {
                correctOptionTf.setText("D");
            });
            dRadio.setToggleGroup(group);

            HBox questionHBox = new HBox();
            questionHBox.getChildren().addAll(questionDetailTa, correctOptionTf);
            questionHBox.setPadding(new Insets(10, 10, 10, 10));
            questionHBox.setSpacing(5);

            clearButton = new Button("Clear");
            clearButton.setOnAction(e -> clearButtonClicked());
            addButton = new Button("Add");
            addButton.setOnAction(e -> addButtonClicked());
            updateButton = new Button("Update");
            updateButton.setOnAction(e -> {
                QuestionTO questionTO = new QuestionTO();
                //Integer sequenceId = Integer.valueOf(qsSequenceIdTf.getText());
                //questionTO.setQsSequenceId(sequenceId);
                questionTO.setQuestionDetail(questionDetailTa.getText());
                questionTO.setOptionA(optionATa.getText());
                questionTO.setOptionB(optionBTa.getText());
                questionTO.setOptionC(optionCTa.getText());
                questionTO.setOptionD(optionDTa.getText());
                questionTO.setCorrectOption(correctOptionTf.getText());

                String fileName = null;
                if (!itemsListView.getSelectionModel().isEmpty()) {
                    fileName = itemsListView.getSelectionModel().getSelectedItem();
                }
                if (fileName != null) {
                    questionTO.setFileLocation(fileName);
                } else {
                    questionTO.setFileLocation("text");
                }

                updateButtonClicked(questionTO);
            });
            deleteButton = new Button("Delete");

            //addButton.getStyleClass().add("current-question");
            //clearButton.getStyleClass().add("current-question");
            //updateButton.getStyleClass().add("current-question");
            //deleteButton.getStyleClass().add("current-question");
            deleteButton.setOnAction(e -> deleteButtonClicked());
            HBox upperOptions = new HBox();
            upperOptions.getChildren().addAll(aRadio, optionATa, bRadio, optionBTa, clearButton, addButton, updateButton, deleteButton);
            upperOptions.setPadding(new Insets(10, 10, 10, 10));
            upperOptions.setSpacing(5);
            /////////////////////////
            Integer currentQ = ScreensFramework.currentQuestion;
            if (currentQ != null) {
                myTable.getSelectionModel().select(currentQ);
                QuestionTO questionTO = myTable.getSelectionModel().getSelectedItem();
                if (questionTO != null) {
                    getTablesRowValues(questionTO);
                }

            }

            myTable.setOnMouseClicked(e -> {
                QuestionTO questionTO = myTable.getSelectionModel().getSelectedItem();
                getTablesRowValues(questionTO);
            });
            // this will not work due to drag facility
            myTable.setRowFactory(new Callback<TableView<QuestionTO>, TableRow<QuestionTO>>() {
                @Override
                public TableRow<QuestionTO> call(TableView<QuestionTO> tableView2) {
                    final TableRow<QuestionTO> row = new TableRow<>();
                    row.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            final int index = row.getIndex();
                            if (index >= 0 && index < myTable.getItems().size() && myTable.getSelectionModel().isSelected(index)) {
                                myTable.getSelectionModel().clearSelection();
                                event.consume();
                            }
                        }
                    });
                    return row;
                }
            });
            //////////////// not worked //////
            myTable.setRowFactory(tv -> {
                TableRow<QuestionTO> row = new TableRow<>();

                row.setOnDragDetected(event -> {
                    clearFields();
                    if (!row.isEmpty()) {
                        Integer index = row.getIndex();
                        Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                        db.setDragView(row.snapshot(null, null));
                        ClipboardContent cc = new ClipboardContent();
                        cc.put(SERIALIZED_MIME_TYPE, index);
                        db.setContent(cc);
                        event.consume();
                    }
                });

                row.setOnDragOver(event -> {
                    Dragboard db = event.getDragboard();
                    if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                        if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                            event.consume();
                        }
                    }
                });

                row.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();
                    if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                        int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                        QuestionTO draggedQuestion = myTable.getItems().remove(draggedIndex);

                        int dropIndex;

                        if (row.isEmpty()) {
                            dropIndex = myTable.getItems().size();
                        } else {
                            dropIndex = row.getIndex();
                        }

                        myTable.getItems().add(dropIndex, draggedQuestion);
                        /////////////
                        List<QuestionTO> questionList = myTable.getItems();

                        List<QuestionTO> newQsList = new ArrayList();

                        int i = 0;
                        for (QuestionTO questionTO : questionList) {
                            questionTO.setQsSequenceId(i + 1);
                            newQsList.add(questionTO);
                            i++;
                        }
                        myTable.getItems().setAll(newQsList);
                        ///////////////////
                        event.setDropCompleted(true);
                        myTable.getSelectionModel().select(dropIndex);
                        event.consume();
                    }
                });

                return row;
            });
            /////////////////////////
            HBox lowerOptions = new HBox();
            Button viewMediaButton = new Button("Preview");
            viewMediaButton.setOnAction(e -> {
                String mediaFileAddress = itemsListView.getSelectionModel().getSelectedItem();
                if (mediaFileAddress != null) {
                    displayMedia("Title", "Media Preview", mediaFileAddress);
                }
            });

            lowerOptions.getChildren().addAll(cRadio, optionCTa, dRadio, optionDTa, itemsListView, viewMediaButton);

            lowerOptions.setPadding(new Insets(10, 10, 10, 10));
            lowerOptions.setSpacing(5);

            /*HBox questionControllerBox = new HBox();
            questionControllerBox.getChildren().addAll();
            questionControllerBox.setPadding(new Insets(10, 10, 10, 10));
            questionControllerBox.setSpacing(5);*/
            //questionControllerBox.setAlignment(Pos.BOTTOM_RIGHT);
            VBox questionControllerVBox = new VBox();
            questionControllerVBox.getChildren().addAll(questionHBox, upperOptions, lowerOptions);
            layout.getChildren().addAll(questionControllerVBox);

            layout.getStyleClass().add("windows7");
            layout.setAlignment(Pos.CENTER);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            layout.getStyleClass().add("windows7");

            layout.setOnKeyTyped(e -> {
                if (e.getCharacter().equalsIgnoreCase("x")) {
                    System.out.println("Exiting now... pressed ' " + e.getCharacter() + " '");
                    //window.close();
                }
            });

            Scene scene = new Scene(layout, Color.BLACK);
            scene.getStylesheets().add("app/quiz/presentationtier/quizApplicationCommonDesign.css");
            window.setX(0);
            window.setY(0);
            window.setWidth(bounds.getWidth());
            window.setHeight(bounds.getHeight());

            window.initStyle(StageStyle.TRANSPARENT);
            window.setScene(scene);
            window.showAndWait();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return true;
    }
    static String css;

    public static void intervalScreen(String intervalImageAddress) {
        Stage window = new Stage();
        try {
            window.initModality(Modality.APPLICATION_MODAL);

            window.setMinWidth(250);
            window.setMinHeight(600);

            VBox layout = new VBox();

            Label titleLabel = new Label("Click Here");
            titleLabel.getStyleClass().add("answered-question");
            titleLabel.setOnMouseClicked(e -> {
                window.close();
            });

            layout.getChildren().addAll(titleLabel);
            layout.getStyleClass().add("windows7");
            layout.setOnKeyTyped(e -> {
                if (e.getCharacter().equalsIgnoreCase("x")) {
                    window.close();
                }
            });
            Scene scene = new Scene(layout, Color.BLACK);
            scene.getStylesheets().add("app/quiz/presentationtier/quizApplicationCommonDesign.css");
            //css = window.getClass().getResource("quizApplicationCommonDesign.css").toExternalForm();
            //scene.getStylesheets().add(css);
            window.setX(0);
            window.setY(0);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            window.setWidth(bounds.getWidth());
            window.setHeight(bounds.getHeight());

            window.initStyle(StageStyle.TRANSPARENT);
            window.setScene(scene);
            window.showAndWait();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    static boolean authUser = false;

    public static boolean authenticateUser(String modificationType) {
        Stage window = new Stage();
        authUser = false;
        window.initModality(Modality.APPLICATION_MODAL);

        VBox layout = new VBox();
        HBox titleBox = new HBox();

        Label titleLabel = new Label("Login Authentication");
        titleLabel.getStyleClass().add("title-area");

        titleBox.getChildren().addAll(titleLabel);
        titleBox.setAlignment(Pos.CENTER);

        group = new ToggleGroup();
        RadioButton adminRadio = new RadioButton();
        RadioButton operatorRadio = new RadioButton();

        adminRadio.setToggleGroup(group);
        operatorRadio.setToggleGroup(group);

        Label adminLabel = new Label("Admin");
        Label operatorLabel = new Label("Operator");

        HBox radioButtons = new HBox();
        radioButtons.getChildren().addAll(adminRadio, adminLabel, operatorRadio, operatorLabel);

        radioButtons.setAlignment(Pos.CENTER);
        radioButtons.setPadding(new Insets(10, 10, 10, 10));
        radioButtons.setSpacing(5);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        HBox buttons = new HBox();
        Button authenticateButton = new Button("Authenticate");
        authenticateButton.getStyleClass().add("answered-question");

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("answered-question");
        cancelButton.setOnMouseClicked(e -> {
            window.close();
        });
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(authenticateButton, cancelButton);

        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(5);

        Label wrongPasswordLabel = new Label();
        authenticateButton.setOnMouseClicked(e -> {
            if (adminRadio.isSelected() && password.getText().equals("1122")) {
                authUser = true;
                ScreensFramework.loggedInUser = "admin";
                window.close();
            } else if (operatorRadio.isSelected() && password.getText().equals("1234") && modificationType == "load") {
                authUser = true;
                ScreensFramework.loggedInUser = "operator";
                window.close();
            } else {
                authUser = false;
                wrongPasswordLabel.setText("You are not authenticated!");
            }

        });

        layout.getChildren().addAll(titleBox, radioButtons, password, buttons, wrongPasswordLabel);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setSpacing(5);

        layout.getStyleClass().add("windows7");
        Scene scene = new Scene(layout, Color.BLACK);
        scene.getStylesheets().add("app/quiz/presentationtier/quizApplicationCommonDesign.css");

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        window.setWidth(300);
        window.setHeight(250);

        window.setX(bounds.getWidth() / 2 - window.getWidth() / 2);
        window.setY(bounds.getHeight() / 2 - window.getHeight() / 2);

        window.initStyle(StageStyle.TRANSPARENT);

        window.initStyle(StageStyle.TRANSPARENT);
        window.setScene(scene);
        window.showAndWait();

        return authUser;
    }

    public static void commonPlayAction(String fileAddressStr, Double height, Double width) {
        String fileAddress = new File(fileAddressStr).getAbsolutePath();
        media = new Media(new File(fileAddress).toURI().toString());
        player = new MediaPlayer(media);
        player.stop();

        mediaView = new MediaView(player);
        mediaView.setFitHeight(height);
        mediaView.setFitWidth(width);
        player.play();
    }

    public static void commonViewAction(String fileAddressStr, Double height, Double width) {
        String fileAddress = new File(fileAddressStr).getAbsolutePath();
        image = new Image(new File(fileAddress).toURI().toString());
        imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitHeight(width);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) {

    }
}
