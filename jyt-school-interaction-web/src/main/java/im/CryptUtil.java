package im;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yjtoon.framework.tooncode.ToonCode;
import com.yjtoon.framework.tooncode.ToonDesUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

/**
 * 加密算法工具类
 */
public class CryptUtil {
    /**
     * 密钥算法
     * java支持56位密钥，bouncycastle支持64位
     */
    public static final String KEY_ALGORITHM="DES";
    /**
     * 加密/解密算法/工作模式/填充方式
     */
    public static final String CIPHER_ALGORITHM="DES/ECB/PKCS5Padding";
    /**
     * MD5加密(字符集utf-8)
     *
     * @param data
     */
    public static byte[] getSecret(String data) {
        if (data == null) {
            return new byte[0];
        }
        return DigestUtils.md5Hex(data).substring(8,16).getBytes();
    }

    /**
     * 转换密钥
     * @param key 二进制密钥
     * @return Key 密钥
     */
    public static Key toKey(byte[] key) throws Exception{
        //实例化Des密钥
        DESKeySpec dks=new DESKeySpec(key);
        //实例化密钥工厂
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance(KEY_ALGORITHM);
        //生成密钥
        SecretKey secretKey=keyFactory.generateSecret(dks);
        return secretKey;
    }
    /**
     * 加密数据
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密后的数据
     */
    public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
        //还原密钥
        Key k=toKey(key);
        //实例化
        Cipher cipher= Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        //执行操作
        return cipher.doFinal(data);
    }
    /**
     * 解密数据
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     */
    public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
        //欢迎密钥
        Key k =toKey(key);
        //实例化
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        //执行操作
        return cipher.doFinal(data);
    }
    /**
     * 解密数据
     * @param data 待解密数据
     * @param key 密钥
     * @return String 解密后的数据
     */
    public static String decryptBase64(String data,byte[] key) throws Exception{
        return new String(decrypt(new Base64().decode(data.getBytes()), key));
    }
    /**
     * 加密数据
     * @param data 待加密数据
     * @param key 密钥
     * @return String 解密后的数据
     */
    public static String encryptBase64(String data,byte[] key) throws Exception{
        return new String(new Base64().encode(encrypt(data.getBytes(), key)));
    }
    /**
     * 获取code
     * @param feedId feedId
     * @param userId 用户id
     * @param appCode 密钥
     * @return String code
     */
    public static String getCode(String feedId,String userId,String appCode,Gson gson) throws Exception{
        IdentityInformation.Visitor visitor=new IdentityInformation.Visitor();
        visitor.setFeed_id(feedId);
        visitor.setUser_id(userId);
        IdentityInformation information=new IdentityInformation();
        information.setVisitor(visitor);
        return CryptUtil.encryptBase64(gson.toJson(information), CryptUtil.getSecret(appCode));
    }

	public static void main(String[] args) {
//		String feedId = "s_1488845211318794";//
//		String userId = "3";
//		String appCode = "cdc095af4dd14c64949a865a2d004be5";
		String feedId = "s_9214975843484032";
		String userId = "2";
		String appCode = "cdc095af4dd14c64949a865a2d004be5";
		try {
			//生成toonCode
			String toonCode = CryptUtil.getCode(feedId, userId, appCode, new Gson());
			System.out.println("toonCode = " + toonCode);
			//解密toonCode
			String toonInfo =  ToonDesUtils.decryptWithSecret(toonCode,appCode);
		    ToonCode code =  JSONObject.parseObject(toonInfo, ToonCode.class);
//			String user = CryptUtil.decryptBase64(tooCode, CryptUtil.getSecret(appCode));
//			IdentityInformation identityInformation = new Gson().fromJson(user, IdentityInformation.class);
			System.out.println("userId = " + code.getVisitor().getUser_id());
			System.out.println("feedID = " + code.getVisitor().getFeed_id());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
