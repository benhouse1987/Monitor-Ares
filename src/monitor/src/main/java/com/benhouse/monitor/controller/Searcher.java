package com.benhouse.monitor.controller;

import com.benhouse.monitor.dto.QueryParam;
import com.benhouse.monitor.service.LogFilter;
import com.benhouse.monitor.service.LogReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by yunfeima on 2017/7/7.
 */
@Controller
public class Searcher {

    @Autowired
    LogReader logReader;

    @Autowired
    LogFilter logFilter;

    @RequestMapping("/api/search")
    @ResponseBody
    public List<Map<String, Object>> search(@RequestBody QueryParam queryParam,HttpServletRequest httpServletRequest){

        List<Map<String, Object>> data =logReader.read(queryParam.getDays());

        if (data == null) {
            data= logReader.read(queryParam.getDays());
            httpServletRequest.getSession().setAttribute("currentData" + queryParam.getDays(), data);
        }
        Map<String,Object> criteria=queryParam.getCriteria();
       return logFilter.filter(data,criteria,queryParam.getPage(),queryParam.getPageSize());

    }
}
