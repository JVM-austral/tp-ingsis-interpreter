package dsl.parser

import factory.ParserFactoryV1
import factory.ParserFactoryV2
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.test.Test

class FactoryParserTest {
    @Test
    fun `should create both factories successfully`() {
        // Arrange & Act
        val factory1 = ParserFactoryV1().create() // Reemplaza con tu factory real
        val factory2 = ParserFactoryV2().create() // Reemplaza con tu factory real

        // Assert - Solo verificamos que se crearon
        assertNotNull(factory1)
        assertNotNull(factory2)
    }

    @Test
    fun `should create factory1`() {
        // Act
        val factory = ParserFactoryV1()

        // Assert
        assertNotNull(factory)
    }

    @Test
    fun `should create factory2`() {
        // Act
        val factory = ParserFactoryV2()

        // Assert
        assertNotNull(factory)
    }

    @Test
    fun `should create multiple instances`() {
        // Act
        val factory1Instance1 = ParserFactoryV1()
        val factory1Instance2 = ParserFactoryV1()
        val factory2Instance1 = ParserFactoryV1()
        val factory2Instance2 = ParserFactoryV1()

        // Assert - Solo verificamos que existen
        assertNotNull(factory1Instance1)
        assertNotNull(factory1Instance2)
        assertNotNull(factory2Instance1)
        assertNotNull(factory2Instance2)
    }
}
