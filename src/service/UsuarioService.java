package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.BancoDados;
import dao.UsuarioDAO;
import entities.Usuario;

public class UsuarioService {

	public void cadastrarUsuario(Usuario usuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new UsuarioDAO(conn).cadastrarUsuario(usuario);
	}
	
	public int realizarLogin(String nomeUsuario, String senha) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).realizarLogin(nomeUsuario, senha);
	}
}
