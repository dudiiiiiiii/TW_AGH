package com.company;

public class Counter{
    public int count = 0;

    public static void main(){
        new Counter().unsync();
    }

    public void unsync(){

        new Thread ( ) {
            public void run ( ) {
                for(int i=0; i < 10000000; i++) zwieksz() ;
            }
        }.start() ;
        new Thread ( ) {
            public void run ( ) {
                for(int i=0; i < 10000000; i++) zmniejsz() ;
            }
        }.start() ;

        System.out.println("Counter is now: " + this.count);

    }

    public void sync(){

        Thread one = new Thread ( ) {
            public void run ( ) {
                for(int i=0; i < 10000000; i++) zwieksz2() ;
            }
        } ;

        Thread two = new Thread ( ) {
            public void run ( ) {
                for(int i=0; i < 10000000; i++) zmniejsz2() ;
            }
        };

        one.start();
        two.start();

        System.out.println("Counter after is: " + this.count);

    }

    private void zwieksz() {
        count++;
    }

    private void zmniejsz() {
        count--;
    }

    private synchronized void zwieksz2() {
        count++;
    }

    private synchronized void zmniejsz2() {
        count--;
    }
}
