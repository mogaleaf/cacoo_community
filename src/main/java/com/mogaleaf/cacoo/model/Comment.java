
package com.mogaleaf.cacoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("updated")
    @Expose
    public String updated;

}
