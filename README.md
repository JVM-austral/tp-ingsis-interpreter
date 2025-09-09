# PrintScript CLI

A command-line interface for the PrintScript programming language that provides lexical analysis, parsing, interpretation, formatting, and linting capabilities.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
  - [Execution](#execution)
  - [Formatting](#formatting)
  - [Linting](#linting)
  - [Validation](#validation)
- [Supported Versions](#supported-versions)
- [Configuration](#configuration)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## Overview

PrintScript CLI is a comprehensive toolchain for working with PrintScript source code. It provides essential development tools including code execution, formatting, static analysis (linting), and validation capabilities.

## Features

- **Code Execution**: Run PrintScript source files with full interpreter support
- **Code Formatting**: Automatically format your PrintScript code according to configurable style rules
- **Static Analysis**: Lint your code to catch potential issues and enforce coding standards
- **Validation**: Combined formatting and linting in a single command
- **Multi-Version Support**: Support for different versions of the PrintScript language specification
- **Configurable**: Customizable formatter and linter settings via configuration files

## Installation

### Prerequisites

- Java 8 or higher
- Kotlin runtime environment

### Build from Source

```bash
git clone https://github.com/your-org/printscript-cli.git
cd printscript-cli
./gradlew build
```

### Download Binary

Download the latest release from the [releases page](https://github.com/your-org/printscript-cli/releases).

## Usage

The CLI provides four main commands: `execution`, `format`, `analyzing`, and `validation`.

### General Syntax

```bash
printscript-cli <command> [options]
```

### Execution

Run a PrintScript source file:

```bash
printscript-cli execution -f <file> -v <version>
```

**Options:**
- `-f, --file`: Path to the PrintScript file to execute (required)
- `-v, --version`: PrintScript version to use (`V1` or `V2`) (required)

**Example:**
```bash
printscript-cli execution -f hello.ps -v V1
```

### Formatting

Format a PrintScript source file:

```bash
printscript-cli format -f <file> -v <version> [-cf <config-file>]
```

**Options:**
- `-f, --file`: Path to the file to format (required)
- `-v, --version`: PrintScript version to use (`V1` or `V2`) (required)
- `-cf, --configFormatter`: Path to formatter configuration file (optional)

**Example:**
```bash
printscript-cli format -f mycode.ps -v V1 -cf formatter-config.json
```

### Linting

Perform static code analysis:

```bash
printscript-cli analyzing -f <file> -v <version> [-cl <config-file>]
```

**Options:**
- `-f, --file`: Path to the file to analyze (required)
- `-v, --version`: PrintScript version to use (`V1` or `V2`) (required)
- `-cl, --configLinter`: Path to linter configuration file (optional)

**Example:**
```bash
printscript-cli analyzing -f mycode.ps -v V1 -cl linter-config.json
```

### Validation

Run both formatting and linting in sequence:

```bash
printscript-cli validation -f <file> -v <version> [-cl <linter-config>] [-cf <formatter-config>]
```

**Options:**
- `-f, --file`: Path to the file to validate (required)
- `-v, --version`: PrintScript version to use (`V1` or `V2`) (required)
- `-cl, --configLinter`: Path to linter configuration file (optional)
- `-cf, --configFormatter`: Path to formatter configuration file (optional)

**Example:**
```bash
printscript-cli validation -f mycode.ps -v V1 -cl linter.json -cf formatter.json
```

## Supported Versions

Currently, the CLI supports:
- **V1**: Fully implemented with complete lexer, parser, interpreter, formatter, and linter support
- **V2**: Work in progress (marked as TODO in the codebase)

## Configuration

### Formatter Configuration

The formatter can be customized using a JSON configuration file. Pass the configuration file path using the `-cf` option.

### Linter Configuration

The linter rules can be configured using a JSON configuration file. Pass the configuration file path using the `-cl` option.

## Examples

### Basic Workflow

1. **Write your PrintScript code:**
```printscript
// hello.ps
let message: string = "Hello, PrintScript!";
println(message);
```

2. **Format the code:**
```bash
printscript-cli format -f hello.ps -v V1
```

3. **Lint the code:**
```bash
printscript-cli analyzing -f hello.ps -v V1
```

4. **Execute the code:**
```bash
printscript-cli execution -f hello.ps -v V1
```

5. **Or do it all with validation:**
```bash
printscript-cli validation -f hello.ps -v V1
```

### Using Configuration Files

**Formatter config (formatter.json):**
```json
{
  "indentSize": 4,
  "maxLineLength": 100,
  "insertFinalNewline": true
}
```

**Linter config (linter.json):**
```json
{
  "rules": {
    "noUnusedVariables": true,
    "requireSemicolons": true,
    "maxComplexity": 10
  }
}
```

**Using configs:**
```bash
printscript-cli validation -f mycode.ps -v V1 -cl linter.json -cf formatter.json
```

## Error Handling

- **Execution errors**: Runtime errors are collected and displayed after execution
- **Parse errors**: Syntax errors are caught and reported during parsing
- **Lint issues**: Code quality issues are reported with line and column information
- **File errors**: Missing files or read/write errors are handled gracefully

## Architecture

The CLI uses a factory pattern to create appropriate components based on the specified version:

- **Lexer**: Tokenizes PrintScript source code
- **Parser**: Builds Abstract Syntax Trees (AST) from tokens
- **Interpreter**: Executes the AST
- **Formatter**: Formats code according to style rules
- **Linter**: Performs static code analysis

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests for your changes
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

### Development Setup

```bash
git clone https://github.com/your-org/printscript-cli.git
cd printscript-cli
./gradlew build
./gradlew test
```

## License

This project is licensed under the UNIVERSIDAD AUSTRAL License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: Report bugs and request features on [GitHub Issues](https://github.com/your-org/printscript-cli/issues)
- **Documentation**: Additional documentation available in the [docs](docs/) folder
- **Community**: Join our [Discord server](https://discord.gg/printscript) for community support

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed history of changes.

---

**Note**: Version V2 support is currently under development. Please use V1 for production code.
