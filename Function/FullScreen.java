package Function;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * This class allows the user to open a full screen window of media player.
 * 
 * @author andrew
 *
 */

public class FullScreen {

	private long _time;
	protected JPanel _panel;
	private EmbeddedMediaPlayer _mp;

	public FullScreen(String mrl, EmbeddedMediaPlayer _mediaPlayer) {
		
		final JFrame frame = new JFrame();
		Canvas canv = new Canvas();
		_time = _mediaPlayer.getTime();
		
		canv.setBackground(Color.BLACK);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(canv, BorderLayout.CENTER);

		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
		
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		final EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new DefaultFullScreenStrategy(frame));
		mediaPlayer.setVideoSurface(mediaPlayerFactory.newVideoSurface(canv));

		mediaPlayer.setFullScreen(true);
		mediaPlayer.playMedia(mrl);

		if (_time > 0) {
			mediaPlayer.setTime(_time);
		}
		
		_mediaPlayer.pause();
		_mp = _mediaPlayer;
		
		mediaPlayer.setRepeat(true);
		frame.setBounds(0, 0, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		frame.setLocation(0, 0);
		frame.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_ESCAPE) {
					mediaPlayer.pause();
					_mp.play();
					_mp.setTime(mediaPlayer.getTime());
					frame.dispose();
				} else if(code == KeyEvent.VK_SPACE) {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					} else {
						mediaPlayer.play();
					}
				}
			}
		});
	}
}
