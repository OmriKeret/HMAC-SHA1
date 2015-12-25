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
        int i = 0;
        
        if (paddedMsg.length % 64 != 0) {
            System.out.println("Invalid padded data length.");
            System.exit(0);
        }

        int passesReq = paddedMsg.length / 64;

        for (int passCntr = 0; passCntr < passesReq; passCntr++) {
            System.arraycopy(paddedMsg, 64 * passCntr, chunk, 0, 64);
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
    	int[] extendedArray = ExtendedChunkAndConvertToUintArray(chunk);

    	int a = currentResults[0];
    	int b = currentResults[1];
    	int c = currentResults[2];
    	int d = currentResults[3];
    	int e = currentResults[4];

        for (int i = 0; i <= 79; i++)
        {
        	int f;
        	int k;
            if (0 <= i && i <= 19)
            {
                f = (b & c) | ((~b) & d);
                k = 0x5A827999;
            }
            else if (20 <= i && i <= 39)
            {
                f = b ^ c ^ d;
                k = 0x6ED9EBA1;
            }
            else if (40 <= i && i <= 59)
            {
                f = (b & c) | (b & d) | (c & d);
                k = 0x8F1BBCDC;
            }
            else
            {
                f = b ^ c ^ d;
                k = 0xCA62C1D6;
            }

            int temp = LeftRotate(a, 5) + f + e + k + extendedArray[i];
            e = d;
            d = c;
            c = LeftRotate(b, 30);
            b = a;
            a = temp;
        }

        currentResults[0] += a;
        currentResults[1] += b;
        currentResults[2] += c;
        currentResults[3] += d;
        currentResults[4] += e;

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
        return word << numOfBits | word >> (32 - numOfBits);
    }
    
    
    // utils
    private String intArrayToHexStr(int[] data) {
        String output = "";
        String tempStr = "";
        int tempInt = 0;
        for (int cnt = 0; cnt < data.length; cnt++) {

            tempInt = data[cnt];

            tempStr = Integer.toHexString(tempInt);

            if (tempStr.length() == 1) {
                tempStr = "0000000" + tempStr;
            } else if (tempStr.length() == 2) {
                tempStr = "000000" + tempStr;
            } else if (tempStr.length() == 3) {
                tempStr = "00000" + tempStr;
            } else if (tempStr.length() == 4) {
                tempStr = "0000" + tempStr;
            } else if (tempStr.length() == 5) {
                tempStr = "000" + tempStr;
            } else if (tempStr.length() == 6) {
                tempStr = "00" + tempStr;
            } else if (tempStr.length() == 7) {
                tempStr = "0" + tempStr;
            }
            output = output + tempStr;
        }//end for loop
        return output;
    }//end intArrayToHexStr
    //-------------------------------------------//

//    static final String toHexString( ByteBuffer bb) {
//        final StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < bb.limit(); i += 4) {
//            if (i % 4 == 0) {
//                sb.append('\n');
//            }
//            sb.append(toHexString(bb.getInt(i))).append(' ');
//        }
//        sb.append('\n');
//        return sb.toString();
//    }
    
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
}
