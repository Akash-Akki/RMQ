/**
 * @author  Akash P Akki      netid: apa190001
 * @author  Anant Srivastava  netid: aps180006
 */
package aps180006;



public class Rmq {

  Timer timer = new Timer();

  /**
   * This method takes a input and constructs a sparse tabel for the block minima array of given input array a
   * @param a input array a
   * @return sparse table
   */
  public int[][] preProcessing(int[] a) {
    double b = Math.log(a.length)/Math.log(2);
    int blockSize= (int) Math.ceil(b);
    int[] blockMinima = constructBlockMinima(a,blockSize);
    int colSize = findSizeOfSparseTable(blockMinima.length);
    int[][] sparse = constructSparseTable(blockMinima,colSize);
    return  sparse;
  }

  /**
   *  This method finds the minimum element between the two indices i and j by looking up sparse table
   * @param a given input array a
   * @param i given input index i
   * @param j given input index j
   * @param sparse sparse table of block minima array
   * @return minmum element between the given two indices i and j
   */
  public int findRmq(int[] a, int i ,int j,int[][] sparse) {
    int arrayLength = a.length;
    double b = Math.log(arrayLength)/Math.log(2);
    int blockSize= (int) Math.ceil(b);
    int min1 = Integer.MAX_VALUE;
    int min3 = Integer.MAX_VALUE;
    int min =0;
   if( i/blockSize == j/blockSize){
     min=traverseIBlock(i,a, blockSize, j);
   }
   else if(i % blockSize!=0 && (j % blockSize != blockSize-1 && j!=arrayLength)){
        min1 = traverseJBlock(j - j % blockSize,a,j);
        Math.min(traverseIBlock(i,a,blockSize,j), traverseJBlock(j - j % blockSize,a,j));
     if((j/blockSize - i/blockSize ) >1 )
        min3 = query(sparse,(i/blockSize)+1,Math.max((j/blockSize -1),0));
     min = Math.min(min3 ,min1);

    }
    else if(i % blockSize !=0 && (j%blockSize == blockSize-1 || j==arrayLength)){
      min1 = traverseIBlock(i,a,blockSize,j);
      min3 = query(sparse,i/blockSize+1 ,j/blockSize);
      min= Math.min(min1,min3);
    }
    else if( i % blockSize == 0 && (j % blockSize != blockSize-1 && j!=arrayLength)){
      min1 = traverseJBlock(j - j%blockSize,a,j);
       min3 = query(sparse,(i/blockSize),Math.max((j/blockSize -1),0));
      min= Math.min(min1,min3);
    }
    else {
       min = query(sparse, i / blockSize, j / blockSize);
    }
    return min;
  }

  private int traverseJBlock(int l, int[] a, int j) {
    int min =Integer.MAX_VALUE;
    for(l=l;l<=j;l++){
      min = Math.min( min,a[l]);
    }
    return min;
  }

  /**
   * This method traverses the array from initial index i and return the minimum within that block
   * @param i given index i
   * @param a input array a
   * @param blockSize the block size of given array a
   * @param j given index j
   * @return
   */
  private int traverseIBlock(int i, int[] a ,int blockSize,int j) {
    int min = Integer.MAX_VALUE;
     int blockNumber = (i/blockSize)+1;
     int len = blockNumber*blockSize;
    for(i=i ;i<len && i<= j ; i++){
      min = Math.min(a[i],min);

    }
    return min;
  }

  /**
   * This method is used to query the sparse table between the given two indices and return the minimum
   * @param sparse constructed sparse table of block minima array of given array a
   * @param i given index i
   * @param j given index j
   * @return returns the minimum using the given block minima
   */
  private int query(int[][] sparse, int i, int j) {
      int k = findLargestK(i,j);
      int[][] ranges = {{i, (int) (i+Math.pow(2,k)-1)},{(int) (j - (Math.pow(2,k))+1),j}};
      int min=findMin(sparse,ranges);
      return min;


  }

  /**
   * This method makes the constant lookup of the given two ranges and returns the minimum element
   * @param sparse constructed sparse table of block minima
   * @param ranges given ranges of the sparse table
   * @return return the minimum using the sparse table
   */
  private int findMin(int[][] sparse, int[][] ranges) {
    int min =Integer.MAX_VALUE;
    int i1 = ranges[0][0];
    int i2 = ranges[1][0];
    int diffRange1 = ranges[0][1] - i1 + 1;
    int j1 = (int) (Math.log(diffRange1)/Math.log(2));
    int diffRange2 = ranges[1][1] - i2 + 1;
    int j2 = (int)Math.log((diffRange2)/Math.log(2));
    return Math.min(sparse[i1][j1],sparse[i2][j2]);
  }

  /**
   * This method calculates k such the 2^k <= j-i+1
   * @param i given index i
   * @param j given index j
   * @return return k
   */
  private int findLargestK(int i, int j) {
    int k=0;
     while(Math.pow(2,k) <= j-i+1){
       k++;
       if(Math.pow(2,k) > j-i+1){
         k--;
         break;
       }
     }
    return k;
  }

  /**
   * This method constructs the block minima array using input array a and blocksize
   * @param a given array a
   * @param blockSize block size of the given array a
   * @return return the block minima array
   */
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

  /**
   * This method constructs the sparse table  of the block minima array
   * @param blockMinima constructed block minima array
   * @param colsize column size of the sparsetable
   * @return return sparse table of given block minima array
   */
  private int[][] constructSparseTable(int[] blockMinima,int colsize) {
      int rowSize = blockMinima.length;
      int[][] sparseTable = new int[rowSize][colsize];
      for(int i=0;i<rowSize;i++){
        sparseTable[i][0]=blockMinima[i];
      }
      for(int j=1 ; j < colsize;j++){
        for(int i=0; i < rowSize ; i++ ){
          if(  (i + Math.pow(2,j-1)) >= rowSize)
              break;
          sparseTable[i][j] = Math.min(sparseTable[i][j-1], sparseTable[(int) (i + Math.pow(2,j-1))][j-1]);
        }
      }
     return sparseTable;
  }

  /**
   * This method calculates the size of the column of the sparse table to be constructed.
   * @param length length of the constructed block Minima
   * @return return columnsize of the sparse table to be constructed
   */
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
