package mx.edu.ittepic.automatas;

import static mx.edu.ittepic.automatas.Token.*;
import mx.edu.ittepic.automatas.Token;
import java_cup.runtime.*;
import java.util.ArrayList;
import java.util.Collection;
%%
%cupsym sym
%cup
%unicode



%class Lexer
%line
%column
%char


L = [a-zA-Z_]
D = [0-9]
Z = ["\""]
S = " "


/*Comentarios*/
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
OurComment = "<!--" [^*] ~"-->" | "<!---->"
comment = ">-" [^*] ~"!<" | ">- !<"



WHITE=[" "|\t\r\n]

%{
    public String lexeme,componenteL;
    public int linea, columna;
    ArrayList<Error1> manejadorErrores = new ArrayList<>();
    public Symbol token(int simbolo){
            Lexema lexema = new Lexema( yytext() );
            return new Symbol(simbolo,yyline+1,yycolumn,lexema);
    }
    public Symbol token(int simbolo,String componenteLexico){
            Lexema lexema = new Lexema( yytext() );
            return new Symbol(simbolo,yyline+1,yycolumn,lexema);
    }
    public Symbol token(int simbolo,int linea, int columna,String lexeme,String componenteLexico){
            //System.out.println(yytext());
            Lexema lexema = new Lexema( yytext() );
            //System.out.println(lexema);
            componenteL=componenteLexico;
            //Principal.setError("La cadena "+yytext()+" es invalida, se encontro en la linea "+(yyline+1)+", y en la columna "+(yycolumn+1));
            return new Symbol(simbolo,yyline,yycolumn,yytext());
    }
%}
%%

{WHITE}         {/*Ignore*/}
{OurComment}    {}
{comment}       {return token(sym.COMENTARIO, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"COMMENT");}
"\""     {return token(sym.COMILLA_SIMPLE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"COMILLA_SIMPLE");}
("(-"({D}+"."+)+")")|({D}+"."+)+ { return token(sym.INVNUMERO,linea=yyline+1,columna=yycolumn,lexeme=yytext(),"INVALID_NUMERO");}
("(-"{D}+")")|{D}+|{D}"."{D}+   { return token(sym.NUMERO,linea=yyline+1,columna=yycolumn,lexeme=yytext(),"NUMERO");}

