package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton; 
import javax.swing.JFileChooser;
import javax.swing.JFrame; 
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;	
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalSliderUI;

import Function.FullScreen;
import Function.MediaPlayerListener;
import Function.VideoEditor;
import Worker.ExtractWorker;
import Worker.RewindWorker;
import uk.co.caprica.vlcj.player.MediaPlayer; 
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * This class, PlayFrame.java, is a frame class that contains multiple JButtons
 * such as fast forward, rewind, play, pause, and etc (general commands for media players)
 * It also contains a pane containing the media player component where the video is being played.
 * 
 * In the menu tab, we also provided overlay and replace options for the audio. 
 * 
 * @author Andrew Jeong
 *
 */

public class PlayFrame extends JFrame implements ChangeListener, ActionListener, MouseListener {

	static final int VOL_MIN = 0;
	static final int VOL_MAX = 120;
	static final int VOL_INIT = 80;

	private JFrame _frame = new JFrame("Vamix Player");

	private String _newName = "";
	private String _dirPath;
	private int _override = 3;

	private JButton _ff = new JButton(); 
	private JButton _skipF = new JButton();
	private JButton _rewind = new JButton(); 
	private JButton _skipB = new JButton();
	private JButton _pause = new JButton(); 
	private JButton _close = new JButton();
	private JButton _mute = new JButton();

	private JSlider _volume = new JSlider(JSlider.HORIZONTAL,VOL_MIN, VOL_MAX, VOL_INIT);

	private JLabel _vidTime = new JLabel();
	private JSlider _seekBar = new JSlider(JSlider.HORIZONTAL);

	private JPanel _panel = new JPanel();
	private JPanel _panelP = new JPanel();
	private JPanel _seek = new JPanel();
	private static String _file;
	private String _overLay;
	private RewindWorker rw;

	private EmbeddedMediaPlayerComponent _mediaPlayer; 
	private JRadioButtonMenuItem temp;
	private JMenu editSubtitle = new JMenu("SUBTITLE");

	private long _time = 4000;
	private float _speed = 1.5f;
	private int _currentVol;
	private int _value;

	private static void addPanel(JPanel panel, Container container) {

		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(panel);
	}


	PlayFrame (String file) { 
		//construction of GUI design
		_file = file;
		_frame.setTitle("VAMIX - " + _file.substring(_file.lastIndexOf("/") + 1));
		_frame.setBackground(new Color(240, 255, 240));

		_seekBar.setMinimum(0);
		_seekBar.setMaximum(1000);
		_seekBar.setValue(0);
		_seekBar.setBackground(new Color(240, 255, 240));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(240, 255, 240));
		JMenu openVid = new JMenu("OPEN VIDEO");
		JMenu editMenu = new JMenu("EDIT VIDEO");
		menuBar.add(openVid);
		menuBar.add(editMenu);
		menuBar.add(editSubtitle);

		JMenuItem open = new JMenuItem("Open New Video");
		JMenuItem overlay = new JMenuItem("Overlay Audio on video");
		JMenuItem replace = new JMenuItem("Replace Audio on video");
		JMenuItem addText = new JMenuItem("Add Text");
		JMenuItem extract = new JMenuItem("Extract the Audio");
		JMenuItem fullScreen = new JMenuItem("Open Full Screen");
		JMenuItem exportGIF = new JMenuItem("Export GIF");
		final JRadioButtonMenuItem disableSub = new JRadioButtonMenuItem("Disable subtitle");
		final JMenu subtitle = new JMenu("Select Subtitle");
		final JMenuItem addSubtitle = new JMenuItem("Add Subtitle to Video");
		
		openVid.add(open);
		editMenu.add(extract);
		editMenu.add(addText);
		editMenu.add(overlay);
		editMenu.add(replace);
		editMenu.add(fullScreen);
		editMenu.add(exportGIF);
		
