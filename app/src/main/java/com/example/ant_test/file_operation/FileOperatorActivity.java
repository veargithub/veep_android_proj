package com.example.ant_test.file_operation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @Title: FileOperatorActivity.java
 * @Package com.example.ant_test.file_operation
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-5-9 上午10:29:44
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class FileOperatorActivity extends Activity{
	private String fileName = "aaa.txt";
	private String content = "bbb";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		write();
	}
	
	private void write() {
		 try {  
	            /* 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的， 
	             * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 
	             *   public abstract FileOutputStream openFileOutput(String name, int mode) 
	             *   throws FileNotFoundException; 
	             * openFileOutput(String name, int mode); 
	             * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名 
	             *          该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt 
	             * 第二个参数，代表文件的操作模式 
	             *          MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖 
	             *          MODE_APPEND  私有   重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 
	             *          MODE_WORLD_READABLE 公用  可读 
	             *          MODE_WORLD_WRITEABLE 公用 可读写 
	             *  */  
	            FileOutputStream outputStream = openFileOutput(fileName,  
	                    Activity.MODE_PRIVATE);  
	            outputStream.write(content.getBytes());  
	            outputStream.flush();  
	            outputStream.close();  
	            Toast.makeText(FileOperatorActivity.this, "保存成功", Toast.LENGTH_LONG).show();  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}
}
