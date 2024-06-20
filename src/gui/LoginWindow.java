package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import entities.Sessao;
import entities.Usuario;
import service.UsuarioService;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNomeUsuario;
	private JLabel lblSenha;
	private JTextField txtNomeUsuario;
	private JPasswordField txtSenha;
	private JButton btnCadastrar;
	private JButton btnLogin;
	private JLabel lblLogin;
	private JSeparator separator;

	private UsuarioService usuarioService = new UsuarioService();;

	public LoginWindow() {
		initComponents();
	}

	private void realizarLogin() {
		
		try {
			Usuario usuario = usuarioService.realizarLogin(txtNomeUsuario.getText(), String.valueOf(txtSenha.getPassword()));
			if(usuario == null) {
				JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos", "Erro", JOptionPane.ERROR_MESSAGE);
				txtNomeUsuario.setText(null);
				txtSenha.setText(null);
			}else {
				this.dispose();
				new InicioWindow().setVisible(true);
				Sessao.setUsuario(usuario);
			}
		} catch (SQLException | IOException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Erro ao realizar login", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void abrirCadastrar() {
		this.dispose();
		new CadastrarWindow().setVisible(true);
	}

	private void initComponents() {
		setResizable(false);
		setTitle("LOGIN");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 403, 306);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblNomeUsuario = new JLabel("Nome de Usuário");
		lblNomeUsuario.setBounds(54, 72, 284, 14);
		lblNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblNomeUsuario);

		lblSenha = new JLabel("Senha");
		lblSenha.setBounds(54, 131, 284, 14);
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblSenha);

		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setBounds(54, 89, 284, 20);
		txtNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(txtNomeUsuario);
		txtNomeUsuario.setColumns(10);

		txtSenha = new JPasswordField();
		txtSenha.setBounds(54, 147, 284, 20);
		txtSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(txtSenha);

		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCadastrar();
			}
		});
		btnCadastrar.setBounds(54, 205, 115, 23);
		btnCadastrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(btnCadastrar);

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarLogin();
			}
		});
		btnLogin.setBounds(239, 207, 99, 23);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(btnLogin);

		lblLogin = new JLabel("LOGIN");
		lblLogin.setBounds(20, 11, 219, 14);
		lblLogin.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		contentPane.add(lblLogin);

		separator = new JSeparator();
		separator.setBounds(20, 36, 339, 2);
		contentPane.add(separator);

		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
