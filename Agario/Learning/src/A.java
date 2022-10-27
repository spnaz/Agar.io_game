/**
 * Created by bh on 03/08/2016.
 */
public class A {
    private static long count = 0;
    public static long innerCount = 0;

    public static void main(String[] args) {

        Runnable c = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < 10000; i++) {
                        double x = Math.random() * 2;
                        double y = Math.random() * 2;
                        if ((x - 1) * (x - 1) + (y - 1) * (y - 1) <= 1) {
                            innerCount++;
                        }
                        count++;
                    }

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(c);

        Runnable d = new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(((double) (innerCount) / count) * 4 + ":" + count);


                }

            }
        };
        Thread thread1 = new Thread(d);
        thread.start();
        thread1.start();


    }
}
