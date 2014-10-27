package Worker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class GIFWorker extends SwingWorker<String,Integer> {

	private String _startTime;
	private String _duration;
	private String _fileName;
	private String _outputName;
	private boolean _success;
	private int _complete;
	private Process process;
	private ProcessBuilder _builder;
	private JButton _exportButton;
	
	public GIFWorker(String startTime, String duration, String outputName, String fileName, JButton exportButton) {
		_startTime = startTime;
		_duration = duration;
		_outputName = outputName;
		_fileName = fileName;
		_exportButton = exportButton;
		
		_builder = new ProcessBuilder("/bin/bash", "-c", "rm -rf frames ; mkdir frames ; avconv -i " + "\"" + _fileName + "\"" + " -ss " + _startTime + " -t "
				+ _duration + " frames/outputframe_%03d.jpg ; sleep 3 ; convert -resize 427x240 frames/outputframe_%03d.jpg[056-172] "
				+ _outputName + ".gif && echo \"Complete\" || echo \"Error Encountered\" ; rm -rf frames");
		
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
		_exportButton.setEnabled(true);
		if(_complete == 0){
			JOptionPane.showMessageDialog(null, "GIF Exported!");
		} else if(_complete > 0){
			JOptionPane.showMessageDialog(null, "Error Encountered");
		}
	}
}
