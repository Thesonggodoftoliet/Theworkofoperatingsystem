/**
 * 
 */
package Work;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * @author 李煜峰
 *
 * date:2018年10月29日 time:上午10:52:46
 */
public class PCBManage {
	
	private static List<PCB> PCBtable  = new ArrayList<PCB>();//PCB表
	private static int CPU;//运行指针 存ID
	private static Queue<Integer> Ready = new LinkedList<Integer>();//就绪表指针 先进先出 存入ID
	private static List<Integer> Wait = new LinkedList<Integer>();//等待表指针   存入ID

	/**
	 * 
	 */
	public PCBManage() {
		// TODO Auto-generated constructor stub
	}
	
	//查找重名
	public static boolean isNamedBefore(String name){
		int i = 0;
		while(i<PCBtable.size()){
			if(PCBtable.get(i).getName().equals(name))return true;
			i++;
		}
		
		return false;
	}
	
	//在PCB表中通过ID查找，返回索引index
	public static int PCBfindByid(int id){
		int i = 0;
		while(i<PCBtable.size()){
			if(PCBtable.get(i).getId() == id)return i;
			i++;
		}
		
		return -1;
	}
	
	//在等待队列中通过ID查找，返回索引index
	public static int WaitfindByid(int id){
		int i = 0;
		while(i<Wait.size()){
			if(Wait.get(i) == id)return i;
			i++;
		}
		
		return -1;
	}
	
	public static void show(int choice){
		if(choice == 0){//显示全部进程
			int i = 0;
			System.out.println("进程名                  "+"进程号               "+"状态");
			while(i<PCBtable.size()){
				System.out.println(PCBtable.get(i).getName()+"   "+PCBtable.get(i).getId()+"   "+PCBtable.get(i).getStatus());
				i++;
			}
		}
		
		if(choice == 1){//显示就绪进程
			int i = 0;
			System.out.println("进程名                  "+"进程号               "+"状态");
			while(i<PCBtable.size()){
				if(PCBtable.get(i).getStatus() == 0) System.out.println(PCBtable.get(i).getName()+"   "+PCBtable.get(i).getId()+"   "+PCBtable.get(i).getStatus());
				i++;
			}
		}
		
		if(choice == 2){//显示等待进程
			int i = 0;
			System.out.println("进程名                  "+"进程号               "+"状态");
			while(i<PCBtable.size()){
				if(PCBtable.get(i).getStatus() == -1) System.out.println(PCBtable.get(i).getName()+"   "+PCBtable.get(i).getId()+"   "+PCBtable.get(i).getStatus());
				i++;
			}
		}
	}
	
	public static void Create(){
		Scanner in = new Scanner(System.in);
		PCB tpcb = new PCB();
		
		//获取进程名
		System.out.println("执行创建原语：\n"+"请输入进程名称");
		tpcb.setName(in.next());
		while(isNamedBefore(tpcb.getName())){
			System.out.println("该进程已创建，请创建其他进程\n"+"请输入进程名称");
			tpcb.setName(in.next());	
		}

		System.out.println("输入该进程的时间：");
		tpcb.setTime(in.nextInt());
		//获取资源信息
		/*
		System.out.println("请输入资源信息：(是否执行I/O操作)\n");
		tpcb.setResourse(in.nextInt());*/

		//获取进程号
		tpcb.setId(PCBtable.get(PCBtable.size()-1).getId()+1);
		
		//加入PCB表
		PCBtable.add(tpcb);
		
		//加入就绪队列
		PCBtable.get(PCBtable.size()-1).setStatus(0);
		Ready.add(tpcb.getId());
		
		System.out.println("进程"+tpcb.getName()+"已创建，"+"进程号为"+tpcb.getId());
		
	}
	
	public static void Kill(){
		show(0);
		int temp;
		Scanner reader = new Scanner(System.in);
		System.out.println("请输入将要杀死的进程号");
		temp = reader.nextInt();
		if(PCBfindByid(temp)==-1){
			System.out.println("没有此进程");
		}
		else{
			PCBtable.remove(PCBfindByid(temp));
			System.out.println("成功杀死进程");
		}
		
	}
	
	public static void waiting(String reason){
		int temp = PCBfindByid(CPU);
		PCB tpcb = PCBtable.get(temp);
		tpcb.setLeftInfo(reason);
		tpcb.setStatus(-1);
		Wait.add(tpcb.getId());
		CPU=-1;
	}
	
	public static void wakeup(){
		show(2);
		System.out.println("输入准备唤醒的进程");
		Scanner reader = new Scanner(System.in);
		int temp = reader.nextInt();
		if(WaitfindByid(temp)==-1){
			System.out.println("没有此进程");
		}
		else{
			System.out.println("挂起的原因"+PCBtable.get(PCBfindByid(temp)).getLeftInfo());
			temp = WaitfindByid(temp);
			Ready.add(Wait.get(temp));
			Wait.remove(temp);
			System.out.println("成功唤醒进程");
		}
	}
	
	public static void running(){
			CPU=Ready.remove();
			int temp = PCBfindByid(CPU);
			int CPUtime = 10;
			if(Ready.isEmpty()){
				System.out.println("没有等待处理的任务,关机");
				exit(0);
			}
			else if(PCBtable.get(temp).getTime()<=CPUtime){
				System.out.println("CPU正在处理进程"+CPU);
				CPU=Ready.remove();
				PCBtable.remove(temp);
			}
			else {
				System.out.println("时间片自动中断，进程"+CPU+"加入就绪队列");
				PCBtable.get(temp).setTime(PCBtable.get(temp).getTime() - CPUtime);
				Ready.add(CPU);
			}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PCB first = new PCB();
		first.setName("First.exe");
		first.setId(1);
		first.setTime(50);
		first.setStatus(0);
		PCBtable.add(first);
		Ready.add(first.getId());
		int choice;
		while(true) {
			System.out.println("***********     PCB进程管理模拟    ***********");
			System.out.println("****  1.创建进程              2.挂起进程  ****");
			System.out.println("****  3.激活进程              4.撤销进程  ****");
			System.out.println("****  5.查看进程              6.运    行  ****");
			System.out.println("****  7.退    出                          ****");
			System.out.println("***********     --------------     ***********");
			Scanner reader = new Scanner(System.in);
			System.out.println("请输入你的选择:");
			choice = reader.nextInt();
			if (choice == 1)
				Create();
			else if(choice == 2) {
				System.out.println("请输入中断的理由:");
				String reason  = reader.next();
				waiting(reason);
			}
			else if (choice == 3)
				wakeup();
			else if (choice == 4)
				Kill();
			else if (choice == 5)
				show(0);
			else if (choice == 6)
				running();
			else exit(0);
		}
	}

}
