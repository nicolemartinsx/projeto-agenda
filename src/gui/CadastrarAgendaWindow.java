package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import entities.Agenda;
import entities.Sessao;
import service.AgendaService;

public class CadastrarAgendaWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtNome;
	private JTextArea txtDescricao;
	private JLabel lblNome;
	private JLabel lblDescricao;
	private JButton btnCancelar;
	private JButton btnCadastrar;

	private AgendaService agendaService = new AgendaService();
	private Agenda agendaAtual;

	public CadastrarAgendaWindow() {
		initComponets();
	}

	public void preencherCampos(Agenda agenda) {
		txtNome.setText(agenda.getNome());
		txtDescricao.setText(agenda.getDescricao());
		this.agendaAtual = agenda;
	}

	private void cadastrarAgenda() {
		try {
			Agenda agenda = new Agenda();

			agenda.setNome(txtNome.getText());
			agenda.setDescricao(txtDescricao.getText());

			if (this.agendaAtual == null) {
				agendaService.cadastrarAgenda(agenda, Sessao.getUsuario().getIdUsuario());

			} else {
				agenda.setIdAgenda(agendaAtual.getIdAgenda());
				agendaService.editarAgenda(agenda);
			}
			JOptionPane.showMessageDialog(this, "Salvo com sucesso!", "Sucesso!",
					JOptionPane.INFORMATION_MESSAGE);
			this.dispose();

		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao salvar agenda", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initComponets() {
		setType(Type.POPUP);
		setTitle("Agenda");
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 349, 286);
		getContentPane().setLayout(null);

		txtNome = new JTextField();
		txtNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNome.setBounds(33, 54, 263, 20);
		getContentPane().add(txtNome);
		txtNome.setColumns(10);

		txtDescricao = new JTextArea();
		txtDescricao.setLineWrap(true);
		txtDescricao.setWrapStyleWord(true);
		txtDescricao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDescricao.setBounds(33, 107, 263, 68);
		getContentPane().add(txtDescricao);

		lblNome = new JLabel("Nome");
		lblNome.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNome.setBounds(33, 30, 99, 14);
		getContentPane().add(lblNome);

		lblDescricao = new JLabel("Descricao");
		lblDescricao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDescricao.setBounds(33, 85, 126, 14);
		getContentPane().add(lblDescricao);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCancelar.setBounds(33, 196, 89, 23);
		getContentPane().add(btnCancelar);

		btnCadastrar = new JButton("Salvar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarAgenda();
			}
		});
		btnCadastrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCadastrar.setBounds(190, 196, 106, 23);
		getContentPane().add(btnCadastrar);

		setLocationRelativeTo(null);
	}
}
