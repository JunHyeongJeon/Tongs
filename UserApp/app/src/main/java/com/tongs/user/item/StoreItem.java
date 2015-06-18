package com.tongs.user.item;

/**
 * Created by JaeCheol on 15. 5. 23..
 */
public class StoreItem {

    private String id;
    private String name;
    private String location;
    private String description;
    private String waitingNum;
    private String url;

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

    public String getName() {
        return name;
    }
    public void setName(String _name)   {
        name = _name;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String _location)   {
        location = _location;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String _description)   {
        description = _description;
    }

    public String getWaitingNum() {
        return waitingNum;
    }
    public void setWaitingNum(String _waitingNum)   {
        waitingNum = _waitingNum;
    }
}
