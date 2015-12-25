package HMAC;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import SHA1.SHA1Algo;

public class HmacAlgo {

    private final byte INNER_PAD = 0x36;
    private final byte OUTER_PAD = 0x5c;
    static final int SHA1_BLOCK_SIZE = 64;
    static final String Compute = "Compute";
    static final String Verify = "Verify";
    
    private SHA1Algo _SHA1HashAlgo;

    private byte[] keyArray;
    private byte[] ipad;
    private byte[] opad;

    public void HMACAlgorithm(String key)
    {
        _SHA1HashAlgo = new SHA1Algo();

        keyArray = HandleKey(GetASCIIBytes(key)); // Step 1-3
        // GET INNER AND OUTER PAD
	        ipad = getPad(keyArray, INNER_PAD);
	        opad = getPad(keyArray, OUTER_PAD);
		
     //   ipad = keyArray.XOR(ByteArrayUtils.CreatePaddingBuffer(INNER_PAD, SHA1_BLOCK_SIZE)); // Step 4
      //  opad = keyArray.XOR(ByteArrayUtils.CreatePaddingBuffer(OUTER_PAD, SHA1_BLOCK_SIZE)); // Step 7
    }

    private static byte[] getPad(byte[] keyArray, byte pad){
    	byte[] resultPad = new byte[SHA1_BLOCK_SIZE];
		for (int j = 0; j < resultPad.length; j++) {
			resultPad[j] = (byte) (((int) keyArray[j]) ^ ((int) pad));
		}
    	return resultPad;
    }
    
    private static byte[] GetASCIIBytes(String str)
    {
    	byte[] text = str.getBytes(StandardCharsets.US_ASCII);
        return text;
    }

    public byte[] Compute(String message)
    {
        byte[] textBuffer = GetASCIIBytes(message);

        byte[] keyXorInnerPadAndText = new byte[ipad.length + textBuffer.length];
        System.arraycopy(ipad, 0, keyXorInnerPadAndText, 0, ipad.length);
        System.arraycopy(textBuffer, 0, keyXorInnerPadAndText, ipad.length, textBuffer.length);
        
  //      byte[]  keyXorInnerPadAndText = ipad.Concat(textBuffer); // Step 5

        byte[] keyXorInnerPadAndTextHahed = Hash(keyXorInnerPadAndText); // Step 6

        byte[] keyXorOuterPadAndKeyXorInnerpadAndTextHahed = new byte[opad.length + keyXorInnerPadAndTextHahed.length];
        System.arraycopy(opad, 0, keyXorOuterPadAndKeyXorInnerpadAndTextHahed, 0, opad.length);
        System.arraycopy(keyXorInnerPadAndTextHahed, 0, keyXorOuterPadAndKeyXorInnerpadAndTextHahed, opad.length, keyXorInnerPadAndTextHahed.length);
        
        //byte[] keyXorOuterPadAndKeyXorInnerpadAndTextHahed = opad.Concat(keyXorInnerPadAndTextHahed); // Step 8

        byte[] mac = Hash(keyXorOuterPadAndKeyXorInnerpadAndTextHahed); // Step 9

        return mac;
    }

    public Boolean Verifty(String message, byte[] macToVerify)
    {
    	return Arrays.equals(macToVerify, Compute(message));
        // return macToVerify.SequenceEqual(Compute(message));
    }
    
    private byte[] HandleKey(byte[] inputKeyBuffer)
    {
        byte[] keyBuffer = null;

        if (inputKeyBuffer.length == SHA1_BLOCK_SIZE) // Step 1
        {
            keyBuffer = inputKeyBuffer;
        }
        else if (inputKeyBuffer.length > SHA1_BLOCK_SIZE) // Step 2
        {
        	byte[] hashedKeyBuffer = Hash(inputKeyBuffer);
        	keyBuffer = new byte[20];
        	keyBuffer = hashedKeyBuffer;
        
        	//   keyBuffer = hashedKeyBuffer.Pad(0, SHA1_BLOCK_SIZE - hashedKeyBuffer.length);
        }
        else if (inputKeyBuffer.length < SHA1_BLOCK_SIZE) // Step 3
        {      	
    			byte[] tmpKey = new byte[SHA1_BLOCK_SIZE];
    			Arrays.fill(tmpKey, (byte) 0);
    			System.arraycopy(inputKeyBuffer, 0, tmpKey, 0, inputKeyBuffer.length);
    			keyBuffer = new byte[SHA1_BLOCK_SIZE];
    			keyBuffer = tmpKey;
           // keyBuffer = inputKeyBuffer.Pad(0, SHA1_BLOCK_SIZE - inputKeyBuffer.length);
        }

        return keyBuffer;
    }

    private byte[] Hash(byte[] bufferToHash)
    {
        return _SHA1HashAlgo.ComputeHash(bufferToHash);
    }
}
