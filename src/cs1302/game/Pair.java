package cs1302.game;

import java.time.Year;

public class Pair<T> {
        private T first;
//        private U second;

//    public Pair(T first, U second) {
    //      this.first = first;
    //  this.second = second;
    // }

     public Pair(T first) {
         this.first = first;
//         this.second = null;
     }

    public Pair() {
        this.first = null;
        //      this.second = null;
    }
    public static void main(String[] args) {

        Pair <java.time.Year> p = new Pair<>();
    }
//<String, Boolean>
}
