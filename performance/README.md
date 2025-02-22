# Performance | Java | Quickstarts

> **NOTE:** You cannot just jump around to see how to do things. You need to understand the context of advices given. Some techniques if not applied correctly could cause more harm than good.

- Over time what was good practice for performance it is not anymore (e.g.: having big methods to have less of them... Today the opisite is true because of Just-in-Time (JIT) compilers)
- At the begining it was more about dev productivity
- Hotspot change things and made it more about performance
- The JVM provides automatic memory management with pluggable garbage collection subsystem (memory does not need to be track manually by the dev)
- With Java you give up *managed subsystems* so the dev does not need to worry about capability under management
- Performance of the JVM is based on tuning flags
- Performance of the platform is determined more by using best practices in the application code
- The env. for dev and testing are often considered separate areas
- In Java 7u4 you have a new garbage collector (GC) algorithm called G1

> The Java and the JVM are open source. You can contribute: http://openjdk.java.net/

> You have a commercial version of the JVM it includes more tools for performance like: Java Flight Recorder

> Most if these notes are for the open source version of the JVM. (Oracle standard (HotSpot-based))

- Tunning flags can be different from commercial vs open source versions of the JVM (diff. dep. on the JVM)
- Default value for flags varies from one release to another

> **Flag types:** There are two: boolean and ones that require parameter

**Boolean flag:** 
To enable: 
```
-XX:+*FlagName*
```
To disable: 
```
-XX:-*FlagName*
```

**Parameter flag:** 
```
-XX:*FlagName*=*param*
```

> Flags default values can be influence by the env and/or the command-line arguments to the JVM.

> **NOTE:** Flags affected by the env is called *ergonomics*.

- JVM from Oracle and OpenJDK sites is called the "product" build of the JVM
- When the JVM is built from the source code you can get one of these builds types: 
    - debug builds
    - developer builds
    - and many more
- When built you can get more features
    - More flags

## Client Class and Server Class
- Java ergonomics is based on the notion that some machines are "client" class and some are "server" class
    - Map directly to the compiler (for specific platform)
- The GC (garbage collector) is determined by the class of a machine
- *Client-class* machines are any 32-bit JVM
    - Running on Windows (regardless of the number of CPUs)
    - Running on one CPU (regardless of the OS)
- *Server-class* ,achines are any 64-bit JVM
    - Any other machines

## The Story
- You have outside influences that affect performance
- The performance of the JVM and the Java platform is a small part of performance
### Outside influences
- Algorithms
    - How well written the code is
    - Using `HashMap` can help with arrays
    - An example would be to use *Binary Search*
- Write less code
    - Better performance
    - More obj = more have to be allocated (and discarded) -> meaning more work the garbage collector has to do
    - More obj -> GC cycle will take longer
    - More classes the more it needs to read from the drive (slower start)
    - More code the higher the chance it won't fit in the hardware caches

> **NOTE:** *NetBeans* can help by flaging that might cause performance issue. For example if you have a logging that is not called because it is not set with that level of loging Java will still load the code that was put it in (like a method called inside). **Solution:** wrap it with a condition (`if`).

Example: 
```java
if (l.isLoggable(Level.WARN)) {
    log.log(...);
}
```
