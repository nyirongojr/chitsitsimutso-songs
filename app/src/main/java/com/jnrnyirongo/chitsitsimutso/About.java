package com.jnrnyirongo.chitsitsimutso;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class About extends AppCompatActivity {

    private CardView gmail, facebook, whatsapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Action bar Back button
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gmail = (CardView) findViewById(R.id.gmailCard);
        facebook = (CardView) findViewById(R.id.facebookCard);
        whatsapp = (CardView) findViewById(R.id.whatsappCard);

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent email_intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+ "nyirongofodrick@gmail.com"));
                    email_intent.putExtra(Intent.EXTRA_SUBJECT, "Chitsitsimutso App Report");
                    email_intent.putExtra(Intent.EXTRA_TEXT, " ");
                    startActivity(email_intent);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(About.this, "failed to send Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "+265884881653";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                whatsappIntent.setData(Uri.parse(url));
                startActivity(whatsappIntent);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "https://facebook.com/jnrNyirongo";
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)));
            }
        });
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
    //Method ends here

}
