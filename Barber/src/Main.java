public class Main {

    static boolean choosing [] = new boolean [10];
    static int ticket [] = new int [10];

    public static void main(String[] args) {
        startThreads();
    }

    public static void startThreads(){
        new Customer();
        new Barber();
    }

    static int maxValue(int[] arr){
        return 0;
    }

}

class Customer implements Runnable{

    public Customer(){
        new Thread(this).start();
    }

    @Override
    public void run() {
        int x = 0;
        while(true){
            Main.choosing[x] = true;
            Main.ticket[x] = Main.maxValue( Main.ticket ) + 1;
            Main.choosing[x] = false;

            for (int i = 0; i < 10; i++) {
                if (i==x){continue;}
                while (Main.choosing[i] != false){}
                while (Main.ticket[i] != 0 && Main.ticket[i] < Main.ticket[x]){}
                if (Main.ticket[i] == Main.ticket[x] && i < x){
                    while(Main.ticket[i] != 0){}
                }
            }

            // critical code here

            Main.ticket[x] = 0;
        }
    }
}

class Barber implements Runnable {

    public Barber(){
        new Thread(this).start();
    }

    @Override
    public void run() {

    }
}


