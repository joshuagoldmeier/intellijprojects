/**
 * Created by joshuagoldmeier on 3/5/17.
 */

public class Main {


    static int counter = 0;
    static int x = 1;
    static boolean[] choosing = new boolean[10];
    static int[] ticketAddition = new int[10];
    static int[] ticketMult = new int[10];

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new MyThread(i)).start();
        }
    }

}

class MyThread implements Runnable {

    int id;

    public MyThread(int id) {
        this.id = id;
    }

    @Override
    public void run() {

        Main.choosing[id] = true;
        Main.ticketAddition[id] = id + 1;
        Main.ticketMult[id] = id + 1;
        Main.choosing[id] = false;

        for (int i = 0; i < 10; i++) {
            if (i == id) {continue;}
            while (Main.choosing[i]) { emptyMethod(); }
            while (Main.ticketAddition[i] != 0 && Main.ticketAddition[i] < Main.ticketAddition[id]) { emptyMethod();}
            if (Main.ticketAddition[i] == Main.ticketAddition[id] && i < id) {

                while (Main.ticketAddition[i] != 0) {
                    emptyMethod();
                }
            }
        }
        //critical section
        Main.counter++;
        System.out.println(id + " adding 1 to counter = " + Main.counter);

        // Exit Critical Section
        Main.ticketAddition[id] = 0;

        while(Main.counter < 10){
//            System.out.println(id + "stuck");
            Thread.yield();
            emptyMethod();
        }
        System.out.println("TEN TIMES");

        for (int i = 0; i < 10; i++) {
            if (i == id) {continue;}
            while (Main.choosing[i]) { emptyMethod(); }
            while (Main.ticketMult[i] != 0 && Main.ticketMult[i] < Main.ticketMult[id]) { emptyMethod();}
            if (Main.ticketMult[i] == Main.ticketMult[id] && i < id) {

                while (Main.ticketMult[i] != 0) {
                    emptyMethod();
                }
            }
        }

//        critical section
        Main.x *= 2;
        System.out.println(id + " multiplying x by 2 = " + Main.x);

        Main.ticketMult[id] = 0;

    }

    private void emptyMethod() {
        int z = 4 + 4;
    }
}
