package com.benhouse.monitor;

import com.benhouse.monitor.queue.MQConsumerService;
import com.benhouse.monitor.util.HttpServletResponseCopier;
import com.benhouse.monitor.util.MultiReadHttpServletRequest;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;

import javax.jms.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by yunfeima on 2017/6/5.
 */

public class BasicHttpFilter implements Filter {
    protected String encoding = null;
    protected FilterConfig filterConfig;

    private String mqUrl;
    private String mqUserName;
    private String mqPassword;
    public static  String monitorPath="";


    //静态文件清单,用于排除静态文件返回记录
    private final String[] resourceContentTypes = {"text/html;charset=UTF-8", "image/gif", "image/png", "application/javascript", "text/css", "text/html", "image/x-icon"};

    private static MessageProducer producer;

    private static Session session;
    private static Connection connection;

    private static MessageConsumer consumer = null;
    private static MQConsumerService mqConsumerService = null;


    public void destroy() {
        System.out.println("filter destroy");
    }


    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {


        JSONObject msg = new JSONObject();
        long beginTime = System.currentTimeMillis();
        StringBuilder postContent = new StringBuilder();
        UUID requestId=UUID.randomUUID();

        //获取请求发起时间
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int date = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);//小时
        int minute = cal.get(Calendar.MINUTE);//分


        MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
        InputStream in = multiReadRequest.getInputStream();


        if (request.getContentLength() > 0) {
            byte[] contentCache = new byte[request.getContentLength()];
            String tmp;
            int b;
            while ((b = in.read(contentCache)) != -1) {
                tmp = new String(contentCache);
                postContent.append(tmp);

            }
        }

        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
        }

        HttpServletResponseCopier responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);
        String responseContent = "";
        try {
            chain.doFilter(multiReadRequest, responseCopier);
            responseCopier.flushBuffer();
        } finally {
            byte[] copy = responseCopier.getCopy();
            responseContent = new String(copy, response.getCharacterEncoding());


        }


        long endTime = System.currentTimeMillis();


        System.out.println("static resource check :" + response.getContentType() + ";" + Stream.of(resourceContentTypes).anyMatch(type -> type.equals(response.getContentType())));
        //去掉静态文件
        if (response.getContentType() != null && !Stream.of(resourceContentTypes).anyMatch(type -> type.equals(response.getContentType()))) {

            msg.put("requestId", requestId);
            msg.put("date", year + "/" + month + "/" + date);
            msg.put("dateHour", year + "/" + month + "/" + date + " " + hour+":00:00");
            msg.put("beginTime", beginTime);
            msg.put("requestUrl", ((HttpServletRequest) request).getRequestURI());
            msg.put("postContent", postContent.toString());
            msg.put("endTime", endTime);
            msg.put("duration", endTime - beginTime);
            msg.put("response", responseContent);
            msg.put("responseContentType", response.getContentType());
            if (endTime - beginTime > 300) {
                //过长请求
                msg.put("longRequest", "Long Request");
            } else {
                msg.put("longRequest", "Normal Request");
            }
            msg.put("responseStatus", responseCopier.getStatus());


            System.out.println("msg  :" + msg.toString());
            try {
                producer.send(session.createTextMessage(msg.toString()));
                session.commit();
                System.out.println("msg sent!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        ((HttpServletResponse) response).setHeader("requestId",requestId.toString());


    }


    public void init(FilterConfig config) throws ServletException {

        mqUrl = config.getInitParameter("mqUrl");
        mqUserName = config.getInitParameter("mqUserName");
        mqPassword = config.getInitParameter("mqPassword");
        monitorPath = config.getInitParameter("monitorPath");


        //默认逻辑
        if(mqUrl==null || "".equals(mqUrl)){
            mqUrl="tcp://localhost:61616";

        }
        if(mqUserName==null || "".equals(mqUserName)){
            mqUserName="admin";

        }
        if(mqPassword==null || "".equals(mqPassword)){
            mqPassword="admin";

        }
        if(monitorPath==null || "".equals(monitorPath)){
            monitorPath= System.getProperty("user.home")+"/monitor/";

        }



        //init mq
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory; // Connection ：JMS 客户端到JMS
        // Provider 的连接
        Destination destination; // MessageProducer：消息发送者
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar



        connectionFactory = new ActiveMQConnectionFactory(
                mqUserName,
                mqPassword, mqUrl);
        try { // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("Monitor");
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            mqConsumerService = new MQConsumerService(session);

            consumer = session.createConsumer(destination);
            consumer.setMessageListener(mqConsumerService);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        System.out.println("filter init");

    }


}
