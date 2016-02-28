package com.example.jb.thirtyme.List;


import java.lang.String; /**
 * @author danielme.com
 */
public class Content extends Item{

    private String name;
    private String hashtag;
    private int followcnt;
    private int likecnt;
    private String imagepath;


    public Content(String name, String imagepath, String hashtag, int followcnt, int likent) {
        this.imagepath = imagepath;
        this.name = name;
        this.hashtag = hashtag;
        this.followcnt = followcnt;
        this.likecnt = likecnt;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getImagepath() {
        return imagepath;
    }
    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public void setHashtag(String hashtag){
       this.hashtag = hashtag;
    }
    public String getHashtag(){
       return hashtag;
    }

    public void setLikent(int likecnt){
        this.likecnt = likecnt;
    }
    public int getLikecnt(){
        return likecnt;
    }

    public void setFollowcnt(int followcnt){
        this.followcnt = followcnt;
    }
    public int getFollownt(){
        return followcnt;
    }
}
