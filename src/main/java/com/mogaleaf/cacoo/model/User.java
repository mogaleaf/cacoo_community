
package com.mogaleaf.cacoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("nickname")
    @Expose
    public String nickname;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;

}
