import errorhandler.MockErrorHandler
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
}
