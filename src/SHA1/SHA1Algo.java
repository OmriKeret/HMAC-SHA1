package SHA1;

import ArrayUtils.ByteArrayUtils;

public class SHA1Algo {

    private final int BLOCK_SIZE_IN_BYTES = 64;
    private final int LONG_SIZE_IN_BYTES = 8;
    private final int BYTE_SIZE = 8;
    
    public byte[] ComputeHash(byte[] message)
    {
        int[] resultsArray = new int[] { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 };

        byte[] paddedMsg = PrePocessing(message);
        byte[] chunk = new byte[BLOCK_SIZE_IN_BYTES];
        byte[] finalResult = new byte[0];
        byte[] temp;
 
        
        if (paddedMsg.length % 64 != 0) {
            System.out.println("Invalid padded data length.");
            System.exit(0);
        }

        int passesReq = paddedMsg.length / 64;

        for (int passCntr = 0; passCntr < passesReq; passCntr++) {
            System.arraycopy(paddedMsg, BLOCK_SIZE_IN_BYTES * passCntr, chunk, 0, BLOCK_SIZE_IN_BYTES);
            resultsArray = HandleChunk(resultsArray, chunk);
        }
        
        for (int num : resultsArray) {
        	temp = intToByteArray(num);
        	finalResult = ByteArrayUtils.Concat(finalResult, temp);
        }
        return finalResult;
       // return (LeftRotate(resultsArray[0], 128)) | (LeftRotate(resultsArray[1], 96)) | (LeftRotate(resultsArray[2], 64)) | (LeftRotate(resultsArray[3], 32)) | resultsArray[4];
    }

    private byte[] PrePocessing(byte[] message)
    {
//        long messageLenght = message.length;
//        long messageLenghtInBits = messageLenght * BYTE_SIZE;
//        int numOfZeroToAppend = (BLOCK_SIZE_IN_BYTES - LONG_SIZE_IN_BYTES) - ((int)(messageLenght + 1) % BLOCK_SIZE_IN_BYTES);
//        
//        return message.Concat(new byte[] { 0x80 })
//                      .Pad(0, numOfZeroToAppend)
//                      .Concat(EndianUtils.ConvertLongToByteArrayChangeEndian(messageLenghtInBits));
    	int origLength = message.length;
        int tailLength = origLength % 64;
        int padLength = 0;
        if ((64 - tailLength >= 9)) {
            padLength = 64 - tailLength;
        } else {
            padLength = 128 - tailLength;
        }

        byte[] thePad = new byte[padLength];
        thePad[0] = (byte) 0x80;
        long lengthInBits = origLength * 8;

        for (int cnt = 0; cnt < 8; cnt++) {
            thePad[thePad.length - 1 - cnt] = (byte) ((lengthInBits >> (8 * cnt)) & 0x00000000000000FF);
        }

        byte[] output = new byte[origLength + padLength];

        System.arraycopy(message, 0, output, 0, origLength);
        System.arraycopy(thePad, 0, output, origLength, thePad.length);

        return output;
    }

    private int[] HandleChunk(int[] currentResults, byte[] chunk)
    {
    	int temp;
 	    int A, B, C, D, E;
        int[] K = {0x5A827999, 0x6ED9EBA1, 0x8F1BBCDC, 0xCA62C1D6};
 	    int F;
    	int[] W = new int[80];
        for (int outer = 0; outer < 16; outer++) {
            temp = 0;
            for (int inner = 0; inner < 4; inner++) {
                temp = (chunk[outer * 4 + inner] & 0x000000FF) << (24 - inner * 8);
                W[outer] = W[outer] | temp;
            }
        }

        for (int j = 16; j < 80; j++) {
            W[j] = rotateLeft(W[j - 3] ^ W[j - 8] ^ W[j - 14] ^ W[j - 16], 1);
        }

        A = currentResults[0];
        B = currentResults[1];
        C = currentResults[2];
        D = currentResults[3];
        E = currentResults[4];

        for (int j = 0; j < 80; j++) {
        	if ( j < 20) {
	            F = (B & C) | ((~B) & D);
	            //	K = 0x5A827999;
	            temp = rotateLeft(A, 5) + F + E + K[0] + W[j];
	     
	        } else if ( j < 40) {
	            F = B ^ C ^ D;
	            //   K = 0x6ED9EBA1;
	            temp = rotateLeft(A, 5) + F + E + K[1] + W[j];
	  
	        } else if ( j < 60) {
	            F = (B & C) | (B & D) | (C & D);
	            //   K = 0x8F1BBCDC;
	            temp = rotateLeft(A, 5) + F + E + K[2] + W[j];
	  
	        } else {
	            F = B ^ C ^ D;
	            //   K = 0xCA62C1D6;
	            temp = rotateLeft(A, 5) + F + E + K[3] + W[j];
	        }
        	
            E = D;
            D = C;
            C = rotateLeft(B, 30);
            B = A;
            A = temp;
        }

        currentResults[0] += A;
        currentResults[1] += B;
        currentResults[2] += C;
        currentResults[3] += D;
        currentResults[4] += E;
    	
    	
    	
//        currentResults[0] += a;
//        currentResults[1] += b;
//        currentResults[2] += c;
//        currentResults[3] += d;
//        currentResults[4] += e;

        return currentResults;
    }

    private int[] ExtendedChunkAndConvertToUintArray(byte[] chunk)
    {
        //int[] extendedBitArray = new int[80];

        int[] extendedBitArray = new int[80];
        
        for (int outer = 0; outer < 16; outer++) {
            int temp = 0;
            for (int inner = 0; inner < 4; inner++) {
                temp = (chunk[outer * 4 + inner] & 0x000000FF) << (24 - inner * 8);
                extendedBitArray[outer] = extendedBitArray[outer] | temp;
            }
        }
        
        for (int j = 16; j < 80; j++) {
        	extendedBitArray[j] = LeftRotate(extendedBitArray[j - 3] ^ extendedBitArray[j - 8] ^ extendedBitArray[j - 14] ^ extendedBitArray[j - 16], 1);
        }

        return extendedBitArray;
    }

    private int LeftRotate(int word, int numOfBits)
    {
         int q = (word << numOfBits | word >> (32 - numOfBits));
         return q;
    }
    
    
    
    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    
    static final String toHexString(int x) {
        return padStr(Integer.toHexString(x));
    }
    static final String ZEROS = "00000000";

    static final String padStr(String s) {
        if (s.length() > 8) {
            return s.substring(s.length() - 8);
        }
        return ZEROS.substring(s.length()) + s;
    }
    final int rotateLeft(int value, int bits) {
    	 return (value << bits) | (value >>> (32 - bits));
        
    }
}
