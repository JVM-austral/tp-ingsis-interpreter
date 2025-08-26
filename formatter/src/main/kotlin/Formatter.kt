import token.Token

interface Formatter {

    fun format(tokens: List<Result<Token>>): String
}
