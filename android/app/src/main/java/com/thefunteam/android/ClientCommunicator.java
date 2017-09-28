package com.thefunteam.android;

import android.os.AsyncTask;
import android.util.Base64;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ClientCommunicator {
    private static ClientCommunicator ourInstance = new ClientCommunicator();

    public static ClientCommunicator getInstance() { return ourInstance; }

    private ClientCommunicator() { }

    void get(String path, String body) {

    }

    void post(String path, String body) {
        PostTask post = new PostTask(path, body);
        post.execute();
    }

    private class PostTask extends AsyncTask<Void, Void, Void> {

        String path;
        String body;

        public PostTask(String path, String body) {
            this.path = path;
            this.body = body;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://10.0.2.2:8080" + path);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Host", "localhost");
                connection.setRequestProperty("Accept-Charset", "UTF-8");
                connection.setRequestProperty("Accept", "application/json");

                connection.setInstanceFollowRedirects(false);
                connection.setDoOutput(true);

                connection.connect();

                OutputStream requestBody = connection.getOutputStream();
                requestBody.write(body.getBytes());
                requestBody.close();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream responseBody = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));
                    Scanner scanner = new Scanner(reader);
                    String result = "";
                    while(scanner.hasNext()) {
                        result += scanner.next();
                    }
                    int x = 0;
//                StringResponse response = gson.fromJson(reader, com.dawbros.cs340StringProcessor.StringResponse.class);
//                return response.getValue();
                } else {
                    // SERVER RETURNED AN HTTP ERROR
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e ) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
