/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.automatas;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import mx.edu.ittepic.automatas.TextLineNumber;
import mx.edu.ittepic.automatas.Tokedatos;


/**
 *
 * @author t4k3r0
 */
public class Principal extends javax.swing.JFrame {
    
DefaultStyledDocument doc;
static ArrayList<String> listaErrores;
ArrayList<Error1> manejadorErrores = new ArrayList<>();
static String codigointer = "";    
    
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
                        }  else if (text.substring(wordL, wordR).matches("(\\W)*(0|1|2|3|4|5|6|7|8|9)")) {
                            setCharacterAttributes(wordL, wordR - wordL, orange, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(and|or|not)")) {
                            setCharacterAttributes(wordL, wordR - wordL, yellow, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(STRING|NUMERIC)")) {
                            setCharacterAttributes(wordL, wordR - wordL, blue, false);
                        } else {
                            setCharacterAttributes(wordL, wordR - wordL, orange , false);
                        }

                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = textPane.getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(function|private)")) {
                    setCharacterAttributes(before, after - before, red, false);
                } else if (text.substring(before, after).matches("(\\W)*(for|while)")) {
                    setCharacterAttributes(before, after - before, blue, false);
                } else if (text.substring(before, after).matches("(\\W)*(if|else)")) {
                    setCharacterAttributes(before, after - before, green, false);
                } else if (text.substring(before, after).matches("(\\W)*(int|string)")) {
                    setCharacterAttributes(before, after - before, orange, false);
                } else if (text.substring(before, after).matches("(\\W)*(>|<)")) {
                    setCharacterAttributes(before, after - before, yellow, false);
                } else if (text.substring(before, after).matches("(\\W)*(begin|end)")) {
                    setCharacterAttributes(before, after - before, pink, false);
                } else {
                    setCharacterAttributes(before, after - before, orange, false);
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
                objetoCup();
                System.out.print(codigointer);
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
            String error = (manejadorErrores.get(i).toString() + "\n");
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
        if (!parser.ManejadorDeErrores.isEmpty()) {
            manejadorErrores.addAll(parser.ManejadorDeErrores);
            parser.ManejadorDeErrores.clear();
        }else{
            appendToPane(txtPane, "¡Análisis Terminado!", Color.BLUE);
        }
    }
    public void objetoCup() {
        String codigo = textPane.getText();
        Lexer flex = new Lexer(new StringReader(codigo));
        CupObjeto parser;
        ArrayList<Error1> m = new ArrayList<Error1>();
        parser = new CupObjeto(flex, m, textPane.getDocument().getDefaultRootElement().getElementCount());
        System.out.println(flex);
        try {
            parser.parse();
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!parser.ManejadorDeErrores.isEmpty()) {
            manejadorErrores.addAll(parser.ManejadorDeErrores);
            parser.ManejadorDeErrores.clear();
        }else{
            appendToPane(txtPane, "¡Análisis Terminado!", Color.BLUE);
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
        open = new javax.swing.JButton();
        save = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();
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
        jMenu2 = new javax.swing.JMenu();
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

        getContentPane().add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 760, 460));

        txtPane.setFocusable(false);
        jScrollPane1.setViewportView(txtPane);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 520, 1130, 130));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jLabel1.setText("Salida");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, -1, -1));

        compile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/assets/correct.png"))); // NOI18N
        compile.setToolTipText("Analizar");
        compile.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        compile.setContentAreaFilled(false);
        compile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });
        getContentPane().add(compile, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, -1, -1));

        open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/assets/open.png"))); // NOI18N
        open.setToolTipText("Abrir");
        open.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        open.setContentAreaFilled(false);
        open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        getContentPane().add(open, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        save.setBackground(null);
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mx/edu/ittepic/automatas/assets/save.png"))); // NOI18N
        save.setToolTipText("Guardar");
        save.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        save.setContentAreaFilled(false);
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        getContentPane().add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 0, -1, -1));

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

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(772, 40, 340, 430));

        jMenuBar1.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N

        jMenu1.setText("Archivo");
        jMenu1.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem1.setText("Abrir archivo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem12.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem12.setText("Guardar");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        jMenuItem2.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem2.setText("Salir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Editar");
        jMenu3.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N

        jMenuItem7.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem7.setText("Copiar");
        jMenu3.add(jMenuItem7);

        jMenuItem8.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem8.setText("Cortar");
        jMenu3.add(jMenuItem8);

        jMenuItem9.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem9.setText("Pegar");
        jMenu3.add(jMenuItem9);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Ejecutar");
        jMenu4.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N

        jMenuItem10.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem10.setText("Analizar");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem11.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jMenuItem11.setText("Compilar");
        jMenu4.add(jMenuItem11);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Informacion");
        jMenu2.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
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
        

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        
        showMessageDialog(null, "Analizador Lexico elaborado por: \nCasillas Ureña Fermin Michel\n"
                + "Murillo Macias Manuel Alejandro");
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
                
                   fw= new FileWriter(fileName+".abys");
            }
            catch(IOException io)
            {
                  javax.swing.JOptionPane.showMessageDialog(null, "Error en crear el archivo");
            }
 
            //Escribimos
            try
            {
                
                  fw.write(textPane.getText());
                  showMessageDialog(null,"Fichero guardado");
            }
 
            catch(IOException io)
            {
                  showMessageDialog(null, "Error en la escrito del archivo.");
            }
 
            //cerramos el fichero
            try
            {
                  fw.close();
            }
 
            catch(IOException io)
            {
                  showMessageDialog(null, "Error al cerrar el archivo");
            }             
      }
    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

        guardar();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

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

    }//GEN-LAST:event_openActionPerformed

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
    private javax.swing.JPopupMenu clicDMenu;
    private javax.swing.JButton compile;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
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
    private javax.swing.JButton open;
    private javax.swing.JButton save;
    private javax.swing.JFileChooser saveDialog;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextPane textPane;
    private javax.swing.JTextPane txtPane;
    // End of variables declaration//GEN-END:variables
    
}

