package aps180006;


public class RmqDriver {





  public static  void  main(String[] args){

    Rmq rmq = new Rmq();
    int[] arr = new int[]{4,5,8,7,9,4,5,8,4,8,7};

   // System.out.println("arr"+arr.length);
    for(int i= 0; i< arr.length;i++){
      for(int j=i;j<arr.length;j++){
        System.out.print(" "+i+ " "+j);
        rmq.findRmq(arr,i,j);
      }
    }







  }

}
