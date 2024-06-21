package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.AgendaDAO;
import dao.BancoDados;
import entities.Agenda;

public class AgendaService {

	public void cadastrarAgenda(Agenda agenda, int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new AgendaDAO(conn).cadastrarAgenda(agenda, idUsuario);
	}
	
	public void editarAgenda(Agenda agenda) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new AgendaDAO(conn).editarAgenda(agenda);
	}
	
	public List<Agenda> buscarAgendas(int idUsuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new AgendaDAO(conn).buscarAgendas(idUsuario);
	}
	
	public int excluirAgenda(int idAgenda) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new AgendaDAO(conn).excluirAgenda(idAgenda);
	}
}
