package com.example.androidlabproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FirstActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MyAdapter.AnimationListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPrefManager sharedPrefManager;
    private Toolbar toolbar;
    private CardView newNoteButton;
    private TextView textView;
    private RecyclerView recyclerView;
    private DataBaseHelper dataBaseHelper;
    private EditText searchText ;
    private List<Note> myNotes;
    private List<Note> myFavNotes;
    private int favFlag=0;
    private MyAdapter myAdapter;
    private NewAdapter newAdapter;
    private CardView sortByCreationDate,sortAlphabetically;
    private AlertDialog.Builder builder,builder1;
    private AlertDialog alertDialog,alertDialog1;
    private ImageView animationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity2);
        setUpViews();
        toolbar.setTitle(" My Notes");
        setSupportActionBar(toolbar);
        textView=new TextView(this);
       // textView.setText("Hello there");
       // textView.setTextSize(24);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        newNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FirstActivity.this,NewNoteActivity.class);
                intent.putExtra("email",getIntent().getStringExtra("email"));
                intent.putExtra("firstName",getFirstName());
                intent.putExtra("lastName",getLastName());
                intent.putExtra("password",getPassword());
                startActivity(intent);
            }
        });
        toolbar.addView(textView);


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(searchText.getText().toString().trim().isEmpty() && favFlag==0){
                    myNotes.clear();
                    myNotes=readAllNotes();
                    Collections.reverse(myNotes);
                    myAdapter=new MyAdapter(myNotes,FirstActivity.this);
                    newAdapter=new NewAdapter("No Favorite Notes Added");
                    if(myNotes.isEmpty()){
                        recyclerView.setAdapter(newAdapter);
                    }
                    else {
                        recyclerView.setAdapter(myAdapter);
                    }
                }
                else if(searchText.getText().toString().trim().isEmpty() && favFlag==1){
                    Log.d("2","2");
                    myFavNotes.clear();
                    myFavNotes=searchForFavoriteNotesByTitle();
                    myAdapter=new MyAdapter(myFavNotes,FirstActivity.this);
                    newAdapter=new NewAdapter("No Favorite Notes Added ");
                    if(myNotes.isEmpty()){
                        recyclerView.setAdapter(newAdapter);
                    }
                    else {
                        recyclerView.setAdapter(myAdapter);
                    }
                }
                else if(!searchText.getText().toString().trim().isEmpty() && favFlag==1){
                    Log.d("3","3");
                    myFavNotes.clear();
                    Log.d("Its not empty with flag = "+favFlag,"Its not empty with flag = "+favFlag);
                    myFavNotes=searchForFavoriteNotesByTitle();
                    myAdapter=new MyAdapter(myFavNotes,FirstActivity.this);
                    newAdapter=new NewAdapter("No Favorite Notes Added ");
                    if(myNotes.isEmpty()){
                        recyclerView.setAdapter(newAdapter);
                    }
                    else {
                        recyclerView.setAdapter(myAdapter);
                    }
                }
                else {
                    Log.d("4","4");
                    myNotes.clear();
                    myNotes=searchByTitle();
                    Collections.reverse(myNotes);
                    myAdapter=new MyAdapter(myNotes,FirstActivity.this);
                    newAdapter=new NewAdapter("No Favorite Notes Added ");
                    if(myNotes.isEmpty()){
                        recyclerView.setAdapter(newAdapter);
                    }
                    else {
                        recyclerView.setAdapter(myAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
            Intent intent=new Intent(FirstActivity.this,Login.class);
            intent.putExtra("LogoutType",1);
            startActivity(intent);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.nav_all){
            favFlag=0;
            myNotes.clear();
            myNotes=readAllNotes();
            Collections.reverse(myNotes);
            myAdapter=new MyAdapter(myNotes,FirstActivity.this,this);
            newAdapter=new NewAdapter("No Notes To View ");
            if(myNotes.isEmpty()){
                recyclerView.setAdapter(newAdapter);
            }
            else {
                recyclerView.setAdapter(myAdapter);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else if(item.getItemId()==R.id.nav_fav){
            favFlag=1;
            myFavNotes.clear();
            myFavNotes=readFavoriteNotes();
            myAdapter=new MyAdapter(myFavNotes,FirstActivity.this,this);
            newAdapter=new NewAdapter("No Favorite Notes Added ");
            if(myFavNotes.isEmpty()){
                recyclerView.setAdapter(newAdapter);
            }
            else {
                recyclerView.setAdapter(myAdapter);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else if(item.getItemId()==R.id.nav_sorted){
            showOptionDialog();
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else if(item.getItemId()==R.id.nav_settings){
            Intent intent=new Intent(FirstActivity.this,ProfileActivity.class);
            intent.putExtra("email",getIntent().getStringExtra("email"));
            intent.putExtra("password",getPassword());
            startActivity(intent);
        }

        else if(item.getItemId()==R.id.nav_cat){
            View view = findViewById(R.id.nav_cat);
            PopupMenu popupMenu = new PopupMenu(this,view);
            popupMenu.getMenuInflater().inflate(R.menu.cat_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId()==R.id.sportID){
                        myNotes.clear();
                        myNotes=getCatNotes("Sport");
                        Collections.reverse(myNotes);
                        myAdapter=new MyAdapter(myNotes,FirstActivity.this,FirstActivity.this);
                        newAdapter=new NewAdapter("No Sport Categorized Notes added ");
                        if(myNotes.isEmpty()){
                            recyclerView.setAdapter(newAdapter);
                        }
                        else {
                            recyclerView.setAdapter(myAdapter);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    else if(menuItem.getItemId()==R.id.moneyID){
                        myNotes.clear();
                        myNotes=getCatNotes("Money");
                        Collections.reverse(myNotes);
                        myAdapter=new MyAdapter(myNotes,FirstActivity.this,FirstActivity.this);
                        newAdapter=new NewAdapter("No Money Categorized Notes added ");
                        if(myNotes.isEmpty()){
                            recyclerView.setAdapter(newAdapter);
                        }
                        else {
                            recyclerView.setAdapter(myAdapter);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    else if(menuItem.getItemId()==R.id.foodID){
                        myNotes.clear();
                        myNotes=getCatNotes("Food");
                        Collections.reverse(myNotes);
                        myAdapter=new MyAdapter(myNotes,FirstActivity.this,FirstActivity.this);
                        newAdapter=new NewAdapter("No Food Categorized Notes added ");
                        if(myNotes.isEmpty()){
                            recyclerView.setAdapter(newAdapter);
                        }
                        else {
                            recyclerView.setAdapter(myAdapter);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    else if(menuItem.getItemId()==R.id.programmingID){
                        myNotes.clear();
                        myNotes=getCatNotes("Programming");
                        Collections.reverse(myNotes);
                        myAdapter=new MyAdapter(myNotes,FirstActivity.this,FirstActivity.this);
                        newAdapter=new NewAdapter("No Programming Categorized Notes added ");
                        if(myNotes.isEmpty()){
                            recyclerView.setAdapter(newAdapter);
                        }
                        else {
                            recyclerView.setAdapter(myAdapter);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    else if(menuItem.getItemId()==R.id.educationID){
                        myNotes.clear();
                        myNotes=getCatNotes("Education");
                        Collections.reverse(myNotes);
                        myAdapter=new MyAdapter(myNotes,FirstActivity.this,FirstActivity.this);
                        newAdapter=new NewAdapter("No Education Categorized Notes added ");
                        if(myNotes.isEmpty()){
                            recyclerView.setAdapter(newAdapter);
                        }
                        else {
                            recyclerView.setAdapter(myAdapter);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }

                    else {  // war
                        myNotes.clear();
                        myNotes=getCatNotes("War");
                        Collections.reverse(myNotes);
                        myAdapter=new MyAdapter(myNotes,FirstActivity.this,FirstActivity.this);
                        newAdapter=new NewAdapter("No War Categorized Notes added ");
                        if(myNotes.isEmpty()){
                            recyclerView.setAdapter(newAdapter);
                        }
                        else {
                            recyclerView.setAdapter(myAdapter);
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

        else {
            Intent intent=new Intent(FirstActivity.this,Login.class);
            intent.putExtra("LogoutType",1);
            startActivity(intent);
        }

        return true;
    }

    private void showOptionDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.sort_based_on);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alertDialog.dismiss();
            }
        });

        sortByCreationDate = alertDialog.findViewById(R.id.sortByCreationDate);
        sortAlphabetically = alertDialog.findViewById(R.id.sortAlphabetically);

        assert sortAlphabetically != null;
        sortAlphabetically.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortNotesAlphabetically();
            }
        });

        sortByCreationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortNotesByCreationDate();
            }
        });

    }

    public void sortNotesAlphabetically(){
        myNotes.clear();
        myNotes=readAllNotes();
        myNotes=sortNotesAlphabetically(myNotes);
        myAdapter=new MyAdapter(myNotes,this,this);
        newAdapter=new NewAdapter("No Notes To View ");
        if(myNotes.isEmpty()){
            recyclerView.setAdapter(newAdapter);
        }
        else {
            recyclerView.setAdapter(myAdapter);
        }
        alertDialog.dismiss();
    }

    public void sortNotesByCreationDate() {
        myNotes.clear();
        myNotes=readAllNotes();
        Collections.reverse(myNotes);
        myAdapter=new MyAdapter(myNotes,this,this);
        newAdapter=new NewAdapter("No Notes To View ");
        if(myNotes.isEmpty()){
            recyclerView.setAdapter(newAdapter);
        }
        else {
            recyclerView.setAdapter(myAdapter);
        }
        alertDialog.dismiss();
    }

    public void setUpViews(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        newNoteButton=findViewById(R.id.newNoteButton);
        searchText=findViewById(R.id.searchText);
        sharedPrefManager=SharedPrefManager.getInstance(this);
        myFavNotes=new ArrayList<>();
        dataBaseHelper=new DataBaseHelper(this,"androidLabDB",null,1);
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myNotes=new ArrayList<>();
        myNotes=readAllNotes();
        Log.d(myNotes.size()+" the size",myNotes.size()+"");
        Collections.reverse(myNotes);
        newAdapter=new NewAdapter("No Notes To View");
        myAdapter=new MyAdapter(myNotes,FirstActivity.this,this);
        if(myNotes.isEmpty()){
            recyclerView.setAdapter(newAdapter);
        }
        else {
            recyclerView.setAdapter(myAdapter);
        }
    }

    private List<Note> readAllNotes() {
        List<Note> myNotes=new ArrayList<>();
        Cursor cursor=dataBaseHelper.getAllNotes(getIntent().getStringExtra("email").toLowerCase());
        while(cursor.moveToNext()){
            myNotes.add(new Note(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),getIntent().getStringExtra("email"),cursor.getString(4)));
        }
        return myNotes;
    }

    public List<Note> searchByTitle(){
        List<Note> myNotes=new ArrayList<>();
        Cursor cursor=dataBaseHelper.getAllNotes(getIntent().getStringExtra("email").toLowerCase());
        while(cursor.moveToNext()){
            int index=cursor.getString(1).toLowerCase().indexOf(searchText.getText().toString().trim().toLowerCase());
            int index2=cursor.getString(2).toLowerCase().indexOf(searchText.getText().toString().trim().toLowerCase());
            if(index != -1 || index2 != -1){
                myNotes.add(new Note(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),getIntent().getStringExtra("email"),cursor.getString(4)));
            }
        }
        return myNotes;
    }
    private List<Note> readFavoriteNotes() {
        List<Note> myNotes=new ArrayList<>();
        Cursor cursor=dataBaseHelper.getAllNotes(getIntent().getStringExtra("email").toLowerCase());
        while (cursor.moveToNext()){
            if(cursor.getString(4).equals("1")){
                myNotes.add(new Note(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),getIntent().getStringExtra("email"),cursor.getString(4)));
            }
        }
        return myNotes;
    }

    public List<Note> searchForFavoriteNotesByTitle(){
        List<Note> myFavNotes=new ArrayList<>();
        Cursor cursor=dataBaseHelper.getAllNotes(getIntent().getStringExtra("email").toLowerCase());
        while(cursor.moveToNext()){
            int index=cursor.getString(1).toLowerCase().indexOf(searchText.getText().toString().trim().toLowerCase());
            if(index != -1 && cursor.getString(4).equals("1")){
                myFavNotes.add(new Note(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),getIntent().getStringExtra("email"),cursor.getString(4)));
            }
        }
        return myFavNotes;
    }


    public List<Note> sortNotesAlphabetically(List<Note> myNotes){
        myNotes.sort(new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        return myNotes;
    }
    public String getPassword(){
        Cursor cursor=dataBaseHelper.getPassword(getIntent().getStringExtra("email").toLowerCase());
        String password="";
        if(cursor.moveToNext()){
            password=cursor.getString(0);
        }
        return password;
    }

    public String getFirstName() {
        Cursor cursor=dataBaseHelper.getFirstName(getIntent().getStringExtra("email").toLowerCase());
        String firstName="";
        if(cursor.moveToNext()){
            firstName=cursor.getString(0);
        }
        return firstName;
    }

    public String getLastName() {
        Cursor cursor=dataBaseHelper.getLastName(getIntent().getStringExtra("email").toLowerCase());
        String lastName="";
        if(cursor.moveToNext()){
            lastName=cursor.getString(0);
        }
        return lastName;
    }

    @Override
    public void animationMethod() {
        animationView=findViewById(R.id.animationView);
        Animation animationUtils= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.favorite_note);
        animationUtils.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationView.startAnimation(animationUtils);
    }

    public List<Note> getCatNotes(String category){
        Cursor cursor=dataBaseHelper.getCatNotes(category);
        List<Note> catNotes=new ArrayList<>();
        while(cursor.moveToNext()){
            Cursor cursor1=dataBaseHelper.getAllNotes(getIntent().getStringExtra("email").toLowerCase());
            while(cursor1.moveToNext()){
                if(cursor.getInt(0)==cursor1.getInt(0)){
                    catNotes.add(new Note(cursor1.getInt(0),cursor1.getString(1),cursor1.getString(2),cursor1.getString(3),getIntent().getStringExtra("email"),cursor1.getString(4)));
                }
            }
        }
        return catNotes;
    }
}