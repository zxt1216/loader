package com.sean.arya.helper;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
/**
 * 辅助修改名称,实现切换桌面时更换壁纸。
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
		//检查是否由于强制关闭程序引发的出现.temp文件的情况
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
		//更换壁纸
		int key=1;
		Random r=new Random();
		while(key==1){
			for(int i=0;i<fileNumber;i++){
				File[] fs=folds[i].listFiles();
				int rand=r.nextInt(fs.length);
				File temp=new File("Killo_0.jpg.temp");
				File ruleFile=new File(folds[i].getAbsolutePath()+"/"+ruleName);
				//交换
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
