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

> Most if these notes are for the open source version of the JVM.

- Tunning flags can be different from commercial vs open source versions of the JVM
