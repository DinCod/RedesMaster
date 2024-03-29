package controler;

import Utilidades.ColoresCeldasInter;
import Utilidades.PlantillaPDF;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.Capitulo;
import model.Historial;
import model.Pregunta;
import model.Respuesta;
import model.Usuario;
import view.FormLogin;
import view.FormQuestions;

public class controlerQuestion extends MouseAdapter implements ActionListener {

    FormQuestions question;
    FormLogin login;
    Usuario user;
    Capitulo cap;
    Respuesta res;
    Pregunta pre;
    private Historial historial;
    String capitulo;
    int windowX = 0;
    int windowY = 220;
    int pregunta = 1;
    int numeroPregunta;
    int maximoNumero = 49;
    int arreglo[] = new int[maximoNumero];
    int regenerarArray = 0;
    int posicion;
    int numeroCapitulo;
    int buena = 0;
    int mala = 0;
    int notaEstudiante = 0;
    int insertarHistorial = 0;
    String historialDelUsuario = FormLogin.txtusername.getText();
    Date fecha;
    public controlerQuestion(FormQuestions question, FormLogin login) throws Exception {
        this.question = question;
        this.login = login;
        user = new Usuario();
        FormQuestions.btnNext.addActionListener(this);
        FormQuestions.btnIndice.addActionListener(this);
        FormQuestions.btnCap1.addActionListener(this);
        FormQuestions.btnCap2.addActionListener(this);
        FormQuestions.btnCap3.addActionListener(this);
        FormQuestions.btnCap4.addActionListener(this);
        FormQuestions.btnCap5.addActionListener(this);
        FormQuestions.btnCap6.addActionListener(this);
        FormQuestions.btnCap7.addActionListener(this);
        FormQuestions.btnCap8.addActionListener(this);
        FormQuestions.btnCap9.addActionListener(this);
        FormQuestions.btnCap10.addActionListener(this);
        FormQuestions.btnNext.addActionListener(this);
        FormQuestions.btnRegresar.addActionListener(this);
        FormQuestions.btnAceptarPizarra.addActionListener(this);
        FormQuestions.respuesta1.addMouseListener(this);
        FormQuestions.respuesta2.addMouseListener(this);
        FormQuestions.respuesta3.addMouseListener(this);
        FormQuestions.respuesta4.addMouseListener(this);
        FormQuestions.btnHistorial.addActionListener(this);
        FormQuestions.btnNextPregunta.addActionListener(this);
        FormQuestions.btnAleatorio.addActionListener(this);
        FormQuestions.btnSalir.addActionListener(this);
        FormQuestions.btnCerrarHistorial.addActionListener(this);
        FormQuestions.btnGenerarPDF.addActionListener(this);
        FormQuestions.btnAbrirPDF.addActionListener(this);
        question.PanelMenu.setVisible(false);
        question.PanelPreguntas.setVisible(false);
        question.PanelBotonesIndice.setOpaque(false);
        question.PanelRecuerda.setVisible(false);
        question.imagen2.setVisible(false);
        question.imagen1.setVisible(false);
        question.Correcta.setVisible(false);
        question.Incorrecta.setVisible(false);
        question.respuesta1.setOpaque(false);
        question.respuesta2.setOpaque(false);
        question.respuesta3.setOpaque(false);
        question.respuesta4.setOpaque(false);
        question.sumNota.setVisible(false);
        question.PanelHistorial.setVisible(false);
    }
    /*Metodos para la clase*/
    public void message(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
    public void listarHistorial() {
        String[] encab = {"Capítulo", "Nº Aciertos", "Nº Desaciertos", "Nota", "Fecha"};
        DefaultTableModel userHistory = new DefaultTableModel(null, encab) {
            @Override
            public boolean isCellEditable(int i, int j) {
                return false;
            }
        };
        JTableHeader tableheader = question.tablaUserHistorial.getTableHeader();
        tableheader.setBackground(new Color(18,20,35));
        tableheader.setForeground(Color.white);
        question.tablaUserHistorial.setModel(userHistory);
        String[] datos = new String[5];
        String userHistorial = FormLogin.txtusername.getText();
        historial = new Historial();
        try {
            if (historial != null) {
                for (int j = 0; j < historial.listarHistorialUsuario(userHistorial).size(); j++) {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
                    datos[0] = historial.listarHistorialUsuario(userHistorial).get(j).getCapitulo();
                    datos[1] = String.valueOf(historial.listarHistorialUsuario(userHistorial).get(j).getRespuesta_correcta());
                    datos[2] = String.valueOf(historial.listarHistorialUsuario(userHistorial).get(j).getRespuesta_incorrecta());
                    datos[3] = String.valueOf(historial.listarHistorialUsuario(userHistorial).get(j).getNota());
                    datos[4] = formatoFecha.format(historial.listarHistorialUsuario(userHistorial).get(j).getFecha());
                    userHistory.addRow(datos);
                }
            }
            question.tablaUserHistorial.setModel(userHistory);
        } catch (Exception c) {
            message("error" + c);
        }
    }
    public void respuestaCorrecta() {
        buena++;
        question.Correcta.setText("ACIERTOS: " + buena + "");
        FormQuestions.txtRespuesta.setText("<html><font color='green'size=5>RESPUESTA CORRECTA");
        question.PanelOpciones.setVisible(false);
        notaEstudiante();
    }
    public void respuestaIncorrecta() {
        mala++;
        question.Incorrecta.setText("DESACIERTOS: " + mala + "");
        FormQuestions.txtRespuesta.setText("<html><font color='red'size=5>RESPUESTA INCORRECTA");
        question.PanelOpciones.setVisible(false);
        notaEstudiante();
    }
    public void imagenPregunta() {
        if (capitulo == "CAP4" & (numeroPregunta == 15 | numeroPregunta == 16)) {
            question.imagen1.setVisible(true);
        } else {
            question.imagen1.setVisible(false);
        }
        if (capitulo == "CAP4" & numeroPregunta == 20) {
            question.imagen2.setVisible(true);
        } else {
            question.imagen2.setVisible(false);
        }
    }
    public void notaEstudiante() {
        if (insertarHistorial > 0) {
            if (pregunta == 10) {
                question.sumNota.setVisible(true);
                notaEstudiante = buena * 2;
                if (notaEstudiante < 10) {
                    question.sumNota.setText("<html><font color='red'size=5>Su nota es: " + notaEstudiante);
                } else if (notaEstudiante > 10) {
                    question.sumNota.setText("<html><font color='green'size=5>Su nota es: " + notaEstudiante);                    
                }
            }
        }        
    }
    public void regenerarArray() {
        if (arreglo[regenerarArray] == 0) {            
            arreglo[regenerarArray] = (int) (Math.random() * maximoNumero + 1);
            for (int i = 1; i < maximoNumero; i++) {
                arreglo[i] = (int) (Math.random() * maximoNumero + 1);                
                for (int j = 0; j < i; j++) {
                    if (arreglo[i] == arreglo[j]) {
                        i--;
                    }
                }
            }            
        }
    }
    public void capituloAleatorio(){
        pregunta=1;
        insertarHistorial=1;
        int capAlt = ((int) (Math.random()*(11-1))+1);
        numeroCapitulo=capAlt;
       if(numeroCapitulo==1){maximoNumero=19;}
       if(numeroCapitulo==2){maximoNumero=18;}
       if(numeroCapitulo==3){maximoNumero=24;}
       if(numeroCapitulo==4){maximoNumero=20;}
       if(numeroCapitulo==5){maximoNumero=36;}
       if(numeroCapitulo==6){maximoNumero=49;}
       if(numeroCapitulo==7){maximoNumero=49;}
       if(numeroCapitulo==8){maximoNumero=34;}
       if(numeroCapitulo==9){maximoNumero=24;}
       if(numeroCapitulo==10){maximoNumero=22;}
       posicion = 0;       
        for (int n = 0; n < maximoNumero; n++) {
            arreglo[n] = 0;
        }       
       capitulo="CAP"+numeroCapitulo;
    }
    public void desactivarCapitulos(){
        FormQuestions.btnCap1.setEnabled(false);
        FormQuestions.btnCap2.setEnabled(false);
        FormQuestions.btnCap3.setEnabled(false);
        FormQuestions.btnCap4.setEnabled(false);
        FormQuestions.btnCap5.setEnabled(false);
        FormQuestions.btnCap6.setEnabled(false);
        FormQuestions.btnCap7.setEnabled(false);
        FormQuestions.btnCap8.setEnabled(false);
        FormQuestions.btnCap9.setEnabled(false);
        FormQuestions.btnCap10.setEnabled(false);
        question.btnNext.setEnabled(false);
        question.btnRegresar.setEnabled(false);
    }
    public void mostrarDatos(String capi) {
        regenerarArray();
        posicion++;
        numeroPregunta = (arreglo[posicion]);
        cap = new Capitulo();
        res = new Respuesta();
        pre = new Pregunta();
        try {
            String datecapi = capi;
            res = res.obtenerDatos(datecapi, numeroPregunta);
            if (res != null) {
                cap = res.getPregunta().getCapitulo();
                FormQuestions.numeroPregunta.setText("Pregunta: " + pregunta);
                FormQuestions.tituloCap.setText("<html>"+res.getPregunta().getCapitulo().getCapitulo() + "<html>");
                FormQuestions.Pregunta.setText("<html>"+res.getPregunta().getPregunta().toUpperCase()+ "<html>");
                FormQuestions.respuesta1.setText("<html>"+res.getRespuesta1()+"<html>");
                FormQuestions.respuesta2.setText("<html>"+res.getRespuesta2()+"<html>");
                FormQuestions.respuesta3.setText("<html>"+res.getRespuesta3()+"<html>");
                FormQuestions.respuesta4.setText("<html>"+res.getRespuesta4()+"<html>");
                FormQuestions.Recuerda.setText("<html><p ALIGN=justify>" + "<b><u>Recuerda que</u>: </b>" + res.getMensaje() + "</p><html>");
                question.Correcta.setText("ACIERTOS: " + buena + "");
                question.Incorrecta.setText("DESACIERTOS: " + mala + "");
                imagenPregunta();
            }
        } catch (Exception problem) {
            message("Problem" + problem);
        }
    }
    //event's buttom's
    @Override
    public void actionPerformed(ActionEvent e) {
        if (FormQuestions.btnGenerarPDF == e.getSource()) {
            List<Historial> Personas = null;
            PlantillaPDF plantilla= new PlantillaPDF();
            try {
                Personas = historial.listarHistorialUsuario(historialDelUsuario);
                plantilla = new PlantillaPDF(historialDelUsuario, Personas);
                plantilla.crearPDF();
                JOptionPane.showMessageDialog(null,"Archivo Creado Correctamente");
                question.btnAbrirPDF.setEnabled(true);
                question.btnGenerarPDF.setEnabled(false);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (FormQuestions.btnAbrirPDF == e.getSource()) {
            try {
                File path = new File(historialDelUsuario+".pdf");
                Desktop.getDesktop().open(path);
                question.btnAbrirPDF.setEnabled(false);
                question.btnGenerarPDF.setEnabled(true);
            } catch (Exception x) {
                System.out.println(x.getMessage());
            }
        }
        if(FormQuestions.btnSalir==e.getSource()){
            int salida=JOptionPane.showConfirmDialog(null,"¿Desea salir de la Aplicación?"
                    ,"Seleciona una Opción...",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            System.out.println(salida);
            if(salida==0){
                System.exit(0);
            }
        }
        if(FormQuestions.btnHistorial==e.getSource()){
            question.PanelHistorial.setVisible(true);
            question.PanelBotonesIndice.setVisible(false);
            question.PanelMenu.setVisible(false);
            windowX = 0;
            windowY = 220;
            listarHistorial();
        }
        if(FormQuestions.btnCerrarHistorial==e.getSource()){
            question.PanelHistorial.setVisible(false);
            question.PanelBotonesIndice.setVisible(true);
        }
        if(FormQuestions.btnNextPregunta==e.getSource()){
           question.btnNextPregunta.setEnabled(false);
         //question.PanelOpciones.setVisible(false);
           question.PanelRecuerda.setVisible(true);
           if("CAP1".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 2
        if("CAP2".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 3
        if("CAP3".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==21){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==22){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==23){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==24){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 4
        if("CAP4".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 5
        if("CAP5".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==21){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==22){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==23){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==24){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==25){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==26){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==27){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==28){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==29){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==30){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==31){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==32){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==33){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==34){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==35){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==36){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 6
        if("CAP6".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==21){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==22){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==23){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==24){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==25){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==26){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==27){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==28){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==29){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==30){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==31){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==32){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==33){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==34){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==35){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==36){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==37){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==38){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==39){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==40){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==41){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==42){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==43){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==44){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==45){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==46){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==47){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==48){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==49){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 7
        if("CAP7".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==21){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==22){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==23){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==24){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==25){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==26){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==27){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==28){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==29){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==30){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==31){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==32){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==33){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==34){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==35){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==36){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==37){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==38){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==39){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==40){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==41){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==42){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==43){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==44){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==45){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==46){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==47){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==48){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==49){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 8
        if("CAP8".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==21){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==22){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==23){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==24){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==25){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==26){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==27){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==28){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==29){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==30){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==31){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==32){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==33){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==34){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 9
        if("CAP9".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==21){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==22){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==23){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==24){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //CAPITULO 10
        if("CAP10".equals(capitulo)){
            if(numeroPregunta==1){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==2){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==3){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==4){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==5){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==6){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==7){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==8){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==9){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==10){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==11){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==12){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==13){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==14){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==15){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==16){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==17){if(FormQuestions.respuesta2.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==18){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==19){if(FormQuestions.respuesta4.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==20){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==21){if(FormQuestions.respuesta3.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
            if(numeroPregunta==22){if(FormQuestions.respuesta1.isSelected()==false){respuestaIncorrecta();}else{respuestaCorrecta();}}
        }
        //FormQuestions.btnNextPregunta.setEnabled(false);
        }
        if(FormQuestions.btnAceptarPizarra==e.getSource()){
            if(pregunta==10){
                 int rescorrect =buena;
                   int resincorrect=mala;
                //insertar historial usuario;
                if(insertarHistorial>0){
                   String usuario=FormLogin.txtusername.getText();
                   String capitule = capitulo;
                   int nota= buena*2;
                   fecha = Date.valueOf(LocalDate.now()); 
                    try {
                        historial = new Historial(usuario, capitule, rescorrect, resincorrect, nota, fecha);
                        int x = historial.registrarHistorialUsuario();
                        if(x>0){
                            insertarHistorial=0;
                        }else{
                            message("error");
                        }
                    } catch (Exception exception) {
                        message("Error"+exception);
                    }
                }
                //close intsert
                if(FormQuestions.respuesta1.isSelected() || FormQuestions.respuesta2.isSelected() || FormQuestions.respuesta3.isSelected() || FormQuestions.respuesta4.isSelected()){
                question.PanelPreguntas.setVisible(false);
                question.PanelBotonesIndice.setVisible(true);
                pregunta=0;
                buena=0;
                mala=0;
                FormQuestions.btnCap1.setEnabled(true);
                FormQuestions.btnCap2.setEnabled(true);
                FormQuestions.btnCap3.setEnabled(true);
                FormQuestions.btnCap4.setEnabled(true);
                FormQuestions.btnCap5.setEnabled(true);
                FormQuestions.btnCap6.setEnabled(true);
                FormQuestions.btnCap7.setEnabled(true);
                FormQuestions.btnCap8.setEnabled(true);
                FormQuestions.btnCap9.setEnabled(true);
                FormQuestions.btnCap10.setEnabled(true);
                question.btnNext.setEnabled(true);
                question.btnRegresar.setEnabled(true);
                }
            }
            pregunta=pregunta+1;
            mostrarDatos(capitulo);
            FormQuestions.RadioButtomGroup.clearSelection();
            question.RadioButtomGroup.clearSelection();
            question.PanelRecuerda.setVisible(false);
            FormQuestions.PanelOpciones.setVisible(true);
            question.sumNota.setVisible(false);
            //question.btnNextPregunta.setEnabled(true);
        }
        if(FormQuestions.btnCap1==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=19;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP1";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap2==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=18;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP2";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap3==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=24;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP3";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap4==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=20;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP4";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap5==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=36;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP5";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap6==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=49;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP6";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap7==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=49;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP7";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap8==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=34;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP8";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap9==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=24;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP9";
           mostrarDatos(capitulo);
        }
        if(FormQuestions.btnCap10==e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           desactivarCapitulos();
           pregunta=1;
           maximoNumero=22;
           posicion=0;
           for(int n = 0; n<maximoNumero; n++){
               arreglo[n]=0;
           }
           capitulo="CAP10";
           mostrarDatos(capitulo);
        }
        //botón siguiente y regresar del panel indice
        if(FormQuestions.btnNext==e.getSource()){
           question.btnCap1.setVisible(false);
           question.btnCap2.setVisible(false);
           question.btnCap3.setVisible(false);
           question.btnCap4.setVisible(false);
           question.btnCap5.setVisible(false);
           question.btnCap6.setVisible(false);
           question.btnCap7.setVisible(false);
           question.btnNext.setVisible(false);
           question.btnRegresar.setVisible(true);
           question.btnCap8.setVisible(true);
           question.btnCap9.setVisible(true);
           question.btnCap10.setVisible(true);
        }
        if(FormQuestions.btnRegresar==e.getSource()){
           question.btnCap1.setVisible(true);
           question.btnCap2.setVisible(true);
           question.btnCap3.setVisible(true);
           question.btnCap4.setVisible(true);
           question.btnCap5.setVisible(true);
           question.btnCap6.setVisible(true);
           question.btnCap7.setVisible(true);
           question.btnNext.setVisible(true);   
           question.btnCap8.setVisible(false);
           question.btnCap9.setVisible(false);
           question.btnCap10.setVisible(false);
           question.btnRegresar.setVisible(false);
        }
        //Panel Deslice;
        if(FormQuestions.btnAleatorio == e.getSource()){
           question.PanelBotonesIndice.setVisible(false);
           question.PanelPreguntas.setVisible(true);
           question.Correcta.setVisible(true);
           question.Incorrecta.setVisible(true);
           question.PanelMenu.setVisible(false);
           capituloAleatorio();
           mostrarDatos(capitulo);
           windowX=0;
           windowY=220;
        }
        if(FormQuestions.btnIndice == e.getSource()){
            question.PanelMenu.setVisible(true);
            question.Correcta.setVisible(false);
            question.Incorrecta.setVisible(false);
          if (windowX == 220) {  
            question.PanelMenu.setSize(220, 570);
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        for (int i = 220; i >= 0; i--) {
                            Thread.sleep(2);
                        question.PanelMenu.setSize(i, 570);
                            windowY++;
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            };
            th.start();
            windowX = 0;
        }else if(windowX == 0){
            question.PanelMenu.setSize(windowX, 570);
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i <= windowX; i++) {
                            Thread.sleep(2);
                            question.PanelMenu.setSize(i, 570);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
            };
            th.start();
            windowX = 220;
                        }
        }      
    }
    @Override
    //events label/radiobuttoms
    public void mouseClicked(MouseEvent e) {
        //Activacion del boton nextPregunta;
        if (FormQuestions.respuesta1 == e.getSource() || FormQuestions.respuesta2 == e.getSource() || FormQuestions.respuesta3 == e.getSource() || FormQuestions.respuesta4 == e.getSource()){
            FormQuestions.btnNextPregunta.setEnabled(true);
            FormQuestions.btnNextPregunta.setEnabled(true);
        } 
    }
}
