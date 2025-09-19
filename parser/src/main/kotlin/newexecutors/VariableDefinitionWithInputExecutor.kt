package newexecutors

import ast.Ast
import ast.FunctionCallAst
import ast.StringLiteral
import ast.VarDefinition
import executor.StringConcatenationExecutor
import executor.StructureExecutor
import token.Token

class VariableDefinitionWithInputExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast =
        VarDefinition(
            tokens[1].value,
            StringLiteral(tokens[0].value, tokens[0].line, tokens[0].column),
            FunctionCallAst(
                tokens[2].value,
                listOf(
                    StringConcatenationExecutor().execute(tokens.subList(4, tokens.size - 2)),
                ),
                tokens[2].line,
                tokens[2].column,
            ),
            tokens[0].line,
            tokens[0].column,
        )
}
