package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import entities.Agenda;
import entities.Sessao;
import service.AgendaService;
import javax.swing.ListSelectionModel;

public class AgendaWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnCriarAgenda;
	private JButton btnEditarAgenda;
	private JButton btnExcluirAgenda;
	private JButton btnVoltar;
	private JLabel lblTitulo;
	private JSeparator separator;
	private JTable tblAgendas;
	private JScrollPane scrollPane;
	private JButton btnAbrir;
	
	AgendaService agendaService = new AgendaService();
	
	public AgendaWindow() {
		initComponents();
		buscarAgendas();
	}

	private void buscarAgendas() {

		List<Agenda> agendas = new ArrayList<>();
		try {
			agendas = agendaService.buscarAgendas(Sessao.getUsuario().getIdUsuario());
			DefaultTableModel modelo = (DefaultTableModel) tblAgendas.getModel();
			modelo.fireTableDataChanged();
			modelo.setRowCount(0);

			for (Agenda agenda : agendas) {
				modelo.addRow(new Object[] { agenda.getIdAgenda(), agenda.getNome(), agenda.getDescricao() });
			}

		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao obter agendas", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cadastrarAgenda() {
		new CadastrarAgendaWindow().setVisible(true);
		buscarAgendas();
	}
	
	private void abrirAgenda() {
		Agenda agenda = this.salvarAgenda();
		this.dispose();
		new CompromissoWindow(agenda).setVisible(true);
	}

	private void editarAgenda() {
		
		Agenda agenda = this.salvarAgenda();
		CadastrarAgendaWindow edicao = new CadastrarAgendaWindow();
		edicao.preencherCampos(agenda);
		edicao.setVisible(true);
		buscarAgendas();
	}
	
	private Agenda salvarAgenda() {
		Agenda agenda = new Agenda();

		int selecao = tblAgendas.getSelectedRow();

		agenda.setIdAgenda((int) tblAgendas.getValueAt(selecao, tblAgendas.getColumnModel().getColumnIndex("ID")));
		agenda.setNome((String) tblAgendas.getValueAt(selecao, tblAgendas.getColumnModel().getColumnIndex("Nome")));
		agenda.setDescricao(
				(String) tblAgendas.getValueAt(selecao, tblAgendas.getColumnModel().getColumnIndex("Descrição")));
		return agenda;
	}

	private void excluirAgenda() {
		try {
			int op = JOptionPane.showConfirmDialog(null, "Deseja excluir esta agenda?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
			if (op == 0) {
				op = agendaService.excluirAgenda((Integer) tblAgendas.getValueAt(tblAgendas.getSelectedRow(), tblAgendas.getColumnModel().getColumnIndex("ID")));
				if (op != 0) {
					JOptionPane.showMessageDialog(this, "Agenda excluida!", "Sucesso!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				buscarAgendas();
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(this, "Erro ao excluir agenda", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void voltar() {
		this.dispose();
		new InicioWindow().setVisible(true);
	}

	private void initComponents() {
		setResizable(false);
		setTitle("AGENDAS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 653, 416);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnCriarAgenda = new JButton("Criar");
		btnCriarAgenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cadastrarAgenda();
			}
		});
		btnCriarAgenda.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCriarAgenda.setBounds(416, 330, 95, 23);
		contentPane.add(btnCriarAgenda);

		btnEditarAgenda = new JButton("Editar");
		btnEditarAgenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarAgenda();
			}
		});
		btnEditarAgenda.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnEditarAgenda.setBounds(305, 330, 101, 23);
		contentPane.add(btnEditarAgenda);

		btnExcluirAgenda = new JButton("Excluir");
		btnExcluirAgenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluirAgenda();
			}
		});
		btnExcluirAgenda.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExcluirAgenda.setBounds(200, 330, 95, 23);
		contentPane.add(btnExcluirAgenda);

		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				voltar();
			}
		});
		btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnVoltar.setBounds(20, 330, 89, 23);
		contentPane.add(btnVoltar);

		lblTitulo = new JLabel("AGENDAS");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitulo.setBounds(20, 11, 223, 20);
		contentPane.add(lblTitulo);

		separator = new JSeparator();
		separator.setBounds(20, 30, 596, 2);
		contentPane.add(separator);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 42, 596, 267);
		contentPane.add(scrollPane);

		tblAgendas = new JTable();
		tblAgendas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblAgendas.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Nome", "Descri\u00E7\u00E3o"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblAgendas.getColumnModel().getColumn(0).setResizable(false);
		tblAgendas.getColumnModel().getColumn(0).setPreferredWidth(15);
		tblAgendas.getColumnModel().getColumn(1).setResizable(false);
		tblAgendas.getColumnModel().getColumn(2).setResizable(false);
		tblAgendas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(tblAgendas);
		
		btnAbrir = new JButton("Abrir");
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirAgenda();
			}
		});
		btnAbrir.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAbrir.setBounds(521, 330, 95, 23);
		contentPane.add(btnAbrir);

		setLocationRelativeTo(null);
	}
}
