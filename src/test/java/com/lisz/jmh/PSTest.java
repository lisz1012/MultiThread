package com.lisz.jmh;

import org.openjdk.jmh.annotations.*;

public class PSTest {
    @Benchmark
    @Warmup(iterations = 1, time = 3)
    @Fork(5)
    @BenchmarkMode(Mode.Throughput) // 每秒钟多少个下面的方法
    public void testForeach() {
        PS.foreach();
    }
}
/*
# JMH version: 1.21
# VM version: JDK 12.0.2, Java HotSpot(TM) 64-Bit Server VM, 12.0.2+10
# VM invoker: /Library/Java/JavaVirtualMachines/jdk-12.0.2.jdk/Contents/Home/bin/java
# VM options: -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.lisz.jmh.PSTest.test

# Run progress: 0.00% complete, ETA 00:08:20
# Fork: 1 of 5
# Warmup Iteration   1: 179928.275 ops/s
# Warmup Iteration   2: 180132.045 ops/s
# Warmup Iteration   3: 167874.568 ops/s
# Warmup Iteration   4: 169665.435 ops/s
# Warmup Iteration   5: 167390.751 ops/s
Iteration   1: 162745.909 ops/s
Iteration   2:

比老马的机器快
 */
