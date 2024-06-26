package gui;

import java.awt.Toolkit;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import entities.Compromisso;
import service.CompromissoService;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NotificacaoWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	private List<Compromisso> compromissos = new ArrayList<>();
	private CompromissoService compromissoService = new CompromissoService();
	private JLabel lblTexto;
	private JLabel lblHora;
	private JButton btnConfirmar;

	public NotificacaoWindow(List<Compromisso> compromissos) {
		this.compromissos = compromissos;
		initComponents();
		mostrarNotificacao();
	}

	private void mostrarNotificacao() {
		try {
			for (Compromisso compromisso : compromissos) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
				String dataInicio = sdf.format(compromisso.getDataInicio());
				
				lblHora.setText(dataInicio);
				
				Toolkit.getDefaultToolkit().beep();

				this.setVisible(true);
				compromissoService.excluirCompromisso(compromisso.getIdCompromisso());
				
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao exibir notificação", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void esconderNotificacao() {
		this.dispose();
	}

	private void initComponents() {
		setTitle("Notificação de Compromisso");
		setBounds(100, 100, 328, 186);
		getContentPane().setLayout(null);

		lblTexto = new JLabel("Você tem um compromisso agendado para ");
		lblTexto.setHorizontalAlignment(SwingConstants.CENTER);
		lblTexto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTexto.setBounds(10, 11, 291, 56);
		getContentPane().add(lblTexto);

		lblHora = new JLabel("");
		lblHora.setHorizontalAlignment(SwingConstants.CENTER);
		lblHora.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHora.setBounds(20, 50, 281, 26);
		getContentPane().add(lblHora);

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				esconderNotificacao();
			}
		});
		btnConfirmar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConfirmar.setBounds(97, 109, 116, 23);
		getContentPane().add(btnConfirmar);
	}
}
