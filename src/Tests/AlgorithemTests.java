package Tests;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import com.sun.crypto.provider.HmacSHA1;

import HMAC.HmacAlgo;
import SHA1.SHA1Algo;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class AlgorithemTests {
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
				@Test
		        public void TestHMACAlgorithm() throws InvalidKeyException, NoSuchAlgorithmException
		        {
		            final String keyString = "key";
		            final String mesage = "The quick brown fox jumps over the lazy dog";
		            String result;
		            byte[] keyBytes = keyString.getBytes(StandardCharsets.US_ASCII);
		            byte[] textBytes = mesage.getBytes(StandardCharsets.US_ASCII);

			         // get an hmac_sha1 key from the raw key bytes
			         SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);
	
			         // get an hmac_sha1 Mac instance and initialize with the signing key
			         Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			         mac.init(signingKey);
	
			         // compute the hmac on input data bytes
			         byte[] rawHmac = mac.doFinal(mesage.getBytes());
	
			         // base64-encode the hmac
			         result = Base64.getEncoder().encodeToString(rawHmac);

			         HmacAlgo hmacAlgorithm = new HmacAlgo(keyString);
		             byte[] mac2 = hmacAlgorithm.Compute(mesage);

		             assertEquals(bytesToHex(mac2), bytesToHex(rawHmac));

		        }

				@Test
		        public void TestSHA1Algorithm() throws NoSuchAlgorithmException
		        {
		            final String mesage = "The quick brown fox jumps over the lazy dog";

		            byte[] messageBytes = mesage.getBytes(StandardCharsets.US_ASCII); 
		            MessageDigest md = MessageDigest.getInstance("SHA1");
		            md.update(messageBytes);
		            byte[] hash1 = md.digest();
		            byte[] hash2 = new SHA1Algo().ComputeHash(messageBytes);
		            assertEquals(bytesToHex(hash1), bytesToHex(hash2));
		        }
				
				
				public static String bytesToHex(byte[] b) {
				      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
				                         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
				      StringBuffer buf = new StringBuffer();
				      for (int j=0; j<b.length; j++) {
				         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
				         buf.append(hexDigit[b[j] & 0x0f]);
				      }
				      return buf.toString();
				   }
}
