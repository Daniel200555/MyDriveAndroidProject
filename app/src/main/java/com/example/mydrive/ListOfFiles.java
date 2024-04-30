package com.example.mydrive;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mydrive.ListAdapter.FileItems;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.service.FileGet;
import com.example.mydrive.service.RegisterAndLogin;
import com.example.mydrive.util.FilesCallback;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ListOfFiles extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_files);

//        List<FileDTO> files = getFiles();
//        Log.d("FILES LIST", "Size of file list " + files.size());
        listView = (ListView) findViewById(R.id.listOfFilesOne);
        new FileGet().getFiles(new RegisterAndLogin().getEmail(), new FilesCallback() {
            @Override
            public void onFilesReceived(List<FileDTO> files) {
                List<FileDTO> f = new ArrayList<>();
                for (FileDTO file: files) {
                    if (!file.getName().equals("empty") && !file.getDir().equals("empty")) {
                        f.add(file);
                    }
                }
                FileItems items = new FileItems(ListOfFiles.this, f);
                listView.setAdapter(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public List<FileDTO> getFiles() {
        List<FileDTO> f = new ArrayList<>();

        return f;
    }
}