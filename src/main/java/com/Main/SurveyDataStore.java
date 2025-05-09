package com.Main;

import java.util.ArrayList;
import java.util.List;

public class SurveyDataStore {

    private static SurveyDataStore instance;

    private String title;
    private String description;
    private List<SurveyQuestion> questions;

    private SurveyDataStore() {
        questions = new ArrayList<>();
    }

    public static SurveyDataStore getInstance() {
        if (instance == null) {
            instance = new SurveyDataStore();
        }
        return instance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setQuestions(List<SurveyQuestion> questions) {
        this.questions = questions;
    }

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    public void addQuestion(SurveyQuestion question) {
        this.questions.add(question); // Метод для додавання питання
    }

    // Можна також додати метод для перевірки наявності дозволу на введення власних варіантів
    public boolean isCustomAllowedForAnyQuestion() {
        for (SurveyQuestion question : questions) {
            if (question.isCustomAllowed()) {
                return true; // Якщо хоча б для одного питання дозволено введення власного варіанту
            }
        }
        return false;
    }
}
