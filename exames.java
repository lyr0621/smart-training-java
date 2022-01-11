public class exames {
    public static String judge(int score) {     //输入成绩，给出反馈
        if (score > 95) {
            return "真是奇才啊，牛逼";
        } else if (score > 90) {
            return "很厉害啊！";
        } else if (score > 80) {
            return "很不错呢！";
        } else if (score > 75) {
            return "还行.";
        } else if (score > 60) {
            return "还需多加练习哦！";
        } else if (score > 20) {
            return "呜呜～不及格哦，小心回家被扇巴掌哦(*_*)";
        } else {
            return "你是来捣乱的吧～";
        }
    }

    public static void new_question() throws Exception{    //新建问题
        String qtype = io.input("问题的类型是什么？");    //询问问题的类型
        int question_difficulty;     //表示题目的难度等级
        try {
            question_difficulty = Integer.valueOf(io.input("难度等级是什么（难度等级需在1-3级之间）"+"请输入难度等级"));   //询问问题难度等级，强制转化为int类型
        } catch(NumberFormatException e) {    //如果输入的不是纯数字而报错
            io.print("请输入纯数字（整数）");    //提示输入纯数字
            return;   //退出函数
        }
        if (question_difficulty > 3 | question_difficulty < 1) {    //如果问题难度等级高于三或小于一
            io.print("数字需在1-3之间");    //提示要在一至三之间
            return;    //推出函数
        }
        String question = io.input("问题是什么？");     //询问问题是什么
        String answer = io.input("正确答案是什么？");    //询问答案是什么
        switch(question_difficulty) {          //设置初始的题目回答次数以及答错次数
            case 1 :{
                io.write(qtype,question + ":" + answer + ":8:1");    //一级题目的错误比例为0.25及以下（包括0.25）
                break;
            }
            case 2 :{
                io.write(qtype,question + ":" + answer + ":8:3");    //二级题目的错误比例为0.25-0.5（包括0.5）
                break;
            }
            case 3 :{
                io.write(qtype,question + ":" + answer + ":8:5");    //三级题目的错误比例为0.5以上
                break;
            }
        }
        mistake_book_reader.creat_new_question_type_in_mistake_book(qtype);
    }

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

    public static void test_now() throws Exception{   //开始测试
        io.print("您开始了新一轮的测试");
        String[] mistake_book = get_indexed_items(mistake_book_reader.read_mistake_book());     //读取错题本
        if (mistake_book.length == 0) {       // 如果错题本为空
            io.print("目前题库中已经找不到能够难倒你的题目了，快去玩吧！","恭喜你!!!");     //错题本为空表示该用户没有不完全掌握的题型了
            return;    //推出函数
        }

        int right_times = 0;     //表示做对了几题
        int question_number = 0;    //表示第几题
        String[] solved_problems = new String[mistake_book.length];      //用于存储已经解决的错题，长度和 mistakebook一样长，因为这是可能解决的题型的数量上限
        int solved_problems_num = 0;    //表示该列表用到第几项了
        String[] wrong_answers = new String[mistake_book.length];       //用于存储做错的题目
        int wrong_answers_num = 0;    //表示该列表用到第几项了
        String[] question_source, question, list1;                     //用于存储该题类的文件中的内容，存储问题和找到符合要求的题型
        int list1_num = 0;   //表示该列表用到第几项了
        int r;     //用于制造随机数
        int questions_did_times, questions_wrong_times;
        String my_answer;    //用于存储我的答案
        for (int i = 0; i < mistake_book.length; i++) {     //遍历错题本
            if (mistake_book[i] == "") {     //如果这一项的内容为空(可能是字符串处理时导致的)
                continue;    //跳过
            }
            question_number++;    //问题题号加一

            if (mistake_book[i].split(":")[1].equals("0")) {       //如果该题类目前的掌握情况为0级
                try {
                    question_source = io.read(mistake_book[i].split(":")[0]);
                } catch (Exception e) {
                    io.print("错误：无法加载题目文件，请勿私自更改错题本或题库的内容");
                    return;
                }       //先读取该题类的文件，如果找不到就报错
                list1 = new String[question_source.length];
                list1_num = 0;
                for (int m = 0; m < question_source.length; m++) {    //遍历题类的文件
                    questions_did_times = Integer.valueOf(question_source[m].split(":")[2]);    //提取出该题目做过的次数
                    questions_wrong_times = Integer.valueOf(question_source[m].split(":")[3]);    //提取出该题目错误的次数
                    if ((double) questions_wrong_times/(double) questions_did_times > 0.5) {      //找到符合条件(难度等级为3)的题目，错误频率大于0.5
                        list1[list1_num] = question_source[m];     //存入list1
                        list1_num++;    //这个是为了记住list1已经存到第几项了，因为java没有append
                    }
                }
                question_source = get_indexed_items(list1);    //提取list1中不为空的项，存入question_source

                if (question_source.length == 0) {
                    io.print("抱歉，题库不全，对于“" + mistake_book[i].split(":")[0] + "”这个类型没有适合您目前的掌握等级(" + mistake_book[i].split(":")[1] + ")没有合适的题目，将跳过到下一题");
                    continue;
                }
                
                r = (int) (Math.random()*question_source.length);    //在零到符合条件的题目数量之间随机一个数，并强制转化为int
                question = question_source[r].split(":");        //提取随机到的那一项，并以“:”为间隔拆分称列表，存入question中，格式 问题:正确答案:难度等级
                
                mistake_book_reader.add_did_times_for_question(mistake_book[i].split(":")[0], question[0]);    //增加这题的回答次数

                io.print("题目"+question_number+"/"+mistake_book.length);

                my_answer = io.input(question[0]);       //提问题，把答案存入my_answer中

                if (my_answer.equals(question[1])) {    //回答正确
                    right_times++;    //正确次数加一
                    //因为目前该题类的掌握情况是零级，所以一旦答对就可以直接从错题本中删除
                    solved_problems[solved_problems_num] = mistake_book[i].split(":")[0] + ":" + question[0] + ":" + question[1];      //存入solved_problem列表中，格式：   题类名称:问题:答案
                    solved_problems_num++;
                    io.print("恭喜你，答对了！");
                    mistake_book_reader.del_question_type_in_mistake_book(mistake_book[i].split(":")[0]);     //从错题本中删除
                    
                }
                else {     //回答错误
                    io.print("很抱歉，你答错了");
                    wrong_answers[wrong_answers_num] = mistake_book[i].split(":")[0] + ":" + question[0] + ":" + question[1] + ":" + my_answer;     //存入wrong_answers中，格式：  题目类型:问题:正确答案:我的答案
                    wrong_answers_num++;
                    mistake_book_reader.add_level_for_question_in_mistake_book(mistake_book[i].split(":")[0]);    //在错题本中为这个题类的掌握等级加一(从零级变成一级)
                    mistake_book_reader.add_wrong_times_for_question(mistake_book[i].split(":")[0], question[0]);      //增加这题的错误次数
                }
            }

            else if (mistake_book[i].split(":")[1].equals("1")) {    //目前掌握程度为一级
                try {
                    question_source = io.read(mistake_book[i].split(":")[0]);
                } catch (Exception e) {
                    io.print("错误：无法加载题目文件，请勿私自更改错题本或题库的内容");
                    return;
                }
                list1 = new String[question_source.length];
                list1_num = 0;
                for (int m = 0; m < question_source.length; m++) {
                    questions_did_times = Integer.valueOf(question_source[m].split(":")[2]);
                    questions_wrong_times = Integer.valueOf(question_source[m].split(":")[3]);
                    if ((double) questions_wrong_times/(double) questions_did_times <= 0.25) {    //一级的错误频率小于等于0.25
                        list1[list1_num] = question_source[m];
                    }
                }
                question_source = get_indexed_items(list1);

                if (question_source.length == 0) {
                    io.print("抱歉，题库不全，对于“" + mistake_book[i].split(":")[0] + "”这个类型没有适合您目前的掌握等级(" + mistake_book[i].split(":")[1] + ")没有合适的题目，将跳过到下一题");
                    continue;
                }
                
                r = (int) (Math.random()*question_source.length);
                question = question_source[r].split(":");

                mistake_book_reader.add_did_times_for_question(mistake_book[i].split(":")[0], question[0]);
                
                io.print("题目"+question_number+"/"+mistake_book.length);

                my_answer = io.input(question[0]);
                //以上部分与刚刚的代码一样

                if (my_answer.equals(question[1])) {    //回单正确
                    right_times++;
                    io.print("恭喜你，答对了！");
                    mistake_book_reader.add_level_for_question_in_mistake_book(mistake_book[i].split(":")[0]);     //掌握程度的等级加一
                }
                else {    //回答错误
                    io.print("很抱歉，你答错了");
                    //掌握程度不变
                    wrong_answers[wrong_answers_num] = mistake_book[i].split(":")[0] + ":" + question[0] + ":" + question[1] + ":" + my_answer;     //存入wrong_answers中
                    wrong_answers_num++;
                    mistake_book_reader.add_wrong_times_for_question(mistake_book[i].split(":")[0], question[0]);    //增加这题的回答次数
                }
            }

            else if (mistake_book[i].split(":")[1].equals("2")) {     //二级
                try {
                    question_source = io.read(mistake_book[i].split(":")[0]);
                } catch (Exception e) {
                    io.print("错误：无法加载题目文件，请勿私自更改错题本或题库的内容");
                    return;
                }
                list1 = new String[question_source.length];
                list1_num = 0;
                for (int m = 0; m < question_source.length; m++) {
                    questions_did_times = Integer.valueOf(question_source[m].split(":")[2]);
                    questions_wrong_times = Integer.valueOf(question_source[m].split(":")[3]);
                    if ((double) questions_wrong_times/(double) questions_did_times > 0.25 & (double) questions_wrong_times/(double) questions_did_times <= 0.5) {    //二级的频率在0.25~0.5之间
                        list1[list1_num] = question_source[m];
                        list1_num++;
                    }
                }
                question_source = get_indexed_items(list1);

                if (question_source.length == 0) {
                    io.print("抱歉，题库不全，对于“" + mistake_book[i].split(":")[0] + "”这个类型没有适合您目前的掌握等级(" + mistake_book[i].split(":")[1] + ")没有合适的题目，将跳过到下一题");
                    continue;
                }
                
                r = (int) (Math.random()*question_source.length);
                question = question_source[r].split(":");

                mistake_book_reader.add_did_times_for_question(mistake_book[i].split(":")[0], question[0]);    //增加这题的回答次数
                
                io.print("题目"+question_number+"/"+mistake_book.length);

                my_answer = io.input(question[0]);

                if (my_answer.equals(question[1])) {    //回答正确
                    right_times++;
                    io.print("恭喜你，答对了！");
                    mistake_book_reader.add_level_for_question_in_mistake_book(mistake_book[i].split(":")[0]);      //等级加一
                    
                }
                else {    //错误
                    io.print("很抱歉，你答错了");
                    //等级不变
                    wrong_answers[wrong_answers_num] = mistake_book[i].split(":")[0] + ":" + question[0] + ":" + question[1] + ":" + my_answer;
                    wrong_answers_num++;
                    mistake_book_reader.add_wrong_times_for_question(mistake_book[i].split(":")[0], question[0]);    //增加这题的回答次数
                }
            }

            else if (mistake_book[i].split(":")[1].equals("3")) {     //三级
                try {
                    question_source = io.read(mistake_book[i].split(":")[0]);
                } catch (Exception e) {
                    io.print("错误：无法加载题目文件，请勿私自更改错题本或题库的内容");
                    return;
                }
                list1 = new String[question_source.length];
                list1_num = 0;
                for (int m = 0; m < question_source.length; m++) {
                    questions_did_times = Integer.valueOf(question_source[m].split(":")[2]);
                    questions_wrong_times = Integer.valueOf(question_source[m].split(":")[3]);
                    if ((double) questions_wrong_times/(double) questions_did_times > 0.5) {    //大于0.5
                        list1[list1_num] = question_source[m];
                        list1_num++;
                    }
                }
                question_source = get_indexed_items(list1);

                if (question_source.length == 0) {
                    io.print("抱歉，题库不全，对于“" + mistake_book[i].split(":")[0] + "”这个类型没有适合您目前的掌握等级(" + mistake_book[i].split(":")[1] + ")没有合适的题目，将跳过到下一题");
                    continue;
                }
                
                r = (int) (Math.random()*question_source.length);
                question = question_source[r].split(":");

                mistake_book_reader.add_did_times_for_question(mistake_book[i].split(":")[0], question[0]);    //增加这题的回答次数
                
                io.print("题目"+question_number+"/"+mistake_book.length);
                
                my_answer = io.input(question[0]);

                if (my_answer.equals(question[1])) {    //正确
                    right_times++;
                    solved_problems[solved_problems_num] = mistake_book[i].split(":")[0] + ":" + question[0] + ":" + question[1] + ":" + my_answer;    //该问题就解决了
                    solved_problems_num++;
                    io.print("恭喜你，答对了！");
                    mistake_book_reader.del_question_type_in_mistake_book(mistake_book[i].split(":")[0]);     //从错题本中删除该题类
                    
                }
                else {    //错误
                    io.print("很抱歉，你答错了");
                    //等级不变
                    wrong_answers[wrong_answers_num] = mistake_book[i].split(":")[0] + ":" + question[0] + ":" + question[1] + ":" + my_answer;
                    wrong_answers_num++;
                    mistake_book_reader.add_wrong_times_for_question(mistake_book[i].split(":")[0], question[0]);    //增加这题的回答次数
                }
            }
        }

        io.print("考试结束，考试报告请在文本输出行中查看");

        System.out.println("在" + question_number + "道题目中，我做对了" + right_times + "道，正确率" + (right_times/question_number)*100 + "%，" + judge((right_times/question_number)*100));      //反馈正确率

        wrong_answers = get_indexed_items(wrong_answers);      //去除wrong_answers中为空的项

        if (wrong_answers.length != 0) {      //如果有错题的话
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n <<--以下为错题分析-->>");
            for (int i = 0; i < wrong_answers.length; i++) {
                //wrong_answers的格式：   题目类型:问题:正确答案:我的答案
                System.out.println("<<第" + (i+1) + "道错题>>");
                System.out.println("问题类型：" + wrong_answers[i].split(":")[0]);
                System.out.println("问题：" + wrong_answers[i].split(":")[1]);
                System.out.println("我的答案：" + wrong_answers[i].split(":")[3]);
                System.out.println("正确答案：" + wrong_answers[i].split(":")[2]);
                System.out.println("\n");
            }
        }

        solved_problems = get_indexed_items(solved_problems);

        if (solved_problems.length != 0) {     //如果有已经彻底搞定的题型的话
            System.out.println("\n\n\n <<--恭喜你，你已经彻底解决了" + solved_problems.length + "个题型，它们是：-->>");
            //solved_problems的格式：   题类名称:问题:答案
            for (int i = 0; i < solved_problems.length; i++) {
                System.out.println("<<第" + (i+1) + "个题型>>");
                System.out.println(solved_problems[i].split(":")[0]);
            }
        }
    }
}