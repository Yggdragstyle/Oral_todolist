package com.example.ygg.speaknote_with_ibm_watson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    // PROPERTIES
    TextView note;
    Switch record;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    SpeechToText speechToText;
    MicrophoneHelper microphoneHelper;
    MicrophoneInputStream myOggStream;
    RecognizeOptions options;
    List<Transcript> speechResults;

    List<Note> notes = new ArrayList<Note>();
    MyAdapter notesAdaptater;

    SharedPreference sharedPreference = new SharedPreference();

    boolean locked = false;




        // ON RUN...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record = findViewById(R.id.record);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // récupère les notes depuis le SharedPreference
        notes = sharedPreference.getNotes(getApplicationContext());

        progressBar.setVisibility(View.INVISIBLE);

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //puis créer un MyAdapter, lui fournir notre liste de Notes.
        //cet adapter servira à remplir notre recyclerview
        notesAdaptater = new MyAdapter(notes);
        recyclerView.setAdapter(notesAdaptater);


        // Lorsqu'ont change l'état du bouton RECORD
        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked && !get_locked()) { RecordStream(); }
                else { EndRecord(); }
            }
        });

    }




    // METHODS

        // instanciate SPEECH to TEXT
    private SpeechToText initSpeechToTextService() {

        SpeechToText service = new SpeechToText();

        String urlWatson = "https://stream.watsonplatform.net/speech-to-text/api";
        String  userName = "154b0334-35dd-4822-97e5-e88fbe7907c1";
        String  password = "MZ0hu3g3sD7M";

        service.setUsernameAndPassword(userName, password);
        service.setEndPoint(urlWatson);

        return service;
    }



        // Audio RECORD
    private Boolean RecordStream() {

        set_locked(true);

        speechToText = initSpeechToTextService();

        options = new RecognizeOptions.Builder().contentType(ContentType.OPUS.toString())
//                .keywords(new String[]{"banane", "ananas", "cerise", "pomme"})
                .model("fr-FR_BroadbandModel").interimResults(true).inactivityTimeout(2000).build();

        microphoneHelper = new MicrophoneHelper(this);

        myOggStream = microphoneHelper.getInputStream(true);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    speechToText.recognizeUsingWebSocket(myOggStream, options, new BaseRecognizeCallback() {

                        @Override
                        public void onTranscription(SpeechResults results) { speechResults = results.getResults(); }

                        @Override
                        public void onError(Exception e) { showError(e); }

                        @Override
                        public void onDisconnected() {

//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    record.setChecked(false);
//                                }
//                            });
//                          showError( new DisconnectException() );
                        }
                    });
                }
                catch (Exception e) {

                    set_locked(false);
                    showError(e);
                }
            }
        }).start();



        System.out.println("Fin. @Ygg_TAG");

        return true;
    }


    private void EndRecord() {

        // close stream
        microphoneHelper.closeInputStream();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(2000);


                    if (speechResults != null && !speechResults.isEmpty()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String text = speechResults.get(0).getAlternatives().get(0).getTranscript();

                                // ajoute une nouvelle note
                                Note note = new Note(text);
                                notes.add(note);
                                sharedPreference.addNote(getApplicationContext(), note);
                                notesAdaptater.notifyDataSetChanged();

                                showError( new Exception(text) );

                                System.out.println(text + "@Ygg_TAG");
                            }
                        });


//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//
//                        String text = ">>>   ";
//
//                        for (Transcript transcript : speechResults){
//                            for (SpeechAlternative speechAlternative : transcript.getAlternatives()) {
//
//                                text += " <|\n|> " + speechAlternative.getTranscript();
//
////                                        Thread.sleep(200);
//                            }
//                        }
//
//                        showError(new Exception(text));
                    }

                } catch (Exception e) {
                    showError(e);
                }
                finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            set_locked(false);
                        }
                    });
                }
            }
        }).start();
    }


        // if ERROR
    private void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(e.getMessage() + "@Ygg_TAG");
                e.printStackTrace();
            }
        });
    }




    // GET | SET

    public boolean get_locked() { return locked; }
    public void set_locked(boolean value) {

        this.locked = value;
        progressBar.setVisibility( value ? View.VISIBLE : View.INVISIBLE);
    }


}

