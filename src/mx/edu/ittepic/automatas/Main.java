/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.automatas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author t4k3r0
 */
public class Main {
    
    public static void generarLexer(String path){
        File file= new File(path);
        JFlex.Main.generate(file);
        
    }
    
    public static void generarCup(String cup) throws IOException, Exception{        
        String[] archivoCup={"-parser","Cup",cup};
        java_cup.Main.main(archivoCup);
        boolean RedirigidoCup = redirigir("Cup.java");
        boolean RedirigidoSym= redirigir("sym.java");
        if(RedirigidoCup && RedirigidoSym){
            String filePath = new File("").getAbsolutePath();
            String cupSemantico = filePath+File.separator+"src"+File.separator+"mx"+File.separator+"edu"+File.separator+"ittepic"
            +File.separator+"automatas"+File.separator+"CupSemantico.cup";
            generarCupSemantico(cupSemantico);
            System.exit(0);
        }else System.err.println("Falló generar Cup");
     }
    public static void generarCupObjeto(String cup) throws IOException, Exception{        
        String[] archivoCup={"-parser","CupObjeto",cup};
        java_cup.Main.main(archivoCup);
        boolean RedirigidoCup = redirigir("CupObjeto.java");
        boolean RedirigidoSym= redirigir("sym.java");
        if(RedirigidoCup && RedirigidoSym){
            System.exit(0);
        }else System.err.println("Falló generar Cup");
     }
    public static void generarCupSemantico(String cup) throws IOException, Exception{        
        String[] archivoCup={"-parser","CupSemantico",cup};
        java_cup.Main.main(archivoCup);
        boolean RedirigidoCup = redirigir("CupSemantico.java");
        boolean RedirigidoSym= redirigir("sym.java");
        if(RedirigidoCup && RedirigidoSym){
            String filePath = new File("").getAbsolutePath();
            String cupErrores = filePath+File.separator+"src"+File.separator+"mx"+File.separator+"edu"+File.separator+"ittepic"
            +File.separator+"automatas"+File.separator+"CupObjeto.cup";
            generarCupObjeto(cupErrores);
            System.exit(0);
        }else System.err.println("Falló generar Cup");
     }
    public static boolean redirigir(String file) {
        boolean exito = false;
        File archivo = new File(file);
        if (archivo.exists()) {
            Path currentRelativePath = Paths.get("");
            String miLocacion = currentRelativePath.toAbsolutePath().toString()+ File.separator + "src" + File.separator+ "mx" + File.separator+"edu"+ File.separator
                    +"ittepic"+ File.separator+"automatas"+ File.separator+ archivo.getName();
            
            File archivoAnterior = new File(miLocacion);
            archivoAnterior.delete();
            System.err.println(miLocacion);
            if (archivo.renameTo(new File(miLocacion))) {
                exito = true;
            }else System.err.println("Fallo renombrar");
        }else System.err.println("Fallo redirigir");
        return exito;
    }
    public static void main(String[] args) throws Exception{
        String filePath = new File("").getAbsolutePath(); 
        String lexer = filePath+File.separator+"src"+File.separator+"mx"+File.separator+"edu"+File.separator+"ittepic"
        +File.separator+"automatas"+File.separator+"Lexer.flex";
        String cup = filePath+File.separator+"src"+File.separator+"mx"+File.separator+"edu"+File.separator+"ittepic"
        +File.separator+"automatas"+File.separator+"Cup.cup";
        
        System.out.print(cup);
        generarLexer(lexer); 
        generarCup(cup);
        
    }
}

