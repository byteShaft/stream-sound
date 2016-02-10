package com.byteshaft.streamsound;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.byteshaft.streamsound.utils.AppGlobals;
import com.byteshaft.streamsound.utils.Helpers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> songsIdsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songsIdsArray = new ArrayList<>();
        new GetSoundDetailsTask().execute();
    }

    class GetSoundDetailsTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            int responseCode = 0;
            if (Helpers.isNetworkAvailable() && Helpers.isInternetWorking()) {
                try {
                    responseCode = Helpers.getRequest(AppGlobals.USER_URL +AppGlobals.CLIENT_KEY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = jsonParser.parse(Helpers.getParsedString())
                            .getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        if (!songsIdsArray.contains(jsonObject.get("id").getAsInt())) {
                            songsIdsArray.add(jsonObject.get("id").getAsInt());
                        }

                    }
                    System.out.println(songsIdsArray);
                }


            }
            return null;
        }
    }
}

