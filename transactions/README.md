# Transactions | Java | Quickstarts
- Transaction = unit of work
- Transaction library/manager = makes model working
- Transaction "model" (there might be other words to describe it) = provides guarantees

## How helps
- Avoid business environment data corruption
- Protects against data corruption
  - by guaranteeing complete, accurate business transactions for Java based applications (including those written for the Java EE and EJB frameworks)

## Javax Transaction vs. Jakarta EE Transaction
Are just transaction APIs. They are the same.

Depending on the JAVA project the backend will be different.

Examples: 
- **OFBiz:** [Apache Geronimo Transaction Manager (geronimo-txmanager)](https://geronimo.apache.org/)
- **Quarkus:** [Narayana](https://www.narayana.io/)

> [!NOTE]
> OFBiz uses [Apache Geronimo Transaction Manager (geronimo-txmanager)](https://geronimo.apache.org/) backend.
> It is also known as: Geronimo TX. It provides the Java Transaction API (JTA).
> 
> Key Components in OFBiz Transactions 
> - **Transaction Manager**: Provided by **Geronimo TX** (org.apache.geronimo.transaction.manager.TransactionManagerImpl). 
> - **JTA API**: Uses `javax.transaction` interfaces. 
> - **JNDI Lookup**: In some cases, javax.transaction.UserTransaction is retrieved via JNDI. 
> - **XA Support**: **Geronimo TX** supports XA transactions (2PC - Two-Phase Commit).
> 
> Maven configurations: 
> ```xml
> <dependency>
>    <groupId>org.apache.geronimo.specs</groupId>
>    <artifactId>geronimo-jta_1.1_spec</artifactId>
>    <version>1.1.1</version>
> </dependency>
> 
> <dependency>
>    <groupId>org.apache.geronimo.transaction</groupId>
>    <artifactId>geronimo-transaction</artifactId>
>    <version>3.1.4</version>
> </dependency>
> ```
> 
> Look for transaction configuration in `framework/entity/config/entityengine.xml`:
> ```xml
> <transaction-factory class="org.apache.ofbiz.entity.transaction.DerbyTransactionFactory" />
> ```
> 
> Check for `TransactionManagerImpl` in the code:
> ```java
> import org.apache.geronimo.transaction.manager.TransactionManagerImpl;
> 
> TransactionManager txManager = new TransactionManagerImpl();
> ```

## Rules
- Atomicity:
  - To be able to get back to the starting point
  - So that you can try again (if failure occurs)
- Consistency 
  - Data can be place at multiple places at once (multiply)
- Isolation
  - If multiple parties tries to get the data then only one will be able to get it (not more)
- Durability
  - ...

## Quarkus
- [Narayana](https://www.narayana.io/) is what is used by Quarkus

> [!NOTE]
> It is integrated in projects such as: 
> - WildFly application runtime
> - Quarkus application framework
> - Apache Tomcat server (Tomcat is used by [OFBiz](https://github.com/apache/ofbiz-framework))
> - Apache Camel
> - Spring framework

### Narayana
- [Narayana](https://www.narayana.io/) GitHub: https://github.com/jbosstm/narayana
  - transaction manager
  - drive standards including the OMG and Web Services

#### Transaction Modules
Transaction library: 
- JTS transactions
  - Old concept 👴
  - OTS/JTS
    - OTS (Object Transaction Service) standard
    - ORB services - component of the ecosystem
  - OTS defines operations as methods on objects
   - Using IIOP as the communication protocol
  - JTS (Java Transaction Service)
   - Specification for transactional interoperability between EJB containers based on CORBA, OTS and JTA
- JTA transactions
- Long Running Action 🏃‍♂️
- XTS
- Software Transaction Memory
- REST - AT

#### Utils
- https://github.com/jbosstm/transaction-analyser
- https://github.com/jbosstm/performance

## Migrating from Geronimo TX to Narayana in Quarkus
Since **Geronimo TX** is a lightweight transaction manager but lacks advanced distributed transaction handling, switching to Narayana (Quarkus default) is beneficial.

### How to Replace **Geronimo TX** in **SunsetERP**
1. Remove **Geronimo** dependencies (if they exist).
2. Use `jakarta.transaction` API, as Narayana fully supports it.
3. ...

	SunsetERP (Quarkus Narayana)	Migration Action
javax.transaction	jakarta.transaction	Rename package imports
TransactionManagerImpl (Geronimo)	TransactionManager (Narayana)	Use Quarkus CDI injection
JNDI lookup (java:comp/UserTransaction)	@Inject UserTransaction	Direct CDI injection
Manual transactions	@Transactional	Prefer annotation-based transactions
REQUIRES_NEW for nested transactions	Separate CDI beans	Move logic to different classes

| OFBiz (Geronimo TX)                         | Quarkus Narayana                | Migration Action                     |
|---------------------------------------------|---------------------------------|--------------------------------------|
| `javax.transaction`                         | `jakarta.transaction`           | Rename package imports               |
| `TransactionManagerImpl` (Geronimo)         | `TransactionManager` (Narayana) | Use Quarkus CDI injection            |
| JNDI lookup (`java:comp/UserTransaction`)   | `@Inject UserTransaction`       | Direct CDI injection                 |
| Manual transactions                         | `@Transactional`                | Prefer annotation-based transactions |
| `REQUIRES_NEW` for nested transactions      | Separate CDI beans              | Move logic to different classes      |

## Comparison: Apache Geronimo TX vs. Narayana (Quarkus)
| Feature	                                     | Geronimo Transaction Manager (OFBiz)	           | Narayana (Quarkus)                                             |
|----------------------------------------------|-------------------------------------------------|----------------------------------------------------------------|
| Default in	                                  | Apache OFBiz, Geronimo, Tomcat	                 | Quarkus, WildFly, JBoss EAP                                    |
| JTA Compliance	                              | Supports `javax.transaction` 	                  | Supports `jakarta.transaction`                                 |
| Transaction API	                             | Implements JTA (`javax.transaction`)	           | Implements JTA (`jakarta.transaction`)                         |
| XA Transactions	                             | Yes (Supports XA but limited features)	         | Yes (Fully supports XA with advanced features)                 |   
| Two-Phase Commit (2PC)	                      | Yes	                                            | Yes (Optimized for distributed transactions)                   |
| Nested Transactions	                         | No native support	                              | No native support (workaround using separate CDI beans)        |
| Recovery Support	                            | Basic support	                                  | Advanced (can recover across different nodes)                  |
| Performance	                                 | Lightweight	                                    | More robust, better for high-concurrency apps                  |
| Manual Transaction Management	               | Requires JNDI (`java:comp/UserTransaction`)	    | CDI injection (`@Inject UserTransaction`)                      |
| Transaction Propagation	                     | Uses `TransactionAttributeType`	                | Uses `@Transactional`                                          |
| JNDI Requirement	                            | Required for UserTransaction	                   | Not required (uses CDI injection)                              |
| Standalone Usage	                            | Can run outside an app server	                  | Can run standalone but best with Quarkus                       |
| Compatibility	                               | Works with Tomcat, Geronimo	                    | Works with Quarkus, WildFly, JBoss                             |
| Customizability	                             | Basic transaction tuning	                       | Advanced (fine-grained tuning for timeouts, XA recovery, etc.) |
| Configuration Location	                      | Hardcoded in OFBiz (`entityengine.xml`)	        | `application.properties` in Quarkus                            |
| Maturity & Active Development                | Legacy, minimal active development	             | Actively maintained and improved                               |

### Key Takeaways

1. Narayana is more advanced than Geronimo TX, especially in distributed transactions, recovery, and performance.
2. Migration is mostly a package rename, but:
  - `@Transactional` is preferred over manually managing UserTransaction.
  - JNDI lookups (`java:comp/UserTransaction`) should be removed in favor of CDI (`@Inject UserTransaction`).
  - Nested transactions require separate CDI beans to work in Quarkus.
3. **Narayana is battle-tested** for modern microservices and cloud applications, making it a more robust choice.

### **tricky areas**

#### 🚨 Hard-to-Transition Parts and Solutions

| Issue                                                    | Geronimo TX                                                                 | Problem in Quarkus (Narayana)                                            | Solution                                                  |
|----------------------------------------------------------|-----------------------------------------------------------------------------|--------------------------------------------------------------------------|-----------------------------------------------------------|
| JNDI Lookup for Transactions                             | Uses `java:comp/UserTransaction` via InitialContext lookup                  | Quarkus does not use JNDI for transactions                               | Use CDI injection (`@Inject UserTransaction`) instead     |
| Manual Transaction Management                            | `UserTransaction.begin()` and `TransactionManager.begin()` calls are common | Not needed in Quarkus, prefer `@Transactional`                           | Replace with `@Transactional` when possible               |
| Nested Transactions (`REQUIRES_NEW`)                     | Geronimo TX allows them                                                     | Quarkus does NOT support nested transactions within the same CDI bean    | Move nested transactions to a separate CDI bean           |
| Global Transaction Rollback                              | Some OFBiz services manually call `TransactionManager.rollback()`           | Quarkus auto-manages rollback via `@Transactional(dontRollbackOn = ...)` | Use `@Transactional(rollbackOn = Exception.class)`        |
| Transaction Configuration Location                       | Configured in `entityengine.xml` or custom Java classes                     | Quarkus does not use XML for transactions                                | Use application.properties for transaction settings       |
| Thread-based Transaction Handling                        | Some OFBiz services store transaction state in thread-local storage         | Narayana does not use ThreadLocals for transactions                      | Let Quarkus manage transactions via `@Transactional`      |
| Multi-Resource (XA) Transactions                         | OFBiz sometimes directly configures XA transactions                         | Narayana requires proper XA data sources setup                           | Use `quarkus.datasource.jdbc.transactions=xa`             |
| Suspending Transactions (`TransactionManager.suspend()`) | OFBiz sometimes suspends transactions manually                              | Narayana does not support transaction suspension                         | Use `@Transactional(REQUIRES_NEW)` in a separate CDI bean |

#### 🚨 More Details on Hard-to-Migrate Features
##### 1️⃣ JNDI Lookup for Transactions
**Before (OFBiz - Uses JNDI)**
```java
InitialContext ctx = new InitialContext();
UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

tx.begin();
try {
    processOrder();
    tx.commit();
} catch (Exception e) {
    tx.rollback();
}
```

**After (Quarkus - Uses CDI Injection)**
```java
import jakarta.transaction.UserTransaction;
import jakarta.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    UserTransaction userTransaction;

    public void processOrder() throws Exception {
        userTransaction.begin();
        try {
            // Database operations
            userTransaction.commit();
        } catch (Exception e) {
            userTransaction.rollback();
            throw e;
        }
    }
}
```

✅ JNDI lookup is removed, and `UserTransaction` is now injected using CDI.

##### 2️⃣ Nested Transactions (`REQUIRES_NEW`)
OFBiz allows nested transactions within the same class, but Quarkus does not.
The solution is to move the `REQUIRES_NEW` transaction to a separate CDI bean.

**Before (OFBiz - Nested Transactions in Same Class)**
```java
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public void processOrder() {
    updateInventory();
    processPayment(); // Runs in a new transaction
}

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public void processPayment() {
    // Payment logic
}
```

**After (Quarkus - Move Nested Transactions to Separate CDI Bean)**
```java
@ApplicationScoped
public class OrderService {

    @Inject
    PaymentService paymentService;

    @Transactional
    public void processOrder() {
        updateInventory();
        paymentService.processPayment(); // Runs in a separate transactional context
    }
}

@ApplicationScoped
public class PaymentService {

    @Transactional // Runs in a new transaction because it's in a separate bean
    public void processPayment() {
        // Payment logic
    }
}
```
✅ This ensures Quarkus creates a new transaction for `processPayment()`.

##### 3️⃣ Global Rollback Handling
OFBiz sometimes manually rolls back transactions using TransactionManager.rollback(), but in Quarkus, this is automatically handled by @Transactional.

**Before (OFBiz - Manual Rollback)**
```java
try {
    transactionManager.begin();
    orderService.processOrder();
    transactionManager.commit();
} catch (Exception e) {
    transactionManager.rollback();
}
```

**After (Quarkus - Auto Rollback with `@Transactional`)**
```java
@ApplicationScoped
public class OrderService {

    @Transactional(rollbackOn = Exception.class)
    public void processOrder() {
        // Database operations
    }
}
```
✅ Quarkus automatically rolls back transactions on exceptions.

##### 4️⃣ Transaction Configuration
OFBiz uses XML (`entityengine.xml`) to configure transactions, but Quarkus does not.
Instead, transaction settings are configured in `application.properties`.

Before (OFBiz - `entityengine.xml`)
```xml
<transaction-factory class="org.apache.ofbiz.entity.transaction.DerbyTransactionFactory" />
```

**After (Quarkus - `application.properties`)**
```properties
quarkus.transaction-manager.enable-recovery=true
quarkus.transaction-manager.default-timeout=60
quarkus.transaction-manager.node-identifier=SunsetERP
```
✅ All transaction settings are now in `application.properties`.

| OFBiz (Geronimo TX)                               | 	Quarkus (Narayana)                        | Migration Action                                   |
|---------------------------------------------------|--------------------------------------------|----------------------------------------------------|
| `<transaction-factory>` in `entityengine.xml`     | 🚫 Not needed                              | Remove it                                          |
| `DerbyTransactionFactory.getTransactionManager()` | 🚫 Not needed                              | Replace with `@Inject TransactionManager`          |
| `TransactionManager.begin()` manually             | Handled by `@Transactional`                | Prefer `@Transactional`                            |
| **XA Transactions** configured in Java            | Configured via `application.properties`    | Set `quarkus.transaction-manager.*` properties     |


##### 5️⃣ Multi-Resource (XA) Transactions
OFBiz allows multi-resource XA transactions, but Quarkus requires explicit XA setup.

**Before (OFBiz - Default Transactions)**
- OFBiz uses `TransactionManagerImpl` which supports XA but requires manual setup.

**After (Quarkus - Enable XA Transactions)**
- Configure XA in `application.properties`:
```properties
quarkus.datasource.jdbc.transactions=xa
```
✅ This ensures that transactions can span multiple resources.

##### 6️⃣ Suspending Transactions
Some OFBiz code suspends transactions using TransactionManager.suspend(), but Quarkus does not support suspending transactions. The workaround is to use separate CDI beans.

**Before (OFBiz - Uses `suspend()`)**
```java
Transaction tx = transactionManager.suspend();
try {
    processPayment();
} finally {
    transactionManager.resume(tx);
}
```

**After (Quarkus - Separate CDI Bean)**
```java
@ApplicationScoped
public class PaymentService {

    @Transactional // Ensures it runs in a separate transaction
    public void processPayment() {
        // Payment logic
    }
}
```
✅ Quarkus does not support suspending transactions, so we isolate the logic in another CDI bean.

#### ✅ Final Takeaways

| Problematic Feature                 | Geronimo TX                               | Quarkus (Narayana) Solution                     |
|-------------------------------------|-------------------------------------------|-------------------------------------------------|
| JNDI Lookup (`UserTransaction`)     | Uses `java:comp/UserTransaction`          | Use CDI injection (`@Inject UserTransaction`)   |
| Manual Transactions                 | Explicit `begin()` and `commit()` calls   | Use `@Transactional` instead                    |
| Nested Transactions                 | Uses REQUIRES_NEW in same class           | Move to a separate CDI bean                     |
| Manual Rollback                     | Calls `TransactionManager.rollback()`     | Handled by `@Transactional`                     |
| Transaction Configuration           | Defined in `entityengine.xml`             | Use `application.properties`                    |
| Multi-Resource (XA) Transactions    | Manually configured                       | Set `quarkus.datasource.jdbc.transactions=xa`   |
| Transaction Suspension              | Uses `TransactionManager.suspend()`       | Use a separate CDI bean instead                 |

## Resources
- [Introduction to Transactions](https://www.baeldung.com/cs/transactions-intro)
- [Introduction to Transactions in Java and Spring](https://www.baeldung.com/java-transactions)
- [Using Transactions | Oracle](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html)
- [Understanding JTA - The Java Transaction API | progress](https://www.progress.com/tutorials/jdbc/understanding-jta)
- [Java transaction service](https://en.wikipedia.org/wiki/Java_transaction_service)
### Quarkus
- [Narayana: Java library for transaction processing - DevConf.CZ 2021 | DevConf | YouTube](https://www.youtube.com/watch?v=2l6zKdpoPZI)
- [Transactions, J2EE, Java EE, Jakarta EE, MicroProfile and Quarkus | Adam Bien | YouTube](https://www.youtube.com/watch?v=DFQi69briiI)
- [Using transactions in Quarkus | quarkus.io](https://quarkus.io/guides/transaction)
### Jakarta EE
- [Jakarta Transactions 2.0](https://jakarta.ee/specifications/transactions/2.0/jakarta-transactions-spec-2.0.pdf)