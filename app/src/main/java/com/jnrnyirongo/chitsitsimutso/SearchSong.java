package com.jnrnyirongo.chitsitsimutso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class SearchSong extends AppCompatActivity {

    private ListView listView;
    private EditText search;
    private TextView back;
    private ArrayAdapter<String> adapter;

    String receivedSongLanguage; //variable to hold data received from Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_song);

        //Getting the data that was send by the Intent that opened this activity
        Bundle intentExtras = getIntent().getExtras();
        receivedSongLanguage = intentExtras.getString("songLanguage");

        //initialising the object to access the EditText for searching the song
        search = (EditText) findViewById(R.id.songSearch);

        //Initialising the object for accessing the listView which will contain the songs
        listView = (ListView)findViewById(R.id.MylistView);

        //Initialising the object to handle the back icon
        back = (TextView) findViewById(R.id.iconBackFromSearch);

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

                Intent titleIntent = new Intent(SearchSong.this, ViewSong.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("songTitle", selectedTitle);
                bundle.putString("songLanguage", receivedSongLanguage);
                titleIntent.putExtras(bundle);
                startActivity(titleIntent);
            }
        });

        //This chunk is used to filter the ListView so that we find the song we want
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // ends filter hear

        //The following chunk handles the going Back from search Activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Intent myIntent = new Intent(SearchSong.this, SongList.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("songLanguage", receivedSongLanguage);
//                myIntent.putExtras(bundle);
//                startActivity(myIntent);
            }
        });
    }
}
