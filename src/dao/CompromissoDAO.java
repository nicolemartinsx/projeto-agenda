package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
					"insert into compromisso(titulo, descricao, data_inicio, data_termino, local, notificacao, agenda_id, usuario_id) values (?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, compromisso.getTitulo());
			st.setString(2, compromisso.getDescricao());
			st.setTimestamp(3, Timestamp.valueOf(compromisso.getDataInicio()));
			st.setTimestamp(4, Timestamp.valueOf(compromisso.getDataTermino()));
			st.setString(5, compromisso.getLocal());
			st.setTimestamp(6, Timestamp.valueOf(compromisso.getNotificacao()));
			st.setInt(7, compromisso.getAgenda().getIdAgenda());
			st.setInt(8, idUsuario);

			st.executeUpdate();

			rs = st.getGeneratedKeys();
			if (rs.next()) {
				compromisso.setIdCompromisso(rs.getInt(1));
			}

			if (compromisso.getConvidados() != null && !compromisso.getConvidados().isEmpty()) {

				st = conn.prepareStatement(
						"insert into compromisso_convidados (compromisso_id, usuario_id) VALUES (?, ?)");
				for (Usuario convidado : compromisso.getConvidados()) {
					st.setInt(1, compromisso.getIdCompromisso());
					st.setInt(2, convidado.getIdUsuario());
					st.addBatch();
				}
				st.executeBatch();
			}
		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}
	
	public void editarCompromisso(Compromisso compromisso, int idUsuario) throws SQLException {
		
	    PreparedStatement st = null;

	    try {
	        st = conn.prepareStatement(
	                "update compromisso SET titulo = ?, descricao = ?, data_inicio = ?, data_termino = ?, local = ?, notificacao = ?, agenda_id = ?, usuario_id = ? where id = ?");
	        st.setString(1, compromisso.getTitulo());
	        st.setString(2, compromisso.getDescricao());
	        st.setTimestamp(3, Timestamp.valueOf(compromisso.getDataInicio()));
	        st.setTimestamp(4, Timestamp.valueOf(compromisso.getDataTermino()));
	        st.setString(5, compromisso.getLocal());
	        st.setTimestamp(6, Timestamp.valueOf(compromisso.getNotificacao()));
	        st.setInt(7, compromisso.getAgenda().getIdAgenda());
	        st.setInt(8, idUsuario);
	        st.setInt(9, compromisso.getIdCompromisso());

	        st.executeUpdate();
	        
	        if (compromisso.getConvidados() != null && !compromisso.getConvidados().isEmpty()) {
	            
	            st = conn.prepareStatement("insert into compromisso_convidados (compromisso_id, usuario_id) values (?, ?)");
	            for (Usuario convidado : compromisso.getConvidados()) {
	                st.setInt(1, compromisso.getIdCompromisso());
	                st.setInt(2, convidado.getIdUsuario());
	                st.addBatch();
	            }
	            st.executeBatch();
	        }
	    } finally {
	        BancoDados.finalizarStatement(st);
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
