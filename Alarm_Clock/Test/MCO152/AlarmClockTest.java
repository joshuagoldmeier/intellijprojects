package MCO152;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.joda.time.DateTime;
import org.junit.Test;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by joshuagoldmeier on 8/31/16.
 */
public class AlarmClockTest {


    @Test
    public void setAlarmTime() {
        // AlarmClock.x = 7;
        // AAA

        // Arrange
        AlarmClock ac = new AlarmClock(new Clock());

        // Act
        ac.setAlarmTime( Time.newTimeHourMinuteSecond(6, 30, 0) );
        Time t = ac.getAlarmTime();
        HashMap timeInMap = t.getTime();

        // Assert - reminder BDD fluent assertions
        assertThat(timeInMap, instanceOf(HashMap.class));
        assertEquals(6, timeInMap.get("HOUR"));
        assertEquals(30, timeInMap.get("MINUTE"));
        assertEquals(0, timeInMap.get("SECOND"));
    }

    @Test
    public void secondsUntil() {
        Time time1 = Time.newTimeHourMinuteSecond(6, 30, 30);
        Time time2 = Time.newTimeHourMinuteSecond(6, 30, 30);

        int difference = time1.secondsUntil(time2);

        assertEquals(0, difference);

        time2 = Time.newTimeHourMinute(6, 30);
        difference = time1.secondsUntil(time2);
        assertEquals(-30, difference);
        difference = time2.secondsUntil(time1);
        assertEquals(30, difference);
    }


    @Test(expected=IllegalArgumentException.class)
    public void invalidTimeException() {
        Time t = Time.newTimeHourMinuteSecond(200, 0, 0);

    }

    @Test
    public void addHours() {
        Time t = Time.newTimeHourMinuteSecond(0, 0, 0);

        t = t.addHours(5);
        assertEquals("5:00:00", t.toString());
        t=t.addHours(-7);
        assertEquals("22:00:00", t.toString());
        t=t.addHours(2);
        assertEquals("0:00:00", t.toString());
        t=t.addHours(-1);
        assertEquals("23:00:00", t.toString());
    }


    @Test
    public void addMinutes() {
        Time t = Time.newTimeHourMinuteSecond(110, 110, 0);

        t = t.addMinutes(90);
        assertEquals("1:30:00", t.toString());
        t=t.addMinutes(-120);
        assertEquals("23:30:00", t.toString());
        t=t.addMinutes(-30);
        assertEquals("23:00:00", t.toString());
        t=t.addMinutes(1440);
        assertEquals("23:00:00", t.toString());

    }


    @Test
    public void addSeconds() {

        Time t = Time.newTimeHourMinuteSecond(0, 0, 0);

        t=t.addSeconds(90);
        assertEquals("0:01:30", t.toString());
        t=t.addSeconds(-90);
        assertEquals("0:00:00", t.toString());
        t=t.addSeconds(1);
        assertEquals("0:00:01", t.toString());
        t=t.addSeconds(-2);
        assertEquals("23:59:59", t.toString());
        t=t.addSeconds(86400);
        assertEquals("23:59:59", t.toString());
    }

    @Test
    public void testTheEquals() {
        Time t1 = Time.newTimeHourMinuteSecond(6, 0, 0);
        Time t2 = Time.newTimeHourMinuteSecond(6, 0, 0);
        Time t3 = Time.newTimeHourMinuteSecond(6, 0, 1);

        assertTrue( t1.equals(t1));
        assertTrue( t1.equals(t2));
        assertTrue( ! t1.equals(t3));
    }


    @Test
    public void toStringMethods() {
        Time time = Time.newTimeHourMinuteSecond(16, 5, 18);

        String armyTime = time.toString();
        String standardtime = time.toStringStandard();

        assertEquals("16:05:18", armyTime);
        assertEquals("4:05:18 PM", standardtime);
    }

    @Test
    public void alarmIsRinging() {
        AlarmClock ac = new AlarmClock(new MockClock(Time.newTimeHourMinuteSecond(6, 30, 0)));
        ac.setAlarmTime(Time.newTimeHourMinuteSecond(6, 30, 0));

        ac.checkTheAlarm();

        assertTrue(ac.alarmIsRinging());
    }


    @Test
    public void timeServer() throws Exception {
        TimeServerClock ntpTimeClock = new TimeServerClock();
        Time ntpTime = ntpTimeClock.now();
        LocalTime now = LocalTime.now();
        Time systemTime = Time.newTimeHourMinuteSecond(now.getHour(), now.getMinute(), now.getSecond());
        System.out.println(ntpTime.toString());

        assertTrue(Math.abs(ntpTime.secondsUntil(systemTime)) < 2);
        assertTrue( ! (Math.abs(ntpTime.secondsUntil(systemTime)) > 2));

        Thread.sleep(3000);

        now = LocalTime.now();
        systemTime = Time.newTimeHourMinuteSecond(now.getHour(), now.getMinute(), now.getSecond());

        assertTrue(Math.abs(ntpTime.secondsUntil(systemTime)) > 2);
    }


    @Test(expected=RuntimeException.class)
    public void invalidNetworkError() {

        // TODO: BREAK THIS TEST INTO SMALLER TEST THAT ARE MORE SPECIFIC
        FileOutputStream os = null;
        String fileName = FileSystemView.getFileSystemView().getHomeDirectory().toString() + "/robinsonTestCase.txt";
        File storedTimeServers = new File(fileName);
        try {
            os = new FileOutputStream(storedTimeServers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Properties timeServers = new Properties();
        timeServers.setProperty("Should Fail", "FAILLL");
        try {
            timeServers.store(os, "Not sure why I need this");
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            timeServers.load(new FileInputStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> setOfKeys = timeServers.stringPropertyNames();
        Iterator<String> keyIterator = setOfKeys.iterator();


        String TIME_SERVER = keyIterator.next();

        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress;
        while(true){
            try {
                inetAddress = InetAddress.getByName(TIME_SERVER);
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                long returnTime = timeInfo.getReturnTime();
                DateTime time = new DateTime(returnTime);

            } catch (java.net.UnknownHostException e) {
                if (keyIterator.hasNext()){
                    TIME_SERVER = keyIterator.next();
                } else {
                    throw new RuntimeException("None of the servers are available.  Try again later.");
                }

            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

    }

}




class MockClock implements IClock{
    private Time now;
    public MockClock(Time now){
        this.now = now;
    }
    public Time now(){
        return now;
    }
}