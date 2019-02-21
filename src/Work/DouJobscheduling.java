/**
 * 
 */
package Work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author 李煜峰
 *
 * date:2018年10月9日 time:下午3:56:12
 */
public class DouJobscheduling {
	
	private  List<Job1> job = new ArrayList<Job1>();
	private  List<Job1> job_FCFS =new ArrayList<Job1>();
	private  List<Job1> job_SJF = new ArrayList<Job1>();
	private  List<Job1> job_HRN = new ArrayList<Job1>();
	private  List<Job1> job_result = new ArrayList<>();//结果保存
	private  List<Job1> input_well = new ArrayList<Job1>();  //输入井
	private  List<Job1> memory = new ArrayList<Job1>();//内存
	private  Job1 processer;

	/**
	 * 
	 */
	public DouJobscheduling() {
		// TODO Auto-generated constructor stub
	}
	
	public  void initializeJob() throws ParseException{
		SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
		Date d1 = SDF.parse("8:00:00");
		Job1 job1 = new Job1(d1,120,0,1);
		Date d2 =SDF.parse("8:50:00");
		Job1 job2 = new Job1(d2,50,0,2);
		Date d3 = SDF.parse("9:00:00");
		Job1 job3 = new Job1(d3,10,0,3);
		Date d4 = SDF.parse("9:50:00");
		Job1 job4 = new Job1(d4,20,0,4);
		job.add(job1);
		job.add(job2);
		job.add(job3);
		job.add(job4);
		
		for(int i = 0;i < job.size();i++){
			job_FCFS.add(job.get(i));
			job_HRN.add(job.get(i));
			job_SJF.add(job.get(i));
		}
		
		
	}

	public void addintoWell(List<Job1> temp){
		if (input_well.isEmpty()){
			input_well.add(temp.get(0));
		}
		else{
			for (int i = 0;i<temp.size();i++){
				if (true) ;
			}
		}

	}

	public void addintoMememry(){

	}
	@SuppressWarnings("deprecation")
	public  void FCFS(){
		
		double totruntime = 0,totruntime_right = 0;
		Collections.sort(job_FCFS,new Comparator<Job1>(){

			@Override
			public int compare(Job1 arg0, Job1 arg1) {
				// TODO Auto-generated method stub
				if(arg0.getEntertime().after(arg1.getEntertime())) return 1;
				else if(arg0.getEntertime().equals(arg1.getEntertime()))return 0;
				else return -1;
			}
	
		});
		
		input_well.add(job_FCFS.get(0));//将第一个到达的作业加入输入井
		job_FCFS.get(0).setTag(1);
		for(int i = 0;i<job_FCFS.size();i++){
			System.out.print(job_FCFS.get(i).getEntertime());
			System.out.println(job_FCFS.get(i).getRuntime_es());
		}
		
		int tag = 0; //CPU正在处理第几个作业
		while(!input_well.isEmpty()){
			
			Job1 processor = new Job1(input_well.get(0).getEntertime(),input_well.get(0).getRuntime_es(),input_well.get(0).getTag(),input_well.get(0).getNum());//CPU总是提取输入井的第一个作业
			if(memory.size()<=2) memory.add(processor);
			tag++;
			 
			//计算开始时间
			if(tag == 1){//第一个进入内存
				processor.setBegintime(processor.getEntertime());
			}
			else {
				if(job_FCFS.get((tag-1)-1).getEndtime().after(processor.getEntertime()))//如果该作业的进入时间在上一个作业的结束时间之前
					processor.setBegintime(job_FCFS.get((tag-1)-1).getEndtime());
				else{
					processor.setBegintime(processor.getEntertime());
				}
			}
			
			
			//计算结束时间
			Date td =new Date();
			td.setTime(processor.getBegintime().getTime());
			td.setHours((int) (td.getHours()+processor.getRuntime_es()/60));
			td.setMinutes((int) (td.getMinutes()+processor.getRuntime_es()%60));
			processor.setEndtime(td); 
			
			
			//计算周转时间
			int time;
			time =processor.getEndtime().getMinutes()-processor.getEntertime().getMinutes();
			if(time <0){
				time = time+60;
				time += (processor.getEndtime().getHours()-processor.getEntertime().getHours()-1)*60;
			}
			else{
				time += (processor.getEndtime().getHours()-processor.getEntertime().getHours())*60;
			}
			processor.setRuntime(time);
			totruntime+=time;
			
			//计算带权周转时间
			double temptime;
			temptime = processor.getRuntime()/processor.getRuntime_es();
			processor.setRuntime_right(temptime);
			totruntime_right+=temptime;
			
			//将结果保存
			job_FCFS.remove(tag-1);
			job_FCFS.add(tag-1, processor);
			
			//遍历作业将进入时间在正在运行的作业的结束时间之前的作业加入输入井
			for(int i = tag;i<job_FCFS.size();i++){
				if(job_FCFS.get(i).getEntertime().before(processor.getEndtime())&&job_FCFS.get(i).getTag()!=1){
					job_FCFS.get(i).setTag(1);
					input_well.add(job_FCFS.get(i));
				}
					
			}
			
			input_well.remove(0);
			
		}
		
		Job1 job_t = new Job1();
		job_t.setRuntime(totruntime/job_FCFS.size());
		job_t.setRuntime_right(totruntime_right/job_FCFS.size());
		job_FCFS.add(job.size(),job_t);
		
		for(int i = 0;i<job_FCFS.size()-1;i++){
			System.out.print(job_FCFS.get(i).getEntertime());
			System.out.print(job_FCFS.get(i).getRuntime_es());
			System.out.print(job_FCFS.get(i).getBegintime().toString()+job_FCFS.get(i).getEndtime().toString());
			System.out.println(job_FCFS.get(i).getRuntime()+" "+job_FCFS.get(i).getRuntime_right());
		}
		input_well.clear();
		
	}
	
