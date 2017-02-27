package MCO152;

/**
 * Created by joshuagoldmeier on 10/6/16.
 */

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.joda.time.DateTime;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * current time is determined by a Time Server (from NIST, or other source)
 * @author YOUR NAME
 */
class TimeServerClock implements IClock{



    @Override
    public Time now(){
        String fileName = FileSystemView.getFileSystemView().getHomeDirectory().toString() + "/robinson.txt";
        FileOutputStream os = createFileOutputStream(fileName);
        createProperties(os);
        Properties timeServers = loadPropertiesOfServers(fileName);
        return getTheServerTime(timeServers);
    }

    private Time getTheServerTime(Properties timeServers) {
        Set<String> setOfKeys = timeServers.stringPropertyNames();
        Iterator<String> keyIterator = setOfKeys.iterator();
        InetAddress inetAddress = getInetAddress(keyIterator);
        TimeInfo timeInfo = getTimeInfo(inetAddress);
        return convertTimeinfoToTime(timeInfo);
    }

    private TimeInfo getTimeInfo(InetAddress inetAddress) {
        try {
            return new NTPUDPClient().getTime(inetAddress);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }

    }

    private InetAddress getInetAddress(Iterator<String> theIterator) {
        Iterator<String> keyIterator = theIterator;
        String TIME_SERVER = keyIterator.next();
        while(true){
            try {
                return InetAddress.getByName(TIME_SERVER);
            } catch (UnknownHostException e) {
                TIME_SERVER = iterateThroughProps(keyIterator);
            }
        }
    }

    private String iterateThroughProps(Iterator<String> keyIterator) {
        if (keyIterator.hasNext()){
            return keyIterator.next();
        } else{
            throw new RuntimeException("None of the servers are available.  Try again later.");
        }
    }


    private Time convertTimeinfoToTime(TimeInfo timeInfo) {
        long returnTime = timeInfo.getReturnTime();
        DateTime time = new DateTime(returnTime);
        return Time.newTimeHourMinuteSecond(time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute());
    }

    private Properties loadPropertiesOfServers(String fileName) {
        Properties timeServers = new Properties();
        try {
            timeServers.load(new FileInputStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        return timeServers;
    }

    private void createProperties(FileOutputStream os) {
        Properties timeServers = new Properties();
        timeServers.setProperty("Should Fail", "FAILLL");
        timeServers.setProperty("The Cowboy Hats","Once a day");
        timeServers.setProperty("time-a.nist.gov", "time-a.nist.gov");
        try {
            timeServers.store(os, "Not sure why I need this");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileOutputStream createFileOutputStream(String fileName) {
        try {
            return new FileOutputStream(new File(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
    }

}
