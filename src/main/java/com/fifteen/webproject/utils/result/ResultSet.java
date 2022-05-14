package com.fifteen.webproject.utils.result;


import java.io.Serializable;
import java.util.List;

public class ResultSet<T> implements Serializable {
    private List<T> results;
    private Boolean succeed;
    private String message;
    private Long size;

    public ResultSet() {
    }

    public ResultSet(List<T> results, Boolean succeed, String message, Long size) {
        this.results = results;
        this.succeed = succeed;
        this.message = message;
        this.size = size;
    }

    public List<T> getResults() {
        return this.results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public Boolean getSucceed() {
        return this.succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getSize() {
        return this.size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String toString() {
        return "ResultSet{results=" + this.results + ", succeed=" + this.succeed + ", message='" + this.message + '\'' + ", size=" + this.size + '}';
    }
}
