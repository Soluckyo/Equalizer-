import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class equalizer{
    static JFrame f = new JFrame("Equalizer");
    static drawPanel ml;

    public static void main(String[] args) {
        equalizer eq = new equalizer();
        eq.go();
    }

    public void setUpGUI() {
        ml = new drawPanel();
        f.setContentPane(ml);
        f.setBounds(30, 30, 300, 300);
        f.setVisible(true);
    }

    public void go() {
        setUpGUI();
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            sequencer.addControllerEventListener(ml, new int[]{127});

            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();
            int r = 0;
            for (int i = 0; i < 60; i += 4) {
                r = (int) ((Math.random() * 50) + 1);
                track.add(makeEvent(144, 1, r, 100, i));
                track.add(makeEvent(176, 1, 127, 0, i));
                track.add(makeEvent(128, 1, r, 100, i + 2));
            }
            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(220);
            sequencer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) {
        }
        return event;
    }

    class drawPanel extends JPanel implements ControllerEventListener {
        boolean msg = false;
        public void controlChange(ShortMessage event) {
            msg = true;
            repaint();
        }
        public void paintComponent(Graphics g){
            if (msg){
                Graphics2D g2 = (Graphics2D) g;
                int red = (int) (Math.random()*250);
                int green = (int) (Math.random()*250);
                int blue = (int) (Math.random()*250);

                g.setColor(new Color(red, green, blue));

                int height = (int) ((Math.random()*120)+10);
                int width = (int)((Math.random()*120)+10);

                int x = (int)((Math.random()*40)+10);
                int y = (int)((Math.random()*40)+10);

                g.fillRect(x,y,height,width);
                msg = false;
            }
        }
    }
}