package com.benhouse.monitor.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yunfeima on 2017/7/7.
 */
@Service
public class LogFilter {
    public List<Map<String, Object>> filter(List<Map<String, Object>> data, Map<String, Object> criteria,int page,int pageSize){


       if(page!=0) {
           return data.stream().skip(page * pageSize).parallel().filter((d) -> {
               for (Map.Entry<String, Object> entry : criteria.entrySet()) {

                   if (!String.valueOf(d.get(entry.getKey())).equals(entry.getValue())) {
                       return false;
                   }

               }

               return true;
           }).limit(pageSize).collect(Collectors.toList());
       }else {
           return data.stream().parallel().filter((d) -> {
               for (Map.Entry<String, Object> entry : criteria.entrySet()) {

                   if (!String.valueOf(d.get(entry.getKey())).equals(entry.getValue())) {
                       return false;
                   }

               }


               return true;
           }).collect(Collectors.toList());

       }

    }
}
