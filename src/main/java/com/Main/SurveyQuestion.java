package com.Main;

import java.util.List;
import java.util.ArrayList;

public class SurveyQuestion {
    private String questionText;
    private String type;
    private List<String> options;
    private boolean customAllowed; // Дозвіл на власні варіанти відповіді
    private List<String> answers; // Поле для зберігання відповідей

    // Конструктор з чотирма параметрами
    public SurveyQuestion(String questionText, String type, List<String> options, boolean customAllowed) {
        this.questionText = questionText;
        this.type = type;
        this.options = options;
        this.customAllowed = customAllowed;
        this.answers = new ArrayList<>(); // Ініціалізація списку відповідей
    }

    // Конструктор з трьома параметрами (дозвіл на власні варіанти відповіді буде за замовчуванням false)
    public SurveyQuestion(String questionText, String type, List<String> options) {
        this.questionText = questionText;
        this.type = type;
        this.options = options;
        this.customAllowed = false; // За замовчуванням не дозволяються власні варіанти
        this.answers = new ArrayList<>(); // Ініціалізація списку відповідей
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

    public boolean isCustomAllowed() {
        return customAllowed;
    }

    public List<String> getAnswers() {
        return answers; // Геттер для відповідей
    }

    public void addAnswer(String answer) {
        answers.add(answer); // Метод для додавання відповіді
    }
}
