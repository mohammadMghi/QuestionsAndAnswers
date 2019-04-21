package com.example.questionandanswer.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.questionandanswer.R;
import com.example.questionandanswer.model.Question;

import java.util.ArrayList;
import java.util.List;

public class AdapterQuestion extends RecyclerView.Adapter<AdapterQuestion.NewsViewHolder> {


private Context context;
private List<Question> posts;

public AdapterQuestion(Context context, List<Question> posts){
        this.context = context;
        this.posts = posts;
        }
@Override
public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.items_question_main,parent,false);
        return new NewsViewHolder(view);
        }

@Override
public void onBindViewHolder(NewsViewHolder holder, int position) {
        final Question question=posts.get(position);
        holder.title.setText(question.getTitle());
        holder.content.setText(question.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,QuestionContentActivity.class);
                intent.putExtra("ID",question.getId());
                intent.putExtra("TITLE",question.getTitle());
                intent.putExtra("CONTENT",question.getContent());
                intent.putExtra("IMG_URL",question.getUrlImageQuestion());
                context.startActivity(intent);
            }
        });
        }

@Override
public int getItemCount() {
        return posts.size();
        }

public class NewsViewHolder extends RecyclerView.ViewHolder{
    private TextView title;
    private TextView content;


    public NewsViewHolder(View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.title_q);
        content=(TextView)itemView.findViewById(R.id.content_q);

    }
}
}