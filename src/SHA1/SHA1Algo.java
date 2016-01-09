package SHA1;

import ArrayUtils.ByteArrayUtilsFunctions;

public class SHA1Algo {

    private final int BLOCK_SIZE_IN_BYTES = 64;
    
    public byte[] ComputeHash(byte[] message)
    {
        int[] resultsArray = new int[] { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 };

        byte[] paddedMsg = PrePocessing(message);
        byte[] chunk = new byte[BLOCK_SIZE_IN_BYTES];
        byte[] result = new byte[0];
        byte[] temp;
 
        
        if (paddedMsg.length % 64 != 0) {
            System.out.println("Invalid padded data length.");
            System.exit(0);
        }

        int repeatNum = paddedMsg.length / 64;

        for (int i = 0; i < repeatNum; i++) {
            System.arraycopy(paddedMsg, BLOCK_SIZE_IN_BYTES * i, chunk, 0, BLOCK_SIZE_IN_BYTES);
            resultsArray = HandleChunk(resultsArray, chunk);
        }
        
        for (int num : resultsArray) {
        	temp = intToByteArray(num);
        	result = ByteArrayUtilsFunctions.Concat(result, temp);
        }
        return result;
    }

    private byte[] PrePocessing(byte[] message)
    {
    	int originLength = message.length;
        int tailLength = originLength % 64;
        int padLength = 0;
        if (64 - tailLength >= 9) {
            padLength = 64 - tailLength;
        } else {
            padLength = 128 - tailLength;
        }

        byte[] resultPad = new byte[padLength];
        resultPad[0] = (byte) 0x80;
        long lengthInBits = originLength * 8;

        for (int i = 0; i < 8; i++) {
            resultPad[resultPad.length - 1 - i] = (byte) ((lengthInBits >> (8 * i)) & 0x00000000000000FF);
        }

        byte[] result = new byte[originLength + padLength];

        System.arraycopy(message, 0, result, 0, originLength);
        System.arraycopy(resultPad, 0, result, originLength, resultPad.length);

        return result;
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
            W[j] = leftRotate(W[j - 3] ^ W[j - 8] ^ W[j - 14] ^ W[j - 16], 1);
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
	            temp = leftRotate(A, 5) + F + E + K[0] + W[j];
	     
	        } else if ( j < 40) {
	            F = B ^ C ^ D;
	            //   K = 0x6ED9EBA1;
	            temp = leftRotate(A, 5) + F + E + K[1] + W[j];
	  
	        } else if ( j < 60) {
	            F = (B & C) | (B & D) | (C & D);
	            //   K = 0x8F1BBCDC;
	            temp = leftRotate(A, 5) + F + E + K[2] + W[j];
	  
	        } else {
	            F = B ^ C ^ D;
	            //   K = 0xCA62C1D6;
	            temp = leftRotate(A, 5) + F + E + K[3] + W[j];
	        }
        	
            E = D;
            D = C;
            C = leftRotate(B, 30);
            B = A;
            A = temp;
        }

        currentResults[0] += A;
        currentResults[1] += B;
        currentResults[2] += C;
        currentResults[3] += D;
        currentResults[4] += E;

        return currentResults;
    }

    
    
    public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    
    final int leftRotate(int value, int bits) {
    	 return (value << bits) | (value >>> (32 - bits));
        
    }
}
