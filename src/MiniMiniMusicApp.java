import javax.sound.midi.*;


public class MiniMiniMusicApp {   
       
     public static void main(String[] args) {
    	 
        MiniMiniMusicApp mini = new MiniMiniMusicApp();
        mini.play();
     }

    public void play() { 	 // it plays a single note for a short time

      try {

         // make a sequencer, a sequence and a track
         Sequencer sequencer = MidiSystem.getSequencer();         
         sequencer.open();
        
         Sequence seq = new Sequence(Sequence.PPQ, 4);
         Track track = seq.createTrack();     
        
         // make a message and stick it into a midi event 
          ShortMessage a = new ShortMessage();
          a.setMessage(144, 1, 44, 100);
          MidiEvent noteOn = new MidiEvent(a, 1); // <-- means at tick one, the above event happens
          track.add(noteOn);

          ShortMessage b = new ShortMessage();
          b.setMessage(128, 1, 44, 100);
          MidiEvent noteOff = new MidiEvent(b, 16); 
          track.add(noteOff);
          
          //set and execute sequence for chosen length 
          sequencer.setSequence(seq);
         
          sequencer.start();
         
          Thread.sleep(1000);
          sequencer.close();
          System.exit(0);
      } catch (Exception ex) {ex.printStackTrace();}
  } // close play

} // close class
