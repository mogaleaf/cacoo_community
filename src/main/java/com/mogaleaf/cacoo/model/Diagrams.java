
package com.mogaleaf.cacoo.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Diagrams {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("count")
    @Expose
    public Integer count;

}
