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
		if  (!file2 .isDirectory())     //��������ڣ������ļ���
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
	
	//ͳ������
	public List<String> statisticData(){
		int records = 0;                //ѵ�����м�¼
		int users = 0;                  //�û���
		int items = 0;                  //�������ֵ�item
		HashMap<Integer,Integer> itemList = new HashMap<Integer, Integer>(); 
		int itemsnotzero = 0;           //�з�0���ֵ�item��,����Ԥ���item
		HashMap<Integer,Integer> itemListnot0 = new HashMap<Integer, Integer>(); 
		int minratings = 0;             //�������ֵ
		int maxratings = 0;             //�������ֵ
		int testUsers = 0;              //���Լ��е��û���
		int testItems = 0;              //���Լ��е�item��
		HashMap<Integer,Integer> testitemlist = new HashMap<Integer, Integer>();
		int testrecords = 0;            //���Լ��еļ�¼��������Ҫ���Ƶ�������
		String position = "./Result/"; //д����̵��ļ���λ��
		File file2 =new File(position); 
		if  (!file2 .isDirectory())      
		{       
		    System.out.println(position+"������");  
		    file2 .mkdirs();    
		    System.out.println(position+"�������");
		} else   
		{  
		    System.out.println(position+"·������");  
		} 
		
		long startTime3 = System.currentTimeMillis(); // ��ȡ��ʼʱ��

		FileInputStream ft = null;
		InputStreamReader ist = null;
		BufferedReader brt = null; // ���ڷ�װInputStreamReader,��ߴ�������
		try {
			String Line = "";
			int user;              // ��¼user
			int ratingNum;         // ��¼user��ͶƱ����������ratingNum�н���¼(��ͳ�Ƶõ�)��user��ͶƱ
			ft = new FileInputStream("./data/train.txt");
			                       // ���ļ�ϵͳ�е�ĳ���ļ��л�ȡ�ֽ�
			ist = new InputStreamReader(ft);// InputStreamReader
											// ���ֽ���ͨ���ַ���������,
			brt = new BufferedReader(ist);  // ���ַ��������ж�ȡ�ļ��е�����,��װ��һ��new
											// InputStreamReader�Ķ���
			Line = brt.readLine();
			user = Integer.valueOf(Line.split("\\|")[0]);
			users++;               //��ǰΪ1
			ratingNum = Integer.valueOf(Line.split("\\|")[1]);
		    //System.out.println(Line.split("\\|")[1]);  //��41
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
			} // �������ļ����µ�����ͳ��ͬʱ��ɡ�
		} catch (FileNotFoundException e) {
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {
				brt.close();
				ist.close();
				ft.close();
				// �رյ�ʱ����ð����Ⱥ�˳��ر���󿪵��ȹر�
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileInputStream ftest = null;
		InputStreamReader istest = null;
		BufferedReader brtest = null; // ���ڰ�װInputStreamReader,��ߴ������ܡ�
		try {
			String Line = "";
			int user;// ��¼user
			int ratingNum;// ��¼user��ͶƱ����������ratingNum�н���¼(��ͳ�Ƶõ�)��user��ͶƱ
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
			} // �������ļ����µ�����ͳ��ͬʱ��ɡ�
		} catch (FileNotFoundException e) {
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {
				brtest.close();
				istest.close();
				ftest.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		long endTime3 = System.currentTimeMillis(); // ��ȡ����ʱ��
		System.out.println("���Լ�ͳ�����");
		System.out.println("ѵ�������û�����"+users+"��");
		System.out.println("ѵ���������ּ�¼����"+records+"��");
		System.out.println("ѵ������item����"+items+"��");
		System.out.println("ѵ���������ַ�0��item����"+itemsnotzero+"��");
		System.out.println("ѵ��������ҪԤ������item����"+testrecords+"��");
		System.out.println("���Լ����û�����"+testUsers+"��");
		System.out.println("���Լ���item����"+testItems+"��");
		System.out.println("��������ʱ��:" + (endTime3 - startTime3) + "ms");
		

		List<String> result = new ArrayList<String>();
		result.add("ѵ�������û�����"+users+"��");
		result.add("ѵ���������ּ�¼����"+records+"��");
		result.add("ѵ������item����"+items+"��");
		result.add("ѵ���������ַ�0��item����"+itemsnotzero+"��");
		result.add("ѵ��������ҪԤ������item����"+testrecords+"��");
		result.add("���Լ����û�����"+testUsers+"��");
		result.add("���Լ���item����"+testItems+"��");
		result.add("ͳ�ƺ�ʱ:"+(endTime3 - startTime3) + "ms");
		return result;
	}

	//��ѵ�������ݺͲ��Լ����ݽ��д���
	public HashMap<Integer,HashMap<Integer,Integer>> dealData(){
		String position = "./Result/";//д����̵��ļ���λ��
		File file2 =new File(position); 
		if  (!file2 .isDirectory())     //�����Ҫ������·�� 
		{       
		    //System.out.println(position+"������");  
		    file2 .mkdirs();    
		    //System.out.println(position+"�������");
		} else   
		{  
		    //System.out.println(position+"����");  
		}  

		
		HashMap<Integer,ArrayList<Integer>> testSet = new HashMap<Integer, ArrayList<Integer>>();//user-items-ratings ��¼M������Ϣ��ѵ��U
		long startTime3 = System.currentTimeMillis(); // ��ȡ��ʼʱ��

		FileInputStream ftest = null;
		InputStreamReader istest = null;
		BufferedReader brtest = null; // ���ڰ�װInputStreamReader,��ߴ������ܡ�
		try {
			String Line = "";
			
			int user;// ��¼user
			int ratingNum;// ��¼user��ͶƱ����������ratingNum�н���¼(��ͳ�Ƶõ�)��user��ͶƱ
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
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {
				brtest.close();
				istest.close();
				ftest.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime3 = System.currentTimeMillis(); // ����ʱ��
		//�����Լ�Ԥ����������д���ļ�
		long startTime5 = System.currentTimeMillis(); // ��ȡ��ʼʱ��
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
		long endTime5 = System.currentTimeMillis(); // ��ȡ����ʱ��
		//System.out.println("�ļ�д����ɣ�" + "��ʱ" + (endTime5 - startTime5) + "ms");
		testSet.clear();
		
		HashMap<Integer,HashMap<Integer,Integer>> UserItems = new HashMap<Integer, HashMap<Integer, Integer>>();//user-items-ratings ��¼M������Ϣ��ѵ��U
		long startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��

		FileInputStream fFromNode = null;
		InputStreamReader isFromNode = null;
		BufferedReader brFromNode = null; // ���ڰ�װInputStreamReader,��ߴ������ܡ�
		try {
			String Line = "";
			
			int user;// ��¼user
			int ratingNum;// ��¼user��ͶƱ����������ratingNum�н���¼(��ͳ�Ƶõ�)��user��ͶƱ
			fFromNode = new FileInputStream("./data/train.txt");
			// ���ļ�ϵͳ�е�ĳ���ļ��л�ȡ�ֽ�
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
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {
				brFromNode.close();
				isFromNode.close();
				fFromNode.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis(); // ��ȡ����ʱ��
		//System.out.println("user-itemsͳ�����");
		//System.out.println("ͳ�Ƶõ�"+UserItems.size());
		//System.out.println("��������ʱ��:" + (endTime - startTime) + "ms");
		
		//д���ļ�
		long startTime6 = System.currentTimeMillis(); // ��ȡ��ʼʱ��
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
		long endTime6 = System.currentTimeMillis(); // ��ȡ����ʱ��
		//System.out.println("��trainͳ����д���ļ�");
		//System.out.println("д��ʱ��:" + (endTime6 - startTime6) + "ms");
		UserItems.clear();		
		
		//2.2ͳ��item-usersRatings
		HashMap<Integer,HashMap<Integer,Integer>> ItemUsers = new HashMap<Integer, HashMap<Integer, Integer>>();//��¼M������Ϣ��ѵ��V
		long startTime2 = System.currentTimeMillis(); // ��ȡ��ʼʱ��

		FileInputStream fFromNode2 = null;
		InputStreamReader isFromNode2 = null;
		BufferedReader brFromNode2 = null; // ���ڰ�װInputStreamReader
		try {
			String Line = "";
		
			int user;// ��¼user
			int ratingNum;// ��¼user��ͶƱ����������ratingNum�н���¼(��ͳ�Ƶõ�)��user��ͶƱ
			fFromNode2 = new FileInputStream("./data/train.txt");
			// ���ļ�ϵͳ�е�ĳ���ļ��л�ȡ�ֽ�
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
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
		} finally {
			try {
				brFromNode2.close();
				isFromNode2.close();
				fFromNode2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long endTime2 = System.currentTimeMillis(); // ��ȡ����ʱ��
		//System.out.println("item-usersRatingͳ�����");
		//System.out.println("ͳ�Ƶõ�"+ItemUsers.size()+"��");
		//System.out.println("��������ʱ��:" + (endTime2 - startTime2) + "ms");
		return ItemUsers;
	}
	
	//��ȡѵ�������û������ֵ�item
	public static HashMap<Integer,Integer> getOverratedItems(Integer user){
		HashMap<Integer,Integer> candidateItems = new HashMap<Integer, Integer>();
		FileInputStream fcandi = null;
		InputStreamReader iscandi = null;
		BufferedReader brcandi = null; // ���ڰ�װInputStreamReader,��ߴ������ܡ�
		try {
			String Line = "";
			fcandi = new FileInputStream("./Result/Users_Items.txt");
			// ���ļ�ϵͳ�е�ĳ���ļ��л�ȡ�ֽ�
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
			System.out.println("�Ҳ���ָ���ļ�");
		} catch (IOException e) {
			System.out.println("��ȡ�ļ�ʧ��");
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
	
	//��ȡԤ������
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
	
	//��ȡ��ҪԤ���item����֪item�����ƶ�
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
		//�����Ļ�������м���
		float numerator = (float)0;//����
		float denominator = (float)0;//��ĸ
		for(Entry<Integer,Float> e : center1.entrySet()){
			Integer tempUser = e.getKey();
			if(center2.containsKey(tempUser)){//forѭ���������õ�����
				numerator += center1.get(tempUser) * center2.get(tempUser);
			}
		}
		
		float denominator1 = (float)0;
		float denominator2 = (float)0;
		for(Entry<Integer,Float> e : center1.entrySet()){
			Integer tempUser = e.getKey();
			if(center2.containsKey(tempUser)){//forѭ���������õ�����
				denominator1 += center1.get(tempUser) * center1.get(tempUser);
			}
		}
		denominator1 = (float) Math.sqrt((double)denominator1);
		for(Entry<Integer,Float> e : center2.entrySet()){
			Integer tempUser = e.getKey();
			if(center1.containsKey(tempUser)){//forѭ���������õ�����
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
