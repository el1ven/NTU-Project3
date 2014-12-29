package DataBase;

import Bean.GrantBean;
import Bean.SchoolBean;
import Bean.UserBean;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;

/**
 * Created by el1ven on 25/9/14.
 */
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.util.Date;

public class DBManager{//设计成单例模式

    private static DBManager managerDB = null;//单例实例变量

    private static SimpleConnectionPool connPool;
    private PreparedStatement pstat = null;
    private PreparedStatement pstat2 = null;
    private PreparedStatement pstat3 = null;
    private PreparedStatement pstat4 = null;
    private PreparedStatement pstat5 = null;


    private DBManager(){//私有化的构造方法，保证外部的类不能通过构造器来实例化
        connPool = new SimpleConnectionPool();//初始化连接池对象
    }

    public static DBManager getInstance(){//获取单例对象实例
        if(managerDB == null){
            managerDB = new DBManager();
        }
        return managerDB;
    }

    public ArrayList<SchoolBean> queryForSch() throws Exception{

        ArrayList<SchoolBean> schList = new ArrayList<SchoolBean>();
        Connection conn = connPool.getConnection();
        String sql1 = "select * from School";
        pstat = conn.prepareStatement(sql1);
        ResultSet rs = pstat.executeQuery();
        while(rs.next()){
            SchoolBean sch = new SchoolBean();
            sch.setSchId(rs.getInt(1));
            sch.setSchName(rs.getString("name"));
            schList.add(sch);
        }
        //System.out.println("School Size: "+schList.size());
        conn.close();
        rs.close();
        return schList;
    }

    public int queryForLogin(UserBean user) throws Exception{
        Connection conn = connPool.getConnection();
        String sql = "select * from User where name=? and password=?";
        pstat = conn.prepareStatement(sql);
        pstat.setString(1,user.getUserName());
        pstat.setString(2,user.getUserPassword());
        ResultSet rs = pstat.executeQuery();
        if(rs.next()){//登录成功
            connPool.pushConnectionBackToPool(conn);
            return 1;
        }else{//登录失败，没有这个用户名，重新注册
            connPool.pushConnectionBackToPool(conn);
            return -1;
        }
    }

    public String queryForType(UserBean user) throws Exception{
        Connection conn = connPool.getConnection();
        String sql = "select * from User where name=? and password=?";
        pstat = conn.prepareStatement(sql);
        pstat.setString(1,user.getUserName());
        pstat.setString(2,user.getUserPassword());
        ResultSet rs = pstat.executeQuery();
        if(rs.next()){//登录成功
            connPool.pushConnectionBackToPool(conn);
            return rs.getString("type");
        }else{//登录失败，没有这个用户名，重新注册
            connPool.pushConnectionBackToPool(conn);
            return "NULL";
        }
    }

