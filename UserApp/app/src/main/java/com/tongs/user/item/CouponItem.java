package com.tongs.user.item;

/**
 * Created by JaeCheol on 15. 5. 24..
 */
public class CouponItem {

    String id;
    String title;
    String location;
    String description;
    String time;
    String url;

    public String getId()   {
        return id;
    }
    public void setId(String _id)    {
        id = _id;
    }

    public String getUrl()  {
        return url;
    }
    public void setUrl(String _url) {
        url = _url;
    }

    public String getDescription()  {
        return description;
    }
    public void setDescription(String _description)    {
        description = _description;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String _title)   {
        title = _title;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String _location)   {
        location = _location;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String _time)   {
        time = _time;
    }
}
