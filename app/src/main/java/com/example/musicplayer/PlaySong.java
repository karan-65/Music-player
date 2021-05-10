package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPLayer.stop();
        mediaPLayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play,previous,next;
    ArrayList<File> songs;
    MediaPlayer mediaPLayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
      textView.setSelected(true
       );
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPLayer = MediaPlayer.create(this, uri);
        mediaPLayer.start();
        seekBar.setMax(mediaPLayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPLayer.seekTo(seekBar.getProgress());

            }
        });
        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while(currentPosition<mediaPLayer.getDuration()){
                        currentPosition=mediaPLayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            };
        updateSeek.start();
        play.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mediaPLayer.isPlaying()) {
                                            play.setImageResource(R.drawable.play);
                                            mediaPLayer.pause();
                                        } else {
                                            play.setImageResource(R.drawable.pause);
                                            mediaPLayer.start();
                                        }
                                    }
                                });
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPLayer.stop();
                    mediaPLayer.release();
                    if(position!=0){
                        position=position-1;
                    }
                    else{
                        position=songs.size()-1;
                    }
                    Uri uri = Uri.parse(songs.get(position).toString());
                    mediaPLayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPLayer.start();
                    play.setImageResource(R.drawable.pause);
                    seekBar.setMax(mediaPLayer.getDuration());
                    textContent=songs.get(position).getName().toString();
                    textView.setText(textContent);
                }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPLayer.stop();
                mediaPLayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPLayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPLayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPLayer.getDuration());
                textContent=songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });


        }
    }