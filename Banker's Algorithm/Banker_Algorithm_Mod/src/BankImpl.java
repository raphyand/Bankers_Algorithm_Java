//-------------------------------------------------------------------------------------------------
// BankImpl.java
//
// implementation of the Bank
//
import java.io.*;
import java.util.*;

public class BankImpl implements Bank {
    private int n;			// the number of threads in the system
    private int m;			// the number of resources

    private int[] available; 	// the amount available of each resource; storage; usually it'll start with a set
                                // then, as threads request, available is reduced;
                                // as threads release resources, available is increased; available + allocation
    private int[][] maximum; 	// the maximum demand of each thread;
    private int[][] allocation;	// the amount currently allocated to each thread
    private int[][] need;		// the remaining needs of each thread;  calculate: maximum - allocation


    private void showAllMatrices(int[][] alloc, int[][] max, int[][] need, String msg) {
        // todo



    }

    private void showMatrix(int[][] matrix, String title, String rowTitle) {
        // todo
    }

    private void showVector(int[] vect, String msg) {
        // todo
    }

    public BankImpl(int[] resources) {      // create a new bank (with resources)
        // todo
        // set available to the original resources of the bank
        this.available = resources;

        // establish all three matrices by using Thread Count, which is Customer Count, and also number of resources
        this.maximum = new int[Customer.COUNT][resources.length];
        this.allocation = new int[Customer.COUNT][resources.length];
        this.need = new int[Customer.COUNT][resources.length];

        // Initialize Matrices
        for (int i = 0; i < Customer.COUNT; i++){
            for (int k = 0; k < resources.length; k++){
                maximum[i][k] = 0;
                allocation[i][k] = 0;
                need[i][k] = 0;
            }
        }

    }


    // invoked by a thread when it enters the system;  also records max demand
    public void addCustomer(int threadNum, int[] allocated, int[] maxDemand) {
        // in row threadNum, add customer allocated[] to bank allocation[][], and then set maxDemand[] to maximum[][]
        //for (int i = 0; i < available.length; i++){
//            allocation[threadNum][i] += allocated[i];
        //}
    }

    public void getState() {        // output state for each thread
        // todo
    }

    private boolean isSafeState (int threadNum, int[] request) {
        // todo -- actual banker's algorithm
        return false;
    }
    // make request for resources. will block until request is satisfied safely
    public synchronized boolean requestResources(int threadNum, int[] request)  {
        // todo
        return false;
    }

    public synchronized void releaseResources(int threadNum, int[] release)  {
        // todo

    }


}