package service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Compromisso;
import entities.Sessao;
import gui.NotificacaoWindow;

public class NotificacaoThread extends Thread {

	private List<Compromisso> compromissos = new ArrayList<>();
	private CompromissoService compromissoService = new CompromissoService();

	public NotificacaoThread() {
		verificarCompromissos();
	}

	private void verificarCompromissos() {
		try {
			this.compromissos = compromissoService.verificarCompromisso(Sessao.getUsuario().getIdUsuario());

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				verificarCompromissos();
				if (compromissos != null) {
					new NotificacaoWindow(compromissos);
				}
				Thread.sleep(60000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
