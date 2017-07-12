package com.benhouse.monitor.service;

import com.caffinc.jaggr.utils.JsonFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yunfeima on 2017/7/7.
 */
@Service
public class LogReader {
    @Autowired
    LogCacher logCacher;

    public  List<Map<String,Object>> read(int days){
        return logCacher.getLog(days);
    }

}
