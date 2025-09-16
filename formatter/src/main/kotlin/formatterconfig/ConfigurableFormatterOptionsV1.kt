package formatterconfig

data class ConfigurableFormatterOptionsV1(
    val enforceNoSpacingAroundEquals: Boolean = false,
    val enforceSpacingAroundEquals: Boolean = false,
    val enforceSpacingAfterColonInDeclaration: Boolean = false,
    val enforceSpacingBeforeColonInDeclaration: Boolean = false,
    val mandatorySingleSpaceSeparation: Boolean = false,
    val mandatorySpaceSurroundingOperations: Boolean = false,
    val mandatoryLineBreakAfterStatement: Boolean = false,
    val lineBreakAfterPrintLn: Int = 0,
)
