package Action;

import DataBase.*;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import java.util.Map;

/**
 * Created by el1ven on 17/12/14.
 */
public class DeadlineAction extends ActionSupport {

    private String grantId;
    private String deadline1;
    private String deadline2;
    private String deadline3;//为了addDeadlinesByFM服务的三个参数

    public String execute() throws Exception{

        ActionContext context= ActionContext.getContext();
        Map session = (Map)context.getSession();
        String userName = session.get("userName").toString();//获取sesssion中的用户名，可以根据这个查询用户的ID

        int userId = DBManager.getInstance().getUserIdByName(userName);
        int result = DBManager.getInstance().deadlineAddByFM(userId, this.grantId, this.deadline1, this.deadline2, this.deadline3);

        if(result < 0){
            return "fail";
        }
        return "success";
    }

    public void setGrantId(String grantId){ this.grantId = grantId; }
    public String getGrantId(){ return this.grantId; }

    public void setDeadline1(String deadline1){
        this.deadline1 = deadline1;
    }
    public String getDeadline1(){
        return this.deadline1;
    }

    public void setDeadline2(String deadline2){
        this.deadline2 = deadline2;
    }
    public String getDeadline2(){
        return this.deadline2;
    }

    public void setDeadline3(String deadline3){
        this.deadline3 = deadline3;
    }
    public String getDeadline3(){
        return this.deadline3;
    }

}
