package com.example.questionandanswer.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.questionandanswer.R;
import com.example.questionandanswer.model.Answers;
import com.example.questionandanswer.model.Question;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAnswers extends RecyclerView.Adapter<AdapterAnswers.NewsViewHolder> {


    private Context context;
    private List<Answers> answers;

    public AdapterAnswers(Context context, List<Answers> answers){
        this.context = context;
        this.answers = answers;
    }
    @Override
    public AdapterAnswers.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itme_answer,parent,false);
        return new AdapterAnswers.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAnswers.NewsViewHolder holder, int position) {
        final Answers answer =answers.get(position);
        holder.username.setText(answer.getContent());
        holder.content.setText(answer.getContent());
        if(!(answer.getUrlProfile() == "null")) {
            Picasso.get().load("http://192.168.1.4:8000/storage/" + answer.getUrlProfile()).into(holder.imgProfile);
        }
        else {
            holder.imgProfile.setImageResource(R.drawable.default_profile);
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        private TextView username;
        private TextView content;
        private ImageView imgProfile;

        public NewsViewHolder(View itemView) {
            super(itemView);
            username  =  itemView.findViewById(R.id.txt_username_answer);
            content=  itemView.findViewById(R.id.txt_answer_content);
            imgProfile = itemView.findViewById(R.id.img_profile_answer);
        }
    }
}