package SHA1;

public class SHA1Algo {

    private const int BLOCK_SIZE_IN_BYTES = 64;
    private const int LONG_SIZE_IN_BYTES = 8;
    private const int BYTE_SIZE = 8;
    
    public byte[] ComputeHash(byte[] message)
    {
        var resultsArray = new uint[] { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0 };

        return PrePocessing(message).Chunk(BLOCK_SIZE_IN_BYTES)
                                    .Aggregate(resultsArray, HandleChunk)
                                    .SelectMany(EndianUtils.ConvertUintToByteArrayChangeEndian)
                                    .ToArray();
    }

    private IEnumerable<byte> PrePocessing(byte[] message)
    {
        long messageLenght = message.LongLength;
        long messageLenghtInBits = messageLenght * BYTE_SIZE;
        int numOfZeroToAppend = (BLOCK_SIZE_IN_BYTES - LONG_SIZE_IN_BYTES) - ((int)(messageLenght + 1) % BLOCK_SIZE_IN_BYTES);

        return message.Concat(new byte[] { 0x80 })
                      .Pad(0, numOfZeroToAppend)
                      .Concat(EndianUtils.ConvertLongToByteArrayChangeEndian(messageLenghtInBits));
    }

    private uint[] HandleChunk(uint[] currentResults, byte[] chunk)
    {
        uint[] extendedArray = ExtendedChunkAndConvertToUintArray(chunk);

        uint a = currentResults[0];
        uint b = currentResults[1];
        uint c = currentResults[2];
        uint d = currentResults[3];
        uint e = currentResults[4];

        for (int i = 0; i <= 79; i++)
        {
            uint f;
            uint k;
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

            uint temp = LeftRotate(a, 5) + f + e + k + extendedArray[i];
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

    private uint[] ExtendedChunkAndConvertToUintArray(IEnumerable<byte> chunk)
    {
        var extendedBitArray = new uint[80];
        var chunkAsBigEndianUintArray = chunk.Chunk(4)
                                             .Select(EndianUtils.ConvertByteArrayToUintChangeEndian)
                                             .ToArray();

        Array.Copy(chunkAsBigEndianUintArray, extendedBitArray, chunkAsBigEndianUintArray.Length);

        for (int i = 16; i < extendedBitArray.Length; i++)
        {
            extendedBitArray[i] = LeftRotate(extendedBitArray[i - 3] ^ extendedBitArray[i - 8] ^ extendedBitArray[i - 14] ^ extendedBitArray[i - 16], 1);
        }

        return extendedBitArray;
    }

    private uint LeftRotate(uint word, int numOfBits)
    {
        return word << numOfBits | word >> (32 - numOfBits);
    }
}
