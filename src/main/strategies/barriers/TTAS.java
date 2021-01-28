package edu.vt.ece.hw4.barriers;

import edu.vt.ece.hw4.locks.Lock;

import java.util.concurrent.atomic.AtomicBoolean;
public class TTAS implements Lock {
    AtomicBoolean state = new AtomicBoolean(false);
    public void lock(){
        while(true){
            while(state.get()){};
            if(!state.getAndSet(true))
                return;
        }
    }
    public void unlock(){
        state.set(false);
    }
}
