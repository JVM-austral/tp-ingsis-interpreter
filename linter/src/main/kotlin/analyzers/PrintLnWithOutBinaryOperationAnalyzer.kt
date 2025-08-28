package analyzers

import ast.BinaryOperation
import ast.FunctionCallAst

class PrintLnWithOutBinaryOperationAnalyzer : LinterAnalyzer {

    override fun analyze(ast: ast.Ast): java.util.Optional<Error> {
        if (ast is FunctionCallAst) {
            val functionName = ast.getValue()
            if (functionName == "println") {
                if (ast.getListOfChildren().size == 1) {
                    val parameter = ast.getListOfChildren()[0]
                    if (parameter is BinaryOperation) {
                        return java.util.Optional.of(Error("println should`nt have a binary operation as parameter"))
                    }
                }
            }
        }

        return java.util.Optional.empty()
    }
}
