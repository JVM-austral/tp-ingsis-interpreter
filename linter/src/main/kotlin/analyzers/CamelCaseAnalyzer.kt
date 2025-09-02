package analyzers

import ast.Ast
import ast.VarDeclaration
import java.util.Optional

class CamelCaseAnalyzer : LinterAnalyzer {
    override fun analyze(ast: Ast): Optional<Error> {
        if (ast is VarDeclaration || ast is VarDeclaration) {
            val name = ast.getListOfChildren()[0].getValue()
            val regex = Regex("^[a-z]+([A-Z][a-z]*)*$")
            if (!regex.matches(name)) {
                return Optional.of(Error("Variable name '$name' is not in camelCase format"))
            }
        }
        return Optional.empty()
    }
}
