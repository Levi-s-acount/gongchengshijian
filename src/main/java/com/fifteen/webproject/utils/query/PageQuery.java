package com.fifteen.webproject.utils.query;

import java.io.Serializable;

public class PageQuery<T> extends Query<T> implements Serializable {
    private Integer currentPage;
    private Integer pageSize;

    public PageQuery() {
    }

    public PageQuery(T queryBody) {
        super(queryBody);
    }

    public PageQuery(T queryBody, Integer currentPage, Integer pageSize) {
        super(queryBody);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String toString() {
        return "PageQuery{currentPage=" + this.currentPage + ", pageSize=" + this.pageSize + '}';
    }
}
