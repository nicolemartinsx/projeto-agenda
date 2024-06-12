package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entities.Agenda;

public class AgendaDAO {

	Connection conn = null;

	private AgendaDAO(Connection conn) {
		this.conn = conn;
	}

	public void cadastrarAgenda(Agenda agenda) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("insert into agenda(nome, descricao) values(?, ?)");
			st.setString(1, agenda.getNome());
			st.setString(2, agenda.getDescricao());

			st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public void editarAgenda(Agenda agenda, int idAgenda) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("update agenda set nome=?, descricao=? where id=?");
			st.setString(1, agenda.getNome());
			st.setString(2, agenda.getDescricao());
			st.setInt(3, idAgenda);

			st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public int excluirAgenda(int idAgenda) throws SQLException {
		
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("delete from agenda where id = ?");

			st.setInt(1, idAgenda);

			int linhasManipuladas = st.executeUpdate();

			return linhasManipuladas;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

}
