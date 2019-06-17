
package pkgfinal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;

/**
 *
 * @author Mihai
 */
public class PlataCuOraV1 extends javax.swing.JFrame {

    public PlataCuOraV1() {
        initComponents();
    }

        public void truncateExcel() throws IOException{
        
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        
        DefaultTableModel modelGenerare = (DefaultTableModel)jTable_generarePlata.getModel();
        
        Workbook workbook = WorkbookFactory.create(new File("Output plata cu ora/output_plata_cu_ora.xlsx"));    
            
        Sheet sheet = workbook.getSheetAt(0);
        
        Iterator<Row> rowIterator = sheet.rowIterator();
        
        Row row = rowIterator.next();
       
        while(rowIterator.hasNext()){
             XWPFDocument document = new XWPFDocument();      
        
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        
        XWPFRun run = paragraph.createRun();
        
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(document, sectPr);

        XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);

        paragraph = header.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);

        CTTabStop tabStop = paragraph.getCTP().getPPr().addNewTabs().addNewTab();
        tabStop.setVal(STTabJc.RIGHT);
        int twipsPerInch =  1440;
        tabStop.setPos(BigInteger.valueOf(6 * twipsPerInch));
 
        String imgFile="Input plata cu ora/logo.png";
        try {
            run.addPicture(new FileInputStream(imgFile), XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(425), Units.toEMU(100));
            run.addBreak();
        } catch (InvalidFormatException ex) {
            Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        XWPFParagraph paragraph1 = document.createParagraph();
        paragraph1.setAlignment(ParagraphAlignment.LEFT);
        
        XWPFRun run1 = paragraph1.createRun();
        
        run1.setText("DEPARTAMENTUL DE INFORMATICA");
        run1.addBreak();
        
        XWPFParagraph paragraph3 = document.createParagraph();
        paragraph3.setAlignment(ParagraphAlignment.CENTER);
            
        XWPFRun run3 = paragraph3.createRun();
        
        XWPFParagraph paragraph2 = document.createParagraph();
        paragraph2.setAlignment(ParagraphAlignment.LEFT);
            
        XWPFRun run2 = paragraph2.createRun();
               
            XWPFTable table = document.createTable();
            XWPFTableRow tableRow = table.createRow();
            tableRow.getCell(0).setText("Disciplina");
            tableRow.addNewTableCell().setText("Tipul de activitate");
            tableRow.addNewTableCell().setText("Seria/grupa/semigrupa");
            tableRow.addNewTableCell().setText("Nr. de ore");
            tableRow.addNewTableCell().setText("Date");
            tableRow.addNewTableCell().setText("Interval orar");
            
            row = rowIterator.next();
            
            Iterator<Cell> cellIterator = row.cellIterator();
            if(cellIterator.hasNext()){
                cellIterator.next();
                Cell nume = cellIterator.next();
            
                int rowNum = 2;
                for(int i = 0; i < modelGenerare.getRowCount(); i++){
                    if(modelGenerare.getValueAt(i, 1).toString().equals(nume.toString())){
                        XWPFTableRow tableRow1 = table.insertNewTableRow(rowNum++);
                        tableRow1.addNewTableCell().setText(modelGenerare.getValueAt(i, 5).toString());
                        tableRow1.addNewTableCell().setText(modelGenerare.getValueAt(i, 6).toString());
                        tableRow1.addNewTableCell().setText(modelGenerare.getValueAt(i, 7).toString());
                        if(modelGenerare.getValueAt(i, 9).toString().equals("0")){
                            tableRow1.addNewTableCell().setText(modelGenerare.getValueAt(i, 10).toString());
                        }
                        else{
                            tableRow1.addNewTableCell().setText(modelGenerare.getValueAt(i, 9).toString());
                        }
                        String data = "";
                        for(int j = 13; j<=17; j++){
                            if(!modelGenerare.getValueAt(i, j).toString().equals("0")){
                                data = data+modelGenerare.getValueAt(i, j).toString()+",";
                            }
                        }
                        if(data.length()>1)
                            data = data.substring(0, data.length() - 1);
                        tableRow1.addNewTableCell().setText(data+"-0"+month+"-"+year);
                        tableRow1.addNewTableCell().setText(modelGenerare.getValueAt(i, 18).toString());     
                    }
                }        
            
                run2.setText("Numele cadrului didactic: "+nume);
                run2.addBreak();
                table.removeRow(0);
                run3.setText("RAPORT PENTRU PLATA CU ORA \n"+"PE LUNA 0"+month+" ANUL "+year);
                run3.addBreak();

            
                FileOutputStream out = new FileOutputStream(new File("Output plata cu ora/"+nume.toString()+".docx"));
                document.write(out);
                out.close();          
            }
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_generarePlata = new javax.swing.JTable();
        jButton_generarePlata = new javax.swing.JButton();
        jLabel_numeEmail = new javax.swing.JLabel();
        jTextField_numeEmail = new javax.swing.JTextField();
        jLabel_parolaEmail = new javax.swing.JLabel();
        jPasswordField_parolaEmail = new javax.swing.JPasswordField();
        jButton_rapoarteIndividuale = new javax.swing.JButton();
        jButton_inapoi = new javax.swing.JButton();
        jLabel_background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1250, 500));
        setSize(new java.awt.Dimension(1250, 500));

        jPanel1.setLayout(null);

        jTable_generarePlata.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data contract", "Titular", "Titlu", "Statut", "Stat", "Titlu curs", "C/S/L/P", "S/GR/LAB", "OC", "Curs", "Sem", "Ore", "Ziua sapt", "s1", "s2", "s3", "s4", "s5", "Interval", "Sapt pare/impare", "E-mail", "Telefon", "Obs."
            }
        ));
        jScrollPane1.setViewportView(jTable_generarePlata);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 930, 440);

        jButton_generarePlata.setBackground(new java.awt.Color(255, 255, 255));
        jButton_generarePlata.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton_generarePlata.setText("Generare plata cu ora");
        jButton_generarePlata.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_generarePlataMouseClicked(evt);
            }
        });
        jPanel1.add(jButton_generarePlata);
        jButton_generarePlata.setBounds(950, 60, 250, 36);

        jLabel_numeEmail.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel_numeEmail.setText("Nume e-mail:");
        jPanel1.add(jLabel_numeEmail);
        jLabel_numeEmail.setBounds(950, 120, 120, 22);
        jPanel1.add(jTextField_numeEmail);
        jTextField_numeEmail.setBounds(1080, 120, 150, 30);

        jLabel_parolaEmail.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel_parolaEmail.setText("Parola e-mail:");
        jPanel1.add(jLabel_parolaEmail);
        jLabel_parolaEmail.setBounds(950, 170, 120, 24);
        jPanel1.add(jPasswordField_parolaEmail);
        jPasswordField_parolaEmail.setBounds(1080, 170, 150, 30);

        jButton_rapoarteIndividuale.setBackground(new java.awt.Color(255, 255, 255));
        jButton_rapoarteIndividuale.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton_rapoarteIndividuale.setText("Trimite rapoarte individuale");
        jButton_rapoarteIndividuale.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_rapoarteIndividualeMouseClicked(evt);
            }
        });
        jPanel1.add(jButton_rapoarteIndividuale);
        jButton_rapoarteIndividuale.setBounds(950, 230, 260, 36);

        jButton_inapoi.setBackground(new java.awt.Color(255, 255, 255));
        jButton_inapoi.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jButton_inapoi.setText("Inapoi");
        jButton_inapoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_inapoiMouseClicked(evt);
            }
        });
        jPanel1.add(jButton_inapoi);
        jButton_inapoi.setBounds(1125, 420, 100, 28);

        jLabel_background.setIcon(new javax.swing.ImageIcon("D:\\Documents\\FMI\\Anul 3\\Licenta\\Final\\1250.jpg")); // NOI18N
        jPanel1.add(jLabel_background);
        jLabel_background.setBounds(0, 0, 1250, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1250, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_generarePlataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_generarePlataMouseClicked
        try {
            
            Object[] cells = new Object[22];
            
            ArrayList<ZileLibere> zileLibere = new ArrayList<>();
            zileLibere.add(new ZileLibere(1,1));
            zileLibere.add(new ZileLibere(2,1));
            zileLibere.add(new ZileLibere(24,1));
            zileLibere.add(new ZileLibere(26,4));
            zileLibere.add(new ZileLibere(28,4));
            zileLibere.add(new ZileLibere(29,4));
            zileLibere.add(new ZileLibere(1,5));
            zileLibere.add(new ZileLibere(2,5));
            zileLibere.add(new ZileLibere(3,5));
            zileLibere.add(new ZileLibere(1,6));
            zileLibere.add(new ZileLibere(16,6));
            zileLibere.add(new ZileLibere(17,6));
            zileLibere.add(new ZileLibere(15,8));
            zileLibere.add(new ZileLibere(30,11));
            zileLibere.add(new ZileLibere(1,12));
            zileLibere.add(new ZileLibere(25,12));
            zileLibere.add(new ZileLibere(26,12));
            
            ArrayList<Saptamana> saptamana = new ArrayList<>();
            saptamana.add(new Saptamana(1,2));
            saptamana.add(new Saptamana(3,3));
            saptamana.add(new Saptamana(7,4));
            saptamana.add(new Saptamana(11,5));
            
            Calendar cal = Calendar.getInstance();
            
            DefaultTableModel modelGenerare = (DefaultTableModel)jTable_generarePlata.getModel();
            
            Workbook workbook = WorkbookFactory.create(new File("Input plata cu ora/input_plata_cu_ora_orar.xlsx"));
            Workbook workbook1 = WorkbookFactory.create(new File("Input plata cu ora/input_plata_cu_ora_persoane.xlsx"));
            Workbook workbook2 = new XSSFWorkbook();
            
            Sheet sheet = workbook.getSheetAt(0);
            Sheet sheet1 = workbook1.getSheetAt(0);
            Sheet sheet2 = workbook2.createSheet("Angajati");
            
            Font headerFont = workbook2.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            
            CellStyle headerCellStyle = workbook2.createCellStyle();
            headerCellStyle.setFont(headerFont);
            
            Iterator<Row> rowIterator = sheet.rowIterator();
            Iterator<Row> rowIterator1 = sheet1.rowIterator();
            
            Row row = rowIterator.next();
            Row row1 = rowIterator1.next();
            Row headerRow = sheet2.createRow(0);
            
            cells[9] = 0;
            cells[10] = 0;
            cells[13] = 0;
            cells[14] = 0;
            cells[15] = 0;
            cells[16] = 0;
            cells[17] = 0;
            
            while (rowIterator.hasNext()) {
                row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();

                if(cellIterator.hasNext()){
                cells[1] = cellIterator.next();
            
                int i=4;
            
                while (i <= 8) {
                    Cell cell = cellIterator.next();
                    cell.setCellType(CellType.STRING);
                    cells[i] = cell;
                    i++;
                }
            
                Cell cell = cellIterator.next();
                cell.setCellType(CellType.STRING);
                cells[11] = cell;
                cells[12] = cellIterator.next();
                cells[18] = cellIterator.next();
                cells[19] = cellIterator.next();               
            
                modelGenerare.addRow(cells);
                }
            }
            
            while(rowIterator1.hasNext()){
                //row1 = rowIterator1.next();
                
                Iterator<Cell> cellIterator1 = row1.cellIterator();
                
                if(cellIterator1.hasNext()){
                                    
                    Cell data = cellIterator1.next();
                    Cell nume = cellIterator1.next();
                    Cell titlu = cellIterator1.next();
                    Cell statut = cellIterator1.next();
                    Cell email = cellIterator1.next();
                    Cell telefon = cellIterator1.next();
                    telefon.setCellType(CellType.STRING);
                    Cell obs = cellIterator1.next();
                
                    for(int i = 0; i < modelGenerare.getRowCount(); i++){
                        if(nume.toString().equals(modelGenerare.getValueAt(i,1).toString())){
                            modelGenerare.setValueAt(data, i, 0);
                            modelGenerare.setValueAt(titlu, i, 2);
                            modelGenerare.setValueAt(statut, i, 3);
                            modelGenerare.setValueAt(email, i, 20);
                            modelGenerare.setValueAt(telefon, i, 21);
                            modelGenerare.setValueAt(obs, i, 22);
                        }
                    }
                }
                row1 = rowIterator1.next();
            }
            
            for(int i = 0; i < modelGenerare.getRowCount(); i++){
                
                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int month = localDate.getMonthValue();
                
                if("L".equals(modelGenerare.getValueAt(i, 12).toString())){
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE), i, 13);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 14);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 15);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 16);
                    if(cal.get(Calendar.DATE)+28>31){
                        modelGenerare.setValueAt(0, i, 17);
                    }
                    else
                    {
                        modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 17);
                    }
                    for(int j=0; j<zileLibere.size(); j++){                  
                        if(zileLibere.get(j).getMonth()==month){
                            if(zileLibere.get(j).getDay()==(cal.get(Calendar.DATE)))
                                modelGenerare.setValueAt(0, i, 13);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+7)
                                modelGenerare.setValueAt(0, i, 14);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+14)
                                modelGenerare.setValueAt(0, i, 15);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+21)
                                modelGenerare.setValueAt(0, i, 16);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+28)
                                modelGenerare.setValueAt(0, i, 17);
                                                   
                    }
                }
                    
                     for(int j=0; j<zileLibere.size(); j++){
                        if(zileLibere.get(j).getMonth()==month && zileLibere.get(j).getDay() == cal.get(Calendar.DATE)){
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 13);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 14);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 15);
                            if(cal.get(Calendar.DATE)+28>31){
                                modelGenerare.setValueAt(0, i, 16);
                            }
                            else
                            {
                                modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 16);
                            }
                        }
                    }
                    
                    for(int j=0; j<saptamana.size(); j++){
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") 
                                && modelGenerare.getValueAt(i, 19).toString().equals("p")){
                            if(saptamana.get(j).getStartOfMonth()%2==0){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") 
                                && modelGenerare.getValueAt(i, 19).toString().equals("I")){
                            if(saptamana.get(j).getStartOfMonth()%2==1){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getStartOfMonth()==11){
                            modelGenerare.setValueAt(0, i, 17);
                        }
                    }
                }
                
                if("MA".equals(modelGenerare.getValueAt(i, 12).toString())){
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE), i, 13);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 14);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 15);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 16);
                    if(cal.get(Calendar.DATE)+28>31){
                        modelGenerare.setValueAt(0, i, 17);
                    }
                    else
                    {
                        modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 17);
                    }
                    for(int j=0; j<zileLibere.size(); j++){                  
                        if(zileLibere.get(j).getMonth()==month){
                            if(zileLibere.get(j).getDay()==(cal.get(Calendar.DATE)))
                                modelGenerare.setValueAt(0, i, 13);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+7)
                                modelGenerare.setValueAt(0, i, 14);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+14)
                                modelGenerare.setValueAt(0, i, 15);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+21)
                                modelGenerare.setValueAt(0, i, 16);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+28)
                                modelGenerare.setValueAt(0, i, 17);
                                                   
                    }
                }
                    
                     for(int j=0; j<zileLibere.size(); j++){
                        if(zileLibere.get(j).getMonth()==month && zileLibere.get(j).getDay() == cal.get(Calendar.DATE)){
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 13);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 14);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 15);
                            if(cal.get(Calendar.DATE)+28>31){
                                modelGenerare.setValueAt(0, i, 16);
                            }
                            else
                            {
                                modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 16);
                            }
                        }
                    }
                    
                    for(int j=0; j<saptamana.size(); j++){
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("p")){
                            if(saptamana.get(j).getStartOfMonth()%2==0){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("I")){
                            if(saptamana.get(j).getStartOfMonth()%2==1){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getStartOfMonth()==11){
                            modelGenerare.setValueAt(0, i, 17);
                        }
                    }
                }
                
                if("MI".equals(modelGenerare.getValueAt(i, 12).toString())){
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE), i, 13);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 14);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 15);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 16);
                    if(cal.get(Calendar.DATE)+28>31){
                        modelGenerare.setValueAt(0, i, 17);
                    }
                    else
                    {
                        modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 17);
                    }
                    for(int j=0; j<zileLibere.size(); j++){                  
                        if(zileLibere.get(j).getMonth()==month){
                            if(zileLibere.get(j).getDay()==(cal.get(Calendar.DATE)))
                                modelGenerare.setValueAt(0, i, 13);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+7)
                                modelGenerare.setValueAt(0, i, 14);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+14)
                                modelGenerare.setValueAt(0, i, 15);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+21)
                                modelGenerare.setValueAt(0, i, 16);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+28)
                                modelGenerare.setValueAt(0, i, 17);
                                                   
                    }
                }
                    
                     for(int j=0; j<zileLibere.size(); j++){
                        if(zileLibere.get(j).getMonth()==month && zileLibere.get(j).getDay() == cal.get(Calendar.DATE)){
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 13);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 14);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 15);
                            if(cal.get(Calendar.DATE)+28>31){
                                modelGenerare.setValueAt(0, i, 16);
                            }
                            else
                            {
                                modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 16);
                            }
                        }
                    }
                    
                    for(int j=0; j<saptamana.size(); j++){
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("p")){
                            if(saptamana.get(j).getStartOfMonth()%2==0){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("I")){
                            if(saptamana.get(j).getStartOfMonth()%2==1){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getStartOfMonth()==11){
                            modelGenerare.setValueAt(0, i, 17);
                        }
                    }
                }
                
                if("J".equals(modelGenerare.getValueAt(i, 12).toString())){
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE), i, 13);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 14);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 15);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 16);
                    if(cal.get(Calendar.DATE)+28>31){
                        modelGenerare.setValueAt(0, i, 17);
                    }
                    else
                    {
                        modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 17);
                    }
                    for(int j=0; j<zileLibere.size(); j++){                  
                        if(zileLibere.get(j).getMonth()==month){
                            if(zileLibere.get(j).getDay()==(cal.get(Calendar.DATE)))
                                modelGenerare.setValueAt(0, i, 13);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+7)
                                modelGenerare.setValueAt(0, i, 14);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+14)
                                modelGenerare.setValueAt(0, i, 15);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+21)
                                modelGenerare.setValueAt(0, i, 16);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+28)
                                modelGenerare.setValueAt(0, i, 17);
                                                   
                    }
                }
                    
                     for(int j=0; j<zileLibere.size(); j++){
                        if(zileLibere.get(j).getMonth()==month && zileLibere.get(j).getDay() == cal.get(Calendar.DATE)){
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 13);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 14);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 15);
                            if(cal.get(Calendar.DATE)+28>31){
                                modelGenerare.setValueAt(0, i, 16);
                            }
                            else
                            {
                                modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 16);
                            }
                        }
                    }
                    
                    for(int j=0; j<saptamana.size(); j++){
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("p")){
                            if(saptamana.get(j).getStartOfMonth()%2==0){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("I")){
                            if(saptamana.get(j).getStartOfMonth()%2==1){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getStartOfMonth()==11){
                            modelGenerare.setValueAt(0, i, 17);
                        }
                    }
                }
                
                if("V".equals(modelGenerare.getValueAt(i, 12).toString())||"VI".equals(modelGenerare.getValueAt(i, 12).toString())){
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE), i, 13);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 14);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 15);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 16);
                    if(cal.get(Calendar.DATE)+28>31){
                        modelGenerare.setValueAt(0, i, 17);
                    }
                    else
                    {
                        modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 17);
                    }
                    for(int j=0; j<zileLibere.size(); j++){                  
                        if(zileLibere.get(j).getMonth()==month){
                            if(zileLibere.get(j).getDay()==(cal.get(Calendar.DATE)))
                                modelGenerare.setValueAt(0, i, 13);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+7)
                                modelGenerare.setValueAt(0, i, 14);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+14)
                                modelGenerare.setValueAt(0, i, 15);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+21)
                                modelGenerare.setValueAt(0, i, 16);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+28)
                                modelGenerare.setValueAt(0, i, 17);
                                                   
                    }
                }
                    
                     for(int j=0; j<zileLibere.size(); j++){
                        if(zileLibere.get(j).getMonth()==month && zileLibere.get(j).getDay() == cal.get(Calendar.DATE)){
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 13);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 14);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 15);
                            if(cal.get(Calendar.DATE)+28>31){
                                modelGenerare.setValueAt(0, i, 16);
                            }
                            else
                            {
                                modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 16);
                            }
                        }
                    }
                    
                    for(int j=0; j<saptamana.size(); j++){
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("p")){
                            if(saptamana.get(j).getStartOfMonth()%2==0){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("I")){
                            if(saptamana.get(j).getStartOfMonth()%2==1){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getStartOfMonth()==11){
                            modelGenerare.setValueAt(0, i, 17);
                        }
                    }
                }
                
                if("S".equals(modelGenerare.getValueAt(i, 12).toString())){
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE), i, 13);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 14);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 15);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 16);
                    if(cal.get(Calendar.DATE)+28>31){
                        modelGenerare.setValueAt(0, i, 17);
                    }
                    else
                    {
                        modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 17);
                    }
                    for(int j=0; j<zileLibere.size(); j++){                  
                        if(zileLibere.get(j).getMonth()==month){
                            if(zileLibere.get(j).getDay()==(cal.get(Calendar.DATE)))
                                modelGenerare.setValueAt(0, i, 13);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+7)
                                modelGenerare.setValueAt(0, i, 14);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+14)
                                modelGenerare.setValueAt(0, i, 15);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+21)
                                modelGenerare.setValueAt(0, i, 16);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+28)
                                modelGenerare.setValueAt(0, i, 17);
                                                   
                    }
                }
                    
                     for(int j=0; j<zileLibere.size(); j++){
                        if(zileLibere.get(j).getMonth()==month && zileLibere.get(j).getDay() == cal.get(Calendar.DATE)){
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 13);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 14);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 15);
                            if(cal.get(Calendar.DATE)+28>31){
                                modelGenerare.setValueAt(0, i, 16);
                            }
                            else
                            {
                                modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 16);
                            }
                        }
                    }
                    
                    for(int j=0; j<saptamana.size(); j++){
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("p")){
                            if(saptamana.get(j).getStartOfMonth()%2==0){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("I")){
                            if(saptamana.get(j).getStartOfMonth()%2==1){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getStartOfMonth()==11){
                            modelGenerare.setValueAt(0, i, 17);
                        }
                    }
                }
                
                if("D".equals(modelGenerare.getValueAt(i, 12).toString())){
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE), i, 13);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 14);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 15);
                    modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 16);
                    if(cal.get(Calendar.DATE)+28>31){
                        modelGenerare.setValueAt(0, i, 17);
                    }
                    else
                    {
                        modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 17);
                    }
                    for(int j=0; j<zileLibere.size(); j++){                  
                        if(zileLibere.get(j).getMonth()==month){
                            if(zileLibere.get(j).getDay()==(cal.get(Calendar.DATE)))
                                modelGenerare.setValueAt(0, i, 13);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+7)
                                modelGenerare.setValueAt(0, i, 14);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+14)
                                modelGenerare.setValueAt(0, i, 15);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+21)
                                modelGenerare.setValueAt(0, i, 16);
                            if(zileLibere.get(j).getDay()==cal.get(Calendar.DATE)+28)
                                modelGenerare.setValueAt(0, i, 17);
                                                   
                    }
                }
                    
                     for(int j=0; j<zileLibere.size(); j++){
                        if(zileLibere.get(j).getMonth()==month && zileLibere.get(j).getDay() == cal.get(Calendar.DATE)){
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+7, i, 13);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+14, i, 14);
                            modelGenerare.setValueAt(cal.get(Calendar.DATE)+21, i, 15);
                            if(cal.get(Calendar.DATE)+28>31){
                                modelGenerare.setValueAt(0, i, 16);
                            }
                            else
                            {
                                modelGenerare.setValueAt(cal.get(Calendar.DATE)+28, i, 16);
                            }
                        }
                    }
                    
                    for(int j=0; j<saptamana.size(); j++){
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("p")){
                            if(saptamana.get(j).getStartOfMonth()%2==0){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getMonth()==month && modelGenerare.getValueAt(i, 11).toString().equals("1") && modelGenerare.getValueAt(i, 19).toString().equals("I")){
                            if(saptamana.get(j).getStartOfMonth()%2==1){
                                //modelGenerare.setValueAt(0, i, 13);  
                                modelGenerare.setValueAt(0, i, 14);                           
                                //modelGenerare.setValueAt(0, i, 15);                
                                modelGenerare.setValueAt(0, i, 16);                          
                                //modelGenerare.setValueAt(0, i, 17);
                            }
                            else{
                                modelGenerare.setValueAt(0, i, 13);  
                                //modelGenerare.setValueAt(0, i, 14);                           
                                modelGenerare.setValueAt(0, i, 15);                
                                //modelGenerare.setValueAt(0, i, 16);                          
                                modelGenerare.setValueAt(0, i, 17);
                            }                            
                        }
                        if(saptamana.get(j).getStartOfMonth()==11){
                            modelGenerare.setValueAt(0, i, 17);
                        }
                    }
                    }
            }
            
            for(int i=0; i < modelGenerare.getRowCount(); i++){
                if("C".equals(modelGenerare.getValueAt(i, 6).toString())){
                    int count = 0;
                    for(int j = 13; j <= 17 ; j++){
                        if(!"0".equals(modelGenerare.getValueAt(i, j).toString()))
                            count++;
                    }
                    XSSFCell ore = (XSSFCell) modelGenerare.getValueAt(i,11);
                    modelGenerare.setValueAt(Integer.parseInt(ore.getStringCellValue())*count, i, 9);
                }else{
                    int count = 0;
                    for(int j = 13; j <= 17 ; j++){
                        if(!"0".equals(modelGenerare.getValueAt(i, j).toString()))
                            count++;
                    }
                    if(modelGenerare.getValueAt(i, 19).toString().equals("I")||modelGenerare.getValueAt(i, 19).toString().equals("p")){
                        modelGenerare.setValueAt(2*count, i, 10);
                    }
                    else{
                        XSSFCell ore = (XSSFCell) modelGenerare.getValueAt(i,11);
                        modelGenerare.setValueAt(Integer.parseInt(ore.getStringCellValue())*count, i, 10);
                    }
                }
            }
            
            int counterS1 = 0;
            int counterS2 = 0;
            int counterS3 = 0;
            int counterS4 = 0;
            int counterS5 = 0;
            
            for(int i = 0; i < modelGenerare.getRowCount(); i++) {
                if(!modelGenerare.getValueAt(i, 13).toString().equals("0")){
                    counterS1++;
                } 
                if(!modelGenerare.getValueAt(i, 14).toString().equals("0")){
                    counterS2++;
                } 
                if(!modelGenerare.getValueAt(i, 15).toString().equals("0")){
                    counterS3++;
                } 
                if(!modelGenerare.getValueAt(i, 16).toString().equals("0")){
                    counterS4++;
                } 
                if(!modelGenerare.getValueAt(i, 17).toString().equals("0")){
                    counterS5++;
                }                   
            }
            
            for(int i = 13; i <= 17; i++){
                if(counterS1==0){
                    sheet2.setColumnHidden(13, true);
                }
                if(counterS2==0){
                    sheet2.setColumnHidden(14, true);
                }
                if(counterS3==0){
                    sheet2.setColumnHidden(15, true);
                }
                if(counterS4==0){
                    sheet2.setColumnHidden(16, true);
                }
                if(counterS5==0){
                    sheet2.setColumnHidden(17, true);
                }
            }
            
            DefaultTableModel modelGenerare1 = (DefaultTableModel)jTable_generarePlata.getModel();
            
            for(int i = 0; i < modelGenerare1.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(modelGenerare1.getColumnName(i));
                cell.setCellStyle(headerCellStyle);
            }
            
            int rowNum = 1;
            for(int i = 0; i < modelGenerare1.getRowCount(); i++) {
                Row row2 = sheet2.createRow(rowNum++);
               
                row2.createCell(0).setCellValue(modelGenerare1.getValueAt(i, 0).toString());
                row2.createCell(1).setCellValue(modelGenerare1.getValueAt(i, 1).toString());
                row2.createCell(2).setCellValue(modelGenerare1.getValueAt(i, 2).toString());
                row2.createCell(3).setCellValue(modelGenerare1.getValueAt(i, 3).toString());
                row2.createCell(4).setCellValue(modelGenerare1.getValueAt(i, 4).toString());
                row2.createCell(5).setCellValue(modelGenerare1.getValueAt(i, 5).toString());
                row2.createCell(6).setCellValue(modelGenerare1.getValueAt(i, 6).toString());
                row2.createCell(7).setCellValue(modelGenerare1.getValueAt(i, 7).toString());
                row2.createCell(8).setCellValue(modelGenerare1.getValueAt(i, 8).toString());
                row2.createCell(9).setCellValue((Integer) modelGenerare1.getValueAt(i, 9));
                row2.createCell(10).setCellValue((Integer) modelGenerare1.getValueAt(i, 10));
                row2.createCell(11).setCellValue(modelGenerare1.getValueAt(i, 11).toString());
                row2.createCell(12).setCellValue(modelGenerare1.getValueAt(i, 12).toString());
                row2.createCell(13).setCellValue(modelGenerare1.getValueAt(i, 13).toString());
                row2.createCell(14).setCellValue(modelGenerare1.getValueAt(i, 14).toString());
                row2.createCell(15).setCellValue(modelGenerare1.getValueAt(i, 15).toString());
                row2.createCell(16).setCellValue(modelGenerare1.getValueAt(i, 16).toString());
                row2.createCell(17).setCellValue(modelGenerare1.getValueAt(i, 17).toString());
                row2.createCell(18).setCellValue(modelGenerare1.getValueAt(i, 18).toString());  
                row2.createCell(19).setCellValue(modelGenerare1.getValueAt(i, 19).toString());
                row2.createCell(20).setCellValue(modelGenerare1.getValueAt(i, 20).toString());
                row2.createCell(21).setCellValue(modelGenerare1.getValueAt(i, 21).toString());
                row2.createCell(22).setCellValue(modelGenerare1.getValueAt(i, 22).toString());               
            }
            
            for(int i = 0; i < modelGenerare1.getColumnCount(); i++) {
                sheet2.autoSizeColumn(i);
            }
            
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream("Output plata cu ora/output_plata_cu_ora.xlsx");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                workbook2.write(fileOut);
                JOptionPane.showMessageDialog(null, "Fisierul excel a fost creat cu succes!");
            } catch (IOException ex) {
                Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fileOut.close();
            } catch (IOException ex) {
                Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                workbook.close();
                workbook1.close();
                workbook2.close();
            } catch (IOException ex) {
                Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
            }
  
        } catch (IOException | EncryptedDocumentException ex) {
            Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
        }          
        
        try {      
            truncateExcel();
        } catch (IOException ex) {
            Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }//GEN-LAST:event_jButton_generarePlataMouseClicked

    private void jButton_rapoarteIndividualeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_rapoarteIndividualeMouseClicked
        String username = jTextField_numeEmail.getText();
        String password = jPasswordField_parolaEmail.getText();       
        String from = username;  
        String to = "chiticmihai97@gmail.com";
                
        Workbook workbook1;
        
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        
        try {
            workbook1 = WorkbookFactory.create(new File("Input plata cu ora/input_plata_cu_ora_persoane.xlsx"));
            
            Sheet sheet1 = workbook1.getSheetAt(0);
            
            Iterator<Row> rowIterator1 = sheet1.rowIterator();
            
            Row row1 = rowIterator1.next();
            
            Properties properties = System.getProperties();     
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.host", "smtp.gmail.com");
            properties.setProperty("mail.smtp.port", "587");
            
            Session session = Session.getDefaultInstance(properties,
                                new javax.mail.Authenticator(){
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication(){
                                return new PasswordAuthentication(username, password); 
                                }
                            });
            
            try{
            while(rowIterator1.hasNext()){
                row1 = rowIterator1.next();
                
                Iterator<Cell> cellIterator = row1.cellIterator();
                
                if(cellIterator.hasNext()){
                   cellIterator.next();
                Cell nume = cellIterator.next();
                
                File file = new File("Output plata cu ora/"+nume.toString()+".docx");
                
                    if(file.exists()){
                        MimeMessage message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(from));
                        message.addRecipients(Message.RecipientType.TO, to);
                        message.setSubject("Plata cu ora pentru luna "+month+" anul "+year);
                        BodyPart messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setText("Buna ziua,\n\nV-am atasat raportul individual pentru plata cu ora pentru luna "
                                +month+" anul "+year+".");
                        Multipart multipart = new MimeMultipart();
                        multipart.addBodyPart(messageBodyPart);
                        messageBodyPart = new MimeBodyPart();
                        String filename = "Output plata cu ora/"+nume.toString()+".docx";
                        DataSource source = new FileDataSource(filename);
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(filename);
                        multipart.addBodyPart(messageBodyPart);
                        message.setContent(multipart);
                        Transport.send(message);
                    } 
                }         
            }          
            } catch(MessagingException me){
            }
            
        } catch (IOException | EncryptedDocumentException ex) {
            Logger.getLogger(PlataCuOra.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }//GEN-LAST:event_jButton_rapoarteIndividualeMouseClicked

    private void jButton_inapoiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_inapoiMouseClicked
        Meniu meniu = new Meniu();
        meniu.setVisible(true);  
        this.dispose();
    }//GEN-LAST:event_jButton_inapoiMouseClicked

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
            java.util.logging.Logger.getLogger(PlataCuOraV1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlataCuOraV1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlataCuOraV1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlataCuOraV1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlataCuOraV1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_generarePlata;
    private javax.swing.JButton jButton_inapoi;
    private javax.swing.JButton jButton_rapoarteIndividuale;
    private javax.swing.JLabel jLabel_background;
    private javax.swing.JLabel jLabel_numeEmail;
    private javax.swing.JLabel jLabel_parolaEmail;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField_parolaEmail;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_generarePlata;
    private javax.swing.JTextField jTextField_numeEmail;
    // End of variables declaration//GEN-END:variables
}