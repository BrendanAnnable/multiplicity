package multiplicity.network.xmpp.filedistribution.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import multiplicity.networkbase.model.MD5Hash;

public class CryptoUtils {
	public static MD5Hash md5(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(),0,s.length());
		BigInteger i = new BigInteger(1,m.digest());
		return new MD5Hash(String.format("%1$032X", i));
	}

	public static MD5Hash md5(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		InputStream is = new FileInputStream(file);

		is = new DigestInputStream(is, md);
		byte[] buf = new byte[2048];
		@SuppressWarnings("unused")
		int read;
		while((read = is.read(buf))!= -1) {
			//
		}

		byte[] digest = md.digest();
		BigInteger i = new BigInteger(1,digest);
		return new MD5Hash(String.format("%1$032X", i));
	}
}
