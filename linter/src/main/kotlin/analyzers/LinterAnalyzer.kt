package analyzers

import ast.Ast
import error.LinterError
import java.util.Optional

interface LinterAnalyzer {
    fun analyze(ast: Ast): Optional<LinterError>
}
