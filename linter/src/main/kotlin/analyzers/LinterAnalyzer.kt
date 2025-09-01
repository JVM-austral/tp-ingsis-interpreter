package analyzers

import ast.Ast
import java.util.Optional

interface LinterAnalyzer {

    fun analyze(ast: Ast): Optional<Error>
}
