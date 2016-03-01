package jp.ac.it_college.std.s13012.androidchallenge;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by s13008 on 16/03/01.
 */


public class QueueNext extends ArrayList{

    private int count = 0;
    int[][] start;

    LinkedList<int[][]> nextList = new LinkedList<int[][]>();
    LinkedList<Integer> isNextColor = new LinkedList<Integer>();

    public int[][] getNext(){
        return nextList.peekFirst();
    }

    public Integer getColor(){
        return isNextColor.peekFirst();
    }

    public void setNext(int[][] next) {
        nextList.add(next);
    }

    public void setNextColor(int color){
        isNextColor.add(color);
    }


    /*
    private int Array[][] = new int[3][];
    private Queue queue = new PriorityQueue();*/

}