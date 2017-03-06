public class Main {

    static boolean choosingWaiting[] = new boolean [10];
    static int ticketWaiting[] = new int [10];
    static boolean choosingBarber[] = new boolean [10];
    static int ticketBarber[] = new int [10];
    static int ticketNumberWaiting = 0;
    static int ticketNumberBarber = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Customer(i);
        }
    }

    static int maxValueWaiting(){
        return ticketNumberWaiting++;
    }

    static int maxValueBarber(){
        return ticketNumberBarber++;
    }

}

class Customer implements Runnable{

    int id;

    public Customer(int id){
        this.id = id;
        new Thread(this).start();
    }

    @Override
    public void run() {
            int x = this.id;

            Main.choosingWaiting[x] = true;
            Main.ticketWaiting[x] = Main.maxValueWaiting() + 1;
            System.out.println("Thread: " + this.id + " Ticket Number: "+Main.ticketWaiting[x]);
            Main.choosingWaiting[x] = false;

            for (int i = 0; i < 10; i++) {
                if (i==x){continue;}
                while (Main.choosingWaiting[i]){passControlMethod();}
                while (Main.ticketWaiting[i] != 0 && Main.ticketWaiting[i] < Main.ticketWaiting[x]){passControlMethod();}
                if (Main.ticketWaiting[i] == Main.ticketWaiting[x] && i < x){
                    while(Main.ticketWaiting[i] != 0){}
                }
            }
            // critical code here

            while(!WaitingChairs.availableWaitingChair()){
                passControlMethod();
            }
            WaitingChairs.occupiedChairs++;
            System.out.println("Thread " + this.id + " is now in a chair");
            System.out.println("There are now " + WaitingChairs.occupiedChairs + " Waiting chair that are full");

            // exit mutual exclusion
            Main.ticketWaiting[x] = 0;


            Main.choosingBarber[x] = true;
            Main.ticketBarber[x] = Main.maxValueBarber() + 1;
            System.out.println("Barber Thread: " + this.id + " Ticket Number: "+Main.ticketBarber[x]);
            Main.choosingBarber[x] = false;

            for (int i = 0; i < 5; i++) {
                if (i==x){continue;}
                while (Main.choosingBarber[i]){passControlMethod();}
                while (Main.ticketBarber[i] != 0 && Main.ticketBarber[i] < Main.ticketBarber[x]){passControlMethod();}
                if (Main.ticketBarber[i] == Main.ticketBarber[x] && i < x){
                    while(Main.ticketBarber[i] != 0){passControlMethod();}
                }
            }
            // critical code here
            while(!BarberChairs.availableBarberChair()){
                passControlMethod();
            }
            WaitingChairs.occupiedChairs--;
            System.out.println(WaitingChairs.occupiedChairs + " Number of occupied waiting chairs");
            BarberChairs.occupiedBarberChairs++;
            System.out.println("Thread " + this.id + " is now in a Barber chair");
            System.out.println("There are now " + BarberChairs.occupiedBarberChairs + " Barber chair that are full");
            BarberChairs.occupiedBarberChairs--;

            // exit mutual exclusion
            Main.ticketBarber[x] = 0;

        }

    private void passControlMethod() {
        int x = 4 + 5;
    }

}

class WaitingChairs {
    static int occupiedChairs = 0;

    static boolean availableWaitingChair(){
        return occupiedChairs < 5;
    }
}

class BarberChairs {
    static int occupiedBarberChairs = 0;

    static boolean availableBarberChair(){
        return occupiedBarberChairs < 2;
    }
}




