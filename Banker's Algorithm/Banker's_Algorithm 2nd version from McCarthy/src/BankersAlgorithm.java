package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.Semaphore;

import static com.company.Bank.serr;
import static com.company.BankersAlgorithm.so;
import static java.lang.Integer.min;


//--------------------------------------------------------------------------
// class BankersAlgorithm
// --------------------------------------------------------------------------
public class BankersAlgorithm {
  public static PrintStream so = System.out;
  static Semaphore mutex = new Semaphore(1);
  static Semaphore issafe_mutex = new Semaphore(1);

  static void runSimulation(Bank bank) {
    so.println("\nBanker's algorithm simulation beginning...\n--------------------------------------------");
    bank.show("");

    Vector<Customer> customers = bank.getCustomers();
    for (Customer c : customers) {
      c.start();
    }
  }

  //============================================== confirms argc > 1
  private static void verify(String[] args) {
    if (args.length == 0) {
      serr.println("Usage: java bankers filename1 [filename2 filename3 ...]");
      System.exit(1);
    }
  }

  public static String strstrip(String s) {
    String t = s.replaceAll("[^a-zA-Z0-9 ]", "");
    return t;
  }

  private static void processLine(String buf, ExtVector values) {    // gets values from one line
    buf = strstrip(buf);  // strip punctuation
    buf = buf.toLowerCase();
    values.clear();
    String[] tokens = buf.split("\\s+");
    for (String el : tokens) {
      int val = Integer.parseInt(el);
      values.add(val);      // convert to int, add to int array
    }
  }

  private static Bank processFile(String filename) {    // extracts avail for Bank, customers' alloc and max
    String buf = null;
    ExtVector res = new ExtVector();
    File file = new File(filename);      // pass the path to the file as a parameter
    Scanner sc = null;
    Bank bank = null;

    try {
      sc = new Scanner(file);
    } catch(FileNotFoundException e) {
      serr.println("\n\nWarning, could not open file: '" + filename + "'");
      return null;
    }

    so.println("\n\nProcessing file: '" + filename + "'...");
    boolean firstLine = true;
    int idx = 0;
    while (sc.hasNextLine()) {
      buf = sc.nextLine();
      if (buf.length() == 0) { break; }

      processLine(buf, res);
      if (firstLine) {    // first line has bank's resources
        ExtVector avail = new ExtVector(res);
        bank = new Bank(avail);
        firstLine = false;
      } else {
        ExtVector alloc = new ExtVector();
        ExtVector  max  = new ExtVector();
        int size = bank.getAvail().size();
        for (int i = 0; i < size; ++i) {
          alloc.add(res.get(i));            // e.g., for size = 2,  0, 1
          max.add(res.get(i + size));       // ditto,               2, 3
        }
        Customer c = new Customer(idx++, alloc, max, bank);
        bank.addCustomer(c);
      }
    }
    return bank;
  }

  private static void processFiles(String[] args) {    // processes all files in command line
    Bank bank = null;
    for (String filename : args) {
      bank = processFile(filename);

      if (bank == null || bank.getCustomers().isEmpty()) {
        serr.println("\t\tNo customers found... exiting...\n\n");
        System.exit(1);
      }
      runSimulation(bank);
      so.println("\n\n");
    }
  }

  public static void main(String[] args) {
//    ExtVector.runTests();
    processFiles(args);
  }
}
