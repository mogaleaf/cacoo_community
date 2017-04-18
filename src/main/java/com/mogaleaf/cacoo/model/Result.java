
package com.mogaleaf.cacoo.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("imageUrlForApi")
    @Expose
    public String imageUrlForApi;
    @SerializedName("diagramId")
    @Expose
    public String diagramId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("security")
    @Expose
    public String security;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("ownerName")
    @Expose
    public String ownerName;
    @SerializedName("ownerNickname")
    @Expose
    public String ownerNickname;
    @SerializedName("owner")
    @Expose
    public Owner owner;
    @SerializedName("editing")
    @Expose
    public Boolean editing;
    @SerializedName("own")
    @Expose
    public Boolean own;
    @SerializedName("shared")
    @Expose
    public Boolean shared;
    @SerializedName("folderId")
    @Expose
    public Integer folderId;
    @SerializedName("folderName")
    @Expose
    public String folderName;
    @SerializedName("sheetCount")
    @Expose
    public Integer sheetCount;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("updated")
    @Expose
    public String updated;
    @SerializedName("sheets")
    @Expose
    public List<Sheet> sheets = null;
    @SerializedName("comments")
    @Expose
    public List<Comment> comments = null;

}
