package myhungarianalgorithm;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    public static void main(String[] args) {
        
        try {
            int[][] matrix = {  
                /*{7,1,10,8,9},
                {5,1,3,6,9},
                {1,5,10,9,9},
                {8,10,3,2,7},
                {9,2,1,9,6}*/
                /*{6,15,3,12,4,2},
                {14,3,3,7,2,1},
                {3,2,8,15,8,12},
                {3,14,3,15,11,10},
                {3,13,1,9,6,6},
                {15,10,3,4,5,10}*/
                {9,5,5,3,3},
                {5,5,6,10,10},
                {7,3,4,7,2},
                {3,3,5,7,1},
                {3,10,8,2,4}
            };
            MyHungarianAlgorithm algorithm = new MyHungarianAlgorithm(matrix);
            algorithm.calculate();
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
