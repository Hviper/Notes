package 最终版强制指定时间内关闭终止正在进行的线程;

/**
 * @author 拾光
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        Task task = new Task();
        long start = System.currentTimeMillis();
        task.execute(()->{       //函数直接遇到线程的start()方法 return 掉了这个函数
            //download great resource
            while(true){

            }
        });
        task.showdown(100);
        System.out.println(System.currentTimeMillis()-start);

    }
}
class Task{
    Thread thread;     //让这个thread的守护线程去干活，thread的守护线程需要被结束的时候调用thread的interrupt()打断方法
    boolean flag = false;
    public void execute(Runnable task){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread t1 = new Thread(task);
                t1.setDaemon(true);
                t1.start();
                try {
                    t1.join();          //因为thread的守护线程的生命周期很短【因为设置了守护线程】，run运行完就没了，所以需要使用join()方法
                    flag = true;  //能来到这里说明：join（）程序完成，即t1这个线程执行完成
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        //这里start（）以后会直接返回！！！！！，而join（）这里不会直接返回
//        showdown(timeout);
    }
    void showdown(long timeout){
        long begin = System.currentTimeMillis();
        while(!flag){
            if(System.currentTimeMillis()-begin>=timeout){
                System.out.println("任务超时，需要结束他");
                thread.interrupt();     //因为内部的任务线程是由thread进行join()的，所以可以打断
                break;
            }else{
                try {
                    thread.sleep(1);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    System.out.println("当前线程被Main线程打断");
                    break;
                }
            }
            flag = false;
        }

    }
}

