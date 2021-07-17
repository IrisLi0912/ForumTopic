package com.example.forumtopictest;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class  PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context mContext;
    List<Post> mData;
    FirebaseUser firebaseUser;


    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.activity_post_list, parent, false);
        return new ViewHolder(row);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int i) {
        //get data

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mData.get(i);

        String userID = mData.get(i).getUid();
        String userEmail = mData.get(i).getuEmail();
        String userName = mData.get(i).getuName();
        String userProfilePic = mData.get(i).getuPic();
        String postID = mData.get(i).getpID();
        String postTag = mData.get(i).getpTag();
        String postTitle = mData.get(i).getpTitle();
        String postDec = mData.get(i).getpDes();
        String postImg = mData.get(i).getpImage();


        //set data in the post list
        holder.tv_DiscussionTitle.setText(postTitle);
        holder.tv_DiscussionTag.setText(postTag);
        nrLikes(holder.tv_DiscussionLikeNumber, post.getpID());
        nrComments(holder.tv_DiscussionCommentNumber, post.getpID());


        //set post Image into discussion cover

        Picasso.get().load(postImg).into(holder.iv_DiscussionCover);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_DiscussionTitle, tv_DiscussionTag, tv_DiscussionLikeNumber,tv_DiscussionCommentNumber;
        ImageView iv_DiscussionCover;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_DiscussionTitle = itemView.findViewById(R.id.ttDiscussionTitle);
            iv_DiscussionCover = itemView.findViewById(R.id.ivDiscussionCover);
            tv_DiscussionTag = itemView.findViewById(R.id.ttDiscussionTag);
            tv_DiscussionLikeNumber = itemView.findViewById(R.id.ttDiscussionLikeNumber);
            tv_DiscussionCommentNumber = itemView.findViewById(R.id.ttDiscussionCommentNumber);


            //link to topic detail page
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent TopicDetails = new Intent(mContext, TopicDetailActivity.class);
                    TopicDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    int position = getAdapterPosition();
                    TopicDetails.putExtra("postImg", mData.get(position).getpImage());
                    TopicDetails.putExtra("postTag",mData.get(position).getpTag());
                    TopicDetails.putExtra("postTitle", mData.get(position).getpTitle());
                    TopicDetails.putExtra("postDes", mData.get(position).getpDes());
                    TopicDetails.putExtra("postID", mData.get(position).getpID());
                    TopicDetails.putExtra("userPic",mData.get(position).getuPic());
                    TopicDetails.putExtra("userName",mData.get(position).getuName());
                    TopicDetails.putExtra("postTime",mData.get(position).getPostTime());
                    mContext.startActivity(TopicDetails);

                }


            });


        }
    }

    //count number of likes for each topic
    private void nrLikes(final TextView tv_DiscussionLikeNumber, String postID ){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Likes");
        ref.child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                tv_DiscussionLikeNumber.setText(snapshot.getChildrenCount()+ " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    ////count number of comments for each topic
    public void nrComments(final TextView tv_DiscussionCommentNumber, String postID ){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Comment");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //TODO: add filter before count the number of comment

                tv_DiscussionCommentNumber.setText(snapshot.getChildrenCount()+ "comments");




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

        private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm aa",calendar).toString();
        return date;

    }

}










