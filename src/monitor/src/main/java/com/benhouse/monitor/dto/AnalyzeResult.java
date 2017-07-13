package com.benhouse.monitor.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by yunfeima on 2017/7/12.
 */
public class AnalyzeResult {
    private List<Map<String,Object>> gridResult;
    private List<Map<String,Object>> detailResult;

    public List<Map<String, Object>> getGridResult() {
        return gridResult;
    }

    public void setGridResult(List<Map<String, Object>> gridResult) {
        this.gridResult = gridResult;
    }

    public List<Map<String, Object>> getDetailResult() {
        return detailResult;
    }

    public void setDetailResult(List<Map<String, Object>> detailResult) {
        this.detailResult = detailResult;
    }
}
