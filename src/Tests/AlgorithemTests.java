package Tests;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import com.sun.crypto.provider.HmacSHA1;

import HMAC.HmacAlgo;

import java.security.InvalidKeyException;
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
		            final String keyString = "12345678910111213141516171819202122232425262728293031323334353637383940";
		            final String mesage = "The quick brown fox jumps over the lazy dog";
		            String result;
		            byte[] keyBytes = keyString.getBytes(StandardCharsets.US_ASCII);
		            byte[] textBytes = mesage.getBytes(StandardCharsets.US_ASCII);
		            
		            //HmacSHA1 hmac = new HmacSHA1();

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

		             System.out.println("Our compute: ");
		             System.out.println(mac2);
		             System.out.println("----------------------------------------");
		             System.out.println("Theirs compute: ");
		             System.out.println(rawHmac);
		             assertEquals(mac2, rawHmac);
//		            Assert.IsTrue(mac1.SequenceEqual(mac2));
//		            Assert.IsTrue(hmacAlgorithm.Verifty(mesage, mac1));
//		            Assert.IsTrue(hmacAlgorithm.Verifty(mesage, mac2));
		        }

		        
		        public void TestSHA1Algorithm()
		        {
//		            const string mesage = "The quick brown fox jumps over the lazy dog";
//
//		            byte[] messageBytes = Encoding.ASCII.GetBytes(mesage);
//
//		            var hash1 = new SHA1Managed().ComputeHash(messageBytes);
//		            var hash2 = new SHA1Algorithm().ComputeHash(messageBytes);
//
//		            Assert.IsTrue(hash1.SequenceEqual(hash2));
		        }
}
