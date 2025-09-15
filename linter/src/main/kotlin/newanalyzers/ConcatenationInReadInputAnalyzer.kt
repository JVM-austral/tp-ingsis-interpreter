package newanalyzers

import analyzers.LinterAnalyzer
import ast.Ast
import ast.FunctionCallAst
import ast.StringLiteral
import ast.VarDeclaration
import ast.VarDefinition
import ast.VariableIdentifier
import error.LinterError
import java.util.*

class ConcatenationInReadInputAnalyzer : LinterAnalyzer {
    override fun analyze(ast: Ast): Optional<LinterError> {
        if (ast is VarDeclaration || ast is VarDefinition) {
            val readInputAst = lookForReadInput(ast)
            if (readInputAst.isPresent) {
                if (readInputAst.get().getListOfChildren()[0] !is StringLiteral && readInputAst.get().getListOfChildren()[0] !is VariableIdentifier) {
                    return Optional.of(LinterError("readInput should only have a string literal or a variable identifier as parameter", readInputAst.get().getRow(), readInputAst.get().getColumn()))
                }
            }
        }
        return Optional.empty()
    }

    private fun lookForReadInput(ast: Ast): Optional<Ast> {
        if (ast is FunctionCallAst) {
            if (ast.getValue() == "readInput") {
                return Optional.of(ast)
            }
        }
        for (child in ast.getListOfChildren()) {
            if (lookForReadInput(child).isPresent) {
                return Optional.of(lookForReadInput(child).get())
            }
        }
        return Optional.empty()
    }
}
