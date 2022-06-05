# Performance | Java | Quickstarts

> **NOTE:** You cannot just jump around to see how to do things. You need to understand the context of advices given. Some techniques if not applied correctly could cause more harm than good.

- Over time what was good practice for performance it is not anymore (e.g.: having big methods to have less of them... Today the opisite is true because of Just-in-Time (JIT) compilers)
- At the begining it was more about dev productivity
- Hotspot change things and made it more about performance
- The JVM provides automatic memory management with pluggable garbage collection subsystem (memory does not need to be track manually by the dev)
- With Java you give up *managed subsystems* so the dev does not need to worry about capability under management
