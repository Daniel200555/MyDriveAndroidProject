package com.example.mydrive;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mydrive.ListAdapter.FileItems;
import com.example.mydrive.dialog.Add;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.service.FileGet;
import com.example.mydrive.service.RegisterAndLogin;
import com.example.mydrive.util.FilesCallback;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class FragmentListOfFiles extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String value;

    private ListView listView;

    private AppCompatButton buttonAdd;
    private Add add;

    private String mParam1;
    private String mParam2;
    private String email;
    private String path;

    public FragmentListOfFiles(String email) {
        this.email = email;
        Log.d("Fraqment" , this.email);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_files, container, false);
        Bundle args = getArguments();
        if (args != null) {
            setValue(args.getString("option"));
        }
        listView = (ListView) view.findViewById(R.id.listOfFiles);
        buttonAdd = (AppCompatButton) view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        System.out.println(getValue());
        if (getValue() == null) {
            new FileGet().getFiles(getEmail(), new FilesCallback() {
                @Override
                public void onFilesReceived(List<FileDTO> files) {
                    List<FileDTO> f = new ArrayList<>();
                    f.addAll(files);
                    for (int i = 0; i < f.size(); i++) {
                        if (f.get(i).getName().equals("empty") && f.get(i).getDir().equals("empty")) {
                            f.remove(i);
                            break;
                        }
                    }
//                    for (FileDTO file : files) {
//                        System.out.println(file.getName());
////                        if (!file.getName().equals("empty") && !file.getDir().equals("empty")) {
////                            System.out.println(file.getName());
////                            f.add(file);
////                        }
//                    }

                    System.out.println(f.size());
                    FileItems items = new FileItems(getContext(), f);
                    listView.setAdapter(items);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (getValue() != null && getValue().equals("all")) {
            new FileGet().getFiles(getEmail(), new FilesCallback() {
                @Override
                public void onFilesReceived(List<FileDTO> files) {
                    List<FileDTO> f = new ArrayList<>();
                    for (FileDTO file : files) {
                        if (!file.getName().equals("empty") && !file.getDir().equals("empty")) {
                            f.add(file);
                        }
                    }
                    FileItems items = new FileItems(getContext(), f);
                    listView.setAdapter(items);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (getValue() != null && getValue().equals("shared")) {
            new FileGet().getShared(getEmail(), new FilesCallback() {
                @Override
                public void onFilesReceived(List<FileDTO> files) {
                    List<FileDTO> f = new ArrayList<>();
                    for (FileDTO file : files) {
                        if (!file.getName().equals("empty") && !file.getDir().equals("empty")) {
                            f.add(file);
                        }
                    }
                    FileItems items = new FileItems(getContext(), f);
                    listView.setAdapter(items);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (getValue() != null && getValue().equals("starred")) {
            new FileGet().getFiles(getEmail(), new FilesCallback() {
                @Override
                public void onFilesReceived(List<FileDTO> files) {
                    List<FileDTO> f = new ArrayList<>();
                    for (FileDTO file : files) {
                        if (file.isStar()) {
                            f.add(file);
                        }
                    }
                    FileItems items = new FileItems(getContext(), f);
                    listView.setAdapter(items);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAdd) {
            this.add = new Add(requireContext(), getPath());
        }
    }
}