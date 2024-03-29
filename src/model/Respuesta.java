package model;
import ConfigJDBC.GestorJDBC;
import ConfigJDBC.JDBCMySql;
import DataAccessObject.QuestionDAO;
public class Respuesta {
    
    private int idRespuesta;
    private String respuesta1, respuesta2, respuesta3, respuesta4;
    private String Mensaje;
    private Pregunta pregunta;
    private GestorJDBC gestorJDBC = new JDBCMySql();
    private QuestionDAO questionDAO = new QuestionDAO(gestorJDBC);

    public Respuesta(int idRespuesta, String respuesta1, String respuesta2, String respuesta3, String respuesta4, String Mensaje, Pregunta pregunta) {
        this.idRespuesta = idRespuesta;
        this.respuesta1 = respuesta1;
        this.respuesta2 = respuesta2;
        this.respuesta3 = respuesta3;
        this.respuesta4 = respuesta4;
        this.Mensaje = Mensaje;
        this.pregunta = pregunta;
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public String getRespuesta3() {
        return respuesta3;
    }

    public void setRespuesta3(String respuesta3) {
        this.respuesta3 = respuesta3;
    }

    public String getRespuesta4() {
        return respuesta4;
    }

    public void setRespuesta4(String respuesta4) {
        this.respuesta4 = respuesta4;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public GestorJDBC getGestorJDBC() {
        return gestorJDBC;
    }

    public void setGestorJDBC(GestorJDBC gestorJDBC) {
        this.gestorJDBC = gestorJDBC;
    }

    public QuestionDAO getQuestionDAO() {
        return questionDAO;
    }

    public void setQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    public Respuesta() {
    }

    public Respuesta obtenerDatos(String cap, int num) throws Exception {
        gestorJDBC.abrirConexion();
        Respuesta res = questionDAO.obtenerDatos(cap, num);
        gestorJDBC.cerrarConexion();
        return res;
    }
}
