package entities;

import java.awt.image.BufferedImage;
import java.sql.Date;

public class Usuario {

	private String nomeCompleto;
	private Date dataNascimento;
	private String genero;
	private String email;
	private BufferedImage imagemPerfil;
	private String nomeUsuario;
	private String senha;
	private Object getImagemPerfil;
	
	private Usuario(String nomeCompleto, Date dataNascimento, String genero, String email, BufferedImage imagemPerfil,
			String nomeUsuario, String senha) {
		this.nomeCompleto = nomeCompleto;
		this.dataNascimento = dataNascimento;
		this.genero = genero;
		this.email = email;
		this.imagemPerfil = imagemPerfil;
		this.nomeUsuario = nomeUsuario;
		this.senha = senha;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BufferedImage getImagemPerfil() {
		return imagemPerfil;
	}

	public void setImagemPerfil(BufferedImage imagemPerfil) {
		this.imagemPerfil = imagemPerfil;
	}

	public String getnomeUsuario() {
		return nomeUsuario;
	}

	public void setnomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	
	
}