    public int queryForRegister(UserBean user)throws Exception{
        Connection conn = connPool.getConnection();
        String sql = "select * from User where name=?";
        pstat = conn.prepareStatement(sql);
        pstat.setString(1, user.getUserName());
        ResultSet rs = pstat.executeQuery();
        if(rs.next()){//用户名已经重复，注册失败
            connPool.pushConnectionBackToPool(conn);
            return -1;
        }else{//用户名没有重复继续注册
            String sql1 = "insert into User(name,password,email,type)values(?,?,?,?)";
            pstat = conn.prepareStatement(sql1);
            pstat.setString(1,user.getUserName());
            pstat.setString(2,user.getUserPassword());
            pstat.setString(3,user.getUserEmail());
            pstat.setString(4,user.getUserType());
            //pstat.setString(5,user.getUserSchool1());
            //pstat.setString(6,user.getUserSchool2());
            //pstat.setString(7,user.getUserSchool3());
            pstat.executeUpdate();

            try{

                ArrayList<String> schList = new ArrayList<String>();
                schList.add(user.getUserSchool1());
                schList.add(user.getUserSchool2());
                schList.add(user.getUserSchool3());
                //System.out.println("!!!!   "+schList.size());


                for(int i = 0; i < 3; i++){
                    String sql2 = "insert into UrelateS(uid, sid)values(?,?)";
                    int userIdOfInt = DBManager.getInstance().getUserIdByName(user.getUserName());
                    //System.out.println("School Name   "+schList.get(i));
                    int schIdOfInt = DBManager.getInstance().getSchoolIdByName(schList.get(i));
                    pstat = conn.prepareStatement(sql2);
                    pstat.setInt(1, userIdOfInt);
                    pstat.setInt(2, schIdOfInt);
                    pstat.executeUpdate();
                    //System.out.println("!!!!!!!!!!!!!!!!!!!+  "+i);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            //防止上传文件的覆盖问题，只要用户第一次注册成功就会在upload下面创建一个具有改id的
            int count = 1;
            if(count == 1){
                String sql3 = "select * from User where name=?";
                pstat = conn.prepareStatement(sql3);
                pstat.setString(1, user.getUserName());
                ResultSet rs2 = pstat.executeQuery();
                rs2.next();
                int userId = rs2.getInt(1);
                String userIdOfString = String.valueOf(userId);
                String userType = rs2.getString("type");
                rs.close();
                if(!userType.equals("FM")){
                    String basePath = ServletActionContext.getServletContext().getRealPath("/");
                    String finalPath = basePath + "upload/" + userIdOfString;//获取文件上传路径
                    File file = new File(finalPath);
                    file.mkdir();
                }
                count = 2;
            }

            connPool.pushConnectionBackToPool(conn);
            pstat = null;
            rs.close();

            return 1;
        }
    }

    public String queryForLastestDeadline(String currentTime) throws Exception{
        Connection conn = connPool.getConnection();
        String sql1 = "select * from ResearchGrant where status='fresh'";
        pstat = conn.prepareStatement(sql1);
        ResultSet rs1 = pstat.executeQuery();
        while(rs1.next()){
            //针对每一个RG的deadline找出最近的时间和当前时间对比
            int rgid = rs1.getInt("rgid");
            String sql2 = "select * from Deadline where rgid=? order by dtime desc";
            pstat2 = conn.prepareStatement(sql2);
            pstat2.setInt(1, rgid);
            ResultSet rs2 = pstat2.executeQuery();
            rs2.next();//只取一条最近的一条
            String lastestDeadline = rs2.getString("dtime");
            rs2.close();

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = format.parse(lastestDeadline);
            Date d2 = format.parse(currentTime);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(d1);
            c2.setTime(d2);
            if(d1.before(d2)||d1.equals(d2)){
                //过期要修改数据库字段为“outdate”
                String sql3 = "update ResearchGrant set status = 'outdate' where rgid=?";
                pstat3 = conn.prepareStatement(sql3);
                pstat3.setInt(1, rgid);
                pstat3.executeUpdate();
            }
        }
        rs1.close();
        pstat = null;
        pstat = null;
        pstat = null;
        connPool.pushConnectionBackToPool(conn);
        return "success";
    }

    public int grantPost(GrantBean grant)throws Exception{
        Connection conn = connPool.getConnection();

        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userName = session.get("userName").toString();//获取sesssion中的用户名，可以根据这个查询用户的ID
        String userIdOfString = String.valueOf(DBManager.getInstance().getUserIdByName(userName));

        //第一步先添加Research Grant这个表
        String timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String sql1 = "insert into ResearchGrant(status,title,series,contact,content,present)values(?,?,?,?,?,?)";
        pstat = conn.prepareStatement(sql1);
        pstat.setString(1, grant.getGrantStatus());
        pstat.setString(2, grant.getGrantTitle());
        pstat.setString(3, grant.getGrantSeries());
        pstat.setString(4, grant.getGrantContact());
        pstat.setString(5, grant.getGrantContent());
        pstat.setString(6, timeNow);//grant 发布时间
        pstat.executeUpdate();

        //利用插入时间来获取发布的rgid
        String sql2 = "select rgid from ResearchGrant where present=?";
        pstat = conn.prepareStatement(sql2);
        pstat.setString(1, timeNow);
        ResultSet rs = pstat.executeQuery();
        int rgid = 0;
        while(rs.next()) {
            rgid = rs.getInt(1);
        }

        //根据rgid来插入文件名

        String basePath = ServletActionContext.getServletContext().getRealPath("/");
        String finalPath = basePath + "upload/" + userIdOfString + "/";//获取文件上传路径
        String fileName = "";
        String filePath = "";

        if(grant.getUpload() != null) {//上传文件不为空
            for (int i = 0; i < grant.getUpload().size(); i++) {

                //文件多个上传到文件夹
                InputStream is = new FileInputStream(grant.getUpload().get(i));
                fileName = grant.getUploadFileName().get(i);
                filePath = finalPath + fileName;
                OutputStream os = new FileOutputStream(filePath);
                byte[] buffer = new byte[8192];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    os.write(buffer, 0, count);
                }
                os.close();
                is.close();

                //文件名和文件路径写入数据库
                String sql3 = "insert into File(rgid,fileName,filePath)values(?,?,?)";
                pstat = conn.prepareStatement(sql3);
                pstat.setInt(1, rgid);
                pstat.setString(2, fileName);
                pstat.setString(3, filePath);
                pstat.executeUpdate();
            }
        }

        //把多个deadline数据插入到数据库中
        if(grant.getDeadlineDates()!=null){
            for(int i = 0; i < grant.getDeadlineDates().size(); i++){
                String deadlineDate = grant.getDeadlineDates().get(i);
                String deadlineContent = grant.getDeadlineContents().get(i);
                String sql4 = "insert into Deadline(rgid, dtime, description)values(?,?,?)";
                pstat = conn.prepareStatement(sql4);
                pstat.setInt(1, rgid);
                pstat.setString(2, deadlineDate);
                pstat.setString(3, deadlineContent);
                pstat.executeUpdate();
            }
        }

        //把user和grant对应关系存入UmanageRG表中，即纪录哪个人发布了这个research grant
        int userId = 0;

        String sql5 = "select uid from User where name=?";
        pstat = conn.prepareStatement(sql5);
        pstat.setString(1, userName);

        ResultSet rs2 = pstat.executeQuery();
        while(rs2.next()){
            userId = rs2.getInt(1);
            //System.out.println(userId);
        }

        //把关系插入到UmanageRG之中
        String sql6 = "insert into UmanageRG(uid, rgid)values(?,?)";
        pstat = conn.prepareStatement(sql6);
        pstat.setInt(1, userId);
        pstat.setInt(2, rgid);
        pstat.executeUpdate();

        return 1;//发布成功
    }


    public int grantPostAfterModify(GrantBean grant)throws Exception{//修改之后重新发布research grant信息

        Connection conn = connPool.getConnection();

        int grantId = Integer.parseInt(grant.getGrantIdOfStr());
        String sql1 = "update ResearchGrant set title=?, series=?, content=?, contact=? where rgid=?";
        pstat = conn.prepareStatement(sql1);
        pstat.setString(1, grant.getGrantTitle());
        pstat.setString(2, grant.getGrantSeries());
        pstat.setString(3, grant.getGrantContent());
        pstat.setString(4, grant.getGrantContact());
        pstat.setInt(5, grantId);
        pstat.executeUpdate();


        //根据rgid来插入文件名
        String userIdOfString = String.valueOf(DBManager.getInstance().getUserIdByRGID(grantId));

        String basePath = ServletActionContext.getServletContext().getRealPath("/");
        String finalPath = basePath + "upload/" + userIdOfString + "/";//获取文件上传路径
        String fileName = "";
        String filePath = "";

        if(grant.getUpload() != null) {//上传文件不为空
            for (int i = 0; i < grant.getUpload().size(); i++) {

                //文件多个上传到文件夹
                InputStream is = new FileInputStream(grant.getUpload().get(i));
                fileName = grant.getUploadFileName().get(i);
                filePath = finalPath + fileName;
                OutputStream os = new FileOutputStream(filePath);
                byte[] buffer = new byte[8192];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    os.write(buffer, 0, count);
                }
                os.close();
                is.close();

                //文件名和文件路径写入数据库
                String sql3 = "insert into File(rgid,fileName,filePath)values(?,?,?)";
                pstat = conn.prepareStatement(sql3);
                pstat.setInt(1, grantId);
                pstat.setString(2, fileName);
                pstat.setString(3, filePath);
                pstat.executeUpdate();
            }
        }

        //把多个deadline数据插入到数据库中
        if(grant.getDeadlineDates()!=null){
            for(int i = 0; i < grant.getDeadlineDates().size(); i++){
                String deadlineDate = grant.getDeadlineDates().get(i);
                String deadlineContent = grant.getDeadlineContents().get(i);
                String sql4 = "insert into Deadline(rgid, dtime, description)values(?,?,?)";
                pstat = conn.prepareStatement(sql4);
                pstat.setInt(1, grantId);
                pstat.setString(2, deadlineDate);
                pstat.setString(3, deadlineContent);
                pstat.executeUpdate();
            }
        }

        pstat = null;
        connPool.pushConnectionBackToPool(conn);

        return 1;
    }



