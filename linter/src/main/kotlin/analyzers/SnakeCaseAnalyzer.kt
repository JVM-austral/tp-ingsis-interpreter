package analyzers

import ast.Ast
import ast.VarDeclaration
import error.LinterError
import java.util.Optional

class SnakeCaseAnalyzer : LinterAnalyzer {

    override fun analyze(ast: Ast): Optional<LinterError> {
        if (ast is VarDeclaration) {
            val name = ast.getListOfChildren()[0].getValue()
            val regex = Regex("^[a-z]+(_[a-z]+)*$")
            if (!regex.matches(name)) {
                return Optional.of(LinterError("Variable name '$name' is not in snake_case format",0,0))
            }
        }
        return Optional.empty()
    }
}
