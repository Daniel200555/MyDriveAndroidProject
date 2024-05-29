package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

import com.example.mydrive.R;

import java.io.IOException;

public class AudioDialog implements View.OnClickListener {


    private Context context;
    private Dialog dialog;
    private Button playAudio;
    private Button stopAudio;
    private MediaPlayer mediaPlayer;
    private String uri;

    public AudioDialog(Context context, String url) {
        this.context = context;
        setUri(url);
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.audio_dialog);
        this.dialog.setTitle("Audio");
        this.dialog.setCancelable(true);
        this.mediaPlayer = new MediaPlayer();
        this.playAudio = (Button) this.dialog.findViewById(R.id.buttonPlayAudio);
        this.playAudio.setOnClickListener(this);
        this.stopAudio = (Button) this.dialog.findViewById(R.id.buttonStopAudio);
        this.stopAudio.setOnClickListener(this);
        this.dialog.setOnDismissListener(dialog1 -> {
            if (this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.stop();
            }
            this.mediaPlayer.release();
        });
        this.dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == this.playAudio) {
            try {
                this.mediaPlayer.setDataSource(getUri());
                this.mediaPlayer.prepare();
                this.mediaPlayer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } if (v == this.stopAudio) {
            if (this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.stop();
                this.mediaPlayer.reset();
            }
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
