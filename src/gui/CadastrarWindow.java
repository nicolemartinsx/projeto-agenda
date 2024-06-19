package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import entities.Usuario;
import service.UsuarioService;

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
	private JLabel lblImagemPerfil;

	private MaskFormatter mascaraData;
	private JPanel painelImagemPerfil;
	private UsuarioService usuarioService = new UsuarioService();

	public CadastrarWindow() {
		criarMascara();
		initComponents();
	}

	private void cadastrarUsuario() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Usuario usuario = new Usuario();

			usuario.setNomeCompleto(txtNomeCompleto.getText());
			usuario.setDataNascimento(new java.sql.Date(sdf.parse(this.txtDataNasc.getText()).getTime()));
			usuario.setGenero(verificarRbGenero());
			usuario.setEmail(txtEmail.getText());
			usuario.setImagemPerfil(converterIconParaBytes(lblImagemPerfil.getIcon()));
			usuario.setNomeUsuario(txtNomeUsuario.getText());
			usuario.setSenha(txtSenha.getPassword().toString());

			usuarioService.cadastrarUsuario(usuario);

			JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!", "Sucesso!",
					JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
			new InicioWindow().setVisible(true);

		} catch (ParseException | SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao realizar cadastro", "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void editarUsuario(InicioWindow inicio) {
		btnCadastrar.setText("Atualizar");
		lblTitulo.setText("Atualizar Perfil");

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Usuario usuario = new Usuario();

			usuario.setNomeCompleto(txtNomeCompleto.getText());
			usuario.setDataNascimento(new java.sql.Date(sdf.parse(this.txtDataNasc.getText()).getTime()));
			usuario.setGenero(verificarRbGenero());
			usuario.setEmail(txtEmail.getText());
			usuario.setImagemPerfil(converterIconParaBytes(lblImagemPerfil.getIcon()));
			usuario.setNomeUsuario(txtNomeUsuario.getText());
			usuario.setSenha(txtSenha.getPassword().toString());

			this.usuarioService.cadastrarUsuario(usuario);

			JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!", "Sucesso!",
					JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
			new InicioWindow().setVisible(true);

		} catch (ParseException | SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao realizar cadastro", "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	private byte[] converterIconParaBytes(Icon icon) throws IOException {

		Image image = ((ImageIcon) icon).getImage();

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		bufferedImage.getGraphics().drawImage(image, 0, 0, null);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(bufferedImage, "png", baos);
			return baos.toByteArray();
		}
	}

	private void selecionarImagem() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Selecionar Imagem de Perfil");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(
				new javax.swing.filechooser.FileNameExtensionFilter("Imagens", "jpg", "jpeg", "png"));

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			mostrarImagem(selectedFile);
		}
	}

	private void mostrarImagem(File file) {
		try {
			ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
			Image image = imageIcon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
			imageIcon.setImage(image);
			lblImagemPerfil.setIcon(imageIcon);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Não foi possível carregar a imagem", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String verificarRbGenero() {
		if (this.rbMasculino.isSelected()) {
			return this.rbMasculino.getText();
		} else if (this.rbFeminino.isSelected()) {
			return this.rbFeminino.getText();
		} else {
			return this.rbNInformar.getText();
		}
	}

	private void criarMascara() {
		try {
			mascaraData = new MaskFormatter("##/##/####");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
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

		txtDataNasc = new JFormattedTextField(mascaraData);
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

		ButtonGroup btnGroupSexo = new ButtonGroup();
		btnGroupSexo.add(rbMasculino);
		btnGroupSexo.add(rbFeminino);
		btnGroupSexo.add(rbNInformar);

		painelGenero = new JPanel();
		painelGenero.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"G\u00EAnero", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		painelGenero.setBounds(37, 366, 155, 105);
		contentPane.add(painelGenero);

		btnSelecionarImg = new JButton("Add Imagem");
		btnSelecionarImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionarImagem();
			}
		});
		btnSelecionarImg.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSelecionarImg.setBounds(239, 219, 129, 23);
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
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarUsuario();
			}
		});
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

		painelImagemPerfil = new JPanel();
		painelImagemPerfil.setBounds(239, 71, 129, 128);
		contentPane.add(painelImagemPerfil);

		lblImagemPerfil = new JLabel("");
		painelImagemPerfil.add(lblImagemPerfil);

		setLocationRelativeTo(null);
	}

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
}
