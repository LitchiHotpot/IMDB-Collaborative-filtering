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
import javax.swing.JTextArea;	

public class DataCalculate {
	private String position;
	private String filepos;

	public DataCalculate(JTextArea areajt){
		position = "./Result";
		filepos = "Result";
	}
	public void viewFile(){
		
		File file2 =new File(this.position); 
		if  (!file2 .isDirectory())     //如果不存在，创建文件夹
		{       
		    file2 .mkdirs();    
		} else   
		{  
		}  
		try {  
            String[] cmd = new String[5];  
            cmd[0] = "cmd";  
            cmd[1] = "/c";  
            cmd[2] = "start";  
            cmd[3] = " ";  
            cmd[4] = this.filepos;  
            Runtime.getRuntime().exec(cmd);  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        }  
	}
	
	//统计数据
	public List<String> statisticData(){
		int records = 0;                //训练集中记录
		int users = 0;                  //用户数
		int items = 0;                  //被评过分的item
		HashMap<Integer,Integer> itemList = new HashMap<Integer, Integer>(); 
		int itemsnotzero = 0;           //有非0评分的item数,即可预测的item
		HashMap<Integer,Integer> itemListnot0 = new HashMap<Integer, Integer>(); 
		int minratings = 0;             //评分最低值
		int maxratings = 0;             //评分最高值
		int testUsers = 0;              //测试集中的用户数
		int testItems = 0;              //测试集中的item数
		HashMap<Integer,Integer> testitemlist = new HashMap<Integer, Integer>();
		int testrecords = 0;            //测试集中的记录数，即需要估计的评分数
		String position = "./Result/"; //写入磁盘的文件的位置
		File file2 =new File(position); 
		if  (!file2 .isDirectory())      
		{       
		    System.out.println(position+"不存在");  
		    file2 .mkdirs();    
		    System.out.println(position+"创建完成");
		} else   
		{  
		    System.out.println(position+"路径存在");  
		} 
		
		long startTime3 = System.currentTimeMillis(); // 获取开始时间

		FileInputStream ft = null;
		InputStreamReader ist = null;
		BufferedReader brt = null; // 用于封装InputStreamReader,提高处理性能
		try {
			String Line = "";
			int user;              // 记录user
			int ratingNum;         // 记录user的投票数，接下来ratingNum行将记录(可统计得到)该user的投票
			ft = new FileInputStream("./data/train.txt");
			                       // 从文件系统中的某个文件中获取字节
			ist = new InputStreamReader(ft);// InputStreamReader
											// 是字节流通向字符流的桥梁,
			brt = new BufferedReader(ist);  // 从字符输入流中读取文件中的内容,封装了一个new
											// InputStreamReader的对象
			Line = brt.readLine();
			user = Integer.valueOf(Line.split("\\|")[0]);
			users++;               //当前为1
			ratingNum = Integer.valueOf(Line.split("\\|")[1]);
		    //System.out.println(Line.split("\\|")[1]);  //得41
			records +=ratingNum;
			while ((Line = brt.readLine()) != null) {
				if (ratingNum > 0) {
					String[] result = Line.split("  ");
					//System.out.println(result);
					//System.out.println("zzz"+result[0]);
					int item = Integer.valueOf(result[0]);
					int rating = Integer.valueOf(result[1]);
					if(!itemList.containsKey(item)){
						itemList.put(item,0);
						items++;
					}
					if(rating!=0){
						if(!itemListnot0.containsKey(item)){
							itemListnot0.put(item, 0);
							itemsnotzero++;
						}
					}
					
					if(rating>maxratings){
						maxratings = rating;
					}
					if(rating<minratings){
						minratings = rating;
					}
						ratingNum--;
				} else {
					if (Line.contains("|")) {
						String[] result = Line.split("\\|");
						user = Integer.valueOf(result[0]);
						users++;
						ratingNum = Integer.valueOf(result[1]);
						records +=ratingNum;
					} else {

					}
				}
			} // 当读完文件，新的数据统计同时完成。
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				brt.close();
				ist.close();
				ft.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileInputStream ftest = null;
		InputStreamReader istest = null;
		BufferedReader brtest = null; // 用于包装InputStreamReader,提高处理性能。
		try {
			String Line = "";
			int user;// 记录user
			int ratingNum;// 记录user的投票数，接下来ratingNum行将记录(可统计得到)该user的投票
			ftest = new FileInputStream("./data/test.txt");
			istest = new InputStreamReader(ftest);														
			brtest = new BufferedReader(istest);													

			Line = brtest.readLine();
			user = Integer.valueOf(Line.split("\\|")[0]);
			testUsers++;
			ratingNum = Integer.valueOf(Line.split("\\|")[1]);
			testrecords +=ratingNum;
			while ((Line = brtest.readLine()) != null) {
				if (ratingNum > 0) {
						if(!testitemlist.containsKey(Integer.valueOf(Line))){
							testitemlist.put(Integer.valueOf(Line),0);
							testItems++;
						}
						ratingNum--;
				} else {
					if (Line.contains("|")) {
						String[] result = Line.split("\\|");
						user = Integer.valueOf(result[0]);
						testUsers++;
						ratingNum = Integer.valueOf(result[1]);
						testrecords+=ratingNum;
					} else {

					}
				}
			} // 当读完文件，新的数据统计同时完成。
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				brtest.close();
				istest.close();
				ftest.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		long endTime3 = System.currentTimeMillis(); // 获取结束时间
		System.out.println("测试集统计完成");
		System.out.println("训练集中用户共有"+users+"个");
		System.out.println("训练集中评分记录共有"+records+"条");
		System.out.println("训练集中item共有"+items+"个");
		System.out.println("训练集中评分非0的item共有"+itemsnotzero+"个");
		System.out.println("训练集中需要预测评分item共有"+testrecords+"条");
		System.out.println("测试集中用户共有"+testUsers+"个");
		System.out.println("测试集中item共有"+testItems+"个");
		System.out.println("程序运行时间:" + (endTime3 - startTime3) + "ms");
		

		List<String> result = new ArrayList<String>();
		result.add("训练集中用户共有"+users+"个");
		result.add("训练集中评分记录共有"+records+"条");
		result.add("训练集中item共有"+items+"个");
		result.add("训练集中评分非0的item共有"+itemsnotzero+"个");
		result.add("训练集中需要预测评分item共有"+testrecords+"条");
		result.add("测试集中用户共有"+testUsers+"个");
		result.add("测试集中item共有"+testItems+"个");
		result.add("统计耗时:"+(endTime3 - startTime3) + "ms");
		return result;
	}

	//对训练集数据和测试集数据进行处理
	public HashMap<Integer,HashMap<Integer,Integer>> dealData(){
		String position = "./Result/";//写入磁盘的文件的位置
		File file2 =new File(position); 
		if  (!file2 .isDirectory())     //如果需要，创建路径 
		{       
		    //System.out.println(position+"不存在");  
		    file2 .mkdirs();    
		    //System.out.println(position+"创建完成");
		} else   
		{  
		    //System.out.println(position+"存在");  
		}  

		
		HashMap<Integer,ArrayList<Integer>> testSet = new HashMap<Integer, ArrayList<Integer>>();//user-items-ratings 记录M的行信息，训练U
		long startTime3 = System.currentTimeMillis(); // 获取开始时间

		FileInputStream ftest = null;
		InputStreamReader istest = null;
		BufferedReader brtest = null; // 用于包装InputStreamReader,提高处理性能。
		try {
			String Line = "";
			
			int user;// 记录user
			int ratingNum;// 记录user的投票数，接下来ratingNum行将记录(可统计得到)该user的投票
			ftest = new FileInputStream("./data/test.txt");
			istest = new InputStreamReader(ftest);												
			brtest = new BufferedReader(istest);
														

			Line = brtest.readLine();
			user = Integer.valueOf(Line.split("\\|")[0]);
			ratingNum = Integer.valueOf(Line.split("\\|")[1]);
			testSet.put(user, new ArrayList<Integer>());
			while ((Line = brtest.readLine()) != null) {
				if (ratingNum > 0) {
						testSet.get(user).add(Integer.valueOf(Line));
						ratingNum--;
				} else {
					if (Line.contains("|")) {
						String[] result = Line.split("\\|");
						user = Integer.valueOf(result[0]);
						ratingNum = Integer.valueOf(result[1]);
						testSet.put(user, new ArrayList<Integer>());
					} else {

					}
				}
			} 
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				brtest.close();
				istest.close();
				ftest.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime3 = System.currentTimeMillis(); // 结束时间
		//将测试集预处理后的数据写入文件
		long startTime5 = System.currentTimeMillis(); // 获取开始时间
		File testTxt = new File(position + "afterPreprocessTest.txt");
		PrintStream ps2;
		try {
			ps2 = new PrintStream(new FileOutputStream(testTxt));
			for(Entry<Integer,ArrayList<Integer>> e : testSet.entrySet()){
				String s = "";
				s += e.getKey()+":";
				for(Integer i:e.getValue()){
					s += i + ",";
				}
				s = s.substring(0,s.length()-1);
				ps2.println(s);
			}
			ps2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime5 = System.currentTimeMillis(); // 获取结束时间
		//System.out.println("文件写入完成，" + "耗时" + (endTime5 - startTime5) + "ms");
		testSet.clear();
		
		HashMap<Integer,HashMap<Integer,Integer>> UserItems = new HashMap<Integer, HashMap<Integer, Integer>>();//user-items-ratings 记录M的行信息，训练U
		long startTime = System.currentTimeMillis(); // 获取开始时间

		FileInputStream fFromNode = null;
		InputStreamReader isFromNode = null;
		BufferedReader brFromNode = null; // 用于包装InputStreamReader,提高处理性能。
		try {
			String Line = "";
			
			int user;// 记录user
			int ratingNum;// 记录user的投票数，接下来ratingNum行将记录(可统计得到)该user的投票
			fFromNode = new FileInputStream("./data/train.txt");
			// 从文件系统中的某个文件中获取字节
			isFromNode = new InputStreamReader(fFromNode);
															
			brFromNode = new BufferedReader(isFromNode);														

			Line = brFromNode.readLine();
			user = Integer.valueOf(Line.split("\\|")[0]);
			ratingNum = Integer.valueOf(Line.split("\\|")[1]);
			UserItems.put(user, new HashMap<Integer,Integer>());
			while ((Line = brFromNode.readLine()) != null) {
				if (ratingNum > 0) {
					String[] pair = Line.split("  ");
					int item = Integer.valueOf(pair[0]);
					int rating = Integer.valueOf(pair[1]);
					if(rating!=0){
						UserItems.get(user).put(item, rating);
						ratingNum--;
					}else{
						ratingNum--;
					}
				} else {
					if (Line.contains("|")) {
						String[] result = Line.split("\\|");
						user = Integer.valueOf(result[0]);
						ratingNum = Integer.valueOf(result[1]);
						UserItems.put(user, new HashMap<Integer,Integer>());
					} else {
						
					}
				}
			} 
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				brFromNode.close();
				isFromNode.close();
				fFromNode.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis(); // 获取结束时间
		//System.out.println("user-items统计完成");
		//System.out.println("统计得到"+UserItems.size());
		//System.out.println("程序运行时间:" + (endTime - startTime) + "ms");
		
		//写入文件
		long startTime6 = System.currentTimeMillis(); // 获取开始时间
		File userItemsRatingsTxt = new File(position + "Users_Items.txt");
		PrintStream ps3;
		try {
			ps3 = new PrintStream(new FileOutputStream(userItemsRatingsTxt));
			for(Entry<Integer, HashMap<Integer, Integer>> e : UserItems.entrySet()){
				String s = "";
				s += e.getKey()+":";
				int length1 = s.length();
				for(Entry<Integer,Integer> item:e.getValue().entrySet()){
					s += item.getKey()+","+item.getValue()+";";
				}
				int length2 = s.length();
				if(length1==length2){
					ps3.println(s);
				}else{
					s = s.substring(0,s.length()-1);
					ps3.println(s);
				}
			}
			ps3.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime6 = System.currentTimeMillis(); // 获取结束时间
		//System.out.println("对train统计已写入文件");
		//System.out.println("写入时间:" + (endTime6 - startTime6) + "ms");
		UserItems.clear();		
		
		//2.2统计item-usersRatings
		HashMap<Integer,HashMap<Integer,Integer>> ItemUsers = new HashMap<Integer, HashMap<Integer, Integer>>();//记录M的列信息，训练V
		long startTime2 = System.currentTimeMillis(); // 获取开始时间

		FileInputStream fFromNode2 = null;
		InputStreamReader isFromNode2 = null;
		BufferedReader brFromNode2 = null; // 用于包装InputStreamReader
		try {
			String Line = "";
		
			int user;// 记录user
			int ratingNum;// 记录user的投票数，接下来ratingNum行将记录(可统计得到)该user的投票
			fFromNode2 = new FileInputStream("./data/train.txt");
			// 从文件系统中的某个文件中获取字节
			isFromNode2 = new InputStreamReader(fFromNode2);												
			brFromNode2 = new BufferedReader(isFromNode2);														

			Line = brFromNode2.readLine();
			user = Integer.valueOf(Line.split("\\|")[0]);
			ratingNum = Integer.valueOf(Line.split("\\|")[1]);
			while ((Line = brFromNode2.readLine()) != null) {
				if (ratingNum > 0) {
					String[] pair = Line.split("  ");
					int item = Integer.valueOf(pair[0]);
					int rating = Integer.valueOf(pair[1]);
					if(rating!=0){
						if(ItemUsers.containsKey(item)){
							ItemUsers.get(item).put(user,rating);
						}else{
							ItemUsers.put(item, new HashMap<Integer,Integer>());
							ItemUsers.get(item).put(user, rating);
						}
						ratingNum--;
					}else{
						ratingNum--;
					}
				} else {
					if (Line.contains("|")) {
						String[] result = Line.split("\\|");
						user = Integer.valueOf(result[0]);
						ratingNum = Integer.valueOf(result[1]);
					} else {

					}
				}
			} 
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				brFromNode2.close();
				isFromNode2.close();
				fFromNode2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime2 = System.currentTimeMillis(); // 获取结束时间
		//System.out.println("item-usersRating统计完成");
		//System.out.println("统计得到"+ItemUsers.size()+"项");
		//System.out.println("程序运行时间:" + (endTime2 - startTime2) + "ms");
		return ItemUsers;
	}
	
	//获取训练集中用户评过分的item
	public static HashMap<Integer,Integer> getOverratedItems(Integer user){
		HashMap<Integer,Integer> candidateItems = new HashMap<Integer, Integer>();
		FileInputStream fcandi = null;
		InputStreamReader iscandi = null;
		BufferedReader brcandi = null; // 用于包装InputStreamReader,提高处理性能。
		try {
			String Line = "";
			fcandi = new FileInputStream("./Result/Users_Items.txt");
			// 从文件系统中的某个文件中获取字节
			iscandi = new InputStreamReader(fcandi);
															
			brcandi = new BufferedReader(iscandi);
														
			boolean flag = false;
			while ((Line = brcandi.readLine()) != null && flag!=true) {
				if(Line.contains(String.valueOf(user)+":")){
					flag = true;
					String[] s = Line.split(":");
					if(s.length==2){
						String[] items = s[1].split(";");
						for(int i=0;i<items.length;i++){
							String[] info = items[i].split(",");
							candidateItems.put(Integer.valueOf(info[0]),Integer.valueOf(info[1]));
						}
					}else{
					}
					
				}
			} 
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				brcandi.close();
				iscandi.close();
				fcandi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return candidateItems;
	}
	
	//获取预测评分
	public static float getPreRating(List<Float> sim,HashMap<Integer,Integer> SimItemscandidate){
		float numerator = (float)0;
		float denominator = (float)0;
		int i=0;
		for(Entry<Integer,Integer> e : SimItemscandidate.entrySet()){
			float temp = sim.get(i);
			if(temp>0){
				denominator += temp;
				numerator += temp * (float)e.getValue();
			}else{
			}
			i++;
		}
		if(denominator == (float)0){
			return (float)0;
		}else{
			return numerator/denominator;
		}
	}
	
	//获取需要预测的item和已知item的相似度
	public static float getCosineSim(HashMap<Integer,Integer> targetInfo,HashMap<Integer,Integer> candidateInfo){
		int sumT = 0;
		for(Entry<Integer,Integer> e:targetInfo.entrySet()){
			sumT += e.getValue();
		}
		int sumC = 0;
		for(Entry<Integer,Integer> e:candidateInfo.entrySet()){
			sumC += e.getValue();
		}
		int num1 = targetInfo.size();
		int num2 = candidateInfo.size();
		float ave1 = (float)sumT/(float)num1;
		float ave2 = (float)sumC/(float)num2;
	
		HashMap<Integer,Float> center1 = new HashMap<Integer, Float>();
		HashMap<Integer,Float> center2 = new HashMap<Integer, Float>();
		
		for(Entry<Integer,Integer> e:targetInfo.entrySet()){
			center1.put(e.getKey(), (float)e.getValue() - ave1);
		}
		for(Entry<Integer,Integer> e:candidateInfo.entrySet()){
			center2.put(e.getKey(), (float)e.getValue() - ave2);
		}
		//对中心化结果进行计算
		float numerator = (float)0;//分子
		float denominator = (float)0;//分母
		for(Entry<Integer,Float> e : center1.entrySet()){
			Integer tempUser = e.getKey();
			if(center2.containsKey(tempUser)){//for循环结束，得到分子
				numerator += center1.get(tempUser) * center2.get(tempUser);
			}
		}
		
		float denominator1 = (float)0;
		float denominator2 = (float)0;
		for(Entry<Integer,Float> e : center1.entrySet()){
			Integer tempUser = e.getKey();
			if(center2.containsKey(tempUser)){//for循环结束，得到分子
				denominator1 += center1.get(tempUser) * center1.get(tempUser);
			}
		}
		denominator1 = (float) Math.sqrt((double)denominator1);
		for(Entry<Integer,Float> e : center2.entrySet()){
			Integer tempUser = e.getKey();
			if(center1.containsKey(tempUser)){//for循环结束，得到分子
				denominator2 += center2.get(tempUser) * center2.get(tempUser);
			}
		}
		denominator2 = (float) Math.sqrt((double)denominator2);
		denominator = denominator1 * denominator2;
		
		if(denominator == 0){
			return (float)0;
		}else{
			return (numerator/denominator);
		}
		/*
		float denominator1 = (float)0;
		float denominator2 = (float)0;
		for(Entry<Integer,Float> e : center1.entrySet()){
			denominator1 += e.getValue() * e.getValue();
		}
		denominator1 = (float) Math.sqrt((double)denominator1);
		for(Entry<Integer,Float> e : center2.entrySet()){
			denominator2 += e.getValue() * e.getValue();
		}
		denominator2 = (float) Math.sqrt((double)denominator2);
		denominator = denominator1 * denominator2;
		
		if(denominator == 0){
			return (float)0;
		}else{
			return (numerator/denominator);
		}*/
	}
	
}
