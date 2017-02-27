public class Main {

    public static void main(String[] args) {
        Tracker tracker = new Tracker();
        new Cook(tracker);
        new Savage(tracker);


    }

}

class Cook implements Runnable {

    Tracker tracker;

    public Cook(Tracker tracker) {
        this.tracker = tracker;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(true) {
            tracker.flag[0] = true;
            tracker.turn = 1;
            while (tracker.flag[1] && tracker.turn == 1) {/* busy wait */ }

            if (Pot.piecesOfMeat == 0) {
                fillPot();
                System.out.println("Cooking: " + Pot.piecesOfMeat + " pieces of meat in the pot");
            }

            // end of critical section
            tracker.flag[0] = false;
        }
    }

    void fillPot(){
        Pot.piecesOfMeat += 10;
    }
}

class Savage implements Runnable {

    Tracker tracker;

    public Savage(Tracker tracker) {
        this.tracker = tracker;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(true){
            tracker.flag[1] = true;
            tracker.turn = 0;
            while (tracker.flag[0] && tracker.turn == 0){/* busy wait */}

            if (Pot.piecesOfMeat > 0){
                eatFromPot();
                System.out.println("Eating: " + Pot.piecesOfMeat + " pieces of meat in the pot");
            }

            // end of critical section
            tracker.flag[1] = false;
        }
    }

    void eatFromPot(){
        Pot.piecesOfMeat--;
    }
}

class Tracker {
    boolean flag[] = {false, false};
    int turn;
}

class Pot {
    static int piecesOfMeat;
}
