//-------------------------------------------------------------------------------------------------
// BankImpl.java
//
// implementation of the Bank
//
import java.io.*;
import java.util.*;

public class BankImpl implements Bank {
    private int n = 0;			// the number of threads in the system
    private int m = 0;			// the number of resources

    private int[] available; 	// the amount available of each resource; storage; usually it'll start with a set
    // then, as threads request, available is reduced;
    // as threads release resources, available is increased; available + allocation
    private int[][] maximum; 	// the maximum demand of each thread;
    private int[][] allocation;	// the amount currently allocated to each thread
    private int[][] need;		// the remaining needs of each thread;  calculate: maximum - allocation



    private void showAllMatrices(int[][] alloc, int[][] max, int[][] need, String msg) {
        // todo
        showMatrix(allocation, "ALLOCATION", "P0");
        showMatrix(maximum, "MAXIMUM", "P1");
        showMatrix(need, "NEED", "P2");
        System.out.println(msg);

    }

    private void showMatrix(int[][] matrix, String title, String rowTitle) {
        // todo

        System.out.print(title + " " + rowTitle + "\n");
        for (int i = 0; i < matrix.length; i++){
            System.out.print("[ ");
            for (int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("]\n");
        }
    }

    private void showVector(int[] vect, String msg) {
        // todo
        System.out.print("[ ");
        for (int i = 0; i < vect.length; i++){
            System.out.print(vect[i] + " ");
        }
        System.out.print("] ");
        System.out.print(msg + "\n");

    }

    private boolean allFlagsTrue(boolean[] flags){
        // If all flags are true, do nothing, then return true;
        // If even one flag is false, return false;
        for (int i = 0; i < flags.length; i++){
            if(flags[i] == false)
                return false;
        }
        System.out.println("System is in safe state/all flags are true.");
        return true;
    }

    private boolean isVectorLessOrEqualThanVector(int[] lhs, int[] rhs){
        for (int i = 0; i < lhs.length; i++){
            if (lhs[i] > rhs[i])
                return false;
        }
        return true;
    }

    private boolean isThreadNeedComplete(int threadNum){
        for (int i = 0; i < need[threadNum].length; i++){
            if (need[threadNum][i] != 0)
                return false;
        }
        return true;
    }

    public BankImpl(int[] resources) {      // create a new bank (with resources)
        // todo
        // set available to the original resources of the bank
        this.available = resources;
        this.n = resources.length;


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
        for (int i = 0; i < allocated.length; i++){
            allocation[threadNum][i] += allocated[i];
        }
        for (int i = 0; i <maxDemand.length; i++){
            maximum[threadNum][i] = maxDemand[i];
        }

        for (int i = 0; i < need[threadNum].length; i++){
            need[threadNum][i] = maxDemand[i] - allocated[i];
        }
        n++;

        showVector(available, "These are the current available resources of the bank.");
        showVector(allocated, "This is allocated for the Customer.");
        showVector(maxDemand, "This is the customer's maximum Demand.");
        showVector(need[threadNum], "This is the calculated need.");

        showAllMatrices(allocation, maximum, need, "All Matrices being shown.");
    }

    public void getState() {        // output state for each thread
        // todo
        // For one thread, is it in safe state or unsafe state?
        showAllMatrices(allocation, maximum, need, "All Matrices");
    }

    private synchronized boolean isSafeState (int threadNum, int[] request) {
        // todo -- actual banker's algorithm
        //Work and Finish Vectors
        int[] simulatedAvailable = new int[available.length];
        System.arraycopy(available, 0, simulatedAvailable, 0, available.length);
        boolean[] isEveryNeedSatisfied = new boolean[simulatedAvailable.length];
        int count = 0;


        // 0. we PRETEND that request is granted, and simulate it being granted to threadNum's allocation
        // 1. establish all flags as 0
        // 2. For each process, find whether isEveryNeedSatisfied[i] = false && need[threadNum][i] <= simulatedAvailable
        // 3. for


        // Step 0
        //
        for (int i = 0; i < request.length; i++){
            allocation[threadNum][i] += request[i];
            simulatedAvailable[i] -= request[i];
        }

        // Step 1
        for (int i = 0; i < isEveryNeedSatisfied.length; i++){
            isEveryNeedSatisfied[i] = false;
        }

        // Step 2
        while (!allFlagsTrue(isEveryNeedSatisfied) && count < isEveryNeedSatisfied.length){
            // For each process
            for (int i = 0; i < isEveryNeedSatisfied.length; i++){
                // Is the flag false and is the need of this process less than simulated available?
                if (isEveryNeedSatisfied[i] == false && isVectorLessOrEqualThanVector(need[i], simulatedAvailable)){
                    // Step 3.
                    // If yes, then add allocation to simulated available, then set flag to true
                    for (int j = 0; j < simulatedAvailable.length; j++){
                        simulatedAvailable[j] += allocation[i][j];
                        isEveryNeedSatisfied[i] = true;
                    }
                }
            }
            // Theoretically, if after an entire run through every process has been made and it hasn't exited
            // the while loop, then it is in an unsafe state.  Then we must exit out of the loop through count++
            // being greater than the length of threads.
            count++;
        }



        // If we are in a safe state, then reset allocation vector of threadNum to state before simulation
        //if (allFlagsTrue(isEveryNeedSatisfied)){
            for(int i = 0; i < request.length; i++){
                allocation[threadNum][i] -= request[i];
            }
        //}

        //Step 4
        return allFlagsTrue(isEveryNeedSatisfied);
    }

    // make request for resources. will block until request is satisfied safely
    public synchronized boolean requestResources(int threadNum, int[] request)  {
        // todo
        // Algorithm:
        // Request is made.
        // Request compares itself with available
        // if request < available,
        //      subtract itself from available;
        //      then add itself to allocation
        //      then return true because request is ACCEPTED
        // if request > available
        //      return false because request is DENIED
        System.out.print("P"+ threadNum + " Requesting Resources: \n");
        showVector(available, "Available");
        showVector(request, " Requested");
        showVector(need[threadNum], " Need");

        //Step 1
        if (isVectorLessOrEqualThanVector(request, need[threadNum])){
            if (isVectorLessOrEqualThanVector(request, available)){
                if (isSafeState(threadNum, request)){

                    // Acceptance Area
                    for (int i = 0; i < request.length; i++){
                        available[i] -= request[i];
                        allocation[threadNum][i] += request[i];
                        need[threadNum][i] -= request[i];
                        notifyAll();
                        // need to update NEED
                        //need = max - allocation
                    }
                    System.out.print("Request: ACCEPTED. \n");
                    return true;
                }
                else{
                    System.out.println("Request: DENIED.");
                    return false;
                }
            }
            // How to implement wait???  For now, just deny request
            else{
                try{wait();} catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }

                System.out.print("Request: DENIED.  Waiting. \n");
                return false;
            }
        }
        else {
            System.out.println("Request: DENIED. Thread request is greater than Thread Need.");
            return false;
        }


    }

    public synchronized void releaseResources(int threadNum, int[] release)  {
        // todo

        if (isThreadNeedComplete(threadNum)) {
            for (int i = 0; i < allocation[threadNum].length; i++) {
                System.out.println("Release Resources here.");
                available[i] += allocation[threadNum].length + release[i];
                allocation[threadNum][i] = 0;
                Thread.currentThread().interrupt();
            }

            // Stop thread from running
            //return false;
        }
        //else return true; // Continue running.

    }


}