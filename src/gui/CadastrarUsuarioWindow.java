package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import entities.Sessao;
import entities.Usuario;
import service.UsuarioService;

public class CadastrarUsuarioWindow extends JFrame {

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
	private JLabel lblNomeUsuario;
	private JLabel lblSenha;
	private JPasswordField txtSenha;
	private JButton btnCadastrar;
	private JLabel lblTitulo;
	private JSeparator separator;
	private JLabel lblImagemPerfil;
	private JPanel painelImagemPerfil;
	private JButton btnExcluir;
	private JTextField txtEmail;
	private JTextField txtNomeUsuario;
	private JButton btnVoltar;

	private MaskFormatter mascaraData;
	private UsuarioService usuarioService = new UsuarioService();

	public CadastrarUsuarioWindow() {
		criarMascara();
		initComponents();
		recuperarUsuario();
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
			usuario.setSenha(String.valueOf(txtSenha.getPassword()));

			usuarioService.cadastrarUsuario(usuario);

			JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!", "Sucesso!",
					JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
			new LoginWindow().setVisible(true);

		} catch (ParseException | SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao realizar cadastro", "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void editarUsuario() {
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

			this.usuarioService.editarUsuario(usuario);

			JOptionPane.showMessageDialog(this, "Cadastro atualizado com sucesso!", "Sucesso!",
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

	public ImageIcon converterBytesParaIcon(byte[] bytes) throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
			BufferedImage bufferedImage = ImageIO.read(bais);
			return new ImageIcon(bufferedImage);
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

	private void excluirUsuario() {
		try {
			int op = JOptionPane.showConfirmDialog(null, "Deseja excluir sua conta?");

			if (op == 0) {
				int result = usuarioService.excluirUsuario(Sessao.getUsuario().getIdUsuario());

				if (result == 1) {
					JOptionPane.showMessageDialog(this, "Exclusão realizada com sucesso!", "Sucesso!",
							JOptionPane.INFORMATION_MESSAGE);
					this.dispose();
					new LoginWindow().setVisible(true);
					Sessao.setUsuario(null);

				} else {
					JOptionPane.showMessageDialog(this, "Erro ao excluir usuário", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao excluir usuário", "Erro", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(this, "Erro ao criar máscara de data", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void voltar() {
		this.dispose();
		new InicioWindow().setVisible(true);
	}

	private void recuperarUsuario() {
		try {
			if (Sessao.getUsuario() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

				this.setTitle("EDITAR");
				this.lblTitulo.setText("EDITAR USUÁRIO");
				this.btnCadastrar.setText("Salvar");
				this.btnExcluir.setVisible(true);
				this.btnExcluir.setEnabled(true);
				this.txtNomeCompleto.setText(Sessao.getUsuario().getNomeCompleto());
				this.txtDataNasc.setValue(sdf.format(Sessao.getUsuario().getDataNascimento()));
				this.txtEmail.setText(Sessao.getUsuario().getEmail());
				this.txtNomeUsuario.setText(Sessao.getUsuario().getNomeCompleto());
				this.txtSenha.setText(Sessao.getUsuario().getSenha());
				this.lblImagemPerfil.setIcon(converterBytesParaIcon(Sessao.getUsuario().getImagemPerfil()));
				if (Sessao.getUsuario().getGenero().equals("Masculino")) {
					this.rbMasculino.setSelected(true);
				} else if (Sessao.getUsuario().getGenero().equals("Feminino")) {
					this.rbFeminino.setSelected(true);
				} else {
					this.rbNInformar.setSelected(true);
				}

			} else {
				this.btnExcluir.setVisible(false);
				this.btnExcluir.setEnabled(false);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao recuperar dados de usuário", "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initComponents() {
		setTitle("CADASTRAR");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 386, 567);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblDataNasc = new JLabel("Data de Nascimento");
		lblDataNasc.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDataNasc.setBounds(20, 131, 155, 14);
		contentPane.add(lblDataNasc);

		txtNomeCompleto = new JTextField();
		txtNomeCompleto.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNomeCompleto.setBounds(20, 89, 155, 20);
		contentPane.add(txtNomeCompleto);
		txtNomeCompleto.setColumns(10);

		txtDataNasc = new JFormattedTextField(mascaraData);
		txtDataNasc.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDataNasc.setBounds(20, 148, 155, 20);
		contentPane.add(txtDataNasc);

		lblNomeCompleto_1 = new JLabel("Nome Completo");
		lblNomeCompleto_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNomeCompleto_1.setBounds(20, 71, 155, 14);
		contentPane.add(lblNomeCompleto_1);

		rbFeminino = new JRadioButton("Feminino");
		rbFeminino.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbFeminino.setBounds(41, 381, 109, 23);
		contentPane.add(rbFeminino);

		rbMasculino = new JRadioButton("Masculino");
		rbMasculino.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbMasculino.setBounds(41, 407, 109, 23);
		contentPane.add(rbMasculino);

		rbNInformar = new JRadioButton("Não Informar");
		rbNInformar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		rbNInformar.setBounds(41, 433, 109, 23);
		contentPane.add(rbNInformar);

		ButtonGroup btnGroupSexo = new ButtonGroup();
		btnGroupSexo.add(rbMasculino);
		btnGroupSexo.add(rbFeminino);
		btnGroupSexo.add(rbNInformar);

		painelGenero = new JPanel();
		painelGenero.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"G\u00EAnero", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		painelGenero.setBounds(20, 366, 155, 105);
		contentPane.add(painelGenero);

		btnSelecionarImg = new JButton("Add Imagem");
		btnSelecionarImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionarImagem();
			}
		});
		btnSelecionarImg.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSelecionarImg.setBounds(222, 219, 129, 23);
		contentPane.add(btnSelecionarImg);

		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(20, 193, 155, 14);
		contentPane.add(lblEmail);

		lblNomeUsuario = new JLabel("Nome de Usuário");
		lblNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNomeUsuario.setBounds(20, 251, 155, 14);
		contentPane.add(lblNomeUsuario);

		lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSenha.setBounds(20, 308, 155, 14);
		contentPane.add(lblSenha);

		txtSenha = new JPasswordField();
		txtSenha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtSenha.setBounds(20, 324, 155, 20);
		contentPane.add(txtSenha);

		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Sessao.getUsuario() == null) {
					cadastrarUsuario();
				} else {
					editarUsuario();
				}
			}
		});
		btnCadastrar.setBackground(new Color(240, 240, 240));
		btnCadastrar.setForeground(new Color(0, 0, 0));
		btnCadastrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCadastrar.setBounds(222, 448, 129, 23);
		contentPane.add(btnCadastrar);

		lblTitulo = new JLabel("CADASTRAR NOVO USUÁRIO");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setBounds(20, 11, 340, 20);
		contentPane.add(lblTitulo);

		separator = new JSeparator();
		separator.setBounds(20, 30, 340, 2);
		contentPane.add(separator);

		painelImagemPerfil = new JPanel();
		painelImagemPerfil.setBounds(222, 71, 129, 128);
		contentPane.add(painelImagemPerfil);

		lblImagemPerfil = new JLabel("");
		painelImagemPerfil.add(lblImagemPerfil);

		btnExcluir = new JButton("Excluir Conta");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluirUsuario();
			}
		});
		btnExcluir.setEnabled(false);
		btnExcluir.setForeground(new Color(255, 0, 0));
		btnExcluir.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExcluir.setBackground(UIManager.getColor("Button.background"));
		btnExcluir.setBounds(222, 482, 129, 23);
		contentPane.add(btnExcluir);

		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtEmail.setBounds(20, 209, 155, 20);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);

		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtNomeUsuario.setBounds(20, 266, 155, 20);
		contentPane.add(txtNomeUsuario);
		txtNomeUsuario.setColumns(10);

		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnVoltar.setBounds(20, 482, 89, 23);
		contentPane.add(btnVoltar);

		setLocationRelativeTo(null);
	}
}
