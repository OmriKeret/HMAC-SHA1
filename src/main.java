

public class main {

    private const string USAGE_MESSAGE = "USAGE: HMAC.Console.exe <input file> <digest file> <key file> <compute | verify>";

    public static void Main(string[] args)
    {
        try
        {
            if (args.Length != 4)
            {
                ShowUsageMessage();
            }
            else
            {
                var inputTextFilePath = args[0];
                var digestFilePath = args[1];
                var keyFilePath = args[2];
                var commandString = args[3];

                var hmacUtil = new HMACAlgorithm(ReadTextFromFile(keyFilePath));
                string message = ReadTextFromFile(inputTextFilePath);
                switch (ParseCommand(commandString))
                {
                    case HMACCommand.Compute:
                        WriteToDigestFile(digestFilePath, hmacUtil.Compute(message));
                        break;
                    case HMACCommand.Verify:
                        System.Console.WriteLine(hmacUtil.Verifty(message, ReadFromDigestFile(digestFilePath)) ? "ACCEPT" : "REJECT");
                        break;
                    default:
                        ShowUsageMessage();
                        break;
                }
            }
        }
        catch (Exception ex)
        {
            System.Console.WriteLine(ex.Message);
        }
    }

    private static void ShowUsageMessage()
    {
        System.Console.WriteLine(USAGE_MESSAGE);
    }

    private static string ReadTextFromFile(string filePath)
    {
        try
        {
            if (!File.Exists(filePath))
            {
                throw new ArgumentException("Could not read file: " + filePath);
            }

            return File.ReadAllText(filePath);
        }
        catch (Exception)
        {
            throw new ArgumentException("Could not read file: " + filePath);
        }
    }

    private static byte[] ReadFromDigestFile(string digestFilePath)
    {
        return Convert.FromBase64String(ReadTextFromFile(digestFilePath));
    }

    private static void WriteToDigestFile(string digestFilePath, byte[] mac)
    {
        File.WriteAllText(digestFilePath, Convert.ToBase64String(mac));
    }

    private static HMACCommand ParseCommand(string commandString)
    {
        if (commandString.ToLower().Equals("compute"))
        {
            return HMACCommand.Compute;
        }

        if (commandString.ToLower().Equals("verify"))
        {
            return HMACCommand.Verify;
        }

        throw new ArgumentException("Command " + commandString + " is not supported.");
    }
}
