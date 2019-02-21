package Work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;

public class Scheduling {
    private List<Job1> job = new ArrayList<>();//作业列表
    private List<Job1> job_FCFS = new ArrayList<>();
    private List<Job1> input_well = new ArrayList<>();

    public Scheduling(){

    }

    public void initJoblist() throws ParseException {
        SimpleDateFormat SDF= new SimpleDateFormat("HH:mm:ss");
        Date d1 = SDF.parse("10:00:00");
        Job1 job1 = new Job1(d1,60,0,1);
        Date d2 =SDF.parse("10:10:00");
        Job1 job2 = new Job1(d2,60,0,2);
        Date d3 = SDF.parse("10:25:00");
        Job1 job3 = new Job1(d3,15,0,3);
        job.add(job1);
        job.add(job2);
        job.add(job3);
    }

    public List<Job1> FCFS(){
        job_FCFS.addAll(job);
        List<Job1> result = new ArrayList<>();//保存结果

        double totruntime = 0,totruntime_right = 0;
        Collections.sort(job_FCFS,new Comparator<Job1>(){//将输入按照时间排序

            @Override
            public int compare(Job1 arg0, Job1 arg1) {
                // TODO Auto-generated method stub
                if(arg0.getEntertime().after(arg1.getEntertime())) return 1;
                else if(arg0.getEntertime().equals(arg1.getEntertime()))return 0;
                else return -1;
            }
        });
        while(!job_FCFS.isEmpty()) {
            input_well.add(job_FCFS.remove(0));
            input_well.get(0).setTag(1);//第一个进入输入井

            while (!input_well.isEmpty()) {
                Job1 processor = new Job1(input_well.get(0).getEntertime(), input_well.get(0).getRuntime_es(), input_well.get(0).getTag(),input_well.get(0).getNum());

                //计算开始时间
                if (processor.getTag() == 1) {
                    processor.setBegintime(processor.getEntertime());
                } else {
                    if (result.get(result.size() - 1).getEndtime().after(processor.getEntertime()))//上一个作业的结束时间比正在处理的作业的进入时间晚
                        processor.setBegintime(result.get(result.size() - 1).getEndtime());
                    else processor.setBegintime(processor.getEntertime());
                }

                //计算结束时间
                Date td = new Date();
                td.setTime(processor.getBegintime().getTime());
                td.setHours((int) (td.getHours() + processor.getRuntime_es() / 60));
                td.setMinutes((int) (td.getMinutes() + processor.getRuntime_es() % 60));
                processor.setEndtime(td);

                //计算周转时间
                int time;
                time = processor.getEndtime().getMinutes() - processor.getEntertime().getMinutes();
                if (time < 0) {
                    time = time + 60;
                    time += (processor.getEndtime().getHours() - processor.getEntertime().getHours() - 1) * 60;
                } else time += (processor.getEndtime().getHours() - processor.getEntertime().getHours()) * 60;
                processor.setRuntime(time);
                totruntime += time;

                //计算带权周转时间
                double time_right = processor.getRuntime() / processor.getRuntime_es();
                processor.setRuntime_right(time_right);
                totruntime_right += time_right;

                //保存结果
                result.add(processor);

                //遍历作业将进入时间在正在运行的作业的结束时间之前的作业加入输入井
                int temp = job_FCFS.size();
                for(int i = 0;i<temp;i++){
                    if(job_FCFS.get(0).getEntertime().before(processor.getEndtime()))
                        input_well.add(job_FCFS.remove(0));
                }

                input_well.remove(0);
            }
        }
        return result;
    }

    public static void main(String args[]) throws ParseException {
        Scheduling ppp = new Scheduling();
        ppp.initJoblist();
        List<Job1> temp= ppp.FCFS();
        for(int i = 0;i<temp.size()-1;i++){
            System.out.println("结果大小"+temp.size());
            System.out.print("进入时间"+temp.get(i).getEntertime());
            System.out.print(" 估计运行时间"+temp.get(i).getRuntime_es());
            System.out.print(" 开始时间和结束时间"+temp.get(i).getBegintime().toString()+temp.get(i).getEndtime().toString());
            System.out.println(" 周转时间和带权周转时间"+temp.get(i).getRuntime()+" "+temp.get(i).getRuntime_right());
        }

        System.out.println(" 平均周转时间和平均带权时间"+temp.get(temp.size()-1).getRuntime()+temp.get(temp.size()-1).getRuntime_right());

    }
}


