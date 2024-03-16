package plenix.krecord

import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass


typealias Parser<T> = (String) -> T

object Parsers {

    // TODO Have parse function accept/return null values
    fun <T : Any> parse(kClass: KClass<T>, mask: String?, field: String): T =
        parserFor(kClass, mask)(field)

    // TODO Add custom parser w/factory

    fun parseBigDecimal(value: String, mask: String? = null) =
        parserFor(BigDecimal::class, mask)(value)

    fun parseByte(value: String, mask: String? = null) =
        parserFor(Byte::class, mask)(value)

    fun parseChar(value: String, mask: String? = null) =
        parserFor(Char::class, mask)(value)

    fun parseDouble(value: String, mask: String? = null) =
        parserFor(Double::class, mask)(value)

    fun parseFloat(value: String, mask: String? = null) =
        parserFor(Float::class, mask)(value)

    fun parseInt(value: String, mask: String? = null) =
        parserFor(Int::class, mask)(value)

    fun parseLong(value: String, mask: String? = null) =
        parserFor(Long::class, mask)(value)

    fun parseShort(value: String, mask: String? = null) =
        parserFor(Short::class, mask)(value)

    fun parseString(value: String, mask: String? = null) =
        parserFor(String::class, mask)(value)

    fun parseLocalDate(value: String, mask: String? = null) =
        parserFor(LocalDate::class, mask)(value)

    fun parseLocalDateTime(value: String, mask: String? = null) =
        parserFor(LocalDateTime::class, mask)(value)

    fun parseLocalTime(value: String, mask: String? = null) =
        parserFor(LocalTime::class, mask)(value)

    private val parsers = mutableMapOf<KClass<*>, MutableMap<String?, (String) -> Any?>>(
        baseParser(BigDecimal::class, String::toBigDecimal),
        baseParser(Byte::class, String::toByte),
        baseParser(Char::class) { it.toCharArray()[0] },
        baseParser(Double::class, String::toDouble),
        baseParser(Float::class, String::toFloat),
        baseParser(Int::class, String::toInt),
        baseParser(Long::class, String::toLong),
        baseParser(Short::class, String::toShort),
        baseParser(String::class, String::toString),
        baseParser(LocalDate::class) {
            LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
        },
        baseParser(LocalDateTime::class) {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        },
        baseParser(LocalTime::class) {
            LocalTime.parse(it, DateTimeFormatter.ISO_LOCAL_TIME)
        },
    )

    private val parserFactories =
        mutableMapOf<KClass<*>, (String?) -> ((String) -> Any?)>(
            BigDecimal::class to { mask ->
                DecimalFormat(mask)
                    .let { format ->
                        format.isParseBigDecimal = true
                        { value -> format.parse(value) as BigDecimal }
                    }
            },
            Double::class to { mask ->
                DecimalFormat(mask)
                    .let { format ->
                        { value -> format.parse(value).toDouble() }
                    }
            },
            Float::class to { mask ->
                DecimalFormat(mask)
                    .let { format ->
                        { value -> format.parse(value).toFloat() }
                    }
            },
            Int::class to { mask ->
                DecimalFormat(mask)
                    .let { format ->
                        { value -> format.parse(value).toInt() }
                    }
            },
            Long::class to { mask ->
                DecimalFormat(mask)
                    .let { format ->
                        { value -> format.parse(value).toLong() }
                    }
            },
            Short::class to { mask ->
                DecimalFormat(mask)
                    .let { format ->
                        { value -> format.parse(value).toShort() }
                    }
            },
            LocalDate::class to { mask ->
                mask
                    .let {
                        it
                            ?.let { DateTimeFormatter.ofPattern(it) }
                            ?: DateTimeFormatter.ISO_LOCAL_DATE
                    }
                    .let { formatter ->
                        { value -> LocalDate.parse(value, formatter) }
                    }
            },
            LocalDateTime::class to { mask ->
                mask
                    .let {
                        it
                            ?.let { DateTimeFormatter.ofPattern(it) }
                            ?: DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    }
                    .let { formatter ->
                        { value -> LocalDateTime.parse(value, formatter) }
                    }
            },
            LocalTime::class to { mask ->
                mask
                    .let {
                        it
                            ?.let { DateTimeFormatter.ofPattern(it) }
                            ?: DateTimeFormatter.ISO_LOCAL_TIME
                    }
                    .let { formatter ->
                        { value -> LocalTime.parse(value, formatter) }
                    }
            },
        )

    private fun <T : Any> baseParser(kClass: KClass<T>, parser: Parser<T>):
            Pair<KClass<*>, MutableMap<String?, (String) -> Any?>> =
        kClass to mutableMapOf(null to parser)

    @Suppress("unchecked_cast")
    private fun <T : Any> parserFor(kClass: KClass<T>, mask: String?): Parser<T> =
        parsers
            .computeIfAbsent(kClass) {
                throw NoSuchElementException("No parsers for '${kClass.qualifiedName}'")
            }
            .computeIfAbsent(mask) {
                parserFactories[kClass]
                    ?.let { factory ->
                        factory(mask)
                    }
                    ?: throw NoSuchElementException("No parser factory for '${kClass.qualifiedName}'")
            }
                as Parser<T>
}