		// link appropriate file path for icons
		_skipB.setIcon(new ImageIcon("./src/Icon/skipB.png"));
		_skipB.setForeground(new Color(240, 255, 240));
		_skipB.setBackground(new Color(240, 255, 240));
		_skipB.setBounds(32, 510, 64, 64);
		_rewind.setIcon(new ImageIcon("./src/Icon/rewind.png"));
		_rewind.setForeground(new Color(240, 255, 240));
		_rewind.setBackground(new Color(240, 255, 240));
		_rewind.setBounds(128, 510, 64, 64);
		_pause.setIcon(new ImageIcon("./src/Icon/pause.png"));
		_pause.setToolTipText("||");
		_pause.setForeground(new Color(240, 255, 240));
		_pause.setBackground(new Color(240, 255, 240));
		_pause.setBounds(224, 510, 64, 64);
		_ff.setIcon(new ImageIcon("./src/Icon/fforward.png"));
		_ff.setForeground(new Color(240, 255, 240));
		_ff.setBackground(new Color(240, 255, 240));
		_ff.setBounds(416, 510, 64, 64);
		_skipF.setIcon(new ImageIcon("./src/Icon/skipF.png"));
		_skipF.setForeground(new Color(240, 255, 240));
		_skipF.setBackground(new Color(240, 255, 240));
		_skipF.setBounds(512, 510, 64, 64);
		_mute.setIcon(new ImageIcon("./src/Icon/mute.png"));
		_mute.setForeground(new Color(240, 255, 240));
		_mute.setBackground(new Color(240, 255, 240));
		_mute.setBounds(608, 510, 64, 64);
		_close.setIcon(new ImageIcon("./src/Icon/quit.png"));
		_close.setForeground(new Color(240, 255, 240));
		_close.setBackground(new Color(240, 255, 240));
		_close.setBounds(936, 510, 64, 64);
		
		_panel.setBackground(new Color(240, 255, 240));
		_panel.add(_skipB);
		_panel.add(_rewind);
		_panel.add(_pause);
		_panel.add(_ff);
		_panel.add(_skipF);
		_panel.add(_mute);
		_panel.add(_close);
		_seek.setBackground(new Color(240, 255, 240));
		_seek.add(_vidTime);
		_seek.add(_seekBar);
		
		_panel.add(_volume);
		_volume.setBackground(new Color(240, 255, 240));
		_volume.setBounds(704, 530, 200, 23);

		Dimension prefSize = _seekBar.getPreferredSize();
		prefSize.width = 800;
		_seekBar.setPreferredSize(prefSize);
		_panelP.setBackground(new Color(250, 255, 240));
		_panelP.setLayout(new BoxLayout(_panelP, BoxLayout.Y_AXIS));

		addPanel(_seek, _panelP);
		addPanel(_panel, _panelP);

		_mediaPlayer = new EmbeddedMediaPlayerComponent(); 

		_frame.setLocation(100, 100);
		_frame.setSize(1050, 650);
		
		//only the play frame closes when the x button is closed.
		_frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		_frame.setVisible(true);

		//check if the file to be played is a playable media type
		if(_file.endsWith(".mp4") || _file.endsWith(".mp3")|| _file.endsWith(".avi") || _file.endsWith(".mov")|| _file.endsWith(".aac") || _file.endsWith(".mkv")){
			_frame.setContentPane(_mediaPlayer); 
			_frame.getContentPane().add(_panelP, BorderLayout.SOUTH);
			_frame.getContentPane().add(menuBar, BorderLayout.NORTH);
			_mediaPlayer.getMediaPlayer().playMedia(_file);
			_mediaPlayer.getMediaPlayer().start();
			setUpSubtitleMenu(subtitle, disableSub, addSubtitle);
			long duration = _mediaPlayer.getMediaPlayer().getLength()/1000;
			_overLay = Integer.toString((int)duration);

		}

