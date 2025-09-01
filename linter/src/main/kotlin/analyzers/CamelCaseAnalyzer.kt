package analyzers

import ast.Ast
import ast.VarDeclaration
import error.LinterError
import java.util.Optional

class CamelCaseAnalyzer : LinterAnalyzer {
    override fun analyze(ast: Ast): Optional<LinterError> {
        if (ast is VarDeclaration) {
            val name = ast.getListOfChildren()[0].getValue()
            val regex = Regex("^[a-z]+([A-Z][a-z]*)*$")
            if (!regex.matches(name)) {
                return Optional.of(LinterError("Variable name '$name' is not in camelCase format", 0, 0))
            }
        }
        return Optional.empty()
    }
}
