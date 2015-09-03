Run benchmarks:

  mvn clean package
  java -jar target/benchmarks.jar -i 10 -f 2 -jvmArgs '-XX:+AggressiveOpts -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:-TieredCompilation -XX:-BackgroundCompilation'

Current results:

  Benchmark                                                                          Mode  Cnt  Score   Error  Units
  ImplementationBenchmarks.measureBigIntegerChecker                                    ss   20  5.697 ± 0.296   s/op
  ImplementationBenchmarks.measureByteArrayChecker                                     ss   20  0.693 ± 0.030   s/op
  ImplementationBenchmarks.measureCompactDigitStoreChecker                             ss   20  0.561 ± 0.036   s/op
  ImplementationBenchmarks.measureCompactDigitStoreCheckerWithAlternativeCarryCheck    ss   20  0.687 ± 0.043   s/op
  ImplementationBenchmarks.measureCompactDigitStoreCheckerWithInlining                 ss   20  0.547 ± 0.039   s/op

