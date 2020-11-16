

//-------------------------------------------------------------------------------------------------
// Factory.java
//
// Factory class that creates the bank and each bank customer

import java.io.*;
import java.util.*;



public class Factory {
    // Usage:  java Factory 10 5 7

    public static void main(String[] args) {


        // Had to add these to fix issues McCarthy left out

        int threadNum = 0;  //threadNum is the
        int resourceNum = 0;
        int placementInArray = 0;

        //-------------------------------------------------

        String filename = "/Users/williammccarthy/IdeaProjects/BankersAlgorithmProject_ch7/src/infile.txt";
        String myFileName = "D:\\IntelliJ 351 Operating Systems\\Banker's Algorithm\\src\\infile.txt";
        //String myFileName = "infile.txt";

        File f = new File(myFileName);
        if(f.exists() && !f.isDirectory()) {
            // do something
            System.out.println("File Exists!!!!");
        }

        ExtVector myVector = new ExtVector();


        //File Handling
        //----------------------------------------------------------------------
        StringBuilder fileContents = new StringBuilder();
        try {
            File file = new File(myFileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine() + " ";
                fileContents.append(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        String input = String.valueOf(fileContents);
        //StringTokenizer st
        System.out.print(input);
        //-------------------------------------------------------------------------


        //Tokenization
        //--------------------------------------------------------------------------
        String tokenizedArray[] = input.split(",|\\n|\\s");

        int inputNumbers[] = new int[tokenizedArray.length];
        for (int i = 0; i < inputNumbers.length; i++){
            inputNumbers[i] = Integer.parseInt(tokenizedArray[i]);
        }
        System.out.println();
        for (int i = 0; i < inputNumbers.length; i++)
            System.out.print(inputNumbers[i]);
        System.out.println();
        //----------------------------------------------------------------------------


        int nResources = args.length;
        int[] resources = new int[nResources];
        //Temporary Passage
        //int resources[] = {10, 5, 7};

        // For each index in resources[] (less than size of nResources), fill
        for (int i = 0; i < nResources; i++) {
            resources[i] = Integer.parseInt(args[i].trim());
        }

        // make Bank object called theBank and pass resources[] into BankImpl; these are your starting resources
        Bank theBank = new BankImpl(resources);

        // I think these are the Customer's resources and maxDemand;
        // I need to read in the inFile, and for each 6 numbers (allocated, maxDemand), read them in and then
        // initialize them into allocated and maxdemand; probably going to have to loop through it.
        int[] allocated = new int[nResources];
        int[] maxDemand = new int[nResources];



        // An array of Threads called workers;
        Thread[] workers = new Thread[Customer.COUNT];      // the customers

        // read in values and initialize the matrices
        // todo
        // ...

        //This section initializes the threads.
        //-------------------------------------------------------------------------------------
        for (int i = 0; i < Customer.COUNT; i++){

            for (int j = 0; j < allocated.length; j++){
                allocated[j] = inputNumbers[placementInArray];
                placementInArray++;
            }
            for (int k = 0; k < maxDemand.length; k++){
                maxDemand[k] = inputNumbers[placementInArray];
                placementInArray++;
            }
            workers[i] = new Thread(new Customer(threadNum, maxDemand, theBank));
            theBank.addCustomer(threadNum, allocated, maxDemand);
            threadNum++;
        }
        //-------------------------------------------------------------------------------------

        System.out.println("Bank fully initialized.");
        resourceNum = 0;
        System.out.println("FACTORY: created threads");     // start the customers
        for (int i = 0; i < Customer.COUNT; i++) {
            workers[i].start();
            //workers[i].
        }
        System.out.println("FACTORY: started threads");

        System.out.println("The Bank Final State");
        theBank.getState();

    }







}

