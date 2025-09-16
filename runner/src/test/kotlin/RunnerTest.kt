import errorhandler.MockErrorHandler
import mock.MockOutputHandler
import mock.StdOutputHandler
import runner.RunnerImplementation
import kotlin.test.Test

class RunnerTest {

    @Test
    fun `dummy test`() {
        val output = StdOutputHandler()

        val input = """
            let a : string = "hello";
            let b : string = "world";
            println(a + " " + b);
        """.trimIndent()
        val runner = RunnerImplementation("V2", output)
        runner.run(input.byteInputStream())
    }

    @Test
    fun `dummy test v1`() {
        val output = StdOutputHandler()
        val errorHandler = MockErrorHandler()

        val input = """
            const booleanResult: boolean = false;
            if(booleanResult) {
                println("else statement not working correctly");
            } else {
                println("else statement working correctly");
            }
            println("outside of conditional");
        """.trimIndent()

        val runner = RunnerImplementation("V2", output)
        runner.run(input.byteInputStream())
    }

    @Test
    fun `dummy test v2`() {
        val output = StdOutputHandler()
        val errorHandler = MockErrorHandler()

        val input = """
            let PI : number;
            PI = 3.14;
            println( PI/2 );
        """.trimIndent()

        val runner = RunnerImplementation("V2", output)
        runner.run(input.byteInputStream())
    }

    @Test
    fun `dummy test v3`() {
        val output = MockOutputHandler()
        val errorHandler = MockErrorHandler()

        val input = """
            let numberResult: number = 5 * 5 - 8;
println(numberResult);
        """.trimIndent()

        val runner = RunnerImplementation("V1", output)
        runner.run(input.byteInputStream())
        assert(output.captured.contains("17.0"))
    }

    @Test
    fun `dummy test v4`() {
        val output = MockOutputHandler()
        val errorHandler = MockErrorHandler()

        val input = """
            const booleanValue: boolean = false;
            if(booleanValue) {
            println("if statement is not working correctly");
            }
            println("outside of conditional");

        """.trimIndent()
        val runner = RunnerImplementation("V2", output)
        runner.run(input.byteInputStream())
        assert(output.captured.contains("outside of conditional"))
    }

    @Test
    fun `dummy test v5`() {
        val output = MockOutputHandler()
        val errorHandler = MockErrorHandler()

        val input = """
            const booleanValue: boolean = true;
            if(booleanValue) {
            println("if statement is working correctly");
            }
            println("outside of conditional");

        """.trimIndent()
        val runner = RunnerImplementation("V2", output)
        runner.run(input.byteInputStream())
        assert(output.captured.contains("outside of conditional") && output.captured.contains("if statement is working correctly"))
    }

    @Test
    fun `dummy test v6`() {
        val output = MockOutputHandler()

        val input = """
            const a: string = "constant declaration should not be allowed in version 1.0";
        """.trimIndent()
        val runner = RunnerImplementation("V1", output)
        runner.run(input.byteInputStream())
        val erroHandler = runner.getErrorHandler()
        println(erroHandler.getCapturedErrors())
        assert(erroHandler.getCapturedErrors().isNotEmpty())
    }

}
