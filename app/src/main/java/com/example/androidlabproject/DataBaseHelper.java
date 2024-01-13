package com.example.androidlabproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseHelper extends android.database.sqlite.SQLiteOpenHelper{
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE userData(email text  PRIMARY KEY,firstName text,lastName text,password text)");
        sqLiteDatabase.execSQL("CREATE TABLE notes(noteID INTEGER PRIMARY KEY ,title text,content text,creationDate text,email text,isFavorite text)");
        sqLiteDatabase.execSQL("CREATE TABLE category(catID INTEGER PRIMARY KEY , noteID INTEGER , catType text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertUser(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("email",user.getEmail());
        contentValues.put("firstName",user.getFirstName());
        contentValues.put("lastName",user.getLastName());
        contentValues.put("password",user.getPassword());
        sqLiteDatabase.insert("userData",null,contentValues);
        sqLiteDatabase.close();
    }

    public void insertNote(Note note){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",note.getTitle());
        contentValues.put("content",note.getContent());
        contentValues.put("creationDate",note.getDate());
        contentValues.put("email",note.getEmail().toLowerCase());
        contentValues.put("isFavorite",note.getIsFavorite());
        sqLiteDatabase.insert("notes",null,contentValues);
        sqLiteDatabase.close();
    }

    public void insertToCat(int noteID,String category){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="insert into category (noteID , catType) values(?,?)";
        String []args={String.valueOf(noteID),category};
        sqLiteDatabase.execSQL(query,args);
        sqLiteDatabase.close();
    }

    public Cursor getAllUsers(){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        return sqLiteDatabase.rawQuery("select * from userData",null);
    }


    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("userData", null, null);
        db.close();
    }

    public void updateFirstname(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("firstName",user.getFirstName());
        String whereClause = "email=?";
        String[] whereArgs = { user.getEmail()};
        sqLiteDatabase.update("userData",contentValues,whereClause,whereArgs);
        sqLiteDatabase.close();
    }

    public void updateLastName(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("lastName",user.getLastName());
        String whereClause = "email=?";
        String[] whereArgs = { user.getEmail()};
        sqLiteDatabase.update("userData",contentValues,whereClause,whereArgs);
        sqLiteDatabase.close();
    }

    public void updatePassword(User user){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("password",user.getPassword());
        String whereClause = "email=?";
        String[] whereArgs = { user.getEmail()};
        sqLiteDatabase.update("userData",contentValues,whereClause,whereArgs);
        sqLiteDatabase.close();
    }

    public Cursor getAllNotes(String email) {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        //return sqLiteDatabase.rawQuery("select * from notes where email="+email,null);
        String query="Select noteID,title,content,creationDate,isFavorite FROM notes WHERE email = ?";
        String [] selectionArgs={email};
        return  sqLiteDatabase.rawQuery(query,selectionArgs);
    }

    public Cursor searchForNotes(String title){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query="Select title,content,creationDate FROM notes WHERE title = ?";
        String [] selectionArgs={title};
        return sqLiteDatabase.rawQuery(query,selectionArgs);
    }
    public void setNoteFavorite(Note note,String fav){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="update notes set isFavorite= ? where noteID=? and title=? and content=? and creationDate=? and email=?";
        String [] args={fav,String.valueOf(note.getNoteID()),note.getTitle(),note.getContent(),note.getDate(),note.getEmail().toLowerCase()};
        sqLiteDatabase.execSQL(query,args);
        sqLiteDatabase.close();
    }

    public void deleteNote(int noteID) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="delete from notes where noteID = ?";
        Integer []args={noteID};
        sqLiteDatabase.execSQL(query,args);
        sqLiteDatabase.close();
    }

    public Cursor getPassword(String email) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="select password from userdata where email=?";
        String []args={email};
        return sqLiteDatabase.rawQuery(query,args);
    }

    public Cursor getFirstName(String email) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="select firstName from userdata where email=?";
        String []args={email};
        return sqLiteDatabase.rawQuery(query,args);
    }

    public Cursor getLastName(String email) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="select lastName from userdata where email=?";
        String []args={email};
        return sqLiteDatabase.rawQuery(query,args);
    }

    public Cursor getNoteContent(int noteID) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="select content from notes where noteID=?";
        String id=noteID+"";
        String []args={id};
        return sqLiteDatabase.rawQuery(query,args);
    }

    public Cursor getNoteTitle(int noteID) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="select title from notes where noteID=?";
        String id=noteID+"";
        String []args={id};
        return sqLiteDatabase.rawQuery(query,args);
    }

    public void updateNote(String newTitle , String newContent , int noteID) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="update notes set title=? , content=? where noteID=?";
        Log.d("new title : "+newTitle+" new content : "+newContent,"new title : "+newTitle+" new content : "+newContent);
        String []args={newTitle,newContent,String.valueOf(noteID)};
        sqLiteDatabase.execSQL(query,args);
        sqLiteDatabase.close();
    }


    public Cursor getAllCatNotes(int noteID) {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query="select catType from category where noteID = ?";
        String[] args ={String.valueOf(noteID)};
        return  sqLiteDatabase.rawQuery(query,args);
    }

    public Cursor getCatNotes(String category) {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query="select noteID from category where catType=?";
        String []args={category};
        return sqLiteDatabase.rawQuery(query,args);
    }

    public void deleteCatNote(int noteID) {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="delete from category where noteID=?";
        String []args={String.valueOf(noteID)};
        sqLiteDatabase.execSQL(query,args);
        sqLiteDatabase.close();
    }
    public Cursor checkIfCategorized(String category , int noteID){
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query="SELECT * FROM category where catType=? and noteID=?";
        String []args={category,String.valueOf(noteID)};
        return sqLiteDatabase.rawQuery(query,args);
        /*Cursor cursor=sqLiteDatabase.rawQuery(query,args);
        if(cursor.moveToFirst()){
            return cursor;
        }
        else {
            return null;
        }*/
    }

    public void deleteCatNoteWithSpecificCategory(int noteID, String category) {
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String query="DELETE FROM category where catType=? and noteID=?";
        String []args={category,String.valueOf(noteID)};
        sqLiteDatabase.execSQL(query,args);
        sqLiteDatabase.close();
    }
}