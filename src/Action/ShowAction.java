package Action;

import DataBase.*;
import Bean.GrantBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by el1ven on 28/10/14.
 */
public class ShowAction extends ActionSupport{

    private ArrayList<GrantBean> grantList = new ArrayList<GrantBean>();//用于页面显示的grant数据集合

    //用于分页的属性
    private int pageNow = 1;//当前页面，从第一页开始
    private int pageSize = 3;//页面数据大小
    private int rowCount;//总行数
    private int pageCount;//总页数

    public String showRSO() throws Exception{//完成RSO分页并显示出来结果
        Map request=(Map) ActionContext.getContext().get("request");//创建request对象

        this.rowCount = DBManager.getInstance().grantCount();
        if(rowCount%pageSize == 0){
            pageCount = rowCount/pageSize;
        }else{
            pageCount = rowCount/pageSize + 1;
        }
        if(pageNow <= 1){
            pageNow = 1;
        }
        if(pageNow >= pageCount){
            pageNow = pageCount;
        }

        //System.out.println(this.pageNow+" "+this.pageSize);

        ArrayList<GrantBean> list = DBManager.getInstance().grantShowRSO(pageNow,pageSize);

        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面显示

        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userType = session.get("userType").toString();
        //通过session来获取用户类型跳转到相应的显示界面

        return userType;//不同用户跳转到不同界面显示
    }

    public String showRSO2() throws Exception{//完成RSO2分页并显示出来结果
        Map request=(Map) ActionContext.getContext().get("request");//创建request对象

        this.rowCount = DBManager.getInstance().grantCount();
        if(rowCount%pageSize == 0){
            pageCount = rowCount/pageSize;
        }else{
            pageCount = rowCount/pageSize + 1;
        }
        if(pageNow <= 1){
            pageNow = 1;
        }
        if(pageNow >= pageCount){
            pageNow = pageCount;
        }

        //System.out.println(this.pageNow+" "+this.pageSize);

        ArrayList<GrantBean> list = DBManager.getInstance().grantShowRSO2(pageNow,pageSize);

        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面显示

        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userType = session.get("userType").toString();
        //通过session来获取用户类型跳转到相应的显示界面

        return userType;//不同用户跳转到不同界面显示
    }

    public String showSRO() throws Exception{//完成researchgrant分页并显示出来

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象

        this.rowCount = DBManager.getInstance().grantCount();
        if(rowCount%pageSize == 0){
            pageCount = rowCount/pageSize;
        }else{
            pageCount = rowCount/pageSize + 1;
        }
        if(pageNow <= 1){
            pageNow = 1;
        }
        if(pageNow >= pageCount){
            pageNow = pageCount;
        }

        //System.out.println(this.pageNow+" "+this.pageSize);

        ArrayList<GrantBean> list = DBManager.getInstance().grantShowRSO(pageNow,pageSize);//因为都是用的RG，所以函数和RSO一样

        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面显示

        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userType = session.get("userType").toString();
        //通过session来获取用户类型跳转到相应的显示界面

        return userType;
    }

    public String showSRO2() throws Exception{//完成researchgrant分页并显示出来

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象

        this.rowCount = DBManager.getInstance().grantCount();
        if(rowCount%pageSize == 0){
            pageCount = rowCount/pageSize;
        }else{
            pageCount = rowCount/pageSize + 1;
        }
        if(pageNow <= 1){
            pageNow = 1;
        }
        if(pageNow >= pageCount){
            pageNow = pageCount;
        }

        //System.out.println(this.pageNow+" "+this.pageSize);

        ArrayList<GrantBean> list = DBManager.getInstance().grantShowRSO2(pageNow,pageSize);//因为都是用的RG，所以函数和RSO一样

        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面显示

        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userType = session.get("userType").toString();
        //通过session来获取用户类型跳转到相应的显示界面

        return userType;
    }





