package com.jnrnyirongo.chitsitsimutso;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {

    private Spinner font, readingMode;

    public static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Action bar Back button
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //********************** SETTINGS FOR THE FONT ****************************************************************************
        font = (Spinner) findViewById(R.id.spinner_font_size);

        //string array of the items that we want to display as a drop down list where a user will choose
        String[] fontItems = new String[]{"Small Text", "Normal Text", "Large Text", "Huge Text"};

        //Takes the items from the items array and sets them in a way that they will be set to appear in some older
        ArrayAdapter<String> FontAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, fontItems);

        //set the spinners adapter to the previously created one.
        font.setAdapter(FontAdapter);

        //Lets retrieve the Shared preferences that is being stored
        preferences = getSharedPreferences("mySavedData", 0);
        int retrievedData = preferences.getInt("myValue", 20);

        //Initialising the dropdown spinner using data obtained from shared preferences
        int itemPosition = 0;
        if(retrievedData == 20){
            itemPosition =0;
        }
        else if(retrievedData == 25){
            itemPosition = 1;
        }
        else if(retrievedData == 30){
            itemPosition = 2;
        }
        else if(retrievedData == 35){
            itemPosition =3;
        }
        font.setSelection(itemPosition);

        //Now we are setting the Listeners which also will help in saving data in the shared pref
        font.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int font = 20;//Default font

                if(position == 0){
                    font = 20;
                }
                else if(position == 1){
                    font = 25;
                }
                else if(position == 2){
                    font = 30;
                }
                else if(position == 3){
                    font = 35;
                }

                //Toast.makeText(Settings.this, "Success: Font set to "+ selectedFont, Toast.LENGTH_SHORT).show();

                preferences = getSharedPreferences("mySavedData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("myValue", font);
                editor.commit();

                //startActivity(new Intent(Settings.this, MainActivity.class));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //********************** SETTINGS FOR THE FONT ENDS HERE ****************************************************************************


        //********************** SETTINGS FOR THE READING MODE ****************************************************************************
        readingMode = (Spinner) findViewById(R.id.spinner_read_mode);

        //string array of the items that we want to display as a drop down list where a user will choose
        String[] mode = new String[]{"Day Mode", "Night Mode"};

        //Takes the items from the items array and sets them in a way that they will be set to appear in some older
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, mode);

        //set the spinners adapter to the previously created one.
        readingMode.setAdapter(modeAdapter);

        //Lets retrieve the Shared preferences that is being stored
        preferences = getSharedPreferences("mySavedData", 0);
        String retrievedMode = preferences.getString("myMode", "Day Mode");

        //Initialising the dropdown spinner using data obtained from shared preferences
        int modePosition = 0;
        if(retrievedMode == "Day Mode"){
            modePosition =0;
        }
        else if(retrievedMode == "Night Mode"){
            modePosition = 1;
        }
        readingMode.setSelection(modePosition);

        //Now we are setting the Listeners which also will help in saving data in the shared pref
        readingMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mode = "Day Mode";//Default Mode

                if(position == 0){
                    mode = "Day Mode";
                }
                else if(position == 1){
                    mode = "Night Mode";
                }

                //Toast.makeText(Settings.this, "Success: Font set to "+ selectedFont, Toast.LENGTH_SHORT).show();

                preferences = getSharedPreferences("mySavedData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("myMode", mode);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //********************** SETTINGS FOR THE READING MODE ****************************************************************************
    }

    //Overridden methods to handling actionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    //Methods ends here

}
