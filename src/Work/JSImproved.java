package Work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSImproved {
    private List<Job1> job =new ArrayList<>();//作业列表
    private List<Job1> job_SJF = new ArrayList<>();
    private List<Job1> input_well = new ArrayList<>();
    private List<Job1> memory = new ArrayList<>();
    private List<Job1> result = new ArrayList<>();
    private Job1 CPU;
    private int size =1;

    public  void initializeJob() throws ParseException {
        SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
        Date d1 = SDF.parse("10:00:00");
        Job1 job1 = new Job1(d1,60,0,1);
        Date d2 =SDF.parse("10:10:00");
        Job1 job2 = new Job1(d2,60,0,2);
        Date d3 = SDF.parse("10:25:00");
        Job1 job3 = new Job1(d3,15,0,3);
        job.add(job1);
        job.add(job2);
        job.add(job3);

        for(int i = 0;i < job.size();i++){
            //job_FCFS.add(job.get(i));
            //job_HRN.add(job.get(i));
            job_SJF.add(job.get(i));
        }

    }

    public void SJF(){
        double totruntime  =0,totruntime_right =0;
        //按照时间顺序排序
        Collections.sort(job_SJF,new Comparator<Job1>(){
            @Override
            public int compare(Job1 arg0, Job1 arg1) {
                // TODO Auto-generated method stub
                if(arg0.getEntertime().after(arg1.getEntertime())) return 1;
                else if(arg0.getEntertime().equals(arg1.getEntertime()))return 0;
                else return -1;
            }
        });

        int tag = 1;//用来记录是否前面有任务
        while(!job_SJF.isEmpty()){//遍历整个列表

           if (input_well.isEmpty()) {//如果输入井为空
               input_well.add(job_SJF.remove(0));
               tag = 0;
           }

           while (!input_well.isEmpty()) {//遍历输入井
               //加入内存
               while (memory.size() < size && !input_well.isEmpty())
                   memory.add(input_well.remove(0));

               if (memory.size() == 1 )
                   CPU = memory.remove(0);
               else {
                   //在内存中的最短时间抢占CPU
                   CPU = memory.get(0);
                   for (int i = 1; i < memory.size(); i++)
                       if (CPU.getRuntime_es() > memory.get(i).getRuntime_es())
                           CPU = memory.get(i);
                   memory.remove(CPU);
               }


               if (tag!=0) {//该任务前面有任务已完成，重新计算是否抢占CPU
                   int index = result.size() - 1;
                   int runningtime = (int) ((CPU.getEntertime().getTime() - result.get(index).getBegintime().getTime()) / (60 * 1000));
                   result.get(index).setLefttime(result.get(index).getLefttime() - runningtime);//更改估计运行时间
                   if (result.get(index).getLefttime() > CPU.getLefttime()) {
                       result.get(index).setTag(1);//设置中断标志
                       memory.add(result.remove(index));//重新计算
                       tag = 0;
                   }
               }

               //开始时间
               if (tag == 0)
                   CPU.setBegintime(CPU.getEntertime());
               else if (CPU.getTag()==1){}
               else {
                   //如果上一个任务的结束时间在正在处理的任务的进入时间之后
                   int index = result.size()-1;
                   if (result.get(index).getEndtime().after(CPU.getEntertime()))
                       CPU.setBegintime(result.get(index).getEndtime());
                   else CPU.setBegintime(CPU.getEntertime());
               }

               //结束时间
               if (CPU.getTag() ==1){
                   int index = result.size() - 1;
                   Date td = new Date();
                   td.setTime(CPU.getBegintime().getTime());
                   td.setHours((int) (td.getHours() + (CPU.getRuntime_es()+result.get(index).getRuntime_es())/ 60));
                   td.setMinutes((int) (td.getMinutes() + (CPU.getRuntime_es()+result.get(index).getRuntime_es()) % 60));
                   CPU.setEndtime(td);
               }
               else {
                   Date td = new Date();
                   td.setTime(CPU.getBegintime().getTime());
                   td.setHours((int) (td.getHours() + CPU.getRuntime_es() / 60));
                   td.setMinutes((int) (td.getMinutes() + CPU.getRuntime_es() % 60));
                   CPU.setEndtime(td);
               }

               //周转时间
               long time;
               time = (CPU.getEndtime().getTime() - CPU.getEntertime().getTime()) / (1000 * 60);
               CPU.setRuntime(time);
               totruntime += time;

               //带权周转时间
               double temptime;
               temptime = CPU.getRuntime() / CPU.getRuntime_es();
               CPU.setRuntime_es(temptime);
               totruntime_right += temptime;

               //保存结果
               result.add(CPU);

               //遍历作业将进入时间在正在运行的作业的结束时间之前的作业加入输入井
               int i = 0;
                while (i<job_SJF.size()){
                    if (job_SJF.get(i).getEntertime().before(CPU.getEndtime())){
                        input_well.add(job_SJF.remove(i));
                        tag = 1;
                    }
                    else i++;
                }
                //按照时间先后顺序排序
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
        }
        Job1 job1 = new Job1();
        job1.setRuntime(totruntime/result.size());
        job1.setRuntime_right(totruntime_right/result.size());
        result.add(job1);
    }

    public static void main(String args[]){

    }
}
