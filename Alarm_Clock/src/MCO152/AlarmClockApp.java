package MCO152;

import java.awt.*;
import javax.swing.JFrame;

/**
 * Created by joshuagoldmeier on 10/6/16.
 */


public class AlarmClockApp extends JFrame{
    // dependent DigitalClockView
}

class ClockApp extends JFrame{
    IClock clock;
    ClockView  view;

    ClockApp(){
        clock = new Clock();
        view = new DigitalClockView(clock);

        this.getContentPane().add(view);
        this.setSize(400, 400);
        //this.pack(); resize frame to fit panel size
        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setForeground(Color.green);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g){
        view.display();
    }


}
