package com.example.forumtopictest;


import com.google.firebase.database.ServerValue;

public class Comment {

    private String content,uid,uimg,uname;
    private Object timestamp;

    private String postLinker;


    public Comment() {
    }

    public Comment(String content, String uid, String uimg, String uname, String postLinker) {
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.postLinker = postLinker;
        this.timestamp = ServerValue.TIMESTAMP;

    }

    public Comment(String content, String uid, String uimg, String uname, Object timestamp) {
        //normally, this method should not be used
        this.content = content;
        this.uid = uid;
        this.uimg = uimg;
        this.uname = uname;
        this.postLinker = postLinker;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getPostLinker() {
        return postLinker;
    }

    public void setPostLinker(String postLinker) {
        this.postLinker = postLinker;
    }
}