    public String showFM1() throws Exception{//显示FacultyMember part1页面

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象

        this.rowCount = DBManager.getInstance().grantCount();
        if(rowCount%pageSize == 0){
            pageCount = rowCount/pageSize;
        }else{
            pageCount = rowCount/pageSize + 1;
        }
        if(pageNow <= 1){
            pageNow = 1;
        }
        if(pageNow >= pageCount){
            pageNow = pageCount;
        }

        //System.out.println(this.pageNow+" "+this.pageSize);

        ArrayList<GrantBean> list = DBManager.getInstance().grantShowFM1(pageNow,pageSize);

        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面

        return "FM1";

    }

    public String showFM2() throws Exception{//显示FacultyMember part2页面

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象
        ArrayList<GrantBean> list = DBManager.getInstance().grantShowFM2();
        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面
        return "FM2";

    }



    public String showDetail()throws Exception{//点击标题显示每个research grant的detail

        String grantId = ServletActionContext.getRequest().getParameter("grantId");//通过这个来获取通过链接传到action中的参数
        int grantIdOfInt = Integer.parseInt(grantId);

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象


        ArrayList<GrantBean> list = DBManager.getInstance().grantDetail(grantIdOfInt);
        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面

         //System.out.println("grantListSize: " + list.size());

        return "detail";
    }

    public String showDetail2()throws Exception{//点击标题显示每个research grant的detail

        String grantId = ServletActionContext.getRequest().getParameter("grantId");//通过这个来获取通过链接传到action中的参数
        int grantIdOfInt = Integer.parseInt(grantId);

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象


        ArrayList<GrantBean> list = DBManager.getInstance().grantDetail2(grantIdOfInt);
        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面

        //System.out.println("grantListSize: " + list.size());

        return "detail";
    }

    public String showModify()throws Exception{//修改，现需要显示每个rg的detail，所以和showDetail函数差不多

        try {

            String grantId = ServletActionContext.getRequest().getParameter("grantId");//通过这个来获取通过链接传到action中的参数
            int grantIdOfInt = Integer.parseInt(grantId);

            Map request = (Map) ActionContext.getContext().get("request");//创建request对象


            ArrayList<GrantBean> list = DBManager.getInstance().grantDetail(grantIdOfInt);
            this.setGrantList(list);//注入
            request.put("grantList", this.getGrantList());//grantlist变量用于前台界面
        }catch(Exception e){
            e.printStackTrace();
        }

        return "modify";
    }

    public String showModify2()throws Exception{//show modify after delete one deadline 删除deadline或者是file之后我们还需要回到formModify这个页面！

        String grantId = ServletActionContext.getRequest().getParameter("grantId");//通过这个来获取通过链接传到action中的参数
        int grantIdOfInt = Integer.parseInt(grantId);

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象


        ArrayList<GrantBean> list = DBManager.getInstance().grantDetail(grantIdOfInt);
        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面

        return "modify";
    }

    public String showModify3()throws Exception{//show modify after delete one file 删除file之后我们还需要回到formModify这个页面！

        String grantId = ServletActionContext.getRequest().getParameter("grantId");//通过这个来获取通过链接传到action中的参数
        int grantIdOfInt = Integer.parseInt(grantId);

        Map request=(Map) ActionContext.getContext().get("request");//创建request对象


        ArrayList<GrantBean> list = DBManager.getInstance().grantDetail(grantIdOfInt);
        this.setGrantList(list);//注入
        request.put("grantList", this.getGrantList());//grantlist变量用于前台界面

        return "modify";
    }

    public void setGrantList(ArrayList<GrantBean> grantList){this.grantList = grantList;}
    public ArrayList<GrantBean> getGrantList(){return grantList;}

    public void setPageNow(int pageNow){this.pageNow = pageNow;}
    public int getPageNow(){return this.pageNow;}

    public void setPageSize(int pageSize){this.pageSize = pageSize;}
    public int getPageSize(){return this.pageSize;}

    public void setRowCount(int rowCount){this.rowCount = rowCount;}
    public int getRowCount(){return this.rowCount;}

    public void setPageCount(int pageCount){this.pageCount = pageCount;}
    public int getPageCount(){return this.pageCount;}

}
