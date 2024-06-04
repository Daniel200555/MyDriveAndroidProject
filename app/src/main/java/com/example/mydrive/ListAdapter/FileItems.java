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
import com.example.mydrive.dialog.InfoDialog;
import com.example.mydrive.dialog.ListOfShareUser;
import com.example.mydrive.dialog.Rename;
import com.example.mydrive.dialog.SaveFolder;
import com.example.mydrive.dialog.ShareDialog;
import com.example.mydrive.dto.FileDTO;
import com.example.mydrive.service.FileService;
import com.example.mydrive.service.RegisterAndLogin;

import java.util.List;

public class FileItems extends ArrayAdapter<FileDTO> {

    private TextView textViewNameOFFile;
    private AppCompatButton buttonMore;
    private Drawable yellow_drawable;
    private Context context;

    public FileItems(@NonNull Context context, List<FileDTO> items) {
        super(context, 0, items);
        this.context = context;
        yellow_drawable = context.getResources().getDrawable(R.drawable.yellow_color);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.file_item_for_list, parent, false);
        FileDTO file = getItem(position);
        if (file.isStar())
            convertView.setBackground(yellow_drawable);
        Log.d("FILE SHOW ", file.getType());
        textViewNameOFFile = (TextView) convertView.findViewById(R.id.textViewNameOfFile);
        textViewNameOFFile.setText(file.getName());
        buttonMore = (AppCompatButton) convertView.findViewById(R.id.buttonMore);
        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, file);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (file.getType()) {
                    case "PICTURE":
                        FileService.showImage(getContext(), file.getDir(), file.getFormat());
                        Log.d("FILE SHOW ", "show file");
                        break;
                    case "VIDEO":
                        FileService.showVideo(getContext(), file.getDir(), file.getFormat());
                        Log.d("FILE SHOW ", "show file");
                        break;
                    case "AUDIO":
                        FileService.showAudio(context, file.getDir());
                        break;
                    case "DOCUMENT":
                        FileService.showDocument(context, file.getDir());
                        break;
                    case "NULL":
                        FileService.showDownloadInfo(context, "Please Download file to watch it", file.getDir(), file.getName());
                        break;
                }
            }
        });

        return convertView;
    }

    private void showPopupMenu(View view, FileDTO fileDTO) {
        String owner = fileDTO.getOwner();
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        MenuItem delete = popupMenu.getMenu().findItem(R.id.action_delete);
        MenuItem star = popupMenu.getMenu().findItem(R.id.action_star);
        MenuItem download = popupMenu.getMenu().findItem(R.id.action_download);
        MenuItem share = popupMenu.getMenu().findItem(R.id.action_share);
        MenuItem rename = popupMenu.getMenu().findItem(R.id.action_rename);
        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Log.d("Share Size", "Size: " + fileDTO.getSharedToUsers().size());
                if (owner.equals(new RegisterAndLogin().getEmail())) {
                    if (fileDTO.getSharedToUsers().size() > 1)
                        new ListOfShareUser(context, fileDTO);
                    else
//                    new SaveFolder(context, "bla");
                        new ShareDialog(context, fileDTO);
                    Log.d("SHARE FILE", fileDTO.getDir());
                    Toast.makeText(getContext(), "Share clicked. Dir " + fileDTO.getDir(), Toast.LENGTH_SHORT).show();
                } else {
                    new InfoDialog(context, "You could not to share this file because you are not owner of this file");
                }
                return true;
            }
        });
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (owner.equals(new RegisterAndLogin().getEmail())) {
                    new FileService().deleteFile(context, new RegisterAndLogin().getEmail(), fileDTO);
                } else {
                    new InfoDialog(context, "You could not to delete this file because you are not owner of this file");
                }
                Toast.makeText(getContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        star.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (owner.equals(new RegisterAndLogin().getEmail())) {
                    if (!fileDTO.isStar())
                        new FileService().starFile(context, new RegisterAndLogin().getEmail(), fileDTO.getDir());
                    else
                        new FileService().unStarFile(context, new RegisterAndLogin().getEmail(), fileDTO.getDir());
                } else {
                    new InfoDialog(context, "You could not to delete this file because you are not owner of this file");
                }
                Toast.makeText(getContext(), "Star clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        download.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                new FileService().downloadFile(fileDTO.getDir(), fileDTO.getName());
                Toast.makeText(getContext(), "Download clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
        rename.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                if (owner.equals(new RegisterAndLogin().getEmail())) {
                    if (fileDTO.getSharedToUsers().size() == 1)
                        new Rename(context, fileDTO.getDir());
                    else
                        new InfoDialog(context, "You could not to rename this file because this file shared to other users");
                } else
                    new InfoDialog(context, "You could not to rename this file because you are not owner of this file");
                Toast.makeText(getContext(), "Rename clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
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
