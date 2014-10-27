package Worker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * This class carries out the bash commands for adding subtitle to a video file.
 * 
 * @author andrew
 *
 */

public class SubtitleWorker extends SwingWorker<String, Integer> {

	private String _vidName;
	private String _subName;
	private String _outName;
	private JButton _exportButton;
	private boolean _success;
	private int _complete;
	private Process process;
	private ProcessBuilder _builder;

	public SubtitleWorker(String vidName, String subName, String outName, JButton exportButton) {
		_vidName = vidName;
		_subName = subName;
		_outName = outName;
		_exportButton = exportButton;

		_builder = new ProcessBuilder("/bin/bash", "-c", "avconv -i "+ "\""+ _vidName + "\"" + " -i " + "\"" + _subName + "\"" 
				+ " -vcodec copy -acodec copy -scodec copy -metadata:s:s:0 language=eng " + "\"" + _outName + "\"" 
				+ ".mkv && echo \"Successful\" || echo \"Error\"");

		_builder.redirectErrorStream(true);
	}
	
	@Override
	protected String doInBackground() throws Exception {

		process = _builder.start();

		InputStream stdout = process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));

		String line = null;

		while ((line = stdoutBuffered.readLine()) != null ) {
			if(line.equals("Complete")){
				_success = true;
			}else{
				_success = false;
			}
		}
		_complete = process.exitValue();
		return line;
	}
	
	@Override
	protected void done(){
		//when the process is done, enable the add subtitle button
		_exportButton.setEnabled(true);
		if(_complete == 0){
			//when successful
			if(_success == true){
				JOptionPane.showMessageDialog(null, "Video Exported!");
			}
		} else if(_complete > 0){
			JOptionPane.showMessageDialog(null, "Error Encountered");
		}
	}

}
