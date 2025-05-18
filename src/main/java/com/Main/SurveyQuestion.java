package com.Main;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestion {

    private String questionText;
    private String type;                      // «Один варіант», «Декілька варіантів», «Власна відповідь»
    private List<String> options;            // всі варіанти відповіді
    private List<String> selectedAnswers;    // вибрані користувачем відповіді
    private boolean customAllowed;

    // ◀ Конструктор для NewSurveyController (без customAllowed — за замовчуванням false)
    public SurveyQuestion(String questionText, String type, List<String> options) {
        this(questionText, type, options, false);
    }

    // ◀ Повний конструктор
    public SurveyQuestion(String questionText, String type, List<String> options, boolean customAllowed) {
        this.questionText = questionText;
        this.type = type;
        this.options = options != null ? options : new ArrayList<>();
        this.selectedAnswers = new ArrayList<>();
        this.customAllowed = customAllowed;
    }

    // ◀ Старий конструктор з одним аргументом
    public SurveyQuestion(String questionText) {
        this(questionText, null, null, false);
    }

    // ────────────────────────────────────────────────────────────────────

    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }
    public void setOptions(List<String> options) {
        this.options = options;
    }
    public void addOption(String option) {
        this.options.add(option);
    }

    public boolean isCustomAllowed() {
        return customAllowed;
    }
    public void setCustomAllowed(boolean customAllowed) {
        this.customAllowed = customAllowed;
    }

    public List<String> getSelectedAnswers() {
        return selectedAnswers;
    }
    public void setSelectedAnswers(List<String> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }
    public void addAnswer(String answer) {
        this.selectedAnswers.add(answer);
    }
    public void clearSelectedAnswers() {
        this.selectedAnswers.clear();
    }

}
