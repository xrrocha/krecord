package plenix.krecord

interface Splitter {
    fun split(line: String): List<String>
}

class DelimSplitter(private val delim: Regex): Splitter {
    override fun split(line: String): List<String> =
        delim.split(line)
}

class FixedRecord()