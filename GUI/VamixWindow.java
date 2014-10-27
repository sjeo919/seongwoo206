package GUI;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This is our main frame where the user can choose what action to take.
 * The user can pick either Download, Extract, or Play option.
 * 
 *  @author Andrew Jeong
 * 
 * */

public class VamixWindow extends JFrame {
	
	private JLabel _image = new JLabel();
	private JButton _download = new JButton();
	private JButton _play = new JButton("");
	private JButton _extract = new JButton("");
	private JButton _readme = new JButton("");
	private JLabel lblDownload = new JLabel("Download");
	private JLabel lblPlay = new JLabel("Play");
	private JLabel lblExtract = new JLabel("Extract");
	private JLabel lblQuit = new JLabel("Help");
	private JLabel lblWelcomTo = new JLabel("Welcom to VAMIX! Please select one from following");
	private String _dirPath;
	private JPanel contentPane;

	private VamixWindow() {
		
		//if download button is pressed, open a new frame
		_download.addActionListener(new ActionListener() {
			
			JFrame downloadFrame = null;
			
			public void actionPerformed(ActionEvent e) {
				
				//if download was pressed and no frame came up, open the frame
				if(downloadFrame == null){
					downloadFrame = new DownloadFrame();
				}

				downloadFrame.setVisible(!downloadFrame.isVisible());
				
			}
		});
		
		//when the play button is pressed, open a new PlayFrame
		_play.addActionListener(new ActionListener() {
			
			JFrame playFrame = null;
			
			public void actionPerformed(ActionEvent e) {
				if(playFrame == null || playFrame.isVisible() == false){
					JFileChooser chooseFile = new JFileChooser();
					//filter the file types
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio & Video files", "mp4", "avi"
							, "mp3", "mov", "aac", "mkv");
					chooseFile.setFileFilter(filter);
					
					//set directory to be current directory
					File dir = new File(System.getProperty("user.dir"));
					chooseFile.setCurrentDirectory(dir);
					int result = chooseFile.showDialog(null, null);

					if (chooseFile.getSelectedFile() == null) {
						try {
							_dirPath = null;
							playFrame.dispose();
						} catch (NullPointerException arg0) {
							// do nothing
						}
					} else if(result == JFileChooser.APPROVE_OPTION) {
						if(chooseFile.getSelectedFile() != null){

							_dirPath = chooseFile.getSelectedFile().getAbsolutePath();
							
						}
					}
					if (_dirPath != null) {
						// open a new PlayFrame
						playFrame = new PlayFrame(_dirPath);
					}
				}
			}
		});
		
		//when the extract option is chosen, open a new ExtractFrame
		_extract.addActionListener(new ActionListener(){

			JFrame extractFrame = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(extractFrame == null){
					extractFrame = new ExtractFrame();
				}
				extractFrame.setVisible(!extractFrame.isVisible());
			}
					
		});
		
		//when Help button is clicked, open the ReadMe window
		_readme.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ReadMe readme = new ReadMe();
				readme.setVisible(true);
			}
					
		});
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				VamixWindow vamix = new VamixWindow();
				vamix.createAndShowGUI();
			}
		});
	}
	
	// this method builds and shows GUI
	protected void createAndShowGUI() {
		
		VamixWindow mainframe = new VamixWindow();
		
		mainframe.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setSize(450, 322);
		setBounds(100, 100, 450, 322);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		_image.setIcon(new ImageIcon("./src/Icon/vamix.png"));
		_image.setBounds(138, 11, 168, 148);
		contentPane.add(_image);
		
		_download.setIcon(new ImageIcon("./src/Icon/download.png"));
		_download.setForeground(Color.WHITE);
		_download.setBackground(Color.WHITE);
		_download.setBounds(37, 192, 64, 64);
		contentPane.add(_download);
		
		_play.setIcon(new ImageIcon("./src/Icon/play.png"));
		_play.setBounds(138, 192, 64, 64);
		contentPane.add(_play);
		
		_extract.setIcon(new ImageIcon("./src/Icon/extract.png"));
		_extract.setForeground(Color.WHITE);
		_extract.setBackground(Color.WHITE);
		_extract.setBounds(239, 192, 64, 64);
		contentPane.add(_extract);
		
		_readme.setIcon(new ImageIcon("./src/Icon/readme.png"));
		_readme.setForeground(Color.WHITE);
		_readme.setBackground(Color.WHITE);
		_readme.setBounds(340, 192, 64, 64);
		contentPane.add(_readme);
		
		lblDownload.setHorizontalAlignment(SwingConstants.CENTER);
		lblDownload.setFont(new Font("Tekton Pro Ext", Font.BOLD, 12));
		lblDownload.setBounds(27, 261, 80, 20);
		contentPane.add(lblDownload);
		
		lblPlay.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlay.setFont(new Font("Tekton Pro Ext", Font.BOLD, 12));
		lblPlay.setBounds(138, 261, 64, 20);
		contentPane.add(lblPlay);
		
		lblExtract.setHorizontalAlignment(SwingConstants.CENTER);
		lblExtract.setFont(new Font("Tekton Pro Ext", Font.BOLD, 12));
		lblExtract.setBounds(239, 261, 64, 20);
		contentPane.add(lblExtract);
		
		lblQuit.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuit.setFont(new Font("Tekton Pro Ext", Font.BOLD, 12));
		lblQuit.setBounds(340, 261, 64, 20);
		contentPane.add(lblQuit);
		
		lblWelcomTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomTo.setFont(new Font("Tekton Pro", Font.BOLD, 14));
		lblWelcomTo.setBounds(10, 156, 424, 14);
		contentPane.add(lblWelcomTo);
		
		mainframe.add(contentPane);
		
		mainframe.setVisible(true);
	}
}
