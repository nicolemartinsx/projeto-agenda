package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import entities.Agenda;
import entities.Compromisso;
import entities.Sessao;
import service.CompromissoService;
import service.UsuarioService;

public class CadastrarCompromissoWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblDataInicio;
	private JTextField txtTitulo;
	private JFormattedTextField txtDataInicio;
	private JLabel lblTitulo;
	private JLabel lblLocal;
	private JLabel lblNotificacao;
	private JButton btnCadastrar;
	private JLabel lblTituloPagina;
	private JSeparator separator;
	private JTextField txtLocal;
	private JButton btnVoltar;
	private JLabel lblDataFinal;
	private JFormattedTextField txtDataFinal;
	private JLabel lblHoraInicio;
	private JFormattedTextField txtHoraInicio;
	private JLabel lblHoraFinal;
	private JFormattedTextField txtHoraFinal;
	private JComboBox cbNotificacao;
	private JLabel lblConvidados;
	private JList listaConvidados;

	private MaskFormatter mascaraData;
	private MaskFormatter mascaraHora;
	private JLabel lblDescricao;
	private JTextArea txtDescricao;

	private Agenda agenda;
	private Compromisso compromissoAtual;
	private CompromissoService compromissoService = new CompromissoService();
	private UsuarioService usuarioService = new UsuarioService();

	public CadastrarCompromissoWindow(Agenda agenda) {
		criarMascara();
		initComponents();
		buscarUsuarios();
		this.agenda = agenda;
	}

	private void cadastrarOuEditarCompromisso() {
		try {

			Compromisso compromisso = new Compromisso();

			compromisso.setTitulo(txtTitulo.getText());
			compromisso.setDescricao(txtDescricao.getText());
			compromisso.setDataInicio(converterTimestamp(txtDataInicio.getText(), txtHoraInicio.getText()));
			compromisso.setDataTermino(converterTimestamp(txtDataFinal.getText(), txtHoraFinal.getText()));
			compromisso.setAgenda(agenda);
			compromisso.setLocal(txtLocal.getText());
			compromisso.setConvidados(listaConvidados.getSelectedValuesList());
			compromisso.setNotificacao(verificarNotificacao(converterTimestamp(txtDataInicio.getText(), txtHoraInicio.getText())));

			if (this.compromissoAtual == null) {
				compromissoService.cadastrarCompromisso(compromisso, Sessao.getUsuario().getIdUsuario());
			} else {
				compromisso.setIdCompromisso(compromissoAtual.getIdCompromisso());
				compromissoService.editarCompromisso(compromisso, Sessao.getUsuario().getIdUsuario());
			}
			JOptionPane.showMessageDialog(this, "Compromisso salvo com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
			this.dispose();
			new CompromissoWindow(agenda).setVisible(true);

		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao salvar compromisso", "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void buscarUsuarios() {
		try {
			DefaultListModel model = new DefaultListModel();

			List<String> usuarios;
			usuarios = usuarioService.buscarUsuarios();

			for (String usuario : usuarios) {
				if(!usuario.equals(Sessao.getUsuario().getNomeUsuario())) {
					model.addElement(usuario);
				}
			}
			listaConvidados.setModel(model);
		} catch (SQLException | IOException e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(this, "Erro ao recuperar convidados", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private Timestamp converterTimestamp(String um, String dois) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
			Date date = sdf.parse(um + " " + dois);
			Instant ins = date.toInstant();

			return Timestamp.from(ins);

		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Erro ao converter data", "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	private Timestamp verificarNotificacao(Timestamp data) {
		Instant ins = data.toInstant();
		if (cbNotificacao.getSelectedIndex() == 1) {
			ins = ins.minusSeconds(900);
		} else if (cbNotificacao.getSelectedIndex() == 2) {
			ins = ins.minusSeconds(1800);
		} else {
			ins = ins.minusSeconds(3600);
		}
		
		return Timestamp.from(ins);
	}

	private void criarMascara() {
		try {
			mascaraData = new MaskFormatter("##/##/####");
			mascaraHora = new MaskFormatter("##:##");
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Erro ao criar máscara de data", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void preencherCampos(Compromisso compromisso) {
		btnCadastrar.setText("Salvar");
		lblTituloPagina.setText("EDITAR COMPROMISSO");
		txtTitulo.setText(compromisso.getTitulo());
		txtDescricao.setText(compromisso.getDescricao());
		txtLocal.setText(compromisso.getLocal());
		this.compromissoAtual = compromisso;
		
		int[] indices = new int[compromisso.getConvidados().size()];
        int counter = 0;
		for(int i=0; i<listaConvidados.getModel().getSize(); i++) {
			if(compromisso.getConvidados().contains(listaConvidados.getModel().getElementAt(i))) {
				indices[counter] = i;
                counter++;
			}
		}
		listaConvidados.setSelectedIndices(indices);
	}

	private void initComponents() {
		setType(Type.POPUP);
		setResizable(false);
		setTitle("Compromisso");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 382, 599);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblDataInicio = new JLabel("Data de Inicio");
		lblDataInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDataInicio.setBounds(20, 100, 155, 14);
		contentPane.add(lblDataInicio);

		txtTitulo = new JTextField();
		txtTitulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtTitulo.setColumns(10);
		txtTitulo.setBounds(20, 69, 326, 20);
		contentPane.add(txtTitulo);

		txtDataInicio = new JFormattedTextField(mascaraData);
		txtDataInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDataInicio.setBounds(20, 117, 155, 20);
		contentPane.add(txtDataInicio);

		lblTitulo = new JLabel("Título");
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTitulo.setBounds(20, 50, 155, 14);
		contentPane.add(lblTitulo);

		lblLocal = new JLabel("Local");
		lblLocal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLocal.setBounds(20, 196, 155, 14);
		contentPane.add(lblLocal);

		lblNotificacao = new JLabel("Notificar");
		lblNotificacao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNotificacao.setBounds(191, 336, 155, 14);
		contentPane.add(lblNotificacao);

		btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarOuEditarCompromisso();
			}
		});
		btnCadastrar.setForeground(Color.BLACK);
		btnCadastrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCadastrar.setBackground(UIManager.getColor("Button.background"));
		btnCadastrar.setBounds(217, 526, 129, 23);
		contentPane.add(btnCadastrar);

		lblTituloPagina = new JLabel("CADASTRAR COMPROMISSO");
		lblTituloPagina.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTituloPagina.setBounds(20, 11, 326, 21);
		contentPane.add(lblTituloPagina);

		separator = new JSeparator();
		separator.setBounds(20, 30, 326, 2);
		contentPane.add(separator);

		txtLocal = new JTextField();
		txtLocal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtLocal.setColumns(10);
		txtLocal.setBounds(20, 212, 326, 20);
		contentPane.add(txtLocal);

		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new CompromissoWindow(agenda).setVisible(true);
			}
		});
		btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnVoltar.setBounds(20, 526, 89, 23);
		contentPane.add(btnVoltar);

		lblDataFinal = new JLabel("Data de Término");
		lblDataFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDataFinal.setBounds(20, 148, 155, 14);
		contentPane.add(lblDataFinal);

		txtDataFinal = new JFormattedTextField(mascaraData);
		txtDataFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDataFinal.setBounds(20, 165, 155, 20);
		contentPane.add(txtDataFinal);

		lblHoraInicio = new JLabel("Hora de Inicio");
		lblHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHoraInicio.setBounds(191, 100, 155, 14);
		contentPane.add(lblHoraInicio);

		txtHoraInicio = new JFormattedTextField(mascaraHora);
		txtHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtHoraInicio.setBounds(191, 117, 155, 20);
		contentPane.add(txtHoraInicio);

		lblHoraFinal = new JLabel("Hora de Término");
		lblHoraFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHoraFinal.setBounds(191, 148, 155, 14);
		contentPane.add(lblHoraFinal);

		txtHoraFinal = new JFormattedTextField(mascaraHora);
		txtHoraFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtHoraFinal.setBounds(191, 165, 155, 20);
		contentPane.add(txtHoraFinal);

		cbNotificacao = new JComboBox();
		cbNotificacao.setModel(
				new DefaultComboBoxModel(new String[] {"", "5", "15 min antes", "30 min antes", "1 hora antes"}));
		cbNotificacao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cbNotificacao.setBounds(191, 352, 155, 22);
		contentPane.add(cbNotificacao);

		lblConvidados = new JLabel("Convidados");
		lblConvidados.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConvidados.setBounds(20, 336, 155, 14);
		contentPane.add(lblConvidados);

		listaConvidados = new JList();
		listaConvidados.setModel(new AbstractListModel() {
			String[] values = new String[] {};

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listaConvidados.setFont(new Font("Tahoma", Font.PLAIN, 14));
		listaConvidados.setBounds(20, 353, 155, 162);
		contentPane.add(listaConvidados);

		lblDescricao = new JLabel("Descrição");
		lblDescricao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDescricao.setBounds(20, 243, 155, 14);
		contentPane.add(lblDescricao);

		txtDescricao = new JTextArea();
		txtDescricao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtDescricao.setBounds(20, 261, 326, 64);
		contentPane.add(txtDescricao);

		setLocationRelativeTo(null);
	}
}
