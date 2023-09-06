import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class MainProcess extends JFrame{
	private JButton dataStatistic;         //����ͳ��
	private JButton startTest;             //��ʼ����
	private JButton stopTest;              //ֹͣ����
	private JButton viewResult;            //�鿴���
	private JTextArea testResult;          //���Թ��̺ͽ��
	private JScrollPane resultProcessPane;
	private boolean runnable = true;
	private boolean flag;
	private String showText;
	private DataCalculate dataCalculate;
	public MainProcess() {
		this.setTitle("��ִ���ļ�");
		this.setLayout(null);;
		this.setResizable(false);
		this.setSize(540, 500);
		this.setLocationRelativeTo(null);
		
		dataCalculate = new DataCalculate(testResult);
		Container container = this.getContentPane();

		dataStatistic = new JButton("����ͳ��");
		dataStatistic.setBounds(80,360,100,30);
		container.add(dataStatistic);
		
		startTest = new JButton("��ʼ����");
		startTest.setBounds(80,410,100,30);
		container.add(startTest);
		
		stopTest = new JButton("ֹͣ����");
		stopTest.setBounds(340,360,100, 30);
		container.add(stopTest);
		
		viewResult = new JButton("�鿴���");
		viewResult.setBounds(340,410,100,30);
		container.add(viewResult);
		
		testResult = new JTextArea("");
		testResult.setBounds(25, 25, 460, 320);
		testResult.setEditable(false);
		
		resultProcessPane = new JScrollPane(testResult);
		resultProcessPane.setBounds(25, 25, 480, 320);
		container.add(resultProcessPane);
		
		showText = "";
		testResult.setText(showText);
		flag = false;
		java.lang.Runnable rnner=new Runnable(){    
	           public void run(){
	        	   runExe(dataCalculate);
	           }
	       };
	       final Thread thread = new Thread(rnner);  
	
	    //ͳ��ѵ�����Ͳ��Լ�����
	    dataStatistic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				List<String> result = dataCalculate.statisticData();
				String s = testResult.getText()+"����ͳ�ƽ�����£�\n";
				for(String ss:result){
					s+=ss+"\n";
				}
				testResult.setText(s);
			}
		});
	   
	    //��ʼ�Բ��Լ����ݽ��м�������
	    startTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				testResult.append("��ʼѵ����Ԥ��\n");
				thread.start();
				flag = true;
			}
		});
	       
	    //��ͣ����
	    stopTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(flag==true){
					runnable = false;
				}else{
					
				}
			}
		});
		
		//�鿴����ļ�
	    viewResult.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dataCalculate.viewFile();
			}
		});

	}
   
		
	private void runExe(DataCalculate calculate){
		String position = "./Result/";
		File file2 =new File(position); 
		if  (!file2 .isDirectory())     
		{       
		    System.out.println(position+"������");  
		    file2 .mkdirs();    
		    System.out.println(position+"�������");
		} else   
		{  
		    System.out.println(position+"����");  
		}  
 	    HashMap<Integer,HashMap<Integer,Integer>> ItemUsers = calculate.dealData();
 	    //��ʼ����
		List<Float> resultOfRatings = new ArrayList<Float>();
		List<String> resultInFile = new ArrayList<String>();

		FileInputStream fc = null;
		InputStreamReader isc = null;
		BufferedReader brc = null; // ���ڷ�װInputStreamReader
		try {
			String Line = "";
			fc = new FileInputStream(position+"afterPreprocessTest.txt");//��ȡԤ������test�ļ�
			isc = new InputStreamReader(fc);												
			brc = new BufferedReader(isc);
														
			//System.out.println("��ʼ��ȡtestͳ���ļ�");
           while ((Line = brc.readLine()) != null && this.runnable==true) {
				
				String[] temp = Line.split(":");
				Integer user = Integer.valueOf(temp[0]);//user
				/*
				 * ��ȡ�û�����Ͷ��Ʊ��item���б����item�б����Ϊ��
				 * ����getOverratedItems�ǵ�һ����Ҫ�����쳣����ĵط�
				 * ���item����б����Ϊ�գ���ôSimItemscandidate��ȡ�����б���Ϊ0���б�Ϊ��
				 */
				HashMap<Integer,Integer> SimItemscandidate = calculate.getOverratedItems(user);//user����Ͷ��Ʊ��item���������value��ֵ
				/*
				 * ���candidateItems����б�Ϊ�յĻ�
				 * candidateItems����ϢҲ��Ϊ�յ�
				 */
				List<HashMap<Integer,Integer>> candidateInfo = new ArrayList<HashMap<Integer, Integer>>();//��¼����items��
					for(Entry<Integer,Integer> i:SimItemscandidate.entrySet()){
							candidateInfo.add(ItemUsers.get(i.getKey()));//���i��һ�����ڵģ���Ϊ������һ���û���������0�ķ���
					}
				String[] items = temp[1].split(",");//items
				String k = testResult.getText();
				k += user+"|"+items.length+"\n";
				testResult.setText(k);
				System.out.println(user+"|"+items.length);
				resultInFile.add(user+"|"+items.length);
				for(int i=0;i<items.length;i++){//forһ�Σ��õ�һ��item��Ԥ��
					int target = Integer.valueOf(items[i]);//Ŀ��item��id
					HashMap<Integer,Integer> targetInfo = new HashMap<Integer, Integer>();
					
					if(ItemUsers.containsKey(target)){
						targetInfo = ItemUsers.get(target);
						List<Float> sim = new ArrayList<Float>();//����Ϊ�գ����sim
						
						if(candidateInfo.size()==0){
							resultOfRatings.add((float)0);
							System.out.println(items[i]+"  "+String.valueOf(0));
							String s = testResult.getText();
							
							s += items[i]+"  "+String.valueOf(0)+"\n";
							testResult.setText(s);
							resultInFile.add(items[i]+"  "+String.valueOf(0));
						}else{
							for(HashMap<Integer,Integer> hm : candidateInfo){
								sim.add(calculate.getCosineSim(hm,targetInfo));
							}
							float ratings = calculate.getPreRating(sim,SimItemscandidate);//sim��SimItemscandidate����ͬ���ȵ�
							resultOfRatings.add(ratings);
							System.out.println(items[i]+"  "+String.valueOf(ratings));
							String s = testResult.getText();
							s += items[i]+"  "+String.valueOf(ratings)+"\n";
							testResult.setText(s);
							resultInFile.add(items[i]+"  "+String.valueOf(ratings));
						}
					}else{
						resultOfRatings.add((float)0);
						System.out.println(items[i]+"  "+String.valueOf(0));
						String s = testResult.getText();
						s += items[i]+"  "+String.valueOf(0)+"\n";
						testResult.setText(s);
						resultInFile.add(items[i]+"  "+String.valueOf(0));
					}					
				}	
			} 		
			// �������ļ����µ�����ͳ��ͬʱ��ɡ�
		} catch (FileNotFoundException e1) {
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e1) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {				//��󿪵��ȹر�
				brc.close();
				isc.close();
				fc.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//System.out.println("�������");
		//System.out.println("�ѽ�������д���ļ�");
		//���õ����д���ļ�
		File RatingsTxt = new File(position + "Result.txt");
		PrintStream ps5;
		try {
			ps5 = new PrintStream(new FileOutputStream(RatingsTxt));
				for(String ss:resultInFile){
					ps5.println(ss);
				}
			ps5.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println("��������д���ļ�");
		testResult.append("������ɣ��ѽ�������д���ļ�\n");
		
		File DelTxt1 = new File(position + "afterPreprocessTest.txt");
		DelTxt1.delete();
		File DelTxt2 = new File(position + "User_Items.txt");
		DelTxt2.delete();

		
    }	
	

	
	public static void main(String args[]) {
		  MainProcess window = new MainProcess();
		  window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		  window.setVisible(true);
	}
		
}
