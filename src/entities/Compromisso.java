package entities;

import java.sql.Timestamp;
import java.util.List;

public class Compromisso {

	private int idCompromisso;
	private String titulo;
	private String descricao;
	private Timestamp dataInicio;
	private Timestamp dataTermino;
	private String local;
	private Agenda agenda;
	private List<String> convidados;
	private Timestamp notificacao;

	public Compromisso() {
		
	}

	public int getIdCompromisso() {
		return idCompromisso;
	}

	public void setIdCompromisso(int idCompromisso) {
		this.idCompromisso = idCompromisso;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Timestamp getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Timestamp dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Timestamp getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Timestamp dataTermino) {
		this.dataTermino = dataTermino;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	public List<String> getConvidados() {
		return convidados;
	}

	public void setConvidados(List<String> convidados) {
		this.convidados = convidados;
	}

	public Timestamp getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Timestamp notificacao) {
		this.notificacao = notificacao;
	}

}
