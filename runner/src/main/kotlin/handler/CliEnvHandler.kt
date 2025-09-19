package handler

import ast.Ast
import ast.BooleanLiteral
import ast.NumberLiteral
import ast.StringLiteral

class CliEnvHandler {
    private val numberRegex = Regex("^-?\\d+(?:\\.\\d+)?$")

    private fun isNumeric(value: String): Boolean = numberRegex.matches(value.trim())

    private fun isBoolean(value: String): Boolean {
        val v = value.trim().lowercase()
        return v == "true" || v == "false"
    }

    private fun quoteIfNeeded(raw: String): String {
        val value = raw.trim()
        if (isNumeric(value) || isBoolean(value)) return value

        if ((value.startsWith('"') && value.endsWith('"')) || (value.startsWith('\'') && value.endsWith('\''))) return value
        val escaped =
            value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
        return "\"$escaped\""
    }

    fun processEnv(env: MutableMap<String, String>): MutableMap<String, Ast> {
        val resultMap = mutableMapOf<String, Ast>()
        for ((key, value) in env) {
            val normalizedValue = quoteIfNeeded(value)
            val envVarAsString: Ast =
                when {
                    isNumeric(normalizedValue) -> NumberLiteral(normalizedValue, 0, 0)
                    isBoolean(normalizedValue) -> BooleanLiteral(normalizedValue, 0, 0)
                    else -> StringLiteral(normalizedValue, 0, 0)
                }
            resultMap[key] = envVarAsString
        }
        return resultMap
    }
}
