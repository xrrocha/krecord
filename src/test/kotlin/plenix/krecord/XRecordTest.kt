package plenix.krecord

import java.io.StringReader
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class XRecordTest {
    @Test
    fun readsRecords() {
        val lines = """
                Mac Air|$1,099|3|2024/03/14|Y
                Magic Mouse|$67.99|2|2024/03/15|N
            """.trimIndent()

        class Example(fields: List<String>) : KRecord(fields) {
            val name = string(0)
            val price = bigDecimal(1, "$###,####.##")
            val quantity = int(2)
            val deliveryDate = localDate(3, "yyyy/MM/dd")
            val discounted = boolean(4, "Y")
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
                    result[0].deliveryDate == LocalDate.parse("2024-03-14") &&
                    result[0].discounted
        )
        assertTrue(
            result[1].name == "Magic Mouse" &&
                    result[1].price == BigDecimal("67.99") &&
                    result[1].quantity == 2 &&
                    result[1].deliveryDate == LocalDate.parse("2024-03-15") &&
                    !result[1].discounted
        )

        val text =
            KRecord(::Example, StringReader(lines), Regexes.fromString("|"))
                .map {
                    val weekDay = it.deliveryDate.dayOfWeek
                    val total = it.price * it.quantity.toBigDecimal()
                    "${it.name} (${it.quantity}): deliver on $weekDay, collect $total"
                }
                .joinToString("\n")
        assertEquals(
            """
                Mac Air (3): deliver on THURSDAY, collect 3297
                Magic Mouse (2): deliver on FRIDAY, collect 135.98
            """.trimIndent(),
            text
        )
    }
}