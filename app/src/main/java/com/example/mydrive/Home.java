package com.example.mydrive;

import static com.example.mydrive.R.*;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mydrive.dialog.Add;
import com.example.mydrive.dialog.SaveFile;
import com.example.mydrive.service.ChangeActivity;
import com.example.mydrive.service.FileManager;
import com.example.mydrive.service.RegisterAndLogin;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Home extends AppCompatActivity implements View.OnClickListener {

    String email;
    String option;
    private Button buttonLogout;
    private Button buttonAllFiles;
    private Button buttonSharedFiles;
    private Button buttonStarredFiles;
    private AppCompatButton buttonAddFile;
    private Add add;
    private static final int REQUEST_CODE_FILE_PICKER = 100;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        storageReference = FirebaseStorage.getInstance().getReference();
        this.buttonLogout = (Button) findViewById(R.id.buttonLogout);
        this.buttonAllFiles = (Button) findViewById(R.id.buttonAllFiles);
        this.buttonSharedFiles = (Button) findViewById(R.id.buttonSharedFiles);
        this.buttonStarredFiles = (Button) findViewById(R.id.buttonStarredFiles);
//        this.buttonAddFile = (AppCompatButton) findViewById(R.id.buttonAdd);
//        this.buttonAddFile.setOnClickListener(this);
        this.buttonLogout.setOnClickListener(this);
        this.buttonAllFiles.setOnClickListener(this);
        this.buttonSharedFiles.setOnClickListener(this);
        this.buttonStarredFiles.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            setEmail(intent.getStringExtra("email"));
            setOption(intent.getStringExtra("option"));
            System.out.println(getOption());
            if (getEmail() == null || getEmail().equals("")) {
                setEmail(new RegisterAndLogin().getEmail());
                Log.d("GET EMAIL", getEmail());
            }
            if (getOption() == null) {
                if (savedInstanceState == null) {
                    Log.d("CREATE FRAGMENT", "Creating....");
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragmentListOfFile, new FragmentListOfFiles(getEmail()))
                            .commit();
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v == this.buttonLogout) {
            new RegisterAndLogin().logout();
            new ChangeActivity(this, MainActivity.class);
        } if (v == this.buttonAllFiles) {
            Bundle args = new Bundle();
            args.putString("option", "all");
            FragmentListOfFiles fragment = new FragmentListOfFiles(getEmail());
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentListOfFile, fragment)
                    .commit();
        } if (v == this.buttonSharedFiles) {
            Bundle args = new Bundle();
            args.putString("option", "shared");
            FragmentListOfFiles fragment = new FragmentListOfFiles(getEmail());
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentListOfFile, fragment)
                    .commit();
        } if (v == this.buttonStarredFiles) {
            Bundle args = new Bundle();
            args.putString("option", "starred");
            FragmentListOfFiles fragment = new FragmentListOfFiles(getEmail());
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentListOfFile, fragment)
                    .commit();
        } if (v == buttonAddFile) {
//            this.add = new Add(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SaveFile.onActivityResult(requestCode, resultCode, data);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}