import java.lang.*;
import java.util.*;

public class Test{
  public int i;
  public Test(){
    i=0;
  }
  public boolean equals(Test t){
    if (t.i==this.i){
      return true;
    }
    else{
      return false;
    }
  }
  public static void main(String[] args){
    HashMap<Test, String> map=new HashMap<Test, String>();
    Test s1=new Test();
    Test s2=new Test();
    map.put(s1,"123");
    System.out.println(map.put(s2,"234"));
    System.out.println(map.get(s1));   
  }
}