"("     {return token(sym.PARENTESIS_A, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"PARENTESIS_ABRIR");}
")"     {return token(sym.PARENTESIS_C, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"PARENTESIS_CERRAR");}
"{"     {return token(sym.LLAVE_A, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"LLAVE_ABRIR");}
"}"     {return token(sym.LLAVE_C, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"LLAVE_CERRAR");}
"["     {return token(sym.CORCHETE_A, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"CORCHETE_ABRIR");}
"]"     {return token(sym.CORCHETE_C, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"CORCHETE_CERRAR");}
"_"     {return token(sym.GUIONBAJO, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"GUION_BAJO");}
";"     {return token(sym.PUNTOCOMA, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"PUNTO_COMA");}
","     {return token(sym.COMA, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"COMA");}
":"     {return token(sym.DOSPUNTOS, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"DOSPUNTOS");}
"."     {return token(sym.PUNTO, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"PUNTO");}
"&&"     {return token(sym.AND, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"AND");}
"&"     {return token(sym.ANDSIMPLE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"AND_SIMPLE");}
"||"     {return token(sym.OR, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"OR");}
"|"     {return token(sym.ORSIMPLE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ORSIMPLE");}
"!"     {return token(sym.NOT, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"NOT");}
"^"     {return token(sym.XOR, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"XOR");}
">"     {return token(sym.MAYORQUE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"MAYORQUE");}
">="     {return token(sym.MAYORIGUAL, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"MAYORIGUAL");}
"<"     {return token(sym.MENORQUE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"MENORQUE");}
"<="     {return token(sym.MENORIGUAL, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"MENORIGUAL");}
"=="     {return token(sym.IGUAL, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"IGUAL");}
"==="     {return token(sym.IGUAL2, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"IGUAL2");}
"!="     {return token(sym.DIFERENTE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"DIFERENTE");}
"+"     {return token(sym.SUMA, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"SUMA");}
"-"     {return token(sym.RESTA, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"RESTA");}
"*"     {return token(sym.MULTIPLICACION, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"MULTIPLICACION");}
"/"     {return token(sym.DIVISION, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"DIVISION");}
"="     {return token(sym.ASIGNACION, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ASIGNACION");}
"+="     {return token(sym.ASIGNACIONINCRE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ASIGNACIONINCRE");}
"-="     {return token(sym.ASIGNACIONDECRE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ASIGNACIONDECRE");}
"*="     {return token(sym.ASIGNACIONMULT, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ASIGNACIONMULT");}
"/="     {return token(sym.ASIGNACIONDIV, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ASIGNACIONDIV");}
"++"     {return token(sym.OPERCREMENTO, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"OPERCREMENTO");}
"--"     {return token(sym.OPERDECREMENTO, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"OPERDECREMENTO");}

//PALABRAS RESERVADAS

"CSS"     {return token(sym.CSS, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"CSS");}
"HTML"     {return token(sym.HTML, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"HTML");}
"VAR"     {return token(sym.VAR, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"VAR");}
"MAIN"     {return token(sym.MAIN, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"MAIN");}
"FUNCTION"     {return token(sym.FUNCTION, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"FUNCTION");}
"IF"     {return token(sym.IF, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"IF");}
"FOR"     {return token(sym.FOR, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"FOR");}
"ARRAY"     {return token(sym.ARRAY, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ARRAY");}
"DECLARE"     {return token(sym.DECLARE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"DECLARE");}
"JS"     {return token(sym.JS, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"JS");}
"CREATE"     {return token(sym.CREATE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"CREATE");}
"PAGE"     {return token(sym.PAGE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"PAGE");}
"NULL"     {return token(sym.NULL, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"NULL");}
"TRUE"     {return token(sym.TRUE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"TRUE");}
"FALSE"     {return token(sym.FALSE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"FALSE");}
"THIS"     {return token(sym.THIS, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"THIS");}
"RETURN"     {return token(sym.RETURN, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"RETURN");}
"ELSE"     {return token(sym.ELSE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ELSE");}
"CATCH"     {return token(sym.CATCH, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"CATCH");}
"TRY"     {return token(sym.TRY, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"TRY");}
"BREAK"     {return token(sym.BREAK, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"BREAK");}
"MAPX"     {return token(sym.MAPX, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"MAPX");}
"FORMATTER"     {return token(sym.FORMATTER, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"FORMATTER");}
"DATE"     {return token(sym.DATE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"DATE");}
"IN"     {return token(sym.EN, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"EN");}
"WHILE" {return token(sym.WHILE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"WHILE");}
"TABLE" {return token(sym.TABLE, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"TABLE");}
"DIV" {return token(sym.DIV, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"DIV");}
"LIST" {return token(sym.LIST, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"LIST");}

"optSelect" {return token(sym.optSelect, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"optSelect");}
"AClass"     {return token(sym.AClass, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"AClass");}
"RClass"     {return token(sym.RClass, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"RClass");}
"getAtt"     {return token(sym.getAtt, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getAtt");}
"docCreateElem"     {return token(sym.docCreateElem, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"docCreateElem");}
"docGetElemID"     {return token(sym.docGetElemID, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"docGetElemID");}
"docGetElemClass"     {return token(sym.docGetElemClass, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"docGetElemClass");}
"CONSOL"     {return token(sym.CONSOL, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"CONSOL");}
"Child"     {return token(sym.Child, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"Child");}
"ChildText"     {return token(sym.ChildText, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ChildText");}
"ChildTextH"     {return token(sym.ChildTextH, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ChildTextH");}
"RChild"     {return token(sym.RChild, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"RChild");}
"beforeChild"     {return token(sym.beforeChild, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"beforeChild");}


"JQElem"     {return token(sym.JQElem, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"JQElem");}
"fileName"    {return token(sym.fileName, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"fileName");}
"JQDocReady"     {return token(sym.JQDocReady, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"JQDocReady");}
"docGetElemTag"     {return token(sym.docGetElemTag, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"docGetElemTag");}
"docCreateElemTag"     {return token(sym.docCreateElemTag, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"docCreateElemTag");}
"docOn"     {return token(sym.docOn, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"docOn");}
"Remove"     {return token(sym.Remove, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"Remove");}
"Value"     {return token(sym.Value, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"Value");}
"Val"     {return token(sym.Val, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"Val");}
"makeTable"     {return token(sym.makeTable, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"makeTable");}
"setHTML"     {return token(sym.setHTML, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"setHTML");}
"HG"     {return token(sym.HG, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"HG");}
"WD"     {return token(sym.WD, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"WD");}
"inHTML"     {return token(sym.inHTML, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"inHTML");}
"outHTML"     {return token(sym.outHTML, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"outHTML");}
"css"     {return token(sym.css, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"css");}
"getBody"     {return token(sym.getBody, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getBody");}
"getDate"     {return token(sym.docCreateElemTag, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getDate");}
"getYear"     {return token(sym.getYear, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getYear");}
"getHour"     {return token(sym.getHour, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getHour");}
"getMSecond"     {return token(sym.getMSecond, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getMSecond");}
"getMinute"     {return token(sym.getMinute, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getMinute");}
"getMonth"     {return token(sym.getMonth, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getMonth");}
"getSecond"     {return token(sym.getSecond, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"getSecond");}
"div"     {return token(sym.div, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"div");}
"hvr"     {return token(sym.hvr, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"hvr");}

"PrintCon"     {return token(sym.PrintCon, linea=yyline+1,columna=yycolumn,lexeme=yytext(),"PrintCon");}


{L}({L}|{D})*                   {return token(sym.ID,linea=yyline+1,columna=yycolumn,lexeme=yytext(),"IDENTIFICADOR");}
\"[^\"\\]*\" {return token(sym.STRING,linea=yyline+1,columna=yycolumn,lexeme=yytext(),"STRING");}

.           {manejadorErrores.add( new Error1("ES",yyline,yycolumn,"Error lexico en la linea: "+(yyline+1)+", columna: "+(yycolumn+1)+".Simbolo '"+yytext()+"' inesperado")); 
            return token(sym.ERROR,linea=yyline+1,columna=yycolumn,lexeme=yytext(),"ERROR");}
