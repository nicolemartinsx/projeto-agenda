package service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.BancoDados;
import dao.CompromissoDAO;
import dao.UsuarioDAO;
import entities.Compromisso;
import entities.Sessao;
import entities.Usuario;

public class UsuarioService {

	public void cadastrarUsuario(Usuario usuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new UsuarioDAO(conn).cadastrarUsuario(usuario);
	}
	
	public List<String> buscarUsuarios() throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).buscarUsuarios();
	}
	
	public Usuario realizarLogin(String nomeUsuario, String senha) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).realizarLogin(nomeUsuario, senha);
	}
	
	public void editarUsuario(Usuario usuario) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		new UsuarioDAO(conn).editarUsuario(usuario);
		Sessao.setUsuario(usuario);
	}
	
	public int excluirUsuario(int id) throws SQLException, IOException {
		Connection conn = BancoDados.conectar();
		return new UsuarioDAO(conn).excluirUsuario(id);
	}
}
