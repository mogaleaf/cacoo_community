
package com.mogaleaf.cacoo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sheet {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("imageUrlForApi")
    @Expose
    public String imageUrlForApi;
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("width")
    @Expose
    public Integer width;
    @SerializedName("height")
    @Expose
    public Integer height;

}
