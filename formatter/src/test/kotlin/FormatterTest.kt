import analyzers.CanNotStartLineWithSpaceAnalyzer
import analyzers.NewLineAfterSemiColonAnalyzer
import analyzers.NewLinesBeforePrintlnAnalyzer
import analyzers.OnlyOneSpaceAnalyzer
import analyzers.SpaceAfterColonAnalyzer
import analyzers.SpaceAfterEqualsAnalyzer
import analyzers.SpaceAfterOperatorAnalyzer
import analyzers.SpaceBeforeColonAnalyzer
import analyzers.SpaceBeforeEqualsAnalyzer
import analyzers.SpaceBeforeOperatorAnalyzer
import formatter.FormatterImpl
import lexer.LexerImplementation
import lexer.rules.EnterAnalyzer
import lexer.rules.KeywordAnalyzer
import lexer.rules.MidNumberAnalyzer
import lexer.rules.MidStringAnalyzer
import lexer.rules.NumberAnalyzer
import lexer.rules.NumberTypeAnalyzer
import lexer.rules.OperatorAnalyzer
import lexer.rules.PunctuationAnalyzer
import lexer.rules.StringAnalyzer
import lexer.rules.StringTypeAnalyzer
import lexer.rules.TokenAnalyzer
import lexer.rules.VariableAnalyzer
import lexer.rules.WhitespaceAnalyzer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FormatterTest {

    private lateinit var formatter: formatter.Formatter
    private lateinit var lexer: lexer.Lexer

    @BeforeEach
    fun setup() {
        formatter = FormatterImpl(
            listOf(
                CanNotStartLineWithSpaceAnalyzer(), NewLinesBeforePrintlnAnalyzer(1), SpaceAfterColonAnalyzer(),
                SpaceAfterEqualsAnalyzer(), SpaceAfterOperatorAnalyzer(),
                SpaceBeforeEqualsAnalyzer(), SpaceBeforeOperatorAnalyzer(),
                SpaceBeforeColonAnalyzer(), NewLineAfterSemiColonAnalyzer(), OnlyOneSpaceAnalyzer(),

            ),

        )
        lexer = LexerImplementation(
            listOf<TokenAnalyzer>(
                KeywordAnalyzer(), NumberAnalyzer(), NumberTypeAnalyzer(),
                OperatorAnalyzer(), PunctuationAnalyzer(), StringAnalyzer(), StringTypeAnalyzer(),
                VariableAnalyzer(), WhitespaceAnalyzer(), MidStringAnalyzer(), MidNumberAnalyzer(), EnterAnalyzer(),
            ),
        )
    }

    @Test
    fun `string format`() {
        val tokens = lexer.tokenize("let   hola :number = 3+ 8;")
        val result = formatter.format(tokens)

        assertEquals(result, "let hola : number = 3 + 8;")
    }

    @Test
    fun `debe permitir espacio antes de los dos puntos`() {
        val tokens = lexer.tokenize("let hola: number = 5;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5;")
    }

    @Test
    fun `debe permitir sin espacio antes de los dos puntos`() {
        val tokens = lexer.tokenize("let hola :number = 5;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5;")
    }

    @Test
    fun `debe permitir espacio después de los dos puntos`() {
        val tokens = lexer.tokenize("let hola: number = 5;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5;")
    }

    @Test
    fun `debe permitir sin espacio después de los dos puntos`() {
        val tokens = lexer.tokenize("let hola:number = 5;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5;")
    }

    @Test
    fun `debe permitir espacio antes y después del igual`() {
        val tokens = lexer.tokenize("let hola : number = 5;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5;")
    }

    @Test
    fun `debe permitir sin espacio antes y después del igual`() {
        val tokens = lexer.tokenize("let hola : number=5;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5;")
    }

    @Test
    fun `debe haber salto de línea después de punto y coma`() {
        val tokens = lexer.tokenize("let hola : number = 5;let chau : string = \"ok\";")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5;\nlet chau : string = \"ok\";")
    }

    @Test
    fun `debe haber un solo espacio entre tokens`() {
        val tokens = lexer.tokenize("let    hola   :   number   =   5   ;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5 ;")
    }

    @Test
    fun `debe haber espacio antes y después de operador`() {
        val tokens = lexer.tokenize("let hola : number = 5+8;")
        val result = formatter.format(tokens)
        assertEquals(result, "let hola : number = 5 + 8;")
    }

    @Test
    fun `debe formatear expresiones complejas`() {
        val tokens = lexer.tokenize("let   x:number=3*2+4/2-1;")
        val result = formatter.format(tokens)
        assertEquals(result, "let x : number = 3 * 2 + 4 / 2 - 1;")
    }

    @Test
    fun `debe formatear múltiples declaraciones con diferentes tipos`() {
        val tokens = lexer.tokenize("let nombre:string=\"Juan\";let edad:number=25;let activo:boolean=true;let salario:number=1500.50;")
        val result = formatter.format(tokens)
        assertEquals(result, "let nombre : string = \"Juan\";\nlet edad : number = 25;\nlet activo : boolean = true;\nlet salario : number = 1500.50;")
    }

    @Test
    fun `debe formatear expresiones matemáticas complejas con paréntesis`() {
        val tokens = lexer.tokenize("let resultado:number=(5+3)*(8-2)/4+10/3;")
        val result = formatter.format(tokens)
        assertEquals(result, "let resultado : number = (5 + 3) * (8 - 2) / 4 + 10 / 3;")
    }

    @Test
    fun `debe formatear cadenas con espacios internos sin modificarlas`() {
        val tokens = lexer.tokenize("let mensaje:string=\"Hola    mundo   con espacios\";let otro:string=\"   espacios al inicio\";")
        val result = formatter.format(tokens)
        assertEquals(result, "let mensaje : string = \"Hola    mundo   con espacios\";\nlet otro : string = \"   espacios al inicio\";")
    }

    @Test
    fun `debe formatear asignaciones múltiples sin espacios irregulares`() {
        val tokens = lexer.tokenize("let a:number=1;let b:number=a+2;let c:number=b+3;let d:number=c-a;let e:number=d/b;")
        val result = formatter.format(tokens)
        assertEquals(result, "let a : number = 1;\nlet b : number = a + 2;\nlet c : number = b + 3;\nlet d : number = c - a;\nlet e : number = d / b;")
    }

    @Test
    fun `debe formatear código con espaciado muy irregular`() {
        val tokens = lexer.tokenize("let    variable1   :    string    =    \"valor1\"   ;   let   variable2:number=   100   +   200   ;")
        val result = formatter.format(tokens)
        assertEquals(result, "let variable1 : string = \"valor1\" ;\nlet variable2 : number = 100 + 200 ;")
    }

    @Test
    fun `debe formatear expresiones con diferentes tipos de operadores`() {
        val tokens = lexer.tokenize("let suma:number=a+b;let resta:number=c-d;let multiplicacion:number=e+f;let division:number=g/h;let modulo:number=i+j;")
        val result = formatter.format(tokens)
        assertEquals(result, "let suma : number = a + b;\nlet resta : number = c - d;\nlet multiplicacion : number = e + f;\nlet division : number = g / h;\nlet modulo : number = i + j;")
    }

    @Test
    fun `debe manejar espacios extremos y formatear correctamente`() {
        val tokens = lexer.tokenize("   let     a   :   number   =   (   1   +   2   )   +   3   ;\n    let   y:string=   \"test\"   ;   ")
        val result = formatter.format(tokens)
        assertEquals(result, "let a : number = ( 1 + 2 ) + 3 ;\nlet y : string = \"test\" ;\n")
    }

    @Test
    fun `debe manejar enters antes del println correctamente`() {
        val tokens = lexer.tokenize("let a : number=3; \n\n\nprintln(a);")
        val result = formatter.format(tokens)
        assertEquals(result, "let a : number = 3;\nprintln(a);")
    }
}
