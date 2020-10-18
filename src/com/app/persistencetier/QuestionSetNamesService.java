package com.app.persistencetier;

import app.quiz.businesstier.QuestionSetNameTO;
import app.quiz.businesstier.QuestionTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class QuestionSetNamesService {

    public static Integer createQuestionSet(QuestionSetNameTO questionSetNameTO) {
        Integer questionSetId = null;
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "INSERT INTO QuestionSetName (questionSetName,questionSetterName,questionSetDate) VALUES('"
                    + questionSetNameTO.getQuestionSetName()
                    + "','"
                    + questionSetNameTO.getQuestionSetterName()
                    + "','"
                    + questionSetNameTO.getQuestionSetDate()
                    + "')";
            statement.executeUpdate(sql);
            statement.close();
            statement = connection.createStatement();
            questionSetId = statement.getGeneratedKeys().getInt(1);
            //sql = "INSERT INTO roundQuestions (questionId,roundId) VALUES('"+questionId+"','"+roundId+"')";
            // statement.executeUpdate(sql);
            // System.out.println(questionId);
            //System.out.println();
            //statement.close();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        //System.out.println("dksjaf");
        return questionSetId;
    }

    public static List<QuestionSetNameTO> getAllQuestionSets(Integer limit) {
        Connection connection = null;
        Statement statement = null;
        QuestionSetNameTO questionSetNameTO = null;
        List<QuestionSetNameTO> questionSets = new ArrayList<QuestionSetNameTO>();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "SELECT * FROM QuestionSetName";
            //c.prepareStatement(sql, roundId);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Integer questionSetId = Integer.parseInt(rs.getString("questionSetId"));
                List<QuestionTO> questions = QuestionsService.getAllQuestions(questionSetId);
                if (questions.size() >= limit) {
                    questionSetNameTO = new QuestionSetNameTO();
                    questionSetNameTO.setQuestionSetId(Integer.parseInt(rs.getString("questionSetId")));
                    questionSetNameTO.setQuestionSetName(rs.getString("questionSetName"));
                    questionSetNameTO.setQuestionSetterName(rs.getString("questionSetterName"));
                    questionSetNameTO.setQuestionSetDate(rs.getString("questionSetDate"));
                    questionSets.add(questionSetNameTO);
                }
            }
            statement.close();
            connection.close();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return (ArrayList<QuestionSetNameTO>) questionSets;
    }
    public static QuestionSetNameTO getQuestionSetDetails(Integer questionSetId){
        Connection connection = null;
        Statement statement = null;
        QuestionSetNameTO questionSetNameTO = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "SELECT * FROM QuestionSetName WHERE questionSetId = '"+questionSetId+"'";
            //c.prepareStatement(sql, roundId);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                questionSetId = Integer.parseInt(rs.getString("questionSetId"));
                questionSetNameTO = new QuestionSetNameTO();
                questionSetNameTO.setQuestionSetId(Integer.parseInt(rs.getString("questionSetId")));
                questionSetNameTO.setQuestionSetName(rs.getString("questionSetName"));
                questionSetNameTO.setQuestionSetterName(rs.getString("questionSetterName"));
                questionSetNameTO.setQuestionSetDate(rs.getString("questionSetDate"));
            }
            statement.close();
            connection.close();

        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return questionSetNameTO;
    }
    public static void deleteQuestionSetDetails(Integer questionSetId) {
         Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager
                    .getConnection("jdbc:sqlite:MultimediaQuiz_db.db");
            statement = connection.createStatement();
            String sql = "DELETE FROM QuestionSetName WHERE questionSetId = '"+questionSetId+"'";
            statement.execute(sql);
            statement.close();
            connection.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
    public static void main(String[] args) {
        List<QuestionSetNameTO> questionSets = new QuestionSetNamesService().getAllQuestionSets(1);
        for (QuestionSetNameTO questionSet : questionSets) {
            System.out.println(questionSet.getQuestionSetId() + ":>>" + questionSet.getQuestionSetName());
        }
    }

    
}
