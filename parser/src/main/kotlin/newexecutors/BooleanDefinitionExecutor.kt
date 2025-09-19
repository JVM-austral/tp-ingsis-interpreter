package newexecutors

import ConditionExecutor
import ast.Ast
import ast.StringLiteral
import ast.VarDefinition
import executor.StructureExecutor
import token.Token

class BooleanDefinitionExecutor : StructureExecutor {
    override fun execute(tokens: List<Token>): Ast =
        VarDefinition(
            tokens[1].value,
            StringLiteral(tokens[0].value, tokens[0].line, tokens[0].column),
            ConditionExecutor().execute(tokens.subList(2, tokens.size - 1)),
            tokens[1].line,
            tokens[1].column,
        )
}
