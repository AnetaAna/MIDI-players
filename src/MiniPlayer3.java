import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class MiniPlayer3 {
	
	// MiniPlayer version 3: it makes a series of midi messages/events to play notes on piano and draws a new rectangle with each new event (GUI) 
	
	static JFrame frame = new JFrame("My first music video");
	static MyGraphicPanel panel;

	public static void main(String[] args) {
		
		
		MiniPlayer3 mini = new MiniPlayer3();
		mini.work();
		
	}
	
	public void configureGUI() {		// configure frame and panel
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new MyGraphicPanel();
		frame.setContentPane(panel);
		frame.setBounds(30,30,300,300);
		frame.setVisible(true);
		
	}
	
public void work() {
		
		configureGUI();
		
		try {
			
			// make a sequencer, a sequence and a track	
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			sequencer.addControllerEventListener(panel, new int[] {127});	// register the Listener with the event (127 - NOTE ON)
			
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			// create a series of events with random choice of piano notes (from note 5 to 60)
			int r = 0;
			for (int i = 5; i < 61; i+=4) {
				
				r = (int) ((Math.random() *50) +1);
				track.add(createEvent(144,1,r,100,i));
				track.add(createEvent(176,1,127,0,i));		// 176: event type is Controller Event
				track.add(createEvent(128,1,r,100,i+2));
			}
			
			// set and start running the sequence
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(220);
			sequencer.start();
			
		} catch (Exception ex) {ex.printStackTrace(); }
		
	}

public static MidiEvent createEvent(int comd, int channel, int one, int two, int tick) {
	// create the message
	// (command; channel; one - note to play; two - velocity; tick - when the message should happen)
	
	MidiEvent event = null;
	
	try {
		
		ShortMessage a = new ShortMessage();
		a.setMessage(comd, channel, one, two);
		event = new MidiEvent(a, tick);
		
	} catch (Exception e) {}
	
	return event;
	
}

class MyGraphicPanel extends JPanel implements ControllerEventListener {		// Panel as a listener
	
	boolean mes = false;		// flag set to false; true only when we get an event
	
	public void controlChange(ShortMessage event) {
		
		mes = true;	// we get an event: flag changes
		repaint();
			
}

public void paintComponent(Graphics g) {		// generate a random colour and half-random dimensions and paint a rectangle
	
	if (mes) {		// flag in order to repaint only when there's a ControllerEvent
		
		Graphics2D g2 = (Graphics2D) g;
		
		int c = (int) (Math.random() * 250);
		int z = (int) (Math.random() * 250);
		int n = (int) (Math.random() * 250);
		
		g.setColor(new Color(c,z,n));
		
		int height = (int) ((Math.random() * 120) + 10);
		int width = (int) ((Math.random() * 120) + 10);
		
		int x = (int) ((Math.random() * 40) + 10);
		int y = (int) ((Math.random() * 40) + 10);
		
		g.fillRect(x, y, width, height);
		mes = false;
		
			} // close if
		} // close method
	} // close inner class
} //close class
