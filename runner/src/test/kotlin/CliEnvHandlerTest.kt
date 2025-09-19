import handler.CliEnvHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import runner.RunnerImplementation

class CliEnvHandlerTest {

    @Test
    fun testEnvAdapter() {
        val envAdapter = CliEnvHandler()
        val envMap: MutableMap<String, String> = System.getenv()
        println(envAdapter.processEnv(envMap))
    }

    @Test
    fun testEnvAdapterWithRunner() {
        val envAdapter = CliEnvHandler()
        val envMap: MutableMap<String, String> = System.getenv()
        val env = envAdapter.processEnv(envMap)
        val runner = RunnerImplementation("V2", env = env)
        val code = """ 
            const a: string = readEnv("BEST_FOOTBALL_CLUB");
            println(a);
        """.trimIndent()

        val codeStream = code.byteInputStream()
        runner.run(codeStream)
    }

    @Test
    fun testEnvAdapterWithRunnerAndVarNotFound() {
        assertThrows<Exception> {
            val envAdapter = CliEnvHandler()
            val envMap: MutableMap<String, String> = System.getenv()
            val env = envAdapter.processEnv(envMap)
            val runner = RunnerImplementation("V2", env = env)
            val code = """ 
                const a: string = readEnv("NOT_EXISTING_VAR");
                println(a);
            """.trimIndent()

            val codeStream = code.byteInputStream()
            runner.run(codeStream)
        }.apply {
            assertEquals("La variable de entorno 'NOT_EXISTING_VAR' no existe", message)
        }
    }
}
