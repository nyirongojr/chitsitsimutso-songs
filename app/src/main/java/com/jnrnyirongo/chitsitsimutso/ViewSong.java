package com.jnrnyirongo.chitsitsimutso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSong extends AppCompatActivity{

    CharSequence receivedSongTitle; //variable to hold data about song Title received from Intent
    static String receivedSongLanguage; //variable to hold data about the songLanguage received from Intent

    CharSequence previousActivity; //To hod data sent by Intent from previous activity

    private static TextView songNumber, songTitle, songBody;
    private static View lineSeparator;
    private static RelativeLayout songViewLayout;

    static String title;
    static String returnedSongNumber;
    static String returnedBody;

    //Database Handler
    DatabaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_song);

        //Setting up the actionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Declaration of the view handlers
        songNumber = (TextView) findViewById(R.id.songNumber);
        songTitle = (TextView) findViewById(R.id.songTitle);
        songBody = (TextView) findViewById(R.id.body);
        songViewLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
        lineSeparator = (View) findViewById(R.id.lineSeparator);

        //Shared preferences that control the font of the body
        SharedPreferences preferences = getSharedPreferences("mySavedData", 0);
        int retrievedData = preferences.getInt("myValue", 20);
        songBody.setTextSize(retrievedData);

        //Shared preferences that control the font of the body
        String modeData = preferences.getString("myMode", "Day Mode");
        if(modeData == "Night Mode"){
            songNumber.setTextColor(Color.WHITE);
            songTitle.setTextColor(Color.WHITE);
            songBody.setTextColor(Color.WHITE);
            songViewLayout.setBackgroundColor(Color.BLACK);
            lineSeparator.setBackgroundColor(Color.WHITE);
        }

        //Getting the data that was send by the Intent that opened this activity
        Bundle intentExtras = getIntent().getExtras();
        receivedSongTitle = intentExtras.getCharSequence("songTitle");
        receivedSongLanguage = intentExtras.getString("songLanguage");

        if(receivedSongLanguage != null) {
            //Here we want to format the title that we obtained so that we can work with it
            String titleString = receivedSongTitle.toString();
            title = titleString.substring(titleString.indexOf(".") + 4);
            title.trim();

            //Fetching the data for songTitle, songNUmber and song Body
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            returnedSongNumber = databaseAccess.getSongNumber(title, receivedSongLanguage);
            returnedBody = databaseAccess.getBody(title, receivedSongLanguage);
            databaseAccess.close();
        }
        else{
            //Getting the data that was send by the Intent that opened this activity
            intentExtras = getIntent().getExtras();
            receivedSongTitle = intentExtras.getCharSequence("songTitle");
            //Here we want to format the title that we obtained so that we can work with it
            title = receivedSongTitle.toString();

            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            returnedSongNumber = databaseAccess.getSongNumber(title);

            returnedBody = databaseAccess.getBody(title);
            databaseAccess.close();
        }

        //Setting the received title on the TextView widget to view it
        songTitle.setText(title);

        //Setting songNumber
        songNumber.setText(returnedSongNumber);

        //setting the songBody
        songBody.setText(Html.fromHtml(returnedBody));

        //Action bar Back button
        getSupportActionBar().setTitle(title.substring(0,1).toUpperCase()+title.substring(1).toLowerCase());//Setting up the song title on the action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //for the favourite button to work we have to invalidate the options menu first
        invalidateOptionsMenu();
    }


    //Overridden methods to handling actionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_song, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Initialising and Opening the Database
        myDatabase = new DatabaseHelper(this);

        if (id == R.id.action_favourite) {
            if(myDatabase.checkIfFavouriteExist(songTitle.getText().toString())){
                myDatabase.deleteItem(songTitle.getText().toString());
                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_favorite);
                item.setIcon(myDrawable);
                Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
            }
            else{
                boolean result = myDatabase.insertData(receivedSongLanguage, songTitle.getText().toString());

                if(result == true){
                    Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
                    //Changing the symbol after clicking to add to favourite
                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_favorite_checked);
                    item.setIcon(myDrawable);
                }
                else{
                    Toast.makeText(this, "Failed to add Song to favourites List", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }

        if (id == R.id.action_share) {
            //launching separate app for sharing
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            String mobile_app_text = "\n\nShared from Chitsitsimutso Mobile App";
            String string_to_share = Html.fromHtml(returnedBody).toString();
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, title+"\n"+string_to_share+" "+mobile_app_text);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        if (id == R.id.action_about) {
            // launch settings activity
            startActivity(new Intent(ViewSong.this, About.class));
            return true;
        }
        if(id == R.id.action_search){

            Intent searchsongIntent = new Intent(ViewSong.this, SearchSong.class);
            if(receivedSongLanguage == null){
                //Do nothing
            }
            else{
                Bundle bundle = new Bundle();
                bundle.putString("songLanguage", receivedSongLanguage);
                searchsongIntent.putExtras(bundle);
                startActivity(searchsongIntent);
            }
        }

        if(id == android.R.id.home){
            finish();
            toBackRefresh();
            return true;
        }

        myDatabase.close();

        return super.onOptionsItemSelected(item);
    }
    //Method ends here


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //Initialising and Opening the Database
        myDatabase = new DatabaseHelper(this);
        //We will check if the song being viewed was added to the favourites list
        if(myDatabase.checkIfFavouriteExist(songTitle.getText().toString())){
            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_favorite_checked);
            menu.findItem(R.id.action_favourite).setIcon(myDrawable);
        }
        myDatabase.close();
        //Checking ends here

        if(receivedSongLanguage == null){
            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_star);
            menu.findItem(R.id.action_search).setIcon(myDrawable);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toBackRefresh();
    }


    //To go back and refresh
    public void toBackRefresh(){
        Bundle intentExtras = getIntent().getExtras();
        previousActivity = intentExtras.getCharSequence("activity_name");

        if(receivedSongLanguage != null){
            //if received songLanguage is set, then go back to song List
            Intent myIntent = new Intent(ViewSong.this, SongList.class);
            Bundle bundle = new Bundle();
            bundle.putString("songLanguage", receivedSongLanguage);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }
        else{
            //Lets now convert the class name received from the Intent into a class name
            if(previousActivity != null)
            {
                String activityName = previousActivity.toString();
                try {
                    Class<?> c = Class.forName(activityName);
                    startActivity(new Intent(ViewSong.this, c));
                } catch (ClassNotFoundException ignored) {
                    Toast.makeText(this, "class Not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
