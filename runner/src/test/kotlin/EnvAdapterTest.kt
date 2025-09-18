import envadapter.EnvAdapter
import org.junit.jupiter.api.Test
import runner.RunnerImplementation

class EnvAdapterTest {

    @Test
    fun testEnvAdapter() {
        val envAdapter = EnvAdapter()
        val envMap: MutableMap<String, String> = System.getenv()
        println(envAdapter.processEnv(envMap))
    }

    @Test
    fun testEnvAdapterWithRunner() {
        val envAdapter = EnvAdapter()
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
}
