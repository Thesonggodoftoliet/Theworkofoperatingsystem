package Work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JSother {
    private List<Job1> job = new ArrayList<Job1>();
    private  List<Job1> job_result = new ArrayList<>();//结果保存
    private  List<Job1> input_well = new ArrayList<Job1>();  //输入井
    private  List<Job1> memory = new ArrayList<Job1>();//内存
    private List<Date> timer = new ArrayList<>();
    private int size = 2;
    private  Job1 processer;

    public  void initializeJob() throws ParseException {
        SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
        Date d1 = SDF.parse("8:00:00");
        timer.add(d1);
        Job1 job1 = new Job1(d1,120,0,1);
        Date d2 =SDF.parse("8:50:00");
        timer.add(d2);
        Job1 job2 = new Job1(d2,50,0,2);
        Date d3 = SDF.parse("9:00:00");
        timer.add(d3);
        Job1 job3 = new Job1(d3,10,0,3);
        Date d4 = SDF.parse("9:50:00");
        timer.add(d4);
        Job1 job4 = new Job1(d4,20,0,4);
        job.add(job1);
        job.add(job2);
        job.add(job3);
        job.add(job4);
        System.out.println("加载完毕");
    }

    public void SJF(){
        int tag = 1;
        double totruntime = 0,totruntime_right = 0;
        processer = job.get(0);
        for (int i = 1;i<timer.size();i++){
            //所有的处理都发生在某一刻
            Date nowtime = timer.get(i);
            for (int j =1;j<job.size();j++){
                if (job.get(i).getEntertime().equals(nowtime))
                    input_well.add(job.get(i));
            }

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

            //处理作业
            System.out.println("开始处理作业"+processer.getNum());
            //开始时间
            if (job_result.size() == 0 || job_result.get(job_result.size()-1).getEndtime().before(processer.getEntertime())){
                processer.setBegintime(nowtime);
            }
            else {
                if (processer.getTag() ==1)//如果是被中断过的，记录重新开始的时间，以计算真实结束时间
                    processer.setBeginagain(job_result.get(job_result.size()-1).getEndtime());
                else processer.setBegintime(job_result.get(job_result.size()-1).getEndtime());
            }
            System.out.println("开始时间"+processer.getBegintime());

            //结束时间
            if (processer.getRuntime_es()<(nowtime.getTime()-processer.getBegintime().getTime())/(60*1000)){
                Date td =new Date();
                td.setTime(processer.getBegintime().getTime());
                td.setHours((int) (td.getHours()+processer.getRuntime_es()/60));
                td.setMinutes((int) (td.getMinutes()+processer.getRuntime_es()%60));
                processer.setEndtime(td);
                processer.setTag(0);//处理完毕
                System.out.println("处理完毕");
            }
            else if (processer.getRuntime_es()==(nowtime.getTime()-processer.getBegintime().getTime())/(60*1000)) {
                processer.setEndtime(nowtime);
                processer.setTag(0);
            }
            else {//在其他任务进入时，还没有处理完
                int runningtime = (int) (nowtime.getTime()-processer.getBegintime().getTime()/(60*1000));
                processer.setLefttime(processer.getRuntime_es()-runningtime);//保留中断现场
                processer.setTag(1);
                memory.add(processer);
            }

            //计算运行时间
            if (processer.getTag()==0) {

                int time;
                time = processer.getEndtime().getMinutes() - processer.getEntertime().getMinutes();
                if (time < 0) {
                    time = time + 60;
                    time += (processer.getEndtime().getHours() - processer.getEntertime().getHours() - 1) * 60;
                } else {
                    time += (processer.getEndtime().getHours() - processer.getEntertime().getHours()) * 60;
                }
                processer.setRuntime(time);
                totruntime += time;

                //计算带权周转时间
                double temptime;
                temptime = processer.getRuntime()/processer.getRuntime_es();
                processer.setRuntime_right(temptime);
                totruntime_right+=temptime;

                job_result.add(processer);
            }

            //加入内存
            while (memory.size()<size && !input_well.isEmpty()){
                memory.add(input_well.remove(0));
            }

            //抢夺CPU，最短处理时间优先
            if (memory.size() ==1 ){
                processer = memory.remove(0);
            }
            else {
                processer = memory.remove(0);
                Job1 temp = new Job1();
                for (int m = 1;m<memory.size();i++){
                    if (processer.getLefttime() > memory.get(m).getLefttime())
                        temp = memory.get(m);//找到运行时间最短的
                }
                memory.remove(temp);

            }

        }
        for (int i = 0;i<job_result.size();i++){
            System.out.println(job_result.get(i).getBegintime()+" "+job_result.get(i).getEndtime());
        }
    }

    public static void main(String args[]) throws ParseException {
        JSother jSother = new JSother();
        jSother.initializeJob();
        jSother.SJF();

    }
}
