// import ast.Ast
// import ast.BooleanBinaryOperation
// import ast.ScapeAst
// import executor.ParseResult
// import executor.StructureExecutor
// import token.Token
// import token.TokenType
//
// class ConditionExecutor : StructureExecutor {
//    private var index = 0
//    private lateinit var tokens: List<Token>
//    private val numberExecutor = BinaryNumberOperatorExecutor() // reutilizamos el aritmético
//
//    override fun execute(tokens: List<Token>): Ast {
//        this.tokens = tokens
//        this.index = 0
//        val result = parseOrExpression()
//        return when {
//            result is ParseResult.Success && index == tokens.size -> result.ast
//            else -> ScapeAst()
//        }
//    }
//
//    // Nivel 1: ||
//    private fun parseOrExpression(): ParseResult {
//        var left = parseAndExpression()
//        if (left is ParseResult.Failure) return ParseResult.Failure
//
//        while (index < tokens.size && isOrOperator(peek())) {
//            val op = next()
//            val right = parseAndExpression()
//            if (right is ParseResult.Failure) return ParseResult.Failure
//            left = ParseResult.Success(
//                BooleanBinaryOperation(op.value, (left as ParseResult.Success).ast, (right as ParseResult.Success).ast, op.line, op.column)
//            )
//        }
//
//        return left
//    }
//
//    // Nivel 2: &&
//    private fun parseAndExpression(): ParseResult {
//        var left = parseComparison()
//        if (left is ParseResult.Failure) return ParseResult.Failure
//
//        while (index < tokens.size && isAndOperator(peek())) {
//            val op = next()
//            val right = parseComparison()
//            if (right is ParseResult.Failure) return ParseResult.Failure
//            left = ParseResult.Success(
//                BooleanBinaryOperation(op.value, (left as ParseResult.Success).ast, (right as ParseResult.Success).ast, op.line, op.column)
//            )
//        }
//
//        return left
//    }
//
//    // Nivel 3: comparadores (entre expresiones numéricas o booleanas)
//    private fun parseComparison(): ParseResult {
//        val left = parseArithmeticOrPrimary()
//        if (left is ParseResult.Failure) return ParseResult.Failure
//
//        if (index < tokens.size && isComparisonOperator(peek())) {
//            val op = next()
//            val right = parseArithmeticOrPrimary()
//            if (right is ParseResult.Failure) return ParseResult.Failure
//            return ParseResult.Success(
//                BooleanBinaryOperation(op.value, (left as ParseResult.Success).ast, (right as ParseResult.Success).ast, op.line, op.column)
//            )
//        }
//
//        return left
//    }
//
//    // Permite literales, booleanos, variables, paréntesis y expresiones numéricas
//    private fun parseArithmeticOrPrimary(): ParseResult {
//        if (index >= tokens.size) return ParseResult.Failure
//
//        val token = peek()
//        return when (token.type) {
//            TokenType.BOOLEAN_LITERAL -> {
//                val t = next()
//                ParseResult.Success(ast.BooleanLiteral(t.value, t.line, t.column))
//            }
//            TokenType.PUNCTUATION -> {
//                if (token.value == "(") {
//                    next() // consume "("
//                    val expr = parseOrExpression()
//                    if (expr is ParseResult.Failure) return ParseResult.Failure
//                    if (index >= tokens.size || next().value != ")") return ParseResult.Failure
//                    expr
//                } else {
//                    ParseResult.Failure
//                }
//            }
//            else -> {
//                // si no es booleano ni paréntesis -> intentamos parsear como expresión numérica
//                val start = index
//                // buscamos hasta un operador lógico o cierre de paréntesis
//                while (index < tokens.size && !isStopToken(peek())) {
//                    index++
//                }
//                val subTokens = tokens.subList(start, index)
//                val expr = numberExecutor.execute(subTokens)
//                if (expr is ast.ScapeAst) {
//                    ParseResult.Failure
//                } else {
//                    ParseResult.Success(expr)
//                }
//            }
//        }
//    }
//
//    // Helpers
//    private fun peek() = tokens[index]
//
//    private fun next() = tokens[index++]
//
//    private fun isOrOperator(token: Token) =
//        token.type == TokenType.OPERATOR && token.value == "||"
//
//    private fun isAndOperator(token: Token) =
//        token.type == TokenType.OPERATOR && token.value == "&&"
//
//    private fun isComparisonOperator(token: Token) =
//        token.type == TokenType.OPERATOR &&
//            (token.value == "==" || token.value == "!=" ||
//                token.value == "<" || token.value == "<=" ||
//                token.value == ">" || token.value == ">=")
//
//    private fun isStopToken(token: Token): Boolean =
//        token.type == TokenType.OPERATOR && (
//            isComparisonOperator(token) || isAndOperator(token) || isOrOperator(token)
//            ) || (token.type == TokenType.PUNCTUATION && token.value == ")")
// }
