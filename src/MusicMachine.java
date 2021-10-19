import java.awt.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.util.*;
import java.awt.event.*;

public class MusicMachine {		// MusicMachine is a simple beatbox midi sequencer

	JPanel mainPanel;
	ArrayList<JCheckBox> checkBoxList;		// store the checkboxes (possible choices of sounds) in ArrayList
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	JFrame mainFrame;
	
	// String names of the used (drum) instruments and int of their "key" represents 
	String[] instrumentsNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap", "High Tom", 	"Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", 	"Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
	
	int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
		
	public static void main(String[] args) {

		MusicMachine machine = new MusicMachine();
		machine.createGUI();

	}
	
	public void createGUI() {		// configure frame and panel
		
		mainFrame = new JFrame("MusicMachine");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel backgroundPanel = new JPanel(layout);
		backgroundPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		// create checkboxes, buttons and field for instruments' names
		checkBoxList = new ArrayList<JCheckBox>();		
		Box buttonField = new Box(BoxLayout.Y_AXIS);
		
		JButton start = new JButton("Start");
		start.addActionListener((ActionListener) new MyStartListener());
		buttonField.add(start);
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		buttonField.add(stop);
		
		JButton tempoG = new JButton("Faster");
		tempoG.addActionListener(new MyTempoGListener());
		buttonField.add(tempoG);
		
		JButton tempoD = new JButton("Slower");
		tempoD.addActionListener(new MyTempoDListener());
		buttonField.add(tempoD);
		
		Box nameField = new Box(BoxLayout.Y_AXIS);			// put instruments' names vertically
		for (int i = 0; i < 16; i++) {
			nameField.add(new Label(instrumentsNames[i]));
		}
		
		// add components to Panel according to BorderLayout, and then to Frame
		backgroundPanel.add(BorderLayout.EAST, buttonField);
		backgroundPanel.add(BorderLayout.WEST, nameField);
		
		mainFrame.getContentPane().add(backgroundPanel);
		
		// set GridLayout for checkboxes and add to Panel (BorderLayout) 
		GridLayout checkBoxGrid = new GridLayout(16,16);	// so that each of 16 instruments has 16 boxes - possibility to choose at chosen time
		
		checkBoxGrid.setVgap(1);
		checkBoxGrid.setHgap(2);
		mainPanel = new JPanel(checkBoxGrid);
		backgroundPanel.add(BorderLayout.CENTER, mainPanel);
		
		// 256 boxes altogether, set false (they are unchecked), add to ArrayList and Panel
		for (int i = 0; i < 256; i++) {		
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkBoxList.add(c);
			mainPanel.add(c);	
		}
		
		configureMidi();	
		
		// set Frame
		mainFrame.setBounds(50,50,300,300);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		}
	
	public void configureMidi() {
		
		try {
			
			// make a sequencer, a sequence and a track	
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(220);
			
		} catch (Exception e) {e.printStackTrace(); }
		
	}
	
	public void createTrackAndPlay() {		// create a 16-element array to hold the values for one instrument across 16 beats
		
		int[] tracklist = null;
		
		// create a new track
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		
		for (int i = 0; i < 16; i++) {		// this is for each of the 16 ROWS 
			tracklist = new int[16];
			
			int key = instruments[i];		// set the 'key' representing the instrument
			
			for (int j = 0; j < 16; j++) {		// this is for each BEAT of the row 
				JCheckBox jc = (JCheckBox) checkBoxList.get(j + (16*i));
				if (jc.isSelected()) {		// put key value to the selected checkbox (jc), otherwise don't play it (value zero) 
					tracklist[j] = key;
				} else {
				  tracklist [j] = 0;
				}
			} // close inner loop
		
		// make events and add to the track	
		createTrack(tracklist);
		track.add(createEvent(176,1,127,0,16));	// 176: event type is Controller Event
		
		} // close outer loop
		
		track.add(createEvent(192,9,1,0,15)); 
		// 192: change instrument (so that there is always an event at beat 16 and beatbox go to the full before it starts over)
		
		try {
			// set the sequence
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
			sequencer.setTempoInBPM(120);
			sequencer.start();
			
		} catch (Exception e) {e.printStackTrace(); }
		
	} // close createTrackAndPlay()
	
	
	// implement ActionListeners for the buttons
	
	public class MyStartListener implements ActionListener {	// "Start"
		
		public void actionPerformed(ActionEvent a) {
			createTrackAndPlay();
		}
	}
			
	public class MyStopListener implements ActionListener {		// "Stop"
		
		public void actionPerformed(ActionEvent a) {
			sequencer.stop();
		}
	}
	
	public class MyTempoGListener implements ActionListener {		// "Faster"
		
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) ((float)tempoFactor * 1.03));
		}
	}
	
	public class MyTempoDListener implements ActionListener {		// "Slower"
		
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) ((float)tempoFactor * .97));
		}
	}
	
	public void createTrack(int[] list) {		// make events for one instrument at a time for all 16 beats
		
		for (int i = 0; i < 16; i++) {
			int key = list[i];		// index in the Array holds either the key of the instrument or 0 (it's not played)
			if (key != 0) {			
				track.add(createEvent(144,9,key,100,i));
				track.add(createEvent(128,9,key,100,i+1));
			}
		}
	} // close inner class
	
	public static MidiEvent createEvent(int comd, int channel, int one, int two, int tick) {
		// create the message
		// (command; channel; one - note to play; two - velocity; tick - when the message should happen)
		
		MidiEvent event = null;
		
		try {
			
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, channel, one, two);
			event = new MidiEvent(a, tick);
			
		} catch (Exception e) {e.printStackTrace();}
		
		return event;
		
	}	// close method
	
} // close class
