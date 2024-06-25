package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import entities.Sessao;

public class InicioWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnEditarPerfil;
	private JButton btnAgenda;
	private JButton btnConvites;
	private JButton btnLogout;
private JLabel lblConvite;

	private CompromissoService compromissoService = new CompromissoService();

	public InicioWindow() {
		initComponents();
buscarConvites();
	}

	private void editarUsuario() {
		this.dispose();
		new CadastrarUsuarioWindow().setVisible(true);
	}

	private void abrirAgenda() {
		this.dispose();
		new AgendaWindow().setVisible(true);
	}

	private void buscarConvites() {
		try {
			List<Compromisso> convites;
			convites = compromissoService.buscarConvites(Sessao.getUsuario().getIdUsuario());
			if (!convites.isEmpty())
				lblConvite.setVisible(true);
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao obter convites", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void realizarLogout() {
		this.dispose();
		new LoginWindow().setVisible(true);
		Sessao.setUsuario(null);
	}
	
	private void initComponents() {
		setResizable(false);
		setTitle("HOME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 258, 243);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("BEM VINDO");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setBounds(10, 11, 223, 20);
		contentPane.add(lblTitulo);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 29, 223, 2);
		contentPane.add(separator);

		btnAgenda = new JButton("Agendas");
		btnAgenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirAgenda();
			}
		});
		btnAgenda.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAgenda.setBounds(52, 86, 130, 23);
		contentPane.add(btnAgenda);

		btnEditarPerfil = new JButton("Editar Perfil");
		btnEditarPerfil.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarUsuario();
			}
		});
		btnEditarPerfil.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditarPerfil.setBounds(52, 52, 130, 23);
		contentPane.add(btnEditarPerfil);

		btnConvites = new JButton("Convites");
		btnConvites.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConvites.setBounds(52, 120, 130, 23);
		contentPane.add(btnConvites);
		
		btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarLogout();
			}
		});
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnLogout.setBounds(52, 154, 130, 23);
		contentPane.add(btnLogout);

		lblConvite = new JLabel("Você possui convites não respondidos!");
		lblConvite.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConvite.setBounds(10, 42, 279, 20);
		lblConvite.setVisible(false);
		contentPane.add(lblConvite);

		setLocationRelativeTo(null);
	}

}
