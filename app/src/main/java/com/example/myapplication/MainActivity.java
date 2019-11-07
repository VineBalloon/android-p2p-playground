package com.example.myapplication;

import android.os.Bundle;

import com.example.myapplication.model.Node;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView peersTextView;
    private TextView framesTextView;

    Node node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "Hello!";
                node.broadcastFrame(str.getBytes());

                Snackbar.make(view, "You just broadcasted: \"" + str + "\"!",
                        Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        node = new Node(this);

        peersTextView = findViewById(R.id.peersView);
        framesTextView = findViewById(R.id.framesView);

        refreshPeers();
        refreshFrames();
    }

    @Override
    public void onStart() {
        super.onStart();

        node.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (node != null) {
            node.stop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    public void sendFrames(View view) {
        // broadcast data (i.e. 'frame') to
        node.broadcastFrame(new byte[1]);

        for(int i = 0; i < 2000; ++i)
        {
            byte[] frameData = new byte[1024];
            new Random().nextBytes(frameData);

            node.broadcastFrame(frameData);
        }
    }
     */

    public void refreshPeers() {
        //peersTextView.setText(node.getLinks().size() + " connected");
        peersTextView.setText(getString(R.string.peers, node.getLinks().size()));
    }

    public void refreshFrames() {
        //framesTextView.setText(node.getFramesCount() + " frames");
        framesTextView.setText(getString(R.string.frames, node.getFramesCount()));
    }

    public void recv() {

    }
}
