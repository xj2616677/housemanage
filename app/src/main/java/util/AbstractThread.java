package util;

/**
 * Created by admin on 2016/11/20.
 */
public abstract class  AbstractThread extends Thread{

    private int threadId;
    private boolean isRun=true;

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }



    @Override
    public void run() {
        super.run();
        while(isRun){
            run2();
        }
    }

    protected abstract void run2();


    public void stopThread(){
        isRun=false;
    }

    public boolean isRuning(){
        return isRun;
    }


}
