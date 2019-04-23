
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Mahasiswa extends javax.swing.JFrame {
    int idBaris = 0;
    String role;
    DefaultTableModel model;
    
    public Mahasiswa() {
        initComponents();
        
        aturModelTabel();
        showForm(false);
        showData("");
    }
    private void aturModelTabel(){ 
        Object[] kolom = {"NIM","NAMA","KELAS","ANGKATAN","IPK"};
        model = new DefaultTableModel(kolom,0);
       
        tbl_datamhs.setModel(model);  
        tbl_datamhs.setRowHeight(20);  

    } 
    private void showForm(boolean b){   
        areaSplit.setDividerLocation(0.3);  
        areaSplit.getLeftComponent().setVisible(b);   
    } 
    private void resetForm(){    
          tbl_datamhs.clearSelection();  
          txtnim.setText("");       
          txtnama.setText("");         
          txtkelas.setText("");       
          txtangkatan.setText(""); 
          txtipk.setText(""); 
          txtnim.requestFocus();         
    }   
    
    private void showData(String key){ 
        model.getDataVector().removeAllElements();
        String where = "";     
        if(!key.isEmpty()){    
            where += "WHERE nim LIKE '%"+key+"%' "  
                    + "OR nama LIKE '%"+key+"%' "     
                    + "OR kelas LIKE '%"+key+"%' "  
                    + "OR angkatan LIKE '%"+key+"%' "      
                    + "OR ipk LIKE '%"+key+"%'";
        } 
         String sql = "SELECT * FROM data_mahasiswa "+where;    
         Connection con;      
         Statement st;        
         ResultSet rs;   
         int baris = 0; 
          try {    
              con = DBKoneksi.getKoneksi();
               st = con.createStatement();  
               rs = st.executeQuery(sql);      
          while (rs.next()) {               
                   
                   Object nim = rs.getString(1);  
                   Object nama = rs.getString(2);   
                   Object kelas = rs.getString(3);  
                   Object angkatan = rs.getString(4);  
                   Object ipk = rs.getString(5);     
                   Object[] data = {nim,nama,kelas,angkatan,ipk};   
                   model.insertRow(baris, data);
                   baris++;    
          } 
           st.close();       
           con.close();        
           tbl_datamhs.revalidate();     
           tbl_datamhs.repaint();     
        } catch (SQLException e) { 
            System.err.println("showData(): "+e.getMessage());   
        }   
    }
    private void resetView(){   
        resetForm();         
        showForm(false);      
        showData("");
        if ((int)tbl_datamhs.getRowCount() == -1) {
            btnHapus.setEnabled(false);   
        }else{
            btnHapus.setEnabled(true);   
        }
        
        idBaris = 0; 
    }   
     private void pilihData(String n){ 
         btnHapus.setEnabled(true);     
         String sql = "SELECT * FROM data_mahasiswa WHERE id='"+n+"'";    
         Connection con;      
         Statement st;       
         ResultSet rs;      
         try {           
             con = DBKoneksi.getKoneksi();
             st = con.createStatement();     
             rs = st.executeQuery(sql);       
             while (rs.next()) {              
                 String nim = rs.getString(1);     
                 String nama = rs.getString(2);       
                 String kelas = rs.getString(3);
                 String angkatan = rs.getString(4);
                 String ipk = rs.getString(5);       
                         
                 txtnim.setText(nim);   
                 txtnama.setText(nama);   
                 txtkelas.setText(kelas);
                 txtangkatan.setText(angkatan);
                 txtipk.setText(ipk);          
             }            
             st.close();   
             con.close();   
             showForm(true);    
         } catch (SQLException e) {    
             System.err.println("pilihData(): "+e.getMessage());       
         }  
     }   
     private void simpanData(){ 
         String nim = txtnim.getText();
         String nama = txtnama.getText();   
         String kelas = txtkelas.getText();
         String angkatan = txtangkatan.getText();  
         String ipk = txtipk.getText();      
         if(nim.isEmpty() || nama.isEmpty() || kelas.isEmpty() || angkatan.isEmpty() ||   
            ipk.isEmpty()){       
             JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
         }else{                
            String sql =  "INSERT INTO data_mahasiswa (nim,nama,kelas,"        
                        + "angkatan,ipk) "      
                        + "VALUES (\""+nim+"\",\""+nama+"\"," 
                        + "\""+kelas+"\",\""+angkatan+"\",\"" +ipk+ "\")"; 
    
             Connection con;    
             Statement st;       
             try {                
                 con = DBKoneksi.getKoneksi();  
                 
                 st = con.createStatement();       
                 st.executeUpdate(sql);            
                 st.close();              
                 con.close();              
                 resetView();            
                 JOptionPane.showMessageDialog(this,"Data telah  disimpan!");     
             } catch (SQLException e) {         
                 JOptionPane.showMessageDialog(this, e.getMessage());     
             }       
         }  
     } 
     private void ubahData(){  
         String nim = txtnim.getText();
         String nama = txtnama.getText();   
         String kelas = txtkelas.getText();
         String angkatan = txtangkatan.getText();  
         String ipk = txtipk.getText();        
         if(nim.isEmpty() || nama.isEmpty() || kelas.isEmpty() || angkatan.isEmpty() ||   
            ipk.isEmpty()){          
             JOptionPane.showMessageDialog(this, "Mohon lengkapi data!");
         }else{     
             String sql = "UPDATE data_mahasiswa "    
                     + "SET nim=\""+nim+"\","     
                     + "nama=\""+nama+"\","          
                     + "kelas=\""+kelas+"\","     
                     + "angkatan=\""+angkatan+"\","       
                     + "ipk=\""+ipk+"\" WHERE nim=\""+idBaris+"\"";  
             Connection con;        
             Statement st;        
             try {                
                 con = DBKoneksi.getKoneksi();     
                 st = con.createStatement();      
                 st.executeUpdate(sql);           
                 st.close();              
                 con.close();             
                 resetView();             
                 JOptionPane.showMessageDialog(this,"Data telah diubah!");   
             } catch (SQLException e) {            
                 JOptionPane.showMessageDialog(this, e.getMessage());  
             }      
         }   
     }
    private void hapusData(String nim ){      
        Connection con;       
        Statement st;       
        try {                  
            con = DBKoneksi.getKoneksi(); 
            st = con.createStatement();   
            st.executeUpdate("DELETE FROM data_mahasiswa WHERE nim = '"+ nim +"'");    
            st.close();          
            con.close();        
            resetView();        
            JOptionPane.showMessageDialog(this, "Data telah dihapus");    
        } catch (SQLException e) {     
            JOptionPane.showMessageDialog(this, e.getMessage()); 
        }    
    }
    
    public void updateData(String nim, String nama, String kelas, String angkatan, String ipk){
       String sql = "UPDATE data_mahasiswa SET Nama      = '"+ nama +"',"
                                  + "Kelas   = '"+ kelas +"',"
                                  + "Angkatan     = '"+ angkatan +"',"
                                  + "IPK     = '"+ ipk +"' WHERE nim = '"+ nim +"'";
       Connection con;
       Statement st;
       try {
           con = DBKoneksi.getKoneksi();
           st = con.createStatement();
           st.executeUpdate(sql);
           st.close();
           con.close();
           resetView();
        JOptionPane.showMessageDialog(this,"Data telah diupdate!" );
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(this, e.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        areaSplit = new javax.swing.JSplitPane();
        panelKiri = new javax.swing.JPanel();
        txt_nim = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtnim = new javax.swing.JTextField();
        txtnama = new javax.swing.JTextField();
        txtipk = new javax.swing.JTextField();
        btnTutup = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        txtkelas = new javax.swing.JTextField();
        txtangkatan = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_datamhs = new javax.swing.JTable();
        btnUpdate = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnTambah.setText("TAMBAH DATA");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setText("HAPUS DATA");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        btnCari.setText("CARI");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        panelKiri.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        txt_nim.setText("NIM");

        jLabel2.setText("NAMA");

        jLabel3.setText("KELAS");

        jLabel4.setText("ANGKATAN");

        jLabel5.setText("IPK");

        txtnama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnamaActionPerformed(evt);
            }
        });

        btnTutup.setText("TUTUP");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        txtangkatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtangkatanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKiriLayout = new javax.swing.GroupLayout(panelKiri);
        panelKiri.setLayout(panelKiriLayout);
        panelKiriLayout.setHorizontalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_nim)
                            .addComponent(jLabel2))
                        .addGap(54, 54, 54)
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnama)
                            .addComponent(txtnim))
                        .addContainerGap())
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTutup)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addComponent(txtkelas, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtipk)
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addComponent(btnSimpan)
                                        .addGap(0, 47, Short.MAX_VALUE))
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addComponent(txtangkatan)
                                        .addGap(52, 52, 52)))
                                .addContainerGap())))))
        );
        panelKiriLayout.setVerticalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nim)
                    .addComponent(txtnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtnama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtkelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtangkatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtipk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTutup)
                    .addComponent(btnSimpan))
                .addContainerGap(102, Short.MAX_VALUE))
        );

        areaSplit.setLeftComponent(panelKiri);

        tbl_datamhs.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tbl_datamhs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        jScrollPane2.setViewportView(tbl_datamhs);

        areaSplit.setRightComponent(jScrollPane2);

        btnUpdate.setText("UPDATE");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(areaSplit, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnHapus)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari)
                    .addComponent(btnUpdate))
                .addGap(32, 32, 32)
                .addComponent(areaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
       role = "Tambah";
       btnSimpan.setText("Simpan");
       idBaris = 0;
       resetForm();
        showForm(true);
        btnHapus.setEnabled(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        idBaris = tbl_datamhs.getSelectedRow();
        System.out.println(idBaris);
        if(idBaris == -1){
           JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
       }else{
           hapusData((String) tbl_datamhs.getModel().getValueAt(tbl_datamhs.getSelectedRow(), 0));
       }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if(role.equals("Tambah")){
            simpanData();
        }else if(role.equals("Ubah")){
            if ((int)tbl_datamhs.getSelectedRow() != -1) {
                String nim = (String) tbl_datamhs.getModel().getValueAt(tbl_datamhs.getSelectedRow(), 0);
                String nama = txtnama.getText();
                String kelas = txtkelas.getText();
                String angkatan = txtangkatan.getText();
                String ipk = txtipk.getText();
                updateData(nim, nama, kelas, angkatan, ipk);
            }else{
                JOptionPane.showMessageDialog(this, "Data Belum dipilih");
            }
        }
    }//GEN-LAST:event_btnSimpanActionPerformed
    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {                                        
        resetForm();
        showForm(false);
        btnHapus.setEnabled(false);
        idBaris = 0;
    }   
    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {                                        
        role = "Ubah";
        int row = tbl_datamhs.getRowCount();
        if (row > 0){
            int sel = tbl_datamhs.getSelectedRow();
            if(sel != -1){
                pilihData(tbl_datamhs.getValueAt(sel, 0).toString());
                btnSimpan.setText("UBAH DATA");
            }
        }
    } 
    private void txtnamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamaActionPerformed

    private void txtangkatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtangkatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtangkatanActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String Key = txtCari.getText();
        showData(Key);
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        role = "Ubah";
        btnSimpan.setText("Update");
        idBaris = 0;
        resetForm();
        showForm(true);
        btnHapus.setEnabled(false);
        txtnim.setEnabled(false);
    }//GEN-LAST:event_btnUpdateActionPerformed
        
    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {                                        
        String Key = txtCari.getText();
        showData(Key);
    }   
    private void formComponentResized(java.awt.event.ComponentEvent evt) {                                        
        areaSplit.setDividerLocation(0.3);
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
            java.util.logging.Logger.getLogger(Mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mahasiswa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mahasiswa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane areaSplit;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelKiri;
    private javax.swing.JTable tbl_datamhs;
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel txt_nim;
    private javax.swing.JTextField txtangkatan;
    private javax.swing.JTextField txtipk;
    private javax.swing.JTextField txtkelas;
    private javax.swing.JTextField txtnama;
    private javax.swing.JTextField txtnim;
    // End of variables declaration//GEN-END:variables
}
