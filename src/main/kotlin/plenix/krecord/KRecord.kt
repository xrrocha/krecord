package plenix.krecord

import java.io.BufferedReader
import java.io.Reader

abstract class KRecord(private val fields: List<String>) {
    // TODO Make Parsers contextual
    // TODO Add multivalued fields
    // TODO Add structured fields
    // TODO Add fixed-length records
    companion object {
        operator fun <T : KRecord> invoke(
            new: (List<String>) -> T,
            reader: Reader,
            delim: Regex
        ): Sequence<T> {
            val br = BufferedReader(reader)
            return generateSequence { br.readLine() }
                .map { line -> new(line.split(delim)) }
        }
    }

    protected fun bigDecimal(fieldIndex: Int, mask: String? = null) =
        Parsers.parseBigDecimal(fields[fieldIndex], mask)

    protected fun byte(fieldIndex: Int, mask: String? = null) =
        Parsers.parseByte(fields[fieldIndex], mask)

    protected fun char(fieldIndex: Int, mask: String? = null) =
        Parsers.parseChar(fields[fieldIndex], mask)

    protected fun double(fieldIndex: Int, mask: String? = null) =
        Parsers.parseDouble(fields[fieldIndex], mask)

    protected fun float(fieldIndex: Int, mask: String? = null) =
        Parsers.parseFloat(fields[fieldIndex], mask)

    protected fun int(fieldIndex: Int, mask: String? = null) =
        Parsers.parseInt(fields[fieldIndex], mask)

    protected fun long(fieldIndex: Int, mask: String? = null) =
        Parsers.parseLong(fields[fieldIndex], mask)

    protected fun short(fieldIndex: Int, mask: String? = null) =
        Parsers.parseShort(fields[fieldIndex], mask)

    protected fun string(fieldIndex: Int, mask: String? = null) =
        Parsers.parseString(fields[fieldIndex], mask)

    protected fun localDate(fieldIndex: Int, mask: String? = null) =
        Parsers.parseLocalDate(fields[fieldIndex], mask)

    protected fun localDateTime(fieldIndex: Int, mask: String? = null) =
        Parsers.parseLocalDateTime(fields[fieldIndex], mask)

    protected fun localTime(fieldIndex: Int, mask: String? = null) =
        Parsers.parseLocalTime(fields[fieldIndex], mask)
}
