# KRecords: Easy File Loading in Kotlin

`KRecords` is a lightweight Kotlin library with a humble, yet useful, mission:
making file record loading easy to write and easy to read.

Thus, the following file (`orders.txt`):

````
Mac Air|$1,099|3|Y|2024-03-14
Magic Mouse|$67.99|2|N|2024-03-15
````

can be processed like so:

````kotlin
class Order(fields: List<String>) : KRecord(fields) {
    val name = string(0)
    val price = bigDecimal(1, "$###,####.##")
    val quantity = int(2)
    val deliveryDate = localDate(4, "yyyy-MM-dd")
}

KRecord(::Order, FileReader("orders.txt"), "|")
    .forEach { order ->
        val weekDay = order.deliveryDate.dayOfWeek
        val total = order.price * order.quantity.toBigDecimal()
        println("${order.name}: deliver on $weekDay, collect $total")
    }
````

which will print:

```
Mac Air: deliver on THURSDAY, collect 3297
Magic Mouse: deliver on FRIDAY, collect 135.98
```