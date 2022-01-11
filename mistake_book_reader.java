public class mistake_book_reader {
    public static String[] get_indexed_items(String[] list) {   //提取列表中内容不为空的项
        int indexed_items_num = 0;     //表示该列表中有多少个项是不为空的
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null & list[i] != "") {    //如果内容不为空
                indexed_items_num++;     //增加一
            }
        }
        String[] indexed_item = new String[indexed_items_num];    //indexed_item表示有内容的项
        int num = 0;    //表示目前indexed_item用到第几项了
        for (int i = 0; i < list.length; i++) {   //遍历 
            if (list[i] != null & list[i] != "") {    //只要这一项不是空
                indexed_item[num] = list[i];    //放到indexed_item中
                num++;
            }
        }
        return indexed_item;    //返回有内容的项
    }

    public static void creat_new_question_type_in_mistake_book(String qtype) throws Exception {   //在错题本中新增错题类型并初始化
        if (! contains.str_contains(qtype + ":",io.read("../错题本"))) {    //如果该类型还不在错题本里
            io.write("../错题本",qtype + ":" + "0");      //错题本的格式:     题类名称:当前等级
        }
    }

    public static void intiliaze_mistake_book() throws Exception {   //重置错题本至初始状态
        String[] old_file = io.read("../错题本");         //先读取之前的错题本
        String[] new_file = new String[old_file.length];       //创建空列表new_file，使其长度为之前错题本的长度
        for (int i = 0; i < old_file.length; i++) {
            new_file[i] = old_file[i].split(":")[0] + ":0";       //每一种错题初始的等级都是0级
        }
        io.write("../错题本",new_file,true);      //写入文件
    }

    public static void del_question_type_in_mistake_book(String qtype) throws Exception {     //在错题本中删除一个错题类型
        String[] old_file = io.read("../错题本");     //读取原来的错题本
        String[] new_file = new String[old_file.length];     //创建列表new_file，其长度为原来错题本的长度－1
        for (int i = 0; i < old_file.length; i++) {
            if (old_file[i].split(":")[0].equals(qtype)) {     //如果时我们要删除的这一项
                new_file[i] = null;    //为空，也就是从新错题本中删除
            } else{     //如果是其他的
                new_file[i] = old_file[i];       //原封不动放进new_file中
            }
        }
        io.write("../错题本",get_indexed_items(new_file),true);    //写入文件，用函数getindexeditem删除了的错题(因为刚刚将它设为了空)
    }

    public static void add_level_for_question_in_mistake_book(String qtype) throws Exception {   //将错题本中某一个题类的掌握等级提升1级
        String[] old_file = io.read("../错题本");     //先读取原来的错题本
        String[] new_file = new String[old_file.length];       //新建列表new_file，长度和old_file一样
        int level;   //level表示当前题目的等级
        for (int i = 0; i < old_file.length; i++) {
            if (old_file[i].split(":")[0].equals(qtype)) {       //找到该问题
                if (old_file[i].split(":")[1].equals("3")) {    //如果该问题目前等级是三级
                    del_question_type_in_mistake_book(qtype);    //删除该问题
                    return;   //退出函数
                }
                else {      //不是三级
                    level = Integer.parseInt(old_file[i].split(":")[1]);    //提取当前的等级数
                    level++;      //将等级数加一
                    new_file[i] = old_file[i].split(":")[0] + ":" + level;    //存入new_file中
                }
            }
            else {    //如果不是我们要找到问题
                new_file[i] = old_file[i];     //原封不动的存入new_file
            }
        }


        io.write("../错题本",get_indexed_items(new_file),true);    //写入文件
    }

    public static String[] read_mistake_book() throws Exception {      //读取错题本
        return io.read("../错题本");    //读取文件
    }

    public static void add_did_times_for_question(String qtype, String question) throws Exception{     //在题库中增加某题类的某题型做过的次数,qtype为题目类型，question为题目的问题
        String[] old_file = io.read(qtype);     //读取原来的文件
        String[] new_file = new String[old_file.length];     //创建列表new_file，并开辟内存空间，长度与old_file一样
        int did_times, wrong_times;         //did_times用于表示做过的次数，wrong_times用于表示错误次数
        for (int i = 0; i < old_file.length; i++) {     //遍历原来的文件
            if (old_file[i].split(":")[0].equals(question)) {     //找到我们要修改的那一个题目
                did_times = Integer.valueOf(old_file[i].split(":")[2]);     //提取做过的次数
                did_times++;    //增加做过的次数（加一）
                wrong_times = Integer.valueOf((old_file[i].split(":")[3]));    //提取错误次数，不需要改变
                new_file[i] = old_file[i].split(":")[0] + ":" + old_file[i].split(":")[1] + ":" + did_times + ":" + wrong_times;    //合并，并存入new_file中
                continue;   //跳过循环，下面的这行的代码就不会被执行
            }
            new_file[i] = old_file[i];    //如果不是我们要找的题，直接原样照搬
        }
        io.write(qtype,new_file,true);    //写入文件，并替换以前的文件
    }

    public static void add_wrong_times_for_question(String qtype, String question) throws Exception{     //在题库中增加某题类的某题型的错误次数,qtype为题目类型，question为题目的问题
        String[] old_file = io.read(qtype);
        String[] new_file = new String[old_file.length];
        int did_times, wrong_times;
        for (int i = 0; i < old_file.length; i++) {
            if (old_file[i].split(":")[0].equals(question)) {
                did_times = Integer.valueOf(old_file[i].split(":")[2]);
                wrong_times = Integer.valueOf((old_file[i].split(":")[3]));
                wrong_times++;     //改变错误次数，不改变做过的次数
                new_file[i] = old_file[i].split(":")[0] + ":" + old_file[i].split(":")[1] + ":" + did_times + ":" + wrong_times;
                continue;
            }
            new_file[i] = old_file[i];
        }
        io.write(qtype,new_file,true);
        //所有部分都与add_did_times_for_question相同，除了167行
    }
}