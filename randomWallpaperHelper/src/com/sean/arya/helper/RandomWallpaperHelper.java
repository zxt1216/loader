package com.sean.arya.helper;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
/**
 * �����޸�����,ʵ���л�����ʱ������ֽ��
 * @author Sean
 * @since  2017-06-30
 * @version 1.0.0
 */
public class RandomWallpaperHelper {

	public static void main(String[] args) {
		int fileNumber=0;
		int flashTime=300000;
		String base=null;
		String ruleName=null;
		File[] folds=null;
		Properties p=new Properties();
		try {
			p.load(RandomWallpaperHelper.class.getResourceAsStream("/source.properties"));
			fileNumber=Integer.parseInt(p.getProperty("fileNumber"));
			flashTime=Integer.parseInt(p.getProperty("flashTime"));
			base=p.getProperty("baseFilename");
			folds=new File[fileNumber];
			for(int i=0;i<fileNumber;i++){
				folds[i]=new File(base+"/"+p.getProperty("filename"+(char)('A'+i)));
			}
			ruleName=p.getProperty("ruleName");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//����Ƿ�����ǿ�ƹرճ��������ĳ���.temp�ļ������
		for(int i=0;i<fileNumber;i++){
			File[] fs=folds[i].listFiles();
			for(File f:fs){
				if(f.getAbsolutePath().endsWith(".temp")){
					String name=f.getAbsolutePath();
					f.renameTo(new File(name.substring(0, name.length()-5)));
					break;
				}
			}
		}
		//������ֽ
		int key=1;
		Random r=new Random();
		while(key==1){
			for(int i=0;i<fileNumber;i++){
				File[] fs=folds[i].listFiles();
				int rand=r.nextInt(fs.length);
				File temp=new File("Killo_0.jpg.temp");
				File ruleFile=new File(folds[i].getAbsolutePath()+"/"+ruleName);
				//����
				fs[rand].renameTo(temp);
				ruleFile.renameTo(fs[rand]);
				temp.renameTo(ruleFile);
			}
			try {
				Thread.sleep(flashTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
