package com.customerapp.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final Gson gson = new Gson();
    private static String token = null;

    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }


    public static String login(String email, String password) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);

        String response = request("POST", "/auth/login", body.toString(), false);
        JsonObject json = gson.fromJson(response, JsonObject.class);

        if (json.has("access_token")) {
            token = json.get("access_token").getAsString();
            return token;
        }

        throw new Exception(json.has("message")
                ? json.get("message").getAsString()
                : "Login failed");
    }


    public static List<com.customerapp.model.Customer> getAll(int page) throws Exception {
        String response = request("GET", "/customers?page=" + page + "&size=10", null, true);
        JsonObject json = gson.fromJson(response, JsonObject.class);
        JsonArray results = json.getAsJsonArray("results");
        Type listType = new TypeToken<List<com.customerapp.model.Customer>>() {}.getType();
        return gson.fromJson(results, listType);
    }

    public static int getTotalPages(int page) throws Exception {
        String response = request("GET", "/customers?page=" + page + "&size=10", null, true);
        JsonObject json = gson.fromJson(response, JsonObject.class);
        int count = json.get("count").getAsInt();
        return (int) Math.ceil(count / 10.0);
    }

    public static com.customerapp.model.Customer getById(int id) throws Exception {
        String response = request("GET", "/customers/" + id, null, true);
        return gson.fromJson(response, com.customerapp.model.Customer.class);
    }

    public static com.customerapp.model.Customer create(
            String name, String email, String phone, String password
    ) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        body.addProperty("email", email);
        body.addProperty("phone", phone);
        body.addProperty("password", password);

        String response = request("POST", "/customers", body.toString(), true);
        JsonObject json = gson.fromJson(response, JsonObject.class);

        if (json.has("message")) {
            throw new Exception(json.get("message").getAsString());
        }
        return gson.fromJson(response, com.customerapp.model.Customer.class);
    }

    public static com.customerapp.model.Customer update(
            int id, String name, String email, String phone, String password
    ) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        body.addProperty("email", email);
        body.addProperty("phone", phone);
        body.addProperty("password", password); 

        String response = request("PUT", "/customers/" + id, body.toString(), true);
        JsonObject json = gson.fromJson(response, JsonObject.class);

        if (json.has("message")) {
            throw new Exception(json.get("message").getAsString());
        }
        return gson.fromJson(response, com.customerapp.model.Customer.class);
    }

    public static void delete(int id) throws Exception {
        request("DELETE", "/customers/" + id, null, true);
    }


    private static String request(
            String method, String path, String body, boolean auth
    ) throws Exception {
        try {
            URL url = new URL(BASE_URL + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (auth && token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            if (body != null) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }
            }

            int status = conn.getResponseCode();
            InputStream is = status >= 400
                    ? conn.getErrorStream()
                    : conn.getInputStream();

            if (is == null) return "{}";

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                return sb.toString();
            }

        } catch (ConnectException e) {
            throw new Exception("API is unreachable. Make sure the server is running on port 8080.");
        }
    }

    public static void register(
            String name, String email, String phone, String password
    ) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        body.addProperty("email", email);
        body.addProperty("phone", phone);
        body.addProperty("password", password);

        String response = request("POST", "/auth/register", body.toString(), false);
        JsonObject json = gson.fromJson(response, JsonObject.class);

        if (json.has("message") && !json.get("message").getAsString().equals("User registered successfully")) {
            throw new Exception(json.get("message").getAsString());
        }
    }
}