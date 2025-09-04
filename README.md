# Event-Driven Order Processing System (Java)


## Features
- Domain Model: `Order`, `OrderItem`, `OrderStatus`
- Events: `OrderCreatedEvent`, `PaymentReceivedEvent`, `ShippingScheduledEvent`, `OrderCancelledEvent`
- Ingestion: Read from a `.jsonl` file (one event per line)
- Processing: Maintains orders and transitions status (`PENDING` → `PARTIALLY_PAID` → `PAID` → `SHIPPED`/`CANCELLED`)
- Observers: `LoggerObserver` and `AlertObserver`


## Build & Run
```bash
mvn -q clean package
java -cp target/classes:$(mvn -q -Dexec.executable=echo --non-recursive \
org.codehaus.mojo:exec-maven-plugin:3.5.0:classpath -Dexec.args='%classpath') \
org.example.ordersystem events.jsonl
```
> On Windows PowerShell, replace the classpath with `;` instead of `:` and consider running via IDE.

## Run in IntelliJ IDEA (Recommended 🚀)

1. Open the project in IntelliJ (**File → Open** and select the project root).  
2. Wait for **Maven** to download & index dependencies.  
3. Open `src/main/java/org/example/ordersystem.java`.  
4. Right-click on the `main` method → **Run 'ordersystem.main()'**.  
5. Configure program arguments:  
   - Go to **Run → Edit Configurations…**  
   - Select your run configuration (`Application`)  
   - In the **Program arguments** field, enter:  
     ```
     events.jsonl
     ```
6. Place your `events.jsonl` file in the **project root** (same folder as `pom.xml`).  
7. Run again → Output will appear in **IntelliJ console**.  



## Sample Input (`events.jsonl`)
```json
{"eventId":"e1","timestamp":"2025-07-29T10:00:00Z","eventType":"OrderCreated","orderId":"ORD001","customerId":"CUST001","items":[{"itemId":"P001","qty":2},{"itemId":"P002","qty":1}],"totalAmount":150.00}
{"eventId":"e2","timestamp":"2025-07-29T10:02:00Z","eventType":"PaymentReceived","orderId":"ORD001","amountPaid":50.00}
{"eventId":"e3","timestamp":"2025-07-29T10:05:00Z","eventType":"PaymentReceived","orderId":"ORD001","amountPaid":100.00}
{"eventId":"e4","timestamp":"2025-07-29T11:00:00Z","eventType":"ShippingScheduled","orderId":"ORD001","shippingDate":"2025-07-30"}
{"eventId":"e5","timestamp":"2025-07-29T12:05:00Z","eventType":"OrderCreated","orderId":"ORD002","customerId":"CUST777","items":[{"itemId":"X","qty":1}],"totalAmount":999.0}
{"eventId":"e6","timestamp":"2025-07-29T12:06:00Z","eventType":"OrderCancelled","orderId":"ORD002","reason":"Out of stock"}
{"eventId":"e7","timestamp":"2025-07-29T12:06:30Z","eventType":"FooUnknown","orderId":"ORD001"}
```


## Expected Output (snippet)
```
[Logger] Event processed: OrderCreated(#e1) @ 2025-07-29T10:00:00Z -> Order status: PENDING
[Logger] Event processed: PaymentReceived(#e2) @ 2025-07-29T10:02:00Z -> Order status: PARTIALLY_PAID
[Logger] Order ORD001 status changed: PENDING -> PARTIALLY_PAID
[Logger] Event processed: PaymentReceived(#e3) @ 2025-07-29T10:05:00Z -> Order status: PAID
[Logger] Order ORD001 status changed: PARTIALLY_PAID -> PAID
[ALERT] Sending alert for Order ORD001: Status changed to PAID
[Logger] Event processed: ShippingScheduled(#e4) @ 2025-07-29T11:00:00Z -> Order status: SHIPPED
[Logger] Order ORD001 status changed: PAID -> SHIPPED
[ALERT] Sending alert for Order ORD001: Status changed to SHIPPED
[WARN] Unsupported event type: FooUnknown


=== FINAL ORDERS STATE ===
Order{orderId='ORD001', customerId='CUST001', items=[{P001:2}, {P002:1}], totalAmount=150.0, status=SHIPPED, eventHistory=[...]}
Order{orderId='ORD002', customerId='CUST777', items=[{X:1}], totalAmount=999.0, status=CANCELLED, eventHistory=[...]}
```
