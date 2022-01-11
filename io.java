import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class io {
    static Scanner sc = new Scanner(System.in);                  //新建输入读取器

    public static void print(String s) {
        System.out.println(s);
    }

    public static void print(String s,String end) {
        System.out.print(s+end);
    }

    public static String input(String question) {       //询问问题
        print(question,"");              //先打印问题
        return sc.nextLine().toString();          //返回输入内容
    }


    public static String[] read(String filename) throws Exception {    //读取文件，参数filename表示文件的名称
        String path = "题库/" + filename;       //设置路径
        FileInputStream fileInputStream = new FileInputStream(path);     //新建fileinputstream对象，用于打开文件

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));        //新建bufferedReader对象，用于读取文件

        String line = null;    //表示目前正在读取的行

        String result = "";    //用来存储读取文件的结果

        while ((line = bufferedReader.readLine()) != null) {
            result += line + "/";     //将结果存入result，以"/为间隔"
        }

        fileInputStream.close();   //关闭文件

        result = result.trim();     //去掉没有内容的行

        return result.split("/");     //以“/”为间隔再把result拆开并返回，因此返回的是一个字符串列表，表示每一行的数据
    }


    public static void write(String filename, String line, boolean replace) throws Exception {   //写入单行文件，参数filename表示文件名，line表示要写入的行，replace表示是否要覆盖原来的文件
        String path = "题库/" + filename;    //设置路径
        File file = new File(path);      //创建file类型文件
        String[] old_file = new String[0];       //创建列表old_file用于存储原来的文件
        if (!replace) {          //如果不需要覆盖原来的文件
            try {
                old_file = read(filename);      //读取文件，存入列表old_file
            } catch(FileNotFoundException e) {}     //如果没找到该文件，什么都不用做，当新文件处理
        }
        

        String[] content = new String[old_file.length+1];     //创建列表content，用于存储要写入的东西，长度时old_file的长度加一(因为是写入单行文件)

        for (int i = 0; i<old_file.length; i++) {
            content[i] = old_file[i];    //将 oldfile中的项都复制给content
        }

        content[old_file.length] = line;    //content的最后一项(也就是第old_file.length项，因为其长度为old_file.length＋1)

        FileOutputStream fileOutputStream = new FileOutputStream(file);   //新建写入文件的对象
        for (int i = 0; i<content.length; i++) {
            if (content[i] != ""){
                fileOutputStream.write(content[i].getBytes());    //写入
                fileOutputStream.write('\n');   //换行
            }
        }

        fileOutputStream.close();   //关闭文件
    }


    public static void write(String filename,  String line) throws Exception {    //如果没有说明是否要覆盖文件的话
        write(filename,line,false);   //不覆盖文件
    }


    public static void write(String filename, String[] lines, boolean replace) throws Exception {   //写入多行文件，传入的参数lines是一个列表，表示要写入的行(不止一行)
        if (replace) {     //如果要覆盖
            write(filename, "", true);     //先覆盖原来的文件，并写入空字符(相当于把原来的文件变成了空的)
            for (int i = 0; i<lines.length; i++) {
                write(filename,lines[i]);    //一行行写入
            }
        }
        else {   //不用覆盖
            for (int i = 0; i<lines.length; i++) {
                write(filename,lines[i]);    //一行行写入
            }
        }
        
    }


    public static void write(String filename, String[] lines) throws Exception {    //写入多行文件，没有说明是否要覆盖时
        write(filename, lines, false);    //不覆盖
    }
}