		//add listener to the media player
		_mediaPlayer.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerListener(this));
		_mediaPlayer.getMediaPlayer().addMediaPlayerEventListener(new UpdateLabel());
		_seekBar.addMouseListener(this);

		//if the Add Text button is clicked, open a new TextAdderFrame window
		addText.addActionListener(new ActionListener() { 

			JFrame addTextFrame = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(addTextFrame == null || addTextFrame.isVisible() == false){
					int time = (int)(_mediaPlayer.getMediaPlayer().getLength()/1000);

					addTextFrame = new TextAdderFrame(_file, time);
				}
				addTextFrame.setVisible(!addTextFrame.isVisible());
			}
		});

		//when the close button is selected, close the frame.
		_close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().stop();
				_frame.setVisible(false);
				_frame.dispose();
			}
		});

		//when the pause button is pressed
		_pause.addActionListener(this);

		//when the rewind button is pressed
		_rewind.addActionListener(this);

		//when skip forward button is pressed, skip a certain length of the video
		_skipF.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().skip(_time);
			}

		});

		//when skip backward button is pressed, skip back a certain length of the video
		_skipB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				_mediaPlayer.getMediaPlayer().skip(-_time);	
			}

		});

		//when fast forward button is pressed, increase the rate of the change of the frames
		_ff.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fastForward();
			}

		});

		//when extract button is pressed,
		extract.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//ask the user to enter the new file's name
				_newName = JOptionPane.showInputDialog("Enter new name for audio file (without .mp3 extension): ");
				File fName =new File(_newName + ".mp3");
				
				//the file name cannot be null
				if (_newName != null){ 
					//if the file already exists in the directory, check if the user's intention is to override
					if (fName.exists()) {
						int o = JOptionPane.showConfirmDialog(null, "<html>File already exists. Do you wish to override? <br> no will resume previous download<br> cancel will cancel Download <html>");
						 
						//if yes, delete the previous file
						if ( o == JOptionPane.YES_OPTION){
							_override = 0;
							fName.delete();
						}	
						//if no, resume downloading from the last download
						else if( o == JOptionPane.NO_OPTION){
							_override = 1;	
						}
						else{
							// if intention is not to override, cancel download
							JOptionPane.showMessageDialog(null, "Download Cancelled.");
							_override = 3;
						}
					}
					//check if the file name contains the extension.
					if(! _newName.contains(".mp3") && !_newName.contains(".")) {
						if(_override == 1 || _override == 0 || !(fName.exists())){				
							ExtractWorker eworker = new ExtractWorker(_file, null, null, _newName);
							eworker.execute();
						}
					}
					//if the file name does include extensions, display an error message
					else if (_newName.contains(".mp3") || _newName.contains(".")) {
						JOptionPane.showMessageDialog(null, "The file name must not include the extension");
					}
				}
			}

		});

		//if mute button is pressed
		_mute.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				//set the volume to zero
				if(_mediaPlayer.getMediaPlayer().getVolume() != 0){
					_currentVol = _mediaPlayer.getMediaPlayer().getVolume();
					_mediaPlayer.getMediaPlayer().setVolume(0);
					_volume.setValue(0);
				}
				//if the volume is already set to zero, turn the volume on again
				else if(_mediaPlayer.getMediaPlayer().getVolume() == 0){
					_mediaPlayer.getMediaPlayer().setVolume(_currentVol);
					_volume.setValue(_currentVol);
				}
			}

		});

		_volume.addChangeListener(this);

		//if the overlay option in the menu bar is selected, open a VideoEditor window
		overlay.addActionListener(new ActionListener(){
			JFrame vd = null;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vd == null || vd.isVisible() == false){
					vd = new VideoEditor("Overlay", _overLay, _file);
					vd.setVisible(true);
				}

			}
		});
		
		//if the replace option in the menu bar is selected, open a VideoEditor window
		replace.addActionListener(new ActionListener(){
			JFrame vd = null;
			public void actionPerformed(ActionEvent arg0) {
				if(vd == null || vd.isVisible() == false){
					vd = new VideoEditor("Replace", _overLay, _file);
					vd.setVisible(true);
				}
			}
		});
		
		fullScreen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FullScreen fs = new FullScreen(_mediaPlayer.getMediaPlayer().mrl(), _mediaPlayer.getMediaPlayer());
			}
			
		});
		
		exportGIF.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				GIFFrame gifFrame = new GIFFrame(_file);
				gifFrame.setVisible(true);
			}
			
		});
		
		open.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser chooseFile = new JFileChooser();
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
					} catch (NullPointerException arg01) {
						// do nothing
					}
				} else if(result == JFileChooser.APPROVE_OPTION) {
					if(chooseFile.getSelectedFile() != null){

						_dirPath = chooseFile.getSelectedFile().getAbsolutePath();
						
					}
				}
				if (_dirPath != null) {
					_mediaPlayer.getMediaPlayer().stop();
					_mediaPlayer.getMediaPlayer().playMedia(_dirPath);
					editSubtitle.removeAll();
					setUpSubtitleMenu(subtitle, disableSub, addSubtitle);
					_frame.setTitle("VAMIX - " + _dirPath.substring(_dirPath.lastIndexOf("/") + 1));
				}
			}
			
		});
		
		addSubtitle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AddSubtitle sub = new AddSubtitle();
				sub.setVisible(true);
			}
			
		});
		
		_mediaPlayer.getMediaPlayer().setRepeat(true);
		
	}

	//this class is responsible for updating the time next to the seek bar as the media progresses
	private class UpdateLabel extends MediaPlayerEventAdapter{
		
		public void positionChanged(MediaPlayer _mediaPlayer, float pos) {
			long currentMillis = (long)(_mediaPlayer.getLength()*pos);
			long totalMillis = _mediaPlayer.getLength();

			//change the time in the desired format
			String currentTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currentMillis),
					TimeUnit.MILLISECONDS.toMinutes(currentMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(currentMillis)), 
					TimeUnit.MILLISECONDS.toSeconds(currentMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentMillis)));
			String totalTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMillis),
					TimeUnit.MILLISECONDS.toMinutes(totalMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMillis)), 
					TimeUnit.MILLISECONDS.toSeconds(totalMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMillis)));

			_vidTime.setText(currentTime + "/" + totalTime);
		}
	}

	//when the volume bar is changed, adjust to the new set volume
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			int vol = (int)source.getValue();
			_mediaPlayer.getMediaPlayer().setVolume(vol);
		}
	}

	//general rules for clicking any other buttons
	@Override
	public void actionPerformed(ActionEvent e) {

		//if rewind button is clicked, change the text to play, then execute the rewind worker
		if(e.getSource() == _rewind){
			if (!_pause.getToolTipText().equals("▷")) {
				rewind();
			}
		}
		
		//if pause button is pressed, change the button to a play button
		else if(e.getSource() == _pause) {
			if(_pause.getToolTipText().equals("||")){
				_mediaPlayer.getMediaPlayer().pause();
				_pause.setToolTipText("▷");
				_pause.setIcon(new ImageIcon("./src/Icon/play.png"));
			//if the button pressed was a play button, cancel rewind
			} else {
				if(rw!=null){
					rw.cancel(true);
				}
				
				//change the rate of the media back to 1 (normal rate)
				if(_mediaPlayer.getMediaPlayer().getRate() != 1){
					_mediaPlayer.getMediaPlayer().setRate(1);
					_mediaPlayer.getMediaPlayer().play();
				//if the media was not playing, start the media
				}else if(!_mediaPlayer.getMediaPlayer().isPlaying()){
					_mediaPlayer.getMediaPlayer().start();
					
				//if the media was stopped, resume the play
				}else{
					_mediaPlayer.getMediaPlayer().play();
				}
				_pause.setToolTipText("||");
				_pause.setIcon(new ImageIcon("./src/Icon/pause.png"));
			}
		} 
		
		else if(e.getSource().toString().contains("Subtitle") || e.getSource().toString().contains("Disable")) {
			
			if (e.getSource().toString().contains("Subtitle")) {
				int spuVal = Integer.parseInt(e.getActionCommand().split(" ")[1]);
				_mediaPlayer.getMediaPlayer().setSpu(spuVal + 1);
			}else if (e.getSource().toString().contains("Disable")){
				_mediaPlayer.getMediaPlayer().setSpu(-1);
			}
			if(temp != null){
				temp.setSelected(false);
			}
			temp = (JRadioButtonMenuItem) e.getSource();
		}

	}

	public void updateSeekBar(int time) {
		_seekBar.setValue(time);

	}

	//if the media reached its end, place the seek bar back to the start
	public void returntoStart(){
		_seekBar.setValue(0);
		_mediaPlayer.getMediaPlayer().setTime(0);
		_pause.setIcon(new ImageIcon("./src/Icon/pause.png"));
	}

	public void changeMediaPlayerTime(long t) {
		_mediaPlayer.getMediaPlayer().setTime(t);

	}

	//if the user clicks a point on the seek bar, go to that frame
	@Override
	public void mouseClicked(MouseEvent e) {
		_seekBar.setUI(new MetalSliderUI() {
			protected void scrollDueToClickInTrack(int direction) {

				_value = _seekBar.getValue(); 

				if (_seekBar.getOrientation() == JSlider.HORIZONTAL) {
					_value = this.valueForXPosition(_seekBar.getMousePosition().x);

				}
				_seekBar.setValue(_value);
				_mediaPlayer.getMediaPlayer().setTime(_value*(_mediaPlayer.getMediaPlayer().getLength())/1000);
			}
		});

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	// a method for fast-forwarding
	public void fastForward() {
		if (_pause.getToolTipText().equals(("||"))) {
			_pause.setToolTipText("▷");
			_pause.setIcon(new ImageIcon("./src/Icon/play.png"));
			
			}
			
			if(_mediaPlayer.getMediaPlayer().getRate() == 1){
				_mediaPlayer.getMediaPlayer().setRate(_speed);
			}else if(_mediaPlayer.getMediaPlayer().getRate() == _speed){
				_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
			}else{
				_mediaPlayer.getMediaPlayer().setRate(_speed + 1.5f);
			}
	}

	// a method for rewinding
	public void rewind() {
		_pause.setToolTipText("▷");
		_pause.setIcon(new ImageIcon("./src/Icon/play.png"));;
		rw = new RewindWorker(_mediaPlayer);
		rw.execute();
	}
	
	public void setSubtitleTrue(File subtitle) {
		_mediaPlayer.getMediaPlayer().setSubTitleFile(subtitle);
	}
	
	// method that sets up the menu bar for the subtitle actions
	public void setUpSubtitleMenu(JMenu subtitle, JMenuItem disableSub, JMenuItem addSubtitle) {
		if (_mediaPlayer.getMediaPlayer().getSpuCount() != 0) {
			
			subtitle.add(disableSub);
			disableSub.addActionListener(this);
			subtitle.setEnabled(true);
			disableSub.setEnabled(true);
			temp = (JRadioButtonMenuItem)disableSub;
			disableSub.setSelected(true);
			_mediaPlayer.getMediaPlayer().setSpu(-1);
			
			for(int i = 1; i<_mediaPlayer.getMediaPlayer().getSpuCount(); i++){
				JRadioButtonMenuItem j = new JRadioButtonMenuItem( "Subtitle " + (i));
				subtitle.add(j);
				j.setActionCommand("Subtitle "+(i)); 
				j.addActionListener(this);
			}
			
		} else if (_mediaPlayer.getMediaPlayer().getSpuCount() == 0) {
			subtitle.setEnabled(false);
			disableSub.setEnabled(false);
		}
		editSubtitle.add(subtitle);
		editSubtitle.add(addSubtitle);
		
	}

}