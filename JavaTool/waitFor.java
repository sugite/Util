
Runtime rt = Runtime.getRuntime();  
String command = "cmd /c ffmpeg -loglevel quiet -i "+srcpath+" -ab "+bitrate+"k -acodec libmp3lame "+desfile;  
try {  
	p = rt.exec(command ,null,new File("C:\\ffmpeg-git-670229e-win32-static\\bin"));  
	//获取进程的标准输入流  
	final InputStream is1 = p.getInputStream();   
	//获取进城的错误流  
	final InputStream is2 = p.getErrorStream();  
	//启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流  
	new Thread() {  
	public void run() {  
	   BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));  
	    try {  
	        String line1 = null;  
	        while ((line1 = br1.readLine()) != null) {  
	              if (line1 != null){}  
	          }  
	    } catch (IOException e) {  
	         e.printStackTrace();  
	    }  
	    finally{  
	         try {  
	           is1.close();  
	         } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	      }  
	    }  
	 }.start();  
                            
	new Thread() {   
	  public void  run() {   
	   BufferedReader br2 = new  BufferedReader(new  InputStreamReader(is2));   
	      try {   
	         String line2 = null ;   
	         while ((line2 = br2.readLine()) !=  null ) {   
	              if (line2 != null){}  
	         }   
	       } catch (IOException e) {   
	             e.printStackTrace();  
	       }   
	      finally{  
	         try {  
	             is2.close();  
	         } catch (IOException e) {  
	             e.printStackTrace();  
	         }  
	       }  
	    }   
	}.start();    
                            
	p.waitFor();  
	p.destroy();   
	System.out.println("我想被打印...");  
	} catch (Exception e) {  
	    try{  
	        p.getErrorStream().close();  
	        p.getInputStream().close();  
	        p.getOutputStream().close();  
	        }  
	    catch(Exception ee){}  
	}  
}