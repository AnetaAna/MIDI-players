
import javax.sound.midi.*;

public class MiniPlayer1 { 			// MiniPlayer version 1: it makes a series of midi messages/events to play notes on piano 

	public static void main(String[] args) {
		
		try {
			
			 // make a sequencer, a sequence and a track
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			// create a series of events to make the notes keep going up (from piano note 5 to 60)
			for (int i = 5; i < 61; i+=4) {
				track.add(createEvent(144,1,i,100,i));
				track.add(createEvent(128,1,i,100,i+2));
			}
			
			// set and start running the sequence
			sequencer.setSequence(seq);
			sequencer.setTempoInBPM(220);
			sequencer.start();
			
		} catch (Exception ex) { ex.printStackTrace(); }
	
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
