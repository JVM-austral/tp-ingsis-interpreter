import com.github.ajalt.clikt.core.subcommands
import commands.ExecutionCommand
import commands.FormatCommand
import commands.PrintScriptCLI
import commands.LintCommand
import factory.FormatterFactory
import factory.InterpreterFactory
import factory.LexerFactory
import factory.ParserFactory
import factory.linterfactory.LinterFactory

private val lexer = LexerFactory().create()
private val parser=ParserFactory().create()
private val interpreter=InterpreterFactory().create()
private val linter= LinterFactory().create()
private val formatter= FormatterFactory().create()


fun main(args: Array<String>) = PrintScriptCLI()
    .subcommands(
        LintCommand(linter,parser,lexer),
        FormatCommand(formatter,lexer),
        ExecutionCommand(interpreter, parser, lexer)
    )
    .main(args)
