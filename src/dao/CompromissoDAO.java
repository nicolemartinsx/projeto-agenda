package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import entities.Agenda;
import entities.Compromisso;
import entities.Usuario;

public class CompromissoDAO {

	private Connection conn = null;

	public CompromissoDAO(Connection conn) {
		this.conn = conn;
	}

	public void cadastrarCompromisso(Compromisso compromisso, int idUsuario) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"insert into compromisso(titulo, descricao, data_inicio, data_termino, local, notificacao, agenda_id, usuario_id) values (?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, compromisso.getTitulo());
			st.setString(2, compromisso.getDescricao());
			st.setTimestamp(3, compromisso.getDataInicio());
			st.setTimestamp(4, compromisso.getDataTermino());
			st.setString(5, compromisso.getLocal());
			st.setTimestamp(6, compromisso.getNotificacao());
			st.setInt(7, compromisso.getAgenda().getIdAgenda());
			st.setInt(8, idUsuario);

			st.executeUpdate();

			rs = st.getGeneratedKeys();
			if (rs.next()) {
				compromisso.setIdCompromisso(rs.getInt(1));
			}

			if (compromisso.getConvidados() != null && !compromisso.getConvidados().isEmpty()) {

				List<Integer> convidados = new ArrayList<>();
				for (String convidado : compromisso.getConvidados()) {
					st = conn.prepareStatement("select id from usuario where nome_usuario=?");
					st.setString(1, convidado);
					rs = st.executeQuery();
					if (rs.next()) {
						convidados.add(rs.getInt("id"));
					}
				}

				for (int convidado : convidados) {
					st = conn.prepareStatement(
							"insert into compromisso_convidados (compromisso_id, usuario_id, convite) values (?, ?, ?)");
					st.setInt(1, compromisso.getIdCompromisso());
					st.setInt(2, convidado);
					st.setInt(3, 0);
					st.executeUpdate();
				}
			}
		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}

	public List<Compromisso> buscarCompromissos(int idAgenda) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("select * from compromisso where agenda_id = ? order by titulo");
			st.setInt(1, idAgenda);

			rs = st.executeQuery();

			List<Compromisso> listaCompromissos = new ArrayList<>();

			while (rs.next()) {

				Compromisso compromisso = new Compromisso();

				compromisso.setIdCompromisso(rs.getInt("id"));
				compromisso.setTitulo(rs.getString("titulo"));
				compromisso.setDescricao(rs.getString("descricao"));
				compromisso.setDataInicio(rs.getTimestamp("data_inicio"));
				compromisso.setDataTermino(rs.getTimestamp("data_termino"));
				compromisso.setLocal(rs.getString("local"));
				compromisso.setNotificacao(rs.getTimestamp("notificacao"));

				listaCompromissos.add(compromisso);
			}

			return listaCompromissos;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	public void importarCompromissos() {
		
	}

	public void exportarCompromissos() {
		
	}
	
	public Compromisso buscarCompromisso(int idCompromisso) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("select * from compromisso where id = ?");
			st.setInt(1, idCompromisso);

			rs = st.executeQuery();

			Compromisso compromisso = new Compromisso();

			if (rs.next()) {

				compromisso.setIdCompromisso(rs.getInt("id"));
				compromisso.setTitulo(rs.getString("titulo"));
				compromisso.setDescricao(rs.getString("descricao"));
				compromisso.setDataInicio(rs.getTimestamp("data_inicio"));
				compromisso.setDataTermino(rs.getTimestamp("data_termino"));
				compromisso.setLocal(rs.getString("local"));
				compromisso.setNotificacao(rs.getTimestamp("notificacao"));

			}

			st = conn.prepareStatement(
					"select nome_usuario from compromisso_convidados join usuario on compromisso_convidados.usuario_id = usuario.id where compromisso_id = ?");
			st.setInt(1, idCompromisso);

			rs = st.executeQuery();

			List<String> convidados = new ArrayList<>();
			while (rs.next()) {
				convidados.add(rs.getString("nome_usuario"));
			}
			compromisso.setConvidados(convidados);

			return compromisso;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}

	public List<Compromisso> buscarConvites(int idUsuario) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement(
					"select compromisso_id from compromisso_convidados where usuario_id = ? and convite = 0");
			st.setInt(1, idUsuario);

			rs = st.executeQuery();

			List<Compromisso> convites = new ArrayList<>();
			ResultSet rs2 = null;

			while (rs.next()) {
				st = conn.prepareStatement("select * from compromisso where id = ?");
				st.setInt(1, rs.getInt("compromisso_id"));
				rs2 = st.executeQuery();

				if (rs2.next()) {
					Compromisso convite = new Compromisso();

					convite.setIdCompromisso(rs2.getInt("id"));
					convite.setTitulo(rs2.getString("titulo"));
					convite.setDescricao(rs2.getString("descricao"));
					convite.setDataInicio(rs2.getTimestamp("data_inicio"));
					convite.setDataTermino(rs2.getTimestamp("data_termino"));
					convite.setLocal(rs2.getString("local"));
					convite.setNotificacao(rs2.getTimestamp("notificacao"));

					st = conn.prepareStatement(
							"select nome_usuario from compromisso_convidados join usuario on compromisso_convidados.usuario_id = usuario.id where compromisso_id = ?");
					st.setInt(1, convite.getIdCompromisso());

					rs2 = st.executeQuery();

					List<String> convidados = new ArrayList<>();
					while (rs2.next()) {
						convidados.add(rs2.getString("nome_usuario"));
					}

					convite.setConvidados(convidados);
					convites.add(convite);
				}
			}

			return convites;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}

	public void recusarConvite(int idCompromisso, int idUsuario) throws SQLException {
		PreparedStatement st = null;

		try {

			st = conn
					.prepareStatement("delete from compromisso_convidados where compromisso_id = ? and usuario_id = ?");
			st.setInt(1, idCompromisso);
			st.setInt(1, idUsuario);

			st.executeUpdate();

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public void aceitarConvite(int idCompromisso, int idUsuario) throws SQLException {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement(
					"update compromisso_convidados where compromisso_id = ? and usuario_id = ? set convite = 1");
			st.setInt(1, idCompromisso);
			st.setInt(1, idUsuario);

			st.executeUpdate();

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public void editarCompromisso(Compromisso compromisso, int idUsuario) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"update compromisso set titulo = ?, descricao = ?, data_inicio = ?, data_termino = ?, local = ?, notificacao = ?, agenda_id = ?, usuario_id = ? where id = ?");
			st.setString(1, compromisso.getTitulo());
			st.setString(2, compromisso.getDescricao());
			st.setTimestamp(3, compromisso.getDataInicio());
			st.setTimestamp(4, compromisso.getDataTermino());
			st.setString(5, compromisso.getLocal());
			st.setTimestamp(6, compromisso.getNotificacao());
			st.setInt(7, compromisso.getAgenda().getIdAgenda());
			st.setInt(8, idUsuario);
			st.setInt(9, compromisso.getIdCompromisso());
			st.executeUpdate();

			st = conn.prepareStatement("delete from compromisso_convidados where compromisso_id = ?");
			st.setInt(1, compromisso.getIdCompromisso());
			st.executeUpdate();

			if (compromisso.getConvidados() != null && !compromisso.getConvidados().isEmpty()) {

				List<Integer> convidados = new ArrayList<>();
				for (String convidado : compromisso.getConvidados()) {
					st = conn.prepareStatement("select id from usuario where nome_usuario=?");
					st.setString(1, convidado);
					rs = st.executeQuery();
					if (rs.next()) {
						convidados.add(rs.getInt("id"));
					}
				}

				for (int convidado : convidados) {
					st = conn.prepareStatement(
							"insert into compromisso_convidados (compromisso_id, usuario_id) values (?, ?)");
					st.setInt(1, compromisso.getIdCompromisso());
					st.setInt(2, convidado);
					st.executeUpdate();
				}
			}
		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}

	public int excluirCompromisso(int idCompromisso) throws SQLException {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("delete from compromisso where id = ?");
			st.setInt(1, idCompromisso);

			int linhasManipuladas = st.executeUpdate();

			return linhasManipuladas;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

}
