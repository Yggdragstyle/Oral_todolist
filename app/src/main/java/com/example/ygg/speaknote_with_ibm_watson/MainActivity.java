package com.example.ygg.speaknote_with_ibm_watson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


//      implements ISpeechDelegate


//    @Override
//    public void onOpen() {
//
//    }
//
//    @Override
//    public void onError(String s) {
//
//    }
//
//    @Override
//    public void onClose(int i, String s, boolean b) {
//
//    }
//
//    @Override
//    public void onMessage(String s) {
//
//    }
//
//    @Override
//    public void onAmplitude(double v, double v1) {
//
//    }


    // PROPERTIES
    TextView note;
    Switch record;
    SpeechToText speechToText;

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

                if(isChecked) {
                    RecordStream();
                }
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





//    private boolean initSTT() throws MalformedURLException, URISyntaxException {
//        // initialize the connection to the Watson STT service
//        String username = "";
//        String password = "";
//        String tokenFactoryURL = "";
//        String model = "";
//        String serviceURL = "wss://stream.watsonplatform.net/speech-to-text/api";
//        SpeechConfiguration sConfig = new SpeechConfiguration(SpeechConfiguration.AUDIO_FORMAT_OGGOPUS);
//
//        com.ibm.watson.developer_cloud.android.speech_to_text.v1.SpeechToText.sharedInstance().initWithContext(new URI(serviceURL), getApplicationContext(), sConfig);
//
//        sharedInstance().initWithContext(new URI(serviceURL), getApplicationContext(), sConfig);
//        // Basic Authentication
//        sharedInstance().setCredentials(username, password);
//        sharedInstance().setModel(model);
//        sharedInstance().setDelegate(this);
//        return true;
//    }






        // Audio RECORD
    private Boolean RecordStream() {

        speechToText = initSpeechToTextService();

//        SpeechToText.sharedInstance().initWithContext(new URI("wss://stream.watsonplatform.net/speech-to-text/api"), this.getApplicationContext(), new SpeechConfiguration());

        // Configuration
//        SpeechConfiguration sConfig = new SpeechConfiguration(SpeechConfiguration.AUDIO_FORMAT_OGGOPUS);
//        // STT
//        sharedInstance().initWithContext(new URI("http..."), this.getApplicationContext(), sConfig);
//
//        sharedInstance().setCredentials("", "");
//        sharedInstance().setDelegate(this);
//
//        sharedInstance().setModel("fr-FR_BroadbandModel");
//
//        sharedInstance().recognize();
//
//
//
//        sharedInstance().stopRecording();






//        SpeechToText service = new SpeechToText();
//        service.setUsernameAndPassword("<username>", "<password>");
//
//        InputStream audio = new FileInputStream("src/test/resources/sample1.wav");
//
//        RecognizeOptions options = new RecognizeOptions.Builder()
//                .interimResults(true)
//                .contentType(HttpMediaType.AUDIO_WAV)
//                .build();
//
//        service.recognizeUsingWebSocket(audio, options, new BaseRecognizeCallback() {
//            @Override
//            public void onTranscription(SpeechResults speechResults) {
//                System.out.println(speechResults);
//            }
//        });
//
//        // wait 20 seconds for the asynchronous response
//        Thread.sleep(20000);






//
//        // Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
//        int sampleRate = 16000;
//        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
//        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
//
//        if (!AudioSystem.isLineSupported(info)) {
//            System.out.println("Line not supported");
//            System.exit(0);
//        }
//
//        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
//        line.open(format);
//        line.start();
//
//        InputStream audio = new StreamPlayer(line);






//        AudioRecord recorder = null;
//        AudioTrack track = null;
//        short[][]   buffers  = new short[256][160];
//        int ix = 0;
//
//        /*
//         * Initialize buffer to hold continuously recorded audio data, start recording, and start
//         * playback.
//         */
//        try
//        {
//            int N = AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
//            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N*10);
//            track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
//                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, N*10, AudioTrack.MODE_STREAM);
//            recorder.startRecording();
//            track.play();
//            /*
//             * Loops until something outside of this thread stops it.
//             * Reads the data from the recorder and writes it to the audio track for playback.
//             */
//            while(!stopped)
//            {
//                Log.i("Map", "Writing new data to buffer");
//                short[] buffer = buffers[ix++ % buffers.length];
//                N = recorder.read(buffer,0,buffer.length);
//                track.write(buffer, 0, buffer.length);
//            }
//        }
//        catch(Throwable x)
//        {
//            Log.w("Audio", "Error reading voice audio", x);
//        }
//        /*
//         * Frees the thread's resources after the loop completes so that it can be run again
//         */
//        finally
//        {
//            recorder.stop();
//            recorder.release();
//            track.stop();
//            track.release();
//        }



////
        RecognizeOptions options = new RecognizeOptions.Builder()
//                .continuous(true)
                .interimResults(true)
                //.inactivityTimeout(5) // use this to stop listening when the speaker pauses, i.e. for 5s
                .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + "1600")
                .build();



        MicrophoneHelper microphoneHelper = new MicrophoneHelper(this);

//        // record PCM data without encoding
        MicrophoneInputStream myInputStream = microphoneHelper.getInputStream(false);

        // record PCM data and encode it with the ogg codec
        MicrophoneInputStream myOggStream = microphoneHelper.getInputStream(true);


        speechToText.recognizeUsingWebSocket(myInputStream, options, new BaseRecognizeCallback() {

            @Override
            public void onTranscription(SpeechResults speechResults){
                String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
                System.out.println(text + "@Ygg_TAG");
            }

            @Override
            public void onError(Exception e) {
            }

            @Override public void onDisconnected() {
            }

        });





//        speechToText.recognizeUsingWebSocket(audio, options, new BaseRecognizeCallback() {
//            @Override
//            public void onTranscription(SpeechResults speechResults) {
//                System.out.println(speechResults);
//            }
//        });

//        System.out.println("Listening to your voice for the next 30s...");
//        Thread.sleep(30 * 1000);

// closing the WebSockets underlying InputStream will close the WebSocket itself.
//        line.stop();
//        line.close();

        System.out.println("Fin. @Ygg_TAG");

        return true;
    }


        // Watson TASK
    private class WatsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    note.setText("run the thread");
//                    note.setText( speechToText.toString() );
                }
            });

            return "get my speech !";
        }
    }
}
