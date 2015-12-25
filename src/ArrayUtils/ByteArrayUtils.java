package ArrayUtils;

public class ByteArrayUtils {
	  public static byte[] Concat(byte[] buffer1, byte[] buffer2)
      {
		  byte[] concatedArray = new byte[buffer1.length + buffer2.length];
		  System.arraycopy(buffer1, 0, concatedArray, 0, buffer1.length);
          System.arraycopy(buffer2, 0, concatedArray, buffer1.length, buffer2.length);
          return concatedArray;
          
      }

      public static byte[] XOR(byte[] buffer1, byte[] buffer2)
      {
    	  byte[] bigBuffer = buffer1.length < buffer2.length ? buffer2 : buffer1;
    	  byte[] smallBuffer = buffer1.length < buffer2.length ? buffer1 : buffer2;
    	  byte[] xoredBuffer = new byte[bigBuffer.length];
          for (int i = 0; i < smallBuffer.length; i++)
          {
              xoredBuffer[i] = (byte)(bigBuffer[i] ^ smallBuffer[i]);
          }

          int leftBytesStartIndex = smallBuffer.length;
          int leftBytesLength = bigBuffer.length - smallBuffer.length;
          System.arraycopy(bigBuffer, leftBytesStartIndex, xoredBuffer, smallBuffer.length, leftBytesLength);

          return xoredBuffer;
      }

      public static byte[] Pad(byte[] buffer, byte paddingByte, int paddingLength)
      {
          return Concat(buffer, CreatePaddingBuffer(paddingByte, paddingLength));
      }

      public static byte[] CreatePaddingBuffer(byte paddingByte, int paddingLength)
      {
    	  byte[] pading = new byte[paddingLength];
    	  for(int i = 0; i < pading.length; i++) {
    		  pading[i] = paddingByte;
    	  }
    	  
          return pading;
      }

    //  public static byte[] Chunk(Iterable<Byte> source, int chunkSize)
     // {
      //    while (source.Aany())
       //   {
        //      yield return source.Take(chunkSize).ToArray();
         //     source = source.Skip(chunkSize);
          //}
      //}
}
