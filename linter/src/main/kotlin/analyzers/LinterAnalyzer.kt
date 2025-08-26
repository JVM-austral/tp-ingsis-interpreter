package analyzers

import ast.Ast
import executors.LinterExecutor

interface LinterAnalyzer {

    fun analyze(ast: Ast): Result<LinterExecutor>
}