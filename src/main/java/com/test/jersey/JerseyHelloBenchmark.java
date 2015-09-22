/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.jersey;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.test.util.server.ContainerRequestBuilder;
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
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 8, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 8, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class JerseyHelloBenchmark {

    private volatile ApplicationHandler handler;
    private volatile ContainerRequest request;

    @Setup
    public void start() throws Exception {
        handler = new ApplicationHandler(new JerseyAppConfig());
        request = ContainerRequestBuilder
                .from("/hello?test=HelloWorld", "GET")
                .build();
    }

    @Setup(Level.Iteration)
    public void request() {
        request = ContainerRequestBuilder
                .from("/hello?test=HelloWorld", "GET")
                .build();
    }

    @TearDown
    public void shutdown() {
    }

    @Benchmark
    public Future<ContainerResponse> measure() throws Exception {
        return handler.apply(request);
    }

    public static void main(String[] args) throws Exception {
        final Options opt = new OptionsBuilder()
                .include(JerseyHelloBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
