package com.mogaleaf.cacoo.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mogaleaf.auth.ExecuteRequest;
import com.mogaleaf.cacoo.model.Diagrams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DiagramsBuilder {
    Integer offset;
    Integer limit;
    Type type;
    SortOn sortOn;
    SortType sortType;
    Integer folderId;
    String keyWord;
    ExecuteRequest requestExecutor;
    final static String url = "https://cacoo.com/api/v1/diagrams.json";

    public DiagramsBuilder(ExecuteRequest requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public DiagramsBuilder setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public DiagramsBuilder setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public DiagramsBuilder setType(Type type) {
        this.type = type;
        return this;
    }

    public DiagramsBuilder setSortOn(SortOn sortOn) {
        this.sortOn = sortOn;
        return this;
    }

    public DiagramsBuilder setSortType(SortType sortType) {
        this.sortType = sortType;
        return this;
    }

    public DiagramsBuilder setFolderId(int folderId) {
        this.folderId = folderId;
        return this;
    }

    public DiagramsBuilder setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        return this;
    }

    public Diagrams build() throws IOException {
        String urlWithParams = buildUrl();
        InputStream inputStream = requestExecutor.executeRequest(urlWithParams);
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        Diagrams diagrams = gson.fromJson(root, Diagrams.class);
        return diagrams;
    }

    private String buildUrl() {
        StringBuilder urlBuilder = new StringBuilder(url);
        boolean hasNoParam = true;
        if (offset != null) {
            urlBuilder.append(hasNoParam ? "?offset=" : "&offset=");
            urlBuilder.append(offset);
            hasNoParam = false;
        }
        if (limit != null) {
            urlBuilder.append(hasNoParam ? "?limit=" : "&limit=");
            urlBuilder.append(limit);
            hasNoParam = false;
        }
        if (type != null) {
            urlBuilder.append(hasNoParam ? "?type=" : "&type=");
            urlBuilder.append(type);
            hasNoParam = false;
        }
        if (sortOn != null) {
            urlBuilder.append(hasNoParam ? "?sortOn=" : "&sortOn=");
            urlBuilder.append(sortOn);
            hasNoParam = false;
        }
        if (sortType != null) {
            urlBuilder.append(hasNoParam ? "?sortType=" : "&sortType=");
            urlBuilder.append(sortType);
            hasNoParam = false;
        }
        if (folderId != null) {
            urlBuilder.append(hasNoParam ? "?folderId=" : "&folderId=");
            urlBuilder.append(folderId);
            hasNoParam = false;
        }
        if (keyWord != null) {
            urlBuilder.append(hasNoParam ? "?keyWord=" : "&keyWord=");
            urlBuilder.append(keyWord);
        }
        return urlBuilder.toString();
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
