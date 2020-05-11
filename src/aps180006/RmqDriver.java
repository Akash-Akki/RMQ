/**
 * @author  Akash P Akki      netid: apa190001
 * @author  Anant Srivastava  netid: aps180006
 */


package aps180006;

import java.util.Random;

public class RmqDriver {

  public static  void  main(String[] args){
    Rmq rmq = new Rmq();
    Random 	random = new Random();
    int n=100;
    int i=0;
    int j=n-1;
    if(args.length > 0) {
      n = Integer.parseInt(args[0]);
      i = Integer.parseInt(args[1]);
      j =Integer.parseInt(args[2]);
    }
   int[] arr = new int[n];
    for(int k=0; k<n; k++) {
      arr[k] = random.nextInt();
    }
    Timer timer = new Timer();
    timer.start();
    System.out.println(" start");
    timer.start();
    int[][] sparse = rmq.preProcessing(arr);
    System.out.println(timer.end());
    timer.start();
    int min = rmq.findRmq(arr,i,j,sparse);
   System.out.println(timer.end());
  }

}
