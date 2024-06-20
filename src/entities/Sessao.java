package entities;

public class Sessao {

	private static Usuario usuario;

	public static Usuario getUsuario() {
		return usuario;
	}

	public static void setUsuario(Usuario usuario) {
		Sessao.usuario = usuario;
	}
	
	
}
