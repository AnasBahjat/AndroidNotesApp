package com.example.androidlabproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Note> myNotes;
    private Context context;
    private DataBaseHelper dataBaseHelper;
    static int [] flag;
    PopupMenu popupMenu;
    AlertDialog.Builder builder1;
    private AnimationListener animationListener;

    public MyAdapter(List<Note> myNotes,Context context,AnimationListener animationListener){
        this.myNotes=myNotes;
        this.context=context;
        flag=new int[myNotes.size()];
        this.animationListener=animationListener;
    }

    public MyAdapter(List<Note> myNotes,Context context){
        this.myNotes=myNotes;
        this.context=context;
        flag=new int[myNotes.size()];
    }

    public interface AnimationListener{
        void animationMethod();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note=myNotes.get(position);
        dataBaseHelper=new DataBaseHelper(context,"androidLabDB",null,1);

        if(note.getIsFavorite().equals("0")){
            holder.addNoteToFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
            flag[position]=0;
        }
        else {
            holder.addNoteToFavorite.setImageResource(R.drawable.baseline_favorite_24);
            flag[position]=1;
        }
        holder.addNoteToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag[position]==0){
                    holder.addNoteToFavorite.setImageResource(R.drawable.baseline_favorite_24);
                    flag[position]=1;
                    dataBaseHelper.setNoteFavorite(note,"1");
                }

                else {
                    holder.addNoteToFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
                    flag[position]=0;
                    //dataBaseHelper=new DataBaseHelper(context,"androidLabDB",null,1);
                    dataBaseHelper.setNoteFavorite(note,"0");
                    //notifyItemRemoved(position);

                }
                if(animationListener != null && flag[position] == 1){
                    animationListener.animationMethod();
                }
            }
        });

        if(myNotes==null){
            holder.titleTextView.setText("No favorite notes "+note.getTitle());
        }
        else {
            holder.titleTextView.setText("Title : "+note.getTitle());
            holder.dateTextView.setText("Creation Date : "+note.getDate());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,NoteView.class);
                intent.putExtra("noteID",myNotes.get(position).getNoteID()+"");
                intent.putExtra("title",myNotes.get(position).getTitle());
                intent.putExtra("content",myNotes.get(position).getContent());
                intent.putExtra("email",myNotes.get(position).getEmail());
                context.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.cardView);
                popupMenu.getMenuInflater().inflate(R.menu.note_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("IntentReset")
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.modifyNote){
                            Intent intent=new Intent(context,ModifyNoteActivity.class);
                            intent.putExtra("noteID",myNotes.get(position).getNoteID());
                            Log.d("note id : "+myNotes.get(position).getNoteID(),"note id : "+myNotes.get(position).getNoteID());
                            intent.putExtra("email",myNotes.get(position).getEmail());
                            context.startActivity(intent);
                        }
                        else if (menuItem.getItemId()==R.id.deleteNote) {
                            Log.d(myNotes.get(position).getNoteID()+"   ID ",myNotes.get(position).getNoteID()+"");
                            dataBaseHelper.deleteNote(myNotes.get(position).getNoteID());
                            dataBaseHelper.deleteCatNote(myNotes.get(position).getNoteID());
                            myNotes.remove(position);
                            notifyItemRemoved(position);
                        }
                        else if(menuItem.getItemId()==R.id.category) {
                            builder1=new AlertDialog.Builder(context,R.style.CustomAlertDialogTheme);
                            builder1.setTitle("Choose Category ");
                            LayoutInflater layoutInflater=LayoutInflater.from(context);
                            View myView=layoutInflater.inflate(R.layout.categorization,null);
                            Button sport=myView.findViewById(R.id.sport);
                            Button money=myView.findViewById(R.id.money);
                            Button food=myView.findViewById(R.id.food);
                            Button programming=myView.findViewById(R.id.programming);
                            Button war=myView.findViewById(R.id.war);
                            Button education=myView.findViewById(R.id.education);

                            builder1.setView(myView);
                            AlertDialog alertDialog = builder1.create();
                            alertDialog.show();


                            sport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Sport",myNotes.get(position).getNoteID())){
                                        Toast.makeText(context,"Note Deleted from Sport category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(myNotes.get(position).getNoteID(),"Sport");
                                    }
                                    else {
                                        insertToCategory(myNotes.get(position).getNoteID(),"Sport");
                                        Toast.makeText(context,"Note added to Sport category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();

                                }
                            });

                            money.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Money",myNotes.get(position).getNoteID())){
                                        Toast.makeText(context,"Note Deleted from Money category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(myNotes.get(position).getNoteID(),"Money");
                                    }
                                    else {
                                        insertToCategory(myNotes.get(position).getNoteID(),"Money");
                                        Toast.makeText(context,"Note added to Money category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            food.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(checkIfExist("Food",myNotes.get(position).getNoteID())){
                                        Toast.makeText(context,"Note Deleted from Food category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(myNotes.get(position).getNoteID(),"Food");
                                    }
                                    else {
                                        insertToCategory(myNotes.get(position).getNoteID(),"Food");
                                        Toast.makeText(context,"Note added to Food category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            programming.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("Programming",myNotes.get(position).getNoteID())){
                                        Toast.makeText(context,"Note Deleted from Programming category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(myNotes.get(position).getNoteID(),"Programming");
                                    }
                                    else {
                                        insertToCategory(myNotes.get(position).getNoteID(),"Programming");
                                        Toast.makeText(context,"Note added to Programming category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();

                                }
                            });

                            war.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(checkIfExist("War",myNotes.get(position).getNoteID())){
                                        Toast.makeText(context,"Note Deleted from War category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(myNotes.get(position).getNoteID(),"War");
                                    }
                                    else {
                                        insertToCategory(myNotes.get(position).getNoteID(),"War");
                                        Toast.makeText(context,"Note added to War category ..",Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            education.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(checkIfExist("Education",myNotes.get(position).getNoteID())){
                                        Toast.makeText(context,"Note Deleted from Education category ..",Toast.LENGTH_SHORT).show();
                                        dataBaseHelper.deleteCatNoteWithSpecificCategory(myNotes.get(position).getNoteID(),"Education");
                                    }
                                    else {
                                        insertToCategory(myNotes.get(position).getNoteID(),"Education");
                                        Toast.makeText(context,"Note added to Education category ..",Toast.LENGTH_SHORT).show();
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
                            gmailIntent.putExtra(Intent.EXTRA_SUBJECT,myNotes.get(position).getTitle());
                            gmailIntent.putExtra(Intent.EXTRA_TEXT,myNotes.get(position).getContent());
                            context.startActivity(gmailIntent);
                        }
                            return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    public boolean checkIfExist(String category,int noteID){
        Cursor cursor=dataBaseHelper.checkIfCategorized(category,noteID);
        if(cursor.moveToFirst())
            return true;
        return false;
    }


    public void insertToCategory(int noteID,String category){
        dataBaseHelper.insertToCat(noteID,category);
    }


    @Override
    public int getItemCount() {
        return myNotes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,dateTextView;
        ImageView addNoteToFavorite;
        CardView cardView ;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            addNoteToFavorite=itemView.findViewById(R.id.addNoteToFavorite);
            titleTextView=itemView.findViewById(R.id.titlePreView);
            dateTextView=itemView.findViewById(R.id.noteDate);
            cardView=itemView.findViewById(R.id.card);
        }
    }
}
