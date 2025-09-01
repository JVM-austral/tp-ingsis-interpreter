package analyzers

import ast.BinaryOperation
import ast.FunctionCallAst
import error.LinterError
import java.util.Optional

class PrintLnWithOutBinaryOperationAnalyzer : LinterAnalyzer {

    override fun analyze(ast: ast.Ast): Optional<LinterError> {
        if (ast is FunctionCallAst) {
            val functionName = ast.getValue()
            if (functionName == "println") {
                if (ast.getListOfChildren().size == 1) {
                    val parameter = ast.getListOfChildren()[0]
                    if (parameter is BinaryOperation) {
                        return Optional.of(LinterError("println should`nt have a binary operation as parameter", 0, 0))
                    }
                }
            }
        }

        return java.util.Optional.empty()
    }
}