	public void SJF(){
		
		double totruntime = 0,totruntime_right = 0;
		
		Collections.sort(job_SJF,new Comparator<Job1>(){

			@Override
			public int compare(Job1 arg0, Job1 arg1) {
				// TODO Auto-generated method stub
				if(arg0.getEntertime().after(arg1.getEntertime())) return 1;
				else if(arg0.getEntertime().equals(arg1.getEntertime()))return 0;
				else return -1;
			}
	
		});
		
		input_well.add(job_SJF.get(0));
		job_SJF.get(0).setTag(1);
		for(int i = 0;i<job_SJF.size();i++){
		System.out.print(job_SJF.get(i).getEntertime());
		System.out.println(job_SJF.get(i).getRuntime_es());
		}
		
		int tag = 0; //内存正在处理第几个作业
		while(!input_well.isEmpty()){
			Job1 processor = new Job1(input_well.get(0).getEntertime(),input_well.get(0).getRuntime_es(),input_well.get(0).getTag(),input_well.get(0).getNum());//内存总是提取输入井的第一个作业
			tag++;
			
			//计算开始时间
			if(tag == 1){//第一个进入内存
				processor.setBegintime(processor.getEntertime());
			}
			else {
				if(job_SJF.get((tag-1)-1).getEndtime().after(processor.getEntertime()))//如果该作业的进入时间在上一个作业的结束时间之前
					processor.setBegintime(job_SJF.get((tag-1)-1).getEndtime());
				else{
					processor.setBegintime(processor.getEntertime());
				}
			}
			
			
			//计算结束时间
			Date td =new Date();
			td.setTime(processor.getBegintime().getTime());
			td.setHours((int) (td.getHours()+processor.getRuntime_es()/60));
			td.setMinutes((int) (td.getMinutes()+processor.getRuntime_es()%60));
			processor.setEndtime(td); 
			
			
			//计算周转时间
			int time;
			time =processor.getEndtime().getMinutes()-processor.getEntertime().getMinutes();
			if(time <0){
				time = time+60;
				time += (processor.getEndtime().getHours()-processor.getEntertime().getHours()-1)*60;
			}
			else{
				time += (processor.getEndtime().getHours()-processor.getEntertime().getHours())*60;
			}
			processor.setRuntime(time);
			totruntime+=time;
			
			//计算带权周转时间
			double temptime;
			temptime = processor.getRuntime()/processor.getRuntime_es();
			processor.setRuntime_right(temptime);
			totruntime_right+=temptime;
			
			//将结果保存
			job_SJF.remove(tag-1);
			job_SJF.add(tag-1, processor);
			
			//遍历作业将进入时间在正在运行的作业的结束时间之前的作业加入输入井
			for(int i = tag;i<job_SJF.size();i++){
				if(job_SJF.get(i).getEntertime().before(processor.getEndtime())&&job_SJF.get(i).getTag()!=1){
					job_SJF.get(i).setTag(1);
					input_well.add(job_SJF.get(i));
				}
					
			}
			
			input_well.remove(0);
			Collections.sort(input_well,new Comparator<Job1>(){

				@Override
				public int compare(Job1 arg0, Job1 arg1) {
					// TODO Auto-generated method stub
					double i = arg0.getRuntime_es() - arg1.getRuntime_es();
					if(i>0) return 1;
					else if(i == 0) return 0;
					else return -1;
				}
		
			});
			
			
		}
		
		Job1 job_t = new Job1();
		job_t.setRuntime(totruntime/job_SJF.size());
		job_t.setRuntime_right(totruntime_right/job_SJF.size());
		job_SJF.add(job.size(),job_t);
		
		for(int i = 0;i<job_SJF.size()-1;i++){
		System.out.print(job_SJF.get(i).getEntertime());
		System.out.print(job_SJF.get(i).getRuntime_es());
		System.out.print(job_SJF.get(i).getBegintime().toString()+job_SJF.get(i).getEndtime().toString());
		System.out.println(job_SJF.get(i).getRuntime()+" "+job_SJF.get(i).getRuntime_right());
		}
		
		input_well.clear();
	}
	
