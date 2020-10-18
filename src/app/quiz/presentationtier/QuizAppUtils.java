/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.quiz.presentationtier;

import app.quiz.businesstier.QuestionTO;
import static app.quiz.businesstier.QuizUtils.browseMultimediaFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Redeemer
 */
public class QuizAppUtils {

    public static String[] getFolderNames(File curDir) {
        String[] folderNames = new String[50];
        int i = 0;
        File[] filesList = curDir.listFiles();
        for (File file : filesList) {
            if (file.isDirectory()) {
                folderNames[i++] = file.getName();
            }
        }
        String[] newFolderNames = new String[i];
        i = 0;
        for (String folderName : folderNames) {
            if (folderName != null) {
                newFolderNames[i++] = folderName;
            }
        }
        return newFolderNames;
    }

    public static String[] getFileNames(File curDir) {
        String[] fileNames = new String[50];
        int i = 0;
        File[] filesList = curDir.listFiles();
        for (File file : filesList) {
            if (file.isFile()) {
                fileNames[i++] = file.getName();
            }
        }
        String[] newfileNames = new String[i];
        i = 0;
        for (String fileName : fileNames) {
            if (fileName != null) {
                newfileNames[i++] = fileName;
            }
        }
        return newfileNames;
    }

    public static File browseQuestionFile() {
        //File fromAddress = browseMultimediaFile();
        Scanner input = new Scanner(System.in);
        File questionFile = null;
        File questionsDirectory = new File("Questions");
        String fileNames[] = QuizAppUtils.getFileNames(questionsDirectory);
        for (int i = 0; i < fileNames.length; i++) {
            System.out.println(i + "." + fileNames[i]);
        }
        System.out.println("Choose One: ");
        questionFile = new File("Questions/" + fileNames[input.nextInt()]);
        return questionFile;
    }

    public static String uploadedFileLocation(String questionFileName, Integer serial) {
        File multimediaFile = new File("MultimediaFiles/" + questionFileName + serial + ".mp3");
        return multimediaFile.getName();
    }

    public static void setNewQuestion() throws IOException {
        Scanner input = new Scanner(System.in);
        String fileName = "";
        Integer totalQuestions = 0;
        System.out.println("Enter File Name: ");
        fileName = input.next();
        List<QuestionTO> questions = new ArrayList();
        File questionFile = new File("Questions/" + fileName + ".csv");
        System.out.println("How Many Questions: ");
        totalQuestions = input.nextInt();
        for (int i = 0; i < totalQuestions; i++) {
            QuestionTO questionTO = new QuestionTO();
            questionTO.setQuestionId(i);
            System.out.println("Enter Question :");
            questionTO.setQuestionDetail(input.next());
            System.out.println("Enter Option A: ");
            questionTO.setOptionA(input.next());
            System.out.println("Enter Option B: ");
            questionTO.setOptionB(input.next());
            System.out.println("Enter Option C: ");
            questionTO.setOptionC(input.next());
            System.out.println("Enter Option D: ");
            questionTO.setOptionD(input.next());
            System.out.println("Enter Correct Option: ");
            questionTO.setCorrectOption(input.next());
            System.out.println("Question Type(0. Text,1. Image,2. Video): ");
            int choice = input.nextInt();
            String type = "";
            if (choice == 0) {
                type = "Text";
            } else if (choice == 1) {
                type = "Image";
            } else if (choice == 2) {
                type = "Video";
            }
            questionTO.setQuestionType(type);
            if (choice == 1 || choice == 2) {
                questionTO.setFileLocation(uploadedFileLocation(fileName, i));
            }
            questions.add(questionTO);
        }
        saveQuestions(questionFile, questions);
    }

    public static void iniatializeQuizApp() {
        List<QuestionTO> questions = new ArrayList();
        File questionFile = new File("Questions/class1.csv");
        try {
            QuestionTO question = new QuestionTO();
            question.setQuestionId(1);
            question.setQuestionDetail("Our National Game?");
            question.setOptionA("Football");
            question.setOptionB("Cricket");
            question.setOptionC("Ha du du");
            question.setOptionD("Kabadi");

            question.setCorrectOption("D");
            question.setFileLocation("none");
            question.setQuestionType("text");
            questions.add(question);

            question = new QuestionTO();
            question.setQuestionId(2);
            question.setQuestionDetail("Our National Flower?");
            question.setOptionA("Rose");
            question.setOptionB("Shapla");
            question.setOptionC("Jaba");
            question.setOptionD("Dolonchapa");

            question.setCorrectOption("B");
            question.setFileLocation("none");
            question.setQuestionType("text");
            questions.add(question);

            saveQuestions(questionFile, questions);
        } catch (IOException ex) {
            Logger.getLogger(QuizAppTester.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    public static int saveQuestions(File questionFile, List<QuestionTO> questions) throws IOException {
        int size = 0;
        String fileString = "";
        fileString += "No.,Type,Question,Option A, Option B, Option C, Option D, Correct Option, File Location\n";
        for (QuestionTO questionTO : questions) {
            fileString += questionTO.getQuestionId() + ",";
            fileString += questionTO.getQuestionType() + ",";
            fileString += questionTO.getQuestionDetail() + ",";
            fileString += questionTO.getOptionA() + ",";
            fileString += questionTO.getOptionB() + ",";
            fileString += questionTO.getOptionC() + ",";
            fileString += questionTO.getOptionD() + ",";
            fileString += questionTO.getCorrectOption() + ",";
            fileString += questionTO.getFileLocation() + "\n";
        }
        FileUtils.writeStringToFile(questionFile, fileString);
        //System.out.println(fileString);
        return questions.size();
    }

    public static List<QuestionTO> getQuestions(File questionFile) throws IOException {
        List<QuestionTO> questions = new ArrayList();
        String fileString = FileUtils.readFileToString(questionFile);
        String questionRows[] = fileString.split("\n");
        for (int i = 0; i < questionRows.length; i++) {
            String csvStrings[] = questionRows[i].split(",");
            QuestionTO question = new QuestionTO();

            question.setQuestionId(Integer.parseInt(csvStrings[0]));
            question.setQuestionDetail(csvStrings[1]);
            question.setOptionA(csvStrings[2]);
            question.setOptionB(csvStrings[3]);
            question.setOptionC(csvStrings[4]);
            question.setOptionD(csvStrings[5]);

            question.setCorrectOption(csvStrings[6]);
            question.setFileLocation(csvStrings[7]);

            questions.add(question);
        }
        return questions;
    }

    public static int showQuestions(List<QuestionTO> questions) {
        int size = 0;
        for (QuestionTO questionTO : questions) {
            System.out.println(questionTO.getQuestionDetail());
        }
        return size;
    }

    public static void startQuiz(List<QuestionTO> questions) {
        Scanner input = new Scanner(System.in);

        for (QuestionTO questionTO : questions) {
            System.out.println(questionTO.getQuestionDetail());
            System.out.println("A: " + questionTO.getOptionA() + "\n" + "B: " + questionTO.getOptionB() + "\n" + "C: " + questionTO.getOptionC() + "\n" + "D: " + questionTO.getOptionD());
            System.out.print("Choose:");
            String answer = input.next();
            if (answer.equalsIgnoreCase(questionTO.getCorrectOption())) {
                System.out.println("Correct");
            } else {
                System.out.println("Incorrect!");
            }
        }
    }

}
