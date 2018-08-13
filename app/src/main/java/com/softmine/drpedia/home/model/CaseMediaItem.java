package com.softmine.drpedia.home.model;

import java.io.Serializable;

public class CaseMediaItem implements Serializable{

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    String thumbnail;
    String image;
    String video;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    String src;

    public boolean isImageAvailable()
    {
        return (image!=null && image!="")?true:false;
    }

    public boolean isVideoAvailable()
    {
        return (video!=null && video!="")?true:false;
    }

    public boolean isThumbnailAvailable()
    {
        return (thumbnail!=null && thumbnail!="")?true:false;
    }

}
