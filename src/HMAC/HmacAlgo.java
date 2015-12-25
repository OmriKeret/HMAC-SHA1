package HMAC;

public class HmacAlgo {


    private const byte INNER_PAD = 0x36;
    private const byte OUTER_PAD = 0x5c;
    private const int SHA1_BLOCK_SIZE = 64;

    private readonly SHA1Algorithm _SHA1HashAlgorithm;

    private byte[] _keyBuffer;
    private readonly byte[] _keyXorInnerPad;
    private readonly byte[] _keyXorOuterPad;

    public HMACAlgorithm(String key)
    {
        _SHA1HashAlgorithm = new SHA1Algorithm();

        _keyBuffer = HandleKey(GetASCIIBytes(key)); // Step 1-3
        _keyXorInnerPad = _keyBuffer.XOR(ByteArrayUtils.CreatePaddingBuffer(INNER_PAD, SHA1_BLOCK_SIZE)); // Step 4
        _keyXorOuterPad = _keyBuffer.XOR(ByteArrayUtils.CreatePaddingBuffer(OUTER_PAD, SHA1_BLOCK_SIZE)); // Step 7
    }

    private static byte[] GetASCIIBytes(string text)
    {
        return System.Text.Encoding.ASCII.GetBytes(text);
    }

    public byte[] Compute(string message)
    {
        byte[] textBuffer = GetASCIIBytes(message);

        var keyXorInnerPadAndText = _keyXorInnerPad.Concat(textBuffer); // Step 5

        var keyXorInnerPadAndTextHahed = Hash(keyXorInnerPadAndText); // Step 6

        var keyXorOuterPadAndKeyXorInnerpadAndTextHahed = _keyXorOuterPad.Concat(keyXorInnerPadAndTextHahed); // Step 8

        var mac = Hash(keyXorOuterPadAndKeyXorInnerpadAndTextHahed); // Step 9

        return mac;
    }

    public bool Verifty(string message, byte[] macToVerify)
    {
        return macToVerify.SequenceEqual(Compute(message));
    }
    
    private byte[] HandleKey(byte[] inputKeyBuffer)
    {
        byte[] keyBuffer = null;

        if (inputKeyBuffer.Length == SHA1_BLOCK_SIZE) // Step 1
        {
            keyBuffer = inputKeyBuffer;
        }
        else if (inputKeyBuffer.Length > SHA1_BLOCK_SIZE) // Step 2
        {
            var hashedKeyBuffer = Hash(inputKeyBuffer);
            keyBuffer = hashedKeyBuffer.Pad(0, SHA1_BLOCK_SIZE - hashedKeyBuffer.Length);
        }
        else if (inputKeyBuffer.Length < SHA1_BLOCK_SIZE) // Step 3
        {
            keyBuffer = inputKeyBuffer.Pad(0, SHA1_BLOCK_SIZE - inputKeyBuffer.Length);
        }

        return keyBuffer;
    }

    private byte[] Hash(byte[] bufferToHash)
    {
        return _SHA1HashAlgorithm.ComputeHash(bufferToHash);
    }
}
