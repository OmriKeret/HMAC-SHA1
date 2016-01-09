package ArrayUtils;

public class ByteArrayUtilsFunctions {
	  public static byte[] Concat(byte[] buffer1, byte[] buffer2)
      {
		  byte[] concatedArray = new byte[buffer1.length + buffer2.length];
		  System.arraycopy(buffer1, 0, concatedArray, 0, buffer1.length);
          System.arraycopy(buffer2, 0, concatedArray, buffer1.length, buffer2.length);
          return concatedArray;
          
      }

	  /**
	   * Xors 2 byte arrays.
	   * @param a
	   * @param b
	   * @return
	   */
	  public static byte[] XOR(byte[] a, byte[] b) {
		  byte[] result = new byte[Math.min(a.length, b.length)];

		  for (int i = 0; i < result.length; i++) {
		    result[i] = (byte) (((int) a[i]) ^ ((int) b[i]));
		  }

		  return result;
		}

	  /**
	   * Add padding to a byte array.
	   * @param buffer
	   * @param paddingByte
	   * @param paddingLength
	   * @return
	   */
      public static byte[] Pad(byte[] buffer, byte paddingByte, int paddingLength)
      {
          return Concat(buffer, CreatePaddingBuffer(paddingByte, paddingLength));
      }

      /**
       * Create a padding buffer.
       * @param paddingByte
       * @param paddingLength
       * @return
       */
      public static byte[] CreatePaddingBuffer(byte paddingByte, int paddingLength)
      {
    	  byte[] pading = new byte[paddingLength];
    	  for(int i = 0; i < pading.length; i++) {
    		  pading[i] = paddingByte;
    	  }
    	  
          return pading;
      }
}
