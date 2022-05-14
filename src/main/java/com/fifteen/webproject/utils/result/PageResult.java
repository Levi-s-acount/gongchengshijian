package com.fifteen.webproject.utils.result;


import java.io.Serializable;
import java.util.List;

public class PageResult<T> extends ResultSet<T> implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPage;

    public PageResult() {
    }

    public PageResult(List<T> results, Boolean succeed, String message, Long size) {
        super(results, succeed, message, size);
    }

    public PageResult(List<T> results, Boolean succeed, String message, Long size, Integer currentPage, Integer pageSize, Integer totalPage) {
        super(results, succeed, message, size);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
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

    public Integer getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public String toString() {
        return "PageResult{currentPage=" + this.currentPage + ", pageSize=" + this.pageSize + ", totalPage=" + this.totalPage + '}';
    }
}
