package com.example.forumtopictest;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class  PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context mContext;
    List<Post> mData;
    private AdapterView.OnItemClickListener onItemClickListener;

    ActionBar actionBar;

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

        String userID = mData.get(i).getUid();
        String userEmail = mData.get(i).getuEmail();
        String userName = mData.get(i).getuName();
        String userProfilePic = mData.get(i).getuPic();
        String postID = mData.get(i).getpID();
        String postTag = mData.get(i).getpTag();
        String postTitle = mData.get(i).getpTitle();
        String postDec = mData.get(i).getpDes();
        String postImg = mData.get(i).getpImage();
        String pTimeStamp = mData.get(i).getpTime();

//        Calendar cal = Calendar.getInstance(Locale.getDefault());
//        try {
//            cal.setTimeInMillis(Long.parseLong(pTimeStamp));
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        }
//        String PostTime = DateFormat.format("dd-MM-yyyy hh:mm aa", cal).toString();

        //set data in the post list
        holder.tv_DiscussionTitle.setText(postTitle);
        holder.tv_DiscussionTag.setText(postTag);




        //set post Image into discussion cover

        Picasso.get().load(postImg).into(holder.iv_DiscussionCover);
//        Glide.get(mContext.getApplicationContext()).load(postTag).into(holder.tv_DiscussionTag);
//        Glide.get(this).load(postTitle).into(holder.tv_DiscussionTitle);

//         if (postImg == null) {
//            holder.iv_DiscussionCover.setVisibility(View.GONE);
//        }
//         else {
//            try {
////                Glide.with(mContext).load(postImg).into(holder.iv_DiscussionCover);
//                Picasso.get().load(postImg).into(holder.iv_DiscussionCover);
//            }
//            catch (Exception e) {
//            }
//
//        }
    }

    //handle btn click



    @Override
    public int getItemCount() {
        return mData.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_DiscussionTitle, tv_DiscussionTag; //tv_DiscussionLikeNumber,tv_DiscussionCommentNumber;
        ImageView iv_DiscussionCover;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_DiscussionTitle = itemView.findViewById(R.id.ttDiscussionTitle);
            iv_DiscussionCover = itemView.findViewById(R.id.ivDiscussionCover);
            tv_DiscussionTag = itemView.findViewById(R.id.ttDiscussionTag);
//            tv_DiscussionLikeNumber = itemView.findViewById(R.id.ttDiscussionLikeNumber);
//            tv_DiscussionCommentNumber = itemView.findViewById(R.id.ttDiscussionCommentNumber);


            //link to topic detail page
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent TopicDetails = new Intent(mContext, TopicDetails.class);
                    TopicDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    int position = getAdapterPosition();
                    TopicDetails.putExtra("postImg", mData.get(position).getpImage());
                    TopicDetails.putExtra("postTag",mData.get(position).getpTag());
                    TopicDetails.putExtra("postTitle", mData.get(position).getpTitle());
                    TopicDetails.putExtra("postDes", mData.get(position).getpDes());
                    TopicDetails.putExtra("postID", mData.get(position).getpID());
                    TopicDetails.putExtra("userPic",mData.get(position).getuPic());
                    TopicDetails.putExtra("userName",mData.get(position).getuName());
                    TopicDetails.putExtra("postTime",mData.get(position).getpTime());
                    mContext.startActivity(TopicDetails);

                }


            });



        }
    }
}










