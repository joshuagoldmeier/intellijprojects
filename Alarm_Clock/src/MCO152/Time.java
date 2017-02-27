package MCO152;

import java.util.HashMap;

/**
 * Created by joshuagoldmeier on 8/31/16.
 */
class Time {

    private final int HOUR, MINUTE, SECOND;


    private Time(int hour, int minute, int second){
        if (invalidInput(hour, minute, second)){
            String errorMessage = whichUnitIsWrong(hour, minute, second);
            throw new IllegalArgumentException(String.format("%s unit(s) is invalid", errorMessage));
        }
        this.HOUR = hour;
        this.MINUTE = minute;
        this.SECOND = second;
    }


    public static Time newTimeHourMinute(int hour, int minute) {
        return new Time(hour, minute, 0);
    }

    public static Time newTimeHourMinuteSecond(int hour, int minute, int second) {
        return new Time(hour, minute, second);
    }


    public static Time timeCopyConstructor (Time t){
        return new Time(t.getHOUR(), t.getMINUTE(), t.getSECOND());
    }


    @Override
    public boolean equals(Object otherTime) {
        if (this == otherTime) return true;
        if (otherTime == null || getClass() != otherTime.getClass()) return false;

        Time that = (Time) otherTime;

        return this.HOUR == that.HOUR && this.MINUTE == that.MINUTE && this.SECOND == that.SECOND;
    }

    @Override
    public int hashCode() {
        int hashCode = HOUR;
        hashCode = 31 * hashCode + MINUTE;
        hashCode = 31 * hashCode + SECOND;
        return hashCode;
    }

    Time addHours(int hoursAdded){
        int difference = (this.HOUR + hoursAdded)%24;
        int newHours = difference >= 0 ? difference : 24 + difference;
        return new Time(newHours, this.MINUTE, this.SECOND);
    }

    Time addMinutes(int minutesAdded){
        int difference = (this.MINUTE + (minutesAdded%60))%60;
        int hoursToAdd = difference >= 0 ? minutesAdded / 60 : (minutesAdded / 60) - 1;
        Time t = this.addHours(hoursToAdd);
        int newMinutes = difference >= 0 ? difference : 60 + difference;
        return new Time(t.HOUR, newMinutes, this.SECOND);
    }

    Time addSeconds(int secondsAdded){
        int difference = (this.SECOND + (secondsAdded%60))%60;
        int minutesToAdd = difference >= 0 ? secondsAdded / 60 : (secondsAdded / 60) - 1;
        Time t = this.addMinutes(minutesToAdd);
        int newSeconds = difference >= 0 ? difference : 60 + difference;
        return new Time(t.HOUR, t.MINUTE, newSeconds);
    }


    @Override
    public String toString(){
        return String.format("%d:%02d:%02d", this.HOUR, this.MINUTE, this.SECOND);

    }

    public String toStringStandard(){
        int hours = this.HOUR %12 != 0 ? this.HOUR %12 : 12;
        String AMorPM = this.HOUR > 11 ? "PM" : "AM";
        return String.format("%d:%02d:%02d %s", hours, this.MINUTE, this.SECOND, AMorPM);
    }

    int getHOUR(){
        return HOUR;
    }

    int getMINUTE(){
        return MINUTE;
    }

    int getSECOND(){
        return SECOND;
    }


    HashMap getTime(){
        HashMap<String, Integer> time = new HashMap<>();
        time.put("HOUR", this.HOUR);
        time.put("MINUTE", this.MINUTE);
        time.put("SECOND", this.SECOND);
        return time;
    }

    int secondsUntil(Time untilTime) {
        return iterateThroughTimes(this.getTime(), untilTime.getTime());
    }

    private int iterateThroughTimes(HashMap<String, Integer> alarm, HashMap<String, Integer> current){
        int total = 0;
        for (String key: alarm.keySet()){
            if (current.get(key) != alarm.get(key)){
                total += computeTheDifference(current.get(key), alarm.get(key), key);
            }
        }
        return total;
    }

    private int computeTheDifference(int current, int alarm, String key){
        return (current - alarm) * getMultplier(key);
    }

    private int getMultplier(String str){
        int total = 1;
            switch (str){
                case "HOUR":
                    total*=60;
                case "MINUTE":
                    total*=60;
            }
        return total;
    }

    public static boolean invalidInput(int hour, int minute, int second){
        return (hour<0 || hour > 23 || minute < 0 || minute > 59 || second < 0 || second > 59);
    }

    private String whichUnitIsWrong(int hour, int minute, int second) {
        StringBuilder errorMessage = new StringBuilder();
        if (hour<0 || hour > 23){
            errorMessage.append("Hours");
        }
        if (minute < 0 || minute > 59){
            errorMessage.append(", Minutes");
        }
        if (second < 0 || second > 59){
            errorMessage.append(", Seconds");
        }
        return errorMessage.toString();

    }

}
