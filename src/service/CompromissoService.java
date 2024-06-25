package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.AgendaDAO;
import dao.BancoDados;
import dao.CompromissoDAO;
import entities.Agenda;
import entities.Compromisso;

public class CompromissoService {
	
	
	public List<Compromisso> buscarCompromissos(int idAgenda) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new CompromissoDAO(conn).buscarCompromissos(idAgenda);
	}
	
	public List<Compromisso> buscarConvites(int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new CompromissoDAO(conn).buscarConvites(idUsuario);
	}
	
	public void recusarConvite(int idCompromisso, int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new CompromissoDAO(conn).recusarConvite(idCompromisso, idUsuario);
	}
	
	public Compromisso buscarCompromisso(int idCompromisso) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new CompromissoDAO(conn).buscarCompromisso(idCompromisso);
	}
	
	public void cadastrarCompromisso(Compromisso compromisso, int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new CompromissoDAO(conn).cadastrarCompromisso(compromisso, idUsuario);
	}
	
	public void editarCompromisso(Compromisso compromisso, int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new CompromissoDAO(conn).editarCompromisso(compromisso, idUsuario);
	}
	
	public int excluirCompromisso(int idCompromisso) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new CompromissoDAO(conn).excluirCompromisso(idCompromisso);
	}
}
