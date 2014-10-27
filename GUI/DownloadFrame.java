package GUI;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Worker.DownloadWorker;

/**
 * The DownloadFrame class is responsible for receiving necessary inputs for downloading.
 * 
 * @author Andrew Jeong
 *
 */

public class DownloadFrame extends JFrame implements ActionListener{

	//Declaration of all the necessary fields and objects
	private JTextField _text = new JTextField(25);
	private JButton _download = new JButton("Download");
	private JButton _cancel = new JButton("Cancel");
	private String _url;
	private FlowLayout _layout = new FlowLayout();
	private JProgressBar _progressBar = new JProgressBar();
	
	private JPanel _listOp = new JPanel();
	private JLabel _enterURL = new JLabel("Enter URL to download");
	
	private JLabel _openSource = new JLabel("<html>Is this an open-source URL?<html>");
	private JPanel _listPane = new JPanel();
	private JRadioButton _no = new JRadioButton("no", true);
	private JRadioButton _yes = new JRadioButton("yes");
	
	private int _override = 3;
	
	// constructor is defined here.
	DownloadFrame (){
		
		
		
		setResizable(false);
		//setting up the GUI
		setTitle("DOWNLOAD");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400,200);
		setLayout(_layout);
		_enterURL.setFont(new Font("Tekton Pro Ext", Font.BOLD, 14));
		_openSource.setFont(new Font("Tekton Pro Ext", Font.BOLD, 14));
		add(_enterURL);
		add(_text, BorderLayout.CENTER);
		add(_progressBar);

		_listPane.add(_download);
		_listPane.add(_cancel);

		_listOp.add(_openSource);
		_listOp.add(_no);
		_listOp.add(_yes);

		add(_listOp);
		ButtonGroup group = new ButtonGroup();
		group.add(_no);
		group.add(_yes);

		add(_listPane, BorderLayout.CENTER);
		add(_cancel, BorderLayout.WEST);
		//pressing download button will start downloading.
		_download.addActionListener(this);
		
		//pushing the back button will close the download frame
		_cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//obtain url from the text field
		_url = _text.getText();

		//if the URL field is empty, ask for it by sending a message
		if (_url.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please enter the URL");
		}

		//URL should contain "http://"
		if (_url.contains("http://") ) {
			if (_url.endsWith(".mp4") || _url.endsWith(".mp3") || _url.endsWith(".avi") || _url.endsWith(".mov")
					|| _url.endsWith(".aac") || _url.endsWith(".flv")) {
				String fileName = _url.substring( _url.lastIndexOf('/')+1, _url.length() );
				File f = new File (fileName);

				//make sure download only happens if it is from open source
				if(_yes.isSelected()){	

					//if a file with the same base name already exists, ask if intention is to override
					if (f.exists()){
						int o = JOptionPane.showConfirmDialog(null, "<html>File already exists. Do you wish to override? <br> no will resume previous download<br> cancel will cancel Download <html>");

						if ( o == JOptionPane.YES_OPTION){
							_override = 0;
							f.delete();
						}	
						else if( o == JOptionPane.NO_OPTION){
							_override = 1;	
						}
						else{
							// if intention is not to override, cancel download
							JOptionPane.showMessageDialog(null, "Download Cancelled.");
						}
					}
					//commence download if all conditions are met
					if(_override == 1 || _override == 0 || !(f.exists())){				
						DownloadWorker dworker = new DownloadWorker(_url, _override, _progressBar);
						_cancel.addActionListener(dworker);
						dworker.execute();

					}
				}
				//if URL is not open source, display relevant message box
				else{
					JOptionPane.showMessageDialog(DownloadFrame.this, "Only open-source URLs can be downloaded");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please enter the URL in the right format");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please enter the URL in the right format");
		}
		
	}
}