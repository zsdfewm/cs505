package edu.purdue.cs505;

public class TestRecv{
  public static void main(String args[]){
    Channel recver=new Channel("node2", 9877);
    recver.init("localhost",9876);
    recver.rlisten(null);
    try{
      Thread.sleep(30000);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("recver done");
    recver.halt();
  }
}
