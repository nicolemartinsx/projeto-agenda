package dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.imageio.ImageIO;

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
					"insert into usuario (nome_completo, data_nascimento, genero, email, imagem_perfil, nome_usuario, senha) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			st.setString(1, usuario.getNomeCompleto());
			st.setDate(3, new Date(usuario.getDataNascimento().getTime()));
			st.setString(4, usuario.getGenero());
			st.setBytes(5, usuario.getImagemPerfil());
			st.setString(6, usuario.getEmail());
			st.setString(7, usuario.getnomeUsuario());
			st.setString(8, usuario.getSenha());

			st.executeUpdate();
		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public void editarUsuario(Usuario usuario) throws SQLException, IOException {

		PreparedStatement st = null;

		try {
			st = conn.prepareStatement(
					"update usuario set nome_completo=?, data_nascimento=?, genero=?, email=?, imagem_perfil=?, nome_usuario=?, senha=?");
			st.setString(1, usuario.getNomeCompleto());
			st.setDate(3, new Date(usuario.getDataNascimento().getTime()));
			st.setString(4, usuario.getGenero());
			if (usuario.getImagemPerfil() != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//ImageIO.write(usuario.getImagemPerfil(), "jpg", baos);
				byte[] imagemBytes = baos.toByteArray();
				st.setBytes(5, imagemBytes);
			} else {
				st.setNull(5, Types.BLOB);
			}
			st.setString(6, usuario.getEmail());
			st.setString(7, usuario.getnomeUsuario());
			st.setString(8, usuario.getSenha());

			st.executeUpdate();

		} finally {
			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

	public int excluirUsuario(String nomeUsuario) throws SQLException {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("delete from usuario where nome_usuario = ?");

			st.setString(1, nomeUsuario);

			int linhasManipuladas = st.executeUpdate();

			return linhasManipuladas;

		} finally {

			BancoDados.finalizarStatement(st);
			BancoDados.desconectar();
		}
	}

}