	public void HRN(){
		double totruntime = 0,totruntime_right = 0;
		
		Collections.sort(job_HRN,new Comparator<Job1>(){

			@Override
			public int compare(Job1 arg0, Job1 arg1) {
				// TODO Auto-generated method stub
				if(arg0.getEntertime().after(arg1.getEntertime())) return 1;
				else if(arg0.getEntertime().equals(arg1.getEntertime()))return 0;
				else return -1;
			}
	
		});
		
		input_well.add(job_HRN.get(0));
		job_HRN.get(0).setTag(1);
		for(int i = 0;i<job_HRN.size();i++){
		System.out.print(job_HRN.get(i).getEntertime());
		System.out.println(job_HRN.get(i).getRuntime_es());
		}
		
		int tag = 0; //内存正在处理第几个作业
		while(!input_well.isEmpty()){
			Job1 processor = new Job1(input_well.get(0).getEntertime(),input_well.get(0).getRuntime_es(),input_well.get(0).getTag(),input_well.get(0).getNum());//内存总是提取输入井的第一个作业
			tag++;
			
			//计算开始时间
			if(tag == 1){//第一个进入内存
				processor.setBegintime(processor.getEntertime());
			}
			else {
				if(job_HRN.get((tag-1)-1).getEndtime().after(processor.getEntertime()))//如果该作业的进入时间在上一个作业的结束时间之前
					processor.setBegintime(job_HRN.get((tag-1)-1).getEndtime());
				else{
					processor.setBegintime(processor.getEntertime());
				}
			}
			
			
			//计算结束时间
			Date td =new Date();
			td.setTime(processor.getBegintime().getTime());
			td.setHours((int) (td.getHours()+processor.getRuntime_es()/60));
			td.setMinutes((int) (td.getMinutes()+processor.getRuntime_es()%60));
			processor.setEndtime(td); 
			
			
			//计算周转时间
			int time;
			time =processor.getEndtime().getMinutes()-processor.getEntertime().getMinutes();
			if(time <0){
				time = time+60;
				time += (processor.getEndtime().getHours()-processor.getEntertime().getHours()-1)*60;
			}
			else{
				time += (processor.getEndtime().getHours()-processor.getEntertime().getHours())*60;
			}
			processor.setRuntime(time);
			totruntime+=time;
			
			//计算带权周转时间
			double temptime;
			temptime = processor.getRuntime()/processor.getRuntime_es();
			processor.setRuntime_right(temptime);
			totruntime_right+=temptime;
			
			//将结果保存
			job_HRN.remove(tag-1);
			job_HRN.add(tag-1, processor);
			
			//遍历作业将进入时间在正在运行的作业的结束时间之前的作业加入输入井
			for(int i = tag;i<job_HRN.size();i++){
				if(job_HRN.get(i).getEntertime().before(processor.getEndtime())&&job_HRN.get(i).getTag()!=1){
					job_HRN.get(i).setTag(1);
					input_well.add(job_HRN.get(i));
				}
					
			}
			
			input_well.remove(0);
			
			//计算响应比
			for(int i = 0;i<input_well.size();i++){
				double wait_time;
				wait_time=job_HRN.get(tag-1).getEndtime().getMinutes()-input_well.get(i).getEntertime().getMinutes();
				if(time <0){
					time = time+60;
					time += (job_HRN.get(tag-1).getEndtime().getHours()-input_well.get(i).getEntertime().getHours()-1)*60;
				}
				else{
					time += (job_HRN.get(tag-1).getEndtime().getHours()-input_well.get(i).getEntertime().getHours())*60;
				}
				input_well.get(i).setResponse_ratio(wait_time/input_well.get(i).getRuntime_es()+1);
			}
			
			Collections.sort(input_well,new Comparator<Job1>(){

				@Override
				public int compare(Job1 arg0, Job1 arg1) {
					// TODO Auto-generated method stub
					double i = arg0.getResponse_ratio() - arg1.getResponse_ratio();
					if(i>0) return -1;
					else if(i == 0) return 0;
					else return 1;
				}
		
			});
			
			
		}
		
		Job1 job_t = new Job1();
		job_t.setRuntime(totruntime/job_HRN.size());
		job_t.setRuntime_right(totruntime_right/job_HRN.size());
		job_HRN.add(job.size(),job_t);
		
		for(int i = 0;i<job_HRN.size()-1;i++){
		System.out.print(job_HRN.get(i).getEntertime());
		System.out.print(job_HRN.get(i).getRuntime_es());
		System.out.print(job_HRN.get(i).getBegintime().toString()+job_HRN.get(i).getEndtime().toString());
		System.out.println(job_HRN.get(i).getRuntime()+" "+job_HRN.get(i).getRuntime_right());
		}
		
		input_well.clear();
	}


	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		DouJobscheduling dj = new DouJobscheduling();
		dj.initializeJob();
		dj.HRN();;

	}

}
