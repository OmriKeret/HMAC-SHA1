package ArrayUtils;

public class EndianUtils {
	 public static uint ConvertByteArrayToUintChangeEndian(byte[] byteArray)
     {
         return BitConverter.ToUInt32(ChangeEndian(byteArray), 0);
     }

     public static byte[] ConvertUintToByteArrayChangeEndian(uint number)
     {
         return ChangeEndian(BitConverter.GetBytes(number));
     }

     public static byte[] ConvertLongToByteArrayChangeEndian(long number)
     {
         return ChangeEndian(BitConverter.GetBytes(number));
     }

     private static byte[] ChangeEndian(byte[] byteArray)
     {
         return byteArray.Reverse().ToArray();
     }
}
