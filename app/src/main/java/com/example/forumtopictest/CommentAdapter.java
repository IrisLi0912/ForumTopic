package com.example.forumtopictest;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;


    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.activity_discussion_comment_list, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        Comment item = mData.get(position);

        Glide.with(mContext).load(item.getUimg()).into(holder.ivCommentProfilePic);
        holder.ttUsername.setText(item.getUname());
        holder.ttDiscussionComment.setText(item.getContent());
        holder.ddDate.setText(timestampToString((Long)mData.get(position).getTimestamp()));


}


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView ivCommentProfilePic;
        TextView ttUsername,ttDiscussionComment,ddDate;
//        ImageView  ttLike; //ttReply

        public CommentViewHolder(View itemView) {
            super(itemView);
            ivCommentProfilePic = itemView.findViewById(R.id.ivCommentProfilePic);
            ttUsername = itemView.findViewById(R.id.ttUsername);
            ttDiscussionComment = itemView.findViewById(R.id.ttDiscussionComment);
            ddDate = itemView.findViewById(R.id.ddDate);


        }
    }



    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm",calendar).toString();
        return date;


    }


}
