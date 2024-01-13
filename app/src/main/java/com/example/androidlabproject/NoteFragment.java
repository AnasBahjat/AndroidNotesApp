package com.example.androidlabproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,email;
    private int noteID;
    private DataBaseHelper dataBaseHelper;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    AlertDialog.Builder builder1;

    public NoteFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString("titleData", param1);
        args.putString("bodyData", param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        TextView titleFragment = view.findViewById(R.id.fragmentTitle);
        TextView bodyFragment = view.findViewById(R.id.fragmentBody);
        dataBaseHelper=new DataBaseHelper(getActivity(),"androidLabDB",null,1);

        noteID=-1;
        Bundle args=getArguments();
        if(args != null){
            noteID=Integer.parseInt(args.getString("noteID"));
            email=args.getString("email");
        }


        String noteTitle=getNoteTitle();
        String noteContent=getNoteContent();
        titleFragment.setText(noteTitle);
        bodyFragment.setText(noteContent);


        ImageView moreSettings=view.findViewById(R.id.moreSettings);
        moreSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),moreSettings);
                popupMenu.getMenuInflater().inflate(R.menu.note_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("IntentReset")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.modifyNote){
                            Intent intent=new Intent(getActivity(),ModifyNoteActivity.class);
                            intent.putExtra("noteID",noteID);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        }

                        else if (menuItem.getItemId()==R.id.deleteNote){
                            dataBaseHelper.deleteNote(noteID);
                            dataBaseHelper.deleteCatNote(noteID);

                            Intent intent=new Intent(getActivity(),FirstActivity.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                        }

                        else if(menuItem.getItemId()==R.id.category) {
                            builder1=new AlertDialog.Builder(getActivity(),R.style.CustomAlertDialogTheme);
                            builder1.setTitle("Choose Category ");
                            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
                            View myView=layoutInflater.inflate(R.layout.categorization,null);
                            Button sport=myView.findViewById(R.id.sport);
                            Button money=myView.findViewById(R.id.money);
                            Button food=myView.findViewById(R.id.food);
                            Button programming=myView.findViewById(R.id.programming);
                            Button War=myView.findViewById(R.id.war);
                            Button education=myView.findViewById(R.id.education);

                            builder1.setView(myView);
                            AlertDialog alertDialog = builder1.create();
                            alertDialog.show();

                            sport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Sport",noteID)){
                                        Toast.makeText(getActivity(),"Note Deleted from Sport category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(noteID,"Sport");
                                    }
                                    else {
                                        insertToCategory(noteID,"Sport");
                                        Toast.makeText(getActivity(),"Note added to Sport category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            money.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Money",noteID)){
                                        Toast.makeText(getActivity(),"Note Deleted from Money category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(noteID,"Money");
                                    }
                                    else {
                                        insertToCategory(noteID,"Money");
                                        Toast.makeText(getActivity(),"Note added to Money category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();                                }
                            });

                            food.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Food",noteID)){
                                        Toast.makeText(getActivity(),"Note Deleted from Food category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(noteID,"Food");
                                    }
                                    else {
                                        insertToCategory(noteID,"Food");
                                        Toast.makeText(getActivity(),"Note added to Food category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            programming.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Programming",noteID)){
                                        Toast.makeText(getActivity(),"Note Deleted from Programming category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(noteID,"Programming");
                                    }
                                    else {
                                        insertToCategory(noteID,"Programming");
                                        Toast.makeText(getActivity(),"Note added to Programming category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            War.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("War",noteID)){
                                        Toast.makeText(getActivity(),"Note Deleted from War category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(noteID,"War");
                                    }
                                    else {
                                        insertToCategory(noteID,"War");
                                        Toast.makeText(getActivity(),"Note added to War category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            education.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Education",noteID)){
                                        Toast.makeText(getActivity(),"Note Deleted from Education category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(noteID,"Education");
                                    }
                                    else {
                                        insertToCategory(noteID,"Education");
                                        Toast.makeText(getActivity(),"Note added to Education category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);//
                        }

                        else {
                            Intent gmailIntent=new Intent();
                            gmailIntent.setAction(Intent.ACTION_SENDTO);
                            gmailIntent.setType("message/rfc822");
                            gmailIntent.setData(Uri.parse("mailto:"));
                            gmailIntent.putExtra(Intent.EXTRA_SUBJECT,getNoteTitle());
                            gmailIntent.putExtra(Intent.EXTRA_TEXT,getNoteContent());
                            startActivity(gmailIntent);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }



    public boolean checkIfExist(String category,int noteID){
        Cursor cursor=dataBaseHelper.checkIfCategorized(category,noteID);
        if(cursor.moveToNext())
            return true;
        return false;
    }

    public void insertToCategory(int noteID,String category){
        Cursor cursor=dataBaseHelper.getAllCatNotes(noteID);
        while(cursor.moveToNext()){
            if(cursor.getString(0).equals(category)){
                return;
            }
        }
        dataBaseHelper.insertToCat(noteID,category);
    }

    private String getNoteContent() {
        Cursor cursor = dataBaseHelper.getNoteContent(noteID);
        String content="";
        if(cursor.moveToNext()){
            content= cursor.getString(0);
        }
        return content;
    }

    public String getNoteTitle() {
        Cursor cursor = dataBaseHelper.getNoteTitle(noteID);
        String title="";
        if(cursor.moveToNext()){
            title=cursor.getString(0);
        }
        return title;
    }


}