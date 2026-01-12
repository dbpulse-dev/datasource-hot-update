import lombok.Data;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TrafficWeightTest {


    ThreadPoolExecutor executor  = new ThreadPoolExecutor(200,200,30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    },new ThreadPoolExecutor.AbortPolicy());

    @Test
    public void test() throws InterruptedException {

        int weight = 20;
        int cellCount = 10000;
        int grayRang = cellCount/100*weight;
        Random r = new Random();
        CircularLinkedListQueue<Env> queue = new CircularLinkedListQueue<>();
        int grayCount =0;
        for(int i=0;i<cellCount;i++){
            int index = r.nextInt(cellCount);
            if(index<grayRang){
                queue.enqueue(new Env(i,"gray"));
                grayCount++;
            }else {
                queue.enqueue(new Env(i,"normal"));
            }
        }
        System.out.println("灰度计数:"+grayCount);
        AtomicInteger normal = new AtomicInteger(0);
        AtomicInteger gray= new AtomicInteger(0);
        int requests = 10000;
        for(int i=0;i<10000;i++){
            executor.execute(() -> {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Env next = queue.getNext();
                //System.out.println(next.getEnv()+":"+next.index);
                synchronized (TrafficWeightTest.class){
                    if(next.getEnv().equals("gray")){
                        gray.incrementAndGet();
                    }else {
                        normal.incrementAndGet();
                    }
                }
            });
            Thread.sleep(3);
        }
        String format = String.format("%.2f%%", (gray.floatValue() / requests) * 100);
        System.out.println("gray 比例:"+format);
    }


    @Data
    static class Env{
        private int index;
        private String env;

        public Env(int index,String env){
            this.index = index;
            this.env = env;
        }
    }
}

