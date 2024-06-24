package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Agenda;

public class AgendaDAO {

	Connection conn = null;

	public AgendaDAO(Connection conn) {
		this.conn = conn;
	}

	public void cadastrarAgenda(Agenda agenda, int idUsuario) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("insert into agenda(nome, descricao, id_usuario) values(?, ?, ?)");
			st.setString(1, agenda.getNome());
			st.setString(2, agenda.getDescricao());
			st.setInt(3, idUsuario);

			st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public void editarAgenda(Agenda agenda) throws SQLException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("update agenda set nome=?, descricao=? where id=?");
			st.setString(1, agenda.getNome());
			st.setString(2, agenda.getDescricao());
			st.setInt(3, agenda.getIdAgenda());

			st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public List<Agenda> buscarAgendas(int idUsuario) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("select * from agenda where id_usuario = ? order by nome");
			st.setInt(1, idUsuario);

			rs = st.executeQuery();

			List<Agenda> listaAgendas = new ArrayList<>();

			while (rs.next()) {

				Agenda agenda = new Agenda();
				
				agenda.setIdAgenda(rs.getInt("id"));
				agenda.setNome(rs.getString("nome"));
				agenda.setDescricao(rs.getString("descricao"));
				listaAgendas.add(agenda);
			}

			return listaAgendas;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	public Agenda buscarAgenda(int idAgenda) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("select * from agenda where id = ? ");
			st.setInt(1, idAgenda);

			rs = st.executeQuery();

			if (rs.next()) {

				Agenda agenda = new Agenda();
				
				agenda.setIdAgenda(rs.getInt("id"));
				agenda.setNome(rs.getString("nome"));
				agenda.setDescricao(rs.getString("descricao"));
				return agenda;
			}
			return null;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
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
