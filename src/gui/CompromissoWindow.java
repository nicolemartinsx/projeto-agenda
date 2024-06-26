package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import entities.Agenda;
import entities.Compromisso;
import entities.Sessao;
import service.CompromissoService;

public class CompromissoWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tblCompromissos;
	private JScrollPane scrollPane;
	private JButton btnConvidados;
	private JButton btnCriarCompromisso;
	private JButton btnEditar;
	private JButton btnExcluir;
	private JButton btnVoltar;
	private JLabel lblCompromissos;
	private JSeparator separator;
	private JButton btnImportar;
	private JButton btnExportar;

	private Agenda agenda;
	private CompromissoService compromissoService = new CompromissoService();
	private List<Compromisso> compromissos = new ArrayList<>();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public CompromissoWindow(Agenda agenda) {
		this.agenda = agenda;
		initComponents();
		buscarCompromissos();
	}

	private void cadastrarCompromisso() {
		this.dispose();
		new CadastrarCompromissoWindow(agenda).setVisible(true);
		buscarCompromissos();
	}

	private void editarCompromisso() {
		this.dispose();
		CadastrarCompromissoWindow editar = new CadastrarCompromissoWindow(agenda);
		editar.preencherCampos(this.buscarCompromisso((int) tblCompromissos.getValueAt(tblCompromissos.getSelectedRow(),
				tblCompromissos.getColumnModel().getColumnIndex("ID"))));
		editar.setVisible(true);
		buscarCompromissos();
	}

	private Compromisso buscarCompromisso(int idCompromisso) {
		try {
			return compromissoService.buscarCompromisso(idCompromisso);

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao obter compromisso", "Erro", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	private void buscarCompromissos() {
		try {
			this.compromissos = compromissoService.buscarCompromissos(this.agenda.getIdAgenda());
			DefaultTableModel modelo = (DefaultTableModel) tblCompromissos.getModel();
			modelo.fireTableDataChanged();
			modelo.setRowCount(0);

			for (Compromisso compromisso : compromissos) {
				String dataInicio = sdf.format(compromisso.getDataInicio());
				String dataTermino = sdf.format(compromisso.getDataTermino());

				modelo.addRow(new Object[] { compromisso.getIdCompromisso(), compromisso.getTitulo(),
						compromisso.getDescricao(), dataInicio, dataTermino, compromisso.getLocal() });
			}

		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao obter compromissos", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void visualizarConvidados() {
		Compromisso compromisso = this.buscarCompromisso((int) tblCompromissos
				.getValueAt(tblCompromissos.getSelectedRow(), tblCompromissos.getColumnModel().getColumnIndex("ID")));
		String convidados = "Convidados: ";
		for (String convidado : compromisso.getConvidados()) {
			convidados = convidados + "\n" + convidado;
		}
		JOptionPane.showMessageDialog(null, convidados, "Convidados", JOptionPane.DEFAULT_OPTION);
	}

	private void importarCompromissos() {
		try {

			JFileChooser seletor = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("Documento CSV (*.csv)", "csv");
			seletor.setFileFilter(filtro);
			int retorno = seletor.showOpenDialog(null);

			if (retorno == JFileChooser.APPROVE_OPTION) {
				File csvFile = seletor.getSelectedFile();

				BufferedReader br = new BufferedReader(new FileReader(csvFile));
				String linha;
				br.readLine();

				while ((linha = br.readLine()) != null) {
					Compromisso compromisso = new Compromisso();

					String[] valores = linha.split(",");

					compromisso.setTitulo(valores[0]);
					compromisso.setDescricao(valores[1]);
					compromisso.setDataInicio(Timestamp.valueOf(valores[2]));
					compromisso.setDataTermino(Timestamp.valueOf(valores[3]));
					compromisso.setLocal(valores[4]);
					compromisso.setAgenda(agenda);

					compromissoService.cadastrarCompromisso(compromisso, Sessao.getUsuario().getIdUsuario());
				}
				JOptionPane.showMessageDialog(this, "Compromissos salvos!", "Sucesso!",
						JOptionPane.INFORMATION_MESSAGE);
			}

			buscarCompromissos();

		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao importar compromissos", "Erro", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void exportarCompromissos() {
		try {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Salvar Compromissos em CSV");
			fileChooser.setSelectedFile(new File("Compromissos" + ".csv"));
			fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo .CSV", "csv", "text"));

			int userSelection = fileChooser.showSaveDialog(null);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();

				BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));
				writer.write("Título,Descrição,Data Início,Data Término,Local,Agenda Nome\n");

				for (Compromisso compromisso : compromissos) {

					writer.write(compromisso.getTitulo() + ",");
					writer.write(compromisso.getDescricao() + ",");
					writer.write(compromisso.getDataInicio() + ",");
					writer.write(compromisso.getDataTermino() + ",");
					writer.write(compromisso.getLocal() + ",");
					writer.write(agenda.getNome() + ",");
					writer.newLine();
				}

				writer.close();
				JOptionPane.showMessageDialog(this, "Compromissos salvos!", "Sucesso!",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao exportar compromissos", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void excluirCompromisso() {
		try {
			int op = JOptionPane.showConfirmDialog(null, "Deseja excluir esse compromisso?", "Confirmar exclusão",
					JOptionPane.YES_NO_OPTION);
			if (op == 0) {
				op = compromissoService
						.excluirCompromisso((Integer) tblCompromissos.getValueAt(tblCompromissos.getSelectedRow(),
								tblCompromissos.getColumnModel().getColumnIndex("ID")));
				if (op != 0) {
					JOptionPane.showMessageDialog(this, "Compromisso excluido!", "Sucesso!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				buscarCompromissos();
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao excluir compromisso", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initComponents() {
		setResizable(false);
		setTitle("Compromissos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1034, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(18, 55, 839, 299);
		contentPane.add(scrollPane);

		tblCompromissos = new JTable();
		tblCompromissos.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tblCompromissos.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "T\u00EDtulo", "Descri\u00E7\u00E3o", "Inicio", "T\u00E9rmino", "Local" }) {
			Class[] columnTypes = new Class[] { Integer.class, String.class, String.class, String.class, String.class,
					String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblCompromissos.getColumnModel().getColumn(0).setPreferredWidth(15);
		tblCompromissos.getColumnModel().getColumn(0).setMinWidth(10);
		scrollPane.setViewportView(tblCompromissos);

		btnConvidados = new JButton("Convidados");
		btnConvidados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visualizarConvidados();
			}
		});
		btnConvidados.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConvidados.setBounds(867, 80, 130, 23);
		contentPane.add(btnConvidados);

		btnCriarCompromisso = new JButton("Criar");
		btnCriarCompromisso.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarCompromisso();
			}
		});
		btnCriarCompromisso.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCriarCompromisso.setBounds(867, 114, 130, 23);
		contentPane.add(btnCriarCompromisso);

		btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarCompromisso();
			}
		});
		btnEditar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditar.setBounds(867, 148, 130, 23);
		contentPane.add(btnEditar);

		btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluirCompromisso();
			}
		});
		btnExcluir.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExcluir.setBounds(867, 182, 130, 23);
		contentPane.add(btnExcluir);

		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new AgendaWindow().setVisible(true);
			}
		});
		btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnVoltar.setBounds(867, 331, 130, 23);
		contentPane.add(btnVoltar);

		lblCompromissos = new JLabel("COMPROMISSOS");
		lblCompromissos.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCompromissos.setBounds(18, 11, 975, 21);
		contentPane.add(lblCompromissos);

		separator = new JSeparator();
		separator.setBounds(18, 30, 975, 2);
		contentPane.add(separator);

		btnImportar = new JButton("Importar .CSV");
		btnImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importarCompromissos();
			}
		});
		btnImportar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnImportar.setBounds(867, 216, 130, 23);
		contentPane.add(btnImportar);

		btnExportar = new JButton("Exportar .CSV");
		btnExportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportarCompromissos();
			}
		});
		btnExportar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExportar.setBounds(867, 250, 130, 23);
		contentPane.add(btnExportar);

		setLocationRelativeTo(null);
	}
}
