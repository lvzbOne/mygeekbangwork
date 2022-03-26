package week4.question_1.conc0301;

public class ThreadCount {
    public static void main(String[] args) throws InterruptedException {
        /**
         * java.lang.ThreadGroup[name=system,maxpri=10]
         *     Thread[Reference Handler,10,system]
         *     Thread[Finalizer,8,system]
         *     Thread[Signal Dispatcher,9,system]
         *     Thread[Attach Listener,5,system]
         *     java.lang.ThreadGroup[name=main,maxpri=10]
         *         Thread[main,5,main]
         *         Thread[Monitor Ctrl-Break,5,main]
         */
        //System.out.println("system："+Thread.currentThread().getThreadGroup().getParent());
        Thread.currentThread().getThreadGroup().getParent().list();

//        System.out.println("main："+Thread.currentThread().getThreadGroup());
//        Thread.currentThread().getThreadGroup().list();
    }
}
