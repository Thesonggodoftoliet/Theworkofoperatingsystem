package Work;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class FileManage {
    public MFD logininfo;
    public FCB tfcb;
    private List<MFD> mfds;
    private List<UFD> ufds;
    String path;//记录当前目录

    FileManage(){
        logininfo=null;
        tfcb=null;
        mfds = new ArrayList<MFD>();
        ufds = new ArrayList<UFD>();
        path = null;
    }

    public void CreateUser(){
        if (mfds.size()>10){
            System.out.println("用户已满！");
            return;
        }
        Scanner reader = new Scanner(System.in);
        MFD cmfd = new MFD();
        System.out.print("请设定用户名：");
        cmfd.setUsername(reader.nextLine());

        System.out.print("请设定密码：");
        cmfd.setPwd(reader.nextLine());

        mfds.add(cmfd);

        File file = new File("E:\\操作系统\\Users\\"+cmfd.getUsername());
        if(!file.exists()){
            file.mkdir();
        }

    }

    public int Login(){
        int tag =0;
        Scanner reader = new Scanner(System.in);
        String username;
        String userpwd;
        System.out.print("请输入用户名：");
        username= reader.nextLine();
        System.out.print("请输入密码：");
        userpwd = reader.nextLine();

        /*寻找用户*/
        int index = 0;
        for (int i =0;i<mfds.size();i++){
            if (username.equals(mfds.get(i).getUsername())){
                tag++;
                if (userpwd.equals(mfds.get(i).getPwd())) {
                    logininfo = mfds.get(i);
                    tag++;
                    index = i;
                }
            }
        }

        if (tag == 0) {
            System.out.println("没有此用户，请检查用户名");
            return 0;
        }else if (tag == 1) {
            System.out.println("密码错误，请重试");
            return 1;
        }else {
            System.out.println("登陆成功");
            path = "E:\\操作系统\\Users\\"+mfds.get(index).getUsername();
            ufds = mfds.get(index).getUfds();
            return 2;
        }
    }

    public void Create(MFD mfd){
        if (ufds.size()>10){
            System.out.println("该用户的文件已满");
            return;
        }
        Scanner reader = new Scanner(System.in);
        UFD ufd = new UFD();
        FCB fcb = new FCB();
        System.out.println("开始创建文件");
        System.out.print("请输入文件名");
        String filename = reader.nextLine();
        ufd.setFilename(filename);
        fcb.setFilename(filename);
        fcb.setUsername(mfd.getUsername());
        System.out.print("请设定文件权限//0-readonly 1-writeonly 2-write/read");
        fcb.setFilemode(reader.nextInt());
        Date now = new Date();
        fcb.setBuildtime(now);
        fcb.setFileid(new Integer(mfd.getUfds().size()).toString());//如果删除再加入可能会重名
        File file = new File(path,filename);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ufd.setFcb(fcb);//指针连接
        mfd.getUfds().add(ufd);
    }

    public void Open(MFD mfd) throws IOException {
        List<UFD> tempufds = mfd.getUfds();//二级目录
        Scanner reader = new  Scanner(System.in);
        System.out.print("请输入要访问的文件名");
        String filename = reader.nextLine();
        int index = 0;
        for (int i = 0;i<tempufds.size();i++){
            if (tempufds.get(i).getFilename().equals(filename)) {
                index = i;
                break;
            }
        }

        tfcb= tempufds.get(index).getFcb();
    }

    public void Read(FCB fcb) throws IOException {
        if (fcb.getFilemode() == 1) {
            System.out.println("该文件为只写文件，不可读！");
            return;
        }

        BufferedReader BR = new BufferedReader(new FileReader(path+"\\"+fcb.getFilename()));
        String line="";
        line = BR.readLine();//读取操作
        while(line!=null){
            line = BR.readLine();
        }

        BR.close();
    }

    public void Write(FCB fcb) throws IOException {
        if (fcb.getFilemode() == 0){
            System.out.println("该文件为只读文件，不可写！");
            return;
        }

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader BR = new BufferedReader(isr);
        BufferedWriter WR = new BufferedWriter((new FileWriter(path+"\\"+fcb.getFilename())));

        String content = null;
        System.out.println("请输入内容，以end结束");
        while((content=BR.readLine())!=null){
            if (content.equalsIgnoreCase("end"))
                break;
        }
        WR.write(content+"\t\n");

        BR.close();
        WR.close();

    }

    public void Delete(MFD mfd){
        List<UFD> tempufds = mfd.getUfds();//二级目录
        Scanner reader = new  Scanner(System.in);
        System.out.print("请输入要删除的文件名");
        String filename = reader.nextLine();
        int index = 0;
        for (int i = 0;i<tempufds.size();i++){
            if (tempufds.get(i).getFilename().equals(filename)) {
                index = i;
                break;
            }
        }
        File file = new File(path+"\\"+tempufds.get(index).getFcb().getFilename());

        if (file.exists() && file.isFile()){
            file.delete();
        }

    }

    public void clear(){
        logininfo = null;
        tfcb = null;
        ufds = null;
        path = null;
    }

    public void showUser(){
        System.out.println("用户数量"+mfds.size());
        for (int i =0;i<mfds.size();i++) {
            System.out.println(mfds.get(i).getUsername());
        }
    }

    public static void main(String args[]) throws IOException {
        FileManage FM = new FileManage();
        while (true) {
            System.out.println("*********************          文件系统模拟      ************************");
            System.out.println("       1.用户登陆                                  2.用户注册            ");
            System.out.println("       3.查看用户                                  4.退出系统");
            System.out.print("输入选项:");
            Scanner reader = new Scanner(System.in);
            int choice = reader.nextInt();

            switch (choice) {
                case 1:{
                    if(FM.Login()==2){
                        int tag = 0;
                        while (tag == 0){
                            System.out.println("*********************          文件系统模拟      ************************");
                            System.out.println("       1.CREATE                                 2.OPEN            ");
                            System.out.println("       3.READ                                   4.WRITE            ");
                            System.out.println("       5.DELETE                                 6.注销用户           ");
                            System.out.println("       7.退出系统           ");
                            System.out.print("输入选项:");
                            int ch = reader.nextInt();
                            switch (ch){
                                case 1: FM.Create(FM.logininfo);
                                break;
                                case 2:FM.Open(FM.logininfo);
                                break;
                                case 3:FM.Read(FM.tfcb);
                                break;
                                case 4:FM.Write(FM.tfcb);
                                break;
                                case 5:FM.Delete(FM.logininfo);
                                break;
                                case 6:{
                                    tag = 1;
                                    FM.clear();
                                }
                                break;
                                default:exit(0);

                            }
                        }
                    }
                }
                    break;
                case 2:
                    FM.CreateUser();
                    break;
                case 3:FM.showUser();
                break;
                default:
                    exit(0);
            }
        }
    }

}
