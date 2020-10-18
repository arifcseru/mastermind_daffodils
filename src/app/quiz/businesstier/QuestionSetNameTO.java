package app.quiz.businesstier;

import java.sql.Date;

public class QuestionSetNameTO {

    private Integer questionSetId;
    private String questionSetName;
    private String questionSetterName;
    private String questionSetDate;

    public Integer getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(Integer questionSetId) {
        this.questionSetId = questionSetId;
    }

    public String getQuestionSetName() {
        return questionSetName;
    }

    public void setQuestionSetName(String questionSetName) {
        this.questionSetName = questionSetName;
    }

    public String getQuestionSetterName() {
        return questionSetterName;
    }

    public void setQuestionSetterName(String questionSetterName) {
        this.questionSetterName = questionSetterName;
    }

    public String getQuestionSetDate() {
        return questionSetDate;
    }

    public void setQuestionSetDate(String questionSetDate) {
        this.questionSetDate = questionSetDate;
    }

}
