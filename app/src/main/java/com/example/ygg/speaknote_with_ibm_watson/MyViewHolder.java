package com.example.ygg.speaknote_with_ibm_watson;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;



public class MyViewHolder extends RecyclerView.ViewHolder{

    private TextView textViewView;
//    private CheckBox validate;

    //itemView est la vue correspondante à 1 cellule
    public MyViewHolder(View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView
        textViewView = itemView.findViewById(R.id.noteText);
//        validate = itemView.findViewById(R.id.validate);

//        validate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // suprimer l'item si il est coché
//                if(validate.isChecked()) {  }
//                else {  }
//            }
//        });
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(Note myNote){

        textViewView.setText(myNote.get_msg());
    }
}

