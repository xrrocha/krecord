# KRecords: Easy File Loading in Kotlin

`KRecords` is a lightweight Kotlin library with a humble, yet useful, mission:
making file record loading easy to read and easy to write.

Thus, given the following file (`orders.txt`):

````
Mac Air|$1,099|3|2024/03/14|Y
Magic Mouse|$67.99|2|2024/03/15|N
````

we can process its contents like so:

````kotlin
class Order(fields: List<String>) : KRecord(fields) {
  val name = string(0)
  val price = bigDecimal(1, "$###,####.##")
  val quantity = int(2)
  val deliveryDate = localDate(3, "yyyy/MM/dd")
  val discounted = boolean(4, "Y")
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

## Supported Data Types

`KRecord` supports an extensible set of commonly used data types:

- `Byte`, `Char`, `String` and (base64) `ByteArray`
- `Int`, `Long`, `Short`, `Float`, `Double` and `BigDecimal`
- `LocalDate`, `LocalTime` and `LocalDateTime`

All data types (whether built-in or user-provided) support _masks_ to specify
parsing patterns such as numeric punctuation and date symbols. Custom syntax
can be easily implemented for additional, domain-specific data types.

| 👉In the works: nestable structs and arrays of all types

## Supported File Formats

`KRecord` currently supports delimited files with CSV, Excel and 
fixed-length formats in the works.

