package com.example.ygg.speaknote_with_ibm_watson;

import android.widget.Switch;

public class DisconnectException extends Exception
{
    public DisconnectException() {

        super("We're disconnected of Watson API\n");
    }
}
