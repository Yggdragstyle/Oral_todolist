package com.example.ygg.speaknote_with_ibm_watson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    // PROPERTIES
    TextView note;
    Switch record;
    SpeechToText speechToText;

    MicrophoneHelper microphoneHelper;
    MicrophoneInputStream myInputStream;
    MicrophoneInputStream myOggStream;
    RecognizeOptions options;
    List<Transcript> speechResults;

    boolean stopped = true;

        // ON RUN...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        note = (TextView)findViewById(R.id.note);
        record = (Switch)findViewById(R.id.record);

        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) { RecordStream(); }
                else { microphoneHelper.closeInputStream(); }
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

        speechToText = initSpeechToTextService();

//        options = new RecognizeOptions.Builder()
////                .continuous(true)
//                .interimResults(true)
//                //.inactivityTimeout(5) // use this to stop listening when the speaker pauses, i.e. for 5s
//                .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + "1600")
//                .build();



        //                                      .continuous(true)
        options = new RecognizeOptions.Builder().contentType(ContentType.OPUS.toString())
                .model("fr-FR_BroadbandModel").interimResults(true).inactivityTimeout(2000).build();

        microphoneHelper = new MicrophoneHelper(this);

        //        // record PCM data without encoding
//        myInputStream = microphoneHelper.getInputStream(false);

        // record PCM data and encode it with the ogg codec
        myOggStream = microphoneHelper.getInputStream(true);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    speechToText.recognizeUsingWebSocket(myOggStream, options, new BaseRecognizeCallback() {

                        @Override
                        public void onTranscription(SpeechResults results) {


                            speechResults = results.getResults();

                            if (speechResults != null && !speechResults.isEmpty()) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        String text = speechResults.get(0).getAlternatives().get(0).getTranscript();

                                        showError( new Exception(text) );

                                        System.out.println(text + "@Ygg_TAG");
                                    }
                                });
                            }

                        }

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
                    showError(e);
                }
            }
        }).start();



        System.out.println("Fin. @Ygg_TAG");

        return true;
    }



        // if ERROR
    private void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

}

