package com.example.ygg.speaknote_with_ibm_watson;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

    public static final String PREFS_NAME = "ORAL_NOTE_APP";
    public static final String FAVORITES = "NOTES";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveNotes(Context context, List<Note> notes) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonNotes = gson.toJson(notes);

        editor.putString(FAVORITES, jsonNotes);

        editor.commit();
    }

    public void addNote(Context context, Note note) {

        List<Note> notes = getNotes(context);

        if (notes == null)
            notes = new ArrayList<Note>();
        notes.add(note);
        saveNotes(context, notes);
    }

    public void removeNote(Context context, Note note) {
        ArrayList<Note> notes = getNotes(context);
        if (notes != null) {
            notes.remove(note);
            saveNotes(context, notes);
        }
    }

    public ArrayList<Note> getNotes(Context context) {
        SharedPreferences settings;
        List<Note> notes;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonNotes = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Note[] noteItems = gson.fromJson(jsonNotes,
                    Note[].class);

            notes = Arrays.asList(noteItems);
            notes = new ArrayList<Note>(notes);
        } else
            return new ArrayList<Note>();

        return (ArrayList<Note>) notes;
    }
}