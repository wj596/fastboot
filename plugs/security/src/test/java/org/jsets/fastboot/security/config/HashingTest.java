package org.jsets.fastboot.security.config;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashingTest {
	
	public static void main(String[] args) {
		String s = "123456";
		
		String r1 = Hashing.md5().newHasher().putString(s, Charsets.UTF_8).hash().toString();
		String r2 = Hashing.sha256().newHasher().putString(s, Charsets.UTF_8).hash().toString();
		String r3 = Hashing.sha256().newHasher().putString("ssssssssssssssssefaewfaewfafa", Charsets.UTF_8).hash().toString();
		System.out.println(r1.length());
		System.out.println(r2);
		System.out.println(r3.length());
	}
}
