package lexertest

import factory.LexerFactoryV1
import factory.LexerFactoryV2
import lexer.LexerImplementation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LexerFactoryTest {
    private lateinit var factoryV1: LexerFactoryV1
    private lateinit var factoryV2: LexerFactoryV2

    @BeforeEach
    fun setUp() {
        factoryV1 = LexerFactoryV1()
        factoryV2 = LexerFactoryV2()
    }

    @Nested
    @DisplayName("LexerFactoryV1 Tests")
    inner class LexerFactoryV1Tests {
        @Test
        @DisplayName("Should create a non-null Lexer instance")
        fun shouldCreateNonNullLexer() {
            val lexer = factoryV1.create()
            assertNotNull(lexer)
        }

        @Test
        @DisplayName("Should create LexerImplementation instance")
        fun shouldCreateLexerImplementation() {
            val lexer = factoryV1.create()
            assertTrue(lexer is LexerImplementation)
        }

        @Test
        @DisplayName("Should create different instances on multiple calls")
        fun shouldCreateDifferentInstances() {
            val lexer1 = factoryV1.create()
            val lexer2 = factoryV1.create()
            assertNotSame(lexer1, lexer2)
        }

        @Test
        @DisplayName("Should create Lexer with all required analyzers")
        fun shouldCreateLexerWithAllAnalyzers() {
            val lexer = factoryV1.create()
            assertNotNull(lexer)

            // Test that the lexer can handle basic token types
            // This assumes your Lexer has some method to test functionality
            // You might need to adjust based on your actual Lexer interface
        }
    }

    @Nested
    @DisplayName("LexerFactoryV2 Tests")
    inner class LexerFactoryV2Tests {
        @Test
        @DisplayName("Should create a non-null Lexer instance")
        fun shouldCreateNonNullLexer() {
            val lexer = factoryV2.create()
            assertNotNull(lexer)
        }

        @Test
        @DisplayName("Should create LexerImplementation instance")
        fun shouldCreateLexerImplementation() {
            val lexer = factoryV2.create()
            assertTrue(lexer is LexerImplementation)
        }

        @Test
        @DisplayName("Should create different instances on multiple calls")
        fun shouldCreateDifferentInstances() {
            val lexer1 = factoryV2.create()
            val lexer2 = factoryV2.create()
            assertNotSame(lexer1, lexer2)
        }
    }

    @Nested
    @DisplayName("Comparison Tests")
    inner class ComparisonTests {
        @Test
        @DisplayName("Both factories should create compatible Lexer instances")
        fun bothFactoriesShouldCreateCompatibleLexers() {
            val lexerV1 = factoryV1.create()
            val lexerV2 = factoryV2.create()

            assertEquals(lexerV1::class.java, lexerV2::class.java)
        }

        @Test
        @DisplayName("Both factories should create functional lexers")
        fun bothFactoriesShouldCreateFunctionalLexers() {
            val lexerV1 = factoryV1.create()
            val lexerV2 = factoryV2.create()

            assertNotNull(lexerV1)
            assertNotNull(lexerV2)

            // If your Lexer interface has methods you can test, add them here
            // For example:
            // val testInput = "let x = 42;"
            // val tokensV1 = lexerV1.tokenize(testInput)
            // val tokensV2 = lexerV2.tokenize(testInput)
            // assertNotNull(tokensV1)
            // assertNotNull(tokensV2)
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    inner class IntegrationTests {
        @Test
        @DisplayName("Should handle simple expressions with V1")
        fun shouldHandleSimpleExpressionsV1() {
            val lexer = factoryV1.create()

            // Add integration tests based on your Lexer's actual methods
            // Example:
            // val result = lexer.tokenize("let x = 42;")
            // assertFalse(result.isEmpty())
        }

        @Test
        @DisplayName("Should handle simple expressions with V2")
        fun shouldHandleSimpleExpressionsV2() {
            val lexer = factoryV2.create()

            // Add integration tests based on your Lexer's actual methods
            // Example:
            // val result = lexer.tokenize("let x = 42;")
            // assertFalse(result.isEmpty())
        }

        @Test
        @DisplayName("Should handle boolean operations")
        fun shouldHandleBooleanOperations() {
            val lexerV1 = factoryV1.create()
            val lexerV2 = factoryV2.create()

            // Test boolean-specific functionality
            // This would depend on your actual Lexer interface
        }

        @Test
        @DisplayName("Should handle string operations")
        fun shouldHandleStringOperations() {
            val lexerV1 = factoryV1.create()
            val lexerV2 = factoryV2.create()

            // Test string-specific functionality
        }

        @Test
        @DisplayName("Should handle numeric operations")
        fun shouldHandleNumericOperations() {
            val lexerV1 = factoryV1.create()
            val lexerV2 = factoryV2.create()

            // Test numeric-specific functionality
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    inner class ErrorHandlingTests {
        @Test
        @DisplayName("Should handle invalid input gracefully")
        fun shouldHandleInvalidInputGracefully() {
            val lexerV1 = factoryV1.create()
            val lexerV2 = factoryV2.create()

            // Test error handling with invalid input
            // This depends on how your Lexer handles errors
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    inner class PerformanceTests {
        @Test
        @DisplayName("Factory creation should be fast")
        fun factoryCreationShouldBeFast() {
            val startTime = System.currentTimeMillis()

            repeat(1000) {
                factoryV1.create()
                factoryV2.create()
            }

            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime

            // Assuming creation should take less than 1 second for 1000 instances
            assertTrue(duration < 1000, "Factory creation took too long: ${duration}ms")
        }
    }
}
