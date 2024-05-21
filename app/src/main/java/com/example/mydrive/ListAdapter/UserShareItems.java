package com.example.mydrive.ListAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;

import com.example.mydrive.FragmentListOfFiles;
import com.example.mydrive.Home;
import com.example.mydrive.ListOfFiles;
import com.example.mydrive.R;
import com.example.mydrive.dialog.ListOfShareUser;
import com.example.mydrive.dialog.ShareDialog;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserListDTO;
import com.example.mydrive.service.FileService;
import com.example.mydrive.service.RegisterAndLogin;

import java.util.List;

public class UserShareItems extends ArrayAdapter<UserListDTO> {

    private TextView textViewNameOFFile;
    private AppCompatButton buttonMore;
    private Context context;

    public UserShareItems(@NonNull Context context, @NonNull List<UserListDTO> items) {
        super(context, 0, items);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_share_user, parent, false);
        UserListDTO user = getItem(position);
        textViewNameOFFile = (TextView) convertView.findViewById(R.id.textViewNameOfFile);
        textViewNameOFFile.setText(user.getEmail());
        buttonMore = (AppCompatButton) convertView.findViewById(R.id.buttonDeleteUser);
        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileService.deleteFromShare(new RegisterAndLogin().getEmail(), textViewNameOFFile.getText().toString(), user.getDir());
                try {
                    Bundle args = new Bundle();
                    args.putString("option", "all");
                    Thread.sleep(1000);
                    FragmentListOfFiles fragment = new FragmentListOfFiles(new RegisterAndLogin().getEmail());
                    fragment.setArguments(args);
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentListOfFile, fragment)
                            .commit();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return convertView;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(AppCompatActivity context) {
        this.context = context;
    }
}
