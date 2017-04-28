package util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class FileAccessI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RandomAccessFile oSavedFile;
	long nPos;

	public FileAccessI() throws IOException {
		
	}

	public FileAccessI(File file, long nPos) throws IOException {
		oSavedFile = new RandomAccessFile(file, "rw");// ����һ���������ļ��࣬�ɶ�дģʽ
		
		this.nPos = nPos;
		oSavedFile.seek(nPos);
	}
	
	public synchronized int write(byte[] b, int nStart, int nLen)
	     {
	         int n = -1;
	         try
	         {
	             oSavedFile.write(b, nStart, nLen);
	             n = nLen;
	        }
	         catch (IOException e)
	         {
	             e.printStackTrace();
	         }
	        return n;
	     }
	     //ÿ�ζ�ȡ102400�ֽ�
	     public synchronized Detail getContent(long nStart)
	     {
	        Detail detail = new Detail();
	        detail.b = new byte[1024 * 500];
	        try
	         {
	             oSavedFile.seek(nStart);
	             detail.length = oSavedFile.read(detail.b);
	        }
	        catch (IOException e)
	        {
	             e.printStackTrace();
	        }
	         return detail;
	     }
	 
	
	     public class Detail
	    {
	
	       public byte[] b;
	        public int length;
	     }
	 
	     //��ȡ�ļ�����
	     public long getFileLength()
	     {
	        Long length = 0l;
	        try
	        {
	            length = oSavedFile.length();
	        }
	         catch (IOException e)
	        {
	             // TODO Auto-generated catch block
	             e.printStackTrace();
	         }
	        return length;
	     }

}
