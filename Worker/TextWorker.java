package Worker;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * This is a worker class that supports the processing of adding text task.
 * 
 * @author andrew
 *
 */

public class TextWorker extends SwingWorker<String, Integer>{

	private String _vidName;
	private String _newName;
	private String _timeS;
	private String _timeF;
	private String _textS;
	private String _textE;
	private String _font;
	private String _fontSize;
	private String _fontColor;
	private JProgressBar _progressBar = new JProgressBar();

	private int _complete;
	private int _exit;
	private int _totalLength;

	private ProcessBuilder _builder;
	private Process _process;
	
	private final JOptionPane pane = new JOptionPane("Adding Text...");
	private final JDialog dialog = pane.createDialog("Add Text to Video");

	public TextWorker(String name, String newName, String timeS, String timeF, String textS, String textE, String font, String fontSize, String fontColor, JProgressBar prg){
		_vidName = name;
		_newName = newName;
		_timeS = timeS;
		_timeF = timeF;
		_textS = textS;
		_textE = textE;
		_fontSize = fontSize;
		_fontColor = fontColor;
		_progressBar = prg;
		
		//open a JOptionPane while executing
		Thread t = new Thread(new Runnable(){
			public void run(){
				dialog.setVisible(true);
			}
		});
		
		// each of font type
		if(font.equals("Normal")){
				_font = "Ubuntu-L";
		}else if(font.equals("Italics")){
			_font = "Ubuntu_LI";
		}else if(font.equals("Bold")){
			_font = "Ubuntu-B";
		}else if(font.equals("Bold + Italics")){
			_font = "Ubuntu-BI";
		}

		t.start();
		//use ProcessBuilder class to use bash commands
		_builder = new ProcessBuilder("/bin/bash", "-c", "avconv -i "+ _vidName +" -vf \"drawtext="
				+ "fontfile='/usr/share/fonts/truetype/ubuntu-font-family/"+ _font +".ttf': text='"+ _textS +"':x=(main_w-text_w)/2: "
				+ "fontsize="+ _fontSize +":fontcolor="+ _fontColor +":draw='lt(t,"+ _timeS +")':,drawtext=fontfile='/usr/share/fonts/truetype/ubuntu-font-family/"+ _font +".ttf': "
				+ "text='" + _textE + "':x=(main_w-text_w)/2: fontsize="+ _fontSize +":fontcolor="+ _fontColor +":draw='gt(t," + _timeF + ")':\" -c:a copy " + _newName + " && echo \"Successful\" || echo \"Error\"");

		_builder.redirectErrorStream(true);


	}

	@Override
	protected String doInBackground() throws Exception {
		_process = _builder.start();

		// output information and progress to console
		InputStream stdout = _process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

		String line = null;
		boolean firstLength = false;

		while ((line = stdoutBuffered.readLine()) != null ) {

			//calculate the total length of the original file
			if (line.contains("Duration: ") && !firstLength) {
				int x = line.indexOf("Duration: ");
				String length = line.substring(x + 10, x + 21);
				int hour = 60 * 60 * 100 * Integer.parseInt(length.substring(0,2));
				int min = 60 * 100 * Integer.parseInt(length.substring(3,5));
				int sec = 100 * Integer.parseInt(length.substring(6,8));
				int cs = Integer.parseInt(length.substring(9,11));
				int totalLength = hour + min + sec + cs;
				_totalLength = totalLength;
				firstLength = true;
			}
			
			if(line.equals("Successful")){
				_complete = 0;
			}else if (line.equals("Error")){
				_complete = 1;
			}
			
			// read each line that starts with "frame=" and read the time value
			if (line.contains("frame=")) {
				int index = line.indexOf(" bitrate");
				String line1 = line.substring(48, index);
				int x = line1.indexOf('.');
				String y = line1.substring(0, x) + line1.substring(x + 1) + "00";
				int num = Integer.parseInt(y);
				int percentage = (num / _totalLength);
				// publish the percentage of progress
				publish(percentage);
			}
		}
		_exit = _process.exitValue();
		return line;
	}
	
	@Override
	//update progress bar in 'chunks' according to status
	public void process(List<Integer> chunks){
		for (int percentage: chunks){
			_progressBar.setValue(percentage);
			
		}
	}

	@Override
	protected void done() {
		//if addition is completed successfully, display a message
		if(_complete == 0){
			dialog.setVisible(false);
			dialog.dispose();
			JOptionPane.showMessageDialog(null, "Add Text Complete");
		}
		//if error is found, display an error message
		else if(_complete == 1 || _exit != 0){
			dialog.setVisible(false);
			dialog.dispose();
			JOptionPane.showMessageDialog(null, "Error Encountered: Error adding text to video");
		}
	}

}