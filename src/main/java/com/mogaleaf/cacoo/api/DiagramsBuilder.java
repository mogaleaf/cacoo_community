package com.mogaleaf.cacoo.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mogaleaf.auth.ExecuteRequest;
import com.mogaleaf.cacoo.model.Diagrams;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class DiagramsBuilder {
    String offset = "offset";
    String limit = "limit";
    String type = "type";
    String sortOn = "sortOn";
    String sortType = "sortType";
    String folderId = "folderId";
    String keyWord = "keyWord";
    ExecuteRequest requestExecutor;
    final static String url = "https://cacoo.com/api/v1/diagrams.json";

    Map<String,String> params = new HashMap<>();

    public DiagramsBuilder(ExecuteRequest requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public DiagramsBuilder setOffset(int offsetInt) {
        params.put(offset,String.valueOf(offsetInt));
        return this;
    }

    public DiagramsBuilder setLimit(Integer limitInt) {
        params.put(limit,String.valueOf(limitInt));
        return this;
    }

    public DiagramsBuilder setType(Type typeEnum) {
        params.put(type,String.valueOf(typeEnum));
        return this;
    }

    public DiagramsBuilder setSortOn(SortOn sortOnEnum) {
        params.put(sortOn,String.valueOf(sortOnEnum));
        return this;
    }

    public DiagramsBuilder setSortType(SortType sortTypeEnum) {
        params.put(sortType,String.valueOf(sortTypeEnum));
        return this;
    }

    public DiagramsBuilder setFolderId(int folderIdInt) {
        params.put(folderId,String.valueOf(folderIdInt));
        return this;
    }

    public DiagramsBuilder setKeyWord(String keyWord) {
        params.put(keyWord,String.valueOf(keyWord));
        return this;
    }

    public Diagrams build() throws Exception {
        String urlWithParams = buildUrl();
        InputStream inputStream = requestExecutor.executeRequest(urlWithParams);
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        Diagrams diagrams = gson.fromJson(root, Diagrams.class);
        return diagrams;
    }

    private String buildUrl() throws URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        params.forEach((key,value) -> builder.addParameter(key, value));
        return builder.build().toString();
    }

    public enum Type {
        all, own, shared, stencil, template, recyclebin
    }

    public enum SortOn {
        updated, title, owner, folder
    }

    enum SortType {
        desc, asc
    }
}
