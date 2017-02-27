package MCO152;

import java.time.LocalTime;

/**
 * Created by joshuagoldmeier on 8/31/16.
 */

interface IClock
{
    Time now();
}

class Clock implements IClock{
    public Time now() {
        LocalTime now = LocalTime.now();
        return Time.newTimeHourMinuteSecond(now.getHour(), now.getMinute(), now.getSecond());
    }
}

public class AlarmClock {
    private Time alarmTime;
    private IClock clock;
    private boolean alarmIsRinging = false;
    private boolean alarmHasBeenSet = false;
    int counter = 0;

//
//    public static void main(String[] args) {
//
//    }

    public AlarmClock(IClock clock){
        this.clock = clock;
    }

    void setAlarmTime(Time t) {
        alarmTime = Time.timeCopyConstructor(t); // HW: make deep copy
    }

    Time getAlarmTime() {
        return Time.timeCopyConstructor(alarmTime); // HW: deep copy
    }

    void checkTheAlarm() {
        int secondsUntil = alarmTime.secondsUntil(clock.now());

        if (secondsUntil == 0){
            setOffAlarm();

        }
    }

    void setOffAlarm(){
        alarmIsRinging = true;
    }

    boolean alarmIsRinging() {
        return alarmIsRinging;
    }

}