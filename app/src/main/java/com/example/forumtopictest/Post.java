package com.example.forumtopictest;

import com.google.firebase.database.ServerValue;

public class Post {

    private String pID;
    private String pTag;
    private String pTitle;
    private String pDes;
    private String pImage;
    private String uid;
    private String uEmail;
    private String uName;
    private String uPic;
    private Long postTime;

    private int commentCounter;



    public Post() {
    }


    public Long getPostTime() {
        return postTime;
    }

    public void setPostTime(Long postTime) {
        this.postTime = postTime;
    }

    public Post(String pID, String pTag, String pTitle, String pImage, String uid, String uEmail, String uName, Long postTime) {
        this.pID = pID;
        this.pTag = pTag;
        this.pTitle = pTitle;
        this.pImage = pImage;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uName = uName;
        this.postTime = postTime;
    }


    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpTag() {
        return pTag;
    }

    public void setpTag(String pTag) {
        this.pTag = pTag;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDes() {
        return pDes;
    }

    public void setpDes(String pDes) {
        this.pDes = pDes;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic;
    }

    public int getCommentCounter() {
        return commentCounter;
    }

    public void setCommentCounter(int commentCounter) {
        this.commentCounter = commentCounter;
    }

}