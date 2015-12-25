package Tests;

public class AlgorithemTests {
	 [TestMethod]
		        public void TestHMACAlgorithm()
		        {
		            const string keyString = "12345678910111213141516171819202122232425262728293031323334353637383940";
		            const string mesage = "The quick brown fox jumps over the lazy dog";

		            byte[] keyBytes = Encoding.ASCII.GetBytes(keyString);
		            byte[] textBytes = Encoding.ASCII.GetBytes(mesage);

		            var hmac = new HMACSHA1(keyBytes);
		            byte[] mac1 = hmac.ComputeHash(textBytes);

		            var hmacAlgorithm = new HMACAlgorithm(keyString);
		            byte[] mac2 = hmacAlgorithm.Compute(mesage);
w
		            Assert.IsTrue(mac1.SequenceEqual(mac2));
		            Assert.IsTrue(hmacAlgorithm.Verifty(mesage, mac1));
		            Assert.IsTrue(hmacAlgorithm.Verifty(mesage, mac2));
		        }

		        [TestMethod]
		        public void TestSHA1Algorithm()
		        {
		            const string mesage = "The quick brown fox jumps over the lazy dog";

		            byte[] messageBytes = Encoding.ASCII.GetBytes(mesage);

		            var hash1 = new SHA1Managed().ComputeHash(messageBytes);
		            var hash2 = new SHA1Algorithm().ComputeHash(messageBytes);

		            Assert.IsTrue(hash1.SequenceEqual(hash2));
		        }
}
