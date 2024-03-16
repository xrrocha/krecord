package plenix.krecord

object Regexes {
    private val MetaChars = setOf(
        '\\', '^', '|', '.', '$', '?', '*', '+', '(', ')', '[', ']',
    )

    fun fromString(expr: String): Regex =
        expr
            .map {
                if (MetaChars.contains(it)) "\\$it"
                else it.toString()
            }
            .joinToString()
            .toRegex()
}