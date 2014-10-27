package GUI;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Function.JTextFieldLimit;
import Worker.TextWorker;

/**
 * This TextAdderFrame is a frame that opens when each field is requied for
 * adding text to be processed.
 * 
 * @author Andrew Jeong
 *
 */

public class TextAdderFrame extends JFrame implements ActionListener {

	private JLabel _reqTextS = new JLabel("Enter Text to add into start of Video: ");
	private JLabel _reqTimeStart = new JLabel("How long do you wish the text to appear for?");
	private JLabel _reqTextEnd = new JLabel("Enter Text to add into end of Video: ");
	private JLabel _reqTimeEnd = new JLabel("How long do you wish the text to appear for?");
	private JLabel _reqNewName = new JLabel("Save edited video as: (with .mp4 extension)");
	
	private JPanel _tS = new JPanel();
	private JPanel _tF = new JPanel();
	private JProgressBar _progressBar= new JProgressBar();
	
	private JTextArea _enterTextS = new JTextArea(10, 40);
	private JTextField _enterTimeStart = new JTextField("in seconds:", 10);
	private JTextArea _enterTextEnd = new JTextArea(10,40);
	private JTextField _enterTimeEnd = new JTextField("in seconds:", 10);
	private JTextField _enterNewName = new JTextField(20);
	
	private JButton _cancel = new JButton("Cancel");
	private JButton _add = new JButton("Add Text");
	private FlowLayout _layout = new FlowLayout();
	
	private String _textS;
	private String _timeS;
	private String _textE;
	private String _timeE;
	private String _newName;
	private String _vidName;
	private long _time;
	private String _font;
	private String _fontColor;
	private String _fontSize;
	
	private JComboBox<String> cb;
	private JComboBox<String> cb2;
	private JComboBox<String> cb3;
	
	private int _error;
	
	//constructor of the class
	TextAdderFrame(String vidName, long time){
		
		//set up of GUI
		_time = time;
		_vidName = vidName;
		
		_enterTextS.setLineWrap(true);
		_enterTextS.setWrapStyleWord(true);
		_enterTextEnd.setLineWrap(true);
		_enterTextEnd.setWrapStyleWord(true);
		
		_enterTextS.setDocument(new JTextFieldLimit(250));
		_enterTextEnd.setDocument(new JTextFieldLimit(250));
		
		String[] fontsString = new String[] {"Font", "Normal", "Italics", "Bold", "Bold + Italics"};
		JComboBox<String> fonts = new JComboBox<>(fontsString);
		String[] fontSize = new String[] {"Size", "14", "16", "18", "20", "22", "24", "26", "28", "30"};
		JComboBox<String> sizes = new JComboBox<>(fontSize);
		String[] fontColor = new String[] {"Color", "Black", "White", "Yello", "Blue", "Red"};
		JComboBox<String> fontColors = new JComboBox<>(fontColor);
		
		JScrollPane spS = new JScrollPane(_enterTextS);
		JScrollPane spE = new JScrollPane(_enterTextEnd);
		
		setTitle("Add Text to Video");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(550,600);
		setLayout(_layout);
		
		add(_reqTextS);
		add(spS);
		
		_tS.add(_reqTimeStart);
		_tS.add(_enterTimeStart);
		
		add(_tS);
		
		add(_reqTextEnd);
		add(spE);
		
		_tF.add(_reqTimeEnd);
		_tF.add(_enterTimeEnd);
		
		add(_tF);
		
		add(_reqNewName);
		add(_enterNewName);
		
		add(new JLabel("font style:"));
		add(fonts);
		add(new JLabel("font size:"));
		add(sizes);
		add(new JLabel("font Color:"));
		add(fontColors);
		add(_progressBar);
		_progressBar.setStringPainted(true);
		Dimension prefSize = _progressBar.getPreferredSize();
		prefSize.width = 500;
		_progressBar.setPreferredSize(prefSize);
		add(_add);
		add(_cancel);

		//if cancel button is pressed, dispose the frame
		_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		
		//if fonts combo box is clicked
		fonts.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
			        cb = (JComboBox<String>)e.getSource();
			       
			    }
		});
		
		//when size combo box is clicked
		sizes.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
			        cb2 = (JComboBox<String>)e.getSource();
			        
			        
			    }
		});
		
		//when colors combo box is clicked
		fontColors.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
			        cb3 = (JComboBox<String>)e.getSource();
			        
			        
			    }
		});
		
		//if the Add button is clicked
		_add.addActionListener(this);

		
		_enterTimeStart.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
					_enterTimeStart.setText("");
				}
		});
	
		
		_enterTimeEnd.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
					_enterTimeEnd.setText("");
				}
		});
	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
			
		_textS = _enterTextS.getText();
		_timeS = _enterTimeStart.getText();
		_textE = _enterTextEnd.getText();
		_timeE = _enterTimeEnd.getText();
		_newName = _enterNewName.getText();
		 
		//if nothing is selected from the font combo box, display an error message
		 if(cb == null){
				JOptionPane.showMessageDialog(null, "Please select a font type");
	        	_error = 0;
			}else{
				String fontName = (String)cb.getSelectedItem();
				_font = fontName;
				_error = 1;
			}
		
		//if nothing is selected from the size combo box, display an error message
		if(cb2 == null){
			JOptionPane.showMessageDialog(null, "Please select a font size");
        	_error = 0;
		}else{
			String fontSize = (String)cb2.getSelectedItem();
        _fontSize = fontSize;
        _error = 1;
        
		}
		
		//if nothing is selected from the color combo box, display an error message
		if(cb3 == null){
			JOptionPane.showMessageDialog(null, "Please select a font color");
        	_error = 0;
		}else{
			String color = (String)cb3.getSelectedItem();
        _fontColor = color;
        _error = 1;
        
		}
		
		int time = (int) (_time - Integer.parseInt(_timeE));
		_timeE = Integer.toString(time);
	
		//if field entry is not done properly, display an error message
		if (_newName.equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter the name to save the file as");
		} else if (_newName.contains(".") && !_newName.contains(".mp4")) {
			JOptionPane.showMessageDialog(null, "Sorry, Invalid file type");
		} else if (_timeS == null || _timeE == null) {
			JOptionPane.showMessageDialog(null, "Please enter the fields!");
		} else if (!_newName.contains(".mp4")) {
			JOptionPane.showMessageDialog(null, "Please include the file extension");
		}
		//if no particular error is found, start the SwingWorker
		else{
			if(_error != 0){
			TextWorker textWorker = new TextWorker(_vidName, _newName, _timeS, _timeE, _textS, _textE, _font, _fontSize, _fontColor, _progressBar);	
			textWorker.execute();
			}

		}
	}
}