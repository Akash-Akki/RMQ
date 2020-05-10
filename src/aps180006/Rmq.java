package aps180006;

import java.sql.SQLSyntaxErrorException;

public class Rmq {

  public int findRmq(int[] a, int i ,int j) {
    double b = Math.log(a.length)/Math.log(2);
    int blockSize= (int) Math.ceil(b);

   // System.out.println("block size "+blockSize);
    int min1 = Integer.MAX_VALUE;
    int min2 = Integer.MAX_VALUE;
    int min3 = Integer.MAX_VALUE;
    int min =Integer.MAX_VALUE;
    int[] blockMinima = constructBlockMinima(a,blockSize);
    int colSize = findSizeOfSparseTable(blockMinima.length);
    //System.out.println(colSize);
//    for(int k=0;k<blockMinima.length;k++){
//      System.out.print(blockMinima[k]+ " ");
//    }
    //System.out.println("  ");
   int[][] sparse = constructSparseTable(blockMinima,colSize);

//   for(int k=0;k<blockMinima.length;k++){
//     for(int l=0;l<colSize;l++){
//       System.out.print(sparse[k][l]+" ");
//     }
//     System.out.println();
//   }

   if( i/blockSize == j/blockSize){
    // System.out.println("here 9");
     min=traverseIBlock(i,a, blockSize, j);
   }
   else if(i % blockSize!=0 && (j % blockSize != blockSize-1 && j!=a.length)){
     //System.out.println("here 1");
        min1 = traverseIBlock(i,a,blockSize,j);
        min2 = traverseJBlock(j - j % blockSize,a,j);
      //  System.out.println("min1 "+min1);
        //System.out.println("min 2 "+min2);
      //System.out.println((j/blockSize) -1);
     if((j/blockSize - i/blockSize ) >1 )
        min3 = query(sparse,(i/blockSize)+1,(j/blockSize) -1);
    // System.out.println("min 3 "+min3);
        min = Math.min(min3 ,Math.min(min1,min2));

    }
    else if(i % blockSize !=0 && (j%blockSize == blockSize-1 || j==a.length)){
     // System.out.println("here 2");
      min1 = traverseIBlock(i,a,blockSize,j);
      min3 = query(sparse,i/blockSize+1 ,j/blockSize);
      min= Math.min(min1,min3);
    }
    else if( i % blockSize == 0 && (j % blockSize != blockSize-1 && j!=a.length)){
      //System.out.println("here 3");
      min2 = traverseJBlock(j - j%blockSize,a,j);
      min3 = query(sparse,(i/blockSize),Math.max(j/blockSize -1,0));
      min= Math.min(min2,min3);
    }
    else {
      //System.out.println("here 4");
       min = query(sparse, i / blockSize, j / blockSize);
    }
    System.out.println("  "+min);
    return min;
  }

  private int traverseJBlock(int l, int[] a, int j) {
    int min =Integer.MAX_VALUE;
     //System.out.println("j");
    for(l=l;l<=j;l++){
      min = Math.min( min,a[l]);
    }
    return min;
  }

  private int traverseIBlock(int i, int[] a ,int blockSize,int j) {
    int min = Integer.MAX_VALUE;
     int blockNumber = (i/blockSize)+1;
    //System.out.println("b " +blockNumber*blockSize);
    //System.out.println("i is "+i);
    for(i=i ;i<blockNumber*blockSize && i<= j ; i++){
      min = Math.min(a[i],min);

    }
    return min;
  }

  private int query(int[][] sparse, int i, int j) {
      int k = findLargestK(i,j);
     // System.out.println("j is "+j);
      //System.out.println("k is "+k);
      int[][] ranges = {{i, (int) (i+Math.pow(2,k)-1)},{(int) (j - (Math.pow(2,k))+1),j}};
      int min=findMin(sparse,ranges);
      return min;


  }

  private int findMin(int[][] sparse, int[][] ranges) {
    int min =Integer.MAX_VALUE;
    int i1 = ranges[0][0];
    int i2 = ranges[1][0];
    int diffRange1 = ranges[0][1] - i1 + 1;
    int j1 = (int) (Math.log(diffRange1)/Math.log(2));
    int diffRange2 = ranges[1][1] - i2 + 1;
    int j2 = (int)Math.log((diffRange2)/Math.log(2));
    int val1 = sparse[i1][j1];
    int val2 = sparse[i2][j2];
    return Math.min(val1,val2);
  }

  private int findLargestK(int i, int j) {
    int k=0;
     while(Math.pow(2,k)<j-i+1){
       k++;
     }
    return k;
  }

  private int[] constructBlockMinima(int[] a, int blockSize) {
    double blockMinimaSize = (double)a.length/blockSize;
    int[] blockMinima = new int[(int) Math.ceil(blockMinimaSize)];
    int j=0;
    int minimum = a[0];
    for(int i=1;i<a.length;i++){
      if(i % blockSize == 0){
        minimum= Integer.MAX_VALUE;
        j++;
      }
      minimum = Math.min(minimum,a[i]);
      blockMinima[j] = minimum;
    }
    return blockMinima;
  }

  private int[][] constructSparseTable(int[] blockMinima,int colsize) {
      int rowSize = blockMinima.length;
      int[][] sparseTable = new int[blockMinima.length][colsize];
      for(int i=0;i<blockMinima.length;i++){
        sparseTable[i][0]=blockMinima[i];
      }
      for(int j=1 ; j < colsize;j++){
        for(int i=0; i < blockMinima.length ; i++ ){
          if(  (i + Math.pow(2,j-1)) >= blockMinima.length)
              break;
          sparseTable[i][j] = Math.min(sparseTable[i][j-1], sparseTable[(int) (i + Math.pow(2,j-1))][j-1]);
        }
      }
     return sparseTable;
  }

  private int findSizeOfSparseTable(int length) {
     int colsize=1;
     int i=0;
     while(true){
       if(2<<i >=length){
         colsize++;
         break;
       }
       i++;
       colsize++;
     }
    return colsize;
  }


}
