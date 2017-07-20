# Ares

Ares is  a request monitor which can help us to get a whole picture of our system live running status.

# Live Show
You can have a quick look of Ares from here

[Ares Monitor](http://sugarclub.oss-cn-shanghai.aliyuncs.com/monitor.html)

Homemade server, so it's a little slow :)

## Features
### Request Analyzer
Use Ares Request Analyzer to help you analyze your system requests!

![Analyzer Area](/documents/images/ss1.png)

The analyzer can filter request datas by date range, and you can choose request dimentions which you care about to analyze.
Dimentions including the following:

```
1. Request Path
2. Response Status
3. Is Long Request
4. Request Time
```

![Analyzer Area](/documents/images/ss4.png)

When you click one of the X Axis lable, the request belongs to will be showed in detail in the table below.
Now you can sort request detail by click the table header title.
You can also do a live filter by typing in the search textbox.

![Analyzer Area](/documents/images/ss2.png)

### Request Track
Ares will add an unique request id to each response header, to help us locate a specified request.
You can check up the request body and response content detail here.

![Analyzer Area](/documents/images/ss3.png)

## Install
### Quick Launch
1. Install Active MQ for request log collector usage. The default user and password would be admin/admin.
2. Locate the tomcat you want to monit, copy install/request-monitor-0.0.1-SNAPSHOT-jar-with-dependencies.jar to the tomcat's lib/ directory. For example (tomcat/lib/)
3. Put install/monitor.war into tomcat's webapps directory. For example (tomcat/webapps/)
4. Change config tomcat's config file web.xml(tomcat/conf/), add log collector filter

```
......
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.htm</welcome-file>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

      <filter>
     <filter-name>My Filter</filter-name>
     <filter-class>com.benhouse.monitor.BasicHttpFilter</filter-class>
  </filter>
  
  <filter-mapping>
     <filter-name>My Filter</filter-name>
     <url-pattern>/*</url-pattern>
  </filter-mapping>

</web-app>
```

Now it's done!

Start your tomcat, open your browser and visit url
'ip:port/monitor'
such as localhost:8080/monitor




