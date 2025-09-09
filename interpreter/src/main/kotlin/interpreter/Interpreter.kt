package interpreter

import ast.Ast

interface Interpreter {
    fun interpret(parsedStatement: List<Result<Ast>>): List<ExecutionUnit>
    fun runAll(): List<ExecutionUnit>
}
