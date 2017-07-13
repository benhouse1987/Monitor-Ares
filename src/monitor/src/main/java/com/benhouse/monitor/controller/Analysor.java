package com.benhouse.monitor.controller;

import com.benhouse.monitor.dto.AnalyzeResult;
import com.benhouse.monitor.dto.QueryParam;
import com.benhouse.monitor.dto.RequestDetail;
import com.benhouse.monitor.service.LogFilter;
import com.benhouse.monitor.service.LogReader;
import com.caffinc.jaggr.core.Aggregation;
import com.caffinc.jaggr.core.AggregationBuilder;
import com.caffinc.jaggr.core.operations.*;
import com.caffinc.jaggr.utils.JsonFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yunfeima on 2017/6/16.
 */
@Controller
public class Analysor {
    @Value("${monitor.path:}")
    private String monitorPath;


    @Value("${HOME}")
    private String homePath;

    @Autowired
    LogReader logReader;

    @Autowired
    LogFilter logFilter;

    @RequestMapping("analyze")
    @ResponseBody
    public AnalyzeResult analyze(@RequestBody QueryParam queryParam) {
        AnalyzeResult result = new AnalyzeResult();
        List<Map<String, Object>> data = logReader.read(queryParam.getDays());
        List<Map<String, Object>> tmpdata = null;


        if (queryParam.getCriteria() != null) {
            tmpdata = logFilter.filter(data, queryParam.getCriteria(), queryParam.getPage(), queryParam.getPageSize());
        } else {
            tmpdata = data;
        }

        Aggregation aggregation = new AggregationBuilder()
                .setGroupBy(queryParam.getGroupKey())
                .addOperation("avg", new AverageOperation("duration"))
                .addOperation("sum", new SumOperation("duration"))
                .addOperation("min", new MinOperation("duration"))
                .addOperation("max", new MaxOperation("duration"))
                .addOperation("count", new CountOperation())
                .getAggregation();


        result.setDetailResult(tmpdata);
        result.setGridResult(aggregation.aggregateCombined(tmpdata));
        return result;
    }


    @RequestMapping("detail")
    @ResponseBody
    public RequestDetail getRequestDetail(@RequestBody QueryParam queryParam) {
        RequestDetail requestDetail=new RequestDetail();
        List<Map<String, Object>> data = logReader.readDetail(queryParam.getFileDate());
        List<Map<String, Object>> requestLog =
                logFilter.filter(data, queryParam.getCriteria(), queryParam.getPage(), queryParam.getPageSize());;
        requestDetail.setPostContent(requestLog.get(0).get("postContent").toString());
        requestDetail.setResponseContent(requestLog.get(0).get("response").toString());

        return  requestDetail;

    }


}
