
import javax.sound.midi.*;

public class MiniPlayer2 implements ControllerEventListener { 
	
	// MiniPlayer version 2: it makes a series of midi messages/events to play notes on piano and writes "la's" in command line

	public static void main(String[] args) {
		
		MiniPlayer2 mini = new MiniPlayer2();
		mini.work();
		
	}
	
	public void work() {
		
		try {
			
			// make a sequencer, a sequence and a track
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			int[] operatedEvents = {127};		// choose the event (127 - NOTE ON)
			sequencer.addControllerEventListener(this, operatedEvents);		// register the Listener in sequencer with the chosen event
			
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			// create a series of events to make the notes keep going up (from piano note 5 to 60)
			for (int i = 5; i < 61; i+=4) {
				track.add(createEvent(144,1,i,100,i));
				
				track.add(createEvent(176,1,127,0,i));  // 176: event type is Controller Event
				
				track.add(createEvent(128,1,i,100,i+2));
			}
			
			// set and start running the sequence
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(220);
			sequencer.start();
			
		} catch (Exception ex) {ex.printStackTrace(); }
		
	}
	
	public void controlChange(ShortMessage event) {		// event handler method: with every event we get "la" printed in cmd
		
		System.out.println("la");
		
	}
	
public static MidiEvent createEvent(int comd, int channel, int one, int two, int tick) {
	// create the message
	// (command; channel; one - note to play; two - velocity; tick - when the message should happen)
	
		MidiEvent event = null;
		
		try {
			
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, channel, one, two);
			event = new MidiEvent(a, tick);
			
		} catch (Exception ex) {}
		
		return event;
		
	}
}
