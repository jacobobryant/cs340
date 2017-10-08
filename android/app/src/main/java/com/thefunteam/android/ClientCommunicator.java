package com.thefunteam.android;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.thefunteam.android.model.Atom;
import com.thefunteam.android.model.Model;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClientCommunicator {
    String url = "http://10.0.2.2:8080";

    private static ClientCommunicator ourInstance = new ClientCommunicator();

    public static ClientCommunicator getInstance() { return ourInstance; }

    private ClientCommunicator() { }

    void get(final String path, final String body) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL(ClientCommunicator.this.url + path);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Host", "localhost");
                    connection.setRequestProperty("Accept-Charset", "UTF-8");
                    connection.setRequestProperty("Accept", "application/json");

                    connection.setInstanceFollowRedirects(false);
                    connection.setDoInput(true);
                    connection.setDoInput(true);

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

            Gson gson = new Gson();
            Model model = gson.fromJson(reader, Model.class);
//            Model model = gson.fromJson(this.mockData, Model.class);
            Atom.getInstance().setModel(model);
        } else {
            // SERVER RETURNED AN HTTP ERROR
        }
    }
}
