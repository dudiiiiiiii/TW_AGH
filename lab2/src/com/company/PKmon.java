package com.company;

import java.util.ArrayList;
import java.util.List;

class Producer extends Thread {
    private Buffer _buf;

    public Producer(Buffer buf){
        this._buf = buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            _buf.put(i);
        }
    }
}

class Consumer extends Thread {
    private Buffer _buf;

    public Consumer(Buffer buf){
        this._buf = buf;
    }

    public void run() {
        for (int i = 0; i < 100; ++i) {
            System.out.println(_buf.get());
        }
    }
}

class Buffer {
    private ArrayList<Integer> num = new ArrayList<>();

    public synchronized void put(int i) {
        num.add(i);
    }

    public synchronized int get() {
        while(!(num.size() > 0)){
            try{
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        int res = num.get(num.size()-1);
        num.remove(num.size()-1);
        notifyAll();

        return res;
    }
}

public class PKmon {
    public static void main(String[] args) {
        int a = 3;
        int b = 5;
        // V1
//        Buffer buf = new Buffer();
//
//        new Producer(buf).start();
//        new Consumer(buf).start();

        // V2
        Producer prod[] = new Producer[a];
        Consumer con[] = new Consumer[b];
        Buffer buf2 = new Buffer();

        for(int i=0 ; i < a ;i ++){
            prod[i] = new Producer(buf2);
            prod[i].start();
        }

        for(int i=0 ; i < b ;i ++){
            con[i] = new Consumer(buf2);
            con[i].start();
        }

    }
}