    //取出要分页显示的元素放在grantList之中返回
    public ArrayList<GrantBean> grantShowRSO(int pageNow, int pageSize) throws Exception{

        //System.out.println(pageNow+"*"+pageSize);

        String latestDate = "";//最近deadline日期
        String schStr = "";//存储发表这个grant的教授所在的学院
        //String peopleCountOfString = "";//订阅人数统计,字符串类型
        int peopleCountOfInt = 0;//订阅人数统计,整数类型

        //查询用户类型，有的不需要显示deadline和file有的需要
        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userType = session.get("userType").toString();
        String userName = session.get("userName").toString();//获取sesssion中的用户名，可以根据这个查询提交RG的教授所在的学院
        //System.out.println("UserName--"+userName);

        Connection conn = connPool.getConnection();
        Connection conn2 = connPool.getNewConnection();
        Connection conn3 = connPool.getNewConnection();
        ArrayList<GrantBean> grantList = new ArrayList<GrantBean>();
        String sql = "select * from ResearchGrant where status='fresh' limit ?,? ";//只有没过期的grant才能被选择被显示
        pstat = conn.prepareStatement(sql);
        //noinspection JpaQueryApiInspection
        pstat.setInt(1, (pageNow-1)*pageSize);
        pstat.setInt(2, pageNow*pageSize);
        ResultSet rs = pstat.executeQuery();

        //int count = 1;//防止多次循环
        int rgid = 0;

        while(rs.next()){

            GrantBean g = new GrantBean();//简略的显示信息
            rgid = rs.getInt("rgid");
            g.setGrantId(rgid);
            g.setGrantTitle(rs.getString("title"));
            g.setGrantSeries(rs.getString("series"));
            g.setGrantWhen(rs.getString("present"));
            g.setGrantStatus(rs.getString("status"));

            String sql1 = "select uid from UsubscribeRG where rgid=?";
            pstat = conn.prepareStatement(sql1);
            pstat.setInt(1, rgid);
            ResultSet rs1 = pstat.executeQuery();
            while(rs1.next()){
                peopleCountOfInt++;
            }
            String peopleCount = String.valueOf(peopleCountOfInt);
            peopleCountOfInt = 0;//复原

            g.setGrantPeopleCount(peopleCount);

            //查出id,以此来查出deadline和file
            if(userType.equals("RSO")) {
                try {

                    String sql2 = "select * from Deadline where rgid=? order by dtime desc ";
                    pstat2 = conn2.prepareStatement(sql2);
                    pstat2.setInt(1, rgid);
                    ResultSet rs2 = pstat2.executeQuery();
                    while (rs2.next()) {
                        latestDate = rs2.getString("dtime");
                        //g.setHurryDeadline(latestDate);
                        //这种方法可以查出最近的deadline
                    }

                    rs2.close();

                    int userId = DBManager.getInstance().getUserIdByRGID(rgid);

                    String sql3 = "select name from UrelateS a, School b where a.uid = ? and a.sid = b.sid";//根据session提供的姓名来查询提交者所在的学院
                    pstat3 = conn3.prepareStatement(sql3);
                    pstat3.setInt(1, userId);
                    ResultSet rs3 = pstat3.executeQuery();
                    ArrayList<String> sch = new ArrayList<String>();

                    /*查询发表这个research grant的学院*/
                    while (rs3.next()) {
                        String schName = rs3.getString("name");
                        sch.add(schName);
                    }
                    for(int i = 0; i < sch.size(); i++){
                        if(!(sch.get(i)).equals("None")){//用这个函数来判断字符串是否相等
                            schStr += sch.get(i)+" ";
                        }
                    }
                    rs3.close();
                    /*查询发表这个research grant的学院*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            g.setHurryDeadline(latestDate);//注入最近截至日期
            g.setGrantSchool(schStr);//注入发表这个grant的教授的学院，已经组合成一个字符串
            schStr = "";

            grantList.add(g);//这步已经获取到分页之前的元素

        }

        rs.close();

        connPool.pushConnectionBackToPool(conn);
        connPool.pushConnectionBackToPool(conn2);
        connPool.pushConnectionBackToPool(conn3);

        return grantList;
    }

    //取出要分页显示的元素放在grantList之中返回
    public ArrayList<GrantBean> grantShowRSO2(int pageNow, int pageSize) throws Exception{

        //System.out.println(pageNow+"*"+pageSize);

        String latestDate = "";//最近deadline日期
        String schStr = "";//存储发表这个grant的教授所在的学院
        //String peopleCountOfString = "";//订阅人数统计,字符串类型
        int peopleCountOfInt = 0;//订阅人数统计,整数类型

        //查询用户类型，有的不需要显示deadline和file有的需要
        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userType = session.get("userType").toString();
        String userName = session.get("userName").toString();//获取sesssion中的用户名，可以根据这个查询提交RG的教授所在的学院
        //System.out.println("UserName--"+userName);

        Connection conn = connPool.getConnection();
        Connection conn2 = connPool.getNewConnection();
        Connection conn3 = connPool.getNewConnection();
        ArrayList<GrantBean> grantList = new ArrayList<GrantBean>();
        String sql = "select * from ResearchGrant where status='outdate' limit ?,? ";//只有没过期的grant才能被选择被显示
        pstat = conn.prepareStatement(sql);
        //noinspection JpaQueryApiInspection
        pstat.setInt(1, (pageNow-1)*pageSize);
        pstat.setInt(2, pageNow*pageSize);
        ResultSet rs = pstat.executeQuery();

        //int count = 1;//防止多次循环
        int rgid = 0;

        while(rs.next()){

            GrantBean g = new GrantBean();//简略的显示信息
            rgid = rs.getInt("rgid");
            g.setGrantId(rgid);
            g.setGrantTitle(rs.getString("title"));
            g.setGrantSeries(rs.getString("series"));
            g.setGrantWhen(rs.getString("present"));
            g.setGrantStatus(rs.getString("status"));

            String sql1 = "select uid from UsubscribeRG where rgid=?";
            pstat = conn.prepareStatement(sql1);
            pstat.setInt(1, rgid);
            ResultSet rs1 = pstat.executeQuery();
            while(rs1.next()){
                peopleCountOfInt++;
            }
            String peopleCount = String.valueOf(peopleCountOfInt);
            peopleCountOfInt = 0;//复原

            g.setGrantPeopleCount(peopleCount);

            //查出id,以此来查出deadline和file
            if(userType.equals("RSO")) {
                try {

                    String sql2 = "select * from Deadline where rgid=? order by dtime desc ";
                    pstat2 = conn2.prepareStatement(sql2);
                    pstat2.setInt(1, rgid);
                    ResultSet rs2 = pstat2.executeQuery();
                    while (rs2.next()) {
                        latestDate = rs2.getString("dtime");
                        //g.setHurryDeadline(latestDate);
                        //这种方法可以查出最近的deadline
                    }

                    rs2.close();

                    int userId = DBManager.getInstance().getUserIdByRGID(rgid);

                    String sql3 = "select name from UrelateS a, School b where a.uid = ? and a.sid = b.sid";//根据session提供的姓名来查询提交者所在的学院
                    pstat3 = conn3.prepareStatement(sql3);
                    pstat3.setInt(1, userId);
                    ResultSet rs3 = pstat3.executeQuery();
                    ArrayList<String> sch = new ArrayList<String>();

                    /*查询发表这个research grant的学院*/
                    while (rs3.next()) {
                        String schName = rs3.getString("name");
                        sch.add(schName);
                    }
                    for(int i = 0; i < sch.size(); i++){
                        if(!(sch.get(i)).equals("None")){//用这个函数来判断字符串是否相等
                            schStr += sch.get(i)+" ";
                        }
                    }
                    rs3.close();
                    /*查询发表这个research grant的学院*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            g.setHurryDeadline(latestDate);//注入最近截至日期
            g.setGrantSchool(schStr);//注入发表这个grant的教授的学院，已经组合成一个字符串
            schStr = "";

            grantList.add(g);//这步已经获取到分页之前的元素

        }

        rs.close();

        connPool.pushConnectionBackToPool(conn);
        connPool.pushConnectionBackToPool(conn2);
        connPool.pushConnectionBackToPool(conn3);

        return grantList;
    }

    public List<Map<String,Object>> getSubscirbeData(String rgid)throws Exception{

        int rgidOfInt = Integer.parseInt(rgid);
        Connection conn = connPool.getConnection();

        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();




        try {

            /*查询每个学院订阅这个rg的人和所在的学院统计,用json做！！！*/
            //step1:现把学院选择出来并且去除None的学院

            String sql1 = "select * from School where name !='None'";
            pstat = conn.prepareStatement(sql1);
            ResultSet rs1 = pstat.executeQuery();

            while (rs1.next()) {//统计每个学院订阅的人数


                //SubscribeBean s = new SubscribeBean();
                String sql2 = "select count(*) from UsubscribeRG a, UrelateS b, School c WHERE a.uid=b.uid and b.sid=c.sid and c.name=? and a.rgid=?";
                String schName = rs1.getString("name");
                pstat = conn.prepareStatement(sql2);
                pstat.setString(1, schName);
                pstat.setInt(2, rgidOfInt);

                ResultSet rs2 = pstat.executeQuery();
                rs2.next();
                if(!(rs2.getString(1)).equals("0")) {
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("name",schName);
                    map.put("number",rs2.getString(1));
                    list.add(map);
                }
                rs2.close();
                /*查询每个学院订阅这个rg的人和所在的学院统计*/
            }

            rs1.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        conn.close();
        connPool.pushConnectionBackToPool(conn);
        return list;

    }





    public ArrayList<GrantBean> grantShowFM1(int pageNow, int pageSize) throws Exception {

        ArrayList<GrantBean> grantList = new ArrayList<GrantBean>();

        try {

            ActionContext context= ActionContext.getContext();
            Map session = (Map)context.getSession();
            String userName = session.get("userName").toString();//获取session中的用户名，可以根据这个查询提交RG的教授所在的学院
            int userId =  getUserIdByName(userName);//获取用户ID


            Connection conn = connPool.getConnection();
            String sql1 = "select * from ResearchGrant where status = 'fresh' limit ?,?";
            pstat = conn.prepareStatement(sql1);
            //noinspection JpaQueryApiInspection
            pstat.setInt(1, (pageNow-1)*pageSize);
            //noinspection JpaQueryApiInspection
            pstat.setInt(2, pageNow*pageSize);
            ResultSet rs1 = pstat.executeQuery();
            while(rs1.next()){
                GrantBean g = new GrantBean();//封装信息

                int rgid = rs1.getInt("rgid");
                String sql2 = "select * from UsubscribeRG where uid=? and rgid=?";
                pstat2 = conn.prepareStatement(sql2);
                pstat2.setInt(1, userId);
                //noinspection JpaQueryApiInspection
                pstat2.setInt(2, rgid);
                ResultSet rs2 = pstat2.executeQuery();
                if(rs2.next()){
                    g.setGrantChecked("disabled");
                }else{
                    g.setGrantChecked("abled");
                }

                rs2.close();

                g.setGrantId(rs1.getInt("rgid"));
                g.setGrantTitle(rs1.getString("title"));
                g.setGrantSeries(rs1.getString("series"));
                grantList.add(g);
            }

            rs1.close();

            connPool.pushConnectionBackToPool(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }



        return grantList;
    }

    public ArrayList<GrantBean> grantShowFM2() throws Exception {

        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userName = session.get("userName").toString();//获取sesssion中的用户名，可以根据这个查询提交RG的教授所在的学院
        int userId =  getUserIdByName(userName);//获取用户ID


        ArrayList<GrantBean> grantList = new ArrayList<GrantBean>();
        Connection conn = connPool.getConnection();
        String sql1 = "select * from User a, ResearchGrant b, UsubscribeRG c where b.status = 'fresh' and a.uid = c.uid and b.rgid = c.rgid and c.uid = ?";
        pstat = conn.prepareStatement(sql1);
        pstat.setInt(1, userId);
        ResultSet rs1 = pstat.executeQuery();
        while(rs1.next()){
            GrantBean g = new GrantBean();//封装信息
            g.setGrantId(rs1.getInt("rgid"));
            g.setGrantTitle(rs1.getString("title"));
            g.setGrantSeries(rs1.getString("series"));
            grantList.add(g);
        }

        rs1.close();

        connPool.pushConnectionBackToPool(conn);

        return grantList;
    }

    public int addToListByFM(String [] grantIdArr, String userName) throws Exception{// faculty member 把物品放在购物车

        int grantIdArrLenth = grantIdArr.length;

         //注意针对每个物品购物车只能放一次
        Connection conn = connPool.getConnection();

        for(int i = 0; i < grantIdArrLenth; i++){

            String sql1 = "select * from User where name=?";//现根据传来的唯一用户名获取用户的ID值
            pstat = conn.prepareStatement(sql1);
            pstat.setString(1, userName);
            ResultSet rs1 = pstat.executeQuery();
            rs1.next();//否则为空
            int userId = rs1.getInt(1);
            int grantIdOfInt = Integer.parseInt(grantIdArr[i]);

            String sql2 = "select * from UsubscribeRG where uid = ? and rgid = ?";
            pstat = conn.prepareStatement(sql2);
            pstat.setInt(1, userId);
            pstat.setInt(2, grantIdOfInt);
            ResultSet rs2 = pstat.executeQuery();
            if(rs2.next()){//证明已经订阅过了，无法再次订阅
                return -1;
            }

            String sql3 = "insert into UsubscribeRG(uid, rgid)values(?,?)";
            pstat = conn.prepareStatement(sql3);
            pstat.setInt(1, userId);
            pstat.setInt(2, grantIdOfInt);
            pstat.executeUpdate();

        }

        return 1;//插入成功
    }

    //查询grant的总数，用于分页
    public int grantCount()throws Exception{
        Connection conn = connPool.getConnection();
        String sql1 = "select count(*) from ResearchGrant where status='fresh' ";
        pstat = conn.prepareStatement(sql1);
        ResultSet rs1 = pstat.executeQuery();
        rs1.next();//游标指向第一行
        int i = rs1.getInt(1);//获取总行数
        rs1.close();
        connPool.pushConnectionBackToPool(conn);
        pstat = null;
        return i;
    }

    //RSO对指定ID的grant进行删除
    public int grantDeleteByRSO(String[] grantIds)throws Exception{//SRO和这个一样

        Connection conn = connPool.getConnection();
        int lenth = grantIds.length;

        for(int i = 0; i < lenth; i++){
            int id = Integer.parseInt(grantIds[i]);//把string类型转换成整形
            String sql1 = "delete from ResearchGrant where rgid=?";
            pstat = conn.prepareStatement(sql1);
            pstat.setInt(1, id);
            pstat.executeUpdate();
        }

        connPool.pushConnectionBackToPool(conn);
        pstat=null;

        return 1;
    }


    //FM对指定ID的grant进行删除
    public int grantDeleteByFM(int userId, String [] grantIds)throws Exception{

        Connection conn = connPool.getConnection();
        int lenth = grantIds.length;

        for(int i = 0; i < lenth; i++){
            int grantIdOfInt = Integer.parseInt(grantIds[i]);//把string类型转换成整形
            String sql1 = "delete from UsubscribeRG where uid=? and rgid=?";
            pstat = conn.prepareStatement(sql1);
            pstat.setInt(1, userId);
            pstat.setInt(2, grantIdOfInt);
            pstat.executeUpdate();
        }

        connPool.pushConnectionBackToPool(conn);
        pstat = null;

        return 1;
    }

    //修改界面的deadline删除功能
    public int deleteDeadline(String did)throws Exception{

        Connection conn = connPool.getConnection();
        int didOfInt = 0;

        try{
            didOfInt = Integer.parseInt(did);//把string类型转换成整形
            String sql1 = "delete from Deadline where did=?";
            pstat = conn.prepareStatement(sql1);
            pstat.setInt(1, didOfInt);
            pstat.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }

        connPool.pushConnectionBackToPool(conn);
        pstat = null;

        return 1;
    }

    //修改界面的file删除功能
    public int deleteFile(String fid, String grantIdOfStr)throws Exception{

        int fidOfInt = Integer.parseInt(fid);
        //int grantIdOfInt = (int)Integer.parseInt(grantIdOfStr);

        Connection conn = connPool.getConnection();

        int flagForDelete = 0;

        try{

            String sql1 = "select * from File where fid=?";
            pstat = conn.prepareStatement(sql1);
            pstat.setInt(1, fidOfInt);
            ResultSet rs1 = pstat.executeQuery();
            rs1.next();
            String filePath = rs1.getString("filePath");//需要到指定文件夹删除文件
            rs1.close();
            File file = new File(filePath);

            // 路径为文件且不为空则进行删除
            if (file.isFile() && file.exists()) {
                file.delete();
                flagForDelete = 1;
            }

            if(flagForDelete > 0){//文件删除成功开始删除数据库纪录

                String sql2 = "delete from File where fid=?";
                pstat2 = conn.prepareStatement(sql2);
                pstat2.setInt(1, fidOfInt);
                pstat2.executeUpdate();

            }

            pstat = null;
            pstat2 = null;

            }catch(Exception e){
            e.printStackTrace();
        }

        return 1;

    }

    public ArrayList<GrantBean> grantDetail(int grantId)throws Exception{

        ArrayList<GrantBean> grantList = new ArrayList<GrantBean>();

        String grantDeadlineIdOfDetail = "";
        String grantDeadlineOfDetail = "";
        String grantDeadlineDescriptionOfDetail = "";
        String grantFileIdOfDetail = "";
        String grantFilePathOfDetail = "";
        String grantFileNameOfDetail = "";

        //Map request=(Map) ActionContext.getContext().get("request");//创建request对象

        Connection conn = connPool.getConnection();
        String sql1 = "select * from ResearchGrant where rgid=? and status = 'fresh'";

        pstat = conn.prepareStatement(sql1);
        pstat.setInt(1, grantId);
        ResultSet rs1 = pstat.executeQuery();

        String signStr = "    ";

        while(rs1.next()){

            GrantBean g = new GrantBean();//封装信息
            g.setGrantId(rs1.getInt("rgid"));
            g.setGrantTitle(rs1.getString("title"));
            g.setGrantSeries(rs1.getString("series"));
            g.setGrantContact(rs1.getString("contact"));
            g.setGrantContent(rs1.getString("content"));

            String sql2 = "select * from Deadline where rgid=?";
            pstat2 = conn.prepareStatement(sql2);
            pstat2.setInt(1, grantId);
            ResultSet rs2 = pstat2.executeQuery();
            while (rs2.next()) {
                grantDeadlineIdOfDetail += String.valueOf(rs2.getInt("did")) + signStr;
                grantDeadlineOfDetail += rs2.getString("dtime") + signStr;
                grantDeadlineDescriptionOfDetail += rs2.getString("description") + signStr;
            }

            g.setGrantDeadlineIdOfDetail(grantDeadlineIdOfDetail);
            g.setGrantDeadlineOfDetail(grantDeadlineOfDetail);
            g.setGrantDeadlineDescriptionOfDetail(grantDeadlineDescriptionOfDetail);

            rs2.close();

            String sql3 = "select * from File where rgid=?";
            pstat3 = conn.prepareStatement(sql3);
            pstat3.setInt(1, grantId);
            ResultSet rs3 = pstat3.executeQuery();
            while(rs3.next()){
                grantFileIdOfDetail += String.valueOf(rs3.getInt("fid")) + signStr;
                grantFileNameOfDetail += rs3.getString("fileName") + signStr;
                grantFilePathOfDetail += rs3.getString("filePath") + signStr;
            }
            g.setGrantFileIdOfDetail(grantFileIdOfDetail);
            g.setGrantFileNameOfDetail(grantFileNameOfDetail);
            g.setGrantFilePathOfDetail(grantFilePathOfDetail);

            grantList.add(g);
            rs3.close();
        }

        rs1.close();

        connPool.pushConnectionBackToPool(conn);

        return grantList;
    }

    //显示outdate的detail
    public ArrayList<GrantBean> grantDetail2(int grantId)throws Exception{

        ArrayList<GrantBean> grantList = new ArrayList<GrantBean>();

        String grantDeadlineIdOfDetail = "";
        String grantDeadlineOfDetail = "";
        String grantDeadlineDescriptionOfDetail = "";
        String grantFileIdOfDetail = "";
        String grantFilePathOfDetail = "";
        String grantFileNameOfDetail = "";

        //Map request=(Map) ActionContext.getContext().get("request");//创建request对象

        Connection conn = connPool.getConnection();
        String sql1 = "select * from ResearchGrant where rgid=? and status = 'outdate'";

        pstat = conn.prepareStatement(sql1);
        pstat.setInt(1, grantId);
        ResultSet rs1 = pstat.executeQuery();

        String signStr = "    ";

        while(rs1.next()){

            GrantBean g = new GrantBean();//封装信息
            g.setGrantId(rs1.getInt("rgid"));
            g.setGrantTitle(rs1.getString("title"));
            g.setGrantSeries(rs1.getString("series"));
            g.setGrantContact(rs1.getString("contact"));
            g.setGrantContent(rs1.getString("content"));

            String sql2 = "select * from Deadline where rgid=?";
            pstat2 = conn.prepareStatement(sql2);
            pstat2.setInt(1, grantId);
            ResultSet rs2 = pstat2.executeQuery();
            while (rs2.next()) {
                grantDeadlineIdOfDetail += String.valueOf(rs2.getInt("did")) + signStr;
                grantDeadlineOfDetail += rs2.getString("dtime") + signStr;
                grantDeadlineDescriptionOfDetail += rs2.getString("description") + signStr;
            }

            g.setGrantDeadlineIdOfDetail(grantDeadlineIdOfDetail);
            g.setGrantDeadlineOfDetail(grantDeadlineOfDetail);
            g.setGrantDeadlineDescriptionOfDetail(grantDeadlineDescriptionOfDetail);

            rs2.close();

            String sql3 = "select * from File where rgid=?";
            pstat3 = conn.prepareStatement(sql3);
            pstat3.setInt(1, grantId);
            ResultSet rs3 = pstat3.executeQuery();
            while(rs3.next()){
                grantFileIdOfDetail += String.valueOf(rs3.getInt("fid")) + signStr;
                grantFileNameOfDetail += rs3.getString("fileName") + signStr;
                grantFilePathOfDetail += rs3.getString("filePath") + signStr;
            }
            g.setGrantFileIdOfDetail(grantFileIdOfDetail);
            g.setGrantFileNameOfDetail(grantFileNameOfDetail);
            g.setGrantFilePathOfDetail(grantFilePathOfDetail);

            grantList.add(g);
            rs3.close();
        }

        rs1.close();

        connPool.pushConnectionBackToPool(conn);

        return grantList;
    }

    //FM对指定ID的grant设置3个deadline
    public int deadlineAddByFM(int userId, String grantId, String deadline1, String deadline2, String deadline3)throws Exception{

        int grantIdOfInt = Integer.parseInt(grantId);//把string类型转换成整形

        Connection conn = connPool.getConnection();
        String sql1 = "select * from FMsetDeadline where uid =? and rgid=?";
        pstat = conn.prepareStatement(sql1);
        pstat.setInt(1, userId);
        pstat.setInt(2, grantIdOfInt);
        ResultSet rs1 = pstat.executeQuery();
        if(rs1.next()){//不能插入两次
            return -1;
        }
        String sql2 = "insert into FMsetDeadline(uid, rgid, deadline1, deadline2, deadline3)values(?,?,?,?,?) ";
        pstat = conn.prepareStatement(sql2);
        pstat.setInt(1, userId);
        pstat.setInt(2, grantIdOfInt);
        pstat.setString(3, deadline1);
        pstat.setString(4, deadline2);
        pstat.setString(5, deadline3);
        pstat.executeUpdate();

        return 1;
    }


    //通过用户名来查找用户ID
    public int getUserIdByName(String userName)throws Exception{

        Connection conn = connPool.getConnection();
        String sql1 = "select * from User where name=?";
        pstat = conn.prepareStatement(sql1);
        pstat.setString(1, userName);
        ResultSet rs1 = pstat.executeQuery();
        rs1.next();//游标指向第一行
        int userId = rs1.getInt(1);
        rs1.close();
        connPool.pushConnectionBackToPool(conn);
        return userId;//获取userID;
    }


    //通过学校名字找学校ID
    public int getSchoolIdByName(String schName)throws Exception{

        Connection conn = connPool.getConnection();
        String sql1 = "select * from School where name=?";
        pstat = conn.prepareStatement(sql1);
        pstat.setString(1, schName);
        ResultSet rs1 = pstat.executeQuery();
        rs1.next();//游标指向第一行
        int schId = rs1.getInt(1);
        System.out.println("School Id:  "+schId);
        //rs1.close();
        connPool.pushConnectionBackToPool(conn);
        return schId;//获取userID;
    }

    //通过RG来查找用户ID
    public int getUserIdByRGID(int rgid)throws Exception{

        Connection conn = connPool.getConnection();
        String sql1 = "select * from UmanageRG where rgid=?";
        pstat = conn.prepareStatement(sql1);
        pstat.setInt(1, rgid);
        ResultSet rs1 = pstat.executeQuery();
        rs1.next();//游标指向第一行
        int userId = rs1.getInt(2);
        rs1.close();
        connPool.pushConnectionBackToPool(conn);
        return  userId;//获取userID;
    }


}
