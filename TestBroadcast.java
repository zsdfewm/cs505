import edu.purdue.cs505.*;

public class TestBroadcast{
  public static void main(String args[]){
    Broadcast bc=new Broadcast();
    Process p0=new Process("localhost", 9876);
    Process p1=new Process("localhost", 9877);
    bc.init(p0);
    bc.add(p1);
    bc.rblisten(null);
try{
    Thread.sleep(1000);
    Message m=new Message();
    m.setContents("1");
    bc.rbroadcast(m);
}
catch(Exception e){
  e.printStackTrace();
}

  }
}
