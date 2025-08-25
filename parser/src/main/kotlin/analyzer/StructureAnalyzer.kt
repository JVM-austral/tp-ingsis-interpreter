package analyzer
import executor.StructureExecutor
import token.Token

interface StructureAnalyzer {
    fun analyzeStructure(tokens: List<Token>): Boolean

    fun getExecutor(): StructureExecutor
}
