package easii.com.br.iscoolapp.objetos;

import java.util.Date;

/**
 * Created by viniciusthiengo on 8/10/15.
 */
public class Message {
    private String id;
    private String user;
    private String msg;
    private String idDisciplina;
    private String hora;
    private Date data;

    public Message(){}

    public Message(String id, String msg, String user) {
        this.user = user;
        this.msg = msg;
        this.id = id;
    }


    public Message(String idDoUser, String nomeDoUser, String idDisciplina, String mensagem, String hora) {
        this.id = idDoUser;
        this.user = nomeDoUser;
        this.idDisciplina = idDisciplina;
        this.msg = mensagem;
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setIdDoUser(String idDoUser) {
        this.id = idDoUser;
    }

    public String getUser() {
        return user;
    }

    public void setNomeDoUser(String nomeDoUser) {
        this.user = nomeDoUser;
    }

    public String getMsg() {
        return msg;
    }

    public void setMensagem(String mensagem) {
        this.msg = mensagem;
    }

    public String getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(String idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        return data;
    }
}
