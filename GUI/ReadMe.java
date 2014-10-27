package GUI;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ReadMe extends JFrame {

	private JPanel contentPane;
	private JTextArea _txtField;

	public ReadMe() {
		 CreateAndShowGUI();
		try {
			FileReader reader = new FileReader("./src/README.txt");
			BufferedReader br = new BufferedReader(reader);
			_txtField.read(br, null);
			br.close();
			_txtField.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void CreateAndShowGUI() {
		setTitle("Read Me");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		_txtField = new JTextArea();
		_txtField.setEditable(false);
		_txtField.setLineWrap(true);
		contentPane.add(_txtField, BorderLayout.CENTER);
		
		JScrollPane sbrText = new JScrollPane(_txtField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(sbrText);
		setVisible(true);
	}

}
