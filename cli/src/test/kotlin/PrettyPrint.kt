import ast.Ast

/** Pretty print utilities for AST trees */
fun Ast.pretty(indent: String = "", isLast: Boolean = true): String {
    val branch = if (indent.isEmpty()) "" else if (isLast) "└── " else "├── "
    val header = buildString {
        append(indent)
        append(branch)
        append(this@pretty::class.simpleName)
        val value = getValue()
        if (value.isNotBlank()) {
            append(": ")
            append(value)
        }
        append(" (@")
        append(getRow())
        append(":")
        append(getColumn())
        append(")")
    }
    val children = getListOfChildren()
    if (children.isEmpty()) return header
    val childIndent = indent + if (indent.isEmpty()) "" else if (isLast) "    " else "│   "
    return buildString {
        appendLine(header)
        children.forEachIndexed { idx, child ->
            append(child.pretty(childIndent, idx == children.lastIndex))
            if (idx != children.lastIndex) appendLine()
        }
    }
}

fun List<Ast>.prettyForest(): String =
    this.mapIndexed { index, ast -> ast.pretty(isLast = index == this.lastIndex) }.joinToString("\n")
