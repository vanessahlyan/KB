package com.example.vanessayan.kb;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FormReader fr = new FormReader(this);
//        Uri fileUri = Uri.parse("content://" + getPackageName() + "/" + "sampleResponses.csv");
//        String filename = fileUri.toString();
        InputStream source = (InputStream) this.getResources().openRawResource(R.raw.sampleresponses);
        JSONObject fields = fr.ReadCSV(source);
        Log.d("fields", fields.toString());
//        JSONObject fields = fr.ReadCSV("E:/Documents/KhushiBaby/sampleResponses.csv");

        try (FileWriter file = new FileWriter("E:/Documents/KhushiBaby/sample.txt")) {
            file.write(fields.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("JSON Object: " + fields.toString() + "\n");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}

