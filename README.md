# Jython on GraalVM

## Context

The goal of this experiment was to add the support for the *Graal JIT* to *jython*, a Python interpreter running on the JVM.
The implementation was made following [this guide](http://stefan-marr.de/2015/11/add-graal-jit-compilation-to-your-jvm-language-in-5-easy-steps-step-1/).

## Preliminary observations

After making the decision to use *Jython* for this experiment, the first thing I did was to rewrite the *Mandelbrot* benchmark in Python, staying as close to the original as possible.

I then ran the benchmarks and got the following results:
* The Python benchmark is around 2 times faster with *Jython* than with the regular *Python2.7* interpreter
* The native *Java* benchmark is more than 20 times faster than the Python benchmark with *Jython*

I was expecting faster results with the native Java code, but definitely not by a factor of 20...

My Python code could be an issue (since I wanted to keep it as simple as possible), but even then I thought that there was room for improvements and went on with the implementation.

## Implementation

The *Truffle* framework is used if Jython is started with a file and with the argument `--truffle`.

After that, the class `ASTVisitor` (that wound up not being an actual Visitor) makes a translation of the Jython AST using the *Truffle* framework.
The new AST is then executed by the *Graal* VM.

The implementation part was very straightforward, and I only had a few serious issues:
* First of all, there was no way to differenciate between an access to a function parameter or a local variable with the Jython AST, so I had to manually add the parameters to the frame during a function call and treat them as local variables
* Also, as I didn't want to compile the whole `time` module to run the benchmark, I created a special node to manage calls to `time.time()`

My first implementation could only use objects, and adding the support for primitive types reduced the execution time by a factor of 4 (which corresponds to the results obtained by the author of the guide).

## Final results

Here are the results obtained by running the [Mandelbrot](./benchmarks) benchmark 100 times for each interpreter, and keeping the 10 last results:

Interpreter | Time (µs)
--- | ---
Java | 110 311 ± 3 527
Jython + Graal | 148 214 ± 1 334
Jython | 2 814 300 ± 539 700
Python2.7 | 5 528 607 ± 300 144

In the end, the results are very close to native Java, and the execution time was actually divided by nearly 20, which seemed impossible at first.
