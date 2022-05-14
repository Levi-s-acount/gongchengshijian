package com.fifteen.webproject.utils.query;

import java.io.Serializable;


public class Query<T> implements Serializable {
    private T queryBody;

    public Query() {
    }

    public Query(T queryBody) {
        this.queryBody = queryBody;
    }

    public T getQueryBody() {
        return this.queryBody;
    }

    public void setQueryBody(T queryBody) {
        this.queryBody = queryBody;
    }

    public String toString() {
        return "Query{queryBody=" + this.queryBody + '}';
    }
}
