package analyzer

import ast.Ast
import ast.TypeDeclaration
import executor.InterpreterExecutor
import executor.TypeDeclarationExecutor
import interpreter.VariableInfo

class TypeDeclarationAnalyzer : InterpreterAnalyzer {
    override fun analyzeInterpretation(statement: Result<Ast>, heap: MutableMap<String, VariableInfo>): Boolean {
        val ast = statement.getOrNull() ?: return false
        return ast is TypeDeclaration
    }

    override fun getExecutor(heap: MutableMap<String, VariableInfo>): InterpreterExecutor {
        return TypeDeclarationExecutor()
    }
}
