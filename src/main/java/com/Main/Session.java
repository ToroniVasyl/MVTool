package com.Main;

public class Session {

    private static Integer currentUserId; // Зберігаємо ID поточного користувача

    // Метод для отримання поточного користувача
    public static Integer getCurrentUserId() {
        return currentUserId;
    }

    // Метод для встановлення поточного користувача
    public static void setCurrentUserId(Integer userId) {
        currentUserId = userId;
    }

    // Очистка поточного користувача при виході
    public static void clear() {
        currentUserId = null;
    }
}
