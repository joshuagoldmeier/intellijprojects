package MCO152;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by joshuagoldmeier on 10/6/16.
 */

abstract class ClockView extends JPanel{

    protected IClock clock;
    public abstract void display();

    ClockView(IClock clock)
    {
        this.clock = clock;
    }
}

class DigitalClockView extends ClockView {

    public DigitalClockView(IClock clock) {
        super(clock);
    }

    // requires IClock
    @Override
    public void display(){// stub
        JLabel theTime = new JLabel(this.clock.now().toStringStandard());
        theTime.setForeground(Color.green);
        System.out.println(theTime.getText());

        getGraphics().drawString(this.clock.now().toStringStandard(),0,20);
    }
}

class AnalogClockView extends ClockView {

    public AnalogClockView(IClock clock) {
        super(clock);
        setSize(200, 400);
    }

    @Override
    public void display() {
        int diameter = 350;
        int center = diameter/2;
        Time time = this.clock.now();

        createClock(diameter, time);
        placeHourHand(time.getHOUR(), center);
        placeMinuteHand(time.getMINUTE(), center);
    }


    private void createClock(int diameter, Time time) {
        Graphics2D clock = (Graphics2D) getGraphics();
        clock.drawString("Movado", 150, 100);
        clock.drawOval(0, 0, diameter, diameter);
        clock.drawString(time.toStringStandard(), 140, 275);
    }

    private void placeHourHand(int hour, int center) {
        Graphics2D hourHand = (Graphics2D) getGraphics();
        int degreesToRotateHourHand = (int) (hour*30 + hour*.5);
        hourHand.rotate(Math.toRadians(degreesToRotateHourHand), center, center);
        hourHand.drawLine(center, center, center, 50);
    }

    private void placeMinuteHand(int minute, int center) {
        Graphics2D minuteHand = (Graphics2D) getGraphics();
        int degreesToRotateMinuteHand = minute*6;
        minuteHand.rotate(Math.toRadians(degreesToRotateMinuteHand), center, center);
        minuteHand.drawLine(center, center, center, 25);
    }

}