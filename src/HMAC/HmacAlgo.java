package HMAC;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import ArrayUtils.ByteArrayUtilsFunctions;
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

	public HmacAlgo (String key)
    {
        _SHA1HashAlgo = new SHA1Algo();

        keyArray = processKey(GetASCIIBytes(key)); // Step 1-3
        // GET INNER AND OUTER PAD
		
        ipad = ByteArrayUtilsFunctions.XOR(keyArray, ByteArrayUtilsFunctions.CreatePaddingBuffer(INNER_PAD, SHA1_BLOCK_SIZE)); // Step 4
        opad = ByteArrayUtilsFunctions.XOR(keyArray, ByteArrayUtilsFunctions.CreatePaddingBuffer(OUTER_PAD, SHA1_BLOCK_SIZE)); // Step 7
    }
    
    private static byte[] GetASCIIBytes(String str)
    {
    	
    	byte[] text = str.getBytes(StandardCharsets.US_ASCII);
        return text;
    }

    public byte[] Compute(String message)
    {
        byte[] messageBytes = GetASCIIBytes(message);
        
        byte[] keyXorInnerPadAndText = ByteArrayUtilsFunctions.Concat(ipad, messageBytes); // Step 5

        byte[] keyXorInnerPadAndTextHashed = Hash(keyXorInnerPadAndText); // Step 6
        
        byte[] keyXorOuterPadAndKeyXorInnerpadAndTextHashed =  ByteArrayUtilsFunctions.Concat(opad, keyXorInnerPadAndTextHashed);// Step 8

        byte[] mac = Hash(keyXorOuterPadAndKeyXorInnerpadAndTextHashed); // Step 9

        return mac;
    }

    public Boolean Verify(String message, byte[] macToVerify)
    {
    	return Arrays.equals(macToVerify, Compute(message));
    }
    
    /**
     * Process key 
     * @param inputKey
     * @return processed key
     */
    private byte[] processKey(byte[] inputKey)
    {
        byte[] key = null;

        if (inputKey.length == SHA1_BLOCK_SIZE) // Step 1
        {
            key = inputKey;
        }
        else if (inputKey.length > SHA1_BLOCK_SIZE) // Step 2
        {
        	byte[] hashedKey = Hash(inputKey);
        	key = new byte[20];
        	key = hashedKey;
        
        	key = ByteArrayUtilsFunctions.Pad(hashedKey,(byte)0, SHA1_BLOCK_SIZE - hashedKey.length);
        }
        else if (inputKey.length < SHA1_BLOCK_SIZE) // Step 3
        {      	
    			byte[] tmpKey = new byte[SHA1_BLOCK_SIZE];
    			Arrays.fill(tmpKey, (byte) 0);
    			System.arraycopy(inputKey, 0, tmpKey, 0, inputKey.length);
    			key = new byte[SHA1_BLOCK_SIZE];
    			key = tmpKey;
    			key = ByteArrayUtilsFunctions.Pad(inputKey, (byte)0, SHA1_BLOCK_SIZE - inputKey.length);
        }

        return key;
    }

    private byte[] Hash(byte[] bufferToHash)
    {
        return _SHA1HashAlgo.ComputeHash(bufferToHash);
    }
}
