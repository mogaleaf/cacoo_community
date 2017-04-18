package com.mogaleaf.community.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Diagram {

    @SerializedName("name")
    @Expose
    public String name = null;

    @SerializedName("id")
    @Expose
    public String id = null;

    @SerializedName("imageUrl")
    @Expose
    public String imageUrl = null;

    @SerializedName("rate")
    @Expose
    public Double rate = null;

    @SerializedName("numberOfRate")
    @Expose
    public Integer numberOfRate = null;

}
