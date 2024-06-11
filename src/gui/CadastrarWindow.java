package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.JSeparator;

public class CadastrarWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblDataNasc;
	private JTextField txtNomeCompleto;
	private JFormattedTextField txtDataNasc;
	private JLabel lblNomeCompleto_1;
	private JRadioButton rbFeminino;
	private JRadioButton rbMasculino;
	private JRadioButton rbNInformar;
	private JPanel painelGenero;
	private JPanel painelImagem;
	private JButton btnSelecionarImg;
	private JLabel lblEmail;
	private JFormattedTextField txtEmail;
	private JLabel lblNomeUsuario;
	private JFormattedTextField txtNomeUsuario;
	private JLabel lblSenha;
	private JPasswordField txtSenha;
	private JButton btnCadastrar;
	private JLabel lblTitulo;
	private JSeparator separator;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastrarWindow frame = new CadastrarWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CadastrarWindow() {
		setTitle("CADASTRAR");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 421, 554);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblDataNasc = new JLabel("Data de Nascimento");
		lblDataNasc.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDataNasc.setBounds(37, 131, 155, 14);
		contentPane.add(lblDataNasc);
		
		txtNomeCompleto = new JTextField();
		txtNomeCompleto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNomeCompleto.setBounds(37, 89, 155, 20);
		contentPane.add(txtNomeCompleto);
		txtNomeCompleto.setColumns(10);
		
		txtDataNasc = new JFormattedTextField();
		txtDataNasc.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDataNasc.setBounds(37, 148, 155, 20);
		contentPane.add(txtDataNasc);
		
		lblNomeCompleto_1 = new JLabel("Nome Completo");
		lblNomeCompleto_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNomeCompleto_1.setBounds(37, 71, 155, 14);
		contentPane.add(lblNomeCompleto_1);
		
		rbFeminino = new JRadioButton("Feminino");
		rbFeminino.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbFeminino.setBounds(58, 381, 109, 23);
		contentPane.add(rbFeminino);
		
		rbMasculino = new JRadioButton("Masculino");
		rbMasculino.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbMasculino.setBounds(58, 407, 109, 23);
		contentPane.add(rbMasculino);
		
		rbNInformar = new JRadioButton("Não Informar");
		rbNInformar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbNInformar.setBounds(58, 433, 109, 23);
		contentPane.add(rbNInformar);
		
		painelGenero = new JPanel();
		painelGenero.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "G\u00EAnero", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		painelGenero.setBounds(37, 366, 155, 105);
		contentPane.add(painelGenero);
		
		painelImagem = new JPanel();
		painelImagem.setBounds(258, 89, 110, 110);
		contentPane.add(painelImagem);
		
		btnSelecionarImg = new JButton("Add Imagem");
		btnSelecionarImg.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSelecionarImg.setBounds(258, 219, 110, 23);
		contentPane.add(btnSelecionarImg);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(37, 193, 155, 14);
		contentPane.add(lblEmail);
		
		txtEmail = new JFormattedTextField();
		txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtEmail.setBounds(37, 210, 155, 20);
		contentPane.add(txtEmail);
		
		lblNomeUsuario = new JLabel("Nome de Usuário");
		lblNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNomeUsuario.setBounds(37, 251, 155, 14);
		contentPane.add(lblNomeUsuario);
		
		txtNomeUsuario = new JFormattedTextField();
		txtNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNomeUsuario.setBounds(37, 268, 155, 20);
		contentPane.add(txtNomeUsuario);
		
		lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSenha.setBounds(37, 308, 155, 14);
		contentPane.add(lblSenha);
		
		txtSenha = new JPasswordField();
		txtSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtSenha.setBounds(37, 324, 155, 20);
		contentPane.add(txtSenha);
		
		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBackground(new Color(240, 240, 240));
		btnCadastrar.setForeground(new Color(0, 0, 0));
		btnCadastrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCadastrar.setBounds(239, 448, 129, 23);
		contentPane.add(btnCadastrar);
		
		lblTitulo = new JLabel("CADASTRAR NOVO USUÁRIO");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblTitulo.setBounds(10, 23, 219, 14);
		contentPane.add(lblTitulo);
		
		separator = new JSeparator();
		separator.setBounds(10, 41, 385, 2);
		contentPane.add(separator);
	}
}
