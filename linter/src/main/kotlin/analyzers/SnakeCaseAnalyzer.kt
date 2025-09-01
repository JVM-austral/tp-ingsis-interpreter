package analyzers

import ast.Assigment
import ast.Ast
import ast.VarDeclaration
import java.util.Optional

class SnakeCaseAnalyzer : LinterAnalyzer {

    override fun analyze(ast: Ast): Optional<Error> {
        if (ast is VarDeclaration || ast is Assigment) {
            val name = ast.getListOfChildren()[0].getValue()
            val regex = Regex("^[a-z]+(_[a-z]+)*$")
            if (!regex.matches(name)) {
                return Optional.of(Error("Variable name '$name' is not in snake_case format"))
            }
        }
        return Optional.empty()
    }
}
