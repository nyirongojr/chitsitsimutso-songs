package com.jnrnyirongo.chitsitsimutso;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class FavouriteSongs extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    //Database Handler
    DatabaseHelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_songs);

        //Action bar Back button
        getSupportActionBar().setTitle("Favourite songs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialising the object for accessing the listView which will contain the songs
        listView = (ListView)findViewById(R.id.MylistView);

        //Initialising and Opening the Database
        myDatabase = new DatabaseHelper(this);

        //This will retrieve the list of songs from the favourites database. Now we want to link each song from the list to its designated values
        List<String> fav_songs = myDatabase.getFavouriteSongs();

        //Adapter will handle the list that will be obtained from the database
        adapter = new ArrayAdapter<>(this, R.layout.listview_custom, fav_songs);
        this.listView.setAdapter(adapter);

        //If there are no songs, we will display an Alert box
        if(fav_songs.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("You have not added any song to Favourites yet.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(FavouriteSongs.this, MainActivity.class));
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        //When a song is clicked, its title is sent to a next class where it is viewed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView itemSelected = (TextView) view;
                CharSequence selectedTitle = itemSelected.getText(); //Contains the text that has been selected by tapping
                Intent titleIntent = new Intent(FavouriteSongs.this, ViewSong.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("songTitle", selectedTitle);
                bundle.putCharSequence("activity_name", "com.jnrnyirongo.chitsitsimutso.FavouriteSongs");
                titleIntent.putExtras(bundle);
                startActivity(titleIntent);
            }
        });

    }


    //Overridden methods to handling actionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                toEndActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toEndActivity();
    }

    public void toEndActivity(){
        startActivity(new Intent(FavouriteSongs.this, MainActivity.class));
    }
}
