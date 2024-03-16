package plenix.krecord

import java.io.StringReader
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecordTest {
    @Test
    fun readsRecords() {
        val lines = """
                Mac Air|$1,099|3|2024/03/14
                Magic Mouse|$67.99|2|2024/03/15
            """.trimIndent()

        class Example(fields: List<String>) : KRecord(fields) {
            val name = string(0)
            val price = bigDecimal(1, "$###,####.##")
            val quantity = int(2)
            val deliveryDate = localDate(3, "yyyy/MM/dd")
        }

        KRecord(::Example, StringReader(lines), Regexes.fromString("|"))
            .forEach { order ->
                val weekDay = order.deliveryDate.dayOfWeek
                val total = order.price * order.quantity.toBigDecimal()
                println("${order.name}: deliver on $weekDay, collect $total")
            }

        val result = KRecord(
            new = ::Example,
            reader = StringReader(lines),
            delim = Regexes.fromString("|")
        )
            .toList()

        assertEquals(2, result.size)
        assertTrue(
            result[0].name == "Mac Air" &&
                    result[0].price == BigDecimal("1099") &&
                    result[0].quantity == 3 &&
                    result[0].deliveryDate == LocalDate.parse("2024-03-14")
        )
        assertTrue(
            result[1].name == "Magic Mouse" &&
                    result[1].price == BigDecimal("67.99") &&
                    result[1].quantity == 2 &&
                    result[1].deliveryDate == LocalDate.parse("2024-03-15")
        )
    }
}