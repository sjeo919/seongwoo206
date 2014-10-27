package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import Worker.SubtitleWorker;

/**
 * This class opens up a window where necessary inputs are entered for the operation of
 * merging subtitle to a video.
 * 
 * @author andrew
 *
 */

public class AddSubtitle extends JFrame {

	private JPanel contentPane;
	private JTextField _fNameField;
	private JTextField _subNameField;
	private JTextField _outNameField;

	private JButton _browseVid = new JButton("Browse");
	private JButton _browseSub = new JButton("Browse");
	private JButton _start = new JButton("Export");
	private JButton _cancel = new JButton("Cancel");

	public AddSubtitle() {
		CreateAndShowGUI();

		_browseVid.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooseFile = new JFileChooser();
				//limit the files only to certain video files
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Video files", "mp4", "avi"
						, "mov", "aac", "mkv");
				chooseFile.setFileFilter(filter);

				//set directory to be current directory
				File dir = new File(System.getProperty("user.dir"));
				chooseFile.setCurrentDirectory(dir);
				int result = chooseFile.showDialog(null,null);

				if (result == JFileChooser.APPROVE_OPTION){ 
					if(chooseFile.getSelectedFile() != null){

						String dirPath = chooseFile.getSelectedFile().getAbsolutePath();
						_fNameField.setText(dirPath);
					}
				}
			}

		});

		_browseSub.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooseFile = new JFileChooser();

				//set directory to be current directory
				File dir = new File(System.getProperty("user.dir"));
				chooseFile.setCurrentDirectory(dir);
				int result = chooseFile.showDialog(null,null);

				if (result == JFileChooser.APPROVE_OPTION){ 
					if(chooseFile.getSelectedFile() != null){

						String dirPath = chooseFile.getSelectedFile().getAbsolutePath();
						_subNameField.setText(dirPath);
					}
				}
			}
		});
		_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//when the start button is pressed, if all the fields are entered, start processing
				if (_fNameField.getText()!="" && _subNameField.getText()!="" && _outNameField.getText()!="") {
					SubtitleWorker subWorker = new SubtitleWorker(_fNameField.getText(), _subNameField.getText(), _outNameField.getText(), _start);
					subWorker.execute();
					//disable the start button when the process is started
					_start.setEnabled(false);
				} else {
					JOptionPane.showMessageDialog(null, "Please check your entries");
				}
			}

		});
		
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		

	}

	//this method constructs the GUI component of the AddSubtitle window
	private void CreateAndShowGUI() {
		setTitle("Export Video");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 269);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSelectTheVideo = new JLabel("Select the Video file: ");
		lblSelectTheVideo.setBounds(12, 12, 200, 15);
		contentPane.add(lblSelectTheVideo);

		_fNameField = new JTextField(35);
		_fNameField.setEditable(false);
		_fNameField.setBounds(12, 39, 412, 24);
		contentPane.add(_fNameField);

		_browseVid.setBounds(307, 7, 117, 25);
		contentPane.add(_browseVid);

		JLabel lblSelectTheSubtitle = new JLabel("Select the Subtitle file (.srt) :");
		lblSelectTheSubtitle.setBounds(12, 75, 253, 15);
		contentPane.add(lblSelectTheSubtitle);

		_subNameField = new JTextField(35);
		_subNameField.setEditable(false);
		_subNameField.setBounds(12, 102, 412, 24);
		contentPane.add(_subNameField);

		_browseSub.setBounds(307, 70, 117, 25);
		contentPane.add(_browseSub);

		JLabel lblNameOfThe = new JLabel("Name of the output Video :");
		lblNameOfThe.setBounds(12, 138, 217, 15);
		contentPane.add(lblNameOfThe);

		_outNameField = new JTextField(35);
		_outNameField.setBounds(12, 165, 412, 24);
		contentPane.add(_outNameField);

		_start.setBounds(72, 203, 117, 25);
		contentPane.add(_start);

		_cancel.setBounds(261, 203, 117, 25);
		contentPane.add(_cancel);
	}
}
