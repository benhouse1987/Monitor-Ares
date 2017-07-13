package com.benhouse.monitor.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by yunfeima on 2017/6/19.
 */
public class QueryParam {

    //analyze dimensions
    private String groupKey;
    //log within days
    private int days;
    //log of fileDate;
    private String fileDate;
    private Map<String,Object> criteria;
    private int page;
    private int pageSize;

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Map<String, Object> getCriteria() {
        return criteria;
    }

    public void setCriteria(Map<String, Object> criteria) {
        this.criteria = criteria;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }
}
