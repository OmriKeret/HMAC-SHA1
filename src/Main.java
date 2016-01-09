import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Base64;
import HMAC.HMACCommands;
import HMAC.HmacAlgo;

public class Main {

	private final static String USAGE_MESSAGE = "USAGE: HMAC.Console.exe <input file> <digest file> <key file> <compute | verify>";
	private final static String Compute = "compute";
	private final static String Verify = "verify";
	public static void main(String[] args) {
		{
			try
			{
				if (args.length != 4)
				{
					ShowUsageMessage();
				}
				else
				{
					String inputTextFilePath = args[0];
					String digestFilePath = args[1];
					String keyFilePath = args[2];
					String commandString = args[3];

					HmacAlgo hmacUtil = new HmacAlgo (ReadTextFromFile(keyFilePath));
					String message = ReadTextFromFile(inputTextFilePath);
					switch (ParseCommand(commandString))
					{
					case Compute:
						WriteToDigestFile(digestFilePath, hmacUtil.Compute(message));
						break;
					case Verify:
						if (hmacUtil.Verifty(message, ReadFromDigestFile(digestFilePath))) { 
							System.out.println("ACCEPT");
						} else { 
							System.out.println("REJECT");
						}
						break;
					default:
						ShowUsageMessage();
						break;
					}
				}
			}
			catch (Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
	}

	private static void ShowUsageMessage()
	{
		System.out.println(USAGE_MESSAGE);
	}

	private static String ReadTextFromFile(String filePath) throws Exception
	{
		try
		{
			File f = new File(filePath);
			StringBuilder stringResult = new StringBuilder();
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = "";
			if (!f.exists())
			{
				throw new Exception("Could not read file: " + filePath);
			}
			while ((line = reader.readLine()) != null) {
				stringResult.append(line);
			}
			return stringResult.toString();
		}
		
		catch (Exception e)
		{
			throw new Exception("Could not read file: " + filePath);
		}
	}

	private static byte[] ReadFromDigestFile(String digestFilePath) throws Exception
	{
		return Base64.getDecoder().decode(ReadTextFromFile(digestFilePath));
	}

	private static void WriteToDigestFile(String digestFilePath, byte[] mac) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter(digestFilePath);
		out.print(Base64.getEncoder().encodeToString(mac));
		out.flush();
		// File.WriteAllText(digestFilePath, Convert.ToBase64String(mac));
	}

	private static String ParseCommand(String commandString) throws Exception
	{
		if (commandString.toLowerCase().equals("compute"))
		{
			return Compute;
		}

		if (commandString.toLowerCase().equals("verify"))
		{
			return Verify;
		}

		throw new Exception("Command " + commandString + " is not supported.");
	}
}
