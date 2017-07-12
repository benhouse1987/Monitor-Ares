package com.benhouse.monitor.queue;

import com.benhouse.monitor.BasicHttpFilter;
import org.json.JSONObject;

import javax.jms.*;


import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 1. 数据接收线程监听ActiveMQ队列，并读取消息。。
 */
public class MQConsumerService implements MessageListener {

    private Session session;
    public MQConsumerService(Session s){
        session=s;
    }

    /**
     * MessageListener.onMessage
     */
    public void onMessage(Message message) {


        try {
             session.commit();

            //todo
            /**
             * 查找基础数据待写入文件,如果没有则创建
             * 写入基础请求数据
             * 查找分析数据待写入文件,如果没有则创建
             * 依次计算各个指标,并更新
             */

            Date current = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateNowStr = sdf.format(current);


            //请求基本信息
            String baseFileName = dateNowStr + ".reqdtl";
            //详细报文数据
            String detailFileName=dateNowStr + "-detail.reqdtl";

            JSONObject sourceData=new JSONObject(((TextMessage) message).getText());
            JSONObject detailData=new JSONObject();
            detailData.put("requestId",sourceData.get("requestId"));
            detailData.put("postContent",sourceData.get("postContent"));
            detailData.put("response",sourceData.get("response"));
            sourceData.remove("postContent");
            sourceData.remove("response");

            String filePath= BasicHttpFilter.monitorPath+baseFileName;
            String detailFilePath= BasicHttpFilter.monitorPath+detailFileName;
            System.out.println("begin write :" + filePath);


            FileWriter writer = new FileWriter(filePath, true);
            writer.write(sourceData.toString() + "\n");
            writer.flush();

            FileWriter detailWriter = new FileWriter(detailFilePath, true);
            detailWriter.write(detailData.toString() + "\n");
            detailWriter.flush();



            System.out.println("after write :" + filePath);


            System.out.println("consume!================");
            System.out.println(((TextMessage) message).getText());
            System.out.println("=========================");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }

    }
}