package com.example.ygg.speaknote_with_ibm_watson;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class MyViewHolder extends RecyclerView.ViewHolder{

    private TextView textViewView;

    //itemView est la vue correspondante à 1 cellule
    public MyViewHolder(View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView
        textViewView = (TextView) itemView.findViewById(R.id.noteText);
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(Note myNote){

        textViewView.setText(myNote.get_msg());
    }
}

