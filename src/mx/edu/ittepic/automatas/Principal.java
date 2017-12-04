/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.automatas;

import com.ozten.font.JFontChooser;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


/**
 *
 * @author t4k3r0
 */
public class Principal extends javax.swing.JFrame {
    
DefaultStyledDocument doc;
static ArrayList<String> listaErrores;
ArrayList<Error1> manejadorErrores = new ArrayList<>();
ArrayList<Variables> ManejadorVariables = new ArrayList<>();
static CupSemantico parserG;
static String codigointer = "",codigointerJs = "",codigointerCss = "",variables="";
static ArrayList<String> recorridoAutomata= new ArrayList<>(), recorridoGramatica= new ArrayList<>();

    
    /* !!!!! Cambio de color de texto mientras se escribe !!!!!*/
    
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }
    
    
    
    /*!!!! Fin de cambio de color de texto mientras se escribe*/
    /**
     * Creates new form Principal
     */
    public Principal() {
        
       
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet red = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet Black = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        final AttributeSet blue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.blue);
        final AttributeSet green = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.green);
        final AttributeSet yellow = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.yellow);
        final AttributeSet orange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        final AttributeSet pink = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.magenta);
        final AttributeSet purple = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(201,127,9));
        
         doc= new DefaultStyledDocument() {
             

            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = textPane.getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {

                        if (text.substring(wordL, wordR).matches("(\\W)*(function|procedure)")) {
                            setCharacterAttributes(wordL, wordR - wordL, (AttributeSet) red, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(CSS|HTML|VAR|MAIN|FUNCTION|IF|FOR|ARRAY|DECLARE|JS|CREATE|PAGE|NULL|TRUE|FALSE|THIS|RETURN|ELSE|CATCH|TRY|BREAK|DATE|IN|SCRIPT|WHILE)")) {
                            setCharacterAttributes(wordL, wordR - wordL, (AttributeSet) pink, false);
                        }  else if (text.substring(wordL, wordR).matches("(\\W)*([0-9]+)")) {
                            setCharacterAttributes(wordL, wordR - wordL, (AttributeSet) green, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*([+*-])")) {
                            setCharacterAttributes(wordL, wordR - wordL,  purple, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(STRING|NUMERIC)")) {
                            setCharacterAttributes(wordL, wordR - wordL, blue, false);
                        } else {
                            setCharacterAttributes(wordL, wordR - wordL, Black , false);
                        }

                        wordL = wordR; 
                    }
                    wordR++;
                }
            }

            
            
        };
    
        initComponents();
        //txtPane.setEditable(false);
        
       /* Color bgColor = new Color(61,61,61);
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", bgColor);
        textPane.putClientProperty("Nimbus.Overrides", defaults);
        textPane.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        textPane.setBackground(bgColor);*/
        
        
        
        
    }
    //AQUI SE EJECUTA EL ANALISIS
    private void run() throws IOException, Exception {
        CUP$CupObjeto$actions.attrElem="";
        CUP$CupObjeto$actions.attrElem2="";
        CUP$CupObjeto$actions.v="";
        CUP$CupObjeto$actions.fin="";
        CUP$CupObjeto$actions.v2="";
        CUP$CupObjeto$actions.v3="";
        CUP$CupObjeto$actions.vText="";
        CUP$CupObjeto$actions.listaElem="";
        CUP$CupObjeto$actions.expFor1="";
        CUP$CupObjeto$actions.expFor2="";
        CUP$CupObjeto$actions.expFor3="";
        CUP$CupObjeto$actions.expForOper="";
        CUP$CupObjeto$actions.expForAsig="";
        CUP$CupObjeto$actions.forElem="";
        CUP$CupObjeto$actions.varFor="";
        CUP$CupObjeto$actions.v4="";
        CUP$CupObjeto$actions.v5="";
        CUP$CupObjeto$actions.finCss="";
        CUP$CupObjeto$actions.finJs="";
        CUP$CupObjeto$actions.finFun="";
        CUP$CupObjeto$actions.jsText="";
        CUP$CupObjeto$actions.varExpFor1="";
        CUP$CupObjeto$actions.varCss1="";
        CUP$CupObjeto$actions.varCssCadena="";
        CUP$CupObjeto$actions.varFor2="";
        
        CUP$CupSemantico$actions.varNombre="";
        CUP$CupSemantico$actions.varValor="";
        CUP$CupSemantico$actions.varTipo="";
        ManejadorVariables=new ArrayList<>();
        manejadorErrores=new ArrayList<>();
                
        txtPane.setText("");
        DefaultTableModel modelo = (DefaultTableModel) tblDatos.getModel();
        for (int i = 0; i < tblDatos.getRowCount(); i++) {
           modelo.removeRow(i);
           i-=1;
       }
        File fichero = new File("fichero.txt");

        PrintWriter writer;
        try{
            writer = new PrintWriter(fichero);
            writer.print(textPane.getText());
            writer.close();
            
        }catch(FileNotFoundException e){
            System.out.print(e);
            
        }
        
        String filePath = new File("").getAbsolutePath();
        Reader reader = new BufferedReader(new FileReader(filePath+""+File.separator+"fichero.txt"));
        analizarCodigo(reader);
        sintactico();
        
        String log="";
        System.out.println("Tamaño manejador errores: " + manejadorErrores.size());
        if (manejadorErrores.size() == 0) {
                semanticoCup();
        } else {
            Collections.sort(manejadorErrores, new Comparator<Error1>() { //Ordenamiento a partir de numero de linea
                @Override
                public int compare(Error1 p1, Error1 p2) {
                    return new Integer(p1.getLinea()).compareTo(new Integer(p2.getLinea()));
                }
            });
            String merrores = mostrarManejadorErrores();
            appendToPane(txtPane, merrores, Color.RED);
            
            //System.out.println(merrores);
            manejadorErrores.clear();
            
        }
    }
    public String mostrarManejadorErrores() {
        String errores = "";
        for (int i = 0; i <= manejadorErrores.size() - 1; i++) {
            String error = (manejadorErrores.get(i) + "\n");
            if (!error.equals("\n")) {
                errores += error;
            }
        }
        return errores;
    }
    public String mostrarManejadorVariables() {
        String errores = "";
        for (int i = 0; i <= ManejadorVariables.size() - 1; i++) {
            String error = ("Nombre: "+ManejadorVariables.get(i).nombre 
                    +", Valor: "+ManejadorVariables.get(i).valor +", Tipo: "+ManejadorVariables.get(i).tipo + "\n");
            if (!error.equals("\n")) {
                errores += error;
            }
        }
        return errores;
    }
    public void analizarCodigo(Reader codigo) throws Exception {
        String log = "";
        txtPane.setText(log);
        //String codigo = textPane.getText();
        DefaultTableModel modelo = (DefaultTableModel) tblDatos.getModel();
        for (int i = 0; i < tblDatos.getRowCount(); i++) {
           modelo.removeRow(i);
           i-=1;
       }
        Lexer lexer = new Lexer(codigo);
        
        int simbolo=lexer.next_token().sym;
        while(true){ 
            switch (simbolo){
                case sym.ERROR: //Descomentar en lexer.flex para que suceda.
                    Object[] errid = {lexer.componenteL, lexer.lexeme, lexer.linea};
                    modelo.addRow(errid);
                    log = log+ "Error lexico de identificador:"+"Numero de linea:"+lexer.linea+". Identificador incorrecto: "+lexer.lexeme+"\n";
                    //manejadorErrores.add(new Error1("ES",lexer.linea,lexer.columna,"Error lexico en la linea: "+(lexer.linea+1)+", columna: "+(lexer.columna+1)+".")); 
                    break;
                case sym.error:
                    log = log+"Error lexico. "+"Cadena: "+lexer.lexeme+"      Numero de linea:"+lexer.linea+" \n";
                    txtPane.setText(log);
                    Object[] error = {"Error Lexico",lexer.lexeme , lexer.linea};
                    modelo.addRow(error);
                    break;
                default:
                    Object[] string = {lexer.componenteL, lexer.lexeme, lexer.linea};
                    modelo.addRow(string);
            }
            if(lexer.next_token().sym==0){
                return;
            }
        }
    }

    public void sintactico() {
        String codigo = textPane.getText();
        Lexer flex = new Lexer(new StringReader(codigo));
        Cup parser;
        
        ArrayList<Error1> m = new ArrayList<Error1>();
        parser = new Cup(flex, m, textPane.getDocument().getDefaultRootElement().getElementCount());
        try {
            parser.parse();
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!parser.ManejadorDeErrores.isEmpty() || !flex.manejadorErrores.isEmpty()) {
            manejadorErrores.addAll(flex.manejadorErrores);
            manejadorErrores.addAll(parser.ManejadorDeErrores);
            parser.ManejadorDeErrores.clear();
        }else{
            //System.out.println(recorridoAutomata);
            //appendToPane(txtPane, "¡Análisis Terminado!", Color.BLUE);
        }
    }
    public void objetoCup() {
        String codigo = textPane.getText();
        Lexer flex = new Lexer(new StringReader(codigo));
        CupObjeto parser;
        ArrayList<Error1> m = new ArrayList<Error1>();
        parser = new CupObjeto(flex,ManejadorVariables, m, textPane.getDocument().getDefaultRootElement().getElementCount());
        try {
            parser.parse();
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!parser.ManejadorDeErrores.isEmpty() ) {
            manejadorErrores.addAll(parser.ManejadorDeErrores);
            parser.ManejadorDeErrores.clear();
        }else{
            System.out.println(codigointer);
            //System.out.println(codigointerJs);
            //System.out.println(codigointerCss);
            
            appendToPane(txtPane, "¡Análisis Terminado!", Color.BLUE);
        }
    }
    public void semanticoCup() {
        String codigo = textPane.getText();
        Lexer flex = new Lexer(new StringReader(codigo));
        CupSemantico parser;
        ArrayList<Variables> v = new ArrayList<Variables>();
        ArrayList<Error1> m = new ArrayList<Error1>();
        parser = new CupSemantico(flex,v, m, textPane.getDocument().getDefaultRootElement().getElementCount());
        try {
            parser.parse();
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        if (!parser.ManejadorDeErrores.isEmpty() ) {
            manejadorErrores.addAll(parser.ManejadorDeErrores);
            parser.ManejadorDeErrores.clear();
            appendToPane(txtPane,mostrarManejadorErrores() , Color.RED);
        }else{
            ManejadorVariables.addAll(parser.ManejadorVariables);
            parser.ManejadorVariables.clear();
            String variables = mostrarManejadorVariables();
            System.out.println(variables);
            objetoCup(); 
        }
    }
    private void guardar(String texto,String path){
        BufferedWriter bw = null;
		FileWriter fw = null;
        try {
			String content = "This is the content to write into file\n";
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(texto);
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }
    //AQUI AGREGA AL PANEL EL TEXTO EN COLOR
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    public static void setError(String error) {
        System.out.println(error);
        //listaErrores.add(error);
    }
    
    public void limpiar(DefaultTableModel ea){
       for (int i = 0; i < tblDatos.getRowCount(); i++) {
           ea.removeRow(i);
           i-=1;
       }
    }
     


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        clicDMenu = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        saveDialog = new javax.swing.JFileChooser();
        scrollPane = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane(doc);
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPane = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        compile = new javax.swing.JButton();
        auto = new javax.swing.JButton();
        save = new javax.swing.JButton();
        open = new javax.swing.JButton();
        com = new javax.swing.JButton();
        grama = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();
        labelfondo = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem(new DefaultEditorKit.CopyAction());
        jMenuItem8 = new javax.swing.JMenuItem(new DefaultEditorKit.CutAction());
        jMenuItem9 = new javax.swing.JMenuItem(new DefaultEditorKit.PasteAction());
        jMenu4 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        jMenuItem3.setText("Copiar - Ctrl + c");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        clicDMenu.add(jMenuItem3);

        jMenuItem5.setText("Pegar - Ctrl + v");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        clicDMenu.add(jMenuItem5);

        jMenuItem6.setText("Cortar - Ctrl + x");
        clicDMenu.add(jMenuItem6);

        saveDialog.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Abysscript");
        setBackground(new java.awt.Color(61, 61, 61));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(null);
        setResizable(false);
        setSize(new java.awt.Dimension(1100, 500));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textPane.setFont(new java.awt.Font("PT Mono", 0, 16)); // NOI18N
        textPane.setText("MAIN index {\n\tHTML {\n\t               DIV div1{\n\t                       docCreateElem(\"h1\").setHTML(\"Class\",\"h1\").ChildTextH(\"Titulo de ejemplo\")\n \t               }\n\t              FOR(VAR I=0;I<2;I++){\n\t                     docCreateElem(\"label\").setHTML(\"Class\",\"col-md-4 clase\").ChildTextH(\"Etiqueta\")\n\t                     docCreateElem(\"input\").setHTML(\"type\",\"text\").setHTML(\"Class\",\"form-control col-md-4 clase\")\n\t              }\n                                           docCreateElem(\"button\").setHTML(\"value\",\"button\").setHTML(\"Class\",\"btn btn-default clase2\").ChildTextH(\"Aceptar\")\n \t             docCreateElem(\"button\").setHTML(\"value\",\"button\").setHTML(\"Class\",\"btn btn-danger clase2\").ChildTextH(\"Cancelar\")\n\t \n           \t             TABLE(\"idTabla\",[\"Columna1\",\"Columna2\",\"Columna3\"],[\"Celda1\",\"Celda2\",\"Celda3\"],[\"Celda1\",\"Celda2\",\"Celda3\"])\n\t             LIST(\"idLista\",[\"Lista1\",\"Lista2\",\"Lista3\"])\t\t\n\t}\n\tJS E {\n\t\tVAR E=\"QERG\";\n\t\tdocGetElemID(\"titulo\").setHTML(\"class\",\".personal\")\n\t\tdocGetElemID(\"titulo2\").getAtt(\"id\")\n\t\tCONSOL(\"Entro\")\n\t}\n\tCSS(\".clase\",[\n\t\t\"display\": \"block\",\n\t\t\"margin-bottom\":\"10px\"\n \t])\n\tCSS(\".clase2\",[\n\t\t\"margin-bottom\":\"30px\",\n\t\t\"display\": \"inline-block\",\n\t\t\"margin-bottom\":\"10px\",\n\t\t\"margin\": \"auto\"\n\t])\n\tCSS(\"#div1\",[\n\t\t\"text-align\":\"center\"\n\t])\n\tCSS(\"#div2\",[\n\t\t\"display\": \"block\"\n\t])\n\tCSS(\"#idTabla\",[\n\t\t\"margin-bottom\":\"30px\",\n\t\t\"width\": \"100px\",\n\t\t\"margin\":\"auto\"\n\t])\n\tCSS(\"#idLista\",[\n\t\t\"margin\":\"auto\"\n\t])\n}");
        textPane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        textPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                textPaneMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                textPaneMouseReleased(evt);
            }
        });
        scrollPane.setViewportView(textPane);
        TextLineNumber tln = new TextLineNumber(textPane);
        tln.setForeground(Color.WHITE);
        scrollPane.setRowHeaderView( tln );

        getContentPane().add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 760, 420));

        txtPane.setFocusable(false);
        jScrollPane1.setViewportView(txtPane);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 1120, 130));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Salida");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, -1, -1));

        compile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/analizar.png"))); // NOI18N
        compile.setToolTipText("Analizar");
        compile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        compile.setBorderPainted(false);
        compile.setContentAreaFilled(false);
        compile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                compileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                compileMouseExited(evt);
            }
        });
        compile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });
        getContentPane().add(compile, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 70, 70));

        auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/grafo.png"))); // NOI18N
        auto.setToolTipText("Automata");
        auto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        auto.setBorderPainted(false);
        auto.setContentAreaFilled(false);
        auto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                autoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                autoMouseExited(evt);
            }
        });
        auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoActionPerformed(evt);
            }
        });
        getContentPane().add(auto, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 70, 70));

        save.setBackground(null);
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/guardar.png"))); // NOI18N
        save.setToolTipText("Guardar");
        save.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        save.setBorderPainted(false);
        save.setContentAreaFilled(false);
        save.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveMouseExited(evt);
            }
        });
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        getContentPane().add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 70, 70));

        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/carpeta2.png"))); // NOI18N
        open.setToolTipText("Abrir");
        open.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        open.setBorderPainted(false);
        open.setContentAreaFilled(false);
        open.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openMouseEntered(evt);
            }
        });
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        getContentPane().add(open, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 70, 70));

        com.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/caja.png"))); // NOI18N
        com.setToolTipText("Analizar");
        com.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        com.setBorderPainted(false);
        com.setContentAreaFilled(false);
        com.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                comMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comMouseExited(evt);
            }
        });
        com.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comActionPerformed(evt);
            }
        });
        getContentPane().add(com, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 70, 70));

        grama.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/abc.png"))); // NOI18N
        grama.setToolTipText("Gramatica");
        grama.setBorderPainted(false);
        grama.setContentAreaFilled(false);
        grama.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                gramaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                gramaMouseExited(evt);
            }
        });
        grama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gramaActionPerformed(evt);
            }
        });
        getContentPane().add(grama, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, -10, 70, 90));

        tblDatos.setFont(new java.awt.Font("Malayalam MN", 0, 14)); // NOI18N
        tblDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Componente Lexico", "Lexemas", "# de linea"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblDatos);
        if (tblDatos.getColumnModel().getColumnCount() > 0) {
            tblDatos.getColumnModel().getColumn(0).setPreferredWidth(200);
            tblDatos.getColumnModel().getColumn(2).setResizable(false);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 70, 340, 420));

        labelfondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/fondo2.png"))); // NOI18N
        getContentPane().add(labelfondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1140, 660));

        jMenuBar1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N

        jMenu1.setText("Archivo");
        jMenu1.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 16)); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        jMenuItem1.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/carpeta.png"))); // NOI18N
        jMenuItem1.setText("Abrir archivo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, 0));
        jMenuItem12.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/guardar3.png"))); // NOI18N
        jMenuItem12.setText("Guardar");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, 0));
        jMenuItem2.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/salir3.png"))); // NOI18N
        jMenuItem2.setText("Salir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Editar");
        jMenu3.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 16)); // NOI18N

        jMenuItem7.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/copiar3.png"))); // NOI18N
        jMenuItem7.setText("Copiar");
        jMenu3.add(jMenuItem7);

        jMenuItem8.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/tijeras3.png"))); // NOI18N
        jMenuItem8.setText("Cortar");
        jMenu3.add(jMenuItem8);

        jMenuItem9.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/pegar3.png"))); // NOI18N
        jMenuItem9.setText("Pegar");
        jMenu3.add(jMenuItem9);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Ejecutar");
        jMenu4.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 16)); // NOI18N

        jMenuItem10.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/analizar3.png"))); // NOI18N
        jMenuItem10.setText("Analizar");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem11.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/caja3.png"))); // NOI18N
        jMenuItem11.setText("Compilar");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Temas");
        jMenu5.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 16)); // NOI18N

        jMenuItem19.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem19.setText("Fuente");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem19);

        jMenuItem15.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem15.setText("Deafault");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuItem16.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem16.setText("Rojo");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem16);

        jMenuItem17.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem17.setText("Naranja");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem17);

        jMenuItem18.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem18.setText("Verde");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem18);

        jMenuBar1.add(jMenu5);

        jMenu2.setText("Información");
        jMenu2.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 16)); // NOI18N

        jMenuItem13.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/inter.png"))); // NOI18N
        jMenuItem13.setText("Codigo intermedio");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem13);

        jMenuItem14.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/opti.png"))); // NOI18N
        jMenuItem14.setText("Optimización");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem4.setFont(new java.awt.Font("Lucida Sans Unicode", 0, 14)); // NOI18N
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/informacion3.png"))); // NOI18N
        jMenuItem4.setText("Sobre el programa");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

   
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        
        int returnVal = fileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        try {
          BufferedReader br = new BufferedReader( new FileReader( file.getAbsolutePath() ) );
              
          StringBuilder sb = new StringBuilder();
    
          String line = br.readLine();

          while (line != null) {
        
              sb.append(line);
              sb.append(System.lineSeparator());
              line = br.readLine();
    }
          textPane.setText(sb.toString());
        } catch (IOException ex) {
          System.out.println("problem accessing file"+file.getAbsolutePath());
        }
    } else {
        System.out.println("File access cancelled by user.");
    }
    

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (Desktop.isDesktopSupported()) {
            try {
                String filePath = new File("").getAbsolutePath(); 
                String pdf = filePath+File.separator+"src"+File.separator+"mx"+File.separator+"edu"+File.separator+"ittepic"
                    +File.separator+"automatas"+File.separator+"Manual.pdf";
                File myFile = new File(pdf);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
       /* showMessageDialog(null, "Analizador Lexico elaborado por: \nCasillas Ureña Fermin Michel\n"
                + "Murillo Macías Manuel Alejandro\nJuan Carlos Ramírez Tapia\nEdgar Ernesto Lozano Mora");*/
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /*private void onPaste(){
    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable t = c.getContents(this);
    if (t == null)
        return;
    try {
        String asde = textPane.getText();
        textPane.setText(asde+(String) t.getTransferData(DataFlavor.stringFlavor));
    } catch (Exception e){
        e.printStackTrace();
    }//try
    }//onPaste*/
    
    
    
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

       /* StringSelection stringSelection = new StringSelection (textPane.getText());
        Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        clpbrd.setContents (stringSelection, null);*/
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    /*private void rightMenu(MouseEvent evt){
        if(evt.isPopupTrigger()){
            clicDMenu.show(this, evt.getX(), evt.getY());
        }
    }*/
    private void textPaneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textPaneMouseReleased
        //rightMenu(evt);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_textPaneMouseReleased

    private void textPaneMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textPaneMousePressed
       /* rightMenu(evt);*/
// TODO add your handling code here:
    }//GEN-LAST:event_textPaneMousePressed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        //onPaste();
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed

        try {
            run();
            //probarLexerFile();
            // TODO add your handling code here:
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem10ActionPerformed
    FileWriter fw;
    public void guardar()
      {
            
            try
            {
                String fileName = showInputDialog("Nombre del archivo");
                
                  
                   if(fileName!=null){
                        fw= new FileWriter(fileName+".abys");
                        fw.write(textPane.getText());
                        showMessageDialog(null,"Fichero guardado");
                        fw.close();
                    }
            }
            catch(IOException io)
            {
                  javax.swing.JOptionPane.showMessageDialog(null, "Error en crear el archivo");
            }
 
           
      }
    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

        guardar();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void autoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoActionPerformed
        txtPane.setText("");
        if(!recorridoAutomata.isEmpty()){
            String x="";
            V_Automata a=new V_Automata();
            Collections.reverse(recorridoAutomata);
            for(int i=0;i<recorridoAutomata.size();i++){
                x=x+recorridoAutomata.get(i);
            }
            a.setText(x);
            a.setVisible(true);
        }else{
            appendToPane(txtPane, "No se puede abrir el automata porque no se ha compilado", Color.RED);
        }
    }//GEN-LAST:event_autoActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
	guardar();
            

        // TODO add your handling code here:
    }//GEN-LAST:event_saveActionPerformed

    private void compileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileActionPerformed

        try {
            run();
            //probarLexerFile();
            // TODO add your handling code here:
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
    }

        // TODO add your handling code here:
    }//GEN-LAST:event_compileActionPerformed

    private void autoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoMouseExited
        auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("grafo.png")));        // TODO add your handling code here:
    }//GEN-LAST:event_autoMouseExited

    private void autoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_autoMouseEntered
      auto.setIcon(new javax.swing.ImageIcon(getClass().getResource("grafo2.png")));        // TODO add your handling code here:
    }//GEN-LAST:event_autoMouseEntered

    private void saveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseEntered
     save.setIcon(new javax.swing.ImageIcon(getClass().getResource("guardar2.png")));        // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseEntered

    private void saveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveMouseExited
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("guardar.png")));         // TODO add your handling code here:
    }//GEN-LAST:event_saveMouseExited

    private void compileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_compileMouseEntered
       compile.setIcon(new javax.swing.ImageIcon(getClass().getResource("analizar2.png")));         // TODO add your handling code here:
    }//GEN-LAST:event_compileMouseEntered

    private void compileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_compileMouseExited
        compile.setIcon(new javax.swing.ImageIcon(getClass().getResource("analizar.png")));          // TODO add your handling code here:
    }//GEN-LAST:event_compileMouseExited

    private void comMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comMouseExited
        com.setIcon(new javax.swing.ImageIcon(getClass().getResource("caja.png")));
    }//GEN-LAST:event_comMouseExited

    private void comMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comMouseEntered
        com.setIcon(new javax.swing.ImageIcon(getClass().getResource("caja2.png")));
    }//GEN-LAST:event_comMouseEntered

    private void comActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comActionPerformed
       txtPane.setText("");
       if (manejadorErrores.isEmpty() && codigointer!="" && codigointer!=null) {
            
            String x= "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Index</title>"
                    + "<script src='https://code.jquery.com/jquery-3.2.1.slim.min.js' integrity='sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN' crossorigin='anonymous'></script><script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js' integrity='sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh' crossorigin='anonymous'></script>"
                    + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css' integrity='sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb' crossorigin='anonymous'>"
                    + "<script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js' integrity='sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ' crossorigin='anonymous'></script>"
                    + "<link rel='stylesheet' type='text/css' href='css/index.css'>"
                    + "<script type='text/javascript' src='js/index.js'></script>"
                    + "</head>"
                    + "<body>"
                    +codigointer
                    + "</body>"
                    + "</html>";
            String x2 = "$(document).ready(function () {"+codigointerJs+"});";
            String x3 = codigointerCss;
            
            Path currentRelativePath = Paths.get("");
            String locacion=currentRelativePath.toAbsolutePath().toString()+ File.separator+"EjemploPagina";
            File directorio1 = new File(locacion+File.separator);
            directorio1.mkdirs();
            File directorio = new File(locacion+File.separator+"css");
            File directorio2 = new File(locacion+File.separator+"js");
            directorio.mkdirs();
            directorio2.mkdirs();
            guardar(x2,locacion+File.separator+"js"+File.separator+"index.js");
            guardar(x3,locacion+File.separator+"css"+File.separator+"index.css");
            guardar(x,locacion+File.separator+"index.html");
            txtPane.setText("");
            appendToPane(txtPane, "¡Se construyó el proyecto con exito!", Color.BLUE);
            try {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                  desktop = Desktop.getDesktop();
                  desktop.open(new File(locacion+File.separator+"index.html"));
                }
              } catch (IOException ioe) {
                ioe.printStackTrace();
              }
        }else{
            appendToPane(txtPane, "No se puede construir porque el codigo no se ha compilado", Color.RED);
        }
    }//GEN-LAST:event_comActionPerformed

    private void openMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMouseExited
         open.setIcon(new javax.swing.ImageIcon(getClass().getResource("carpeta2.png")));
    }//GEN-LAST:event_openMouseExited

    private void openMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openMouseEntered
           open.setIcon(new javax.swing.ImageIcon(getClass().getResource("carpeta3.png")));         // TODO add your handling code here:
    }//GEN-LAST:event_openMouseEntered

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
int returnVal = fileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        try {
          BufferedReader br = new BufferedReader( new FileReader( file.getAbsolutePath() ) );
              
          StringBuilder sb = new StringBuilder();
    
          String line = br.readLine();

          while (line != null) {
        
              sb.append(line);
              sb.append(System.lineSeparator());
              line = br.readLine();
    }
          textPane.setText(sb.toString());
        } catch (IOException ex) {
          System.out.println("problem accessing file"+file.getAbsolutePath());
        }
    } else {
        System.out.println("File access cancelled by user.");
    }
             
        // TODO add your handling code here:
    }//GEN-LAST:event_openActionPerformed

    private void gramaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gramaActionPerformed

        txtPane.setText("");
        if(!recorridoGramatica.isEmpty()){
            String x="";
            V_gramatica g=new V_gramatica();
            Collections.reverse(recorridoGramatica);
            for(int i=0;i<recorridoGramatica.size();i++){
                x=x+recorridoGramatica.get(i);
            }
            g.setText(x);
            g.setVisible(true);
        }else{
            appendToPane(txtPane, "No se puede abrir la gramatica porque no se ha compilado", Color.RED);
        }
        
    }//GEN-LAST:event_gramaActionPerformed

    private void gramaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gramaMouseEntered
          grama.setIcon(new javax.swing.ImageIcon(getClass().getResource("abc2.png")));                  // TODO add your handling code here:
    }//GEN-LAST:event_gramaMouseEntered

    private void gramaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gramaMouseExited
          grama.setIcon(new javax.swing.ImageIcon(getClass().getResource("abc.png"))); // TODO add your handling code here:
    }//GEN-LAST:event_gramaMouseExited

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        txtPane.setText("");
        if(!codigointer.equals("") && codigointer!=null){
            Optimizacion e = new Optimizacion();
            e.setOptimizacion(codigointer);
            e.setVisible(true);
        }else{
            appendToPane(txtPane, "No se puede abrir la ventana de codigo intermedio porque no se ha compilado", Color.RED);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
       txtPane.setText("");
        if(!codigointer.equals("") && codigointer!=null){
           String x= "<!DOCTYPE html>"
                    + "\n<html>"
                    + "\n<head>"
                    + "\n<title>Index</title>"
                    + "\n<script src='https://code.jquery.com/jquery-3.2.1.slim.min.js' ></script>"
                   + "\n<script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js' ></script>"
                    + "\n<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css' >"
                    + "\n<script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js' ></script>"
                    + "\n<link rel='stylesheet' type='text/css' href='css/index.css'>"
                    + "\n<script type='text/javascript' src='js/index.js'></script>"
                    + "\n</head>"
                    + "\n<body>"
                    +codigointer
                    + "\n</body>"
                    + "\n</html>";
            Optimizacion e = new Optimizacion();
            e.setOptimizacion(x);
            e.setVisible(true);
        }else{
            appendToPane(txtPane, "No se puede abrir la ventana de optimizacion porque no se ha compilado", Color.RED);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
       fondos(labelfondo,"fondo2.png");        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        fondos(labelfondo,"fondo_rojo.png");          // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        fondos(labelfondo,"fondo_naranja.jpg");          // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
              fondos(labelfondo,"fondo_verde.jpg");          // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
           JFontChooser fc = new JFontChooser();
        JOptionPane.showMessageDialog(this, fc, "Seleccione el tipo de fuente", JOptionPane.PLAIN_MESSAGE);
        textPane.setFont(fc.getPreviewFont());        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
               txtPane.setText("");
       if (manejadorErrores.isEmpty() && codigointer!="" && codigointer!=null) {
            
            String x= "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Index</title>"
                    + "<script src='https://code.jquery.com/jquery-3.2.1.slim.min.js' integrity='sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN' crossorigin='anonymous'></script><script src='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js' integrity='sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh' crossorigin='anonymous'></script>"
                    + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css' integrity='sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb' crossorigin='anonymous'>"
                    + "<script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js' integrity='sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ' crossorigin='anonymous'></script>"
                    + "<link rel='stylesheet' type='text/css' href='css/index.css'>"
                    + "<script type='text/javascript' src='js/index.js'></script>"
                    + "</head>"
                    + "<body>"
                    +codigointer
                    + "</body>"
                    + "</html>";
            String x2 = "$(document).ready(function () {"+codigointerJs+"});";
            String x3 = codigointerCss;
            
            Path currentRelativePath = Paths.get("");
            String locacion=currentRelativePath.toAbsolutePath().toString()+ File.separator+"EjemploPagina";
            File directorio1 = new File(locacion+File.separator);
            directorio1.mkdirs();
            File directorio = new File(locacion+File.separator+"css");
            File directorio2 = new File(locacion+File.separator+"js");
            directorio.mkdirs();
            directorio2.mkdirs();
            guardar(x2,locacion+File.separator+"js"+File.separator+"index.js");
            guardar(x3,locacion+File.separator+"css"+File.separator+"index.css");
            guardar(x,locacion+File.separator+"index.html");
            txtPane.setText("");
            appendToPane(txtPane, "¡Se construyó el proyecto con exito!", Color.BLUE);
            try {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                  desktop = Desktop.getDesktop();
                  desktop.open(new File(locacion+File.separator+"index.html"));
                }
              } catch (IOException ioe) {
                ioe.printStackTrace();
              }
        }else{
            appendToPane(txtPane, "No se puede construir porque el codigo no se ha compilado", Color.RED);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed
public void fondos(JLabel label,String ruta){
        ImageIcon imagen= new ImageIcon(getClass().getResource(ruta));
        Icon icono= new ImageIcon(imagen.getImage().
                getScaledInstance(label.getWidth(),label.getHeight(),Image.SCALE_DEFAULT));
        label.setIcon(icono);
        label.repaint();
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
                
                
            }
        });
        
        
        
        
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton auto;
    private javax.swing.JPopupMenu clicDMenu;
    private javax.swing.JButton com;
    private javax.swing.JButton compile;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton grama;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelfondo;
    private javax.swing.JButton open;
    private javax.swing.JButton save;
    private javax.swing.JFileChooser saveDialog;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextPane textPane;
    private javax.swing.JTextPane txtPane;
    // End of variables declaration//GEN-END:variables
    
}

