package dsl

import ast.Ast
import ast.IfDeclaration
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object AstJsonDsl {
    private val gson =
        GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()

    fun List<Result<Ast>>.toJson(): String {
        val jsonArray = JsonArray()

        forEach { result ->
            when {
                result.isSuccess -> result.getOrNull()?.let { jsonArray.add(it.toJsonObject()) }
                else -> jsonArray.add(createError(result.exceptionOrNull()?.message ?: "Error"))
            }
        }

        return gson.toJson(jsonArray)
    }

    private fun Ast.toJsonObject(): JsonObject {
        val json = JsonObject()
        json.addProperty("type", this::class.simpleName)
        json.addProperty("value", getValue())

        val children = getListOfChildren()
        if (children.isNotEmpty()) {
            val childrenArray = JsonArray()
            children.forEach { child -> childrenArray.add(child.toJsonObject()) }
            json.add("children", childrenArray)
        }

        if (this is IfDeclaration) {
            val successArray = JsonArray()
            getOnSuccess().forEach { result ->
                when {
                    result.isSuccess -> result.getOrNull()?.let { successArray.add(it.toJsonObject()) }
                    else -> successArray.add(createError(result.exceptionOrNull()?.message ?: "Error"))
                }
            }
            json.add("onSuccess", successArray)

            val failureArray = JsonArray()
            getOnFailure().forEach { result ->
                when {
                    result.isSuccess -> result.getOrNull()?.let { failureArray.add(it.toJsonObject()) }
                    else -> failureArray.add(createError(result.exceptionOrNull()?.message ?: "Error"))
                }
            }
            json.add("onFailure", failureArray)
        }

        return json
    }

    private fun createError(message: String): JsonObject {
        val error = JsonObject()
        error.addProperty("type", "ERROR")
        error.addProperty("message", message)
        return error
    }
}
