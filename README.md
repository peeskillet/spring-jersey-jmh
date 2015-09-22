Spring MVC JMH Throughput Test
====

####To Run:

>    mvn clean package

To run Jersey test

>    java -jar target/benchmarks.jar JerseyHello

To run Spring test

>    java -jar target/benchmarks.jar SpringHello

Running Spring, you should see this stacktrace

```java
java.lang.reflect.InvocationTargetException
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:483)
        at com.test.spring.SpringHelloBenchmark.measure(SpringHelloBenchmark.java:67)
        at com.test.spring.generated.SpringHelloBenchmark_measure.measure_thrpt_jmhStub(SpringHelloBenchmark_measure.java:149)
        at com.test.spring.generated.SpringHelloBenchmark_measure.measure_Throughput(SpringHelloBenchmark_measure.java:83)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:483)
        at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:452)
        at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:434)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
        at java.lang.Thread.run(Thread.java:745)
Caused by: java.lang.NullPointerException
        at org.springframework.web.servlet.FrameworkServlet.publishRequestHandledEvent(FrameworkServlet.java:1073)
        at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1002)
        ... 17 more
```

The NPE points to [this line (1073) in the FrameworkServlet][1], which leads me to believe the
`webApplicationContext` is null. But I am not sure how when I set it here in the benchmark

```java
@Setup
public void start() throws Exception {
    appContext = new AnnotationConfigWebApplicationContext();
    appContext.register(SpringAppConfig.class);
    dispatcherServlet = new DispatcherServlet(appContext);

    processRequest = dispatcherServlet.getClass().getSuperclass()
            .getDeclaredMethod("processRequest",
                    HttpServletRequest.class,
                    HttpServletResponse.class);
    processRequest.setAccessible(true);
}
```

[1]: https://github.com/spring-projects/spring-framework/blob/master/spring-webmvc/src/main/java/org/springframework/web/servlet/FrameworkServlet.java#L1073
