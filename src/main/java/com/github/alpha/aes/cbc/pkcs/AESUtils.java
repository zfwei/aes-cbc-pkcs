package com.github.alpha.aes.cbc.pkcs;

import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * AES/CBC/PKCS7Padding和AES/CBC/PKCS5Padding加密结果是一样的
 * 使用AES/CBC/PKCS7Padding，还要引入第三方包bcprov-jdk16-1.46.jar支持
 * @author zfwei
 *
 */
public class AESUtils {
	static String ivStr = "01020304050607080102030405060708";
	static String keyStr = "01020304050607080102030405060708";


	/**将十六进制字符串转换成byte[]*/
	public static byte[] revert(String hex){
		if ((hex == null) || (hex.length() == 0))
			return null;
	    if (hex.length() % 2 != 0)
	    		return null;
	    int i = 0; int j = 0;
	    int len = hex.length();
	    byte[] out = new byte[len / 2];
	    
	    while (i < len){
	    		i += 2;
	    		String s = hex.substring(i - 2, i);
	    		int intB = Integer.parseInt(s, 16);
	    		if (intB > 127)
	    			intB = -((intB ^ 0xFFFFFFFF) + 1);
	    		
	    		byte b = (byte)intB;
	    		out[(j++)] = b;
	    }
	    return out;
	}

	/**将byte[]转换成十六进制字符串*/
	public static String toHex(byte[] bs){
	    StringBuffer s = new StringBuffer(100);
	    String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	    
	    for (int i = 0; i < bs.length; ++i){
	    		int n = bs[i];
	    		if (n < 0)
	    			n = 256 + n;
	    		
	    		int d1 = n / 16;
	    		int d2 = n % 16;
	    		
	    		s.append(hexDigits[d1] + hexDigits[d2]);
	    }
	    return s.toString();
	}


	/** 
	 * 加密数据
	 * @param data 待加密数据
	 * @return byte[] 加密后的数据
	 * */  
	public static byte[] encrypt(byte[] data) throws Exception{
	    //密钥
		SecretKey k=new SecretKeySpec(revert(keyStr),"AES");
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	    Cipher cipher=Cipher.getInstance("AES/CBC/PKCS7Padding", new BouncyCastleProvider());
	    //初始化，设置为加密模式
	    cipher.init(Cipher.ENCRYPT_MODE, k,new IvParameterSpec(revert(ivStr)));
	    //执行操作
	    return cipher.doFinal(data);
	}
	
	/** 
	 * 加密数据
	 * @param data 待加密数据
	 * @param key key
	 * @param iv
	 * @return byte[] 加密后的数据
	 * */  
	public static byte[] encrypt(byte[] data, String key, String iv) throws Exception{
	    //密钥
		SecretKey k=new SecretKeySpec(revert(key),"AES");
		//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	    Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding"/*, new BouncyCastleProvider()*/);
	    //初始化，设置为加密模式
	    cipher.init(Cipher.ENCRYPT_MODE, k,new IvParameterSpec(revert(key)));
	    //执行操作
	    return cipher.doFinal(data);
	}
	
	public static String encryptWithBase64(String content, String key, String iv) throws Exception {
		String base64Str = Base64.getEncoder().encodeToString(content.getBytes("UTF-8"));
		return toHex(encrypt(base64Str.getBytes("UTF-8"), key, iv));
	}


	/** 
	 * 解密数据
	 * @param data 待解密数据
	 * @return byte[] 解密后的数据
	 * */
	    //密钥
	public static byte[] decrypt(byte[] data) throws Exception{
	    SecretKey k=new SecretKeySpec(revert(keyStr),"AES");
	    
	    Cipher cipher=Cipher.getInstance("AES/CBC/PKCS7Padding", new BouncyCastleProvider());
	    //初始化，设置为解密模式
	    cipher.init(Cipher.DECRYPT_MODE, k,new IvParameterSpec(revert(ivStr)));
	    //执行操作
	    return cipher.doFinal(data);
	}
	
	/** 
	 * 解密数据
	 * @param data 待解密数据
	 * @param key
	 * @param iv
	 * @return byte[] 解密后的数据
	 * */
	public static byte[] decrypt(byte[] data, String key, String iv) throws Exception{
	    //密钥
	    SecretKey k=new SecretKeySpec(revert(key),"AES");
	    
	    Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding"/*, new BouncyCastleProvider()*/);
	    //初始化，设置为解密模式
	    cipher.init(Cipher.DECRYPT_MODE, k,new IvParameterSpec(revert(iv)));
	    //执行操作
	    return cipher.doFinal(data);
	}
	
	public static String decryptWithBase64(String content, String key, String iv) throws Exception {
		String decryptStr = new String(decrypt(revert(content), key, iv));
		return new String(Base64.getDecoder().decode(decryptStr.getBytes("UTF-8")));
	}
	
	public static void main(String[] args) throws Exception {
		String context = "hello world";
		System.out.println("明文：" + context);
		String encryptText = toHex(encrypt(context.getBytes("UTF-8")));
		System.out.println(encryptText);
		System.out.println(new String(decrypt(revert(encryptText))));
		//key sha256迭代N次，取前32位
		//iv md5值
		//源数据->BASE64->加密
		//解密->BASE64->源数据
		String data ="12345";
		System.out.println("明文：" + data);
		String encryptStr = encryptWithBase64(data, keyStr, ivStr);
		System.out.println("加密后：" + encryptStr);
		System.out.println("解密后：" + decryptWithBase64(encryptStr, keyStr, ivStr));
	}
}
