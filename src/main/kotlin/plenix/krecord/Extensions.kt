package plenix.krecord

import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringReader

operator fun <T : KRecord> KRecord.Companion.invoke(
    new: (List<String>) -> T,
    lines: String,
    delim: String
): Sequence<T> =
    invoke(new, StringReader(lines), Regexes.fromString(delim))

operator fun <T : KRecord> KRecord.Companion.invoke(
    new: (List<String>) -> T,
    inputStream: InputStream,
    delim: String
): Sequence<T> =
    invoke(new, InputStreamReader(inputStream), Regexes.fromString(delim))

operator fun <T : KRecord> KRecord.Companion.invoke(
    new: (List<String>) -> T,
    reader: Reader,
    delim: String
): Sequence<T> =
    invoke(new, reader, Regexes.fromString(delim))
