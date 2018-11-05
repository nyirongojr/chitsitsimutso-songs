package com.jnrnyirongo.chitsitsimutso;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //The cardviews variables that will be used as button clicks from the start page
    CardView chichewa, chitumbuka, favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the actionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chichewa = (CardView) findViewById(R.id.chichewaSongsTab);
        chitumbuka = (CardView) findViewById(R.id.chitumbukaSongsTab);
        favourite = (CardView) findViewById(R.id.favouritesTab);

        chichewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chichewaListIntent = new Intent(MainActivity.this, SongList.class);
                Bundle bundle = new Bundle();
                bundle.putString("songLanguage", "chichewa");
                chichewaListIntent.putExtras(bundle);
                startActivity(chichewaListIntent);
            }
        });

        chitumbuka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chitumbukaListIntent = new Intent(MainActivity.this, SongList.class);
                Bundle bundle = new Bundle();
                bundle.putString("songLanguage", "chitumbuka");
                chitumbukaListIntent.putExtras(bundle);
                startActivity(chitumbukaListIntent);
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FavouriteSongs.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, Settings.class));
            return true;
        }

        if (id == R.id.action_about) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        //We will prompt use to confirm before exiting
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}