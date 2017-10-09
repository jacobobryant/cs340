package com.thefunteam.android;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;
import com.thefunteam.android.model.ServerError;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientCommunicator {
//    String url = "http://10.0.2.2:8080";
    String url = "http://192.168.0.105:8080";

    private static ClientCommunicator ourInstance = new ClientCommunicator();

    public static ClientCommunicator getInstance() { return ourInstance; }

    private ClientCommunicator() { }

    void post(final String path, final String body) {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL(ClientCommunicator.this.url + path);
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

                    getResponse(connection);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e ) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void getResponse(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));

            String message = org.apache.commons.io.IOUtils.toString(reader);

            Gson gson = new Gson();
            ServerError error = gson.fromJson(message, ServerError.class);
            if(error.getCode() != null) {
                Atom.getInstance().setError(error.getMessage());
            } else {
                Model model = gson.fromJson(message, Model.class);
                Atom.getInstance().setModel(model);
            }

        } else {
            // SERVER RETURNED AN HTTP ERROR
        }
    }
}
