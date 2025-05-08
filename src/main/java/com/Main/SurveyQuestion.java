package com.Main;

import java.util.List;

public class SurveyQuestion {
    private String questionText;
    private String type;
    private List<String> options;

    public SurveyQuestion(String questionText, String type, List<String> options) {
        this.questionText = questionText;
        this.type = type;
        this.options = options;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }
}
