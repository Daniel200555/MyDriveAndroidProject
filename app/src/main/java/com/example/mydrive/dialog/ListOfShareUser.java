package com.example.mydrive.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.mydrive.ListAdapter.FileItems;
import com.example.mydrive.ListAdapter.UserShareItems;
import com.example.mydrive.ListOfFiles;
import com.example.mydrive.R;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.dto.UserListDTO;
import com.example.mydrive.service.FileGet;
import com.example.mydrive.service.RegisterAndLogin;
import com.example.mydrive.util.UsersCallback;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ListOfShareUser {

    private Dialog dialog;
    private Context context;
    private AppCompatButton addUserShareButton;
    private ListView listView;

    public ListOfShareUser(Context context, FileDTO fileDTO) {
        this.context = context;
        this.dialog = new Dialog(context);
        this.dialog.setContentView(R.layout.list_of_shared_users_dialog);
        this.dialog.setTitle("Share Users");
        this.dialog.setCancelable(true);
        this.listView = (ListView) this.dialog.findViewById(R.id.listOfSharedUsers);
        new FileGet().getAllUsersShared(new RegisterAndLogin().getEmail(), fileDTO.getDir(), new UsersCallback() {
            @Override
            public void onUsersReceived(List<UserListDTO> users) {
                List<UserListDTO> u = new ArrayList<>();
                for (UserListDTO user: users) {
                    if (!user.getEmail().equals("empty")) {
                        u.add(user);
                    }
                }
                UserShareItems items = new UserShareItems(context, u);
                listView.setAdapter(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.addUserShareButton = this.dialog.findViewById(R.id.buttonAddShareUser);
        this.addUserShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog(context, fileDTO);
            }
        });
        this.dialog.show();
    }

}
