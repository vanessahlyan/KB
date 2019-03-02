package com.example.vanessayan.kb;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FormReader {
    public static final int UTF_CONVERSION = 8;
    public static final int VERSION = 1;
    private Context mContext;
    /* This function assumes that the form builder app outputs a csv file with lines reading "fieldname, type, max"
     * The form builder app also asks the user for the size of their nfc tag.
     */

    public FormReader(Context context) {
        mContext = context;
//        InputStream source = (InputStream) mContext.getResources().openRawResource(R.raw.sampleresponses);
//        JSONObject read = ReadCSV(source);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public JSONObject ReadCSV(InputStream csvStream) {
        ArrayList<ArrayList> store = new ArrayList<>();
        JSONObject form = new JSONObject();


        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvStream))) {
            int totalBits = 0;
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList field = new ArrayList();
                // use comma as separator
                String[] split = line.split(","); // 3 tokens

                int max = Integer.parseInt(split[2]);
                int size = 0;

                if (split[1].equals("text")) {
                    // utf-8 encoding
                    size = UTF_CONVERSION * max;
                    totalBits += size;
                } else if (split[1].equals("integer")) {
                    //log base 2 of pos int
                    int count = 0;
                    while (max > 0) {
                        count++;
                        max /= 2;
                    }
                    size = count;
                    totalBits += size;
                } else if (split[1].equals("boolean")) {
                    size = 1;
                    totalBits += size;
                }
                field.add(split[0]);
                field.add(split[1]);
                field.add(Integer.toString(size));
                store.add(field);

                form.put("metadata", new JSONObject().put("version", VERSION).put("info", Integer.toString(totalBits)));
                for (int i = 0; i < store.size(); i++) {
                    form.put((String) store.get(i).get(0), new JSONObject().put("type", store.get(i).get(1)).put("size", store.get(i).get(2)));
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return form;
    }
}
