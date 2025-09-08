import analyzers.CanNotStartLineWithSpaceAnalyzer
import analyzers.NewLineAfterSemiColonAnalyzer
import analyzers.OnlyOneSpaceAnalyzer
import analyzers.SpaceAfterColonAnalyzer
import analyzers.SpaceAfterEqualsAnalyzer
import analyzers.SpaceAfterOperatorAnalyzer
import analyzers.SpaceBeforeColonAnalyzer
import analyzers.SpaceBeforeEqualsAnalyzer
import analyzers.SpaceBeforeOperatorAnalyzer
import formatterconfig.ConfigurableAnalyzerFormatter
import newanalyzers.IndentationAnalyzer
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FormatterConfigWithJsonTest {

    @Test
    fun `test buildAnalyzers with custom config file`() {
        val formatter = ConfigurableAnalyzerFormatter("src/test/resources/exampleconfig.json")
        val analyzers = formatter.buildFormatter().getAnalyzers()

        assertTrue(analyzers.any { it is SpaceBeforeColonAnalyzer })
        assertFalse(analyzers.any { it is SpaceAfterColonAnalyzer })
        assertTrue(analyzers.any { it is SpaceBeforeEqualsAnalyzer })
        assertFalse(analyzers.any { it is SpaceAfterEqualsAnalyzer })

        assertTrue(analyzers.any { it is NewLineAfterSemiColonAnalyzer })
        assertTrue(analyzers.any { it is SpaceAfterOperatorAnalyzer })
        assertTrue(analyzers.any { it is SpaceBeforeOperatorAnalyzer })
        assertTrue(analyzers.any { it is OnlyOneSpaceAnalyzer })
        assertTrue(analyzers.any { it is CanNotStartLineWithSpaceAnalyzer })
        assertTrue(analyzers.any { it is NewLineAfterSemiColonAnalyzer })
        assertTrue(analyzers.any { it is IndentationAnalyzer })
    }
}
