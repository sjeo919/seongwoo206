package Worker;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * This RewindWorker class is a worker class that helps our rewind function.
 * 
 * @author Andrew Jeong
 *
 */

public class RewindWorker extends SwingWorker<Void,Void>{
	EmbeddedMediaPlayerComponent _mediaP;
	public RewindWorker(EmbeddedMediaPlayerComponent mp){
		_mediaP = mp;
	}

	@Override
	protected Void doInBackground() throws Exception {
		//while rewinding goes on, skip back 3 frames at a time
		while(!isCancelled()){
			_mediaP.getMediaPlayer().skip(-3);
		}
		return null;
	}

}