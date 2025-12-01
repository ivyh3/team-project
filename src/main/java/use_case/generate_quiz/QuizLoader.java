package use_case.generate_quiz;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import entity.Question;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizLoader {
    public static List<Question> loadQuestionsFromJson(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            JsonArray arr = root.getAsJsonArray("questions");
            Type questionListType = new TypeToken<List<Question>>() {}.getType();

            return gson.fromJson(arr, questionListType);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public static List<Question> loadQuestionsFromJsonString(String jsonString) {
        Gson gson = new Gson();
        JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();

        JsonArray arr = root.getAsJsonArray("questions");
        Type questionListType = new TypeToken<List<Question>>() {}.getType();

        return gson.fromJson(arr, questionListType);
    }
}

