package com.llh.advance.lock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.计算多个区间的交集
 * 区间用长度为2的数字数组表示，如[2, 5]表示区间2到5（包括2和5）；
 * 区间不限定方向，如[5, 2]等同于[2, 5]；
 * 实现`getIntersection 函数`
 * 可接收多个区间，并返回所有区间的交集（用区间表示），如空集用null表示
 * 示例：
 * getIntersection([5, 2], [4, 9], [3, 6]); // [4, 5]
 * getIntersection([1, 7], [8, 9]); // null
 */
public class Demo {

    public static void main(String[] args) {
        List<int[]> ints = new ArrayList<>();
        int[] a = {5, 2};
        ints.add(a);
        int[] b = {4, 9};
        ints.add(b);
        int[] c = {3, 6};
        ints.add(c);

        System.out.println(getIntersection(ints));
    }

    public static int[] getIntersection(List<int[]> ints) {
        int[] result = null;


        return result;
    }


}


/**
 * 欢迎参加平安产险数据平台测试组的面试！
 *
 * 本次考试共5道题，为后续工作强相关内容，每题1分， 总计5分，通过分为3分。
 * 答题时间50分钟。考试不能代表全部，如果您有其他方面的特长，请在后续面试充分展现自己，谢谢！
 * 联系人 钉钉或微信 pythontesting
 *
 * 1,SQL基础题
 *
 *
 * 选择OrderItems表中产品名prod_id为C-Z开头的行(注意prod_id还有一些是_或中文等开头的)。：
 *
 * 预期结果
 * +-----------+------------+---------+----------+------------+
 * | order_num | order_item | prod_id | quantity | item_price |
 * +-----------+------------+---------+----------+------------+
 * |     20007 |          5 | RGAN01  |       50 |       4.49 |
 * |     20008 |          1 | RGAN01  |        5 |       4.99 |
 * +-----------+------------+---------+----------+------------+
 *
 *
 *
 *
 * 数据库参考（只需关注题目中提及的表即可）：
 *
 * mysql> select * from Customers;
 * +------------+---------------+----------------------+-----------+------------+----------+--------------+--------------------+-----------------------+
 * | cust_id    | cust_name     | cust_address         | cust_city | cust_state | cust_zip | cust_country | cust_contact       | cust_email            |
 * +------------+---------------+----------------------+-----------+------------+----------+--------------+--------------------+-----------------------+
 * | 1000000001 | Village Toys  | 200 Maple Lane       | Detroit   | MI         | 44444    | USA          | John Smith         | sales@villagetoys.com |
 * | 1000000002 | Kids Place    | 333 South Lake Drive | Columbus  | OH         | 43333    | USA          | Michelle Green     | NULL                  |
 * | 1000000003 | Fun4All       | 1 Sunny Place        | Muncie    | IN         | 42222    | USA          | Jim Jones          | jjones@fun4all.com    |
 * | 1000000004 | Fun4All       | 829 Riverside Drive  | Phoenix   | AZ         | 88888    | USA          | Denise L. Stephens | dstephens@fun4all.com |
 * | 1000000005 | The Toy Store | 4545 53rd Street     | Chicago   | IL         | 54545    | USA          | Kim Howard         | NULL                  |
 * +------------+---------------+----------------------+-----------+------------+----------+--------------+--------------------+-----------------------+
 *
 * mysql> select * from OrderItems;
 * +-----------+------------+---------+----------+------------+
 * | order_num | order_item | prod_id | quantity | item_price |
 * +-----------+------------+---------+----------+------------+
 * |     20005 |          1 | BR01    |      100 |       5.49 |
 * |     20005 |          2 | BR03    |      100 |      10.99 |
 * |     20006 |          1 | BR01    |       20 |       5.99 |
 * |     20006 |          2 | BR02    |       10 |       8.99 |
 * |     20006 |          3 | BR03    |       10 |      11.99 |
 * |     20007 |          1 | BR03    |       50 |      11.49 |
 * |     20007 |          2 | BNBG01  |      100 |       2.99 |
 * |     20007 |          3 | BNBG02  |      100 |       2.99 |
 * |     20007 |          4 | BNBG03  |      100 |       2.99 |
 * |     20007 |          5 | RGAN01  |       50 |       4.49 |
 * |     20008 |          1 | RGAN01  |        5 |       4.99 |
 * |     20008 |          2 | BR03    |        5 |      11.99 |
 * |     20008 |          3 | BNBG01  |       10 |       3.49 |
 * |     20008 |          4 | BNBG02  |       10 |       3.49 |
 * |     20008 |          5 | BNBG03  |       10 |       3.49 |
 * |     20009 |          1 | BNBG01  |      250 |       2.49 |
 * |     20009 |          2 | BNBG02  |      250 |       2.49 |
 * |     20009 |          3 | BNBG03  |      250 |       2.49 |
 * +-----------+------------+---------+----------+------------+
 *
 * mysql> select * from Orders;
 * +-----------+---------------------+------------+
 * | order_num | order_date          | cust_id    |
 * +-----------+---------------------+------------+
 * |     20005 | 2012-05-01 00:00:00 | 1000000001 |
 * |     20006 | 2012-01-12 00:00:00 | 1000000003 |
 * |     20007 | 2012-01-30 00:00:00 | 1000000004 |
 * |     20008 | 2012-02-03 00:00:00 | 1000000005 |
 * |     20009 | 2012-02-08 00:00:00 | 1000000001 |
 * +-----------+---------------------+------------+
 *
 * mysql> select * from Products;
 * +---------+---------+---------------------+------------+-----------------------------------------------------------------------+
 * | prod_id | vend_id | prod_name           | prod_price | prod_desc                                                             |
 * +---------+---------+---------------------+------------+-----------------------------------------------------------------------+
 * | BNBG01  | DLL01   | Fish bean bag toy   |       3.49 | Fish bean bag toy, complete with bean bag worms with which to feed it |
 * | BNBG02  | DLL01   | Bird bean bag toy   |       3.49 | Bird bean bag toy, eggs are not included                              |
 * | BNBG03  | DLL01   | Rabbit bean bag toy |       3.49 | Rabbit bean bag toy, comes with bean bag carrots                      |
 * | BR01    | BRS01   | 8 inch teddy bear   |       5.99 | 8 inch teddy bear, comes with cap and jacket                          |
 * | BR02    | BRS01   | 12 inch teddy bear  |       8.99 | 12 inch teddy bear, comes with cap and jacket                         |
 * | BR03    | BRS01   | 18 inch teddy bear  |      11.99 | 18 inch teddy bear, comes with cap and jacket                         |
 * | RGAN01  | DLL01   | Raggedy Ann         |       4.99 | 18 inch Raggedy Ann doll                                              |
 * | RYL01   | FNG01   | King doll           |       9.49 | 12 inch king doll with royal garments and crown                       |
 * | RYL02   | FNG01   | Queen doll          |       9.49 | 12 inch queen doll with royal garments and crown                      |
 * +---------+---------+---------------------+------------+-----------------------------------------------------------------------+
 *
 * mysql> select * from Vendors;
 * +---------+-----------------+-----------------+------------+------------+----------+--------------+
 * | vend_id | vend_name       | vend_address    | vend_city  | vend_state | vend_zip | vend_country |
 * +---------+-----------------+-----------------+------------+------------+----------+--------------+
 * | BRE02   | Bear Emporium   | 500 Park Street | Anytown    | OH         | 44333    | USA          |
 * | BRS01   | Bears R Us      | 123 Main Street | Bear Town  | MI         | 44444    | USA          |
 * | DLL01   | Doll House Inc. | 555 High Street | Dollsville | CA         | 99999    | USA          |
 * | FNG01   | Fun and Games   | 42 Galaxy Road  | London     | NULL       | N16 6PS  | England      |
 * | FRB01   | Furball Inc.    | 1000 5th Avenue | New York   | NY         | 11111    | USA          |
 * | JTS01   | Jouets et ours  | 1 Rue Amusement | Paris      | NULL       | 45678    | France       |
 * +---------+-----------------+-----------------+------------+------------+----------+--------------+
 *
 *
 *
 * 2,编程题 -- 此题如能在本机(unbuntu)上调试出来更佳。
 * 某项目要求字符串只能包含a-zA-Z0-9，
 *     请使用linux命令、shell或python、java等实现字符串检查,以python为例
 * $ python test.py test
 * True
 * $ python test.py test@
 * False
 *
 *
 *
 *
 * 3,测试点设计: 请列出至少15个测试点
 * ubuntu 20.04安装mysql
 *
 *
 * 4,测试基础题
 * 性能测试需要关注哪些指标，请列出至少5个，并简述含义?列出3个你在性能测试中发现的问题。性能测试如果发现问题？
 *
 * 5.描述下你测试过产品的前后台架构，建议画图描述, 可以手写拍照
 * 描述你在上述产品你发现的跨至少３个模块的bug，至少３个。
 * 描述下你常去的国外的能解决问题的网站。
 *
 *
 * 6.个人信息收集:
 * 优点\缺点\未来3年内的规划\现在月薪及年薪\要求的月薪及年薪\对加班怎么看\住所(主要看到公司有多远)请提出一些独到的测试想法，比如你近期申请过或准备申请的测试专利。
 */
class RegexDemo {
    public static void main(String[] args) {

        System.out.println(validateRegex("test@"));
        System.out.println(validateRegex("test"));

    }

    public static boolean validateRegex(String text) {
        String regEx = "^[0-9a-zA-Z]*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}