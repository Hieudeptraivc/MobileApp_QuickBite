package com.example.foodyapp.Helper;

import android.content.Context;
import android.content.Intent;

public class ActivityHelper {

    public static void startNextActivity(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.trim().toLowerCase().split("\\s+"); // Tách chuỗi thành các từ
        StringBuilder capitalizedText = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalizedText.append(Character.toUpperCase(word.charAt(0))) // Viết hoa chữ cái đầu
                        .append(word.substring(1)) // Giữ nguyên phần còn lại
                        .append(" "); // Thêm khoảng trắng giữa các từ
            }
        }

        return capitalizedText.toString().trim(); // Loại bỏ khoảng trắng thừa ở cuối chuỗi
    }

}