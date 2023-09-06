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
	private JButton dataStatistic;         //数据统计
	private JButton startTest;             //开始测试
	private JButton stopTest;              //停止测试
	private JButton viewResult;            //查看结果
	private JTextArea testResult;          //测试过程和结果
	private JScrollPane resultProcessPane;
	private boolean runnable = true;
	private boolean flag;
	private String showText;
	private DataCalculate dataCalculate;
	public MainProcess() {
		this.setTitle("可执行文件");
		this.setLayout(null);;
		this.setResizable(false);
		this.setSize(540, 500);
		this.setLocationRelativeTo(null);
		
		dataCalculate = new DataCalculate(testResult);
		Container container = this.getContentPane();

		dataStatistic = new JButton("数据统计");
		dataStatistic.setBounds(80,360,100,30);
		container.add(dataStatistic);
		
		startTest = new JButton("开始测试");
		startTest.setBounds(80,410,100,30);
		container.add(startTest);
		
		stopTest = new JButton("停止测试");
		stopTest.setBounds(340,360,100, 30);
		container.add(stopTest);
		
		viewResult = new JButton("查看结果");
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
	
	    //统计训练集和测试集数据
	    dataStatistic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				List<String> result = dataCalculate.statisticData();
				String s = testResult.getText()+"数据统计结果如下：\n";
				for(String ss:result){
					s+=ss+"\n";
				}
				testResult.setText(s);
			}
		});
	   
	    //开始对测试集数据进行计算评分
	    startTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				testResult.append("开始训练并预测\n");
				thread.start();
				flag = true;
			}
		});
	       
	    //暂停计算
	    stopTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(flag==true){
					runnable = false;
				}else{
					
				}
			}
		});
		
		//查看结果文件
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
		    System.out.println(position+"不存在");  
		    file2 .mkdirs();    
		    System.out.println(position+"创建完成");
		} else   
		{  
		    System.out.println(position+"存在");  
		}  
 	    HashMap<Integer,HashMap<Integer,Integer>> ItemUsers = calculate.dealData();
 	    //开始计算
		List<Float> resultOfRatings = new ArrayList<Float>();
		List<String> resultInFile = new ArrayList<String>();

		FileInputStream fc = null;
		InputStreamReader isc = null;
		BufferedReader brc = null; // 用于封装InputStreamReader
		try {
			String Line = "";
			fc = new FileInputStream(position+"afterPreprocessTest.txt");//读取预处理后的test文件
			isc = new InputStreamReader(fc);												
			brc = new BufferedReader(isc);
														
			//System.out.println("开始读取test统计文件");
           while ((Line = brc.readLine()) != null && this.runnable==true) {
				
				String[] temp = Line.split(":");
				Integer user = Integer.valueOf(temp[0]);//user
				/*
				 * 获取用户曾经投过票的item，列表这个item列表可能为空
				 * 所以getOverratedItems是第一个需要进行异常处理的地方
				 * 如果item这个列表真的为空，那么SimItemscandidate获取到的列表长度为0，列表为空
				 */
				HashMap<Integer,Integer> SimItemscandidate = calculate.getOverratedItems(user);//user曾经投过票的item，这里面的value是值
				/*
				 * 如果candidateItems这个列表为空的话
				 * candidateItems的信息也是为空的
				 */
				List<HashMap<Integer,Integer>> candidateInfo = new ArrayList<HashMap<Integer, Integer>>();//记录所有items的
					for(Entry<Integer,Integer> i:SimItemscandidate.entrySet()){
							candidateInfo.add(ItemUsers.get(i.getKey()));//这个i是一定存在的，因为至少有一个用户给评过非0的分数
					}
				String[] items = temp[1].split(",");//items
				String k = testResult.getText();
				k += user+"|"+items.length+"\n";
				testResult.setText(k);
				System.out.println(user+"|"+items.length);
				resultInFile.add(user+"|"+items.length);
				for(int i=0;i<items.length;i++){//for一次，拿到一个item的预测
					int target = Integer.valueOf(items[i]);//目标item的id
					HashMap<Integer,Integer> targetInfo = new HashMap<Integer, Integer>();
					
					if(ItemUsers.containsKey(target)){
						targetInfo = ItemUsers.get(target);
						List<Float> sim = new ArrayList<Float>();//可能为空，这个sim
						
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
							float ratings = calculate.getPreRating(sim,SimItemscandidate);//sim和SimItemscandidate是相同长度的
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
			// 当读完文件，新的数据统计同时完成。
		} catch (FileNotFoundException e1) {
			System.out.println("找不到指定文件");
		} catch (IOException e1) {
			System.out.println("读取文件失败");
		} finally {
			try {				//最后开的先关闭
				brc.close();
				isc.close();
				fc.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//System.out.println("计算完成");
		//System.out.println("已将计算结果写入文件");
		//将得到结果写入文件
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
		//System.out.println("计算结果已写入文件");
		testResult.append("计算完成，已将计算结果写入文件\n");
		
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
