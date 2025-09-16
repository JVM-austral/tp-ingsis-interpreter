package formatterfactory

import formatter.FormatterImpl
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertTrue

class FormatterFactoryTest {

    @Test
    fun `FormatterFactoryV1 should create FormatterImpl with expected analyzers`() {
        val factory = FormatterFactoryV1()
        val formatter = factory.create()

        assertNotNull(formatter, "Formatter should not be null")
        assertTrue(formatter is FormatterImpl, "FormatterFactoryV1 should return FormatterImpl")
    }

    @Test
    fun `FormatterFactoryWithJsonV1 should use default path when null`() {
        val factory = FormatterFactoryWithJsonV1(null)
        val formatter = factory.create()

        assertNotNull(formatter, "Formatter should not be null")
        val defaultPath = "src/test/resources/formatter-rules-v-1.json"
        assertTrue(File(defaultPath).exists(), "Default config file should exist: $defaultPath")
    }

    @Test
    fun `FormatterFactoryWithJsonV1 should use provided path`() {
        val tempConfigPath = "src/test/resources/formatter-rules-v-1.json"
        File(tempConfigPath).writeText("{}") // dummy JSON

        val factory = FormatterFactoryWithJsonV1(tempConfigPath)
        val formatter = factory.create()

        assertNotNull(formatter, "Formatter should not be null")
    }

    @Test
    fun `FormatterFactoryWithJsonV2 should use default path when null`() {
        val factory = FormatterFactoryWithJsonV2(null)
        val formatter = factory.create()

        assertNotNull(formatter, "Formatter should not be null")
        val defaultPath = "src/test/resources/formatter-rules-v-1.json"
        assertTrue(File(defaultPath).exists(), "Default config file should exist: $defaultPath")
    }

    @Test
    fun `FormatterFactoryWithJsonV2 should use provided path`() {
        val tempConfigPath = "src/test/resources/formatter-rules-v-2.json"
        File(tempConfigPath).writeText("{}") // dummy JSON

        val factory = FormatterFactoryWithJsonV2(tempConfigPath)
        val formatter = factory.create()

        assertNotNull(formatter, "Formatter should not be null")
    }
}
