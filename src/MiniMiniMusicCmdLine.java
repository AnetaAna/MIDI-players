
import javax.sound.midi.*;
import java.util.*;


public class MiniMiniMusicCmdLine {   
       
     public static void main(String[] args) {
    	 
        MiniMiniMusicCmdLine mini = new MiniMiniMusicCmdLine();
        
        // read user's arguments using Scanner
        Scanner in = new Scanner(System.in);
        System.out.println("Enter two values from 0 to 127 (the instrument and note args)");
        mini.play(in.nextInt(), in.nextInt());
          
        }
     
    public void play(int instrument, int note) {    // it plays a single note according to the int values chosen by the user
     
    	try {
    	
    	// make a sequencer, a sequence and a track	
         Sequencer player = MidiSystem.getSequencer();         
         player.open();
        
         Sequence seq = new Sequence(Sequence.PPQ, 4);         
         Track track = seq.createTrack();  
          
         
         // make messages and stick them into midi events 
         ShortMessage first = new ShortMessage();
         first.setMessage(192, 1, instrument, 0);	// 192 -> change-instrument message
         MidiEvent changeInstrument = new MidiEvent(first, 1); 
         track.add(changeInstrument);

         
         ShortMessage a = new ShortMessage();
         a.setMessage(144, 1, note, 100);	
         MidiEvent noteOn = new MidiEvent(a, 1); 
         track.add(noteOn);

         ShortMessage b = new ShortMessage();
         b.setMessage(128, 1, note, 100);
         MidiEvent noteOff = new MidiEvent(b, 16); 
         track.add(noteOff);
         
       //set and execute sequence for chosen length 
         player.setSequence(seq); 
         player.start();
         
         Thread.sleep(2000);
         player.close();
         System.exit(0);

      } catch (Exception ex) {ex.printStackTrace();}
  } // close play

} // close class
