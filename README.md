# KRecords: Easy File Loading in Kotlin

`KRecords` is a lightweight Kotlin library with a humble, yet useful, mission:
making file record loading easy to read and easy to write.

Thus, given the following file (`orders.txt`):

````
Mac Air|$1,099|3|2024/03/14
Magic Mouse|$67.99|2|2024/03/15
````

we can process its contents like so:

````kotlin
class Order(fields: List<String>) : KRecord(fields) {
  val name = string(0)
  val price =
    bigDecimal(1, "$###,####.##")
  val quantity = int(2)
  val deliveryDate =
    localDate(3, "yyyy/MM/dd")
}

KRecord(::Order, FileReader("orders.txt"), "|")
  .forEach {
    val day = it.deliveryDate.dayOfWeek
    val total = it.price * it.quantity.toBigDecimal()
    println(
      "${it.name}: deliver on $day, collect $total")
  }
````

which will print:

```
Mac Air: deliver on THURSDAY, collect 3297
Magic Mouse: deliver on FRIDAY, collect 135.98
```