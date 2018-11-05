package com.jnrnyirongo.chitsitsimutso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class SongList extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    static String receivedSongLanguage; //variable to hold data received from Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        //Getting the data that was send by the Intent that opened this activity
        Bundle intentExtras = getIntent().getExtras();
        receivedSongLanguage = intentExtras.getString("songLanguage");

        //Setting up the actionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Action bar Back button
        getSupportActionBar().setTitle(receivedSongLanguage+ " songs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialising the object for accessing the listView which will contain the songs
        listView = (ListView)findViewById(R.id.MylistView);

        //Accessing the Database through DatabaseAccess class
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> songs = databaseAccess.getSongTitles(receivedSongLanguage);
        databaseAccess.close();

        //Adapter will handle the list that will be obtained from the database
        adapter = new ArrayAdapter<>(this, R.layout.listview_custom, songs);
        this.listView.setAdapter(adapter);

        //When a song is clicked, its title is sent to a next class where it is viewed
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView itemSelected = (TextView) view;

                CharSequence selectedTitle = itemSelected.getText(); //Contains the text that has been selected by tapping

                Intent titleIntent = new Intent(SongList.this, ViewSong.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("songTitle", selectedTitle);
                bundle.putString("songLanguage", receivedSongLanguage);
                titleIntent.putExtras(bundle);
                startActivity(titleIntent);
            }
        });
    }

    //Overridden methods to handling actionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_songlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(SongList.this, Settings.class));
            return true;
        }

        if (id == R.id.action_about) {
            // launch settings activity
            startActivity(new Intent(SongList.this, About.class));
            return true;
        }

        if(id == android.R.id.home){
            finish();
            toEndActivity();
                return true;
        }

        if(id == R.id.action_search){

            Intent searchsongIntent = new Intent(SongList.this, SearchSong.class);

            Bundle bundle = new Bundle();
            bundle.putString("songLanguage", receivedSongLanguage);
            searchsongIntent.putExtras(bundle);
            startActivity(searchsongIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    //Method ends here

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toEndActivity();
    }

    public void toEndActivity(){
        startActivity(new Intent(SongList.this, MainActivity.class));
    }

}
