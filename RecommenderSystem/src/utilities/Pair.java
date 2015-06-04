package utilities;

import java.io.Serializable;

public class Pair<T, U> implements Serializable {         
    public final T t;
    public final U u;

    public Pair(T t, U u) {         
        this.t= t;
        this.u= u;
     }
    
    public T getFirst(){
    	return t;
    }
    
    public U getSecond(){
    	return u;
    }
 }
