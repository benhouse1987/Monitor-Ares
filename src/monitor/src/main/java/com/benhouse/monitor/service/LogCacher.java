package com.benhouse.monitor.service;

import com.caffinc.jaggr.utils.JsonFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yunfeima on 2017/7/10.
 */
@Service
public class LogCacher {
    private static ConcurrentHashMap<String,List<Map<String,Object>>> logCache=new ConcurrentHashMap();

    @Value("${monitor.path:}")
    private  String monitorPath;


    @Value("${HOME}")
    private  String homePath;
    @Autowired
    LogReader logReader;

    public List<Map<String,Object>> getLog(int days){
        //遍历拼合各个文件日志记录,并读取最新的当天记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        Date logDate = c.getTime();
        String logDateNumber = String.valueOf(sdf.format(logDate));

        List<Map<String,Object>> result=new ArrayList<>();

        result.addAll(this.read(logDateNumber));
        for (int i=days-1;i>0;i--) {
            c.add(Calendar.DATE, -1);
            logDate = c.getTime();
            logDateNumber = String.valueOf(sdf.format(logDate));

            if (logCache.get(logDateNumber)==null){


                logCache.put(logDateNumber, this.read(logDateNumber));

            }
            result.addAll(logCache.get(logDateNumber));

        }
        return result;

    }




    public  List<Map<String,Object>> read(String logDate){
        //读取数据
        String path = homePath + monitorPath+logDate+".reqdtl"; // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return  new ArrayList<>();
        }

        try {
            return JsonFileUtil.readJsonFromFile(path);
        }catch (Exception e){
            e.printStackTrace();
        }

        return  null;


    }



}
