package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import Worker.GIFWorker;

/**
 * This class opens a window where the necessary inputs are entered for the operation of
 * exporting a gif from a video file.
 * 
 * @author andrew
 *
 */

public class GIFFrame extends JFrame {

	private JPanel contentPane;
	private JTextField startTime;
	private JTextField duration;
	private JButton _exportButton;
	private JButton _closeButton;
	private JLabel outputLabel;
	private JTextField outputName;
	
	private String _startTime;
	private String _duration;
	private String _outputName;
	private String _fileName;
	

	public GIFFrame(final String filePath) {
		createAndShowGUI();
		_fileName = filePath;
		
		_exportButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				_startTime = startTime.getText();
				_duration = duration.getText();
				_outputName = outputName.getText();
				
				// make sure all the fields are entered
				if (_startTime.isEmpty() || _duration.isEmpty() || _outputName.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter all the fields!");
				} else {
					File f = new File("./" + _outputName + ".gif");
					//check if gif with the same name already exists
					if (f.exists()) {
						JOptionPane.showMessageDialog(null, "File name already exists, please enter another name.");
					} else {
						GIFWorker gifWorker = new GIFWorker(_startTime, _duration, _outputName, _fileName, _exportButton);
						gifWorker.execute();
						//disable the export button if the process is started.
						_exportButton.setEnabled(false);
					}
				}
			}
		});
		
		_closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
	}
	
	protected void createAndShowGUI() {
		//construction of GUI
		setResizable(false);
		setTitle("Export Gif");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 425, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel startTimeLabel = new JLabel("Enter the time to start the extraction from (hh:mm:ss) :");
		startTimeLabel.setBounds(12, 12, 414, 15);
		contentPane.add(startTimeLabel);
		
		startTime = new JTextField();
		startTime.setText("00:00:00");
		startTime.setBounds(12, 41, 382, 15);
		contentPane.add(startTime);
		startTime.setColumns(10);
		
		JLabel durationLabel = new JLabel("How long do you want the Gif to cover for?");
		durationLabel.setBounds(12, 68, 414, 15);
		contentPane.add(durationLabel);
		
		duration = new JTextField();
		duration.setText("in seconds (Max of 10 seconds)");
		duration.setBounds(12, 95, 382, 15);
		contentPane.add(duration);
		duration.setColumns(10);
		
		_exportButton = new JButton("Export");
		_exportButton.setBounds(81, 186, 117, 25);
		contentPane.add(_exportButton);
		
		_closeButton = new JButton("Close");
		_closeButton.setBounds(221, 186, 117, 25);
		contentPane.add(_closeButton);
		
		outputLabel = new JLabel("Output file name (without the extension):");
		outputLabel.setBounds(12, 122, 389, 15);
		contentPane.add(outputLabel);
		
		outputName = new JTextField();
		outputName.setBounds(12, 149, 382, 15);
		contentPane.add(outputName);
		outputName.setColumns(10);
	}
}
