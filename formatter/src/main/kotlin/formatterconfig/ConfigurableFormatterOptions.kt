package formatterconfig

data class ConfigurableFormatterOptions(
    val spaceBeforeColon: Boolean = false,
    val spaceAfterColon: Boolean = false,
    val spaceBeforeEquals: Boolean = false,
    val spaceAfterEquals: Boolean = false,
    val amountOfNewLinesBeforePrint: Int = 0,
    val indentationSize: Int = 1,
)
