/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.spring;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 8, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 8, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class SpringHelloBenchmark {

    private volatile AnnotationConfigWebApplicationContext appContext;
    private volatile FrameworkServlet dispatcherServlet;
    private volatile HttpServletRequest request;
    private volatile HttpServletResponse response;
    private volatile Method processRequest;

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

    @Setup(Level.Iteration)
    public void request() throws Exception {
        request = new MockHttpServletRequest("GET", "/hello");
        response = new MockHttpServletResponse();
    }

    @Benchmark
    public void measure() throws Exception {
        processRequest.invoke(dispatcherServlet, request, response);
    }

    public static void main(String[] args) throws Exception {
        final Options opt = new OptionsBuilder()
                .include(SpringHelloBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
