package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Compromisso;
import entities.Usuario;

public class UsuarioDAO {

	private Connection conn = null;

	public UsuarioDAO(Connection conn) {
		this.conn = conn;
	}

	public void cadastrarUsuario(Usuario usuario) throws SQLException, IOException {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"insert into usuario (nome_completo, data_nascimento, genero, email, imagem_perfil, nome_usuario, senha) VALUES (?, ?, ?, ?, ?, ?, ?)");
			st.setString(1, usuario.getNomeCompleto());
			st.setDate(2, new Date(usuario.getDataNascimento().getTime()));
			st.setString(3, usuario.getGenero());
			st.setString(4, usuario.getEmail());
			st.setBytes(5, usuario.getImagemPerfil());
			st.setString(6, usuario.getNomeUsuario());
			st.setString(7, usuario.getSenha());

			st.executeUpdate();
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}
	
	public List<String> buscarUsuarios() throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("select * from usuario order by nome_completo");

			rs = st.executeQuery();

			List<String> listaUsuarios = new ArrayList<>();

			while (rs.next()) {

				listaUsuarios.add(rs.getString("nome_usuario"));
			}

			return listaUsuarios;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}

	public Usuario realizarLogin(String nomeUsuario, String senha) throws SQLException {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from usuario where nome_usuario = ? and senha = ?");
			st.setString(1, nomeUsuario);
			st.setString(2, senha);

			rs = st.executeQuery();

			if (rs.next()) {
				Usuario usuario = new Usuario();

				usuario.setIdUsuario(rs.getInt("id"));
				usuario.setNomeCompleto(rs.getString("nome_completo"));
				usuario.setDataNascimento(rs.getDate("data_nascimento"));
				usuario.setGenero(rs.getString("genero"));
				usuario.setImagemPerfil(rs.getBytes("imagem_perfil"));
				usuario.setEmail(rs.getString("email"));
				usuario.setNomeUsuario(rs.getString("nome_usuario"));
				usuario.setSenha(rs.getString("senha"));

				return usuario;
			}

			return null;

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.finalizarResultSet(rs);
			BancoDados.desconectar();
		}
	}

	public void editarUsuario(Usuario usuario) throws SQLException, IOException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"update usuario set nome_completo=?, data_nascimento=?, genero=?, imagem_perfil=?, email=?, nome_usuario=?, senha=? where id=?");
			st.setString(1, usuario.getNomeCompleto());
			st.setDate(2, new Date(usuario.getDataNascimento().getTime()));
			st.setString(3, usuario.getGenero());
			st.setBytes(4, usuario.getImagemPerfil());
			st.setString(5, usuario.getEmail());
			st.setString(6, usuario.getNomeUsuario());
			st.setString(7, usuario.getSenha());
			st.setInt(8, usuario.getIdUsuario());

			st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public int excluirUsuario(int id) throws SQLException {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("delete from usuario where id = ?");

			st.setInt(1, id);

			int linhasManipuladas = st.executeUpdate();

			return linhasManipuladas;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

}
