package newexecutors

import ast.Ast
import ast.FunctionCallAst
import ast.StringLiteral
import ast.TypeDeclaration
import ast.VarDefinition
import executor.StructureExecutor
import token.Token

class VariableDefinitionWithEnvExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast {
        return VarDefinition(
            tokens[1].value,
            StringLiteral(tokens[0].value, tokens[0].line, tokens[0].column),
            FunctionCallAst(
                tokens[2].value,
                listOf(
                    TypeDeclaration(tokens[4].value, tokens[4].line, tokens[4].column),
                ),
                tokens[2].line,
                tokens[2].column,
            ),
            tokens[0].line,
            tokens[0].column,
        )
    }
}
