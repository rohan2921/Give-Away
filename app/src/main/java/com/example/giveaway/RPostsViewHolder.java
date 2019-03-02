package com.example.giveaway;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RPostsViewHolder extends RecyclerView.ViewHolder {

    View view;

    public RPostsViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public  void setname (String name) {
        TextView n = view.findViewById(R.id.name);
        n.setText(name);

    }

    public void setpost(String post) {
        TextView n = view.findViewById(R.id.ptext);
        n.setText(post);

    }

    public  void setimage(String image) {
        ImageView i = view.findViewById(R.id.image);
        Picasso.get().load(image).into(i);
    }

    public void setpimage(String pimage) {
        CircleImageView pi = view.findViewById(R.id.pimage);
        Picasso.get().load(pimage).placeholder(R.drawable.profile).into(pi);

    }
}
