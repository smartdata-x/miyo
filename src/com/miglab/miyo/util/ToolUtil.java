package com.miglab.miyo.util;

import java.security.MessageDigest;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by tudou on 2015/5/10.
 */
public class ToolUtil {

    private static Executor executor = Executors.newSingleThreadExecutor();
    public static final void executeInSingleThread(Runnable run) {
        executor.execute(run);
    }

    public static String md5(String plainText) {
        // �����ַ���
        String md5Str = null;
        try {
            // �����ַ���
            StringBuffer buf = new StringBuffer();
            MessageDigest md = MessageDigest.getInstance("MD5");
            // ���Ҫ���м���ժҪ����Ϣ,ʹ�� plainText �� byte �������ժҪ��
            md.update(plainText.getBytes());
            // �����ժҪ,��ɹ�ϣ���㡣
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }
                // ������ ʮ���� i ת��Ϊ16λ����ʮ�����Ʋ�����ʾ���޷�������ֵ���ַ�����ʾ��ʽ��
                buf.append(Integer.toHexString(i));
            }
            // 32λ�ļ���
            md5Str = buf.toString();
            // 16λ�ļ���
            // md5Str = buf.toString().md5Strstring(8,24);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Str;
    }
}
