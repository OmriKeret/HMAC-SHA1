package ArrayUtils;

public class ByteArrayUtils {
	  public static byte[] Concat(this byte[] buffer1, byte[] buffer2)
      {
          return Enumerable.Concat(buffer1, buffer2).ToArray();
      }

      public static byte[] XOR(this byte[] buffer1, byte[] buffer2)
      {
          var bigBuffer = buffer1.Length < buffer2.Length ? buffer2 : buffer1;
          var smallBuffer = buffer1.Length < buffer2.Length ? buffer1 : buffer2;
          var xoredBuffer = new byte[bigBuffer.Length];
          for (int i = 0; i < smallBuffer.Length; i++)
          {
              xoredBuffer[i] = (byte)(bigBuffer[i] ^ smallBuffer[i]);
          }

          int leftBytesStartIndex = smallBuffer.Length;
          int leftBytesLength = bigBuffer.Length - smallBuffer.Length;
          Array.Copy(bigBuffer, leftBytesStartIndex, xoredBuffer, smallBuffer.Length, leftBytesLength);

          return xoredBuffer;
      }

      public static byte[] Pad(this byte[] buffer, byte paddingByte, int paddingLength)
      {
          return buffer.Concat(CreatePaddingBuffer(paddingByte, paddingLength));
      }

      public static byte[] CreatePaddingBuffer(byte paddingByte, int paddingLength)
      {
          return Enumerable.Repeat(paddingByte, paddingLength).ToArray();
      }

      public static IEnumerable<byte[]> Chunk(this IEnumerable<byte> source, int chunkSize)
      {
          while (source.Any())
          {
              yield return source.Take(chunkSize).ToArray();
              source = source.Skip(chunkSize);
          }
      }
}
