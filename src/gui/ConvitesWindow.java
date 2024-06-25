package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import entities.Compromisso;
import entities.Sessao;
import service.CompromissoService;

public class ConvitesWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblConvites;
	private JSeparator separator;
	private JTable tblConvites;
	private JScrollPane scrollPane;
	private JButton btnAceitar;
	private JButton btnRecusar;
	private JButton btnVoltar;

	private CompromissoService compromissoService = new CompromissoService();
	private List<Compromisso> convites = new ArrayList<>();
	private List<Agenda> agendas = new ArrayList<>();

	public ConvitesWindow(List<Agenda> agendas) {
		initComponents();
		buscarConvites();
		this.agendas = agendas;
	}

	private void buscarConvites() {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			this.convites = compromissoService.buscarConvites(Sessao.getUsuario().getIdUsuario());

			DefaultTableModel modelo = (DefaultTableModel) tblConvites.getModel();
			modelo.fireTableDataChanged();
			modelo.setRowCount(0);

			for (Compromisso convite : convites) {
				String dataInicio = sdf.format(convite.getDataInicio());
				String dataTermino = sdf.format(convite.getDataTermino());

				modelo.addRow(new Object[] { convite.getIdCompromisso(), convite.getTitulo(), convite.getDescricao(),
						dataInicio, dataTermino, convite.getLocal() });
			}
		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao obter convites", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void aceitarConvite() {
		try {
			
			JComboBox<Agenda> comboBox = new JComboBox<>();
			DefaultComboBoxModel<Agenda> model = new DefaultComboBoxModel();
			
			for(Agenda agenda : agendas) {
				model.addElement(agenda);
			}
			
			comboBox.setModel(model);

			Object[] mensagem = { "Selecione uma agenda:", comboBox };

			int option = JOptionPane.showConfirmDialog(null, mensagem, "Escolha uma agenda", JOptionPane.OK_CANCEL_OPTION);

			if (option == JOptionPane.OK_OPTION) {
				Agenda agendaSelecionada = (Agenda) comboBox.getSelectedItem();
				
				for (Compromisso convite : convites) {
					if (convite.getIdCompromisso() == ((Integer) tblConvites.getValueAt(tblConvites.getSelectedRow(),
							tblConvites.getColumnModel().getColumnIndex("ID")))) {
						convite.setAgenda(agendaSelecionada);
						compromissoService.cadastrarCompromisso(convite, Sessao.getUsuario().getIdUsuario());
					}
				}
				
				JOptionPane.showMessageDialog(this, "Compromisso salvo com sucesso!", "Sucesso!",JOptionPane.INFORMATION_MESSAGE);
			}

			buscarConvites();
			
		} catch (SQLException | IOException e) {

			JOptionPane.showMessageDialog(this, "Erro ao salvar compromisso", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void recusarConvite() {
		try {
			int idCompromisso = compromissoService.excluirCompromisso((Integer) tblConvites
					.getValueAt(tblConvites.getSelectedRow(), tblConvites.getColumnModel().getColumnIndex("ID")));
			if (idCompromisso != -1) {
				compromissoService.recusarConvite(idCompromisso, Sessao.getUsuario().getIdUsuario());
			} else {
				JOptionPane.showMessageDialog(null, "Seleciona uma linha da tabela!", null,
						JOptionPane.INFORMATION_MESSAGE);
			}
			buscarConvites();

		} catch (SQLException | IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao recusar convite", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void initComponents() {
		setResizable(false);
		setTitle("Convites");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 698, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblConvites = new JLabel("CONVITES");
		lblConvites.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblConvites.setBounds(10, 11, 199, 20);
		contentPane.add(lblConvites);

		separator = new JSeparator();
		separator.setBounds(10, 29, 662, 2);
		contentPane.add(separator);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 55, 662, 320);
		contentPane.add(scrollPane);

		tblConvites = new JTable();
		tblConvites.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tblConvites.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "T\u00EDtulo", "Descri\u00E7\u00E3o", "Inicio", "T\u00E9rmino", "Local" }) {
			Class[] columnTypes = new Class[] { Integer.class, String.class, String.class, String.class, String.class,
					String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		tblConvites.getColumnModel().getColumn(0).setPreferredWidth(15);
		scrollPane.setViewportView(tblConvites);

		btnAceitar = new JButton("Aceitar");
		btnAceitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceitarConvite();
			}
		});
		btnAceitar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAceitar.setBounds(583, 386, 89, 23);
		contentPane.add(btnAceitar);

		btnRecusar = new JButton("Recusar");
		btnRecusar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recusarConvite();
			}
		});
		btnRecusar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnRecusar.setBounds(485, 386, 89, 23);
		contentPane.add(btnRecusar);

		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new AgendaWindow().setVisible(true);
			}
		});
		btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnVoltar.setBounds(10, 386, 89, 23);
		contentPane.add(btnVoltar);

		setLocationRelativeTo(null);
	}
}
