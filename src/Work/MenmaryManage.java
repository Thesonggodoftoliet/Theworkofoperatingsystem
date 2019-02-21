package Work;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class MenmaryManage {
    private static int Free = 0; //空闲状态
    private static int Busy = 1; //已用状态
    private static int OK = 1; //完成
    private static int ERROR = 0; //出错
    private static int MAX_length = 512; //最大内存空间为32767KB
    private static int LIMIT = 10;//如果最小剩余空间小于10则不再切割
    public static List<FreeArea> block = new LinkedList<>();//空间表

    public static int Initblock(){
        FreeArea temp = new FreeArea();
        temp.setAddress(0);
        temp.setSize(MAX_length);
        temp.setID(Free);
        temp.setState(Free);
        block.add(temp);
        return OK;
    }

    //内存分配
    public static int Alloc(){
        Scanner reader = new Scanner(System.in);

        System.out.println("*********************************");
        System.out.println("**  1:首先适应     2:最佳适应  **");
        System.out.println("**  3.最坏适应       **");
        System.out.println("*********************************");
        System.out.print("请选择算法（序号）：");
        int ch = reader.nextInt();
        int ID,request;
        System.out.print("请输入作业(分区号):");
        ID = reader.nextInt();
        System.out.print("请输入需要分配的主存大小(单位:KB):");
        request=reader.nextInt();

        if(request<0||request==0){
            System.out.println("分配大小不合适，请重试！");
            return ERROR;
        }
        if(ch == 2){
            if(Best_fit(ID,request)==OK)
                System.out.println("分配成功！");
            else
                System.out.println("内存不足，分配失败！");
            return OK;
        }
        else if(ch == 3){
            if(Worst_fit(ID,request)== OK)
                System.out.println("分配成功！");
            else
                System.out.println("内存不足，分配失败！");
            return OK;
        }
        else {//默认首先适应算法
            if (First_fit(ID, request) == OK)
                System.out.println("分配成功！");
            else
                System.out.println("内存不足，分配失败！");
            return OK;
        }

    }

    public static int First_fit(int ID,int request){
        FreeArea temp = new FreeArea();
        temp.setID(ID);
        temp.setSize(request);
        temp.setState(Busy);
        int index = 0;
        while(index<block.size()){
            FreeArea p = block.get(index);
            if(p.getState()==Free && p.getSize() == request){
                p.setState(Busy);
                p.setID(ID);
                return OK;
            }

            if(p.getState()==Free && p.getSize() > request){//切割
                temp.setAddress(p.getAddress());
                if(p.getSize()-request<=LIMIT){
                    temp.setSize(p.getSize());
                    block.add(index,temp);
                    block.remove(p);
                    return OK;
                }
                else {
                    p.setAddress(temp.getAddress() + temp.getSize());
                    p.setSize(p.getSize() - request);
                    block.add(index, temp);
                    return OK;
                }
            }
            index++;
        }
        return ERROR;
    }//首先适应算法

    public static int Best_fit(int ID,int request){
        long ch=0; //记录最小剩余空间
        FreeArea temp = new FreeArea();
        temp.setID(ID);
        temp.setSize(request);
        temp.setState(Busy);
        int best = -1; //记录最佳位置
        int index = 0;
        while(index<block.size()){//第一次寻找空闲空间
            FreeArea p =block.get(index);
            if (p.getState()==Free && p.getSize()>=request){
                best = index;
                ch = p.getSize()-request;
                break;
            }
            index++;
        }
        while(index<block.size()){//遍历空间表直到找到最小的剩余空间
            FreeArea p = block.get(index);
            if (p.getState() == Free && p.getSize() == request){
                p.setID(ID);
                p.setState(Busy);
                return OK;
            }
            if(p.getState() == Free && p.getSize()>request){
                if (p.getSize()-request<ch){
                    ch = p.getSize()-request;
                    best=index;
                }
            }
            index++;
        }
        if(best==-1)return ERROR;
        else {//切割
            FreeArea p = block.get(best);
            if (ch<=LIMIT){
                temp.setSize(p.getSize());
                block.add(best,temp);
                block.remove(p);
                return OK;
            }
            else {
                temp.setAddress(p.getAddress());
                p.setAddress(p.getAddress() + request);
                p.setSize(ch);
                block.add(best, temp);
                return OK;
            }
        }
    }//最佳适应算法

    public static int Worst_fit(int ID,int request){
        FreeArea temp = new FreeArea();
        temp.setID(ID);
        temp.setSize(request);
        temp.setState(Busy);
        long ch = 0;
        int index = 0;
        int worst = -1;
        while(index<block.size()){
            FreeArea p =block.get(index);
            if (p.getState()==Free && p.getSize()>=request){
                worst = index;
                ch = p.getSize()-request;
                break;
            }
            index++;
        }
        while(index<block.size()){
            FreeArea p = block.get(index);
            if(p.getState() == Free && p.getSize()>=request){
                if (p.getSize()-request>ch){
                    ch = p.getSize()-request;
                    worst=index;
                }
            }
            index++;
        }
        if(worst==-1)return ERROR;
        else {//切割
            FreeArea p = block.get(worst);
            if (ch<=LIMIT){
                temp.setSize(p.getSize());
                block.add(worst,temp);
                block.remove(p);
                return OK;
            }
            else {
                temp.setAddress(p.getAddress());
                p.setAddress(p.getAddress() + request);
                p.setSize(ch);
                block.add(worst, temp);
                return OK;
            }
        }
    }//最坏适应算法

    public static int free(int ID){
        int index = 0;
        //int status = 0;//0 不邻 1 上邻 2 下邻 3 上下邻
        while (index<block.size()){
            FreeArea p = block.get(index);
            if(p.getID()==ID){
                p.setState(Free);
                p.setID(0);
                if(index>0 &&block.get(index-1).getState() == Free) {//上邻
                    p.setSize(p.getSize()+block.get(index-1).getSize());
                    p.setAddress(block.get(index-1).getAddress());
                    block.remove(index-1);
                    index--;
                }

                if (index<block.size() && block.get(index+1).getState() == Free){//下邻
                    p.setSize(p.getSize()+block.get(index+1).getSize());
                    block.remove(index+1);
                }
            }
            index++;
        }
        return OK;
    }

    public static void show(){
        System.out.println("**********-----------***********");
        System.out.println("****     主存分配情况       ****");
        System.out.println("**********-----------***********");
        int index = 0;
        while(index < block.size()){
            FreeArea p = block.get(index);
            System.out.print("分区号:");
            if(p.getState()==Free) System.out.println("Free");
            else System.out.println(p.getID());
            System.out.println("起始地址:"+p.getAddress());
            System.out.println("分区大小:"+p.getSize());
            System.out.print("状    态:");
            if (p.getState()==0) System.out.println("空闲");
            else System.out.println("已分配");
            System.out.println("--------------------------");
            index++;
        }
    }
    public static void main(String args[]){
        Initblock();
        int choice;
        Scanner reader = new Scanner(System.in);
        while (true){
            System.out.println("*********************************");
            System.out.println("**  1:分配内存     2:回收内存  **");
            System.out.println("**  3.查看内存     4.退    出  **");
            System.out.println("*********************************");
            System.out.print("请输入操作（序号）：");
            choice =reader.nextInt();
            switch (choice){
                case 1:Alloc();break;
                case 2:{
                    int ID;
                    System.out.print("请输入要释放的分区号：");
                    ID=reader.nextInt();
                    free(ID);
                    break;
                }
                case 3:show();break;
                default:exit(0);
            }
        }

    }
}
