import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class PageOut {
    private static int Pagesize;
    private static int size;
    private static List<Integer> Page = null;//页
    public static List<Integer> In = null;//输入

    public static void initPage(){
        Page = new LinkedList<>();
    }

    public static void initIn(){
        int data;
        int i = 0;
        In = new ArrayList<>();
        Scanner reader = new Scanner(System.in);
        while(i<size) {
            System.out.print("请输入数据：");
            data = reader.nextInt();
            i++;
            In.add(data);
        }
    }

    public static int FIFO(){
        int Result[][] =new int[Pagesize+1][size];
        int result = 0;
        int i = 0;
        int index;
        while(i<In.size()){
            index = Page.indexOf(In.get(i));
            if(index==-1){//没有找到
                if(Page.size()<Pagesize){
                    Page.add(0,In.get(i));
                    result++;
                }
                else{
                    Page.remove(Page.size()-1);
                    Page.add(0,In.get(i));
                    result++;
                }
                Result[Pagesize][i] = -1;//表示×
            }
            else Result[Pagesize][i] = -2;//表示√

            //保存结果
            for (int m = 0;m< Page.size();m++){
                Result[m][i] = Page.get(m);
            }
            i++;

        }
        for (int tag2=0;tag2<In.size();tag2++)
            System.out.print(In.get(tag2)+" ");
        System.out.println();
        for (int tag1 = 0;tag1<=Pagesize;tag1++){
            for (int tag2=0;tag2<In.size();tag2++){
                if (Result[tag1][tag2] ==0)
                    System.out.print(" "+" ");
                else if (Result[tag1][tag2] == -1)
                    System.out.print("×");
                else if (Result[tag1][tag2] == -2)
                    System.out.print("√");
                else System.out.print(Result[tag1][tag2]+" ");
            }
            System.out.println();
        }
        Page.clear();
        System.out.println("中断"+result+"次");
        return result;
    }

    public static int LRU(){
        int Result[][] =new int[Pagesize+1][size];
        int result = 0;
        int i =0;
        int index;
        while(i<In.size()){
            index = Page.indexOf(In.get(i));
            if(index == -1){
                if(Page.size()<Pagesize){
                    Page.add(0,In.get(i));
                    result++;
                }
                else{
                    Page.remove(Page.size()-1);
                    Page.add(0,In.get(i));
                    result++;
                }
                Result[Pagesize][i] = -1;
            }
            else{
                Page.remove((int)index);//更新到最新的页面
                Page.add(0,In.get(i));
                Result[Pagesize][i] = -2;
            }

            for (int m = 0;m< Page.size();m++){
                Result[m][i] = Page.get(m);
            }
            i++;
        }
        for (int tag2=0;tag2<In.size();tag2++)
            System.out.print(In.get(tag2)+" ");
        System.out.println();
        for (int tag1 = 0;tag1<=Pagesize;tag1++){
            for (int tag2=0;tag2<In.size();tag2++){
                if (Result[tag1][tag2] ==0)
                    System.out.print(" "+" ");
                else if (Result[tag1][tag2] == -1)
                    System.out.print("×");
                else if (Result[tag1][tag2] == -2)
                    System.out.print("√");
                else System.out.print(Result[tag1][tag2]+" ");
            }
            System.out.println();
        }
        System.out.println("中断"+result+"次");
        Page.clear();
        return result;
    }

    public static int OPT(){
        int Result[][] =new int[Pagesize+1][size];
        int result = 0;
        int i = 0;
        int index;
        List<Integer> list1 = new ArrayList<>();
        list1.addAll(In);//用于模拟已知情况

        while(i<In.size()){
            index = Page.indexOf(In.get(i));
            if(index==-1){//没有找到
                if(Page.size()<Pagesize){
                    Page.add(0,In.get(i));
                    result++;
                }
                else{
                    int best=-1;
                    int temp =-1;
                    for (int j = 0;j<Pagesize;j++){
                        temp=list1.indexOf(Page.get(j));
                        if (best<temp && temp!=-1) best = temp;
                        if (temp == -1) {//如果在剩余输入中没有找到，相当于无限远
                            best=j;
                            break;
                        }
                    }
                    if(temp == -1) Page.remove(best);
                    else Page.remove(list1.get(best));//移掉最远的那个元素
                    Page.add(0,In.get(i));
                    result++;
                }
                Result[Pagesize][i] = -1;
            }
            else Result[Pagesize][i] = -2;

            list1.remove(0);

            for (int m = 0;m< Page.size();m++){
                Result[m][i] = Page.get(m);
            }
            i++;
        }
        for (int tag2=0;tag2<In.size();tag2++)
            System.out.print(In.get(tag2)+" ");
        System.out.println();
        for (int tag1 = 0;tag1<=Pagesize;tag1++){
            for (int tag2=0;tag2<In.size();tag2++){
                if (Result[tag1][tag2] ==0)
                    System.out.print(" "+" ");
                else if (Result[tag1][tag2] == -1)
                    System.out.print("×");
                else if (Result[tag1][tag2] == -2)
                    System.out.print("√");
                else System.out.print(Result[tag1][tag2]+" ");
            }
            System.out.println();
        }

        System.out.println("中断"+result+"次");
        Page.clear();
        return result;
    }

    public static void main(String args[]){
        Scanner reader = new Scanner(System.in);
        System.out.println("---------*********页面淘汰算法模拟*********--------------");
        System.out.println();
        System.out.println("请输入页面大小:");
        Pagesize = reader.nextInt();
        System.out.println("请输入数据大小:");
        size =reader.nextInt();
        initPage();
        initIn();
        System.out.flush();
        int choice;
        while (true){
            System.out.println("---------*********页面淘汰算法模拟*********--------------");
            System.out.println();
            System.out.println("       1.先进先出算法              2.理想页面算法");
            System.out.println();
            System.out.println("       3.最近最少使用算法          4.修改页框大小");
            System.out.println();
            System.out.println("       5.退出");
            System.out.println("---------*********                *********--------------");
            System.out.print("请选择：");
            choice = reader.nextInt();
            switch (choice){
                case 1:FIFO();break;
                case 2:OPT();break;
                case 3:LRU();break;
                case 4:{
                    System.out.println("请输入页面大小:");
                    Pagesize = reader.nextInt();
                }break;
                default:exit(0);
            }
        }
    }
}
