package com.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.sun.jna.*;
import com.sun.jna.win32.*;
import com.sun.jna.darwin.*;
import com.sun.jna.ptr.*;

/** 
 * 
 * 
 * 
 * */
public class Judge {

	public List<String> inJudge(String id, String corjava, String answer,
			String timeLimit, String momery, String userid) {

		System.out
				.println("+++++++++++++++++++++++++++++++++++++++++++beginjudge+userid:"
						+ userid);
		
		List<String> result = new LinkedList<String>();
		long timeLimits = Long.parseLong(timeLimit);
		String filepath = "D:\\Program Files\\apache-tomcat-7.0.32\\bin\\"
				+ userid + "main.exe";
		if (corjava.equals("java")) {
			new File("D:\\dataForPrograms\\onlinejudge\\" + userid).mkdir();
			filepath = "D:\\dataForPrograms\\onlinejudge\\" + userid
					+ "\\Main.class";// java
		}

		File exec = new File(filepath); // ׼�����������main.exe���ϴα���ģ�����ɾ��
		if (exec.exists()) {
			exec.delete();
			System.out.println("ԭmain.exe�ļ�ɾ���ɹ���");
		}
		String file = "";
		if (corjava.equals("c")) {
			file = "D:\\dataForPrograms\\onlinejudge\\" + userid + "main.cpp";
		} else {
			file = "D:\\dataForPrograms\\onlinejudge\\" + userid
					+ "\\Main.java";
		}

		File sourceFile = new File(file);
		if (sourceFile.exists()) { // ����Ѿ����ڣ���ɾ��������ļ�������һֱֻ��һ���ļ���Ҳ���ǵ�ǰѧ����Դ���룬���к��ɾ��
			sourceFile.delete();
			// System.out.println("Delete Success!!!");
		}
		try {
			sourceFile.createNewFile();
			System.out.println("Create Success!!!!");
		} catch (Exception e) {
			e.getMessage();
		}

		try { // �ѱ����ύ�Ĵ���д��Դ�ļ�
			FileWriter fw = new FileWriter(sourceFile);
			fw.write(answer);
			fw.close();
		} catch (IOException e) {
			e.getMessage();
		}

		if (corjava.equals("c")) { // �����c�ļ�

			Process processCompile = null;
			try {
				// Runtime.getRuntime().exec("F:");
				// //����c�ļ������������main��exe�ļ��Ƿ��Ѿ����ɣ������ɣ���˵���������󡣴�ʱ�и����⣬��ȥ����ļ��Ƿ����ɣ���������ļ����Ǹ��߳������˺�ͷ����ô�죿
				// Runtime.getRuntime().exec("cd dataForPrograms/onlinejudge");
				// g++.exe "C:\Users\Administrator\Desktop\r.cpp" -o
				// "C:\Users\Administrator\Desktop\r.exe"
				String compilepath = "g++ " + "\"" + file + "\"" + " -o "
						+ "\""
						+ "D:\\Program Files\\apache-tomcat-7.0.32\\bin\\"
						+ userid + "main.exe" + "\"";
				System.out.println(compilepath);
				// String[] cmdnm = new String[]{"g++.exe ","\"",file
				// ,"\""," -o " ,
				// "\"","D:\\Program Files\\apache-tomcat-7.0.32\\bin\\",userid,"main.exe","\""};
				processCompile = Runtime.getRuntime().exec(compilepath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			try {  
				 //��ȡ���̵ı�׼������  
				 final InputStream is1 = processCompile.getInputStream();   
				 //��ȡ���ǵĴ�����  
				 final InputStream is2 = processCompile.getErrorStream();  
				 //���������̣߳�һ���̸߳������׼���������һ���������׼������  
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
				                                
				      processCompile.waitFor();  
				      //p.destroy();   
				     System.out.println("���뱻��ӡ...");  
				    } catch (Exception e) {  
				            try{  
				            	processCompile.getErrorStream().close();  
				            	processCompile.getInputStream().close();  
				            	processCompile.getOutputStream().close();  
				                }  
				             catch(Exception ee){}  
				    }  


			if (exec.exists()) { // ˵������ͨ��,����������ִ������ļ������ڲ������ݿ����ж��飬������ѭ����ִ�У���Ȼ��ֻҪ����һ����ģ���break�˳���
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(processCompile.getInputStream()));
				String kengstr;
				try {
					while ((kengstr = bufferedReader.readLine()) != null)
						;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				processCompile.destroy();
				try {
					bufferedReader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("��main.exe �ļ����ڣ�˵��" + id + "����ͨ���ˡ�");

				for (int i = 0; i < 10; i++) {
					File in = new File("D:\\dataForPrograms\\" + id + "\\" + i
							+ ".in"); // ������ڣ��Ϳ��Զ���

					if (in.exists()) {
						System.out.println("D:\\dataForPrograms\\" + id + "\\"
								+ i + ".in�ļ�����");

						try {/**//**/
							FileReader readInputTest = new FileReader(in); // ���������ļ�����
							char[] buf = new char[1024];
							int len = readInputTest.read(buf);
							String input = "";
							if (len >= 0) {
								input = new String(buf, 0, len);
							} // �����ļ������ݷ����ַ���input��
							readInputTest.close();
							// System.out.println(input);

							ExecutorService executor = Executors
									.newCachedThreadPool(); // ���￪ʼ������ʱִ���̲߳���
							ExeCode execode = new ExeCode(input, filepath, "c");
							Future<String> future = executor.submit(execode);
							String failReason = null, key = null;
							try {
								key = future.get(timeLimits + 3000,
										TimeUnit.MILLISECONDS); // ��ʱִ��
							} catch (InterruptedException e) {
								execode.destroyProcess();
								e.printStackTrace();
							} catch (ExecutionException e) {
								execode.destroyProcess();
								e.printStackTrace();
							} catch (TimeoutException e) { // ��ʱ�쳣
								execode.destroyProcess();
								failReason = "timeout";
								executor.shutdownNow();
								//Runtime.getRuntime().exec("taskkill /im " + userid + "main.exe"+ " /f");
							}
							if (failReason != null) { // �ж���û�г�ʱ���������null����˵���Ѿ���ʱ
								result.add(failReason);
								break;
							}

							// Runtime.getRuntime().exec("cmd /c f:\\killProcess.bat");

							BufferedReader outReader = new BufferedReader(
									new FileReader("D:\\dataForPrograms\\" + id
											+ "\\" + i + ".out"));
							String output = "";
							String readTemp = "";
							while ((readTemp = outReader.readLine()) != null) {
								output = output + readTemp + "\n";
							}
							outReader.close();

							System.out.println("key is :" + key);
							System.out.println("output is :" + output);

							if (key.trim().equals(output.trim())) {
								result.add("correct");
							} else {
								result.add("wrong");
								break; // һ���������д��󣬾Ϳ���break
							}

						} catch (IOException e) {
							e.printStackTrace();
						}

					} else { // .in�ļ������ڣ���������ѭ����
						result.add("correct");
						System.out.println("D:\\dataForPrograms\\" + id + "\\"
								+ i + ".in�ļ������ڣ���������");
						break;
					}
					try {
						Runtime.getRuntime().exec("taskkill /im " +filepath.substring(filepath.lastIndexOf('\\')+1)+ " /f");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			} else { // main.exe�����ڣ�˵������ûͨ��
				System.out.println("��main.exe�����ڣ�˵��" + id + "����δͨ��������");

				// BufferedReader br = new BufferedReader(new
				// InputStreamReader(processCompile.getInputStream()));
				StringBuffer compileError = new StringBuffer();
				// try {
				// while ((inline = br.readLine()) != null) {
				// compileError = compileError +inline;
				// }
				// } catch (IOException e) {
				//
				// e.printStackTrace();
				// }
				// System.out.println("000000000000000000000000000000000000000000000000000000000000000\n"+compileError);
				//
				String line = null;

				// get the process's errorStream
				InputStream errorStream = processCompile.getErrorStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(errorStream));

				System.out.println("-----------------error informaiton------");
				try {
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
						compileError.append(line).append("\n");
					}
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println("---------------error informaiton------\n");

				int exitValue = -1;
				try {
					exitValue = processCompile.waitFor();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Return Value:" + exitValue);

				result.add(compileError.toString());

				try {
					reader.close();
					errorStream.close();
					processCompile.destroy();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			

		}

		if (corjava.equals("java")) {
			Process processCompile = null;
			try {
				String compilepath = "javac " + file;
				processCompile = Runtime.getRuntime().exec(compilepath);
				System.out.println(compilepath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				processCompile.waitFor();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			if (exec.exists()) {
				processCompile.destroy();
				System.out.println("��Main.class �ļ����ڣ�˵��" + id + "����ͨ���ˡ�");

				for (int i = 0; i < 10; i++) {
					File in = new File("D:\\dataForPrograms\\" + id + "\\" + i
							+ ".in"); // ������ڣ��Ϳ��Զ���

					if (in.exists()) {
						System.out.println("D:\\dataForPrograms\\" + id + "\\"
								+ i + ".in�ļ�����");

						try {/**//**/
							FileReader readInputTest = new FileReader(in); // ���������ļ�����
							char[] buf = new char[1024];
							int len = readInputTest.read(buf);
							String input = "";
							if (len >= 0) {
								input = new String(buf, 0, len);
							} // �����ļ������ݷ����ַ���input��
							readInputTest.close();
							// System.out.println(input);

							ExecutorService executor = Executors
									.newCachedThreadPool(); // ���￪ʼ������ʱִ���̲߳���
							ExeCode execode = new ExeCode(input, filepath,
									"java");
							Future<String> future = executor.submit(execode);
							String failReason = null, key = null;
							try {
								key = future.get(timeLimits + 3000,
										TimeUnit.MILLISECONDS); // ��ʱִ��
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							} catch (TimeoutException e) { // ��ʱ�쳣
								execode.destroyProcess();
								failReason = "timeout";
								executor.shutdownNow();
							}
							if (failReason != null) { // �ж���û�г�ʱ���������null����˵���Ѿ���ʱ
								result.add(failReason);
								break;
							}

							BufferedReader outReader = new BufferedReader(
									new FileReader("D:\\dataForPrograms\\" + id
											+ "\\" + i + ".out"));
							String output = "";
							String readTemp = "";
							while ((readTemp = outReader.readLine()) != null) {
								output = output + readTemp + "\n";
							}
							outReader.close();

							System.out.println("key is :" + key);
							System.out.println("output is :" + output.trim());

							if (key.trim().equals(output.trim())) {
								result.add("correct");
							} else {
								result.add("wrong");
								break; // һ���������д��󣬾Ϳ���break
							}

						} catch (IOException e) {
							e.printStackTrace();
						}

					} else { // .in�ļ������ڣ���������ѭ����
						result.add("correct");
						System.out.println("D:\\dataForPrograms\\" + id + "\\"
								+ i + ".in�ļ������ڣ���������");
						break;
					}

				}
			} else {
				System.out.println("��Main.class�����ڣ�˵��" + id + "����δͨ��������");

				StringBuffer compileError = new StringBuffer();
				String line = null;

				// get the process's errorStream
				InputStream errorStream = processCompile.getErrorStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(errorStream));

				System.out.println("-----------------error informaiton------");
				try {
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
						compileError.append(line).append("\n");
					}
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println("---------------error informaiton------\n");

				int exitValue = -1;
				try {
					exitValue = processCompile.waitFor();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Return Value:" + exitValue);

				result.add(compileError.toString());

				try {
					reader.close();
					errorStream.close();
					processCompile.destroy();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			File tempFile = new File("D:\\dataForPrograms\\onlinejudge\\"
					+ userid);
			if (tempFile.isDirectory()) {
				File[] files = tempFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			tempFile.delete();
		}
		if(corjava.equals("c")){
			try {
				Runtime.getRuntime().exec("taskkill /im " +filepath.substring(filepath.lastIndexOf('\\')+1)+ " /f");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<String> judgeResult = new LinkedList<String>();
		judgeResult.add(id);
		if (result.get(result.size() - 1).equals("correct")) {
			judgeResult.add("Answer Correct.");
		} else if (result.get(result.size() - 1).equals("wrong")) {
			judgeResult.add("Wrong Answer.");
		} else if (result.get(result.size() - 1).equals("timeout")) {
			judgeResult.add("Time Limit!!!!(��������ѭ�������ڴ��������)");
		} else {
			judgeResult.add(result.get(0));
		}
		
		return judgeResult;

	}

}

class ExeCode implements Callable<String> {

	private String input = null;
	private String filepath;
	private Process process = null;
	private String cOrJava = null;

	public ExeCode(String input, String filepath, String str) {
		super();
		this.input = input;
		this.filepath = filepath;
		this.cOrJava = str;
	}

	public void destroyProcess() {
		int pid=0;
		if (process.getClass().getName().equals("java.lang.Win32Process")
				|| process.getClass().getName().equals("java.lang.ProcessImpl")) {
			/* determine the pid on windows plattforms */
			try {
				Field f = process.getClass().getDeclaredField("handle");
				f.setAccessible(true);
				long handl = f.getLong(process);

				Kernel32 kernel = Kernel32.INSTANCE;
				W32API.HANDLE handle = new W32API.HANDLE();
				handle.setPointer(Pointer.createConstant(handl));
				pid = kernel.GetProcessId(handle);
			} catch (Throwable e) {
			}
		}
		if(pid!=0){
			try {
				Runtime.getRuntime().exec("taskkill /pid " +String.valueOf(pid)+ " /f");
				Runtime.getRuntime().exec("taskkill /im " +"cc1plus.exe"+ " /f");
				Runtime.getRuntime().exec("taskkill /im " +"cc1plus.exe"+ " /f");
				if(cOrJava.equals("c")){
					Runtime.getRuntime().exec("taskkill /im " +filepath.substring(filepath.lastIndexOf('\\')+1)+ " /f");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		process.destroy();
	}

	@Override
	public String call() throws Exception {
		String cmds[];
		if (cOrJava.equals("c")) {
			cmds = new String[] {filepath };
		} else {
			cmds = new String[]{"java","-classpath",filepath.substring(0, filepath.length() - 11), "Main"};
		}
		System.out.println(cmds);
		ProcessBuilder builder = new ProcessBuilder(cmds);
		builder.redirectErrorStream(true);
		process = builder.start();

		// try {
		// if (cOrJava.equals("c")) {
		// process = Runtime.getRuntime().exec("cmd /c "+filepath);
		// } else {
		// process =
		// Runtime.getRuntime().exec("java -classpath "+filepath.substring(0,filepath.length()-11
		// )+" Main" );
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		BufferedWriter bufferWriter = new BufferedWriter(
				new OutputStreamWriter(process.getOutputStream())); // ���������ļ���������dos����
		try {
			bufferWriter.write(input);
			System.out.println("Write data to console:" + input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bufferWriter.close();
			System.out.println("close the bufferwriter");
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String temp, key = "";
		try {
			while ((temp = bufferReader.readLine()) != null) {
				System.out.println("temp=" + temp);
				key = key + temp + "\n";
			}
			key = key.trim();
			System.out.println("answer is :" + key);
		} catch (IOException e) {
			e.printStackTrace();
		}

		process.destroy();
		System.out.println("destroy the running process.");
		try {
			bufferReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return key;

	}

}
