package factory

import Cli
import Formatter
import Linter
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser

class CliFactory : Factory<Cli> {
    override fun create(): Cli {
        return createCli()
    }
    private fun createLexer(): Lexer {
        return LexerFactory().create()
    }
    private fun createParser(): Parser {
        return ParserFactory().create()
    }
    private fun createInterpreter(): Interpreter {
        return InterpreterFactory().create()
    }
    private fun createLinter(): Linter {
        TODO()
        // Implement linter creation logic here
    }
    private fun createFormatter(): Formatter {
        TODO()
        // Implement formatter creation logic here
    }
    private fun createCli(): Cli {
        return Cli(
            createLexer(),
            createParser(),
            createInterpreter(),
            createLinter(),
            createFormatter(),
        )
    }
}
